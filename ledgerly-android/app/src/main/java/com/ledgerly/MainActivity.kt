package com.ledgerly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ledgerly.ui.navigation.LedgerlyNavigation
import com.ledgerly.ui.theme.LedgerlyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LedgerlyTheme {
                LedgerlyNavigation()
            }
        }
    }
}