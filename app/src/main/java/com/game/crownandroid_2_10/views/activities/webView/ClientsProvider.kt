package com.game.crownandroid_2_10.views.activities.webView

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.JsResult
import android.webkit.SafeBrowsingResponse
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher
import com.game.crownandroid_2_10.views.activities.WebActivity

object ClientsProvider {

    fun webViewClient(startActivityCallback: (Intent) -> Boolean): WebViewClient {
        return object : WebViewClient() {
            private var safeBrowsingHitTimes = 0
            private var lastContent = true
            private var receivedLoginAccount  = ""

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url.toString()
                lastContent = true
                return if (uri.contains("/")) {
                    Log.i("Uri", uri)
                    if (uri.contains("http")) {
                        lastContent = false
                        false
                    } else {
                        startActivityCallback(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                    }
                } else true
            }

            override fun onReceivedLoginRequest(
                view: WebView,
                realm: String,
                account: String?,
                args: String
            ) {
                receivedLoginAccount = account.toString()
                super.onReceivedLoginRequest(view, realm, account, args)
            }

            override fun onSafeBrowsingHit(
                view: WebView?,
                request: WebResourceRequest?,
                threatType: Int,
                callback: SafeBrowsingResponse?
            ) {
                safeBrowsingHitTimes++
                when(safeBrowsingHitTimes) {
                    100 -> Log.i("Web view client", "100 safe browsing hits!")
                    500 -> Log.i("Web view client", "500 safe browsing hits!")
                    1000 -> Log.i("Web view client", "1000 safe browsing hits!")
                }
                super.onSafeBrowsingHit(view, request, threatType, callback)
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                var urlOk = false
                if(url != null) {
                    if (url.contains("http")) {
                        if (url.contains("/")) {
                            urlOk = true
                        }
                    }
                    else if (url.contains("file:")) {
                        urlOk = true
                    }
                }

                Log.i("Web view client", "Url is ${if(urlOk) "OK" else "bad"}.\n" +
                        "Is reload? $isReload")

                super.doUpdateVisitedHistory(view, url, isReload)
            }
        }
    }
    /**
     * @param callback Sets [ValueCallback] inside [WebActivity] and invokes
     * [ActivityResultLauncher]. Should returns number bigger than 0 (for example, 1).
     */
    fun webChromeClient(
        callback: (ValueCallback<Array<Uri>>) -> Int
    ): WebChromeClient {
        return object : WebChromeClient() {

            private var someProgress: Int = 0
                set(value) {
                    when(value) {
                        100 -> Log.i("Web chrome client", "Progress is 100.")
                        50 -> Log.i("Web chrome client", "Progress is 50.")
                        0 -> Log.i("Web chrome client", "Progress is 0.")
                    }
                    field = value
                }
            private var geoPromptIsShow: Pair<Boolean, GeolocationPermissions.Callback?>
                = false to null

            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                return callback(filePathCallback) > 0
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                someProgress = newProgress
                super.onProgressChanged(view, newProgress)
            }

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                geoPromptIsShow = true to callback
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }

            override fun onGeolocationPermissionsHidePrompt() {
                geoPromptIsShow = false to null
                super.onGeolocationPermissionsHidePrompt()
            }

            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                Log.i("Js alert", "View is ${if(view == null) "" else "not "}null. " +
                        "Url: $url. Message: $message. Result: $result.")
                return super.onJsAlert(view, url, message, result)
            }
        }
    }
}