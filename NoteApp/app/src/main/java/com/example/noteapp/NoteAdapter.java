package com.example.noteapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position, @NonNull Note note) {
        holder.note_title_text_view.setText(note.title);
        holder.note_content_text_view.setText(note.content);
        holder.note_timestamp_text_view.setText(Utility.timestampToString(note.timestamp));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteDetailsActivity.class);
                intent.putExtra("title", note.title);
                intent.putExtra("content", note.content);

                String docId = getSnapshots().getSnapshot(position).getId();
                intent.putExtra("docId", docId);
                context.startActivity(intent);

            }
        });




    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView note_title_text_view, note_content_text_view, note_timestamp_text_view;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            note_title_text_view = itemView.findViewById(R.id.note_title_text_view);
            note_content_text_view = itemView.findViewById(R.id.note_content_text_view);
            note_timestamp_text_view = itemView.findViewById(R.id.note_timestamp_text_view);
        }
    }
}
