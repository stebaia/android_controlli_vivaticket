package com.vivaticket.controlli;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Modulo {

    @SerializedName("Id")
    public int Id;
    @SerializedName("IdModulo")
    public int IdModulo;
    @SerializedName("IdManifestazione")
    public int IdManifestazione;
    @SerializedName("IdStatus")
    public int IdStatus;

    @SerializedName("AllRagionesociale")
    public String AllRagionesociale;
    @SerializedName("AllIndirizzo")
    public String AllIndirizzo;
    @SerializedName("AllCap")
    public String AllCap;
    @SerializedName("AllIdComune")
    public String AllIdComune;
    @SerializedName("AllComune")
    public String AllComune;
    @SerializedName("AllProvincia")
    public String AllProvincia;
    @SerializedName("AllNazione")
    public String AllNazione;
    @SerializedName("AllTelefono")
    public String AllTelefono;
    @SerializedName("AllFax")
    public String AllFax;
    @SerializedName("AllPivacf")
    public String AllPivacf;
    @SerializedName("AllEmail")
    public String AllEmail;

    @SerializedName("Nome")
    public String Nome;
    @SerializedName("Cognome")
    public String Cognome;
    @SerializedName("Ruoloinazienda")
    public String Ruoloinazienda;

    @SerializedName("EspRagionesociale")
    public String EspRagionesociale;
    @SerializedName("EspIndirizzo")
    public String EspIndirizzo;
    @SerializedName("EspCap")
    public String EspCap;
    @SerializedName("EspIdComune")
    public String EspIdComune;
    @SerializedName("EspComune")
    public String EspComune;
    @SerializedName("EspProvincia")
    public String EspProvincia;
    @SerializedName("EspNazione")
    public String EspNazione;
    @SerializedName("EspTelefono")
    public String EspTelefono;
    @SerializedName("EspFax")
    public String EspFax;
    @SerializedName("EspPivacf")
    public String EspPivacf;
    @SerializedName("EspEmail")
    public String EspEmail;
    @SerializedName("EspStand")
    public String EspStand;
    @SerializedName("EspPadiglione")
    public String EspPadiglione;

    @SerializedName("Note")
    public String Note;

    //MODULO L
    @SerializedName("Datainizio")
    public String Datainizio;
    @SerializedName("Orainizio")
    public String Orainizio;
    @SerializedName("Oratermine")
    public String Oratermine;
    @SerializedName("Ore")
    public double Ore;
    @SerializedName("Costototale")
    public double Costototale;

    //MODULO N2
    public ValoriN2[] ListModuloN2;
    @SerializedName("Rifiuta")
    public int Rifiuta;

    //MODULO K
    public ValoriK[] ListModuloK;

    public void setEsp(String espRagionesociale, String espIndirizzo, String espCap, String espIdComune,
                       String espComune, String espProvincia, String espNazione, String espTelefono,
                       String espFax, String espPivacf, String espEmail, String espStand, String espPadiglione,
                       String nome, String cognome, String ruoloinazienda, String note) {
        EspRagionesociale = espRagionesociale;
        EspIndirizzo = espIndirizzo;
        EspCap = espCap;
        EspIdComune = espIdComune;
        EspComune = espComune;
        EspProvincia = espProvincia;
        EspNazione = espNazione;
        EspTelefono = espTelefono;
        EspFax = espFax;
        EspEmail = espEmail;
        EspPivacf = espPivacf;
        EspStand = espStand;
        EspPadiglione = espPadiglione;
        Nome = nome;
        Cognome = cognome;
        Ruoloinazienda = ruoloinazienda;
        Note = note;
    }

    public void setAll(String allRagionesociale, String allIndirizzo, String allCap, String allIdComune,
                       String allComune, String allProvincia, String allNazione, String allTelefono,
                       String allFax, String allPivacf, String allEmail){
        AllRagionesociale = allRagionesociale;
        AllIndirizzo = allIndirizzo;
        AllCap = allCap;
        AllIdComune = allIdComune;
        AllComune = allComune;
        AllProvincia = allProvincia;
        AllNazione = allNazione;
        AllTelefono = allTelefono;
        AllFax = allFax;
        AllPivacf = allPivacf;
        AllEmail = allEmail;
    }

    public void setAllInEsp(String espRagionesociale, String espIndirizzo, String espCap, String espIdComune,
                            String espComune, String espProvincia, String espNazione, String espTelefono,
                            String espFax, String espPivacf, String espEmail, String espStand, String espPadiglione,
                            String nome, String cognome, String ruoloinazienda, String note) {
        setAll(espRagionesociale, espIndirizzo, espCap, espIdComune, espComune, espProvincia, espNazione,
                espTelefono, espFax, espPivacf, espEmail);
        EspStand = espStand;
        EspPadiglione = espPadiglione;
        Nome = nome;
        Cognome = cognome;
        Ruoloinazienda = ruoloinazienda;
        Note = note;
    }

    public void setL(String datainizio, String orainizio, String oratermine, double ore, double costototale){
        Datainizio = datainizio;
        Orainizio = orainizio;
        Oratermine = oratermine;
        Ore = ore;
        Costototale = costototale;
    }

    public void setN2(ValoriN2[] listmodulon2, double costototale){
        ListModuloN2 = listmodulon2;
        Costototale = costototale;
    }

    public void setK(ValoriK[] listmodulok, double costototale){
        ListModuloK = listmodulok;
        Costototale = costototale;
    }

    @Override
    public String toString() {
        return "Modulo{" +
                "IdModulo=" + IdModulo +
                ", IdManifestazione=" + IdManifestazione +
                ", AllRagionesociale='" + AllRagionesociale + '\'' +
                ", AllIndirizzo='" + AllIndirizzo + '\'' +
                ", AllCap='" + AllCap + '\'' +
                ", AllIdComune='" + AllIdComune + '\'' +
                ", AllComune='" + AllComune + '\'' +
                ", AllProvincia='" + AllProvincia + '\'' +
                ", AllNazione='" + AllNazione + '\'' +
                ", AllTelefono='" + AllTelefono + '\'' +
                ", AllFax='" + AllFax + '\'' +
                ", AllPivacf='" + AllPivacf + '\'' +
                ", AllEmail='" + AllEmail + '\'' +
                ", Nome='" + Nome + '\'' +
                ", Cognome='" + Cognome + '\'' +
                ", Ruoloinazienda='" + Ruoloinazienda + '\'' +
                ", EspRagionesociale='" + EspRagionesociale + '\'' +
                ", EspIndirizzo='" + EspIndirizzo + '\'' +
                ", EspCap='" + EspCap + '\'' +
                ", EspIdComune='" + EspIdComune + '\'' +
                ", EspComune='" + EspComune + '\'' +
                ", EspProvincia='" + EspProvincia + '\'' +
                ", EspNazione='" + EspNazione + '\'' +
                ", EspTelefono='" + EspTelefono + '\'' +
                ", EspFax='" + EspFax + '\'' +
                ", EspPivacf='" + EspPivacf + '\'' +
                ", EspEmail='" + EspEmail + '\'' +
                ", EspStand='" + EspStand + '\'' +
                ", EspPadiglione='" + EspPadiglione + '\'' +
                ", Note='" + Note + '\'' +
                ", Datainizio='" + Datainizio + '\'' +
                ", Orainizio='" + Orainizio + '\'' +
                ", Oratermine='" + Oratermine + '\'' +
                ", Ore=" + Ore +
                ", Costototale=" + Costototale +
                ", ListModuloN2=" + Arrays.toString(ListModuloN2) +
                ", Rifiuta=" + Rifiuta +
                ", ListModuloK=" + Arrays.toString(ListModuloK) +
                '}';
    }

}
