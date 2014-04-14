package br.bcn.admclin.dao.model;

import java.sql.Date;

/**
 * Modelo de Exame.
 * @author BCN
 */
public class Exames {
    private int HANDLE_EXAME, HANDLE_CLASSEDEEXAME, usuarioId, duracao, id_areas_atendimento;
    private String NOME, qtdHoras, laudo, modalidade;
    Date data;

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public int getHANDLE_EXAME() {
        return HANDLE_EXAME;
    }

    public void setHANDLE_EXAME(int HANDLE_EXAME) {
        this.HANDLE_EXAME = HANDLE_EXAME;
    }

    public String getLaudo() {
        return laudo;
    }

    public void setLaudo(String laudo) {
        this.laudo = laudo;
    }

    
    public String getQtdHoras() {
        return qtdHoras;
    }

    public void setQtdHoras(String qtdHoras) {
        this.qtdHoras = qtdHoras;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getHANDLE_CLASSEDEEXAME() {
        return HANDLE_CLASSEDEEXAME;
    }

    public void setHANDLE_CLASSEDEEXAME(int HANDLE_CLASSEDEEXAME) {
        this.HANDLE_CLASSEDEEXAME = HANDLE_CLASSEDEEXAME;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public int getId_areas_atendimento() {
        return id_areas_atendimento;
    }

    public void setId_areas_atendimento(int id_areas_atendimento) {
        this.id_areas_atendimento = id_areas_atendimento;
    }
    
}
