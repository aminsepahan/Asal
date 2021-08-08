package com.aradpardaz.asal

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var myWebView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myWebView = findViewById(R.id.webView)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled =true


//        myWebView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36 AsalApp"
        myWebView.loadUrl("https://www.asal.ir/")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    return processUrl(request.url.toString(), view)
                }
            }
        } else {
            myWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return processUrl(url!!, view!!)
                }
            }
        }
        myWebView.webChromeClient = WebChromeClient()
    }

    private fun processUrl(url: String, view: WebView) : Boolean{
        return if (url.startsWith("tel")) {
            openPhoneIntent(url)
            true
        } else {
            view.loadUrl(url)
            false
        }
    }

    private fun openPhoneIntent(telUrl: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(telUrl)
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {
            myWebView.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }
}