package com.ledgerly.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.utils.CSVExporter
import com.ledgerly.viewmodel.TransactionViewModel

@Composable
fun ExportScreen(
    transactionViewModel: TransactionViewModel
) {
    val context = LocalContext.current

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

        Text(
            text = "Export",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Export Transactions"
                )

                Text(
                    text =
                        "Generate a CSV file containing all transactions."
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        val csv =
                            CSVExporter.exportTransactions(
                                transactionViewModel.transactions
                            )

                        val shareIntent =
                            Intent(
                                Intent.ACTION_SEND
                            ).apply {

                                type = "text/csv"

                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    csv
                                )
                            }

                        context.startActivity(
                            Intent.createChooser(
                                shareIntent,
                                "Export CSV"
                            )
                        )
                    }
                ) {
                    Text("Export CSV")
                }
            }
        }
    }
}