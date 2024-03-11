package com.example.personalvocab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import io.grpc.Context;

public class WordAdapter extends FirestoreRecyclerAdapter<Word,WordAdapter.WordViewHolder> {
Context context;


    public WordAdapter(@NonNull FirestoreRecyclerOptions<Word> options, MainActivity context) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WordViewHolder holder, int position, @NonNull Word word) {
holder.TitleTextview.setText(word.soz);
holder.ContentTextview.setText(word.kontent);
holder.TimestampTextview.setText(Utility.timestampToString(word.timestamp));



    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_wor_item,parent,false);

return  new WordViewHolder(view);
    }

    class WordViewHolder extends RecyclerView.ViewHolder{

        TextView TitleTextview,ContentTextview,TimestampTextview;
        public WordViewHolder(@NonNull View itemView) {
            super(itemView);

            TitleTextview = itemView.findViewById(R.id.word_title_text_view);
            ContentTextview = itemView.findViewById(R.id.word_content_text_view);
            TitleTextview = itemView.findViewById(R.id.word_timestamp_text_view);
        }
    }

}
