package com.dsh.mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.dsh.mobile.engine.EngineHost
import com.dsh.mobile.native.NativeBridge

/**
 * DSH Android shell.
 *
 * Loads the DSH-style frontend from assets into a WebView and exposes the
 * native bridge (window.DSHNative) plus the engine host lifecycle.
 *
 * v1 scope: UI 1:1-styled replica + engine-host architecture + native bridge.
 * The complete on-device @deepseek-ai/dsh engine bundle is staged in P1.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var engine: EngineHost
    private lateinit var nativeBridge: NativeBridge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)

        engine = EngineHost(applicationContext)
        nativeBridge = NativeBridge(this, engine)

        configureWebView()
        webView.addJavascriptInterface(nativeBridge, "DSHNative")
        webView.loadUrl("file:///android_asset/web/index.html")
    }

    private fun configureWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.setSupportZoom(false)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    return true
                }
                return false
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    override fun onDestroy() {
        engine.stop()
        webView.destroy()
        super.onDestroy()
    }
}
