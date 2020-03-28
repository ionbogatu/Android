package com.example.myshop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int openedModalIndex = -1;

    ListView listView;

    String[] titleArray = {"Product 1", "Product 2", "Product 3"};
    String[] descriptionArray = {"Description 1", "Description 2", "Description 3"};
    ArrayList<Integer> wishList = new ArrayList<>();
    Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        MyAdapter adapter = new MyAdapter(this, titleArray, descriptionArray);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openedModalIndex = position;

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialogBuilder.setMessage("Add this product to your wishlist?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.this.wishList.add(position);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.setOnDismissListener((dialog) -> {
                    MainActivity.this.dialog = new Dialog(MainActivity.this);
                    MainActivity.this.ShowPopup(
                            MainActivity.this.titleArray[position],
                            MainActivity.this.descriptionArray[position]
                    );
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Check if menu contains location and sensors item
        NavigationView navigationView = findViewById(R.id.navigation_view);
        MenuItem locationAndSensorsMenuItem = navigationView.getMenu().findItem(R.id.menu_location_and_sensors);
        if (locationAndSensorsMenuItem != null) {

            // Check if device has sensors
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager == null || sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0) {

                // set menu item to disabled
                locationAndSensorsMenuItem.setEnabled(false);
            }
        }

        return true;
    }

    public void ShowPopup(String title, String description) {
        dialog.setContentView(R.layout.modal);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView textClose = this.dialog.findViewById(R.id.modal_close);

        textClose.setOnClickListener((v) -> {
            dialog.dismiss();
            openedModalIndex = -1;
        });

        TextView modalProductTitle = dialog.findViewById(R.id.modal_productTitle);
        modalProductTitle.setText(title);

        TextView modalProductDescription = dialog.findViewById(R.id.modal_productDescription);
        modalProductDescription.setText(description);

        dialog.show();
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String[] title;
        String[] description;

        MyAdapter(Context c, String[] t, String d[]) {
            super(c, R.layout.activity_main, R.id.textView1, t);
            this.context = c;
            this.title = t;
            this.description = d;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView title = row.findViewById(R.id.textView1);
            TextView description = row.findViewById(R.id.textView2);

            title.setText(this.title[position]);
            description.setText(this.description[position]);

            return row;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (openedModalIndex != -1) {
            ShowPopup(MainActivity.this.titleArray[openedModalIndex], MainActivity.this.descriptionArray[openedModalIndex]);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("openModalIndex", openedModalIndex);
    }

    public boolean goToPreferences(MenuItem menuItem) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        this.startActivity(intent);

        return true;
    }

    public boolean goToLocationAndSensors(MenuItem menuItem) {
        Intent intent = new Intent(this, LocationAndSensorsActivity.class);
        this.startActivity(intent);

        return true;
    }

    public boolean goToAbout(MenuItem menuItem) {
        Intent intent = new Intent(this, AboutActivity.class);
        this.startActivity(intent);

        return true;
    }

    public boolean callMe(MenuItem menuItem) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Call Me.");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

        return true;
    }
}
