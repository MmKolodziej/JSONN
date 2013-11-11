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
public class CommentAdapter extends ArrayAdapter<Comment> {

    Context context;
    int layoutResourceId;
    ArrayList<Comment> data = null;

    public CommentAdapter(Context context, int layoutResourceId, ArrayList<Comment> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CommentHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CommentHolder();
            holder.firstname = (TextView)row.findViewById(R.id.firstname);
            holder.lastname= (TextView)row.findViewById(R.id.lastname);
            holder.body = (TextView)row.findViewById(R.id.body);
            holder.date = (TextView)row.findViewById(R.id.date);

            row.setTag(holder);
        }
        else
        {
            holder = (CommentHolder)row.getTag();
        }

        Comment Comment = data.get(position);
        holder.firstname.setText(Comment.firstname);
        holder.lastname.setText(Comment.lastname);
        holder.body.setText(Comment.body);
        holder.date.setText(Comment.date);

        return row;
    }

    static class CommentHolder
    {
        TextView firstname;
        TextView lastname;
        TextView body;
        TextView date;
    }
}