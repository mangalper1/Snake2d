package com.example.snake2d;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Marcador extends AppCompatActivity {
    public RemoteMongoCollection<Document> datos;
    ResourceBundle resources;
    StitchAppClient stitchClient;
    List<Document> items = new ArrayList<>();
    StitchAppClient client =
            Stitch.initializeDefaultAppClient("snake2d-deosi");

    RemoteMongoClient mongoClient =
            client.getServiceClient(RemoteMongoClient.factory, "snake");

    RemoteMongoCollection<Document> coll =
            mongoClient.getDatabase("snake").getCollection("snake");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcador);

        //Se conecta a la base de datos y coge lo que hay


        //datos = coll;

        client.getAuth().loginWithCredential(new AnonymousCredential());


        //InsertInto

        /*
        Document newItem = new Document()
                .append("nombre", "vacío")
                .append("puntos", 50);


        final Task <RemoteInsertOneResult> insertTask = coll.insertOne(newItem);
        insertTask.addOnCompleteListener(new OnCompleteListener <RemoteInsertOneResult> () {
            @Override
            public void onComplete(@NonNull Task <RemoteInsertOneResult> task) {
                if (task.isSuccessful()) {
                    Log.d("app", String.format("successfully inserted item with id %s",
                            task.getResult().getInsertedId()));
                } else {
                    Log.e("app", "failed to insert document with: ", task.getException());
                }
            }
        });
        */



        //FIND
        Document filterDoc = new Document()
                .append("reviews.0", new Document().append("$exists", true));

        RemoteFindIterable findResults = coll
                .find(filterDoc);

        Task<List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
        itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    items = task.getResult();
                    Log.d("app", String.format("successfully found %d documents", items.size()));
                    for (Document item: items) {
                        Log.d("app", String.format("successfully found:  %s", item.toString()));
                    }
                } else {
                    Log.e("app", "failed to find documents with: ", task.getException());
                }
            }
        });


    }
}
