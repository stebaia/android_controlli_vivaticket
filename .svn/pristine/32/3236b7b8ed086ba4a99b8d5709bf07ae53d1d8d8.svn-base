package com.supergianlu.controlli.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Helper {

    public final static int CHOOSE_CONTROL_CODE = 100;
    public final static int SIGNALATIONS_CODE = 101;
    public final static int SERVICES_CODE = 102;
    public final static int L_ULTIMOGIORNO_CODE = 1;
    public final static int L_GENERICO_CODE = 2;
    public final static int N2_CODE = 3;
    public final static int K_CODE = 4;

    public final static int STATUS_INCOMPLETE = 0;
    public final static int STATUS_COMPLETE = 1;
    public final static int STATUS_DELETED = 2;

    public final static String EXTRA_IDMODULO = "extra_idmodulo";
    public final static String EXTRA_SEARCH = "extra_search";
    public final static String EXTRA_SIGNATURE = "extra_signature";

    public final static String PREFS_NAME = "controlli_preferences";
    public final static String PREFS_USER_LOGGED = "prefs_user_logged";
    public final static String PREFS_USER_EMAIL = "prefs_user_email";
    public final static String PREFS_USER_ID = "prefs_user_id";
    public final static String PREFS_USER_ROLE = "prefs_user_role";
    public final static String PREFS_CONTROL_CODE = "prefs_control_code";
    public final static String PREFS_IS_ITA = "prefs_is_ita";
    public final static String PREFS_MANIFESTATION_VALUE= "prefs_manifestation_value";
    public final static String PREFS_MANIFESTATION_NAME = "prefs_manifestation_name";

    public final static String NAMESPACE = "http://tempuri.org/";
    public final static String URL = "https://rncontrolli.vivaticket.com/wscontrolli.asmx";
    public final static String METHOD_LOGINUTENTE = "LoginUtente";
    public final static String METHOD_SETSEGNALAZIONI = "SetSegnalazioni";
    public final static String METHOD_GETGRUPPI = "GetGruppi";
    public final static String METHOD_SETMODULO = "SetModulo";
    public final static String METHOD_GETINFOL = "GetInfoL";
    public final static String METHOD_GETINFON2 = "GetInfoN2";
    public final static String METHOD_GETINFOK = "GetInfoK";
    public final static String METHOD_GETCOMUNI = "GetComuni";
    public final static String METHOD_GETTESTIN2 = "GetTestiN2";
    public final static String METHOD_GETMANIFESTAZIONI = "GetManifestazioni";
    public final static String METHOD_GETRICERCA = "GetRicerca";
    public final static String METHOD_GETMODULO = "GetModulo";
    public final static String SOAP_ACTION_LOGINUTENTE = NAMESPACE + METHOD_LOGINUTENTE;
    public final static String SOAP_ACTION_SETSEGNALAZIONI = NAMESPACE + METHOD_SETSEGNALAZIONI;
    public final static String SOAP_ACTION_GETGRUPPI = NAMESPACE + METHOD_GETGRUPPI;
    public final static String SOAP_ACTION_SETMODULO = NAMESPACE + METHOD_SETMODULO;
    public final static String SOAP_ACTION_GETINFOL = NAMESPACE + METHOD_GETINFOL;
    public final static String SOAP_ACTION_GETINFON2 = NAMESPACE + METHOD_GETINFON2;
    public final static String SOAP_ACTION_GETINFOK = NAMESPACE + METHOD_GETINFOK;
    public final static String SOAP_ACTION_GETCOMUNI = NAMESPACE + METHOD_GETCOMUNI;
    public final static String SOAP_ACTION_GETTESTIN2 = NAMESPACE + METHOD_GETTESTIN2;
    public final static String SOAP_ACTION_GETMANIFESTAZIONI = NAMESPACE + METHOD_GETMANIFESTAZIONI;
    public final static String SOAP_ACTION_GETRICERCA = NAMESPACE + METHOD_GETRICERCA;
    public final static String SOAP_ACTION_GETMODULO = NAMESPACE + METHOD_GETMODULO;

    public final static String COLUMN_KEY_ID = "key_id";

    public final static String TABLE_STATE = "State";
    public final static String COLUMN_STATE_NAME = "state_name";
    public final static String COLUMN_STATE_CODE = "state_code";

    public final static String TABLE_PROVINCE = "Province";
    public final static String COLUMN_PROVINCE_NAME = "province_name";
    public final static String COLUMN_PROVINCE_CODE = "province_code";

    //RICHIEDO I PERMESSI DELLA FOTOCAMERA E DI SCRITTURA
    public static void requestPermissions(Activity context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
    }
}
