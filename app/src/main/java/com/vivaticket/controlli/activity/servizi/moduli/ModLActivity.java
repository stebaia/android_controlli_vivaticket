package com.vivaticket.controlli.activity.servizi.moduli;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vivaticket.controlli.R;
import com.vivaticket.controlli.activity.servizi.ServicesActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.vivaticket.controlli.util.Helper.EXTRA_IDMODULO;
import static com.vivaticket.controlli.util.Helper.L_GENERICO_CODE;
import static com.vivaticket.controlli.util.Helper.METHOD_GETINFOL;
import static com.vivaticket.controlli.util.Helper.NAMESPACE;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_GETINFOL;
import static com.vivaticket.controlli.util.Helper.STATUS_COMPLETE;
import static com.vivaticket.controlli.util.Helper.URL;

public class ModLActivity extends ServicesActivity {

    private DatePicker datePicker;
    private TimePicker timePickerFrom;
    private TimePicker timePickerTo;
    private TextView textViewTotalHours;
    private TextView textViewHourAmount;
    private double totalHours;
    private double hourAmountU3;
    private double hourAmountO3;

    private static final int INTERVAL = 60;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");
    private NumberPicker minutePickerFrom;
    private NumberPicker minutePickerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_l);

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int idModulo = intent.getIntExtra(EXTRA_IDMODULO, L_GENERICO_CODE);
        modulo.IdModulo = idModulo;

        textViewHourAmount = findViewById(R.id.textViewHourAmount);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        textViewTotalHours = findViewById(R.id.textViewTotalHours);

        datePicker = findViewById(R.id.datePicker);
        timePickerFrom = findViewById(R.id.timePickerFrom);
        timePickerTo = findViewById(R.id.timePickerTo);
        timePickerFrom.setIs24HourView(true);
        timePickerTo.setIs24HourView(true);
        setMinutePickers();

        timePickerFrom.setOnTimeChangedListener((timePicker, i, i1) -> {
            setTotalAmount();
        });

        timePickerTo.setOnTimeChangedListener((timePicker, i, i1) -> {
            setTotalAmount();
        });

        if(idModulo == L_GENERICO_CODE){
            titleText.setText(getString(R.string.mod_l_generico));
        } else {
            titleText.setText(getString(R.string.mod_l_ultimogiorno));
        }
        new AsyncTaskGetInfoL(this).execute(idModulo);

        confirmButton.setOnClickListener(e -> {
            confirmButton.setClickable(false);

            if(radioButtonAll.isChecked() && allError()){
                confirmButton.setClickable(true);
                Toast.makeText(this, getString(R.string.fields_error_all), Toast.LENGTH_LONG).show();
            } else if(espError()){
                confirmButton.setClickable(true);
                Toast.makeText(this, getString(R.string.fields_error_esp), Toast.LENGTH_LONG).show();
            } else if(amountError()){
                confirmButton.setClickable(true);
                Toast.makeText(this, getString(R.string.amount_error), Toast.LENGTH_LONG).show();
            } else {
                sendForm(STATUS_COMPLETE);
            }
        });
    }

    @Override
    protected  void sendForm(int idStatus){
        modulo.IdStatus = idStatus;
        if(modulo.Id == 0){
            setEditTextToForm();
        }
        modulo.setL(String.format(Locale.ITALIAN, "%02d", datePicker.getDayOfMonth()) + "/" + String.format(Locale.ITALIAN, "%02d", datePicker.getMonth() + 1) + "/" + datePicker.getYear(),
                String.format(Locale.ITALIAN, "%02d", timePickerFrom.getHour()) + ":" + String.format(Locale.ITALIAN, "%02d", getMinuteFrom()),
                String.format(Locale.ITALIAN, "%02d", timePickerTo.getHour()) + ":" + String.format(Locale.ITALIAN, "%02d", getMinuteTo()),
                totalHours,
                totalAmount);
        startSignatureActivity();
    }

    @Override
    protected void getOldFormById(int idKey) {
        if(idKey != -1) {
            new AsyncTaskGetModulo(this).execute(idKey);
            setAllNotClickable();
            datePicker.setEnabled(false);
            timePickerFrom.setEnabled(false);
            timePickerTo.setEnabled(false);
            confirmButton.setText(getString(R.string.extend_time));
            confirmButton.setOnClickListener(e -> {
                confirmButton.setClickable(false);
                Calendar calendar = new GregorianCalendar();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                if(timePickerFrom.getHour() > timePickerTo.getHour()){
                    calendar.add(Calendar.DATE, 1);
                    datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                }
                timePickerFrom.setHour(timePickerTo.getHour());
                timePickerFrom.setMinute(timePickerTo.getMinute());
                timePickerTo.setEnabled(true);
                note.setText("");
                note.setEnabled(true);
                confirmButton.setText(getString(R.string.sign_confirm));
                confirmButton.setOnClickListener(e2 -> {
                    if(amountError()){
                        confirmButton.setClickable(true);
                        Toast.makeText(this, getString(R.string.amount_error), Toast.LENGTH_LONG).show();
                    } else {
                        sendForm(STATUS_COMPLETE);
                    }
                });
            });
        }
    }

    @Override
    protected void setOldFormToEditText() {
        confirmButton.setClickable(true);

        if(modulo.AllRagionesociale == null) {
            radioButtonEsp.setChecked(true);
        } else {
            radioButtonAll.setChecked(true);
            ragioneSocialeAll.setText(modulo.AllRagionesociale);
            nazioneAll.setSelection(nazioniCodes.indexOf(modulo.AllNazione));
            provinciaAll.setVisibility(View.GONE);
            provinciaAllEstero.setVisibility(View.VISIBLE);
            provinciaAllEstero.setText(modulo.AllProvincia);
            comuneAll.setVisibility(View.GONE);
            comuneAllEstero.setVisibility(View.VISIBLE);
            comuneAllEstero.setText(modulo.AllComune);
            capAll.setText(modulo.AllCap);
            indirizzoAll.setText(modulo.AllIndirizzo);
            telefonoAll.setText(modulo.AllTelefono);
            faxAll.setText(modulo.AllFax);
            partitaIvaAll.setText(modulo.AllPivacf);
            emailAll.setText(modulo.AllEmail);
        }
        nome.setText(modulo.Nome);
        cognome.setText(modulo.Cognome);
        ruoloinazienda.setText(modulo.Ruoloinazienda);

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

        standEsp.setText(modulo.EspStand);
        padiglioneEsp.setText(modulo.EspPadiglione);

        note.setText(modulo.Note);

        try {
            Calendar calendarDate = new GregorianCalendar();
            Calendar calendarFrom = new GregorianCalendar();
            Calendar calendarTo = new GregorianCalendar();
            calendarDate.setTime(new SimpleDateFormat("dd/MM/yyy").parse(modulo.Datainizio));
            calendarFrom.setTime(new SimpleDateFormat("HH:mm:ss").parse(modulo.Orainizio));
            calendarTo.setTime(new SimpleDateFormat("HH:mm:ss").parse(modulo.Oratermine));
            datePicker.updateDate(calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH));
            timePickerFrom.setHour(calendarFrom.get(Calendar.HOUR_OF_DAY));
            timePickerFrom.setMinute(calendarFrom.get(Calendar.MINUTE));
            timePickerTo.setHour(calendarTo.get(Calendar.HOUR_OF_DAY));
            timePickerTo.setMinute(calendarTo.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setTotalAmount() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.ITALY);
        try {
            Date startDate = simpleDateFormat.parse(timePickerFrom.getHour() + ":" + getMinuteFrom());
            Date endDate = simpleDateFormat.parse(timePickerTo.getHour() + ":" + getMinuteTo());

            System.out.println(startDate+ " " + endDate);
            long difference = endDate.getTime() - startDate.getTime();
            if(difference < 0) {
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
            }
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            int minToMult = 0;
            switch (min){
                case 0: minToMult = 0;
                    break;
                case 15: minToMult = 25;
                    break;
                case 30: minToMult = 50;
                    break;
                case 45: minToMult = 75;
                    break;
            }
            totalHours = Double.parseDouble(hours + "." + minToMult);
            textViewTotalHours.setText(df0.format(totalHours));

            if(totalHours > 3){
                totalAmount = totalHours * hourAmountO3;
                textViewHourAmount.setText("€ " + df2.format(hourAmountO3));
            } else {
                totalAmount = totalHours * hourAmountU3;
                textViewHourAmount.setText("€ " + df2.format(hourAmountU3));
            }
            textViewTotalAmount.setText("€ " + df2.format(totalAmount));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setMinutePickers() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minuteFrom = timePickerFrom.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minuteFrom != null) && (minuteFrom instanceof NumberPicker)) {
            minutePickerFrom = (NumberPicker) minuteFrom;
            minutePickerFrom.setMinValue(0);
            minutePickerFrom.setMaxValue(numValues - 1);
            minutePickerFrom.setDisplayedValues(displayedValues);
        }

        View minuteTo = timePickerTo.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minuteTo != null) && (minuteTo instanceof NumberPicker)) {
            minutePickerTo = (NumberPicker) minuteTo;
            minutePickerTo.setMinValue(0);
            minutePickerTo.setMaxValue(numValues - 1);
            minutePickerTo.setDisplayedValues(displayedValues);
        }
    }

    private int getMinuteFrom() {
        if (minutePickerFrom != null) {
            return (minutePickerFrom.getValue() * INTERVAL);
        } else {
            return timePickerFrom.getMinute();
        }
    }

    private int getMinuteTo() {
        if (minutePickerTo != null) {
            return (minutePickerTo.getValue() * INTERVAL);
        } else {
            return timePickerTo.getMinute();
        }
    }

    private static class AsyncTaskGetInfoL extends AsyncTask<Integer, Void, SoapObject> {
        private WeakReference<ModLActivity> activityReference;

        AsyncTaskGetInfoL(ModLActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapObject doInBackground(Integer... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETINFOL);
            request.addProperty("idModulo", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_GETINFOL, envelope);
                return (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject response) {
            ModLActivity activity = activityReference.get();
            if(response != null){
                activity.hourAmountU3 = Double.parseDouble((response.getProperty(0)).toString().replace(",","."));
                activity.hourAmountO3 = Double.parseDouble((response.getProperty(1)).toString().replace(",","."));
                activity.textViewHourAmount.setText("€ " + df2.format(activity.hourAmountU3));
            }else{
                activity.exitFromActivity();
            }
        }
    }
}
