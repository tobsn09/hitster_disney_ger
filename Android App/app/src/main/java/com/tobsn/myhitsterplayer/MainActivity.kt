package com.tobsn.myhitsterplayer

import android.os.Bundle
import android.view.WindowInsets
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var myWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setDecorFitsSystemWindows(false)
        window.insetsController?.hide(WindowInsets.Type.statusBars())

        myWebView = findViewById(R.id.webview)
        setupWebView()
        myWebView.loadUrl("https://tobsn09.github.io/hitster_disney_ger/")
    }

    private fun setupWebView() {
        val settings = myWebView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.userAgentString = "Mozilla/5.0 (Linux; Android 14; SM-S928B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36"

        myWebView.webViewClient = object : WebViewClient() {
            // Dies triggert bei JEDEM Laden, auch nach dem Spotify-Redirect
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != null && url.contains("#access_token")) {
                    // Wir schubsen das JavaScript manuell an
                    view?.evaluateJavascript("checkForToken();", null)
                }
            }
        }
    }
}