package com.example.myshop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

public class LocationAndSensorsActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private String accelerometerValue = "";
    private String gravityValue = "";
    private String gyroscopeValue = "";
    private String rotationValue = "";
    private String thermometerValue = "";
    private String barometerValue = "";
    private String photometerValue = "";
    private String orientationValue = "";
    private String magnetometerValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Location & Sensors");
        setContentView(R.layout.activity_location_and_sensors);

        SetSensors();

        // Update view each second
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // send message to update sensor texts
                sensorsMessageHandler.obtainMessage().sendToTarget();
            }
        }, 0, 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        SetLocation();
    }

    // update sensor texts when triggered
    public Handler sensorsMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            TextView accelerometerValue = findViewById(R.id.accelerometer_sensor);
            if (accelerometerValue != null) {
                accelerometerValue.setText(LocationAndSensorsActivity.this.accelerometerValue);
            }

            TextView gravityValue = findViewById(R.id.gravity_sensor);
            if (gravityValue != null) {
                gravityValue.setText(LocationAndSensorsActivity.this.gravityValue);
            }

            TextView gyroscopeValue = findViewById(R.id.gyroscope_sensor);
            if (gyroscopeValue != null) {
                gyroscopeValue.setText(LocationAndSensorsActivity.this.gyroscopeValue);
            }

            TextView rotationValue = findViewById(R.id.rotation_sensor);
            if (rotationValue != null) {
                rotationValue.setText(LocationAndSensorsActivity.this.rotationValue);
            }

            TextView thermometerValue = findViewById(R.id.thermometer_sensor);
            if (thermometerValue != null) {
                thermometerValue.setText(LocationAndSensorsActivity.this.thermometerValue);
            }

            TextView barometerValue = findViewById(R.id.barometer_sensor);
            if (barometerValue != null) {
                barometerValue.setText(LocationAndSensorsActivity.this.barometerValue);
            }

            TextView photometerValue = findViewById(R.id.photometer_sensor);
            if (photometerValue != null) {
                photometerValue.setText(LocationAndSensorsActivity.this.photometerValue);
            }

            TextView orientationValue = findViewById(R.id.orientation_sensor);
            if (orientationValue != null) {
                orientationValue.setText(LocationAndSensorsActivity.this.orientationValue);
            }

            TextView magnetometerValue = findViewById(R.id.magnetometer_sensor);
            if (magnetometerValue != null) {
                magnetometerValue.setText(LocationAndSensorsActivity.this.magnetometerValue);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void SetLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                TextView locationView = findViewById(R.id.location);
                locationView.setText("lat: " + location.getLatitude() + ", " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 10);
            return;
        } else {
            locationManager.requestLocationUpdates("gps", 1000, (float) 0.1, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10) {
            locationManager.requestLocationUpdates("gps", 1000, (float) 0.1, locationListener);
        }
    }

    private void SetSensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0) {
                Sensor accelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);

                if (accelerometer != null) {
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            LocationAndSensorsActivity.this.accelerometerValue = "x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2];
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    }, accelerometer, 1000000, 1000000);
                }
            }

            if (sensorManager.getSensorList(Sensor.TYPE_GRAVITY).size() > 0) {
                Sensor gravity = sensorManager.getSensorList(Sensor.TYPE_GRAVITY).get(0);

                if (gravity != null) {
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            LocationAndSensorsActivity.this.gravityValue = "x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2];
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    }, gravity, 1000000, 1000000);
                }
            }

            if (sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).size() > 0) {
                Sensor gyroscope = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);

                if (gyroscope != null) {
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            LocationAndSensorsActivity.this.gyroscopeValue = "x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2];
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    }, gyroscope, 1000000, 1000000);
                }
            }

            if (sensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR).size() > 0) {
                Sensor rotation = sensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR).get(0);

                if (rotation != null) {
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            LocationAndSensorsActivity.this.rotationValue = "x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2];
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    }, rotation, 1000000, 1000000);
                }
            }

            if (sensorManager.getSensorList(Sensor.TYPE_AMBIENT_TEMPERATURE).size() > 0) {
                Sensor thermometer = sensorManager.getSensorList(Sensor.TYPE_AMBIENT_TEMPERATURE).get(0);

                if (thermometer != null) {
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            LocationAndSensorsActivity.this.thermometerValue = event.values[0] + "";
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    }, thermometer, 1000000, 1000000);
                }
            }

            if (sensorManager.getSensorList(Sensor.TYPE_PRESSURE).size() > 0) {
                Sensor barometer = sensorManager.getSensorList(Sensor.TYPE_PRESSURE).get(0);

                if (barometer != null) {
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            LocationAndSensorsActivity.this.barometerValue = event.values[0] + "";
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    }, barometer, 1000000, 1000000);
                }
            }

            if (sensorManager.getSensorList(Sensor.TYPE_LIGHT).size() > 0) {
                Sensor photometer = sensorManager.getSensorList(Sensor.TYPE_LIGHT).get(0);

                if (photometer != null) {
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            LocationAndSensorsActivity.this.photometerValue = event.values[0] + "";
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    }, photometer, 1000000, 1000000);
                }
            }

            if (sensorManager.getSensorList(Sensor.TYPE_ORIENTATION).size() > 0) {
                Sensor orientation = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);

                if (orientation != null) {
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            LocationAndSensorsActivity.this.orientationValue = "x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2];
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    }, orientation, 1000000, 1000000);
                }
            }

            if (sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).size() > 0) {
                Sensor magnetometer = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);

                if (magnetometer != null) {
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            LocationAndSensorsActivity.this.magnetometerValue = "x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2];
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    }, magnetometer, 1000000, 1000000);
                }
            }
        }
    }
}
