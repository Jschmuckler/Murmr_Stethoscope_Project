package com.example.jordanschmuckler.mystethoscope;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Jordan Schmuckler on 4/12/2015.
 */
public class CustomAdapter extends ArrayAdapter<Recording> {



    private static final String TAG = "CustomAdapter";
    private List<Recording> recordings;
    DataDisplay context;
    private Recording current;



    private static final int MAX_STREAMS = 10;
    private static final float LEFTVOLUME = 1;
    private static final float RIGHTVOLUME = 1;
    private static final int PRIORITY = 0;
    private static final int LOOPFOREVER = -1;
    private static final int LOOPNOT = 0;
    private static final float RATE = 1;
    SoundPool sp;
    int trackScream;
    int positionNow;

    private static class ViewHolder {

        TextView Notes;
        TextView Date;
        TextView Duration;
        TextView Name;
        TextView Time;
        ImageView OrganImage;
    }
    public CustomAdapter(DataDisplay context, int textViewResourceId,RecordingsListManager listManager) {
        super(context, textViewResourceId,listManager.getListCopy());
    this.recordings = listManager.getListCopy();
        this.context = context;
        System.out.println("Custom Adapter has been constructed.");


    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {



        final ViewHolder holder;
        View rowView = null;
        System.out.println("THE GETVIEW METHOD HAS BEEN OPENED at position " + position);
        if(convertView == null)
        {
            LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = li.inflate(R.layout.list_items, null); //used to be convertView = ...;
            holder = new ViewHolder();
            holder.Notes = (TextView)rowView.findViewById(R.id.Notes);
            holder.Date = (TextView)rowView.findViewById(R.id.Date);
            holder.Duration = (TextView)rowView.findViewById(R.id.Duration);
            holder.Name = (TextView)rowView.findViewById(R.id.Name);
            holder.Time = (TextView)rowView.findViewById((R.id.TimeMade));
            holder.OrganImage = (ImageView)rowView.findViewById(R.id.OrganImage);

            rowView.setTag(holder);



        }
        else
        {
            rowView = convertView; //new
            holder = (ViewHolder)rowView.getTag(); //used to be (ViewHolder)convertView.getTag();
        }
        this.current = recordings.get(position);

        holder.Date.setText(current.getDate());
        holder.Notes.setText(current.getNotes());
        holder.Duration.setText(current.getDuration() + " seconds");
        holder.Name.setText(current.getName());
        holder.Time.setText(current.getTimeMade());

        holder.OrganImage.setTag(current.getSoundLoc());

        AsyncSetOrganImage setPicturesTask = new AsyncSetOrganImage(this,holder.OrganImage,current.getImageName());
        setPicturesTask.execute();


        holder.OrganImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get soundpool object
                ImageView organImage = (ImageView)v.findViewById(R.id.OrganImage);
                organImage.getTag().toString();
                Toast.makeText(context, organImage.getTag().toString(), Toast.LENGTH_LONG).show();
                sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
               trackScream = sp.load(organImage.getTag().toString()+".wav", 0);
                //trackScream = sp.load("/storage/emulated/0/StethoscopeAudio/10-28-2015191430.wav", 0);
                sp.play(trackScream, LEFTVOLUME, RIGHTVOLUME, PRIORITY, LOOPNOT, RATE);
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
                sp.stop(trackScream);
            }
        });

        return rowView;
    }

    @Override
    public int getCount()
    {
        System.out.println("Get Count returned: " + this.recordings.size());
        return this.recordings.size();
    }

    @Override
    public Recording getItem(int position)
    {
        System.out.println("Get Item returned: " + this.recordings.get(position));
        return this.recordings.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        System.out.println("Get Item ID returned :" + (long)position);
        return (long)position;
    }

    public void setRecordings(List<Recording> recordings)
    {
        this.recordings = recordings;
    }

    public void setRandomData()
    {
        Recording test1;

        for(int k = 0; k<100; k++)
        {
            test1 = new Recording();

            //setting the date
            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            int year = c.get(Calendar.YEAR);
            String date = month + "/" + day + "/" + year;
            test1.setDate(date);

            //setting the time
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int second = c.get(Calendar.SECOND);
            String time = hour + ":" + minute + ":" + second;
            test1.setTimeMade(time);
            System.out.println(date);
            int onetothree = 1 + (int)(Math.random()*3);

            switch (onetothree)
            {
                case 1:
                    test1.setImage("HEART");
                    break;
                case 2:
                    test1.setImage("LUNGS");
                    break;
                case 3:
                    test1.setImage("QUESTIONMARK");
                    break;

            }
            recordings.add(test1);
            System.out.println(onetothree);
        }
    }
}
