package com.game.crownandroid_2_10.views.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebSettings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.game.crownandroid_2_10.R
import com.game.crownandroid_2_10.extentions.validResultCode
import com.game.crownandroid_2_10.util.FirebaseHelper
import com.game.crownandroid_2_10.views.activities.webView.ClientsProvider
import com.game.crownandroid_2_10.views.activities.webView.WebViewContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException


class WebActivity : AppCompatActivity() {
    private val webViewContainer = WebViewContainer()
    private var primaryCallback: ValueCallback<Array<Uri>>? = null
    private var secondaryCallback: Uri? = null
    private var beginningUrl: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        webViewContainer.webView = findViewById(R.id.webView)
        beginningUrl = FirebaseHelper.savedUrl
        setWebViewProperties()
        setCookies(CookieManager.getInstance())
        webViewContainer.loadUrl(beginningUrl)
    }

    private val futureActions = listOf(
        { webViewContainer.settings.allowContentAccess = true },
        { webViewContainer.settings.allowFileAccess = true },
        { webViewContainer.settings.javaScriptCanOpenWindowsAutomatically = true },
        { webViewContainer.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW },
        { webViewContainer.settings.cacheMode = WebSettings.LOAD_DEFAULT }
    )

    private fun setWebViewProperties() {
        for(action in futureActions) {
            action()
        }
        webViewContainer.settings.run {
            validateSettings(
                ::getAllowContentAccess,
                ::getAllowFileAccess,
                ::getJavaScriptCanOpenWindowsAutomatically,
            )
        }
        setSomeBooleans()
        webViewContainer.makeUserAgentStringRight()
        webViewContainer.setProhibitedProperties(true)
        webViewContainer.webChromeClient = ClientsProvider
            .webChromeClient {
                firstStringLauncher.launch(Manifest.permission.CAMERA)
                primaryCallback = it
                1
            }
        webViewContainer.webViewClient = ClientsProvider.webViewClient {
            startActivity(it)
            true
        }
    }

    private fun setSomeBooleans() {
        webViewContainer.settings.domStorageEnabled = true
        webViewContainer.settings.databaseEnabled = true
        webViewContainer.settings.run {
            validateSettings(
                ::getDomStorageEnabled,
                ::getDatabaseEnabled
            )
        }
        webViewContainer.settings.useWideViewPort = true
        webViewContainer.settings.loadWithOverviewMode = true
    }

    private fun validateSettings(vararg getters: () -> Boolean): Boolean {
        for(getter in getters) {
            if(!getter()) {
                return false
            }
        }
        return true
    }

    private fun setCookies(cookieManager: CookieManager?) {
        if(cookieManager != null) {
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(webViewContainer.webView, true)
        }
        else {
            throw IllegalArgumentException("Cookie manager can't be null!!!")
        }
    }

    private val firstStringLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        lifecycleScope.launch (Dispatchers.IO) {
            val photoFile = try {
                File.createTempFile("paint_file", ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            } catch (ex: IOException) {
                Log.e("Paint file", null, ex)
                null
            }
            val provider = FirstStringLauncherIntentProvider(Uri.fromFile(photoFile))
            secondIntentLauncher.launch(provider.chooser)
        }
    }

    @Suppress("SameParameterValue")
    inner class FirstStringLauncherIntentProvider(
        private val uri: Uri?
    ) {
        private val old: Intent
            get() {
                val old = Intent(Intent.ACTION_GET_CONTENT)
                setCategory(old, Intent.CATEGORY_OPENABLE)
                old.type = "*/*"
                return old
            }

        private val takePaintFile: Intent
            get() {
                val takePaintFile =  Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                putUri(takePaintFile, MediaStore.EXTRA_OUTPUT, uri)
                return takePaintFile
            }

        val chooser: Intent
            get() {
                val chooser = Intent(Intent.ACTION_CHOOSER)
                putExtra(chooser, Intent.EXTRA_INTENT, old)
                putExtra(chooser, Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePaintFile))
                return chooser
            }

        private fun setCategory(intent: Intent, category: String) {
            if(category.isNotBlank()) {
                intent.addCategory(category)
            }
            else {
                throw IllegalArgumentException("Why your category is blank?")
            }
        }

        private fun<T> putExtra(intent: Intent, extra: String, value: T) where T: Parcelable {
            intent.putExtra(extra, value)
        }

        private fun putExtra(intent: Intent, extra: String, value: Array<Parcelable>) {
            intent.putExtra(extra, value)
        }

        private fun putUri(intent: Intent, extra: String, uri: Uri?) {
            intent.putExtra(extra, uri)
        }
    }

    private val secondIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val primaryCallback = this.primaryCallback ?: return@registerForActivityResult
        val whatReceive = if (it.validResultCode()) {
            val d = it.data?.dataString
            if (d != null) {
                val u = Uri.parse(d)
                arrayOf(u)
            } else {
                if (secondaryCallback != null) {
                    arrayOf(secondaryCallback)
                } else {
                    null
                }
            }
        } else {
            null
        }
        if(whatReceive == null) {
            Log.w("Web activity", "Worked the first else branch or the last else" +
                    "branch.\nResult code: ${it.resultCode}.")
        }
        else {
            Log.i("Web activity", "Worked the first true branch or the last true branch.")
        }
        primaryCallback.onReceiveValue(whatReceive)
        this.primaryCallback = null
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webViewContainer.saveState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return webViewContainer.goBackIfCan()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webViewContainer.restoreState(savedInstanceState)
    }
}