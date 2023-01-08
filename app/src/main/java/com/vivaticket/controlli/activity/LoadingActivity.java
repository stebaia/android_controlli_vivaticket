package com.supergianlu.controlli.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.supergianlu.controlli.activity.servizi.moduli.ModKActivity;
import com.supergianlu.controlli.activity.servizi.moduli.ModLActivity;
import com.supergianlu.controlli.activity.servizi.moduli.ModN2Activity;
import com.supergianlu.controlli.activity.segnalazioni.SignalationsActivity;

import static com.supergianlu.controlli.util.Helper.CHOOSE_CONTROL_CODE;
import static com.supergianlu.controlli.util.Helper.K_CODE;
import static com.supergianlu.controlli.util.Helper.L_GENERICO_CODE;
import static com.supergianlu.controlli.util.Helper.L_ULTIMOGIORNO_CODE;
import static com.supergianlu.controlli.util.Helper.N2_CODE;
import static com.supergianlu.controlli.util.Helper.PREFS_CONTROL_CODE;
import static com.supergianlu.controlli.util.Helper.PREFS_NAME;
import static com.supergianlu.controlli.util.Helper.PREFS_USER_LOGGED;
import static com.supergianlu.controlli.util.Helper.SERVICES_CODE;
import static com.supergianlu.controlli.util.Helper.SIGNALATIONS_CODE;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final boolean isLogged = prefs.getBoolean(PREFS_USER_LOGGED, false);
        final int controlCode = prefs.getInt(PREFS_CONTROL_CODE, -1);

        Intent intent = null;
        if(isLogged){
            switch (controlCode) {
                case CHOOSE_CONTROL_CODE:
                    intent = new Intent(this, ChooseControlActivity.class);
                    break;
                case SIGNALATIONS_CODE:
                    intent = new Intent(this, SignalationsActivity.class);
                    break;
                case SERVICES_CODE:
                    intent = new Intent(this, ChooseServiceActivity.class);
                    break;
                case K_CODE:
                    intent = new Intent(this, ModKActivity.class);
                    break;
                case N2_CODE:
                    intent = new Intent(this, ModN2Activity.class);
                    break;
                case L_GENERICO_CODE:
                    intent = new Intent(this, ModLActivity.class);
                    intent.putExtra("l_generico", true);
                    break;
                case L_ULTIMOGIORNO_CODE:
                    intent = new Intent(this, ModLActivity.class);
                    intent.putExtra("l_generico", false);
                    break;
            }
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }


}
