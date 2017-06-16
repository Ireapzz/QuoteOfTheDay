package com.example.dtixador.quoteoftheday;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private static Context mContext;
    ArrayList<Quote> arrayOfQuote = new ArrayList<>();

    private enum Network {
        NO_SERVICE,
        WIFI,
        MOBILE_NETWORK
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = SplashScreen.this;


        /// CHECK MOBILE CONNECTION

        if (chkStatus().equals("NO_SERVICE")) {
            Toast.makeText(SplashScreen.this, "No Connexion", Toast.LENGTH_LONG).show();

            final QuotesBDD quotesBDD = new QuotesBDD(SplashScreen.this);
            quotesBDD.open();

            arrayOfQuote = quotesBDD.getAllQuotes();

        } else {


            final FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Get a reference to the todoItems child items it the database
            final DatabaseReference myRef = database.getReference("quotes");

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String value = null;
                    HashMap<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                    Quote newTask = new Quote();

                    newTask.setmAuthor(dataSnapshot.getKey());
                    newTask.setFav(false);
                    for (Map.Entry mapentry : td.entrySet()) {
                        if (mapentry.getKey().equals("quote")) {
                            value = mapentry.getValue().toString();
                            newTask.setmQuote(value);
                        }
                        if (mapentry.getKey().equals("like")) {
                            newTask.setLike(Integer.parseInt(mapentry.getValue().toString()));
                        }
                        if (mapentry.getKey().equals("dislike")) {
                            newTask.setDislike(Integer.parseInt(mapentry.getValue().toString()));
                        }
                    }
                    arrayOfQuote.add(newTask);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                    Log.w("TAG:", "Failed to read value.", databaseError.toException());
                }
            });
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Gson gson = new Gson();

                final String jsonQuotes = gson.toJson(arrayOfQuote);
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                i.putExtra("list_as_string", jsonQuotes);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }

        }, SPLASH_TIME_OUT);
    }

    static String chkStatus() {
        String isConnect;

        final ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnectedOrConnecting ()) {
            isConnect = String.valueOf(Network.WIFI);
        } else if (mobile.isConnectedOrConnecting ()) {
            isConnect = String.valueOf(Network.MOBILE_NETWORK);
        } else {
            isConnect = String.valueOf(Network.NO_SERVICE);
        }
        return isConnect;
    }

}
