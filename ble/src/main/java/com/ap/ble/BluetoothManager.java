package com.ap.ble;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ap.ble.callback.BleGattCallback;
import com.ap.ble.callback.BleNotifyCallback;
import com.ap.ble.callback.BleScanCallback;
import com.ap.ble.callback.BleWriteCallback;
import com.ap.ble.data.BleDevice;
import com.ap.ble.exception.BleException;
import com.ap.ble.utils.HexUtil;

import java.util.ArrayList;
import java.util.List;

public class BluetoothManager {

    static BleDevice bleDevice;
    static ArrayList<BleDevice> bleDevices;
    BluetoothGattCharacteristic characterstic6;
    BluetoothGattCharacteristic charactersticC;
    BluetoothGattCharacteristic charactersticError;
    String treatmentStatus;
    String batteryPercentage;
    String acStatus;
    String errorDetail;
    String deviceCharacteristicValue;
    Application context;

    static List<DeviceConnectionCallback> deviceConnectionCallbackList = new ArrayList<>();

    public static BluetoothManager getInstance() {
        return BluetoothManagerHolder.S_BLE_MANAGER_1;
    }

    private static class BluetoothManagerHolder {
        private static final BluetoothManager S_BLE_MANAGER_1 = new BluetoothManager();
    }

    public void init(Application context) {
        this.context = context;
        BleManager.getInstance().init(context);
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);
    }

    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        if (deviceList != null && deviceList.size() > 0) {
            for (int i = deviceList.size(); i > 0; i--) {
                if (!deviceList.get(i - 1).getName().toLowerCase().contains("sonal")) {
                    deviceList.remove(i - 1);
                }
            }
        }
    }

    private void startScan(BleScanCallback success) {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean value) {
                success.onScanStarted(value);
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
                success.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
//                if (bleDevice.getName() == null || !bleDevice.getName().toLowerCase().contains("sonal")) {
//                    return;
//                } else {
//                    Bluetooth_bridging.this.bleDevice = bleDevice;
//                    HashMap<String, String> bleDevices= new HashMap<>();
//                    bleDevices.put(bleDevice.getDevice().getAddress(), bleDevice.getName());
//                    success.invoke(bleDevices);
//                }

                success.onScanning(bleDevice);
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                bleDevices = new ArrayList<>();
                for (int i = 0; i < scanResultList.size(); i++) {
                    if (scanResultList.get(i).getName() == null
                            || !scanResultList.get(i).getName().toLowerCase().contains("sonal")) {
                        continue;
                    } else {
//                        bleDevice = scanResultList.get(i);
                        bleDevices.add(scanResultList.get(i));
                    }
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
                    findServices();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String[] response = new String[2];
                            response[0] = "Device Connected";
                            response[1] = deviceCharacteristicValue;
                            deviceCallback.onCharacterises(deviceCharacteristicValue);
                        }
                    }, 5000);
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

        void onCharacterises(String value);

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

    public void deviceList(BleScanCallback success) {
        startScan(success);
    }

    public void sendFrequencyData(String frequency, String treatmentLength, Callback callback) {
        writeData(getFrequency(frequency) + getTreatmentLength(treatmentLength), callback);
    }

    public void getDeviceName(String mac, String name, DeviceConnectionCallback callback) {
        if (TextUtils.isEmpty(mac))
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

    public void disconnectedPeripheral(Callback callback) {
        if (bleDevice != null) {
            if (BleManager.getInstance().isConnected(bleDevice)) {
                callback.invoke("false");
                return;
            }
        }
        callback.invoke("true");
    }

    public void disconnectDevice(Callback callback) {
        if (bleDevice != null) {
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

    public void getBatteryCharacteristic(Callback callback) {
        if (batteryPercentage != null) {
            callback.invoke(batteryPercentage);
        } else {
            callback.invoke("0");
        }
    }

    public void getSixthCharacteristic(Callback callback) {
        if (bleDevice == null)
            return;
        if (characterstic6 != null) {
            BleManager.getInstance().notify(
                    bleDevice,
                    characterstic6.getService().getUuid().toString(),
                    characterstic6.getUuid().toString(),
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
                            treatmentStatus = HexUtil.formatHexString(characterstic6.getValue(), true);
                            if (treatmentStatus != null) {
                                callback.invoke(treatmentStatus);
//                                callback.invoke("02");
                            } else {
                                callback.invoke("00");
                            }
                        }
                    });
        }


        Log.e("SESSION_EVENT", "" + treatmentStatus);
//        if (treatmentStatus != null) {
//            callback.invoke("02");
//        } else {
//            callback.invoke("00");
//        }
    }

    public void getAcStatus(Callback callback) {
        if (acStatus != null) {
            callback.invoke(acStatus);
        } else {
            callback.invoke("00");
        }
    }

    public void getErrorDetail(Callback callback) {
        if (errorDetail != null) {
            Log.e("ERROR_DETAIL", "" + errorDetail.toLowerCase());
            callback.invoke(errorDetail);
        } else {
            Log.e("ERROR_DETAIL", "0000");
            callback.invoke("0000");
        }
    }

    private List<BluetoothGattService> getServices() {
        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
        return gatt.getServices();
    }

    private void findServices() {
        List<BluetoothGattService> services = getServices();
        if (services != null && services.size() > 0) {
            for (BluetoothGattService service : services) {
                if (service.getUuid().toString().contains("6e400001")) {
                    findCharacteristics(service);
                }
            }
        }
    }

    private void findCharacteristics(BluetoothGattService service) {
        if (bleDevice == null)
            return;
        BluetoothGattCharacteristic characterstic5 = null;
        BluetoothGattCharacteristic characterstic9 = null;
        BluetoothGattCharacteristic charactersticE = null;
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        if (characteristics != null && characteristics.size() > 0) {
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                if (characteristic.getUuid().toString().contains("6e400005")) {
                    characterstic5 = characteristic;
                } else if (characteristic.getUuid().toString().contains("6e400006")) {
                    characterstic6 = characteristic;
                    BleManager.getInstance().notify(
                            bleDevice,
                            characterstic6.getService().getUuid().toString(),
                            characterstic6.getUuid().toString(),
                            new BleNotifyCallback() {

                                @Override
                                public void onNotifySuccess() {
                                }

                                @Override
                                public void onNotifyFailure(final BleException exception) {
                                }

                                @Override
                                public void onCharacteristicChanged(byte[] data) {
                                    treatmentStatus = HexUtil.formatHexString(characterstic6.getValue(), true);
                                }
                            });
                } else if (characteristic.getUuid().toString().contains("6e40000e")) {
                    charactersticE = characteristic;
                } else if (characteristic.getUuid().toString().contains("6e40000c")) {
                    charactersticC = characteristic;
                    BleManager.getInstance().notify(
                            bleDevice,
                            charactersticC.getService().getUuid().toString(),
                            charactersticC.getUuid().toString(),
                            new BleNotifyCallback() {

                                @Override
                                public void onNotifySuccess() {
                                    BleManager.getInstance().stopNotify(
                                            bleDevice,
                                            charactersticC.getService().getUuid().toString(),
                                            charactersticC.getUuid().toString());
                                }

                                @Override
                                public void onNotifyFailure(final BleException exception) {

                                }

                                @Override
                                public void onCharacteristicChanged(byte[] data) {
                                    acStatus = HexUtil.formatHexString(charactersticC.getValue(), true);
                                }
                            });
                } else if (characteristic.getUuid().toString().contains("6e400015")) {
                    charactersticError = characteristic;
                    BleManager.getInstance().notify(
                            bleDevice,
                            charactersticError.getService().getUuid().toString(),
                            charactersticError.getUuid().toString(),
                            new BleNotifyCallback() {

                                @Override
                                public void onNotifySuccess() {
                                    BleManager.getInstance().stopNotify(
                                            bleDevice,
                                            charactersticError.getService().getUuid().toString(),
                                            charactersticError.getUuid().toString());
                                }

                                @Override
                                public void onNotifyFailure(final BleException exception) {

                                }

                                @Override
                                public void onCharacteristicChanged(byte[] data) {
                                    errorDetail = HexUtil.formatHexString(charactersticError.getValue(), true);
                                }
                            });
                } else if (characteristic.getUuid().toString().contains("6e400013")) {
                } else if (characteristic.getUuid().toString().contains("6e400009")) {
                    characterstic9 = characteristic;
                }
            }
            if (characterstic9 != null) {
                readBatteryValue(characterstic9);
            }
        }
    }

    private void readBatteryValue(BluetoothGattCharacteristic characteristic) {
        if (characteristic != null && characteristic.getValue() != null) {
            batteryPercentage = new String(characteristic.getValue());
            deviceCharacteristicValue = HexUtil.formatHexString(characteristic.getValue(), true);
        } else {
            batteryPercentage = "0";
            deviceCharacteristicValue = "0";
        }
    }

    private void writeData(String hexString, Callback callback) {
        BluetoothGattCharacteristic characterstic5 = null;
        BluetoothGattCharacteristic characterstic9 = null;
        BluetoothGattCharacteristic charactersticE = null;
        List<BluetoothGattService> services = getServices();
        if (services != null && services.size() > 0) {
            for (BluetoothGattService service : services) {
                if (service.getUuid().toString().contains("6e400001")) {
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    if (characteristics != null && characteristics.size() > 0) {
                        for (BluetoothGattCharacteristic characteristic : characteristics) {
                            if (characteristic.getUuid().toString().contains("6e400005")) {
                                characterstic5 = characteristic;
                            } else if (characteristic.getUuid().toString().contains("6e40000e")) {
                                charactersticE = characteristic;
                            } else if (characteristic.getUuid().toString().contains("6e400009")) {
                                characterstic9 = characteristic;
                            }
                        }
                        if (charactersticE != null) {
                            BluetoothGattCharacteristic finalCharacteristicE = charactersticE;
                            BluetoothGattCharacteristic finalCharacteristic5 = characterstic5;
                            new Handler().post(() -> {
                                if (!TextUtils.isEmpty(hexString)) {
                                    BleManager.getInstance().write(
                                            bleDevice,
                                            finalCharacteristicE.getService().getUuid().toString(),
                                            finalCharacteristicE.getUuid().toString(),
                                            HexUtil.hexStringToBytes(hexString),
                                            new BleWriteCallback() {

                                                @Override
                                                public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                                        BluetoothGattCharacteristic finalCharacteristic = finalCharacteristic5;
                                                        new Handler().post(() -> BleManager.getInstance().write(
                                                                bleDevice,
                                                                finalCharacteristic.getService().getUuid().toString(),
                                                                finalCharacteristic.getUuid().toString(),
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

                            });

                        }
                    }
                }
            }
        }
    }

    public interface Callback {

        void invoke(String args);

        void invoke(List<BleDevice> args);

        void invoke(String[] args);
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
}
