package com.dsh.mobile.native

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.webkit.JavascriptInterface
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dsh.mobile.engine.EngineHost

/**
 * Exposed to the frontend as `window.DSHNative`.
 *
 * v1 provides the native capability bridge (clipboard, notifications, device
 * info, engine control). In P1 these become MCP servers so the real DSH engine
 * can call them through the standard MCP client plugin.
 */
class NativeBridge(
    private val context: Context,
    private val engine: EngineHost,
) {

    @JavascriptInterface
    fun getDeviceInfo(): String {
        val json = """{"os":"android","sdk":${Build.VERSION.SDK_INT},"model":"${json(Build.MODEL)}","brand":"${json(Build.MANUFACTURER)}"}"""
        return json
    }

    @JavascriptInterface
    fun getEngineStatus(): String = engine.statusJson()

    @JavascriptInterface
    fun connectRemote(url: String): String = engine.connectRemote(url)

    @JavascriptInterface
    fun startLocalEngine(): String = engine.startLocal()

    @JavascriptInterface
    fun copyText(text: String): Boolean {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(android.content.ClipData.newPlainText("dsh", text))
        return true
    }

    @JavascriptInterface
    fun readClipboard(): String {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return cm.primaryClip?.getItemAt(0)?.coerceToText(context)?.toString() ?: ""
    }

    @JavascriptInterface
    fun notify(title: String, text: String) {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return
        val n = NotificationCompat.Builder(context, "dsh")
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .build()
        try {
            manager.notify((title.hashCode() and 0x7fffffff), n)
        } catch (e: SecurityException) {
            // POST_NOTIFICATIONS not granted; ignored in v1.
        }
    }

    @JavascriptInterface
    fun vibrate(ms: Long) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (v.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= 26) v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
            else v.vibrate(ms)
        }
    }

    private fun json(s: String): String = s.replace("\\", "\\\\").replace("\"", "\\\"")
}
