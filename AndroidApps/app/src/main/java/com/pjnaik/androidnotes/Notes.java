package com.pjnaik.androidnotes;

import java.io.Serializable;

public class Notes implements Serializable {

    private String title;
    private String body;
    private String date;
    private static int counter = 0;

    public Notes() {
        ++counter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter)
    {
        this.counter = counter;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", description='" + body + '\'' +
                ", counter=" + counter +
                '}';
    }
}
