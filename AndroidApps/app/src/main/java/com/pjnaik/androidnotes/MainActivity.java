package com.pjnaik.androidnotes;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import android.os.Bundle;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<Notes> notesArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter noteAdapter;
    private int position = -1;
    private Notes notes;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        noteAdapter = new NotesAdapter(this, notesArrayList);
        recyclerView.setAdapter(noteAdapter);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);

        int totalNotes = notesArrayList.size();
        if (totalNotes != 0)
            setTitle("Android Notes" + " (" + totalNotes + ")");
        else
            setTitle("Android Notes");

        Note Note = new Note(this);
        Note.execute(getString(R.string.filename));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    public void fetchAsyncTaskData(ArrayList<Notes> arrayList) {
        notesArrayList = arrayList;
        noteAdapter.notesrefresh(notesArrayList);

    }

    private static final int NEWNOTE_REQUEST_CODE = 1;
    private static final int MODIFY_REQUEST_CODE = 2;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.info_menu) {
            Intent infoIntent = new Intent(this, AboutActivity.class);
            startActivity(infoIntent);
        } else if (item.getItemId() == R.id.add_menu) {
            int totalNotes = notesArrayList.size();
            if (totalNotes != 0)
                setTitle("Android Notes" + " (" + totalNotes + ")");
            else
                setTitle("Android Notes");
            Intent addNoteIntent = new Intent(this, EditActivity.class);
            notes = new Notes();
            addNoteIntent.putExtra("Notes", notes);
            startActivityForResult(addNoteIntent, NEWNOTE_REQUEST_CODE);
        }
        return true ;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int totalNotes = notesArrayList.size();
        if (totalNotes != 0)
            setTitle("Android Notes" + " (" + totalNotes + ")");
        else
            setTitle("Android Notes");

        super.onActivityResult(requestCode, resultCode, data);

        Intent addNoteIntent = new Intent(this, EditActivity.class);
        notes = new Notes();

        if (requestCode == NEWNOTE_REQUEST_CODE) {

            totalNotes = notesArrayList.size();
            if (totalNotes != 0)
                setTitle("Android Notes" + " (" + totalNotes + ")");
            else
                setTitle("Android Notes");

            if (resultCode == RESULT_OK) {
                notes = (Notes) data.getSerializableExtra("noteOBJ");

                try {
                    if (notes.getTitle().equals("")) {
/*                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                        builder.setMessage("Note needs a title to be saved. Want to add title ?");
                        AlertDialog dialog = builder.create();
                        dialog.show();
*/
                        Toast.makeText(this, "Un-titled activity was not saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        loadFile(notesArrayList, false, false);
                        totalNotes = notesArrayList.size();
                        if (totalNotes != 0)
                            setTitle("Android Notes" + " (" + totalNotes + ")");
                        else
                            setTitle("Android Notes");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                noteAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == MODIFY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                notes = (Notes) data.getSerializableExtra("noteOBJ");

                try {
                    notesArrayList.set(this.position, notes);
                    loadFile(notesArrayList, true, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                noteAdapter.notifyDataSetChanged();
            }
        }

    }

    private void loadFile(ArrayList<Notes> notesList, Boolean isDelete, Boolean isNoteEdited) throws IOException {

        int totalNotes = notesArrayList.size();
        if (totalNotes != 0)
            setTitle("Android Notes" + " (" + totalNotes + ")");
        else
            setTitle("Android Notes");
        try {
            if(notesList != null) {
                notesArrayList = notesList;
            }

            if (notes != null && (!isDelete || !isNoteEdited)) {
                notesArrayList.add(notes);
            }

            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(getString(R.string.filename), Context.MODE_PRIVATE);
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(fileOutputStream, getString(R.string.encoding)));
            StringWriter stringWriter = new StringWriter();
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();

            for (int i = 0; i < notesArrayList.size(); ++i) {
                jsonWriter.beginObject();
                jsonWriter.name("title").value((notesArrayList.get(i).getTitle()));
                jsonWriter.name("date").value((notesArrayList.get(i).getDate()));
                jsonWriter.name("body").value(notesArrayList.get(i).getBody());
                jsonWriter.endObject();
            }

            jsonWriter.endArray();
            jsonWriter.close();

            jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();

            for (int i = 0; i < notesArrayList.size(); ++i) {
                jsonWriter.beginObject();
                jsonWriter.name("title").value((notesArrayList.get(i).getTitle()));
                jsonWriter.name("date").value((notesArrayList.get(i).getDate()));
                jsonWriter.name("body").value(notesArrayList.get(i).getBody());
                jsonWriter.endObject();
            }

            jsonWriter.endArray();
            jsonWriter.close();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this,"No file present", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doDelete(View view, int position) throws IOException {
        int totalNotes = notesArrayList.size();
        if (totalNotes != 0)
            setTitle("Android Notes" + " (" + totalNotes + ")");
        else
            setTitle("Android Notes");
        notesArrayList.remove(position);
        loadFile(notesArrayList,true,true);
        noteAdapter.notifyDataSetChanged();
    }

    public void doModify(View view, int position) {
        notes = notesArrayList.get(position);
        this.position = position;
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("Notes", notes);
        startActivityForResult(intent, MODIFY_REQUEST_CODE);
    }

    @Override
    protected void onPause() {
        int totalNotes = notesArrayList.size();
        if (totalNotes != 0)
            setTitle("Android Notes" + " (" + totalNotes + ")");
        else
            setTitle("Android Notes");
        try {
            loadFile(notesArrayList,true,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    public ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return activityResultLauncher;
    }

    public void setActivityResultLauncher(ActivityResultLauncher<Intent> activityResultLauncher) {
        this.activityResultLauncher = activityResultLauncher;
    }
}