package com.example.personalvocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton mAddFab, AddWordFab, mAddPersonFab;
    TextView addAlarmActionText, addPersonActionText;
RecyclerView recyclerView;
ImageButton menubtn;

WordAdapter wordAdapter;
    Boolean isAllFabsVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAddFab = findViewById(R.id.add_fab);

        AddWordFab = findViewById(R.id.add_word_fab);
        mAddPersonFab = findViewById(R.id.add_folder_fab);

recyclerView = findViewById(R.id.recycler_view);
setupRecyclerView();

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

                AddWordFab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,WordDetails.class) ));
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

    }

    void setupRecyclerView(){

        Query query = Utility.getCollectionReferenceToWords().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Word> options = new FirestoreRecyclerOptions.Builder<Word>()
                .setQuery(query,Word.class).build();
recyclerView.setLayoutManager(new LinearLayoutManager(this));
wordAdapter = new WordAdapter(options,this);
recyclerView.setAdapter(wordAdapter);

    }
/*
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
    protected void onResume() {
        super.onResume();
        wordAdapter.notifyDataSetChanged();
    }*/
}





