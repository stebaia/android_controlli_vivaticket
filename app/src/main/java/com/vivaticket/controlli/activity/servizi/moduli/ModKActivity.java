package com.vivaticket.controlli.activity.servizi.moduli;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vivaticket.controlli.R;
import com.vivaticket.controlli.ValoriK;
import com.vivaticket.controlli.activity.servizi.ServicesActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.ref.WeakReference;

import static com.vivaticket.controlli.util.Helper.K_CODE;
import static com.vivaticket.controlli.util.Helper.METHOD_GETINFOK;
import static com.vivaticket.controlli.util.Helper.NAMESPACE;
import static com.vivaticket.controlli.util.Helper.PREFS_IS_ITA;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_GETINFOK;
import static com.vivaticket.controlli.util.Helper.STATUS_COMPLETE;
import static com.vivaticket.controlli.util.Helper.STATUS_INCOMPLETE;
import static com.vivaticket.controlli.util.Helper.URL;

public class ModKActivity extends ServicesActivity {

    private LinearLayout layoutOrderModK[];
    private EditText meterB[];
    private EditText meterH[];
    private TextView metersTot[];
    private TextView metersTotCopy[];
    private EditText faces[];
    private TextView mqTot[];
    private TextView mqTotCopy[];
    private TextView mqAmount[];
    private TextView totalAmounts[];

    private double mqAmountValue;
    private int linearLayoutOpened;
    private ImageButton imageButton;

    private TextView message1;
    private TextView message2;
    private TextView message3;
    private TextView message4;
    private TextView message5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_k);

        super.onCreate(savedInstanceState);

        modulo.IdModulo = K_CODE;

        titleText.setText(getString(R.string.mod_k));

        message1 = findViewById(R.id.message1_k);
        message2 = findViewById(R.id.message2_k);
        message3 = findViewById(R.id.message3_k);
        message4 = findViewById(R.id.message4_k);
        message5 = findViewById(R.id.message5_k);
        message3.setPaintFlags(message3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        message4.setPaintFlags(message4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        imageButton = findViewById(R.id.addLineButton);

        layoutOrderModK = new LinearLayout[10];
        meterB = new EditText[10];
        meterH = new EditText[10];
        metersTot = new TextView[10];
        metersTotCopy = new TextView[10];
        faces = new EditText[10];
        mqTot = new TextView[10];
        mqTotCopy = new TextView[10];
        mqAmount = new TextView[10];
        totalAmounts = new TextView[10];

        layoutOrderModK[0] = findViewById(R.id.layoutOrderModK10);
        layoutOrderModK[1] = findViewById(R.id.layoutOrderModK20);
        layoutOrderModK[2] = findViewById(R.id.layoutOrderModK30);
        layoutOrderModK[3] = findViewById(R.id.layoutOrderModK40);
        layoutOrderModK[4] = findViewById(R.id.layoutOrderModK50);
        layoutOrderModK[5] = findViewById(R.id.layoutOrderModK60);
        layoutOrderModK[6] = findViewById(R.id.layoutOrderModK70);
        layoutOrderModK[7] = findViewById(R.id.layoutOrderModK80);
        layoutOrderModK[8] = findViewById(R.id.layoutOrderModK90);
        layoutOrderModK[9] = findViewById(R.id.layoutOrderModK100);

        meterB[0] = findViewById(R.id.meterB10);
        meterB[1] = findViewById(R.id.meterB20);
        meterB[2] = findViewById(R.id.meterB30);
        meterB[3] = findViewById(R.id.meterB40);
        meterB[4] = findViewById(R.id.meterB50);
        meterB[5] = findViewById(R.id.meterB60);
        meterB[6] = findViewById(R.id.meterB70);
        meterB[7] = findViewById(R.id.meterB80);
        meterB[8] = findViewById(R.id.meterB90);
        meterB[9] = findViewById(R.id.meterB100);

        meterH[0] = findViewById(R.id.meterH10);
        meterH[1] = findViewById(R.id.meterH20);
        meterH[2] = findViewById(R.id.meterH30);
        meterH[3] = findViewById(R.id.meterH40);
        meterH[4] = findViewById(R.id.meterH50);
        meterH[5] = findViewById(R.id.meterH60);
        meterH[6] = findViewById(R.id.meterH70);
        meterH[7] = findViewById(R.id.meterH80);
        meterH[8] = findViewById(R.id.meterH90);
        meterH[9] = findViewById(R.id.meterH100);

        metersTot[0] = findViewById(R.id.meterTot10);
        metersTot[1] = findViewById(R.id.meterTot20);
        metersTot[2] = findViewById(R.id.meterTot30);
        metersTot[3] = findViewById(R.id.meterTot40);
        metersTot[4] = findViewById(R.id.meterTot50);
        metersTot[5] = findViewById(R.id.meterTot60);
        metersTot[6] = findViewById(R.id.meterTot70);
        metersTot[7] = findViewById(R.id.meterTot80);
        metersTot[8] = findViewById(R.id.meterTot90);
        metersTot[9] = findViewById(R.id.meterTot100);

        metersTotCopy[0] = findViewById(R.id.meterTotCopy10);
        metersTotCopy[1] = findViewById(R.id.meterTotCopy20);
        metersTotCopy[2] = findViewById(R.id.meterTotCopy30);
        metersTotCopy[3] = findViewById(R.id.meterTotCopy40);
        metersTotCopy[4] = findViewById(R.id.meterTotCopy50);
        metersTotCopy[5] = findViewById(R.id.meterTotCopy60);
        metersTotCopy[6] = findViewById(R.id.meterTotCopy70);
        metersTotCopy[7] = findViewById(R.id.meterTotCopy80);
        metersTotCopy[8] = findViewById(R.id.meterTotCopy90);
        metersTotCopy[9] = findViewById(R.id.meterTotCopy100);

        faces[0] = findViewById(R.id.faces10);
        faces[1] = findViewById(R.id.faces20);
        faces[2] = findViewById(R.id.faces30);
        faces[3] = findViewById(R.id.faces40);
        faces[4] = findViewById(R.id.faces50);
        faces[5] = findViewById(R.id.faces60);
        faces[6] = findViewById(R.id.faces70);
        faces[7] = findViewById(R.id.faces80);
        faces[8] = findViewById(R.id.faces90);
        faces[9] = findViewById(R.id.faces100);

        mqTot[0] = findViewById(R.id.mqTot10);
        mqTot[1] = findViewById(R.id.mqTot20);
        mqTot[2] = findViewById(R.id.mqTot30);
        mqTot[3] = findViewById(R.id.mqTot40);
        mqTot[4] = findViewById(R.id.mqTot50);
        mqTot[5] = findViewById(R.id.mqTot60);
        mqTot[6] = findViewById(R.id.mqTot70);
        mqTot[7] = findViewById(R.id.mqTot80);
        mqTot[8] = findViewById(R.id.mqTot90);
        mqTot[9] = findViewById(R.id.mqTot100);

        mqTotCopy[0] = findViewById(R.id.mqTotCopy10);
        mqTotCopy[1] = findViewById(R.id.mqTotCopy20);
        mqTotCopy[2] = findViewById(R.id.mqTotCopy30);
        mqTotCopy[3] = findViewById(R.id.mqTotCopy40);
        mqTotCopy[4] = findViewById(R.id.mqTotCopy50);
        mqTotCopy[5] = findViewById(R.id.mqTotCopy60);
        mqTotCopy[6] = findViewById(R.id.mqTotCopy70);
        mqTotCopy[7] = findViewById(R.id.mqTotCopy80);
        mqTotCopy[8] = findViewById(R.id.mqTotCopy90);
        mqTotCopy[9] = findViewById(R.id.mqTotCopy100);

        mqAmount[0] = findViewById(R.id.mqAmount10);
        mqAmount[1] = findViewById(R.id.mqAmount20);
        mqAmount[2] = findViewById(R.id.mqAmount30);
        mqAmount[3] = findViewById(R.id.mqAmount40);
        mqAmount[4] = findViewById(R.id.mqAmount50);
        mqAmount[5] = findViewById(R.id.mqAmount60);
        mqAmount[6] = findViewById(R.id.mqAmount70);
        mqAmount[7] = findViewById(R.id.mqAmount80);
        mqAmount[8] = findViewById(R.id.mqAmount90);
        mqAmount[9] = findViewById(R.id.mqAmount100);

        totalAmounts[0] = findViewById(R.id.totalAmount10_k);
        totalAmounts[1] = findViewById(R.id.totalAmount20_k);
        totalAmounts[2] = findViewById(R.id.totalAmount30_k);
        totalAmounts[3] = findViewById(R.id.totalAmount40_k);
        totalAmounts[4] = findViewById(R.id.totalAmount50_k);
        totalAmounts[5] = findViewById(R.id.totalAmount60_k);
        totalAmounts[6] = findViewById(R.id.totalAmount70_k);
        totalAmounts[7] = findViewById(R.id.totalAmount80_k);
        totalAmounts[8] = findViewById(R.id.totalAmount90_k);
        totalAmounts[9] = findViewById(R.id.totalAmount100_k);

        mqAmountValue = 0;

        //questo lo faccio dopo che ottengo il valore di amount/mq.
        for(int i = 0; i < 10; i++) {
            mqAmount[i].setText(df2.format(mqAmountValue));
            meterB[i].addTextChangedListener(new MyTextWatcher(i));
            meterH[i].addTextChangedListener(new MyTextWatcher(i));
            faces[i].addTextChangedListener(new MyTextWatcher(i));

            if(i != 0){
                layoutOrderModK[i].setVisibility(View.GONE);
            }
        }

        linearLayoutOpened = 1;

        imageButton.setOnClickListener(e -> {
            layoutOrderModK[linearLayoutOpened].setVisibility(View.VISIBLE);
            linearLayoutOpened++;
            if(linearLayoutOpened == 10){
                imageButton.setVisibility(View.GONE);
            }
        });

        new AsyncTaskGetInfoK(this).execute();

        confirmButton.setOnClickListener(e -> {
            confirmButton.setClickable(false);
            if(amountError()){
                confirmButton.setClickable(true);
                Toast.makeText(this, getString(R.string.amount_error), Toast.LENGTH_LONG).show();
            } else if(espError()){
                if(ragioneSocialeEsp.getText().toString().equals("") || padiglioneEsp.getText().toString().equals("") || standEsp.getText().toString().equals("")){
                    confirmButton.setClickable(true);
                    Toast.makeText(this, getString(R.string.insert_company_name), Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.form_not_completed_title_no_sign))
                            .setMessage(getString(R.string.form_not_completed_message_k) + ragioneSocialeEsp.getText().toString())
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                                sendForm(STATUS_INCOMPLETE);
                            })
                            .setNegativeButton(getString(R.string.no), (dialog, id) -> {
                                confirmButton.setClickable(true);
                            })
                            .show();
                }
            } else if(radioButtonAll.isChecked() && allError()){
                confirmButton.setClickable(true);
                Toast.makeText(this, getString(R.string.fields_error_all), Toast.LENGTH_LONG).show();
            } else {
                sendForm(STATUS_COMPLETE);
            }
        });
    }

    @Override
    protected void sendForm(int idStatus){
        modulo.IdStatus = idStatus;
        if(idStatus == STATUS_INCOMPLETE){
            if(radioButtonAll.isChecked() && !ragioneSocialeAll.getText().toString().equals("")) {
                modulo.AllRagionesociale = ragioneSocialeAll.getText().toString();
            }
            modulo.EspRagionesociale = ragioneSocialeEsp.getText().toString();
            modulo.EspPadiglione = padiglioneEsp.getText().toString();
            modulo.EspStand = standEsp.getText().toString();
            modulo.Note = note.getText().toString();
            setKData();
            sendFormAfterSignature();
        } else if(idStatus == STATUS_COMPLETE && modulo.Id != 0) {
            setEditTextToForm();
            startSignatureActivity();
        } else if(idStatus == STATUS_COMPLETE) {
            setEditTextToForm();
            setKData();
            startSignatureActivity();
        }
    }

    private void setKData(){
        final ValoriK[] valoris = new ValoriK[10];
        for(int i = 0; i < 10; i++){
            if(isNumeric(meterB[i].getText().toString()) &&
                    isNumeric(meterH[i].getText().toString()) &&
                    isNumeric(faces[i].getText().toString())) {
                valoris[i] = new ValoriK(Double.parseDouble(meterB[i].getText().toString()),
                        Double.parseDouble(meterH[i].getText().toString()),
                        Double.parseDouble(metersTot[i].getText().toString().replace(",",".")),
                        Integer.parseInt(faces[i].getText().toString()),
                        Double.parseDouble(mqTot[i].getText().toString().replace(",",".")),
                        Double.parseDouble(totalAmounts[i].getText().toString().replace(",",".")));
            }
        }
        modulo.setK(valoris, totalAmount);
    }

    @Override
    protected void setEditTextToForm() {
        if (radioButtonAll.isChecked()) {
            setAllToForm();
        }
        setEspToForm();
    }

    @Override
    protected void getOldFormById(int idKey) {
        if(idKey != -1){
            new AsyncTaskGetModulo(this).execute(idKey);

            imageButton.setVisibility(View.GONE);
            for(int i = 0; i < 10; i++){
                layoutOrderModK[i].setVisibility(View.VISIBLE);
                meterB[i].setEnabled(false);
                meterH[i].setEnabled(false);
                faces[i].setEnabled(false);
            }
            confirmButton.setOnClickListener(e-> {
                if(radioButtonAll.isChecked() && allError()){
                    confirmButton.setClickable(true);
                    Toast.makeText(this, getString(R.string.fields_error_all), Toast.LENGTH_LONG).show();
                } else if(espError()){
                    confirmButton.setClickable(true);
                    Toast.makeText(this, getString(R.string.fields_error_esp), Toast.LENGTH_LONG).show();
                } else{
                    sendForm(STATUS_COMPLETE);
                }
            });

        }
    }

    @Override
    protected void setOldFormToEditText() {
        confirmButton.setClickable(true);

        if(radioButtonAll.isChecked() && modulo.AllRagionesociale != null) {
            ragioneSocialeAll.setText(modulo.AllRagionesociale);
        }
        ragioneSocialeEsp.setText(modulo.EspRagionesociale);
        standEsp.setText(modulo.EspStand);
        padiglioneEsp.setText(modulo.EspPadiglione);
        note.setText(modulo.Note);

        for(int i = 0; i < 10; i++){
            if(i <  modulo.ListModuloK.length) {
                meterB[i].setText(modulo.ListModuloK[i].Mtw + "");
                meterH[i].setText(modulo.ListModuloK[i].Mth + "");
                faces[i].setText(modulo.ListModuloK[i].Facce + "");
            } else {
                meterB[i].setText("");
                meterH[i].setText("");
                faces[i].setText("");
            }
        }
    }

    private static class AsyncTaskGetInfoK extends AsyncTask<Integer, Void, SoapObject> {
        private WeakReference<ModKActivity> activityReference;

        AsyncTaskGetInfoK(ModKActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapObject doInBackground(Integer... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETINFOK);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_GETINFOK, envelope);
                return (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            ModKActivity activity = activityReference.get();
            if(result != null){
                activity.mqAmountValue = Double.parseDouble(result.getProperty("COSTO").toString());
                for(int i = 0; i < 10; i++){
                    activity.mqAmount[i].setText(df2.format(activity.mqAmountValue));
                }
                if(activity.sharedPreferences.getBoolean(PREFS_IS_ITA, true)) {
                    activity.message1.setText(result.getProperty("TESTO1ITA").toString());
                    activity.message2.setText(result.getProperty("TESTO2ITA").toString());
                    activity.message3.setText(result.getProperty("TESTO3ITA").toString());
                    activity.message5.setText(result.getProperty("TESTO4ITA").toString());
                    activity.message4.setText(result.getProperty("TESTO5ITA").toString());
                } else {
                    activity.message1.setText(result.getProperty("TESTO1ENG").toString());
                    activity.message2.setText(result.getProperty("TESTO2ENG").toString());
                    activity.message3.setText(result.getProperty("TESTO3ENG").toString());
                    activity.message5.setText(result.getProperty("TESTO4ENG").toString());
                    activity.message4.setText(result.getProperty("TESTO5ENG").toString());
                }
            }else{
                activity.exitFromActivity();
            }
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private int i;

        MyTextWatcher(int i){
            this.i = i;
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start,
        int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
        int before, int count) {
            double metersTotValue = 0;
            double mqTotValue = 0;
            if(isNumeric(meterH[i].getText().toString())
                && isNumeric(meterB[i].getText().toString())) {
                metersTotValue = Double.parseDouble(meterB[i].getText().toString()) *
                        Double.parseDouble(meterH[i].getText().toString());
            }
            metersTot[i].setText(df2.format(metersTotValue));
            metersTotCopy[i].setText(df2.format(metersTotValue));

            if(isNumeric(faces[i].getText().toString())) {
                mqTotValue = metersTotValue * Integer.parseInt(faces[i].getText().toString());
            }

            mqTot[i].setText(df2.format(mqTotValue));
            mqTotCopy[i].setText(df2.format(mqTotValue));

            double totalAmountsValue = mqTotValue * mqAmountValue;
            totalAmounts[i].setText(df2.format(totalAmountsValue));

            totalAmount = 0;
            for (int k = 0; k < 10; k++) {
                totalAmount = totalAmount + Double.parseDouble(totalAmounts[k].getText().toString().replace(",", "."));
            }
            textViewTotalAmount.setText("€ " + df2.format(totalAmount));
        }
    }

}
