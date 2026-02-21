package com.tobsn.myhitsterplayer

import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- STATUSLEISTE AUSBLENDEN (Full Screen) ---
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        // --- WEBVIEW KONFIGURIEREN ---
        val myWebView: WebView = findViewById(R.id.webview)
        val webSettings = myWebView.settings

        webSettings.javaScriptEnabled = true // Für Spotify Logik nötig
        webSettings.domStorageEnabled = true // Für Token-Speicherung nötig

        myWebView.webViewClient = WebViewClient()

        // Deine GitHub-URL laden
        // Prüfen, ob die App über einen Link (QR-Code) geöffnet wurde
        val intentUri = intent.data
        if (intentUri != null) {
            // Wenn ein Link geklickt wurde, lade diesen
            myWebView.loadUrl(intentUri.toString())
        } else {
            // Standard-Startseite
            myWebView.loadUrl("https://tobsn09.github.io/hitster_disney_ger/")
        }
    }
}