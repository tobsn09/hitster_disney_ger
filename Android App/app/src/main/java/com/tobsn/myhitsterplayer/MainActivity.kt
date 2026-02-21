package com.tobsn.myhitsterplayer

import android.os.Bundle
import android.view.WindowInsets
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : AppCompatActivity() {
    private lateinit var myWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fullscreen
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.hide(WindowInsets.Type.statusBars())

        myWebView = findViewById(R.id.webview)
        setupWebView()

        myWebView.addJavascriptInterface(WebAppInterface(), "AndroidBridge")
        myWebView.loadUrl("https://tobsn09.github.io/hitster_disney_ger/")
    }

    private fun setupWebView() {
        val settings = myWebView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false

        // S24 Ultra Chrome-Tarnung für stabilere Logins
        settings.userAgentString = "Mozilla/5.0 (Linux; Android 14; SM-S928B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36"

        myWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Sobald eine Seite fertig geladen ist (auch nach dem Redirect)
                // triggern wir eine JS-Funktion zur Sicherheit
                view?.evaluateJavascript("checkForToken();", null)
            }
        }
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun startScanner() {
            val options = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
            GmsBarcodeScanning.getClient(this@MainActivity, options).startScan()
                .addOnSuccessListener { barcode ->
                    barcode.rawValue?.let { if (it.contains("github.io")) runOnUiThread { myWebView.loadUrl(it) } }
                }
        }
    }
}