package com.example.journalpersonnel;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Test de la ListView
        ArrayList<String> entrees = new ArrayList<>();
        entrees.add("Test 1 - 23/01/2026");
        entrees.add("Test 2 - Travail");
        entrees.add("Test 3 - Voyage");

        ListView listView = findViewById(R.id.listViewJournal);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_journal,
                R.id.tvItem,
                entrees
        );
        listView.setAdapter(adapter);
    }
}
