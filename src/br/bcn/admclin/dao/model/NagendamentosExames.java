package br.bcn.admclin.dao.model;

public class NagendamentosExames {

    private int NAGENEID;
    private int NAGDID;
    private int HORA;
    private int NAGENID;
    private int HANDLE_EXAME;
    private int ID_AREAS_ATENDIMENTO; // essa variavel serve para ver a area de atendimento (para podermos fazer mais de um atendimento separando as areas de atendimento)
    private String nomeExame;
    
    public int getNAGENEID() {
        return NAGENEID;
    }
    public void setNAGENEID(int nAGENEID) {
        NAGENEID = nAGENEID;
    }
    public int getNAGDID() {
        return NAGDID;
    }
    public void setNAGDID(int nAGDID) {
        NAGDID = nAGDID;
    }
    public int getHORA() {
        return HORA;
    }
    public void setHORA(int hORA) {
        HORA = hORA;
    }
    public int getNAGENID() {
        return NAGENID;
    }
    public void setNAGENID(int nAGENID) {
        NAGENID = nAGENID;
    }
    public int getHANDLE_EXAME() {
        return HANDLE_EXAME;
    }
    public void setHANDLE_EXAME(int hANDLE_EXAME) {
        HANDLE_EXAME = hANDLE_EXAME;
    }
    public int getID_AREAS_ATENDIMENTO() {
        return ID_AREAS_ATENDIMENTO;
    }
    public void setID_AREAS_ATENDIMENTO(int iD_AREAS_ATENDIMENTO) {
        ID_AREAS_ATENDIMENTO = iD_AREAS_ATENDIMENTO;
    }
    public String getNomeExame() {
        return nomeExame;
    }
    public void setNomeExame(String nomeExame) {
        this.nomeExame = nomeExame;
    }
    
    
    
}
