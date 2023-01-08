package com.vivaticket.controlli.activity.segnalazioni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.vivaticket.controlli.R;
import com.vivaticket.controlli.activity.LoginActivity;
import com.vivaticket.controlli.util.Helper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;
import static com.vivaticket.controlli.util.Helper.METHOD_GETGRUPPI;
import static com.vivaticket.controlli.util.Helper.METHOD_SETSEGNALAZIONI;
import static com.vivaticket.controlli.util.Helper.NAMESPACE;
import static com.vivaticket.controlli.util.Helper.PREFS_CONTROL_CODE;
import static com.vivaticket.controlli.util.Helper.PREFS_IS_ITA;
import static com.vivaticket.controlli.util.Helper.PREFS_MANIFESTATION_NAME;
import static com.vivaticket.controlli.util.Helper.PREFS_MANIFESTATION_VALUE;
import static com.vivaticket.controlli.util.Helper.PREFS_USER_EMAIL;
import static com.vivaticket.controlli.util.Helper.PREFS_NAME;
import static com.vivaticket.controlli.util.Helper.PREFS_USER_ID;
import static com.vivaticket.controlli.util.Helper.PREFS_USER_LOGGED;
import static com.vivaticket.controlli.util.Helper.PREFS_USER_ROLE;
import static com.vivaticket.controlli.util.Helper.SIGNALATIONS_CODE;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_GETGRUPPI;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_SETSEGNALAZIONI;
import static com.vivaticket.controlli.util.Helper.URL;

public class SignalationsActivity extends AppCompatActivity {

    private static final int PHOTO_NUM = 3;

    private SharedPreferences sharedPreferences;
    private EditText notes;
    private Spinner groupsSpinner;
    private List<String> groupsNames;
    private List<Integer> groupsValues;
    private ImageView[] imageViews;
    private byte[][] imagesInByte;
    private File[] images;
    private Button confirmButton;
    private ProgressDialog progressDialog;
    private boolean groupsNotObtained;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalations);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(PREFS_CONTROL_CODE, SIGNALATIONS_CODE);
        edit.apply();

        notes = findViewById(R.id.notes);
        groupsSpinner = findViewById(R.id.categoriesSpinner);
        confirmButton = findViewById(R.id.button);

        imageViews = new ImageView[PHOTO_NUM];
        imageViews[0] = findViewById(R.id.imageView5);
        imageViews[1] = findViewById(R.id.imageView6);
        imageViews[2] = findViewById(R.id.imageView7);
        imageViews[0].setOnClickListener(e-> startCamera(0));
        imageViews[1].setOnClickListener(e-> startCamera(1));
        imageViews[2].setOnClickListener(e-> startCamera(2));
        images = new File[PHOTO_NUM];
        imagesInByte = new byte[PHOTO_NUM][];

        groupsNames = new ArrayList<>();
        groupsValues = new ArrayList<>();

        confirmButton.setOnClickListener(e-> {
            if(notes.getText().toString().equals("") && imagesInByte[0] == null &&
                    imagesInByte[1] == null && imagesInByte[2] == null){
                Toast.makeText(this, "Inserire delle note o fare una foto!", Toast.LENGTH_LONG).show();
            } else {
                confirmButton.setClickable(false);
                progressDialog = ProgressDialog.show(this, "", "Caricamento in corso…", true);
                new AsyncTaskSetSengalazioni(SignalationsActivity.this).execute(
                        Integer.toString(sharedPreferences.getInt(PREFS_USER_ID, -1)),
                        notes.getText().toString(),
                        groupsValues.get((int) groupsSpinner.getSelectedItemId()).toString());
            }
        });
        confirmButton.setClickable(false);
        Helper.requestPermissions(this);
        new AsyncTaskGetGruppi(SignalationsActivity.this).execute();
    }

    private void startCamera(int index){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        images[index] = new File(getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(images[index]));
        startActivityForResult(intent, index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode >= 0 && requestCode < PHOTO_NUM && resultCode == RESULT_OK){
            Uri selectedImage = Uri.fromFile(images[requestCode]);
            imageViews[requestCode].setImageURI(selectedImage);
            if(imageViews[requestCode].getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) imageViews[requestCode].getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                imagesInByte[requestCode] = baos.toByteArray();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageViews[requestCode].setLayoutParams(lp);
            } else {
                imageViews[requestCode].setImageResource(R.drawable.ic_photo_camera_black_24dp);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageViews[requestCode].setLayoutParams(lp);
                imagesInByte[requestCode] = null;
            }
            if (images[requestCode] != null && images[requestCode].exists()) {
                images[requestCode].delete();
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(groupsNotObtained){
            groupsNotObtained = false;
            new AsyncTaskGetGruppi(SignalationsActivity.this).execute();
        }
    }


    private static class AsyncTaskGetGruppi extends AsyncTask<Void, Void, SoapObject> {
        private WeakReference<SignalationsActivity> activityReference;

        AsyncTaskGetGruppi(SignalationsActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETGRUPPI);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION_GETGRUPPI, envelope);
                return (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject courses) {
            SignalationsActivity activity = activityReference.get();
            if(courses != null) {
                if(courses.getPropertyCount() > 0) {
                    for (int i = 0; i < courses.getPropertyCount(); i++) {
                        SoapObject course = (SoapObject) courses.getProperty(i);
                        activity.groupsValues.add(Integer.parseInt(course.getProperty(0).toString()));
                        activity.groupsNames.add(course.getProperty(1).toString());
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, activity.groupsNames);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    activity.groupsSpinner.setAdapter(spinnerArrayAdapter);
                }
                activity.confirmButton.setClickable(true);
            } else {
                Toast.makeText(activity, "Errore di connessione.\nImpossibile ottenere i gruppi.", Toast.LENGTH_LONG).show();
                activity.groupsNotObtained = true;
            }
        }
    }


    private static class AsyncTaskSetSengalazioni extends AsyncTask<String, Void, Integer> {
        private WeakReference<SignalationsActivity> activityReference;

        AsyncTaskSetSengalazioni(SignalationsActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Integer doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_SETSEGNALAZIONI);
            request.addProperty("idutilizzatore", params[0]);
            request.addProperty("foto1", activityReference.get().imagesInByte[0]);
            request.addProperty("foto2", activityReference.get().imagesInByte[1]);
            request.addProperty("foto3", activityReference.get().imagesInByte[2]);
            request.addProperty("note", params[1]);
            request.addProperty("idgruppo", params[2]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            new MarshalBase64().register(envelope);

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_SETSEGNALAZIONI, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                return Integer.parseInt(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer response) {
            SignalationsActivity activity = activityReference.get();
            if(response == 1) {
                Toast.makeText(activity, "Dati inviati correttamente", Toast.LENGTH_LONG).show();
                activity.notes.setText("");
                for(int i = 0; i < 3; i++){
                    activity.imagesInByte[i] = null;
                    activity.imageViews[i].setImageResource(R.drawable.ic_photo_camera_black_24dp);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    activity.imageViews[i].setLayoutParams(lp);
                }

            } else if(response == 0){
                Toast.makeText(activity, activity.getString(R.string.error_generic), Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(activity, "Procedura non andata a buon fine.", Toast.LENGTH_LONG).show();
            }
            activity.progressDialog.dismiss();
            activity.confirmButton.setClickable(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signalations, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            final SharedPreferences.Editor edit = sharedPreferences.edit();
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
        }
        return true;
    }

}
