package com.waveneuro.ui.dashboard.home

import com.waveneuro.ui.model.client.ClientUi

sealed class HomeViewEvent {
    object Start : HomeViewEvent()
    data class NewQuery(val query: String) : HomeViewEvent()
    data class OnClientClick(val id: Int) : HomeViewEvent()
    data class ClientSuccess(val client: ClientUi) : HomeViewEvent()
    //TODO
    class StartSessionClicked(
//        val treatmentLength: String,
//        val protocolFrequency: String,
//        val sonalId: String
    ) : HomeViewEvent()

    object PairDevice : HomeViewEvent()
    object DeviceConnected : HomeViewEvent()
    object DeviceDisconnected : HomeViewEvent()
}