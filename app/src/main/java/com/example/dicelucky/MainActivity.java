package com.example.dicelucky;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dicelucky.DataSafe.GameDataHandler;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private int totalMoney;
    private EditText betEditText;
    private TextView moneyTextView, resultTextView;
    private Button rollButton;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        betEditText = findViewById(R.id.betEditText);
        moneyTextView = findViewById(R.id.moneyTextView);
        resultTextView = findViewById(R.id.resultTextView);
        rollButton = findViewById(R.id.rollButton);

        rollButton.setOnClickListener(v -> rollDice());

        // Teilweise kopiert von dieser Seite https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview#java
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        totalMoney = GameDataHandler.loadTotalMoney(this);

        updateMoneyDisplay();
        scheduleMoneyIncrement();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameDataHandler.saveTotalMoney(this, totalMoney);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameDataHandler.loadTotalMoney(this);
    }

    private void updateMoneyDisplay() {
        if (totalMoney > 0) {
            moneyTextView.setText(getString(R.string.money) + " " + totalMoney);
        }
        if (totalMoney <= 0) {
            moneyTextView.setText(getString(R.string.noMoney));
        }

    }

    private boolean betCheck() {
        String betString = betEditText.getText().toString();
        if (betString.isEmpty()) {
            Toast.makeText(this, getString(R.string.setValue), Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            int betAmount = Integer.parseInt(betString);
            if (betAmount <= 0) {
                Toast.makeText(this, getString(R.string.higherThanZero), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (betAmount > totalMoney) {
                Toast.makeText(this, getString(R.string.noMoneyLeft), Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.invalidInput), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void rollDice() {
        if (!betCheck()) {
            return;
        }
        Random random = new Random();
        int randomNumber = random.nextInt(10); // Zufällige Zahl zwischen 1 und 10 generieren

        int diceRoll = random.nextInt(10); // Spieler würfelt eine Zahl zwischen 1 und 10

        if (diceRoll > randomNumber) {
            int betAmount = Integer.parseInt(betEditText.getText().toString());
            int winnings = betAmount; // Spieler gewinnt
            totalMoney += winnings;
            resultTextView.setText(getString(R.string.win) + " " + winnings + " " + getString(R.string.moneyy));
            vibrate();
        } else if (diceRoll < randomNumber) {
            int betAmount = Integer.parseInt(betEditText.getText().toString());
            totalMoney -= betAmount;// Spieler verliert
            resultTextView.setText(getString(R.string.lose) + " " + betAmount + " " + getString(R.string.moneyy));
        } else {
            resultTextView.setText(getString(R.string.tie));// Unentschieden
        }
        updateMoneyDisplay();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500);
            }
        }
    }

    private void scheduleMoneyIncrement() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000); // 1 minute (60000 Millisekunden)
                    totalMoney = totalMoney + 1; // Geld um 1 erhöht
                    runOnUiThread(() -> updateMoneyDisplay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) { // diese Funktion wurde von hier aus zusammengebaut https://gist.github.com/iewnait/2138807
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastShakeTime) > 1000) { // Mindestabstand von 1 Sekunde zwischen Schütteln
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
                if (acceleration > 30) { // Empfindlichkeit des Schüttelns
                    rollDice();
                    lastShakeTime = currentTime;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nicht benötigt
    }
}