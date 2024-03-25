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
    private void updateMoneyDisplay() {
        if (totalMoney > 0) {
            moneyTextView.setText(getString(R.string.Money) + " " + totalMoney);
        }
        if (totalMoney <= 0) {
            moneyTextView.setText(getString(R.string.NoMoney));
        }

    }

    private boolean validateBet() {
        String betStr = betEditText.getText().toString();
        if (betStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.SetValue), Toast.LENGTH_SHORT).show();
            return false;
        }
        int betAmount = Integer.parseInt(betStr);
        if (betAmount <= 0) {
            Toast.makeText(this, getString(R.string.HigherThanZero), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (betAmount > totalMoney) {
            Toast.makeText(this, getString(R.string.NoMoneyLeft), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void rollDice() {
        if (!validateBet()) {
            return;
        }
        Random random = new Random();
        int randomNumber = random.nextInt(10); // Zufällige Zahl zwischen 1 und 10 generieren

        int diceRoll = random.nextInt(10); // Spieler würfelt eine Zahl zwischen 1 und 10

        if (diceRoll > randomNumber) {
            int betAmount = Integer.parseInt(betEditText.getText().toString());
            int winnings = betAmount; // Spieler gewinnt
            totalMoney += winnings;
            resultTextView.setText(getString(R.string.Win) + " " + winnings + " " + getString(R.string.Moneyy));
            vibrate();
        } else if (diceRoll < randomNumber) {
            int betAmount = Integer.parseInt(betEditText.getText().toString());
            totalMoney -= betAmount;// Spieler verliert
            resultTextView.setText(getString(R.string.lose) + " " + betAmount + " " + getString(R.string.Moneyy));
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
                    Thread.sleep(3600000); // 1 Stunde (360000 Millisekunden)
                    totalMoney = totalMoney + 20; // Geld um 20 erhöht
                    runOnUiThread(() -> updateMoneyDisplay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nicht benötigt
    }
}