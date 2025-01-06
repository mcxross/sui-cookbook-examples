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

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import xyz.mcxross.example.suiwallet.ui.components.icons.ImportIcon
import xyz.mcxross.example.suiwallet.ui.components.icons.LockIcon
import xyz.mcxross.example.suiwallet.ui.components.icons.AddIcon
import xyz.mcxross.example.suiwallet.ui.components.icons.UnlockIcon
import xyz.mcxross.ksui.account.Account
import xyz.mcxross.ksui.account.Ed25519Account

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WalletCreationScreen(
    onWalletCreated: (Ed25519Account) -> Unit,
    onWalletImported: (Ed25519Account) -> Unit
) {
    var showImport by remember { mutableStateOf(false) }
    var showAccountCreated by remember { mutableStateOf(false) }
    var newAccount: Ed25519Account? by remember { mutableStateOf(null) }
    val account = Account

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6B46C1),
            Color(0xFF1E3A8A),
            Color.Black
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackground)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!showAccountCreated) {
                Text(
                    text = "Welcome to Sui Wallet",
                    style = MaterialTheme.typography.h4,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Create or import your wallet to get started",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))
            }

            // Main content
            AnimatedContent(
                targetState = when {
                    showAccountCreated -> "created"
                    showImport -> "import"
                    else -> "options"
                },
                transitionSpec = {
                    fadeIn() with fadeOut()
                }
            ) { state ->
                when (state) {
                    "created" -> {
                        AccountCreatedContent(
                            account = newAccount!!,
                            onDone = {
                                onWalletCreated(newAccount!!)
                            }
                        )
                    }

                    "import" -> {
                        ImportWalletContent(
                            onBack = { showImport = false },
                            onImport = { importedAccount ->
                                onWalletImported(importedAccount)
                            }
                        )
                    }

                    else -> {
                        WalletCreationOptions(
                            onCreateNew = {
                                newAccount = account.create() as Ed25519Account
                                showAccountCreated = true
                            },
                            onImport = { showImport = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountCreatedContent(
    account: Ed25519Account,
    onDone: () -> Unit
) {
    var showMnemonic by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF10B981),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Wallet Created!",
            style = MaterialTheme.typography.h5,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Below is your recovery phrase. Save it somewhere safe.",
            style = MaterialTheme.typography.subtitle1,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            backgroundColor = Color(0xFF1F2937),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (showMnemonic) {
                    Text(
                        text = account.mnemonic,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "Tap to reveal recovery phrase",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                TextButton(
                    onClick = { showMnemonic = !showMnemonic },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (showMnemonic) LockIcon else UnlockIcon,
                            contentDescription = if (showMnemonic) "Hide" else "Show",
                            tint = Color(0xFF8B5CF6)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (showMnemonic) "Hide" else "Show",
                            color = Color(0xFF8B5CF6)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "⚠️ Never share your recovery phrase with anyone",
            style = MaterialTheme.typography.caption,
            color = Color(0xFFFFB020),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF8B5CF6)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "I've Saved My Recovery Phrase",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ImportWalletContent(
    onBack: () -> Unit,
    onImport: (Ed25519Account) -> Unit
) {
    var recoveryPhrase by remember { mutableStateOf("") }
    val account = Account

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Text(
            text = "Import Wallet",
            style = MaterialTheme.typography.h5,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Enter your 12-word recovery phrase or private key",
            style = MaterialTheme.typography.subtitle1,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = recoveryPhrase,
            onValueChange = { recoveryPhrase = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                cursorColor = Color(0xFF8B5CF6),
                focusedBorderColor = Color(0xFF8B5CF6),
                unfocusedBorderColor = Color.Gray
            ),
            placeholder = {
                Text(
                    "Enter recovery phrase...",
                    color = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Words should be separated by spaces",
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                try {
                    val importedAccount =
                        account.import(recoveryPhrase.split(" ")) as Ed25519Account
                    onImport(importedAccount)
                } catch (e: Exception) {
                    // Handle import error
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF8B5CF6)
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Import Wallet",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Make sure you're in a private place and no one is watching your screen",
            style = MaterialTheme.typography.caption,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun WalletCreationOptions(
    onCreateNew: () -> Unit,
    onImport: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onCreateNew,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF8B5CF6)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = AddIcon,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create New Wallet",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onImport,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF1F2937)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImportIcon,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Import Existing Wallet",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}