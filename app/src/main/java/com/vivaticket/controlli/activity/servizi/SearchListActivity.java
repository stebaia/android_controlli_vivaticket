package com.vivaticket.controlli.activity.servizi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vivaticket.controlli.R;
import com.vivaticket.controlli.util.MyListAdapter;
import com.vivaticket.controlli.util.SearchElement;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.vivaticket.controlli.util.Helper.EXTRA_IDMODULO;
import static com.vivaticket.controlli.util.Helper.EXTRA_SEARCH;
import static com.vivaticket.controlli.util.Helper.METHOD_GETRICERCA;
import static com.vivaticket.controlli.util.Helper.NAMESPACE;
import static com.vivaticket.controlli.util.Helper.PREFS_MANIFESTATION_VALUE;
import static com.vivaticket.controlli.util.Helper.PREFS_NAME;
import static com.vivaticket.controlli.util.Helper.SOAP_ACTION_GETRICERCA;
import static com.vivaticket.controlli.util.Helper.URL;

public class SearchListActivity extends AppCompatActivity {

    private int idModulo;
    private EditText searchEditText;
    private ProgressDialog progressDialog;
    private ListView myListView;
    private List<SearchElement> searchList;
    private RadioGroup radioGroup;
    private MyListAdapter adapter;
    List<SearchElement> filteredList = new ArrayList<>();
    private boolean isFiltered = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchlist);

        Intent intent = getIntent();
        idModulo = intent.getIntExtra(EXTRA_IDMODULO, -1);
        final int idManifestazione = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getInt(PREFS_MANIFESTATION_VALUE, -1);

        myListView = findViewById(R.id.list);
        radioGroup = findViewById(R.id.radioButtonType);
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setEnabled(false);
        searchEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().toLowerCase();
                final List<SearchElement> newSearchList = new ArrayList<>();
                if (!isFiltered) {
                    for (SearchElement se : searchList) {
                        if (se.getRagioneSocialeAll().toLowerCase().contains(search) ||
                                se.getRagioneSocialeEsp().toLowerCase().contains(search) ||
                                se.getPadiglioneEsp().toLowerCase().contains(search) ||
                                se.getStandEsp().toLowerCase().contains(search)) {
                            newSearchList.add(se);
                        }
                    }
                    final MyListAdapter adapter = new MyListAdapter(SearchListActivity.this, newSearchList);
                    myListView.setAdapter(adapter);
                    myListView.setOnItemClickListener((parent, view, position, id) -> {
                        Intent myintent = new Intent();
                        myintent.putExtra(EXTRA_IDMODULO, idModulo);
                        myintent.putExtra(EXTRA_SEARCH, searchList.get(position).getIdKey());
                        setResult(RESULT_OK, myintent);
                        finish();
                    });
                }else {
                    for (SearchElement se : filteredList) {
                        if (se.getRagioneSocialeAll().toLowerCase().contains(search) ||
                                se.getRagioneSocialeEsp().toLowerCase().contains(search) ||
                                se.getPadiglioneEsp().toLowerCase().contains(search) ||
                                se.getStandEsp().toLowerCase().contains(search)) {
                            newSearchList.add(se);
                        }
                    }
                    final MyListAdapter adapter = new MyListAdapter(SearchListActivity.this, newSearchList);
                    myListView.setAdapter(adapter);
                    myListView.setOnItemClickListener((parent, view, position, id) -> {
                        Intent myintent = new Intent();
                        myintent.putExtra(EXTRA_IDMODULO, idModulo);
                        myintent.putExtra(EXTRA_SEARCH, filteredList.get(position).getIdKey());
                        setResult(RESULT_OK, myintent);
                        finish();
                    });
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                filteredList.clear();
                isFiltered = true;
                switch (checkedId){
                    case R.id.radio_tutti:
                        filteredList.addAll(searchList);
                        adapter = new MyListAdapter(SearchListActivity.this, searchList);


                        Log.d("tutti", "click su tutti");
                        break;
                    case R.id.radio_espositore:
                        for (SearchElement searchElement : searchList) {
                            if (!searchElement.getRagioneSocialeEsp().equals("-")){
                                filteredList.add(searchElement);
                            }
                        }
                        adapter = new MyListAdapter(SearchListActivity.this, filteredList);
                        Log.d("tutti", "click su allestitore");
                        break;
                    case R.id.radio_allestitore:
                        for (SearchElement searchElement : searchList) {
                            if (!searchElement.getRagioneSocialeAll().equals("-")){
                                filteredList.add(searchElement);
                            }
                        }
                        adapter = new MyListAdapter(SearchListActivity.this, filteredList);
                        Log.d("tutti", "click su espositore");
                        break;

                }
                myListView.setAdapter(adapter);

            }
        });

        progressDialog = ProgressDialog.show(this, "", getString(R.string.getting_data), true);

        if(idModulo != -1 && idManifestazione != -1) {
            new AsyncTaskGetRicerca(this).execute(idModulo, idManifestazione);
        }

        adapter = new MyListAdapter(SearchListActivity.this, searchList);
        progressDialog.dismiss();
    }

    private static class AsyncTaskGetRicerca extends AsyncTask<Integer, Void, SoapObject> {
        private WeakReference<SearchListActivity> activityReference;

        AsyncTaskGetRicerca(SearchListActivity context){
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected SoapObject doInBackground(Integer... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETRICERCA);
            request.addProperty("idModulo", params[0]);
            request.addProperty("idManifestazione", params[1]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION_GETRICERCA, envelope);
                return (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            SearchListActivity activity = activityReference.get();
            if(result != null) {
                System.out.println(result.toString());
                if(result.getPropertyCount() == 0){
                    Toast.makeText(activity, "Nessun modulo trovato.", Toast.LENGTH_LONG).show();
                    activity.progressDialog.dismiss();
                    activity.finish();
                }else {
                    activity.searchList = new ArrayList<>();
                    for (int i = 0; i < result.getPropertyCount(); i++) {
                        SoapObject ricercaResponse = (SoapObject) result.getProperty(i);
                        String ragioneSocialeAll = ricercaResponse.getProperty(1).toString();
                        String ragioneSocialeEsp = ricercaResponse.getProperty(2).toString();
                        String padiglioneEsp = ricercaResponse.getProperty(3).toString();
                        String standEsp = ricercaResponse.getProperty(4).toString();
                        Integer id = Integer.parseInt(ricercaResponse.getProperty(0).toString());
                        Integer idStatus = Integer.parseInt(ricercaResponse.getProperty(5).toString());

                        activity.searchList.add(new SearchElement(ragioneSocialeAll,
                                ragioneSocialeEsp,
                                padiglioneEsp,
                                standEsp,
                                id,
                                idStatus));
                    }

                    MyListAdapter adapter = new MyListAdapter(activity, activity.searchList);
                    activity.myListView.setAdapter(adapter);
                    activity.myListView.setOnItemClickListener((parent, view, position, id) -> {
                        Intent myintent = new Intent();
                        myintent.putExtra(EXTRA_IDMODULO, activity.idModulo);
                        myintent.putExtra(EXTRA_SEARCH, activity.searchList.get(position).getIdKey());
                        activity.setResult(RESULT_OK, myintent);
                        activity.finish();
                    });
                    activity.searchEditText.setEnabled(true);
                    activity.progressDialog.dismiss();
                }
            } else {
                Toast.makeText(activity, activity.getText(R.string.error_getdata), Toast.LENGTH_LONG).show();
                activity.progressDialog.dismiss();
                activity.finish();
            }
        }
    }



}
