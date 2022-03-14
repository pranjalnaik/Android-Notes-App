package com.pjnaik.androidnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private Notes notes;

    EditText body;
    EditText title;
    Intent text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = (EditText)findViewById(R.id.notetitle);
        body = (EditText)findViewById(R.id.notetext);
        body.setMovementMethod(new ScrollingMovementMethod());

        text = getIntent();
        notes = (Notes) text.getSerializableExtra("Notes");

        if(notes != null) {
            title.setText(notes.getTitle());
            body.setText(notes.getBody());
        }
    }

    protected void onResume() {
        super.onResume();
        return;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newnote_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save_menu) {
            notes.setTitle(title.getText().toString());
            notes.setBody(body.getText().toString());
            notes.setDate(getCurrentTime());

            text.putExtra("noteOBJ", notes);
            setResult(RESULT_OK, text);
            finish();
        }
        return false;
    }

    protected void onPause(){
        notes.setDate(getCurrentTime());
        super.onPause();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notes.setTitle(title.getText().toString());
                notes.setBody(body.getText().toString());
                notes.setDate(getCurrentTime());

                text.putExtra("noteOBJ", notes);
                setResult(RESULT_OK, text);
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        String newLine = System.getProperty("line.separator");
        builder.setMessage("Your note is not saved!" + newLine +  "Save note '" + title.getText().toString() + "'?");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String getCurrentTime() {
        return new SimpleDateFormat("EEE MMM  d, HH:mm a").format(Calendar.getInstance().getTime());
    }
}