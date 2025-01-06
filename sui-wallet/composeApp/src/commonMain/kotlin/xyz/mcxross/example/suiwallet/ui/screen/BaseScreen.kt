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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import xyz.mcxross.example.suiwallet.model.BottomSheetContent
import xyz.mcxross.example.suiwallet.model.Screen
import xyz.mcxross.example.suiwallet.model.SendState
import xyz.mcxross.example.suiwallet.ui.components.ReceiveBottomSheet
import xyz.mcxross.example.suiwallet.ui.components.SendBottomSheet
import xyz.mcxross.example.suiwallet.ui.components.icons.ActivityIcon
import xyz.mcxross.example.suiwallet.util.send
import xyz.mcxross.ksui.Sui
import xyz.mcxross.ksui.account.Account
import xyz.mcxross.ksui.model.AccountAddress

@Composable
fun BaseScreen(
    account: Account,
) {
    val sui = remember { Sui() }
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var currentSheet by remember { mutableStateOf(BottomSheetContent.NONE) }
    var amount by remember { mutableStateOf(0UL) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6B46C1),
            Color(0xFF1E3A8A),
            Color.Black
        )
    )

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            when (currentSheet) {
                BottomSheetContent.SEND -> SendBottomSheet(
                    sui = sui,
                    account = account,
                    onClose = {
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    }
                )

                BottomSheetContent.RECEIVE -> ReceiveBottomSheet(
                    address = account.address.toString(),
                    onClose = {
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    }
                )

                BottomSheetContent.NONE -> {}
            }
        },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetBackgroundColor = Color(0xFF1A1A1A)
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    currentScreen = currentScreen,
                    onScreenChange = { screen -> currentScreen = screen }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            when (currentScreen) {
                Screen.HOME -> HomeScreen(
                    sui = sui,
                    account = account,
                    paddingValues = paddingValues,
                    gradientBackground = gradientBackground,
                    onSendClick = {
                        currentSheet = BottomSheetContent.SEND
                        scope.launch { bottomSheetState.show() }
                    },
                    onReceiveClick = {
                        currentSheet = BottomSheetContent.RECEIVE
                        scope.launch { bottomSheetState.show() }
                    }
                )

                Screen.EXPLORE -> ExploreScreen(
                    sui = sui,
                    account = account,
                    paddingValues = paddingValues,
                    gradientBackground = gradientBackground
                )

                Screen.ACTIVITY -> ActivityScreen(
                    sui = sui,
                    account = account,
                    paddingValues = paddingValues,
                    gradientBackground = gradientBackground
                )
            }
        }
    }
}

@Composable
private fun BottomNavigation(
    currentScreen: Screen,
    onScreenChange: (Screen) -> Unit
) {
    BottomNavigation(
        backgroundColor = Color(0xFF1A1A1A),
        contentColor = Color.White,
        elevation = 16.dp
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentScreen == Screen.HOME,
            onClick = { onScreenChange(Screen.HOME) }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Explore") },
            label = { Text("Explore") },
            selected = currentScreen == Screen.EXPLORE,
            onClick = { onScreenChange(Screen.EXPLORE) }
        )
        BottomNavigationItem(
            icon = { Icon(ActivityIcon, contentDescription = "Activity") },
            label = { Text("Activity") },
            selected = currentScreen == Screen.ACTIVITY,
            onClick = { onScreenChange(Screen.ACTIVITY) }
        )
    }
}