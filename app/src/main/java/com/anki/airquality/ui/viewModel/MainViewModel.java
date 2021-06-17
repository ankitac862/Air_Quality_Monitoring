package com.anki.airquality.ui.viewModel;

import com.anki.airquality.socket.SocketLiveData;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private SocketLiveData socketLiveData;

    public MainViewModel() {
        socketLiveData = SocketLiveData.get();
    }

    public SocketLiveData getSocketLiveData() {
        return socketLiveData;
    }
}
