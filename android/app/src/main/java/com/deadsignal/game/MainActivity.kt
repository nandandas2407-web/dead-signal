package com.deadsignal.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.presage.physiology.proto.MetricsProto.MetricsBuffer
import com.presagetech.smartspectra.SmartSpectraMode
import com.presagetech.smartspectra.SmartSpectraSdk
import com.presagetech.smartspectra.SmartSpectraView

class MainActivity : AppCompatActivity() {
    private lateinit var web: WebView
    private lateinit var ssView: SmartSpectraView
    private val blinks = ArrayDeque<Long>()
    private var lastBlinkT = -1f

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_main)
        ssView = findViewById(R.id.smart_spectra_view)
        web = findViewById(R.id.web)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.mediaPlaybackRequiresUserGesture = false
        web.webViewClient = object : WebViewClient() {
            override fun onPageFinished(v: WebView, u: String) {
                v.evaluateJavascript("document.getElementById('bCam').style.display='none'", null)
            }
        }
        web.loadUrl("file:///android_asset/index.html")
        val key = getSharedPreferences("ds", MODE_PRIVATE).getString("key", null)
        if (key.isNullOrBlank()) askKey() else initSdk(key)
    }

    private fun askKey() {
        val e = EditText(this)
        AlertDialog.Builder(this).setTitle("Presage API key").setView(e).setCancelable(false)
            .setPositiveButton("Save") { _, _ ->
                val k = e.text.toString().trim()
                getSharedPreferences("ds", MODE_PRIVATE).edit().putString("key", k).apply()
                initSdk(k)
            }.show()
    }

    private fun initSdk(key: String) {
        SmartSpectraSdk.getInstance().apply {
            setApiKey(key)
            setSmartSpectraMode(SmartSpectraMode.CONTINUOUS)
            setMetricsBufferObserver { m -> onMetrics(m) }
        }
    }

    private fun onMetrics(m: MetricsBuffer) {
        val p = m.pulse.rateList.lastOrNull() ?: return
        if (!p.value.isFinite()) return
        val br = m.breathing.rateList.lastOrNull()?.value
        val bl = m.face.blinkingList.lastOrNull()
        val now = SystemClock.elapsedRealtime()
        if (bl != null && bl.detected && bl.time != lastBlinkT) { lastBlinkT = bl.time; blinks.addLast(now) }
        while (blinks.isNotEmpty() && now - blinks.first() > 1500) blinks.removeFirst()
        val shock = if (blinks.size >= 2) 1 else 0
        var c = p.confidence
        if (c > 1.5f) c /= 100f
        val brJs = if (br != null && br.isFinite()) "$br" else "null"
        val js = "window.pushSignal({bpm:${p.value},breathingRate:$brJs,expressionShock:$shock,confidence:$c})"
        runOnUiThread { web.evaluateJavascript(js, null) }
    }
}
