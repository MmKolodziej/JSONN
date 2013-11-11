package com.notexample.jsonn;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Marcin on 8/18/13.
 */
public class RegisterActivity extends Activity {
    EditText password, email, firstname, lastname, birthday;
    Button registerButton;
    JSONObject user, users;
    LinearLayout layout;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        birthday = (EditText) findViewById(R.id.birthday);
        registerButton = (Button) findViewById(R.id.registerButton);
        layout = (LinearLayout) findViewById(R.id.layout);
        user = new JSONObject();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    user.put("birthday", birthday.getText().toString());
                    user.put("lastname", lastname.getText().toString());
                    user.put("firstname", firstname.getText().toString());
                    user.put("email", email.getText().toString());
                    user.put("password", password.getText().toString());
                    users = new JSONObject();
                    users.put("user", user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendRegisterRequest();
            }
        });

    }

    public void sendRegisterRequest() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://simpleblog-api.herokuapp.com/users");
        try {
            StringEntity se = new StringEntity(users.toString());
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 201) {
                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 1500);
            } else {
                Toast.makeText(getApplicationContext(), "Registration failed!", Toast.LENGTH_SHORT).show();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}