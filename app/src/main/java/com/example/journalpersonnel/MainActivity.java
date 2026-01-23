package com.example.journalpersonnel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Listes de données
    private ArrayList<String> entrees;
    private ArrayList<String> contenus;
    private ArrayAdapter<String> adapter;

    // Vues
    private ListView listView;
    private EditText etTitre, etContenu;
    private TextView tvDateHeure;
    private CheckBox cbPerso, cbTravail, cbVoyage;
    private Button btnEnregistrer, btnDate, btnHeure;

    // Logique
    private Calendar calendar;
    private int positionEnEdition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation calendrier
        calendar = Calendar.getInstance();

        // Liaison avec les vues XML
        etTitre = findViewById(R.id.Titre);
        etContenu = findViewById(R.id.Contenu);
        tvDateHeure = findViewById(R.id.DateHeure);
        cbPerso = findViewById(R.id.cbPerso);
        cbTravail = findViewById(R.id.cbTravail);
        cbVoyage = findViewById(R.id.cbVoyage);
        btnEnregistrer = findViewById(R.id.Enregistrer);
        btnDate = findViewById(R.id.Date);
        btnHeure = findViewById(R.id.Heure);
        listView = findViewById(R.id.listViewJournal);

        // Configuration liste et adaptateur
        entrees = new ArrayList<>();
        contenus = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.item_journal, R.id.tvItem, entrees);
        listView.setAdapter(adapter);


        majDateHeure();


        listView.setOnItemClickListener((parent, view, position, id) -> {
            afficherMenuActions(position);
        });


        btnDate.setOnClickListener(v -> new DatePickerFragment().show(getFragmentManager(), "datePicker"));
        btnHeure.setOnClickListener(v -> new TimePickerFragment().show(getFragmentManager(), "timePicker"));


        btnEnregistrer.setOnClickListener(v -> enregistrerOuModifierEntree());
    }


    private void afficherMenuActions(int position) {
        String entree = entrees.get(position);
        String contenu = contenus.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setItems(new String[]{"📄 Voir détails", "✏️ Modifier", "🗑️ Supprimer"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            new AlertDialog.Builder(this)
                                    .setTitle("Détails de l'entrée")
                                    .setMessage(entree + "  " + contenu)
                                    .setPositiveButton("Fermer", null)
                                    .show();
                            break;
                        case 1:
                            chargerEdition(position);
                            break;
                        case 2:
                            supprimerEntree(position);
                            break;
                    }
                })
                .show();
    }

    private void chargerEdition(int position) {
        positionEnEdition = position;

        String entree = entrees.get(position);
        String contenu = contenus.get(position);


        String[] parties = entree.split(" - ", 2);
        String titre = parties[0];


        etTitre.setText(titre);
        etContenu.setText(contenu);


        cbPerso.setChecked(entree.contains("perso"));
        cbTravail.setChecked(entree.contains("travail"));
        cbVoyage.setChecked(entree.contains("voyage"));


        btnEnregistrer.setText("Modifier l'entrée");

        Toast.makeText(this, "Mode édition : modifiez et enregistrez", Toast.LENGTH_SHORT).show();
    }

    private void supprimerEntree(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Voulez-vous vraiment supprimer cette entrée ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    entrees.remove(position);
                    contenus.remove(position);
                    adapter.notifyDataSetChanged();


                    if (positionEnEdition == position) {
                        resetFormulaire();
                    }
                    Toast.makeText(this, "Supprimé !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void enregistrerOuModifierEntree() {
        String titre = etTitre.getText().toString().trim();
        String contenu = etContenu.getText().toString().trim();

        if (titre.isEmpty() || contenu.isEmpty()) {
            Toast.makeText(this, "Titre et contenu obligatoires !", Toast.LENGTH_SHORT).show();
            return;
        }


        String dateHeure = String.format("%02d/%02d/%d %02d:%02d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));


        StringBuilder tags = new StringBuilder();
        if (cbPerso.isChecked()) tags.append("perso ");
        if (cbTravail.isChecked()) tags.append("travail ");
        if (cbVoyage.isChecked()) tags.append("voyage ");

        String nouvelleLigne = titre + " - " + dateHeure + " [" + tags.toString().trim() + "]";

        if (positionEnEdition == -1) {

            entrees.add(0, nouvelleLigne);
            contenus.add(0, contenu);
            Toast.makeText(this, "Nouvelle entrée ajoutée !", Toast.LENGTH_SHORT).show();
        } else {

            entrees.set(positionEnEdition, nouvelleLigne);
            contenus.set(positionEnEdition, contenu);
            Toast.makeText(this, "Entrée modifiée avec succès !", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
        resetFormulaire();
    }

    private void resetFormulaire() {
        etTitre.setText("");
        etContenu.setText("");
        cbPerso.setChecked(false);
        cbTravail.setChecked(false);
        cbVoyage.setChecked(false);

        positionEnEdition = -1;
        btnEnregistrer.setText("Enregistrer");
    }

    private void majDateHeure() {
        String date = String.format("%02d/%02d/%d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
        String heure = String.format("%02d:%02d",
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
        tvDateHeure.setText(date + " " + heure);
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            MainActivity activity = (MainActivity) getActivity();
            int year = activity.calendar.get(Calendar.YEAR);
            int month = activity.calendar.get(Calendar.MONTH);
            int day = activity.calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            MainActivity activity = (MainActivity) getActivity();
            activity.calendar.set(year, month, day);
            activity.majDateHeure();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            MainActivity activity = (MainActivity) getActivity();
            int hour = activity.calendar.get(Calendar.HOUR_OF_DAY);
            int minute = activity.calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            MainActivity activity = (MainActivity) getActivity();
            activity.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            activity.calendar.set(Calendar.MINUTE, minute);
            activity.majDateHeure();
        }
    }
}
