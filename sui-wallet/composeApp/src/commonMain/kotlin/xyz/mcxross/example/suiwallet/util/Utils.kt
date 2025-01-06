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
package xyz.mcxross.example.suiwallet.util

import xyz.mcxross.ksui.Sui
import xyz.mcxross.ksui.account.Account
import xyz.mcxross.ksui.model.AccountAddress
import xyz.mcxross.ksui.model.Option
import xyz.mcxross.ksui.ptb.Argument
import xyz.mcxross.ksui.ptb.programmableTx
import xyz.mcxross.ksui.util.inputs

fun formatAddress(address: String, len: Int = 6): String {
    return "${address.substring(0, len)}...${address.substring(address.length - len)}"
}

suspend fun fetchBalance(sui: Sui, account: Account): Long {
    return try {
        val balance = sui.getBalance(account.address)
        if (balance is Option.Some) {
            balance.value?.address?.balance?.totalBalance?.toLong()?.div(1_000_000_000) ?: 0L
        } else {
            0L
        }
    } catch (e: Exception) {
        0L
    }
}

suspend fun send(
    sui: Sui,
    sender: Account,
    recipient: AccountAddress,
    amount: ULong
): Pair<Boolean, String> {
    val ptb = programmableTx {
        command {
            val splitCoins = splitCoins {
                coin = Argument.GasCoin
                into = inputs(amount)
            }

            transferObjects {
                objects = inputs(splitCoins)
                to = input(recipient)
            }
        }
    }

    val resp = sui.signAndExecuteTransactionBlock(sender, ptb)

    if (resp.value?.executeTransactionBlock?.errors != null) {
        return Pair(false, "")
    }

    return Pair(true, resp.value?.executeTransactionBlock?.effects?.transactionBlock?.digest ?: "")

}