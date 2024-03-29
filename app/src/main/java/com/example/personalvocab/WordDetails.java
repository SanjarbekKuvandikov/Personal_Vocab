package com.example.personalvocab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class WordDetails extends AppCompatActivity {
    private EditText titleEdittext, contentEdittext;
    private ImageButton savewordbtn;

    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);

        titleEdittext = findViewById(R.id.words_title_text);
        contentEdittext = findViewById(R.id.words_content_text);
        savewordbtn = findViewById(R.id.save_word_btn);

        savewordbtn.setOnClickListener(v -> saveWord());

        utility = Utility.getInstance();
    }

    void saveWord() {
        String wordtitle = titleEdittext.getText().toString();
        String wordcontent = contentEdittext.getText().toString();
        if (wordtitle.isEmpty() || wordcontent.isEmpty()) {
            titleEdittext.setError("Title is required");
            return;
        }

        Word word = new Word();
        word.setSoz(wordtitle);
        word.setKontent(wordcontent);
        word.setTimestamp(Timestamp.now());

        saveWordToFirebase(word);
    }

    void saveWordToFirebase(Word word) {
        DocumentReference documentReference;
        documentReference = utility.getCollectionReferenceToWords().document();
        documentReference.set(word).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", word);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                } else {

                    Toast.makeText(WordDetails.this, "So`zlarni qo`shishda xatolik!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}