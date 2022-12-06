package com.supergianlu.controlli.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.supergianlu.controlli.R;
import com.supergianlu.controlli.activity.servizi.moduli.ModKActivity;
import com.supergianlu.controlli.activity.servizi.moduli.ModLActivity;
import com.supergianlu.controlli.activity.servizi.moduli.ModN2Activity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.supergianlu.controlli.util.Helper.EXTRA_IDMODULO;
import static com.supergianlu.controlli.util.Helper.K_CODE;
import static com.supergianlu.controlli.util.Helper.L_GENERICO_CODE;
import static com.supergianlu.controlli.util.Helper.L_ULTIMOGIORNO_CODE;
import static com.supergianlu.controlli.util.Helper.METHOD_GETMANIFESTAZIONI;
import static com.supergianlu.controlli.util.Helper.N2_CODE;
import static com.supergianlu.controlli.util.Helper.NAMESPACE;
import static com.supergianlu.controlli.util.Helper.PREFS_CONTROL_CODE;
import static com.supergianlu.controlli.util.Helper.PREFS_IS_ITA;
import static com.supergianlu.controlli.util.Helper.PREFS_MANIFESTATION_NAME;
import static com.supergianlu.controlli.util.Helper.PREFS_MANIFESTATION_VALUE;
import static com.supergianlu.controlli.util.Helper.PREFS_NAME;
import static com.supergianlu.controlli.util.Helper.SERVICES_CODE;
import static com.supergianlu.controlli.util.Helper.SOAP_ACTION_GETMANIFESTAZIONI;
import static com.supergianlu.controlli.util.Helper.URL;

public class ChooseServiceActivity extends AppCompatActivity {

    private final static String ITALIANO = "ITALIANO";
    private final static String INGLESE = "INGLESE";

    private SharedPreferences sharedPreferences;
    private Button modlGenericButton;
    private Button modlLastDayButton;
    private Button modkButton;
    private Button modn2Button;
    private ImageView flagButton;
    private TextView languageTextView;
    private Spinner manifestationsSpinner;

    private List<Integer> manifestationsValues;
    private List<String> manifestationsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseservice);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(PREFS_CONTROL_CODE, SERVICES_CODE);
        edit.apply();

        modlGenericButton = findViewById(R.id.modlGenericButton);
        modlLastDayButton = findViewById(R.id.modlLastDayButton);
        modkButton = findViewById(R.id.modkButton);
        modn2Button = findViewById(R.id.modn2Button);

        modkButton.setOnClickListener(e -> startServicesActivity(K_CODE));
        modn2Button.setOnClickListener(e -> startServicesActivity(N2_CODE));
        modlGenericButton.setOnClickListener(e -> startServicesActivity(L_GENERICO_CODE));
        modlLastDayButton.setOnClickListener(e -> startServicesActivity(L_ULTIMOGIORNO_CODE));

        flagButton = findViewById(R.id.flagButton);
        languageTextView = findViewById(R.id.languageTextView);
        manifestationsSpinner = findViewById(R.id.spinner);

        manifestationsNames = new ArrayList<>();
        manifestationsValues = new ArrayList<>();

        if(sharedPreferences.getBoolean(PREFS_IS_ITA, true)){
            flagButton.setImageDrawable(getDrawable(R.drawable.ita));
            languageTextView.setText(ITALIANO);
        } else {
            flagButton.setImageDrawable(getDrawable(R.drawable.eng));
            languageTextView.setText(INGLESE);
        }

        setAllNotClickable();

        flagButton.setOnClickListener(e -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String languageToLoad;
            if(sharedPreferences.getBoolean(PREFS_IS_ITA, true)){
                languageToLoad  = "en";
                flagButton.setImageDrawable(getDrawable(R.drawable.eng));
                editor.putBoolean(PREFS_IS_ITA, false);
                languageTextView.setText(INGLESE);
            } else {
                languageToLoad  = "it";
                flagButton.setImageDrawable(getDrawable(R.drawable.ita));
                editor.putBoolean(PREFS_IS_ITA, true);
                languageTextView.setText(ITALIANO);
            }
            editor.apply();
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        });

        new AsyncTaskGetManifestazioni(this).execute();
    }

    private void startServicesActivity(int serviceCode){
        setAllNotClickable();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(PREFS_CONTROL_CODE, serviceCode);
        edit.putInt(PREFS_MANIFESTATION_VALUE, manifestationsValues.get(manifestationsSpinner.getSelectedItemPosition()));
        edit.putString(PREFS_MANIFESTATION_NAME, manifestationsNames.get(manifestationsSpinner.getSelectedItemPosition()));
        edit.apply();
        Intent intent = null;
        switch (serviceCode){
            case K_CODE:
                intent = new Intent(this, ModKActivity.class);
                break;
            case N2_CODE:
                intent = new Intent(this, ModN2Activity.class);
                break;
            case L_GENERICO_CODE:
                intent = new Intent(this, ModLActivity.class);
                intent.putExtra(EXTRA_IDMODULO, L_GENERICO_CODE);
                break;
            case L_ULTIMOGIORNO_CODE:
                intent = new Intent(this, ModLActivity.class);
                intent.putExtra(EXTRA_IDMODULO, L_ULTIMOGIORNO_CODE);
                break;
        }
        startActivity(intent);
        finish();
    }

    private static class AsyncTaskGetManifestazioni extends AsyncTask<String, Void, SoapObject> {
        private WeakReference<ChooseServiceActivity> activityReference;

        AsyncTaskGetManifestazioni(ChooseServiceActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETMANIFESTAZIONI);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_GETMANIFESTAZIONI, envelope);
                return (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject manifestations) {
            ChooseServiceActivity activity = activityReference.get();
            if(manifestations != null) {
                if(manifestations.getPropertyCount() == 0){
                    Toast.makeText(activity, activity.getText(R.string.error_getmanifestations), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity, ChooseControlActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }else {
                    for (int i = 0; i < manifestations.getPropertyCount(); i++) {
                        SoapObject manifestation = (SoapObject) manifestations.getProperty(i);
                        activity.manifestationsValues.add(Integer.parseInt(manifestation.getProperty(0).toString()));
                        activity.manifestationsNames.add(manifestation.getProperty(1).toString());
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, activity.manifestationsNames);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    activity.manifestationsSpinner.setAdapter(spinnerArrayAdapter);

                    final int oldManifestationValue = activity.sharedPreferences.getInt(PREFS_MANIFESTATION_VALUE, -1);
                    if(activity.manifestationsValues.contains(oldManifestationValue)){
                        activity.manifestationsSpinner.setSelection(activity.manifestationsValues.indexOf(oldManifestationValue));
                    }
                    activity.setAllClickable();
                }
            } else {
                Toast.makeText(activity, activity.getText(R.string.error_getdata), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setAllNotClickable(){
        modkButton.setClickable(false);
        modn2Button.setClickable(false);
        modlGenericButton.setClickable(false);
        modlLastDayButton.setClickable(false);
        manifestationsSpinner.setClickable(false);
    }

    private void setAllClickable(){
        modkButton.setClickable(true);
        modn2Button.setClickable(true);
        modlGenericButton.setClickable(true);
        modlLastDayButton.setClickable(true);
        manifestationsSpinner.setClickable(true);
    }

}
