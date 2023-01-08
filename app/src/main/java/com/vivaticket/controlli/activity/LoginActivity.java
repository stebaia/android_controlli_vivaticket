package com.supergianlu.controlli.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.supergianlu.controlli.R;
import com.supergianlu.controlli.util.Helper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.ref.WeakReference;

import static com.supergianlu.controlli.util.Helper.METHOD_LOGINUTENTE;
import static com.supergianlu.controlli.util.Helper.NAMESPACE;
import static com.supergianlu.controlli.util.Helper.PREFS_USER_EMAIL;
import static com.supergianlu.controlli.util.Helper.PREFS_NAME;
import static com.supergianlu.controlli.util.Helper.PREFS_USER_ID;
import static com.supergianlu.controlli.util.Helper.PREFS_USER_LOGGED;
import static com.supergianlu.controlli.util.Helper.PREFS_USER_ROLE;
import static com.supergianlu.controlli.util.Helper.SOAP_ACTION_LOGINUTENTE;
import static com.supergianlu.controlli.util.Helper.URL;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TextView email;
    private TextView password;
    private Button confirmButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        email.setText(sharedPreferences.getString(PREFS_USER_EMAIL, ""));

        //VERIFICO LE CREDENZIALI E PASSO ALL'ALTRA SCHERMATA
        confirmButton = findViewById(R.id.button);
        confirmButton.setOnClickListener(view -> {
            //nascondi la tastiera
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputManager != null && getCurrentFocus() != null) {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            if(email.getText().toString().equals("")){
                Toast.makeText(this, "Inserire una email.", Toast.LENGTH_LONG).show();
            } else if (password.getText().toString().equals("")){
                Toast.makeText(this, "Inserire una password.", Toast.LENGTH_LONG).show();
            } else {
                doLogin();
            }
        });

        Helper.requestPermissions(this);
    }

    //QUANDO PREMO IL PULSANTE ACCEDI
    private void doLogin() {
        progressDialog = ProgressDialog.show(this, "", "Verifica in corso…", true);
        confirmButton.setClickable(false);
        new AsyncTaskLoginUtente(LoginActivity.this).execute(email.getText().toString(), password.getText().toString());
    }


    private static class AsyncTaskLoginUtente extends AsyncTask<String, Void, SoapObject> {
        private WeakReference<LoginActivity> activityReference;

        AsyncTaskLoginUtente(LoginActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapObject doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LOGINUTENTE);
            request.addProperty("str", email + "," + password);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_LOGINUTENTE, envelope);
                return (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject userData) {
            LoginActivity activity = activityReference.get();
            if(userData != null){
                int userId = Integer.parseInt((userData.getProperty(0)).toString());
                if (userId == 0) {
                    Toast.makeText(activity, "Credenziali errate.", Toast.LENGTH_LONG).show();
                    activity.confirmButton.setClickable(true);
                    activity.progressDialog.dismiss();
                } else {
                    int roleId = Integer.parseInt((userData.getProperty(1)).toString());
                    final SharedPreferences.Editor editor = activity.sharedPreferences.edit();
                    editor.putString(PREFS_USER_EMAIL, activity.email.getText().toString());
                    editor.putInt(PREFS_USER_ID, userId);
                    editor.putInt(PREFS_USER_ROLE, roleId);
                    editor.putBoolean(PREFS_USER_LOGGED, true);
                    editor.apply();
                    activity.progressDialog.dismiss();
                    Intent intent = new Intent(activity, ChooseControlActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }else{
                Toast.makeText(activity, activity.getString(R.string.error_generic), Toast.LENGTH_LONG).show();
                activity.confirmButton.setClickable(true);
                activity.progressDialog.dismiss();
            }
        }
    }

}
