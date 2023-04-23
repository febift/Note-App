package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText notes_title_text, notes_content_text;
    ImageButton save_note_btn, delete_note_text_view;
    TextView page_title;
    String docId;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        notes_title_text = findViewById(R.id.notes_title_text);
        notes_content_text = findViewById(R.id.notes_content_text);
        save_note_btn = findViewById(R.id.save_note_btn);
        page_title = findViewById(R.id.page_title);
        delete_note_text_view = findViewById(R.id.delete_note_text_view_btn);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null){
            isEditMode = true;
        }



        notes_title_text.setText(title);
        notes_content_text.setText(content);
        if (isEditMode){
            page_title.setText("Edit your notes");
            delete_note_text_view.setVisibility(View.VISIBLE);
        }


        save_note_btn.setOnClickListener( (v) -> saveNote());

        delete_note_text_view.setOnClickListener( (v) -> deleteNoteFromFirebase());
    }



    public void saveNote(){
        String noteTitle = notes_title_text.getText().toString();
        String noteContent = notes_content_text.getText().toString();

        if (noteTitle.isEmpty()){
            notes_title_text.setError("Title Can't Be Empty");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);

    }

    public void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if (isEditMode){
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
            Toast.makeText(this, "Notes Edited Succesfully", Toast.LENGTH_SHORT).show();
        }else{
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.showToast(NoteDetailsActivity.this, "Notes Added Succesfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this, "Notes Can't Be Added");
                }
            }
        });

    }

    public void deleteNoteFromFirebase(){

        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Utility.showToast(NoteDetailsActivity.this, "Notes Deleted Succesfully");
                        finish();
                    }else{
                        Utility.showToast(NoteDetailsActivity.this, "Notes Can't Be Deleted");
                    }
                }
            });

        }

    }