package br.bcn.admclin.dao.model;

import java.sql.Date;
import java.util.ArrayList;

public class Nagendamentos {
    private int NAGENID;
    private Date DIA;
    private String PACIENTE;
    private String TELEFONE;
    private String CELULAR;
    private int HANDLE_CONVENIO;
    private String NOME_CONVENIO;
    private ArrayList<NagendamentosExames> listaExames = new ArrayList<NagendamentosExames>();
    
    public int getNAGENID() {
        return NAGENID;
    }
    public void setNAGENID(int nAGENID) {
        NAGENID = nAGENID;
    }
    public Date getDIA() {
        return DIA;
    }
    public void setDIA(Date dIA) {
        DIA = dIA;
    }
    public String getPACIENTE() {
        return PACIENTE;
    }
    public void setPACIENTE(String pACIENTE) {
        PACIENTE = pACIENTE;
    }
    public String getTELEFONE() {
        return TELEFONE;
    }
    public void setTELEFONE(String tELEFONE) {
        TELEFONE = tELEFONE;
    }
    public String getCELULAR() {
        return CELULAR;
    }
    public void setCELULAR(String cELULAR) {
        CELULAR = cELULAR;
    }
    public int getHANDLE_CONVENIO() {
        return HANDLE_CONVENIO;
    }
    public void setHANDLE_CONVENIO(int hANDLE_CONVENIO) {
        HANDLE_CONVENIO = hANDLE_CONVENIO;
    }
    public String getNOME_CONVENIO() {
        return NOME_CONVENIO;
    }
    public void setNOME_CONVENIO(String nOME_CONVENIO) {
        NOME_CONVENIO = nOME_CONVENIO;
    }
    public ArrayList<NagendamentosExames> getListaExames() {
        return listaExames;
    }
    public void setListaExames(ArrayList<NagendamentosExames> listaExames) {
        this.listaExames = listaExames;
    }
    
}
