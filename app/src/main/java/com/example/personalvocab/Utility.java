package com.example.personalvocab;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    Utility() {
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private static Utility instance;

    public static Utility getInstance() {
        if (instance == null) {
            instance = new Utility();
        }
        return instance;
    }

    CollectionReference getCollectionReferenceToWords() {

        return firebaseFirestore.collection("words").document(firebaseUser.getUid())
                .collection("my_words");
    }

    String timestampToString(Timestamp timestamp) {
        if (timestamp == null) return "";
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}
