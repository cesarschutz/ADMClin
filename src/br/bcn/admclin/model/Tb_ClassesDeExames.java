package br.bcn.admclin.model;

import java.sql.Date;

/**
 * Classe modelo da classe JIFCClassesDeExames
 * 
 * @author BCN
 */
public class Tb_ClassesDeExames {
    private int cod, modIdx, usuarioid;
    private String descricao;
    private Date data;

    public int getModIdx() {
        return modIdx;
    }

    public void setModIdx(int modIdx) {
        this.modIdx = modIdx;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(int usuarioid) {
        this.usuarioid = usuarioid;
    }

}
