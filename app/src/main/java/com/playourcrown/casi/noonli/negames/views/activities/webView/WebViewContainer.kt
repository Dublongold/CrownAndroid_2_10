package com.playourcrown.casi.noonli.negames.views.activities.webView

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewContainer {
    private var privateWebView: WebView? = null
    var webView
        get() = privateWebView ?: throw IllegalStateException("Web view wasn't set.")
        set(value) {
            privateWebView = value
        }

    val settings
        get() = webView.settings

    var webViewClient: WebViewClient?
        get() = null
        set(value) {
            if(value != null) {
                privateWebView?.webViewClient = value
            }
        }

    var webChromeClient: WebChromeClient?
        get() = null
        set(value) {
            if(value != null) {
                privateWebView?.webChromeClient = value
            }
        }


    @Suppress("DEPRECATION")
    fun setProhibitedProperties(value: Boolean) {
        settings.javaScriptEnabled = value
        settings.allowFileAccessFromFileURLs = value
        settings.allowUniversalAccessFromFileURLs = value
    }

    fun makeUserAgentStringRight() {
        val userAgentString = privateWebView?.settings?.userAgentString
        if (userAgentString != null && userAgentString.contains("; wv")) {
            settings.userAgentString = settings.userAgentString.replace("; wv", "")
        }
        else {
            Log.w(
                CLASS_LOG_TAG,
                "Useless calling of the method \"makeUserAgentStringRight\"."
            )
        }
    }

    fun goBackIfCan(): Boolean {
        return if(privateWebView?.canGoBack() == true) {
            privateWebView?.goBack()
            true
        }
        else false
    }

    fun loadUrl(url: String) {
        privateWebView?.loadUrl(url)
    }

    fun saveState(outState: Bundle) {
        privateWebView?.saveState(outState)
    }

    fun restoreState(savedInstanceState: Bundle) {
        privateWebView?.restoreState(savedInstanceState)
    }

    companion object {
        private const val CLASS_LOG_TAG = "Web view container"
    }
}