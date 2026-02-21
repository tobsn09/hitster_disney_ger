package com.tobsn.myhitsterplayer

import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
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

        // Statusleiste ausblenden
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.hide(WindowInsets.Type.statusBars())

        myWebView = findViewById(R.id.webview)
        setupWebView()

        // Scanner-Schnittstelle registrieren
        myWebView.addJavascriptInterface(WebAppInterface(), "AndroidBridge")

        val intentUri = intent.data
        myWebView.loadUrl(intentUri?.toString() ?: "https://tobsn09.github.io/hitster_disney_ger/")
    }

    private fun setupWebView() {
        val settings = myWebView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false
        myWebView.webViewClient = WebViewClient()
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun startScanner() {
            val options = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = GmsBarcodeScanning.getClient(this@MainActivity, options)
            scanner.startScan().addOnSuccessListener { barcode ->
                barcode.rawValue?.let { if (it.contains("github.io")) runOnUiThread { myWebView.loadUrl(it) } }
            }
        }
    }
}