package com.pjnaik.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView date;
    public TextView description;

    public ViewHolder(View view) {
        super(view);

        title = (TextView)view.findViewById(R.id.notetitle);
        date = (TextView)view.findViewById(R.id.datetime);
        description = (TextView)view.findViewById(R.id.notetext);
    }
}