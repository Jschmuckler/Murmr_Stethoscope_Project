package com.example.jordanschmuckler.mystethoscope;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.FileObserver;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.Calendar;
import java.util.List;

/**
 * @author Jordan Schmuckler
 * @version 8.2
 */


public class MainActivity extends ActionBarActivity {

    private List<Recording> recordings;
    SharedPreferences settings;
    public static RecordingsListManager listManager;
    String UUID = "00001101-0000-1000-8000-00805F9B34FB";
    UUID uuid = null;
    BluetoothDevice device;
    BluetoothDevice stethoscope;
    BluetoothSocket btSocketMain = null;
    BluetoothAdapter btAdapter;
    BroadcastReceiver mReceiver;
    IntentFilter filter;
    IntentFilter filterConnection;
    File requests;
    FileObserver audioObserver;
    String date, fileDate;
    String time, fileTime;
    ProgressDialog bluetoothProgressDialog;
    File rootAudio;
    boolean received = false;
    SharedPreferences.OnSharedPreferenceChangeListener spChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bluetooth bt = new Bluetooth(this);
        if(!bt.isBluetoothEnabled())
        {
            Button mButton=(Button)this.findViewById(R.id.button);
            mButton.setText("Start bluetooth");
        }
        if(bt.isBluetoothEnabled())
        {
            Button mButton=(Button)this.findViewById(R.id.button);
            mButton.setText("Record");
        }


        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                }

            }
        };
        // Register the BroadcastReceiver
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        filterConnection = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        registerReceiver(mReceiver, filterConnection); // Don't forget to unregister during onDestroy

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        listManager = new RecordingsListManager(this);

    }

    public void addARequestString(String sBody) {
        try {
            File rootNotes = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!rootNotes.exists()) {
                rootNotes.mkdirs();
            }
            rootAudio = new File(Environment.getExternalStorageDirectory(), "StethoscopeAudio");
            if (!rootAudio.exists()) {
                rootAudio.mkdirs();

            }

            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            int year = c.get(Calendar.YEAR);
            String monthSt = ("" + month);
            String daySt = ("" + day);
            if (month < 10) {

                monthSt = "0" + month;
            }
            if (day < 10) {

                daySt = "0" + day;
            }

            date = monthSt + "-" + daySt + "-" + year;
            fileDate = monthSt + daySt + year;


            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int second = c.get(Calendar.SECOND);
            String hourSt = "" + hour;
            String minuteSt = "" + minute;
            String secondSt = "" + second;
            if (hour < 10) {

                hourSt = "0" + hour;
            }
            if (minute < 10) {

                minuteSt = "0" + minute;
            }
            if (second < 10) {

                secondSt = "0" + second;
            }
            time = hourSt + ":" + minuteSt + ":" + secondSt;
            fileTime = hourSt + minuteSt + secondSt;
            audioObserver = new FileObserver(rootAudio.toString()) { // set up a file observer to watch this directory on sd card
                @Override
                public void onEvent(int event, String file) {
                    //if(event == FileObserver.MOVED_TO){

                if(!received)
                {
                    received = true;
                    audioObserver.stopWatching();
                    createRecordingIntent();
                }
                }
            };
            audioObserver.startWatching(); //START OBSERVING
            received = false;
            if (requests == null) {
                requests = new File(rootNotes, "requests.txt");
            }
            FileWriter writer = new FileWriter(requests);
            writer.append(sBody + " " + fileDate + fileTime);
            writer.flush();
            writer.close();
            Toast.makeText(this, settings.getString("DURATION", "10") + "seconds", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void clearRequests() {

        try {
            File rootNotes = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!rootNotes.exists()) {
                rootNotes.mkdirs();
            }

            if (requests == null) {
                requests = new File(rootNotes, "requests.txt");
            }
            FileWriter writer = new FileWriter(requests);
            writer.append("");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void createRecordingIntent() {
        audioObserver.stopWatching();
        Intent newRecordingIntent = new Intent(MainActivity.this, NewRecordingActivity.class);

        String duration = settings.getString("DURATION", "10");

        newRecordingIntent.putExtra("duration", duration);
        newRecordingIntent.putExtra("date", date);
        newRecordingIntent.putExtra("time", time);

        newRecordingIntent.putExtra("soundLoc", rootAudio + "/" + fileDate + fileTime);

        startActivityForResult(newRecordingIntent, 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        System.out.println("inflating the menu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {

            case R.id.action_settings:
                Intent openSettings = new Intent(this, SettingsActivity.class);
                startActivity(openSettings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public RecordingsListManager getListManager() {
        return listManager;
    }

    public void startListActivityListener(View view) {

        Intent displayActivityIntent = new Intent(this, DataDisplay.class);
        startActivity(displayActivityIntent);
    }




    public void startRecordListener(View view) {

        Bluetooth bt = new Bluetooth(this);
        if(!bt.isBluetoothEnabled())
{
    bt.requestBluetoothBeTurnedOn(this);

}
        btAdapter = btAdapter.getDefaultAdapter();
        if (bt.isBluetoothEnabled()) {
            Button mButton=(Button)this.findViewById(R.id.button);
            mButton.setText("Record");
            Log.i("TAG", "localdevicename : " + btAdapter.getName() + " localdeviceAddress : " + btAdapter.getAddress());
            btAdapter.setName("PiSlave");
            Log.i("TAG", "localdevicename : " + btAdapter.getName() + " localdeviceAddress : " + btAdapter.getAddress());

            if (btAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE && bt.isBluetoothEnabled())
            {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivityForResult(discoverableIntent,2);



            }
            if (btAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                addARequestString(settings.getString("DURATION", "10"));
                makeTheDoingARecordingScreen();
            }
        }


    }



    private void makeTheDoingARecordingScreen()
    {
        bluetoothProgressDialog = new ProgressDialog(MainActivity.this);
        bluetoothProgressDialog.setTitle("Do your recording now...");
        bluetoothProgressDialog.setMessage("Recording in progress ...");
        bluetoothProgressDialog.setProgressStyle(bluetoothProgressDialog.STYLE_SPINNER);
        bluetoothProgressDialog.setCanceledOnTouchOutside(true);
        bluetoothProgressDialog.setCancelable(true);
        bluetoothProgressDialog.show();
        System.out.println("things should be spinning now");

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String notes = data.getStringExtra("notes");
                String image = data.getStringExtra("organ");
                String duration = data.getStringExtra("duration");
                String date = data.getStringExtra("date");
                String time = data.getStringExtra("time");
                String soundLoc = data.getStringExtra("soundLoc");
                bluetoothProgressDialog.setCancelable(true);
                bluetoothProgressDialog.dismiss();
                listManager.addData(name, duration, date, notes, time, image, soundLoc);
            }
            if (resultCode == RESULT_CANCELED) {
                bluetoothProgressDialog.setCancelable(true);
                bluetoothProgressDialog.dismiss();
                if (audioObserver!= null) {
                    audioObserver.stopWatching();
                    clearRequests();
                }
                Toast.makeText(MainActivity.this, "Recording canceled.",Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode == 2)
        {
            if(resultCode==300)
            {
                addARequestString(settings.getString("DURATION", "10"));
                makeTheDoingARecordingScreen();
            }
        }

        if(requestCode == 3)
        {
            if(resultCode!=0)
            {
                Button mButton=(Button)this.findViewById(R.id.button);
                mButton.setText("Record");
            }

        }
    }


    public void connectedToDevice(BluetoothSocket btSocket)
    {
        btSocketMain = btSocket;
        Toast.makeText(MainActivity.this, "I have set the btsocket",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy()
    {
        if(mReceiver != null ) {
            unregisterReceiver(mReceiver);
            clearRequests();
        }
        if (audioObserver!= null) {
            audioObserver.stopWatching();
            clearRequests();
        }
        super.onDestroy();
    }
}

