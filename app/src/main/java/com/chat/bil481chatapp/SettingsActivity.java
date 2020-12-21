package com.chat.bil481chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Translate translate;

    List<Language> languages;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getTranslateService();



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        Toast.makeText(getApplicationContext(),"Selected language: "+languages.get(position).getName(),Toast.LENGTH_SHORT).show();

        ParseUser.getCurrentUser().put("language",languages.get(position).getCode());
        ParseUser.getCurrentUser().saveInBackground();

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.bil481_chat_cloud_translation)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

            languages = translate.listSupportedLanguages();

            int size = languages.size();

            String[] lang = new String[size];

            for (int i=0;i<size;i++){
                lang[i] = languages.get(i).getName();
            }

            Spinner spin =  findViewById(R.id.spinner1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lang);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
            spin.setOnItemSelectedListener(this);

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

}