package com.dsh.mobile.native

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.webkit.JavascriptInterface
import com.dsh.mobile.engine.EngineHost

/**
 * Exposed to the frontend as `window.DSHNative`.
 *
 * Provides the native capability bridge (clipboard, notifications, device
 * info) and host connection control for the connect screen. In P1 these native
 * capabilities are also exposed as MCP servers for the on-device DSH engine.
 */
class NativeBridge(
    private val context: Context,
    private val onConnect: (String) -> Unit,
) {

    @JavascriptInterface
    fun getDeviceInfo(): String {
        return """{"os":"android","sdk":${Build.VERSION.SDK_INT},"model":"${json(Build.MODEL)}","brand":"${json(Build.MANUFACTURER)}"}"""
    }

    @JavascriptInterface
    fun connect(host: String) {
        onConnect(host)
    }

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

    private fun json(s: String): String = s.replace("\\", "\\\\").replace("\"", "\\\"")
}
