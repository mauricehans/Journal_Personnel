package com.example.journalpersonnel;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> entrees;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private EditText etTitre, etContenu;
    private DatePicker datePicker;
    private CheckBox cbPerso, cbTravail, cbVoyage;
    private Button btnEnregistrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les vues
        etTitre = findViewById(R.id.etTitre);
        etContenu = findViewById(R.id.etContenu);
        datePicker = findViewById(R.id.datePicker);
        cbPerso = findViewById(R.id.cbPerso);
        cbTravail = findViewById(R.id.cbTravail);
        cbVoyage = findViewById(R.id.cbVoyage);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);
        listView = findViewById(R.id.listViewJournal);

        // Initialiser la liste
        entrees = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.item_journal, R.id.tvItem, entrees);
        listView.setAdapter(adapter);

        // Bouton Enregistrer
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enregistrerEntree();
            }
        });
    }

    private void enregistrerEntree() {
        String titre = etTitre.getText().toString().trim();
        String contenu = etContenu.getText().toString().trim();

        if (titre.isEmpty() || contenu.isEmpty()) {
            Toast.makeText(this, "Titre et contenu obligatoires !", Toast.LENGTH_SHORT).show();
            return;
        }

        // Date
        int jour = datePicker.getDayOfMonth();
        int mois = datePicker.getMonth() + 1;
        int annee = datePicker.getYear();
        String date = String.format("%02d/%02d/%d", jour, mois, annee);

        // Tags (plusieurs possibles)
        StringBuilder tags = new StringBuilder();
        if (cbPerso.isChecked()) tags.append("perso ");
        if (cbTravail.isChecked()) tags.append("travail ");
        if (cbVoyage.isChecked()) tags.append("voyage ");

        // Créer l'entrée
        String entree = titre + " - " + date + " [" + tags.toString().trim() + "]";
        entrees.add(0, entree);  // Ajouter en haut

        // Rafraîchir la liste
        adapter.notifyDataSetChanged();

        // Vider les champs
        etTitre.setText("");
        etContenu.setText("");
        cbPerso.setChecked(false);
        cbTravail.setChecked(false);
        cbVoyage.setChecked(false);

        Toast.makeText(this, "Entrée ajoutée !", Toast.LENGTH_SHORT).show();
    }
}
