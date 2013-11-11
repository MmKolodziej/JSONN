package com.notexample.jsonn;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by marcin on 16.08.13.
 */
public class PostAdapter extends ArrayAdapter<Post> {

    Context context;
    int layoutResourceId;
    ArrayList<Post> data = null;
    /*Post data[] = null;

    public PostAdapter(Context context, int layoutResourceId, Post[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }*/
    public PostAdapter(Context context, int layoutResourceId, ArrayList<Post> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PostHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PostHolder();
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.body = (TextView)row.findViewById(R.id.body);
            holder.date = (TextView)row.findViewById(R.id.date);

            row.setTag(holder);
        }
        else
        {
            holder = (PostHolder)row.getTag();
        }

        Post post = data.get(position);
        holder.title.setText(post.title);
        holder.body.setText(post.body);
        holder.date.setText(post.date);

        return row;
    }

    static class PostHolder
    {
        TextView title;
        TextView body;
        TextView date;
    }
}