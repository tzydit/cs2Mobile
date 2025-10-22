package br.com.uri.cs2mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import br.com.uri.cs2mobile.ui.skins.SkinsScreen
// ✅ Note the lowercase 'm' in the import
import br.com.uri.cs2mobile.ui.theme.Cs2mobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ✅ And here as well. This is the fix.
            Cs2mobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SkinsScreen()
                }
            }
        }
    }
}