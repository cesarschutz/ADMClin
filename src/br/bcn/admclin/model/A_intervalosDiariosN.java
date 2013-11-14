
package br.bcn.admclin.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class A_intervalosDiariosN {
    
    private int a_intervaloDiarioId, horarioInicial, horarioFinal, usuarioId;
    Date dat,diadoIntervalo;
    String nome, descricao;

    public int getA_intervaloDiarioId() {
        return a_intervaloDiarioId;
    }

    public void setA_intervaloDiarioId(int a_intervaloDiarioId) {
        this.a_intervaloDiarioId = a_intervaloDiarioId;
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

    public Date getDiadoIntervalo() {
        return diadoIntervalo;
    }

    public void setDiadoIntervalo(Date diadoIntervalo) {
        this.diadoIntervalo = diadoIntervalo;
    }

    public int getHorarioFinal() {
        return horarioFinal;
    }

    public void setHorarioFinal(int horarioFinal) {
        this.horarioFinal = horarioFinal;
    }

    public int getHorarioInicial() {
        return horarioInicial;
    }

    public void setHorarioInicial(int horarioInicial) {
        this.horarioInicial = horarioInicial;
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
