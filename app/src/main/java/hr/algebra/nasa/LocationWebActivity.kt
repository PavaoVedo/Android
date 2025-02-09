package hr.algebra.nasa

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.nasa.databinding.FragmentLocationWebviewBinding

class LocationWebActivity : AppCompatActivity() {

    private lateinit var binding: FragmentLocationWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentLocationWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val location = intent.getStringExtra(EXTRA_LOCATION) ?: "Earth"

        binding.webViewMap.webViewClient = WebViewClient()

        val webSettings: WebSettings = binding.webViewMap.settings
        webSettings.javaScriptEnabled = true

        val mapsUrl = "https://www.google.com/maps/search/?api=1&query=$location"

        binding.webViewMap.loadUrl(mapsUrl)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        const val EXTRA_LOCATION = "hr.algebra.nasa.extra.LOCATION"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}