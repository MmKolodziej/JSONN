package com.notexample.jsonn;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.notexample.jsonn.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Marcin on 8/18/13.
 */
public class CommentActivity extends ListActivity {
    private String commentUrl = "http://simpleblog-api.herokuapp.com/posts/";
    private String userUrlBase = "http://simpleblog-api.herokuapp.com/users/";
    ArrayList<Comment> commentList;
    CommentAdapter adapter;
    private String postId;
    private String userUrl;
    private TextView textView;
    private EditText newCommentField;
    private ListView list;
    private Button addCommentButton;
    private String token;
    public static final String tokenFile = "MyPrefsFile";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        SharedPreferences settings = getSharedPreferences(tokenFile, 0);
        token = settings.getString("token", null);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        commentList=new ArrayList<Comment>();
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        commentUrl = commentUrl.concat(postId);
        textView = (TextView) findViewById(R.id.post);
        
        addCommentButton();
        readComments();
    }
    public void addCommentButton(){
        list = (ListView)findViewById(android.R.id.list);
        addCommentButton = new Button(this);
        addCommentButton.setText("Add a Comment");
        list.addFooterView(addCommentButton);
        if(token!=null)
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(token==null)
                    startActivity(new Intent(v.getContext(), LoginActivity.class));
                else
                    addComment();
            }
        });
    }
    public void addComment(){
        newCommentField = new EditText(this);
        list.removeFooterView(addCommentButton);
        list.addFooterView(newCommentField);
        addCommentButton();
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentHelper();
            }
        });
    }
    public void addCommentHelper(){
        String url = commentUrl.concat("/comments?auth_token=").concat(token);
        JSONObject commentBody = new JSONObject();
        JSONObject comment = new JSONObject();
        try{
            commentBody.put("body", newCommentField.getText().toString());
            comment.put("comment", commentBody);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            StringEntity se = new StringEntity(comment.toString());
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 201) {
                Toast.makeText(getApplicationContext(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                commentList.clear();
                list.removeFooterView(newCommentField);
                list.removeFooterView(addCommentButton);
                addCommentButton();
                readComments();
            } else {
                Toast.makeText(getApplicationContext(), "Comment adding failed!", Toast.LENGTH_SHORT).show();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readComments() {
        String comment = readJsonComment();
        try {
            JSONObject jsonObjectComment = new JSONObject(comment);
            textView.setText(jsonObjectComment.getString("body"));
            JSONArray jsonArrayComment = jsonObjectComment.getJSONArray("comments");
            Log.i(MainActivity.class.getName(),
                    "Number of entries " + jsonArrayComment.length());
            for (int i = 0; i < jsonArrayComment.length(); i++) {
                JSONObject jsonObject = jsonArrayComment.getJSONObject(i);
                Log.i(MainActivity.class.getName(), jsonObject.getString("body"));
                String user = readJsonUser(jsonObject.getInt("user_id"));
                String firstname, lastname;

                    JSONObject jsonObjectUser = new JSONObject(user);
                    firstname = jsonObjectUser.getString("firstname");
                    firstname.concat(" ");
                    lastname = jsonObjectUser.getString("lastname");

                commentList.add(new Comment(firstname, jsonObject.getString("created_at"), jsonObject.getString("body"), lastname));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new CommentAdapter(this, R.layout.listview_comments, commentList);
        setListAdapter(adapter);
    }
    public String readJsonComment() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(commentUrl);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(MainActivity.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    public String readJsonUser(int userId) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        userUrl=userUrlBase.concat(Integer.toString(userId));
        HttpGet httpGet = new HttpGet(userUrl);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(MainActivity.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}