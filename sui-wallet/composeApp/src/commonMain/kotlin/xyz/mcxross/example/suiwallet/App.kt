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
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.ui.tooling.preview.Preview
import xyz.mcxross.example.suiwallet.ui.screen.BaseScreen
import xyz.mcxross.example.suiwallet.ui.screen.WalletCreationScreen
import xyz.mcxross.ksui.account.Account

@Composable
@Preview
fun App() {

    var createdImportedAccount by remember { mutableStateOf<Account?>(null) }

    MaterialTheme {
        if (createdImportedAccount != null) {
            BaseScreen(createdImportedAccount!!)
        } else {
            WalletCreationScreen(
                onWalletCreated = { account ->
                    createdImportedAccount = account
                },
                onWalletImported = { account ->
                    createdImportedAccount = account
                }
            )
        }
    }
}