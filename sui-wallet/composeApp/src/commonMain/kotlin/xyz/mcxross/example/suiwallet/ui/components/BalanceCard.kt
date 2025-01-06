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
package xyz.mcxross.example.suiwallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import xyz.mcxross.example.suiwallet.ui.components.icons.CopyIcon
import xyz.mcxross.example.suiwallet.util.fetchBalance
import xyz.mcxross.example.suiwallet.util.formatAddress
import xyz.mcxross.ksui.Sui
import xyz.mcxross.ksui.account.Account

@Composable
fun BalanceCard(sui: Sui, account: Account) {

    var userBalance by remember { mutableStateOf(0L) }
    val updateTrigger = remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = updateTrigger.intValue) {
        userBalance = fetchBalance(sui, account)
    }

    BalanceRefresher(updateTrigger)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0x409333EA),
                            Color(0x403B82F6)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xCC1A1A1A))
                .padding(24.dp)
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.subtitle1,
                color = Color.Gray
            )

            Text(
                text = "$userBalance SUI",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x80333333), RoundedCornerShape(14.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Address: ",
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )

                Text(
                    text = formatAddress(account.address.toString()),
                    style = MaterialTheme.typography.caption,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {},
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = CopyIcon,
                        contentDescription = "Copy",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}