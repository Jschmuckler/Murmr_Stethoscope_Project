package com.example.jordanschmuckler.mystethoscope;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Jordan Schmuckler on 4/12/2015.
 */
public class DataDisplay extends ListActivity {
    private CustomAdapter myAdapter;
    private RecordingsListManager listManager;
     private ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        System.out.println("Inside DataDisplay");
       // ArrayList<Recording> recordings = new ArrayList<Recording>();
         listManager = new RecordingsListManager(this);

        myAdapter = new CustomAdapter(this, R.layout.list_items,listManager);
        setListAdapter(myAdapter);

          myList = (ListView)this.findViewById(list);

    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {

        super.onListItemClick(l, v, position, id);
        System.out.println("User clicked on item at position: " + position + "\n" + " with an ID of: " + id);

        final AlertDialog.Builder recordInfoBuilder = new AlertDialog.Builder(this);
        recordInfoBuilder.setMessage(l.getAdapter().getItem(position).toString());

        recordInfoBuilder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listManager.delete(position);

                myAdapter.setRecordings(listManager.getListCopy());
                myAdapter.notifyDataSetChanged();
            }
        });

        recordInfoBuilder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent EditDataIntent = new Intent(DataDisplay.this, NewRecordingActivity.class);

                Recording currentRecord = listManager.getRecordingAtPosition(position);


                EditDataIntent.putExtra("duration", currentRecord.getDuration());
                EditDataIntent.putExtra("date", currentRecord.getDate());
                EditDataIntent.putExtra("time", currentRecord.getTimeMade());
                EditDataIntent.putExtra("name", currentRecord.getName());
                EditDataIntent.putExtra("notes", currentRecord.getNotes());
                EditDataIntent.putExtra("organ", currentRecord.getImageName());
                EditDataIntent.putExtra("position", position);
                EditDataIntent.putExtra("isDataDisplay", 1);
                startActivityForResult(EditDataIntent, 1);
            }
        });

        recordInfoBuilder.setNegativeButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Recording currentRecord = listManager.getRecordingAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, currentRecord.getImageName() + " data for " + currentRecord.getName());
                intent.putExtra(Intent.EXTRA_TEXT, currentRecord.getDate() + " at " + currentRecord.getTimeMade() + "       Notes      " + currentRecord.getNotes());

                //File f = new File (R.raw.scream);
                //Uri uri = Uri.fromFile(f);
                File file = new File(currentRecord.getSoundLoc() + ".wav");
                Uri URI = Uri.fromFile(file);
                //Uri URI = Uri.parse(currentRecord.getSoundLoc() );
                intent.putExtra(Intent.EXTRA_STREAM,URI);
                startActivity(Intent.createChooser(intent, "Send Email"));


                dialog.dismiss();
            }
        });
         recordInfoBuilder.create();
        recordInfoBuilder.show();

        }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String notes = data.getStringExtra("notes");
                String image = data.getStringExtra("organ");

                int position = data.getIntExtra("position", -1);
                listManager.edit(position,name,notes,image);
                myAdapter.setRecordings(listManager.getListCopy());
                myAdapter.notifyDataSetChanged();


            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }
}
