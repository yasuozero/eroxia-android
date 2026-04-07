package com.fecrin.eroxia.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fecrin.eroxia.R
import com.fecrin.eroxia.presentation.navigation.Control
import com.fecrin.eroxia.presentation.navigation.Home
import com.fecrin.eroxia.presentation.navigation.Viewer
import com.fecrin.eroxia.presentation.screens.home.components.AdminPasswordDialog
import com.fecrin.eroxia.presentation.screens.home.components.HomeStateView

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val adminError by viewModel.adminError.collectAsState()
    var showPasswordDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.adminSuccess.collect {
            navController.navigate(Control) {
                popUpTo(Home) { inclusive = true }
            }
        }
    }



    if (showPasswordDialog) {
        AdminPasswordDialog(
            onDismiss = { showPasswordDialog = false },
            onConfirm = { password ->
                viewModel.loginAsAdmin(password)
            },
            errorMessage = adminError?.let { stringResource(id = it) } ?: ""
        )
    }

    when (uiState) {
        HomeUiState.AdminAvailable -> {
            HomeStateView(
                title = stringResource(id = R.string.admin_available_title),
                description = stringResource(id = R.string.admin_available_description),
                icon = Icons.Rounded.CheckCircle,
                state = uiState,
                onJoinAdminClick = { showPasswordDialog = true },
                onJoinViewerClick = {
                    navController.navigate(Viewer) {
                        popUpTo(Home)
                    }
                }
            )
        }
        HomeUiState.AdminTaken -> {
            HomeStateView(
                title = stringResource(id = R.string.admin_taken_title),
                description = stringResource(id = R.string.admin_taken_description),
                icon = Icons.Rounded.Lock,
                state = uiState
            )
        }
        HomeUiState.Disconnected -> {
            HomeStateView(
                title = stringResource(id = R.string.disconnected_title),
                description = stringResource(id = R.string.disconnected_description),
                icon = Icons.Rounded.Close,
                state = uiState
            )
        }
    }

}
