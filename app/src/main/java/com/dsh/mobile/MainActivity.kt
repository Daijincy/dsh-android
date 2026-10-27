package com.dsh.mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.dsh.mobile.native.NativeBridge

/**
 * DSH Android — real DSH client.
 *
 * v0.2.0: loads the REAL DeepSeek Harness frontend from a running
 * `dsh --profile web` host (genuine original UI + real engine functionality).
 * On first launch a connect screen lets the user enter their host URL (with
 * its ?token=), which is persisted and then loaded in the WebView.
 *
 * The fully-independent on-device engine (bundling @deepseek-ai/dsh + a
 * Node-for-Android runtime, running `dsh --profile web` locally) is staged in
 * P1; this version already delivers the real, functional DSH experience.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var nativeBridge: NativeBridge
    private val prefs by lazy { getSharedPreferences("dsh", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)

        configureWebView()
        nativeBridge = NativeBridge(this) { host ->
            prefs.edit().putString("host", host).apply()
            webView.loadUrl(host)
        }
        webView.addJavascriptInterface(nativeBridge, "DSHNative")

        val saved = prefs.getString("host", null)
        if (saved != null) webView.loadUrl(saved) else webView.loadUrl("file:///android_asset/web/boot.html")
    }

    private fun configureWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        // A real DSH host may be served over plain http on a LAN address.
        webView.settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.setSupportZoom(false)
        webView.settings.userAgentString = webView.settings.userAgentString + " DSH-Android/0.2"
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                if (url.startsWith("file://") || url.startsWith("http://") || url.startsWith("https://")) {
                    // In-app navigation: keep DSH within the WebView. External links only when explicit.
                    return false
                }
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                return true
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
