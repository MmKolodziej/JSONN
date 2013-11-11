package com.notexample.jsonn;

/**
 * Created by Marcin on 8/18/13.
 */
public class Comment {
    public String date;
    public String body;
    public String firstname;
    public String lastname;
    public Comment(){
        super();
    }

    public Comment(String firstname, String date, String body, String lastname) {
        super();
        this.firstname=firstname;
        this.date=date;
        this.body=body;
        this.lastname=lastname;
    }
}
