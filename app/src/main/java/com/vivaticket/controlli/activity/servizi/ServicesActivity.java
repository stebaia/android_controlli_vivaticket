package com.vivaticket.controlli.activity.servizi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vivaticket.controlli.Modulo;
import com.vivaticket.controlli.R;
import com.vivaticket.controlli.activity.ChooseServiceActivity;
import com.vivaticket.controlli.activity.LoginActivity;
import com.vivaticket.controlli.database.MyDatabaseHelper;
import com.vivaticket.controlli.database.Province;
import com.vivaticket.controlli.database.State;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.vivaticket.controlli.util.Helper.EXTRA_IDMODULO;
import static com.vivaticket.controlli.util.Helper.EXTRA_SEARCH;
import static com.vivaticket.controlli.util.Helper.EXTRA_SIGNATURE;
import static com.vivaticket.controlli.util.Helper.METHOD_GETCOMUNI;
import static com.vivaticket.controlli.util.Helper.METHOD_GETMODULO;
import static com.vivaticket.controlli.util.Helper.METHOD_SETMODULO;
import static com.vivaticket.controlli.util.Helper.NAMESPACE;
import static com.vivaticket.controlli.util.Helper.PREFS_CONTROL_CODE;
import static com.vivaticket.controlli.util.Helper.PREFS_IS_ITA;
import static com.vivaticket.controlli.util.Helper.PREFS_MANIFESTATION_NAME;
import static com.vivaticket.controlli.util.Helper.PREFS_MANIFESTATION_VALUE;
import static com.vivaticket.controlli.util.Helper.PREFS_NAME;
import static com.vivaticket.controlli.util.Helper.PREFS_USER_EMAIL;
import static com.vivaticket.controlli.util.Helper.PREFS_USER_ID;
import static com.vivaticket.controlli.util.Helper.PREFS_USER_LOGGED;
import static com.vivaticket.controlli.util.Helper.PREFS_USER_ROLE;
import static com.vivaticket.controlli.util.Helper.SERVICES_CODE;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_GETCOMUNI;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_GETMODULO;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_SETMODULO;
import static com.vivaticket.controlli.util.Helper.URL;

public abstract class ServicesActivity extends AppCompatActivity {

    protected static final int PHOTO_NUM = 3;
    private static final String ITALY = "Italia";
    protected static final int REQUEST_SIGN_CODE = 100;
    private static final int REQUEST_SEARCH_CODE = 101;

    protected static DecimalFormat df2 = new DecimalFormat("0.00");
    protected static DecimalFormat df0 = new DecimalFormat("0");
    protected SharedPreferences sharedPreferences;

    private MyDatabaseHelper databaseHelper;
    protected Modulo modulo;
    protected List<String> nazioniCodes;
    protected List<String> provinceCodes;
    protected List<String> comuniAllCodes;
    protected List<String> comuniAllCaps;
    protected List<String> comuniEspCodes;
    protected List<String> comuniEspCaps;

    protected LinearLayout layoutAll;
    protected TextView titleText;
    protected RadioGroup radioGroup;
    protected RadioButton radioButtonAll;
    protected RadioButton radioButtonEsp;
    protected TextView textViewAll;
    protected TextView textViewEsp;
    protected double totalAmount;
    protected TextView textViewTotalAmount;
    protected Button confirmButton;
    protected ProgressDialog progressDialog;

    protected EditText ragioneSocialeAll;
    protected EditText indirizzoAll;
    protected Spinner nazioneAll;
    protected Spinner provinciaAll;
    protected Spinner comuneAll;
    protected EditText capAll;
    protected EditText telefonoAll;
    protected EditText faxAll;
    protected EditText partitaIvaAll;
    protected EditText emailAll;

    protected EditText nome;
    protected EditText cognome;
    protected EditText ruoloinazienda;

    protected EditText ragioneSocialeEsp;
    protected EditText indirizzoEsp;
    protected EditText capEsp;
    protected Spinner comuneEsp;
    protected Spinner provinciaEsp;
    protected Spinner nazioneEsp;
    protected EditText telefonoEsp;
    protected EditText faxEsp;
    protected EditText partitaIvaEsp;
    protected EditText emailEsp;
    protected EditText standEsp;
    protected EditText padiglioneEsp;

    protected EditText note;

    protected byte[] signatureInByte;

    protected EditText provinciaAllEstero;
    protected EditText comuneAllEstero;

    protected EditText provinciaEspEstero;
    protected EditText comuneEspEstero;

    protected byte[][] imagesInByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String languageToLoad;
        if(sharedPreferences.getBoolean(PREFS_IS_ITA, true)){
            languageToLoad  = "it";
        } else {
            languageToLoad  = "en";
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        databaseHelper = new MyDatabaseHelper(getApplicationContext());
        modulo = new Modulo();
        modulo.IdManifestazione = sharedPreferences.getInt(PREFS_MANIFESTATION_VALUE, -1);

        imagesInByte = new byte[PHOTO_NUM][];

        textViewAll = findViewById(R.id.textViewAll);
        textViewEsp = findViewById(R.id.textViewEsp);
        layoutAll = findViewById(R.id.layoutAll);
        titleText = findViewById(R.id.titleText);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonAll = findViewById(R.id.radioButtonAll);
        radioButtonEsp = findViewById(R.id.radioButtonEsp);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        confirmButton = findViewById(R.id.confirmButton);

        ragioneSocialeAll = findViewById(R.id.ragioneSocialeAll);
        indirizzoAll = findViewById(R.id.indirizzoAll);
        capAll = findViewById(R.id.capAll);
        comuneAll = findViewById(R.id.comuneAll);
        provinciaAll = findViewById(R.id.provinciaAll);
        nazioneAll = findViewById(R.id.nazioneAll);
        telefonoAll = findViewById(R.id.telefonoAll);
        faxAll = findViewById(R.id.faxAll);
        partitaIvaAll = findViewById(R.id.partitaIvaAll);
        emailAll = findViewById(R.id.emailAll);

        nome = findViewById(R.id.name);
        cognome = findViewById(R.id.surname);
        ruoloinazienda = findViewById(R.id.role);

        ragioneSocialeEsp = findViewById(R.id.ragioneSocialeEsp);
        indirizzoEsp = findViewById(R.id.indirizzoEsp);
        capEsp = findViewById(R.id.capEsp);
        comuneEsp = findViewById(R.id.comuneEsp);
        provinciaEsp = findViewById(R.id.provinciaEsp);
        nazioneEsp = findViewById(R.id.nazioneEsp);
        telefonoEsp = findViewById(R.id.telefonoEsp);
        faxEsp = findViewById(R.id.faxEsp);
        partitaIvaEsp = findViewById(R.id.partitaIvaEsp);
        emailEsp = findViewById(R.id.emailEsp);
        standEsp = findViewById(R.id.standEsp);
        padiglioneEsp = findViewById(R.id.padiglioneEsp);

        note = findViewById(R.id.note);

        provinciaAllEstero = findViewById(R.id.provinciaAllEstero);
        comuneAllEstero = findViewById(R.id.comuneAllEstero);

        provinciaEspEstero = findViewById(R.id.provinciaEspEstero);
        comuneEspEstero = findViewById(R.id.comuneEspEstero);

        totalAmount = 0;

        partitaIvaAll.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                partitaIvaAll.setText(partitaIvaAll.getText().toString().toUpperCase());
            }
        });
        partitaIvaEsp.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                partitaIvaEsp.setText(partitaIvaEsp.getText().toString().toUpperCase());
            }
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == radioButtonAll.getId()) {
                textViewAll.setVisibility(View.VISIBLE);
                layoutAll.setVisibility(View.VISIBLE);
            } else {
                textViewAll.setVisibility(View.GONE);
                layoutAll.setVisibility(View.GONE);
            }
        });

        final List<State> nazioni = databaseHelper.getStates();
        final List<String> nazioniNames = new ArrayList<>();
        nazioniCodes = new ArrayList<>();
        for(State state : nazioni){
            nazioniNames.add(state.getName());
            nazioniCodes.add(state.getCode());
        }
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nazioniNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nazioneAll.setAdapter(spinnerArrayAdapter);
        nazioneEsp.setAdapter(spinnerArrayAdapter);
        nazioneAll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(nazioniNames.get(position).equals(ITALY)){
                    provinciaAll.setVisibility(View.VISIBLE);
                    comuneAll.setVisibility(View.VISIBLE);
                    provinciaAllEstero.setVisibility(View.GONE);
                    comuneAllEstero.setVisibility(View.GONE);
                }
                else{
                    capAll.setText("");
                    provinciaAll.setVisibility(View.GONE);
                    comuneAll.setVisibility(View.GONE);
                    provinciaAllEstero.setVisibility(View.VISIBLE);
                    comuneAllEstero.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        nazioneAll.setSelection(nazioniNames.indexOf(ITALY));

        nazioneEsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(nazioniNames.get(position).equals(ITALY)){
                    provinciaEsp.setVisibility(View.VISIBLE);
                    comuneEsp.setVisibility(View.VISIBLE);
                    provinciaEspEstero.setVisibility(View.GONE);
                    comuneEspEstero.setVisibility(View.GONE);
                }
                else{
                    capEsp.setText("");
                    provinciaEsp.setVisibility(View.GONE);
                    comuneEsp.setVisibility(View.GONE);
                    provinciaEspEstero.setVisibility(View.VISIBLE);
                    comuneEspEstero.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        nazioneEsp.setSelection(nazioniNames.indexOf(ITALY));

        final List<Province> province = databaseHelper.getProvinces();
        final List<String> provinceNames = new ArrayList<>();
        provinceNames.add(getString(R.string.select_town));
        provinceCodes = new ArrayList<>();
        for(Province provincia : province){
            provinceNames.add(provincia.getName());
            provinceCodes.add(provincia.getCode());
        }
        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceNames);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinciaAll.setAdapter(spinnerArrayAdapter2);
        provinciaEsp.setAdapter(spinnerArrayAdapter2);
        provinciaAll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comuniAllCaps.clear();
                comuniAllCodes.clear();
                comuneAll.setAdapter(null);
                capAll.setText("");
                if(position != 0) {
                    new AsyncTaskGetComuni(ServicesActivity.this).execute(provinceCodes.get(position - 1), "true");
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        provinciaEsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comuniEspCaps.clear();
                comuniEspCodes.clear();
                comuneEsp.setAdapter(null);
                capEsp.setText("");
                if(position != 0) {
                    new AsyncTaskGetComuni(ServicesActivity.this).execute(provinceCodes.get(position - 1), "false");
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        comuniAllCaps = new ArrayList<>();
        comuniAllCodes = new ArrayList<>();
        comuniEspCaps = new ArrayList<>();
        comuniEspCodes = new ArrayList<>();

        comuneAll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!comuniAllCaps.isEmpty() && position != 0) {
                    capAll.setText(comuniAllCaps.get(position - 1));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        comuneEsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!comuniEspCaps.isEmpty() && position != 0) {
                    capEsp.setText(comuniEspCaps.get(position - 1));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_CODE && resultCode == RESULT_OK) {
            signatureInByte = data.getByteArrayExtra(EXTRA_SIGNATURE);
            sendFormAfterSignature();
        } else if(requestCode == REQUEST_SEARCH_CODE && resultCode == RESULT_OK){
            modulo = new Modulo();
            getOldFormById(data.getIntExtra(EXTRA_SEARCH, -1));
        } else {
            confirmButton.setClickable(true);
        }
    }

    protected void startSignatureActivity(){
        Intent signIntent = new Intent(this, SignatureActivity.class);
        startActivityForResult(signIntent, REQUEST_SIGN_CODE);
    }

    protected void sendFormAfterSignature(){
        progressDialog = ProgressDialog.show(this, "", getString(R.string.loading), true);
        Gson gson = new Gson();
        String json = gson.toJson(modulo);
        System.out.println(json);
        new AsyncTaskSetModulo(this).execute(json);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_services, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        if (item.getItemId() == R.id.action_logout) {
            edit.putBoolean(PREFS_USER_LOGGED, false);
            edit.putString(PREFS_USER_EMAIL, "");
            edit.putInt(PREFS_USER_ID, -1);
            edit.putInt(PREFS_USER_ROLE, -1);
            edit.putInt(PREFS_CONTROL_CODE, -1);
            edit.putBoolean(PREFS_IS_ITA, true);
            edit.putInt(PREFS_MANIFESTATION_VALUE, -1);
            edit.putString(PREFS_MANIFESTATION_NAME, "");
            edit.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else if(item.getItemId() == R.id.action_change_form){
            edit.putInt(PREFS_CONTROL_CODE, SERVICES_CODE);
            edit.apply();
            Intent intent = new Intent(this, ChooseServiceActivity.class);
            startActivity(intent);
            finish();
        } else if(item.getItemId() == R.id.action_find_form){
            Intent intent = new Intent(this, SearchListActivity.class);
            intent.putExtra(EXTRA_IDMODULO, modulo.IdModulo);
            startActivityForResult(intent, REQUEST_SEARCH_CODE);
        }
        return true;
    }

    private static class AsyncTaskGetComuni extends AsyncTask<String, Void, SoapObject> {
        private WeakReference<ServicesActivity> activityReference;
        private boolean isAll;

        AsyncTaskGetComuni(ServicesActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETCOMUNI);
            request.addProperty("siglaprovincia", params[0]);
            isAll = Boolean.parseBoolean(params[1]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_GETCOMUNI, envelope);
                return (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject comuni) {
            ServicesActivity activity = activityReference.get();
            if(comuni != null){
                final List<String> comuniNames = new ArrayList<>();
                comuniNames.add(activity.getString(R.string.select_municipality));
                for (int i = 0; i < comuni.getPropertyCount(); i++) {
                    SoapObject comune = (SoapObject) comuni.getProperty(i);
                    comuniNames.add(comune.getProperty(1).toString());
                    if(isAll) {
                        activity.comuniAllCodes.add(comune.getProperty(0).toString());
                        String cap = comune.getProperty(2).toString();
                        if(cap.equals("0")){
                            cap = "";
                        }
                        activity.comuniAllCaps.add(cap);
                    } else {
                        activity.comuniEspCodes.add(comune.getProperty(0).toString());
                        String cap = comune.getProperty(2).toString();
                        if(cap.equals("0")){
                            cap = "";
                        }
                        activity.comuniEspCaps.add(cap);
                    }
                }
                final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, comuniNames);
                spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if(isAll) {
                    activity.comuneAll.setAdapter(spinnerArrayAdapter2);
                }
                else {
                    activity.comuneEsp.setAdapter(spinnerArrayAdapter2);
                }
            }else{
                Toast.makeText(activity, activity.getString(R.string.error_getcomuni), Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void setAllInEspToForm(){
        modulo.setAllInEsp(ragioneSocialeEsp.getText().toString(),
                indirizzoEsp.getText().toString(),
                capEsp.getText().toString(),
                nazioneEsp.getSelectedItem().toString().equals(ITALY) && !comuniEspCodes.isEmpty()? comuniEspCodes.get(comuneEsp.getSelectedItemPosition() - 1) : "0",
                nazioneEsp.getSelectedItem().toString().equals(ITALY) && comuneEsp.getSelectedItem() != null ? comuneEsp.getSelectedItem().toString() : comuneEspEstero.getText().toString(),
                nazioneEsp.getSelectedItem().toString().equals(ITALY) && provinceCodes.isEmpty() ? provinceCodes.get(provinciaEsp.getSelectedItemPosition() - 1) : provinciaEspEstero.getText().toString(),
                nazioniCodes.get(nazioneEsp.getSelectedItemPosition()),
                telefonoEsp.getText().toString(),
                faxEsp.getText().toString(),
                partitaIvaEsp.getText().toString(),
                emailEsp.getText().toString(),
                standEsp.getText().toString(),
                padiglioneEsp.getText().toString(),
                nome.getText().toString(),
                cognome.getText().toString(),
                ruoloinazienda.getText().toString(),
                note.getText().toString());
    }

    protected void setAllToForm(){
        modulo.setAll(ragioneSocialeAll.getText().toString(),
                indirizzoAll.getText().toString(),
                capAll.getText().toString(),
                nazioneAll.getSelectedItem().toString().equals(ITALY) ? comuniAllCodes.get(comuneAll.getSelectedItemPosition() - 1) : "0",
                nazioneAll.getSelectedItem().toString().equals(ITALY) ? comuneAll.getSelectedItem().toString() : comuneAllEstero.getText().toString(),
                nazioneAll.getSelectedItem().toString().equals(ITALY) ? provinceCodes.get(provinciaAll.getSelectedItemPosition() - 1) : provinciaAllEstero.getText().toString(),
                nazioniCodes.get(nazioneAll.getSelectedItemPosition()),
                telefonoAll.getText().toString(),
                faxAll.getText().toString(),
                partitaIvaAll.getText().toString(),
                emailAll.getText().toString());
    }

    protected void setEspToForm(){
        modulo.setEsp(ragioneSocialeEsp.getText().toString(),
                indirizzoEsp.getText().toString(),
                capEsp.getText().toString(),
                nazioneEsp.getSelectedItem().toString().equals(ITALY) ? comuniEspCodes.get(comuneEsp.getSelectedItemPosition() - 1) : "0",
                nazioneEsp.getSelectedItem().toString().equals(ITALY) ? comuneEsp.getSelectedItem().toString() : comuneEspEstero.getText().toString(),
                nazioneEsp.getSelectedItem().toString().equals(ITALY) ? provinceCodes.get(provinciaEsp.getSelectedItemPosition() - 1) : provinciaEspEstero.getText().toString(),
                nazioniCodes.get(nazioneEsp.getSelectedItemPosition()),
                telefonoEsp.getText().toString(),
                faxEsp.getText().toString(),
                partitaIvaEsp.getText().toString(),
                emailEsp.getText().toString(),
                standEsp.getText().toString(),
                padiglioneEsp.getText().toString(),
                nome.getText().toString(),
                cognome.getText().toString(),
                ruoloinazienda.getText().toString(),
                note.getText().toString());
    }

    protected boolean amountError(){
        return totalAmount == 0;
    }

    protected boolean espError(){
        return isEmpty(ragioneSocialeEsp) || isEmpty(standEsp) || isEmpty(padiglioneEsp);
    }

    protected boolean allError(){
        return isEmpty(ragioneSocialeAll) || isEmpty(indirizzoAll) || isEmpty(telefonoAll)
                || (nazioneAll.getSelectedItem().toString().equals(ITALY) && (provinciaAll.getSelectedItemPosition() == 0 || comuneAll.getSelectedItemPosition() == 0))
                || (!nazioneAll.getSelectedItem().toString().equals(ITALY) && (isEmpty(provinciaAllEstero) || isEmpty(comuneAllEstero)));
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    protected static boolean isNumeric(String strNum) {
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    protected void exitFromActivity(){
        Toast.makeText(this, getString(R.string.error_getdata), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ChooseServiceActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    protected static class AsyncTaskSetModulo extends AsyncTask<String, Void, SoapPrimitive> {
        private WeakReference<ServicesActivity> activityReference;

        public AsyncTaskSetModulo(ServicesActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapPrimitive doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_SETMODULO);
            request.addProperty("js", params[0]);
            request.addProperty("lingua", activityReference.get().sharedPreferences.getBoolean(PREFS_IS_ITA, true) ? "ITA" : "ENG");
            request.addProperty("firma", activityReference.get().signatureInByte);
            request.addProperty("foto1", activityReference.get().imagesInByte[0]);
            request.addProperty("foto2", activityReference.get().imagesInByte[1]);
            request.addProperty("foto3", activityReference.get().imagesInByte[2]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            new MarshalBase64().register(envelope);

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_SETMODULO, envelope);
                return (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapPrimitive response) {
            ServicesActivity activity = activityReference.get();
            if(response != null){
                activity.progressDialog.dismiss();
                Toast.makeText(activity, activity.getString(R.string.load_ok), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(activity, ChooseServiceActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }else{
                Toast.makeText(activity, activity.getString(R.string.error_generic), Toast.LENGTH_LONG).show();
                activity.confirmButton.setClickable(true);
                activity.progressDialog.dismiss();
            }
        }
    }

    protected void setAllNotClickable(){
        findViewById(R.id.radioButtonAll).setEnabled(false);
        findViewById(R.id.radioButtonEsp).setEnabled(false);

        ragioneSocialeAll.setEnabled(false);
        indirizzoAll.setEnabled(false);
        nazioneAll.setEnabled(false);
        provinciaAll.setEnabled(false);
        comuneAll.setEnabled(false);
        capAll.setEnabled(false);
        telefonoAll.setEnabled(false);
        faxAll.setEnabled(false);
        partitaIvaAll.setEnabled(false);
        emailAll.setEnabled(false);

        nome.setEnabled(false);
        cognome.setEnabled(false);
        ruoloinazienda.setEnabled(false);

        ragioneSocialeEsp.setEnabled(false);
        indirizzoEsp.setEnabled(false);
        capEsp.setEnabled(false);
        comuneEsp.setEnabled(false);
        provinciaEsp.setEnabled(false);
        nazioneEsp.setEnabled(false);
        telefonoEsp.setEnabled(false);
        faxEsp.setEnabled(false);
        partitaIvaEsp.setEnabled(false);
        emailEsp.setEnabled(false);
        standEsp.setEnabled(false);
        padiglioneEsp.setEnabled(false);

        provinciaAllEstero.setEnabled(false);
        comuneAllEstero.setEnabled(false);

        provinciaEspEstero.setEnabled(false);
        comuneEspEstero.setEnabled(false);

        note.setEnabled(false);
    }

    //questo metodo viene chiamato dopo aver firmato, serve per caricare le edittext nella classe modulo
    protected void setEditTextToForm(){
        if (radioButtonAll.isChecked()) {
            setAllToForm();
        }
        setEspToForm();
    }

    //permette di inviare il modulo (form) al webservice
    protected abstract void sendForm(int idKey);

    //questo metodo carica il vecchio modulo nelle edittext
    protected abstract void setOldFormToEditText();

    //mi preparo per ottenere i dati del vecchio modulo
    protected abstract void getOldFormById(int idKey);

    protected static class AsyncTaskGetModulo extends AsyncTask<Integer, Void, SoapPrimitive> {
        private WeakReference<ServicesActivity> activityReference;

        public AsyncTaskGetModulo(ServicesActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapPrimitive doInBackground(Integer... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETMODULO);
            request.addProperty("id", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_GETMODULO, envelope);
                return (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapPrimitive response) {
            ServicesActivity activity = activityReference.get();
            if(response != null){
                Gson gson = new GsonBuilder().create();
                activity.modulo = gson.fromJson(response.toString(), Modulo.class);

                System.out.println(response.toString());
                System.out.println(activity.modulo.toString());

                activity.setOldFormToEditText();
            }else{
                activity.exitFromActivity();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.quit))
                .setMessage(getString(R.string.data_will_be_deleted))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> finish())
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
}
