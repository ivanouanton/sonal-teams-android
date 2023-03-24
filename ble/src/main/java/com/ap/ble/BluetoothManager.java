package com.ap.ble;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.ap.ble.callback.BleGattCallback;
import com.ap.ble.callback.BleNotifyCallback;
import com.ap.ble.callback.BleScanCallback;
import com.ap.ble.callback.BleWriteCallback;
import com.ap.ble.data.BleDevice;
import com.ap.ble.exception.BleException;
import com.ap.ble.utils.HexUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothManager {

    static BleDevice bleDevice;
    static ArrayList<BleDevice> bleDevices;

    String treatmentStatus;
    Application context;

    public byte batteryLevel;

    public static final String INACTIVE         = "00";
    public static final String LOADED           = "01";
    public static final String START_SESSION    = "02";
    public static final String PAUSE_SESSION    = "03";
    public static final String END_SESSION      = "04";
    public static final String ERROR            = "05";

    public static final String SERVICE_ID               = "6e400001-b5a3-f393-e0a9-e50ecfdcca9e";

    public static final String OPERATING_MODE           = "6e400005-b5a3-f393-e0a9-e50ecfdcca9e";
    public static final String SESSION_CONFIG           = "6e40000e-b5a3-f393-e0a9-e50ecfdcca9e";
    public static final String BATTERY_INFO             = "6e400009-b5a3-f393-e0a9-e50ecfdcca9e";
    public static final String TREATMENT_SESSION_STATUS = "6e400006-b5a3-f393-e0a9-e50ecfdcca9e";

    static List<DeviceConnectionCallback> deviceConnectionCallbackList = new ArrayList<>();
    static List<OnBatteryLevelChangedCallback> batteryLevelChangedCallbacks = new ArrayList<>();

    public static BluetoothManager getInstance() {
        return BluetoothManagerHolder.S_BLE_MANAGER_1;
    }

    private static class BluetoothManagerHolder {
        private static final BluetoothManager S_BLE_MANAGER_1 = new BluetoothManager();
    }

    public void init(Application context) {
        this.context = context;
        BleManager.getInstance().init(context, new UUID[]{UUID.fromString(SERVICE_ID)});
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);
    }

    private void startScan(BleScanCallback success) {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean value) {
                treatmentStatus = null;
                success.onScanStarted(value);
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
                success.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                bleDevices = new ArrayList<>();
                bleDevices.add(bleDevice);
                success.onScanning(bleDevice);
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                bleDevices = new ArrayList<>();
                for (int i = 0; i < scanResultList.size(); i++) {
                    bleDevices.add(scanResultList.get(i));
                }
                success.onScanFinished(bleDevices);
            }
        });
    }

    private void connect(final BleDevice bleDevice, DeviceConnectionCallback deviceCallback) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                BluetoothManager.bleDevice = null;
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                new Handler().postDelayed(() -> {
                    deviceCallback.onConnected(bleDevice);
                    setupNotifying();
                }, 1000);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                try {
                    deviceCallback.onDisconnected();
                    Log.e("SESSION DISCONNECT BLE", "1");
                    if (deviceConnectionCallbackList != null) {
                        Log.e("SESSION DISCONNECT BLE", "2");
                        for (DeviceConnectionCallback callBack : deviceConnectionCallbackList) {
                            Log.e("SESSION DISCONNECT BLE", "3");
                            callBack.onDisconnected();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BluetoothManager.bleDevice = null;

            }
        });
    }

    public interface DeviceConnectionCallback {
        void onConnected(BleDevice bleDevice);

        void onDisconnected();
    }

    public void registerDeviceConnectionCallback(DeviceConnectionCallback callback) {
        if (!deviceConnectionCallbackList.contains(callback)) {
            deviceConnectionCallbackList.add(callback);
        }
    }

    public void unregisterDeviceConnectionCallback(DeviceConnectionCallback callback) {
        if (!deviceConnectionCallbackList.contains(callback)) {
            deviceConnectionCallbackList.remove(callback);
        }
    }

    public void registerBatteryLevelChangedCallback(OnBatteryLevelChangedCallback callback) {
        if (!batteryLevelChangedCallbacks.contains(callback)) {
            batteryLevelChangedCallbacks.add(callback);
        }
    }

    public void unregisterBatteryLevelChangedCallback(OnBatteryLevelChangedCallback callback) {
        if (!batteryLevelChangedCallbacks.contains(callback)) {
            batteryLevelChangedCallbacks.remove(callback);
        }
    }

    public void deviceList(BleScanCallback success) { startScan(success); }

    public void sendFrequencyData(String frequency, String treatmentLength, Callback callback) {
        writeData(getFrequency(frequency) + getTreatmentLength(treatmentLength), callback);
    }

    public void getDeviceName(String mac, String name, DeviceConnectionCallback callback) {
        if (TextUtils.isEmpty(mac))
            return;

        BleManager.getInstance().cancelScan();

        if (bleDevices == null)
            return;

        for (int i = 0; i < bleDevices.size(); i++) {
            if (mac.equals(bleDevices.get(i).getMac())) {
                bleDevice = bleDevices.get(i);
                break;
            }
        }
        if (bleDevice == null)
            return;
        connect(bleDevice, callback);
    }

    public void disconnectDevice(Callback callback) {
        if (bleDevice != null) {
            BleManager.getInstance().stopNotify(
                    bleDevice,
                    SERVICE_ID,
                    TREATMENT_SESSION_STATUS
            );

            if (BleManager.getInstance().isConnected(bleDevice)) {
                BleManager.getInstance().disconnect(bleDevice);
                if (callback != null)
                    callback.invoke("true");
                return;
            }
        }
        if (callback != null)
            callback.invoke("false");
    }

    public void disconnectDevice() {
        disconnectDevice(null);
    }

    public void getSixthCharacteristic(Callback callback) {
        if (bleDevice == null)
            return;

        BleManager.getInstance().notify(
                bleDevice,
                SERVICE_ID,
                TREATMENT_SESSION_STATUS,
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {
                        callback.invoke("NOTIFY_SUCCESS");
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        callback.invoke("" + exception.getDescription());
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        treatmentStatus = HexUtil.formatHexString(data, true);
                        if (treatmentStatus != null) {
                            callback.invoke(treatmentStatus);
                        } else {
                            callback.invoke("00");
                        }
                    }
                });
    }

    public void setupNotifying() {
        if (bleDevice == null)
            return;

        BleManager.getInstance().notify(
                bleDevice,
                SERVICE_ID,
                BATTERY_INFO,
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {
                        Log.d("TAG_BATTERY", "onNotifySuccess: ");
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        Log.d("TAG_BATTERY", "onNotifyFailure: ");

                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        if (data.length >= 5) {
                            batteryLevel = data[4];
                            for (OnBatteryLevelChangedCallback callback : batteryLevelChangedCallbacks) {
                                callback.onBatteryLevelChanged(data[4]);
                            }
                        }
                    }
                });
    }

    private void writeData(String hexString, Callback callback) {
        new Handler().postDelayed(() -> BleManager.getInstance().write(
                bleDevice,
                SERVICE_ID,
                OPERATING_MODE,
                HexUtil.hexStringToBytes("01"),
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        writeParameters(hexString, callback);
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        callback.invoke("false");
                    }
                }), 1500);
    }

    private void writeParameters(String hexString, Callback callback) {
        if (!TextUtils.isEmpty(hexString)) {
            BleManager.getInstance().write(
                    bleDevice,
                    SERVICE_ID,
                    SESSION_CONFIG,
                    HexUtil.hexStringToBytes(hexString),
                    new BleWriteCallback() {

                        @Override
                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                            new Handler().post(() -> BleManager.getInstance().write(
                                    bleDevice,
                                    SERVICE_ID,
                                    OPERATING_MODE,
                                    HexUtil.hexStringToBytes("02"),
                                    new BleWriteCallback() {

                                        @Override
                                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                            callback.invoke("true");
                                        }

                                        @Override
                                        public void onWriteFailure(final BleException exception) {
                                            callback.invoke("false");
                                        }
                                    }));
                        }

                        @Override
                        public void onWriteFailure(final BleException exception) {
                            callback.invoke("false");
                        }
                    });
        }
    }

    public interface Callback {
        void invoke(String args);
    }

    public String getTreatmentLength(String sessionLength) {
        String hex = Integer.toHexString(Integer.parseInt(sessionLength));
        if (hex.length() == 1) {
            hex = "000" + hex;
        } else if (hex.length() == 2) {
            hex = "00" + hex;
        } else if (hex.length() == 3) {
            hex = "0" + hex;
        }
        return hex.substring(2, 4) + hex.substring(0, 2);
    }

    public String getFrequency(String frequency) {
        int decimal = (int) (10 * Double.parseDouble(frequency));
        return Integer.toHexString(decimal);
    }

    public interface OnBatteryLevelChangedCallback {
        void onBatteryLevelChanged(byte newValue);
    }
}