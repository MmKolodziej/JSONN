package com.notexample.jsonn;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends ListActivity {
    private ListView list;
    private int currentPage=1;
    private String pageUrl="http://simpleblog-api.herokuapp.com/posts";
    private String pageUrlBase="http://simpleblog-api.herokuapp.com/posts?page=";
    ArrayList<Post> Post2;
    private Button MOAR;
    private PostAdapter adapter;
    private ArrayList<String> idList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Post2=new ArrayList<Post>();
        idList=new ArrayList<String>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        list = (ListView)findViewById(android.R.id.list);
        MOAR = new Button(this);
        MOAR.setText("MOAR STUFF");
        list.addFooterView(MOAR);
        MOAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage++;
                pageUrl=pageUrlBase.concat(Integer.toString(currentPage));
                addPage();
            }
        });
        addPage();
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(v.getContext(), CommentActivity.class);
        //intent.putExtra("postId", idList.get(position));
        intent.putExtra("postId",idList.get(position));
        startActivity(intent);
    }

    public void addPage(){
        String readTwitterFeed = readJson();
        try {
            JSONArray jsonArray = new JSONArray(readTwitterFeed);
            Log.i(MainActivity.class.getName(),
                    "Number of entries " + jsonArray.length());
            Post2.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.i(MainActivity.class.getName(), jsonObject.getString("body"));
                Post2.add(new Post(jsonObject.getString("title"),jsonObject.getString("created_at"),jsonObject.getString("body")));
                idList.add(jsonObject.getString("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(currentPage==1){
            adapter = new PostAdapter(this, R.layout.listview_posts, Post2);
            setListAdapter(adapter);
    }
        else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0; i<10; i++)
                        adapter.add(Post2.get(i));
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public String readJson() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(pageUrl);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}