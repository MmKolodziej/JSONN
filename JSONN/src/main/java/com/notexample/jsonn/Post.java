package com.notexample.jsonn;

/**
 * Created by marcin on 16.08.13.
 */
public class Post {
    public String title;
    public String date;
    public String body;
    public Post(){
        super();
    }

    public Post(String title, String date, String body) {
        super();
        this.title=title;
        this.date=date;
        this.body=body;
    }
}