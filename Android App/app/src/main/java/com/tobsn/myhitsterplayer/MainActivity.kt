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

        window.setDecorFitsSystemWindows(false)
        window.insetsController?.hide(WindowInsets.Type.statusBars())

        myWebView = findViewById(R.id.webview)
        val settings = myWebView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false

        // S24 Ultra Tarnung für stabilen Redirect
        settings.userAgentString = "Mozilla/5.0 (Linux; Android 14; SM-S928B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36"

        myWebView.addJavascriptInterface(WebAppInterface(), "AndroidBridge")
        myWebView.webViewClient = WebViewClient()
        myWebView.loadUrl("https://tobsn09.github.io/hitster_disney_ger/")
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