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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import xyz.mcxross.example.suiwallet.model.SendState
import xyz.mcxross.example.suiwallet.util.send
import xyz.mcxross.ksui.Sui
import xyz.mcxross.ksui.account.Account
import xyz.mcxross.ksui.model.AccountAddress

@Composable
fun SendBottomSheet(
    sui: Sui, account: Account, onClose: () -> Unit
) {
    var amountInput by remember { mutableStateOf(0UL) }
    var sending by remember { mutableStateOf(false) }
    var sendState by remember { mutableStateOf(SendState.INPUT) }
    val coroutineScope = rememberCoroutineScope()
    var recipientAddress by remember { mutableStateOf("") }
    var txnDigest by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth().padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Send", style = MaterialTheme.typography.h6, color = Color.White
            )
            IconButton(onClick = {
                sendState = SendState.INPUT
                recipientAddress = ""
                onClose()
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (sendState) {
            SendState.INPUT -> {

                if (sending) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = MaterialTheme.colors.primary,
                            strokeWidth = 1.dp
                        )
                    }
                    return
                }

                OutlinedTextField(
                    value = recipientAddress,
                    onValueChange = {
                        recipientAddress = it
                    },
                    label = { Text("Recipient Address", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color(0xFF8B5CF6),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = amountInput.toString(),
                    onValueChange = {
                        val parsedAmount = it.toULongOrNull()
                        if (parsedAmount != null) {
                            amountInput = parsedAmount
                        }
                    },
                    label = { Text("Amount", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color(0xFF8B5CF6),
                        unfocusedBorderColor = Color.Gray
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val recipient = AccountAddress.fromString(recipientAddress)
                        coroutineScope.launch {
                            sending = true
                            val sent = send(
                                sui, sender = account, recipient = recipient, amount = amountInput
                            )
                            sendState = if (sent.first) {
                                txnDigest = sent.second
                                SendState.SUCCESS
                            } else SendState.FAILED
                            sending = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = recipientAddress.isNotEmpty() && amountInput.toInt() != 0,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF8B5CF6)
                    )
                ) {
                    Text("Confirm Send", color = Color.White)
                }
            }

            SendState.SUCCESS -> {
                TransactionStatus(
                    success = true,
                    message = "Transaction Successful!",
                    explorerUrl = "https://devnet.suivision.xyz/txblock/${txnDigest}"
                )
            }

            SendState.FAILED -> {
                TransactionStatus(
                    success = false,
                    message = "Transaction Failed",
                    explorerUrl = "https://devnet.suivision.xyz/txblock/${txnDigest}"
                )
            }
        }
    }
}