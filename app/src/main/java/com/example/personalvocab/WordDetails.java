package com.example.personalvocab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
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


public class WordDetails extends AppCompatActivity {
    private EditText titleEdittext, contentEdittext;
    private ImageButton savewordbtn;

    private Utility utility;

    TextView pagetitleview,deletewordtxt;
    String title,content,docId;

    boolean isEdit =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);

        titleEdittext = findViewById(R.id.words_title_text);
        contentEdittext = findViewById(R.id.words_content_text);
        savewordbtn = findViewById(R.id.save_word_btn);
        deletewordtxt =findViewById(R.id.delete_word_txt);
        //page title
        pagetitleview = findViewById(R.id.page_title);

        //recieve data from firebase
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

if (docId != null && !docId.isEmpty()){
    isEdit =true;
}
titleEdittext.setText(title);
contentEdittext.setText(content);
if (isEdit){
    pagetitleview.setText("Edit your word");
    deletewordtxt.setVisibility(View.VISIBLE);

}

        savewordbtn.setOnClickListener(v -> saveWord());
 deletewordtxt.setOnClickListener(v -> DeleteWordFromFirebas());

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
        if (isEdit){
            //update word
            documentReference = utility.getCollectionReferenceToWords().document(docId);
        }else {
            //create new word
            documentReference = utility.getCollectionReferenceToWords().document();
        }
        documentReference.set(word).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", word);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                } else {

                    Toast.makeText(WordDetails.this, "Error while adding words!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
void DeleteWordFromFirebas(){
    DocumentReference documentReference;
        documentReference = utility.getCollectionReferenceToWords().document(docId);
    documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

            if (task.isSuccessful()) {
                Toast.makeText(WordDetails.this, "Words deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {

                Toast.makeText(WordDetails.this, "Error while deleting words!", Toast.LENGTH_SHORT).show();
            }
        }
    });
}

}