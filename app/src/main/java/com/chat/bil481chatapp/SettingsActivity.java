package com.chat.bil481chatapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Translate translate;

    List<Language> languages;
    boolean firstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getTranslateService();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        if (firstTime) {
            firstTime = false;
        } else {
            Toast.makeText(getApplicationContext(), "Selected language: " + languages.get(position).getName(), Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().put("language", languages.get(position).getCode());
            ParseUser.getCurrentUser().saveInBackground();
        }

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

            String activeLanguage = (String) ParseUser.getCurrentUser().get("language");
            int pos = 0;
            for (int i = 0; i < size; i++) {
                lang[i] = languages.get(i).getName();
                if (activeLanguage != null && activeLanguage.equals(languages.get(i).getCode())) {
                    pos = i;
                }
            }

            Spinner spin = findViewById(R.id.spinner1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lang);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
            spin.setOnItemSelectedListener(this);
            spin.setSelection(pos);

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

}