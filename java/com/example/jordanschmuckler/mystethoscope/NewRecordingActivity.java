package com.example.jordanschmuckler.mystethoscope;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class NewRecordingActivity extends Activity {

    String[] organs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_new_recording);

        Spinner spinner = (Spinner)findViewById(R.id.SpinnerOrgans);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Organs, R.layout.spinnerlayout);
        spinner.setAdapter(adapter);


        Intent originalIntent = getIntent();
        if(originalIntent.getIntExtra("isDataDisplay",0) == 1) {


            originalIntent.getIntExtra("position", 0);

            originalIntent.getStringExtra("notes");
            EditText firstName = (EditText) findViewById(R.id.Name);
            EditText notes = (EditText) findViewById(R.id.editNotes);
            firstName.setText(originalIntent.getStringExtra("name"));
            notes.setText(originalIntent.getStringExtra("notes"));

            String whichImage = originalIntent.getStringExtra("organ");

            switch (whichImage) {
                case "HEART":
                    spinner.setSelection(adapter.getPosition("Heart"));

                    break;

                case "LUNGS":
                    spinner.setSelection(adapter.getPosition("Lungs"));

                    break;

                default:
                    spinner.setSelection(adapter.getPosition("Other"));
                    break;
            }

        }

    }

    public void cancelButtonListener(View view)
    {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    public void saveButtonListener(View view)
    {
        Intent returnIntent = new Intent();

        Intent originalIntent = getIntent();


        EditText Name = (EditText)findViewById(R.id.Name);

        EditText notes = (EditText)findViewById(R.id.editNotes);

        String organ = ((Spinner)findViewById(R.id.SpinnerOrgans)).getSelectedItem().toString();
        String fullNameText = Name.getText().toString();
        String notesText = notes.getText().toString();

        returnIntent.putExtra("position",originalIntent.getIntExtra("position",-1));
        returnIntent.putExtra("name",fullNameText);
        returnIntent.putExtra("notes",notesText);
        returnIntent.putExtra("soundLoc",originalIntent.getStringExtra("soundLoc"));
        switch (organ)
        {
            case "Heart":
                returnIntent.putExtra("organ","HEART");
                break;

            case "Lungs":
                returnIntent.putExtra("organ","LUNGS");
                break;

            default:
                returnIntent.putExtra("organ","QUESTIONMARK");
                break;
        }

        returnIntent.putExtra("duration",originalIntent.getStringExtra("duration"));
        returnIntent.putExtra("date",originalIntent.getStringExtra("date"));
        returnIntent.putExtra("time",originalIntent.getStringExtra("time"));


        setResult(RESULT_OK,returnIntent);

        finish();

    }


}
