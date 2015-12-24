
package com.example.jordanschmuckler.mystethoscope;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.InputFilter;
import android.view.Gravity;
import android.widget.EditText;


public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FragmentManager manage = getFragmentManager();
        FragmentTransaction transact = manage.beginTransaction();
        OurPrefsFragment prefs = new OurPrefsFragment();
        transact.replace(android.R.id.content, prefs);
        transact.commit();


    }

    public static class OurPrefsFragment extends PreferenceFragment
    {
        SharedPreferences.OnSharedPreferenceChangeListener listener;
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);


        }
    }

}
