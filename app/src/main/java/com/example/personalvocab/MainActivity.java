package com.example.personalvocab;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 346;
    TextView foremail;

    private Utility utility;
    private FloatingActionButton mAddFab, AddWordFab, mAddPersonFab;
    private TextView addAlarmActionText, addPersonActionText;
    private RecyclerView recyclerView;
    private ImageButton menubtn;

    private FirestoreRecyclerAdapter wordAdapter;
    Boolean isAllFabsVisible;
    //
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if ((o)){
                Toast.makeText(MainActivity.this,"Post notification permission granted",Toast.LENGTH_SHORT).show();
            }
        }
    });
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //emailread
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        FirebaseApp.initializeApp(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }
        //emailread

        //Drawer Layout
        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        findViewById(R.id.imageMenu).setOnClickListener((v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        }));
        //drawer layout
        NavigationView navigationView = findViewById(R.id.design_navigation_view);
        View headerview = navigationView.getHeaderView(0);
        foremail = (TextView) headerview.findViewById(R.id.email_text);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = String.valueOf(user.getEmail());
        foremail.setText(userEmail);


        navigationView.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
            if (item.getItemId() == R.id.nav_about) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About us");
                builder.setMessage("Author : Sanjarbek Kuvandikov" + "         " +
                        "Version : 1.0.0");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            if (item.getItemId() == R.id.nav_howit) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+JH64g-bYVyQ1YTcy    "));
                startActivity(intent);
            }
            if (item.getItemId() == R.id.nav_home) {

                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
            if (item.getItemId() == R.id.nav_share) {
                // Create sendIntent
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Personal Vocabulary");
                sendIntent.setType("text/plain");

                // Create shareIntent with chooser
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
            drawerLayout.close();
            item.setCheckable(false);
            return true;
        });

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
                view -> Toast.makeText(MainActivity.this, "Folder will be dded 1.2.0 version", Toast.LENGTH_SHORT
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
    private void getWordList() {
        Query query = utility.getCollectionReferenceToWords().orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Word> response = new FirestoreRecyclerOptions.Builder<Word>()
                .setQuery(query, Word.class)
                .build();


        wordAdapter = new FirestoreRecyclerAdapter<Word, WordViewHolder>(response) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                notifyDataSetChanged();
                Log.d("TAG", "onDataChanged: ");
            }

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
                holder.itemView.setOnClickListener((v) -> {
                    Intent intent = new Intent(MainActivity.this, WordDetails.class);
                    String docId = this.getSnapshots().getSnapshot(position).getId();
                    intent.putExtra("title", word.soz);
                    intent.putExtra("content", word.kontent);
                    intent.putExtra("docId", docId);
                    startActivity(intent);
                });

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
        if (wordAdapter != null) {
            wordAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (wordAdapter != null) {
            wordAdapter.stopListening();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(MainActivity.this, "Words added successfully.", Toast.LENGTH_SHORT).show();
    }
}





