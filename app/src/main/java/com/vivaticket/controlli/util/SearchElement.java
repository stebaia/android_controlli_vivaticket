package com.vivaticket.controlli.util;

public class SearchElement {

    private String ragioneSocialeAll;
    private String ragioneSocialeEsp;
    private String padiglioneEsp;
    private String standEsp;
    private Integer idKey;
    private Integer idStatus;

    public SearchElement(String ragioneSocialeAll, String ragioneSocialeEsp, String padiglioneEsp, String standEsp, Integer idKey, Integer idStatus) {
        this.ragioneSocialeAll = ragioneSocialeAll;
        this.ragioneSocialeEsp = ragioneSocialeEsp;
        this.padiglioneEsp = padiglioneEsp;
        this.standEsp = standEsp;
        this.idKey = idKey;
        this.idStatus = idStatus;
    }

    public String getRagioneSocialeAll() {
        return ragioneSocialeAll;
    }

    public String getRagioneSocialeEsp() {
        return ragioneSocialeEsp;
    }

    public String getPadiglioneEsp() {
        return padiglioneEsp;
    }

    public String getStandEsp() {
        return standEsp;
    }

    public Integer getIdKey() {
        return idKey;
    }

    public Integer getIdStatus() {
        return idStatus;
    }
}
