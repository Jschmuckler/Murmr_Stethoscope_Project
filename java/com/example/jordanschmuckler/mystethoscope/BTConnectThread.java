package com.example.jordanschmuckler.mystethoscope;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Jordan Schmuckler on 10/3/2015.
 */
class BTConnectThread extends Thread{

    private final BluetoothDevice bTDevice;
    private final BluetoothSocket bTSocket;
    private MainActivity context;

    public BTConnectThread(BluetoothDevice bTDevice, UUID UUID,MainActivity context) {
        this.context = context;
        BluetoothSocket tmp = null;
        this.bTDevice = bTDevice;

        try {
            tmp = this.bTDevice.createInsecureRfcommSocketToServiceRecord(UUID);
        }
        catch (IOException e) {
            Log.d("CONNECTTHREAD", "Could not start listening for RFCOMM");
        }
        bTSocket = tmp;
            this.connect();
        if(bTDevice.getBondState()==12)
        {
            this.cancel();
        }
    }

    public boolean connect() {

        try {
            bTSocket.connect();

        } catch(IOException e) {
            Log.d("CONNECTTHREAD","Could not connect: " + e.toString());
            try {
                bTSocket.close();
            } catch(IOException close) {
                Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString());
                return false;
            }
        }
        if(bTDevice.getBondState()==12) {
            context.connectedToDevice(bTSocket);
        }
        return true;
    }

    public boolean cancel() {
        try {
            bTSocket.close();
        } catch(IOException e) {
            return false;
        }
        return true;
    }
}