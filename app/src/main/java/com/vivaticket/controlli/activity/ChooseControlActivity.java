package com.supergianlu.controlli.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.supergianlu.controlli.R;
import com.supergianlu.controlli.activity.segnalazioni.SignalationsActivity;

import static com.supergianlu.controlli.util.Helper.CHOOSE_CONTROL_CODE;
import static com.supergianlu.controlli.util.Helper.PREFS_CONTROL_CODE;
import static com.supergianlu.controlli.util.Helper.PREFS_NAME;
public class ChooseControlActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosecontrol);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(PREFS_CONTROL_CODE, CHOOSE_CONTROL_CODE);
        edit.apply();

        final Button signalationsButton = findViewById(R.id.signalationsButton);
        final Button servicesButton = findViewById(R.id.servicesButton);

        servicesButton.setOnClickListener(e -> {
            signalationsButton.setClickable(false);
            servicesButton.setClickable(false);
            Intent intent = new Intent(this, ChooseServiceActivity.class);
            startActivity(intent);
            finish();
        });

        signalationsButton.setOnClickListener(e -> {
            signalationsButton.setClickable(false);
            servicesButton.setClickable(false);
            Intent intent = new Intent(this, SignalationsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
