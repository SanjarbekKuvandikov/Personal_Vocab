package com.example.personalvocab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 346;


    private Utility utility;
    private FloatingActionButton mAddFab, AddWordFab, mAddPersonFab;
    private TextView addAlarmActionText, addPersonActionText;
    private RecyclerView recyclerView;
    private ImageButton menubtn;

    private FirestoreRecyclerAdapter wordAdapter;
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAddFab = findViewById(R.id.add_fab);

        AddWordFab = findViewById(R.id.add_word_fab);
        mAddPersonFab = findViewById(R.id.add_folder_fab);

        recyclerView = findViewById(R.id.recycler_view);

        // Also register the action name text, of all the FABs.
        addAlarmActionText = findViewById(R.id.add_word_action_text);
        addPersonActionText = findViewById(R.id.add_folder_action_text);

        // Now set all the FABs and all the action name texts as GONE
        AddWordFab.setVisibility(View.GONE);
        mAddPersonFab.setVisibility(View.GONE);
        addAlarmActionText.setVisibility(View.GONE);
        addPersonActionText.setVisibility(View.GONE);

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are invisible
        isAllFabsVisible = false;

        // We will make all the FABs and action name texts
        // visible only when Parent FAB button is clicked So
        // we have to handle the Parent FAB button first, by
        // using setOnClickListener you can see below
        mAddFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                // when isAllFabsVisible becomes true make all
                // the action name texts and FABs VISIBLE
                AddWordFab.show();
                mAddPersonFab.show();
                addAlarmActionText.setVisibility(View.VISIBLE);
                addPersonActionText.setVisibility(View.VISIBLE);

                AddWordFab.setOnClickListener(v -> startActivityForResult(new Intent(MainActivity.this, WordDetails.class), REQUEST_CODE));
                // make the boolean variable true as we
                // have set the sub FABs visibility to GONE
                isAllFabsVisible = true;
            } else {
                // when isAllFabsVisible becomes true make
                // all the action name texts and FABs GONE.
                AddWordFab.hide();
                mAddPersonFab.hide();
                addAlarmActionText.setVisibility(View.GONE);
                addPersonActionText.setVisibility(View.GONE);

                // make the boolean variable false as we
                // have set the sub FABs visibility to GONE
                isAllFabsVisible = false;
            }
        });
        mAddPersonFab.setOnClickListener(
                view -> Toast.makeText(MainActivity.this, "Folder Added", Toast.LENGTH_SHORT
                ).show());

        AddWordFab.setOnClickListener(
                view -> Toast.makeText(MainActivity.this, "Word Added", Toast.LENGTH_SHORT
                ).show());

        setupRecyclerView();
        getWordList();

    }

    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        utility = Utility.getInstance();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getWordList(){
        Query query = utility.getCollectionReferenceToWords().orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Word> response = new FirestoreRecyclerOptions.Builder<Word>()
                .setQuery(query, Word.class)
                .build();


        wordAdapter = new FirestoreRecyclerAdapter<Word, WordViewHolder>(response) {
            @Override
            public void onBindViewHolder(WordViewHolder holder, int position, Word word) {
                if (word != null) {
                    holder.TitleTextview.setText(word.soz);
                    holder.ContentTextview.setText(word.kontent);
                    holder.TimestampTextview.setText(utility.timestampToString(word.timestamp));
                } else {
                    // Handle null case gracefully, for example:
                    holder.TitleTextview.setText("");
                    holder.ContentTextview.setText("");
                    holder.TimestampTextview.setText("");
                }
            }

            @Override
            public WordViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.recycler_wor_item, group, false);

                return new WordViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT
                ).show();
            }
        };

        wordAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(wordAdapter);
    }

    class WordViewHolder extends RecyclerView.ViewHolder {

        TextView TitleTextview, ContentTextview, TimestampTextview;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);

            TitleTextview = itemView.findViewById(R.id.word_title_text_view);
            ContentTextview = itemView.findViewById(R.id.word_content_text_view);
            TimestampTextview = itemView.findViewById(R.id.word_timestamp_text_view);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        wordAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        wordAdapter.stopListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            wordAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,"So`zlar muaffaqiyatli qo`shildi.",Toast.LENGTH_SHORT).show();
        }
    }
}





