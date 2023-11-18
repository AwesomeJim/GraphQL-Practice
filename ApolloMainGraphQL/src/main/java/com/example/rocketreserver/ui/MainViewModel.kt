package com.example.rocketreserver.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.example.rocketreserver.TripsBookedSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    apolloClient: ApolloClient
) : ViewModel() {
    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Listen to Trip booking event using Apollo subscriptions
     * converting the flow to a stateFlow
     */
    val getSubscriptions: StateFlow<String?> =
        apolloClient.subscription(TripsBookedSubscription()).toFlow().map { tripBookedResponse ->
            when (tripBookedResponse.data?.tripsBooked) {
                null -> "Subscription error"
                -1 -> "Trip cancelled"
                else -> "Trip booked! 🚀"
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = null
        )

}