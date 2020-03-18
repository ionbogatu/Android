package com.example.myshop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity {
    private EditText name;
    private Switch syncContacts;
    private DatabaseHelper databaseHelper;

    public PreferencesActivity() {
        this.databaseHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        name = findViewById(R.id.preference_name);
        syncContacts = findViewById(R.id.preference_sync_contacts);

        // Option 1: Retrieve from shared preferences

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);

        if (sharedPreferences.contains("name")) {
            this.name.setText(sharedPreferences.getString("name", ""));
        }

        if (sharedPreferences.contains("syncContacts")) {
            this.syncContacts.setChecked( sharedPreferences.getBoolean("syncContacts", false));
        }

        // Option 2: Retrieve from database

        String name = databaseHelper.retrieveData("name");
        if (name != null) {
            this.name.setText(name);
        }

        String syncContacts = databaseHelper.retrieveData("syncContacts");
        if (syncContacts.equals("1")) {
            this.syncContacts.setChecked(true);
        }
    }

    public boolean savePreferences(View button) {
        String name = this.name.getText().toString();
        boolean syncContacts = this.syncContacts.isChecked();

        // Option 1: Save to shared preferences

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.putBoolean("syncContacts", syncContacts);

        editor.apply();

        // Option 1: Save to sqlite database

        databaseHelper.saveData("name", name);
        databaseHelper.saveData("syncContacts", (syncContacts ? "1": "0"));

        finish();

        return true;
    }

    @Override
    protected void onDestroy() {
        this.databaseHelper = null;

        super.onDestroy();
    }
}
