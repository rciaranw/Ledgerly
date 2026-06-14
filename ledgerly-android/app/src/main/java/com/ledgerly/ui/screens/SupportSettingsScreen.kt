package com.ledgerly.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SupportSettingsScreen(
    onBack: () -> Unit
) {
    val context =
        LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector =
                        Icons.Filled.ArrowBack,
                    contentDescription =
                        "Back"
                )
            }

            Text(
                text = "Support",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Text(
            text =
                "Need help with Ledgerly? Choose one of the options below.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        SupportOptionCard(
            title = "Contact Support",
            description =
                "Send an email to the Ledgerly support team",
            onClick = {
                val intent =
                    Intent(
                        Intent.ACTION_SENDTO
                    ).apply {
                        data =
                            Uri.parse(
                                "mailto:support@ledgerly.app"
                            )

                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            "Ledgerly Support"
                        )
                    }

                context.startActivity(
                    intent
                )
            }
        )

        SupportOptionCard(
            title = "Report a Problem",
            description =
                "Tell us about a bug or something that is not working",
            onClick = {
                val intent =
                    Intent(
                        Intent.ACTION_SENDTO
                    ).apply {
                        data =
                            Uri.parse(
                                "mailto:support@ledgerly.app"
                            )

                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            "Ledgerly Problem Report"
                        )

                        putExtra(
                            Intent.EXTRA_TEXT,
                            """
                            Please describe the problem:

                            What were you doing when it happened?

                            What did you expect to happen?

                            What actually happened?
                            """.trimIndent()
                        )
                    }

                context.startActivity(
                    intent
                )
            }
        )

        SupportOptionCard(
            title = "Privacy Information",
            description =
                "Learn how Ledgerly handles your information",
            onClick = {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://ledgerly.app/privacy"
                        )
                    )

                context.startActivity(
                    intent
                )
            }
        )

        SupportOptionCard(
            title = "Terms of Use",
            description =
                "Read the terms governing use of Ledgerly",
            onClick = {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://ledgerly.app/terms"
                        )
                    )

                context.startActivity(
                    intent
                )
            }
        )
    }
}

@Composable
private fun SupportOptionCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column(
                modifier =
                    Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text = description,
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector =
                    Icons.Filled.ChevronRight,
                contentDescription =
                    "Open $title"
            )
        }
    }
}