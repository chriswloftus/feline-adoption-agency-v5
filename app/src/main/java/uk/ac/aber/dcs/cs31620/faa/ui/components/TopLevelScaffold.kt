package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

/**
 * Creates the page scaffold to contain top app bar, navigation drawer,
 * bottom navigation button and of course the page content.
 * @param navController To pass through the NavHostController since navigation is required
 * @param pageContent So that callers can plug in their own page content.
 * By default an empty lambda.
 * @author Chris Loftus
 */

@Composable
fun TopLevelScaffold(
    navController: NavHostController,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            MainPageTopAppBar(onClick = {
                coroutineScope.launch {
                    if (scaffoldState.drawerState.isOpen){
                        scaffoldState.drawerState.close()
                    } else {
                        scaffoldState.drawerState.open()
                    }
                }
            })
        },
        bottomBar = {
            MainPageNavigationBar(navController)
        },
        drawerContent = {
            MainPageNavigationDrawer(
                navController,
                closeDrawer = {
                    coroutineScope.launch {
                        // We know it will be open
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        content = { innerPadding ->
            pageContent(innerPadding)
        }
    )
}