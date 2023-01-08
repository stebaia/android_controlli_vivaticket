package com.vivaticket.controlli.activity.servizi.moduli;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vivaticket.controlli.R;
import com.vivaticket.controlli.ValoriN2;
import com.vivaticket.controlli.activity.servizi.ServicesActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;

import static android.os.Environment.getExternalStorageDirectory;
import static android.view.View.GONE;
import static com.vivaticket.controlli.util.Helper.METHOD_GETINFON2;
import static com.vivaticket.controlli.util.Helper.METHOD_GETTESTIN2;
import static com.vivaticket.controlli.util.Helper.N2_CODE;
import static com.vivaticket.controlli.util.Helper.NAMESPACE;
import static com.vivaticket.controlli.util.Helper.PREFS_IS_ITA;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_GETINFON2;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_GETTESTIN2;
import static com.vivaticket.controlli.util.Helper.STATUS_COMPLETE;
import static com.vivaticket.controlli.util.Helper.STATUS_INCOMPLETE;
import static com.vivaticket.controlli.util.Helper.URL;

public class ModN2Activity extends ServicesActivity {

    private EditText[] editTextQuantities;
    private TextView[] textViewsRubbish;
    private TextView[] textViewsUnitaryAmounts;
    private TextView[] textViewsTotalAmounts;
    private int[] rubbishId;
    private double[] unitaryAmounts;
    private double[] totalAmounts;

    private TextView message1;
    private TextView message2;
    private TextView message3;

    private ImageView[] imageViews;
    private File[] images;

    private CheckBox rifiuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_n2);

        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        modulo.IdModulo = N2_CODE;

        titleText.setText(getString(R.string.mod_n2));

        textViewAll.setVisibility(GONE);
        layoutAll.setVisibility(GONE);
        textViewEsp.setVisibility(GONE);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = findViewById(checkedId);
            //in N2 non deve fare nulla il click sui radiobutton
        });

        message1 = findViewById(R.id.message1_n2);
        message2 = findViewById(R.id.message2_n2);
        message3 = findViewById(R.id.message3_n2);

        editTextQuantities = new EditText[5];
        textViewsRubbish = new TextView[5];
        textViewsUnitaryAmounts = new TextView[5];
        textViewsTotalAmounts = new TextView[5];
        unitaryAmounts = new double[5];
        totalAmounts = new double[5];
        rubbishId = new int[5];

        rifiuta = findViewById(R.id.checkBoxRifiuta);

        textViewsRubbish[0] = findViewById(R.id.rifiuto1);
        textViewsRubbish[1] = findViewById(R.id.rifiuto2);
        textViewsRubbish[2] = findViewById(R.id.rifiuto3);
        textViewsRubbish[3] = findViewById(R.id.rifiuto4);
        textViewsRubbish[4] = findViewById(R.id.rifiuto5);

        textViewsUnitaryAmounts[0] = findViewById(R.id.unitaryAmount1);
        textViewsUnitaryAmounts[1] = findViewById(R.id.unitaryAmount2);
        textViewsUnitaryAmounts[2] = findViewById(R.id.unitaryAmount3);
        textViewsUnitaryAmounts[3] = findViewById(R.id.unitaryAmount4);
        textViewsUnitaryAmounts[4] = findViewById(R.id.unitaryAmount5);

        textViewsTotalAmounts[0] = findViewById(R.id.totalAmount1_n2);
        textViewsTotalAmounts[1] = findViewById(R.id.totalAmount2_n2);
        textViewsTotalAmounts[2] = findViewById(R.id.totalAmount3_n2);
        textViewsTotalAmounts[3] = findViewById(R.id.totalAmount4_n2);
        textViewsTotalAmounts[4] = findViewById(R.id.totalAmount5_n2);

        unitaryAmounts[0] = 0;
        unitaryAmounts[1] = 0;
        unitaryAmounts[2] = 0;
        unitaryAmounts[3] = 0;
        unitaryAmounts[4] = 0;

        editTextQuantities[0] = findViewById(R.id.quantita1);
        editTextQuantities[1] = findViewById(R.id.quantita2);
        editTextQuantities[2] = findViewById(R.id.quantita3);
        editTextQuantities[3] = findViewById(R.id.quantita4);
        editTextQuantities[4] = findViewById(R.id.quantita5);

        for(int i = 0; i < 5; i++){
            final int j = i;
            editTextQuantities[j].addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(isNumeric(s.toString())){
                        totalAmounts[j] = Double.parseDouble(s.toString()) * unitaryAmounts[j];
                    } else {
                        totalAmounts[j] = 0 * unitaryAmounts[j];
                    }
                    textViewsTotalAmounts[j].setText("€ " + df2.format(totalAmounts[j]));

                    totalAmount = 0;
                    for(int k = 0; k < 5; k++){
                        totalAmount = totalAmount + totalAmounts[k];
                    }
                    textViewTotalAmount.setText("€ " + df2.format(totalAmount));
                }
            });
        }

        imageViews = new ImageView[PHOTO_NUM];
        imageViews[0] = findViewById(R.id.imageView5);
        imageViews[1] = findViewById(R.id.imageView6);
        imageViews[2] = findViewById(R.id.imageView7);
        imageViews[0].setOnClickListener(e-> startCamera(0));
        imageViews[1].setOnClickListener(e-> startCamera(1));
        imageViews[2].setOnClickListener(e-> startCamera(2));
        images = new File[PHOTO_NUM];

        new AsyncTaskGetInfoN2(this).execute();
        new AsyncTaskGetTestiN2(this).execute();

        confirmButton.setOnClickListener(e -> {
            confirmButton.setClickable(false);

            if(espError()){
                confirmButton.setClickable(true);
                if(radioButtonAll.isChecked()) {
                    Toast.makeText(this, getString(R.string.fields_error_all), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getString(R.string.fields_error_esp), Toast.LENGTH_LONG).show();
                }
            } else if(amountError()) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.form_not_completed_title))
                        .setMessage(getString(R.string.undersigned) + (rifiuta.isChecked() ?
                                getString(R.string.refuse_service) : getString(R.string.accept_service)))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                            sendForm(STATUS_INCOMPLETE);
                        })
                        .setNegativeButton(getString(R.string.no), (dialog, id) -> {
                            confirmButton.setClickable(true);
                        })
                        .show();
            } else if(imagesInByte[0] == null && imagesInByte[1] == null && imagesInByte[2] == null) {
                confirmButton.setClickable(true);
                Toast.makeText(this, getString(R.string.photos_error), Toast.LENGTH_LONG).show();
            } else {
                sendForm(STATUS_COMPLETE);
            }
        });
    }

    @Override
    protected void sendForm(int idStatus){
        modulo.IdStatus = idStatus;
        if(idStatus == STATUS_INCOMPLETE){
            setEditTextToForm();
            modulo.Rifiuta = rifiuta.isChecked() ? 1 : 0;
            //startSignatureActivity();
            sendFormAfterSignature();
        } else if(idStatus == STATUS_COMPLETE && modulo.Id != 0) {
            modulo.Note = note.getText().toString();
            setN2Data();
            sendFormAfterSignature();
        } else if(idStatus == STATUS_COMPLETE) {
            setEditTextToForm();
            setN2Data();
            modulo.Rifiuta = rifiuta.isChecked() ? 1 : 0;
            startSignatureActivity();
        }
    }

    private void setN2Data(){
        ValoriN2[] valoris = new ValoriN2[5];
        for (int i = 0; i < 5; i++) {
            double quantity = 0;
            if (isNumeric(editTextQuantities[i].getText().toString())) {
                quantity = Double.parseDouble(editTextQuantities[i].getText().toString());
            }
            valoris[i] = new ValoriN2(rubbishId[i], quantity, totalAmounts[i]);
        }
        modulo.setN2(valoris, totalAmount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode >= 0 && requestCode < PHOTO_NUM && resultCode == RESULT_OK) {
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

    private void startCamera(int index){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        images[index] = new File(getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(images[index]));
        startActivityForResult(intent, index);
    }

    @Override
    protected void setEditTextToForm() {
        if (radioButtonAll.isChecked()) {
            setAllInEspToForm();
        } else {
            setEspToForm();
        }
    }

    @Override
    protected void getOldFormById(int idKey) {
        if(idKey != -1){
            new AsyncTaskGetModulo(this).execute(idKey);
            setAllNotClickable();
            rifiuta.setEnabled(false);
            note.setEnabled(true);
            confirmButton.setText(getString(R.string.confirm));
            confirmButton.setOnClickListener(e -> {
                confirmButton.setClickable(false);
                if(amountError()) {
                    confirmButton.setClickable(true);
                    Toast.makeText(this, getString(R.string.amount_error), Toast.LENGTH_LONG).show();
                } else if (imagesInByte[0] == null && imagesInByte[1] == null && imagesInByte[2] == null) {
                    confirmButton.setClickable(true);
                    Toast.makeText(this, getString(R.string.photos_error), Toast.LENGTH_LONG).show();
                } else {
                    sendForm(STATUS_COMPLETE);
                }
            });
        }
    }

    @Override
    protected void setOldFormToEditText() {
        confirmButton.setClickable(true);
        if(modulo.AllRagionesociale == null) {
            radioButtonEsp.setChecked(true);
            ragioneSocialeEsp.setText(modulo.EspRagionesociale);
            nazioneEsp.setSelection(nazioniCodes.indexOf(modulo.EspNazione));
            provinciaEsp.setVisibility(View.GONE);
            provinciaEspEstero.setVisibility(View.VISIBLE);
            provinciaEspEstero.setText(modulo.EspProvincia);
            comuneEsp.setVisibility(View.GONE);
            comuneEspEstero.setVisibility(View.VISIBLE);
            comuneEspEstero.setText(modulo.EspComune);
            capEsp.setText(modulo.EspCap);
            indirizzoEsp.setText(modulo.EspIndirizzo);
            telefonoEsp.setText(modulo.EspTelefono);
            faxEsp.setText(modulo.EspFax);
            partitaIvaEsp.setText(modulo.EspPivacf);
            emailEsp.setText(modulo.EspEmail);
        } else {
            radioButtonAll.setChecked(true);
            ragioneSocialeEsp.setText(modulo.AllRagionesociale);
            nazioneEsp.setSelection(nazioniCodes.indexOf(modulo.AllNazione));
            provinciaEsp.setVisibility(View.GONE);
            provinciaEspEstero.setVisibility(View.VISIBLE);
            provinciaEspEstero.setText(modulo.AllProvincia);
            comuneEsp.setVisibility(View.GONE);
            comuneEspEstero.setVisibility(View.VISIBLE);
            comuneEspEstero.setText(modulo.AllComune);
            capEsp.setText(modulo.AllCap);
            indirizzoEsp.setText(modulo.AllIndirizzo);
            telefonoEsp.setText(modulo.AllTelefono);
            faxEsp.setText(modulo.AllFax);
            partitaIvaEsp.setText(modulo.AllPivacf);
            emailEsp.setText(modulo.AllEmail);
        }
        nome.setText(modulo.Nome);
        cognome.setText(modulo.Cognome);
        ruoloinazienda.setText(modulo.Ruoloinazienda);

        standEsp.setText(modulo.EspStand);
        padiglioneEsp.setText(modulo.EspPadiglione);

        note.setText(modulo.Note);

        if (modulo.Rifiuta == 0) {
            rifiuta.setChecked(false);
        } else {
            rifiuta.setChecked(true);
        }
    }

    private static class AsyncTaskGetInfoN2 extends AsyncTask<Integer, Void, SoapObject> {
        private WeakReference<ModN2Activity> activityReference;

        AsyncTaskGetInfoN2(ModN2Activity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapObject doInBackground(Integer... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETINFON2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_GETINFON2, envelope);
                return (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject elementi) {
            ModN2Activity activity = activityReference.get();
            if(elementi != null){
                for (int i = 0; i < elementi.getPropertyCount(); i++) {
                    SoapObject elemento = (SoapObject) elementi.getProperty(i);
                    if(activity.sharedPreferences.getBoolean(PREFS_IS_ITA, true)) {
                        activity.textViewsRubbish[i].setText(elemento.getProperty("Descrizione").toString());
                    } else {
                        activity.textViewsRubbish[i].setText(elemento.getProperty("DescrizioneEng").toString());
                    }
                    activity.unitaryAmounts[i] = Double.parseDouble(elemento.getProperty("Prezzo").toString());
                    activity.rubbishId[i] = Integer.parseInt(elemento.getProperty("Idn2").toString());
                    activity.textViewsUnitaryAmounts[i].setText("€ " + df2.format(activity.unitaryAmounts[i]) + "/m³");
                }
            }else{
                activity.exitFromActivity();
            }
        }
    }

    private static class AsyncTaskGetTestiN2 extends AsyncTask<Integer, Void, SoapObject> {
        private WeakReference<ModN2Activity> activityReference;

        AsyncTaskGetTestiN2(ModN2Activity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapObject doInBackground(Integer... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETTESTIN2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_GETTESTIN2, envelope);
                return (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            ModN2Activity activity = activityReference.get();
            if(result != null){
                if(activity.sharedPreferences.getBoolean(PREFS_IS_ITA, true)) {
                    activity.message1.setText(result.getProperty("Testo1Ita").toString());
                    activity.message2.setText(result.getProperty("Testo2Ita").toString());
                    activity.message3.setText(result.getProperty("Testo3Ita").toString());
                } else {
                    activity.message1.setText(result.getProperty("Testo1Eng").toString());
                    activity.message2.setText(result.getProperty("Testo2Eng").toString());
                    activity.message3.setText(result.getProperty("Testo3Eng").toString());
                }
            }else{
                activity.exitFromActivity();
            }
        }
    }

}
