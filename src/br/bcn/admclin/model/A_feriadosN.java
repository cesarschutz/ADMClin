
package br.bcn.admclin.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class A_feriadosN {
    
    private int handleFeriadoN, usuarioId;
    Date dat;
    String nome, descricao, diaDoFeriado;

    public int getHandleFeriadoN() {
        return handleFeriadoN;
    }

    public void setHandleFeriadoN(int handleFeriadoN) {
        this.handleFeriadoN = handleFeriadoN;
    }

    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDiaDoFeriado() {
        return diaDoFeriado;
    }

    public void setDiaDoFeriado(String diaDoFeriado) {
        this.diaDoFeriado = diaDoFeriado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}
