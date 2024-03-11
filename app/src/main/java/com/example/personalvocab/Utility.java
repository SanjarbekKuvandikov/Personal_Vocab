package com.example.personalvocab;

import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import io.grpc.Context;

public class Utility {


static CollectionReference getCollectionReferenceToWords(){
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
     return
             FirebaseFirestore.getInstance()
                     .collection("words").document(firebaseUser.getUid())
                     .collection("my_words");
}

static String timestampToString(Timestamp timestamp){
return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
}
}
