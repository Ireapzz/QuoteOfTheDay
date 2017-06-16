package com.example.dtixador.quoteoftheday;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_quote);

        String quoteListAsString = getIntent().getStringExtra("list_as_string");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Quote>>(){}.getType();
        ArrayList<Quote> quotesList = gson.fromJson(quoteListAsString, type);

        QuotesBDD quotesBDD = new QuotesBDD(MainActivity.this);
        quotesBDD.open();
        quotesBDD.deleteAllQuotes();

        //add newQuote
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.addQuote);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddQuote.class), 1000);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyAdapter(quotesList, quotesBDD, SplashScreen.chkStatus()));
    }


}
