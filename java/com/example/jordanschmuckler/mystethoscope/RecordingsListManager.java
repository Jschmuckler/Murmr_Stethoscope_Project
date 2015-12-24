package com.example.jordanschmuckler.mystethoscope;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordan Schmuckler on 5/9/2015.
 */
public class RecordingsListManager
{
     private List<Recording> recordings;
    Activity context;
    String jsonRecordings;
    public RecordingsListManager(Activity context)
    {
        this.context = context;
        recordings = new ArrayList<Recording>();
    }
//http://stackoverflow.com/questions/14981233/android-arraylist-of-custom-objects-save-to-sharedpreferences-serializable

    public void addData(String name, String duration, String date,String notes,String timeMade,String whichImage,String soundLoc)
    {
        readData();
        Recording newRecording = new Recording();
        newRecording.setName(name);
        newRecording.setDuration(duration);
        newRecording.setDate(date);
        newRecording.setNotes(notes);
        newRecording.setTimeMade(timeMade);
        newRecording.setImage(whichImage);
        newRecording.setSoundLoc(soundLoc);
        recordings.add(newRecording);
        writeData();
    }


    public void delete(int position)
    {
        readData();
       Recording r = recordings.get(position);
        String soundLoc = r.getSoundLoc();
        File file = new File(soundLoc+ ".wav");
        boolean deleted = file.delete();
        recordings.remove(position);
        writeData();
    }

    public Recording getRecordingAtPosition(int position)
    {
        return recordings.get(position);
    }

    public void edit(int position, String name, String notes, String whichImage)
    {
        readData();
        Recording r = recordings.get(position);
        r.setName(name);
        r.setNotes(notes);
        r.setImage(whichImage);
        writeData();
    }
    public List<Recording> getListCopy()
    {
        readData();
        return recordings;
    }

    private void readData()
    {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Gson gson = new Gson();
        jsonRecordings = appSharedPrefs.getString("RecordingsListJSON", "");
        if(!jsonRecordings.equals(""))
        {
            Type type = new TypeToken<List<Recording>>(){}.getType();
            recordings = gson.fromJson(jsonRecordings, type);
        }


    }

    private void writeData()
    {
        Gson gson = new Gson();

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        String jsonRecordings = gson.toJson(recordings);

        prefsEditor.putString("RecordingsListJSON", jsonRecordings);
        prefsEditor.commit();

    }

    public void sortListName()
    {
    readData();
    }

    public void sortListDate()
    {
        readData();

    }

    public void sortListTime()
    {
        readData();

    }

    public void sortListDuration()
    {
        readData();

    }

    public void sortListImageType()
    {
        readData();

    }
}


