/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.dao.model;

import java.sql.Date;

/**
 *
 * @author CeSaR
 */
public class Atendimentos {
    
    private String FLAG_BOLETO_ENTREGUE, OBSERVACAO, VALOR_ATENDIMENTO, CRM_RADIOLOGISTA1, CRM_RADIOLOGISTA2, CRM_RADIOLOGISTA3, MATRICULA_CONVENIO, HANDLE_IMAGEM, FLAG_LAUDO, FLAG_AUDIO, STATUS1, STATUS2, STATUS3, STATUS4, COMPLEMENTO, LAUDO_ENTREGUE_AO_PACIENTE, EXAME_ENTREGUE_AO_PACIENTE, FLAG_IMPRIMIU;
    private Date DATA_ATENDIMENTO, DATA_EXAME_PRONTO, DATA_FECHAMENTO, DAT;
    private int HANDLE_AT, HORA_ATENDIMENTO, DURACAO_ATENDIMENTO, HANDLE_PACIENTE, HANDLE_MEDICO_SOL, HANDLE_AGENDA, HANDLE_CONVENIO, USUARIOID, HORA_EXAME_PRONTO, ID_AREAS_ATENDIMENTO;

    public String getCRM_RADIOLOGISTA1() {
        return CRM_RADIOLOGISTA1;
    }

    public void setCRM_RADIOLOGISTA1(String CRM_RADIOLOGISTA1) {
        this.CRM_RADIOLOGISTA1 = CRM_RADIOLOGISTA1;
    }

    public String getCRM_RADIOLOGISTA2() {
        return CRM_RADIOLOGISTA2;
    }

    public void setCRM_RADIOLOGISTA2(String CRM_RADIOLOGISTA2) {
        this.CRM_RADIOLOGISTA2 = CRM_RADIOLOGISTA2;
    }

    public String getCRM_RADIOLOGISTA3() {
        return CRM_RADIOLOGISTA3;
    }

    public void setCRM_RADIOLOGISTA3(String CRM_RADIOLOGISTA3) {
        this.CRM_RADIOLOGISTA3 = CRM_RADIOLOGISTA3;
    }

    public Date getDATA_ATENDIMENTO() {
        return DATA_ATENDIMENTO;
    }

    public void setDATA_ATENDIMENTO(Date DATA_ATENDIMENTO) {
        this.DATA_ATENDIMENTO = DATA_ATENDIMENTO;
    }

    public Date getDATA_EXAME_PRONTO() {
        return DATA_EXAME_PRONTO;
    }

    public void setDATA_EXAME_PRONTO(Date DATA_EXAME_PRONTO) {
        this.DATA_EXAME_PRONTO = DATA_EXAME_PRONTO;
    }

    public Date getDATA_FECHAMENTO() {
        return DATA_FECHAMENTO;
    }

    public void setDATA_FECHAMENTO(Date DATA_FECHAMENTO) {
        this.DATA_FECHAMENTO = DATA_FECHAMENTO;
    }

    public int getDURACAO_ATENDIMENTO() {
        return DURACAO_ATENDIMENTO;
    }

    public void setDURACAO_ATENDIMENTO(int DURACAO_ATENDIMENTO) {
        this.DURACAO_ATENDIMENTO = DURACAO_ATENDIMENTO;
    }

    public String getFLAG_AUDIO() {
        return FLAG_AUDIO;
    }

    public void setFLAG_AUDIO(String FLAG_AUDIO) {
        this.FLAG_AUDIO = FLAG_AUDIO;
    }

    public String getFLAG_BOLETO_ENTREGUE() {
        return FLAG_BOLETO_ENTREGUE;
    }

    public void setFLAG_BOLETO_ENTREGUE(String FLAG_BOLETO_ENTREGUE) {
        this.FLAG_BOLETO_ENTREGUE = FLAG_BOLETO_ENTREGUE;
    }

    public String getFLAG_LAUDO() {
        return FLAG_LAUDO;
    }

    public void setFLAG_LAUDO(String FLAG_LAUDO) {
        this.FLAG_LAUDO = FLAG_LAUDO;
    }

    public int getHANDLE_AGENDA() {
        return HANDLE_AGENDA;
    }

    public void setHANDLE_AGENDA(int HANDLE_AGENDA) {
        this.HANDLE_AGENDA = HANDLE_AGENDA;
    }

    public int getHANDLE_AT() {
        return HANDLE_AT;
    }

    public void setHANDLE_AT(int HANDLE_AT) {
        this.HANDLE_AT = HANDLE_AT;
    }

    public int getHANDLE_CONVENIO() {
        return HANDLE_CONVENIO;
    }

    public void setHANDLE_CONVENIO(int HANDLE_CONVENIO) {
        this.HANDLE_CONVENIO = HANDLE_CONVENIO;
    }

    public String getHANDLE_IMAGEM() {
        return HANDLE_IMAGEM;
    }

    public void setHANDLE_IMAGEM(String HANDLE_IMAGEM) {
        this.HANDLE_IMAGEM = HANDLE_IMAGEM;
    }

    public int getHANDLE_MEDICO_SOL() {
        return HANDLE_MEDICO_SOL;
    }

    public void setHANDLE_MEDICO_SOL(int HANDLE_MEDICO_SOL) {
        this.HANDLE_MEDICO_SOL = HANDLE_MEDICO_SOL;
    }

    public int getHANDLE_PACIENTE() {
        return HANDLE_PACIENTE;
    }

    public void setHANDLE_PACIENTE(int HANDLE_PACIENTE) {
        this.HANDLE_PACIENTE = HANDLE_PACIENTE;
    }

    public int getHORA_ATENDIMENTO() {
        return HORA_ATENDIMENTO;
    }

    public void setHORA_ATENDIMENTO(int HORA_ATENDIMENTO) {
        this.HORA_ATENDIMENTO = HORA_ATENDIMENTO;
    }

    public String getMATRICULA_CONVENIO() {
        return MATRICULA_CONVENIO;
    }

    public void setMATRICULA_CONVENIO(String MATRICULA_CONVENIO) {
        this.MATRICULA_CONVENIO = MATRICULA_CONVENIO;
    }

    public String getOBSERVACAO() {
        return OBSERVACAO;
    }

    public void setOBSERVACAO(String OBSERVACAO) {
        this.OBSERVACAO = OBSERVACAO;
    }

    public String getSTATUS1() {
        return STATUS1;
    }

    public void setSTATUS1(String STATUS1) {
        this.STATUS1 = STATUS1;
    }

    public String getSTATUS2() {
        return STATUS2;
    }

    public void setSTATUS2(String STATUS2) {
        this.STATUS2 = STATUS2;
    }

    public String getSTATUS3() {
        return STATUS3;
    }

    public void setSTATUS3(String STATUS3) {
        this.STATUS3 = STATUS3;
    }

    public String getSTATUS4() {
        return STATUS4;
    }

    public void setSTATUS4(String STATUS4) {
        this.STATUS4 = STATUS4;
    }

    public String getVALOR_ATENDIMENTO() {
        return VALOR_ATENDIMENTO;
    }

    public void setVALOR_ATENDIMENTO(String VALOR_ATENDIMENTO) {
        this.VALOR_ATENDIMENTO = VALOR_ATENDIMENTO;
    }

    public Date getDAT() {
        return DAT;
    }

    public void setDAT(Date DAT) {
        this.DAT = DAT;
    }

    public int getUSUARIOID() {
        return USUARIOID;
    }

    public void setUSUARIOID(int USUARIOID) {
        this.USUARIOID = USUARIOID;
    }

    public String getCOMPLEMENTO() {
        return COMPLEMENTO;
    }

    public void setCOMPLEMENTO(String COMPLEMENTO) {
        this.COMPLEMENTO = COMPLEMENTO;
    }

    public int getHORA_EXAME_PRONTO() {
        return HORA_EXAME_PRONTO;
    }

    public void setHORA_EXAME_PRONTO(int HORA_EXAME_PRONTO) {
        this.HORA_EXAME_PRONTO = HORA_EXAME_PRONTO;
    }

    public String getEXAME_ENTREGUE_AO_PACIENTE() {
        return EXAME_ENTREGUE_AO_PACIENTE;
    }

    public void setEXAME_ENTREGUE_AO_PACIENTE(String EXAME_ENTREGUE_AO_PACIENTE) {
        this.EXAME_ENTREGUE_AO_PACIENTE = EXAME_ENTREGUE_AO_PACIENTE;
    }

    public String getLAUDO_ENTREGUE_AO_PACIENTE() {
        return LAUDO_ENTREGUE_AO_PACIENTE;
    }

    public void setLAUDO_ENTREGUE_AO_PACIENTE(String LAUDO_ENTREGUE_AO_PACIENTE) {
        this.LAUDO_ENTREGUE_AO_PACIENTE = LAUDO_ENTREGUE_AO_PACIENTE;
    }

    public String getFLAG_IMPRIMIU() {
        return FLAG_IMPRIMIU;
    }

    public void setFLAG_IMPRIMIU(String FLAG_IMPRIMIU) {
        this.FLAG_IMPRIMIU = FLAG_IMPRIMIU;
    }

    public int getID_AREAS_ATENDIMENTO() {
        return ID_AREAS_ATENDIMENTO;
    }

    public void setID_AREAS_ATENDIMENTO(int iD_AREAS_ATENDIMENTO) {
        ID_AREAS_ATENDIMENTO = iD_AREAS_ATENDIMENTO;
    }    
}
