package com.example.journalpersonnel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
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

    private ArrayList<String> entrees;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private EditText etTitre, etContenu;
    private TextView tvDateHeure;
    private CheckBox cbPerso, cbTravail, cbVoyage;
    private Button btnEnregistrer, btnDate, btnHeure;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser Calendar
        calendar = Calendar.getInstance();

        // Initialiser les vues
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

        // Initialiser la liste
        entrees = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.item_journal, R.id.tvItem, entrees);
        listView.setAdapter(adapter);

        // Date/Heure actuelles
        majDateHeure();

        // Boutons
        btnDate.setOnClickListener(v -> new DatePickerFragment().show(getFragmentManager(), "datePicker"));
        btnHeure.setOnClickListener(v -> new TimePickerFragment().show(getFragmentManager(), "timePicker"));
        btnEnregistrer.setOnClickListener(v -> enregistrerEntree());
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

    private void enregistrerEntree() {
        String titre = etTitre.getText().toString().trim();
        String contenu = etContenu.getText().toString().trim();

        if (titre.isEmpty() || contenu.isEmpty()) {
            Toast.makeText(this, "Titre et contenu obligatoires !", Toast.LENGTH_SHORT).show();
            return;
        }

        // Date/Heure formatée
        String dateHeure = String.format("%02d/%02d/%d %02d:%02d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));

        // Tags
        StringBuilder tags = new StringBuilder();
        if (cbPerso.isChecked()) tags.append("perso ");
        if (cbTravail.isChecked()) tags.append("travail ");
        if (cbVoyage.isChecked()) tags.append("voyage ");

        // Entrée
        String entree = titre + " - " + dateHeure + " [" + tags.toString().trim() + "]";
        entrees.add(0, entree);
        adapter.notifyDataSetChanged();

        // Vider
        etTitre.setText("");
        etContenu.setText("");
        cbPerso.setChecked(false);
        cbTravail.setChecked(false);
        cbVoyage.setChecked(false);

        Toast.makeText(this, "Entrée ajoutée !", Toast.LENGTH_SHORT).show();
    }

    // Dialog Date
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public DatePickerDialog onCreateDialog(Bundle savedInstanceState) {
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

    // Dialog Heure
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            MainActivity activity = (MainActivity) getActivity();
            int hour = activity.calendar.get(Calendar.HOUR_OF_DAY);
            int minute = activity.calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
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
