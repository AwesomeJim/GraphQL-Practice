package com.example.rocketreserver.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rocketreserver.R
import com.example.rocketreserver.data.TokenRepository
import com.example.rocketreserver.ui.nav.MainNavHost
import com.example.rocketreserver.ui.theme.RocketReserverTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenRepository.init(this)
        setContent {
            RocketReserverTheme {
                val snackbarHostState = remember { SnackbarHostState() }
//                val tripBookedFlow =
//                    remember { apolloClient.subscription(TripsBookedSubscription()).toFlow() }
//                val tripBookedResponse: ApolloResponse<TripsBookedSubscription.Data>? by tripBookedFlow.collectAsState(
//                    initial = null
//                )
                val subscriptionState =
                    mainViewModel.getSubscriptions.collectAsStateWithLifecycle().value

                LaunchedEffect(subscriptionState) {
//                    if (tripBookedResponse == null) return@LaunchedEffect
//                    val message = when (tripBookedResponse!!.data?.tripsBooked) {
//                        null -> "Subscription error"
//                        -1 -> "Trip cancelled"
//                        else -> "Trip booked! 🚀"
//                    }
                    subscriptionState?.let {
                        snackbarHostState.showSnackbar(
                            message = it,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                Scaffold(
                    topBar = { TopAppBar({ Text(stringResource(R.string.app_name)) }) },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { paddingValues ->
                    Box(Modifier.padding(paddingValues)) {
                        MainNavHost()
                    }
                }
            }
        }
    }
}