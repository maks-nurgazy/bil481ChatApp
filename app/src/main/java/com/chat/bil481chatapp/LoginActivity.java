package com.chat.bil481chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout stubContainer;
    private boolean isLoginPage = false;
    private View activePage;

    public void redirectIfLoggedIn(){
        if (ParseUser.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void login(View view){
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e==null){
                    redirectIfLoggedIn();
                }
                else{
                    String message = e.getMessage();
                    assert message != null;
                    if (message.toLowerCase().contains("java")){
                        message = e.getMessage().substring(e.getMessage().indexOf(" "));
                    }
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void signUp(View view){

        EditText etEmail = findViewById(R.id.etRegEmail);
        EditText etLanguage = findViewById(R.id.etRegName);
        EditText etPhone = findViewById(R.id.etRegPhone);
        EditText etPassword = findViewById(R.id.etRegPassword);

        String email = etEmail.getText().toString().trim();
        String language = etLanguage.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        ParseUser user = new ParseUser();
        user.put("language",language);
        user.setUsername(email);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    redirectIfLoggedIn();
                }else{
                    String message = e.getMessage();
                    assert message != null;
                    if (message.toLowerCase().contains("java")){
                        message = e.getMessage().substring(e.getMessage().indexOf(" "));
                    }
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        stubContainer = findViewById(R.id.stubContainer);
        togglePage(activePage);

        redirectIfLoggedIn();

    }


    public void togglePage(View view) {
        if (activePage!=null){
            stubContainer.removeView(activePage);
        }
        if (!isLoginPage){
            activePage = getLayoutInflater().inflate(R.layout.layout_login,null);
            isLoginPage = true;
        }else{
            activePage = getLayoutInflater().inflate(R.layout.layout_register,null);
            isLoginPage = false;
        }
        stubContainer.addView(activePage);
    }


}