package com.anki.airquality.ui.viewModel;

import com.anki.airquality.socket.SocketLiveData;

import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {

    private SocketLiveData socketLiveData;

    public DetailViewModel() {
        socketLiveData = SocketLiveData.get();
    }

    public SocketLiveData getSocketLiveData() {
        return socketLiveData;
    }
}
