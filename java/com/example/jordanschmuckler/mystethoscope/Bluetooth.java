package com.example.jordanschmuckler.mystethoscope;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Jordan Schmuckler on 10/3/2015.
 */
public class Bluetooth {

private BluetoothAdapter BTAdapter;

    private Activity main;
    public static int REQUEST_BLUETOOTH = 3;
    public Bluetooth(MainActivity main)
    {

         BTAdapter  = BluetoothAdapter.getDefaultAdapter();

    }

    public boolean checkIfBluetoothIsOnTheDevice()
    {
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(main)
                    .setTitle("Not compatible")
                    .setMessage("Your device does not support Bluetooth, you cannot use this app.")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        return false;
        }
        return true;
    }

    public void requestBluetoothBeTurnedOn(MainActivity main1)
    {
        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            main1.startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

    }
    public boolean isBluetoothEnabled()
    {
        if (BTAdapter.isEnabled())
        {
            return true;
        }
        return false;
    }



    public void discoverDevices()
    {
        BTAdapter.startDiscovery();
    }

    public void createInsecureConnection(BluetoothDevice device, UUID uuid)
    {

    }



    }

