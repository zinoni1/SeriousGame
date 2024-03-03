package com.zenonrodrigo.seriousgame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.zenonrodrigo.seriousgame.room.roomDao
import com.zenonrodrigo.seriousgame.room.roomTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Nivell3 : AppCompatActivity() {
    var webView: WebView? = null
    private lateinit var taskDao: roomDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nivell3)
        webView = findViewById(R.id.webview)

        setDesktopMode(webView, true)
           val db = (applicationContext as App).db
        taskDao = db.taskDao()
        load()
    }

    fun load() {
        val settings = webView!!.settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.domStorageEnabled = true
        webView!!.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView!!.isScrollbarFadingEnabled = true
        webView!!.webChromeClient = object : WebChromeClient() {}
        webView!!.webViewClient = Callback()
        webView!!.addJavascriptInterface(WebAppInterface(this, taskDao), "AndroidFunction")
        webView!!.loadUrl("file:///android_asset/index.html")
    }
    class WebAppInterface(private val mContext: Context, private val taskDao: roomDao) {

        @JavascriptInterface
        fun redirectToMainActivity() {
            val intent = Intent(mContext, MainActivity::class.java)
            mContext.startActivity(intent)
        }

        @JavascriptInterface
        fun insertTask() {
            CoroutineScope(Dispatchers.IO).launch {
                val task = roomTask(nivell = 3, punts = 1, completat = true)
                taskDao.insertLvl(task)
                taskDao.updateLvl(task)
            }
        }
    }
    private inner class Callback : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return false
        }
    }

    fun setDesktopMode(webView: WebView?, enabled: Boolean) {
        var newUserAgent = webView!!.settings.userAgentString
        if (enabled) {
            try {
                val ua = webView.settings.userAgentString
                val androidOSString =
                    webView.settings.userAgentString.substring(ua.indexOf("("), ua.indexOf(")") + 1)
                newUserAgent =
                    webView.settings.userAgentString.replace(androidOSString, "(X11; Linux x86_64)")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            newUserAgent = null
        }
        webView.settings.setUserAgentString(newUserAgent)
        webView.settings.useWideViewPort = enabled
        webView.settings.loadWithOverviewMode = enabled
        webView.reload()
    }

    override fun onBackPressed() {
        if (webView != null && webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            super.onBackPressed()
        }
    }

}
