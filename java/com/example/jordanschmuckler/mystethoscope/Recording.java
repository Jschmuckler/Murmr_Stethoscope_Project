package com.example.jordanschmuckler.mystethoscope;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Jordan Schmuckler on 4/12/2015.
 */
public class Recording implements Serializable {

    private String name;
    private String duration;
    private String date;
    private String notes;
    private String timeMade;
    private String whichImage;
    private String soundLoc;


    public Recording() {
        //this.name = "nameTest";
        //this.duration = "durationTest";
        //this.date = "dateTest";
        //this.notes = "notesTest";
       // this.whichImage = "HEART";
    }


    @Override
    public String toString()
    {
        String name = ("Name: " + this.name);
        String duration = ("Duration: " + this.duration);
        String date = ("Date: " + this.date);
        String time = ("Time: " + this.timeMade);
        String notes = ("Notes: " + this.notes);
        String data = (name + "\n" + duration + "\n" + date + "\n" + time + "\n" + notes);
        return data;
    }

    public String getImageName() {return this.whichImage;}

    public String getName(){return this.name;}

    public String getDuration(){return this.duration;}

    public String getDate(){return this.date;}

    public String getNotes(){return this.notes;}

    public String getTimeMade(){return this.timeMade;}

    public String getSoundLoc(){return this.soundLoc;}

    public void setName(String name){this.name = name;}

    public void setDuration(String duration){this.duration = duration;}

    public void setDate(String date){this.date = date;}

    public void setNotes(String notes){this.notes = notes;}

    public void setImage(String whichImage){this.whichImage = whichImage;}

    public void setTimeMade(String timeMade){this.timeMade = timeMade;}

    public void setSoundLoc(String soundLoc){this.soundLoc = soundLoc;}
}

//name, date, time, duration, type
    class ComparatorName implements Comparator<Recording>
{

    @Override
    public int compare(Recording lhs, Recording rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }
}


class ComparatorDate implements Comparator<Recording>
{

    @Override
    public int compare(Recording lhs, Recording rhs) {
        return 0;
    }
}

class ComparatorTime implements Comparator<Recording>
{

    @Override
    public int compare(Recording lhs, Recording rhs) {
        return 0;
    }
}

class ComparatorDuration implements Comparator<Recording>
{

    @Override
    public int compare(Recording lhs, Recording rhs) {
        return 0;
    }
}

class ComparatorOrgan implements Comparator<Recording>
{

    @Override
    public int compare(Recording lhs, Recording rhs) {



        return 0;
    }
}