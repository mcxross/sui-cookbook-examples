/*
 * Copyright 2025 McXross
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.mcxross.example.suiwallet.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import xyz.mcxross.example.suiwallet.model.Token
import xyz.mcxross.example.suiwallet.ui.components.ActionButton
import xyz.mcxross.example.suiwallet.ui.components.BalanceCard
import xyz.mcxross.example.suiwallet.ui.components.Header
import xyz.mcxross.example.suiwallet.ui.components.TokenItem
import xyz.mcxross.example.suiwallet.ui.components.icons.ReceiveIcon
import xyz.mcxross.example.suiwallet.ui.components.icons.SendIcon
import xyz.mcxross.ksui.Sui
import xyz.mcxross.ksui.account.Account

@Composable
fun HomeScreen(
    sui: Sui,
    account: Account,
    paddingValues: PaddingValues,
    gradientBackground: Brush,
    onSendClick: () -> Unit,
    onReceiveClick: () -> Unit
) {
    val tokens = listOf(
        Token("ETH", "1.234", "$2,468.00", "+2.3%"),
        Token("BTC", "0.0456", "$1,824.00", "-1.2%"),
        Token("USDC", "350.00", "$350.00", "0%")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackground)
            .padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Header()
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                BalanceCard(sui, account)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                ActionButtons(
                    sui = sui,
                    account = account,
                    onSendClick = onSendClick,
                    onReceiveClick = onReceiveClick
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Assets",
                    style = MaterialTheme.typography.h6,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(tokens) { token ->
                TokenItem(token)
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun ActionButtons(
    sui: Sui,
    account: Account,
    onSendClick: () -> Unit,
    onReceiveClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionButton(
                text = "Send",
                icon = SendIcon,
                gradient = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6))
                ),
                modifier = Modifier.weight(1f),
                onClick = onSendClick
            )

            ActionButton(
                text = "Receive",
                icon = ReceiveIcon,
                gradient = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6))
                ),
                modifier = Modifier.weight(1f),
                onClick = onReceiveClick
            )
        }

        ActionButton(
            text = "Request Test Tokens",
            icon = Icons.Default.Add,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6))
            ),
            onClick = {
                coroutineScope.launch {
                    try {
                        sui.requestTestTokens(account.address)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        )
    }
}