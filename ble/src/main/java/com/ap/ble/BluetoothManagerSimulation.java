package com.ap.ble;

import android.content.Context;
import android.os.Handler;

public class BluetoothManagerSimulation extends BluetoothManager{

//    public BluetoothManagerSimulation(Context context) {
//        super(context);
//    }

//    @Override
//    public void sendFrequencyData(String frequency, String treatmentLength) {
//
//    }

    @Override
    public void getSixthCharacteristic(Callback callback) {
        Handler handlerStart = new Handler();
        handlerStart.postDelayed(() -> {
            callback.invoke("02");
        }, 5_000);

        Handler handlerEnd = new Handler();
        handlerEnd.postDelayed(() -> {
            callback.invoke("04");
        }, 130_000);

//
//        Handler handlerPause = new Handler();
//        handlerPause.postDelayed(() -> {
//            callback.invoke("03");
//        }, 15_000);
//
//        Handler handlerResume = new Handler();
//        handlerResume.postDelayed(() -> {
//            callback.invoke("02");
//        }, 25_000);
//
//        Handler handlerError = new Handler();
//        handlerError.postDelayed(() -> {
////                callback.invoke("05");
//        }, 45_000);
    }

}
