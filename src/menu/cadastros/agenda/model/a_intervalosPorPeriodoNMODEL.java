
package menu.cadastros.agenda.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class a_intervalosPorPeriodoNMODEL {
    
    private int a_intervaloPorPeriodoNId, horarioInicial, horarioFinal, usuarioId;
    Date dat,diaInicial, diaFinal;
    String nome, descricao;

    public int getA_intervaloPorPeriodoNId() {
        return a_intervaloPorPeriodoNId;
    }

    public void setA_intervaloPorPeriodoNId(int a_intervaloPorPeriodoNId) {
        this.a_intervaloPorPeriodoNId = a_intervaloPorPeriodoNId;
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

    public Date getDiaFinal() {
        return diaFinal;
    }

    public void setDiaFinal(Date diaFinal) {
        this.diaFinal = diaFinal;
    }

    public Date getDiaInicial() {
        return diaInicial;
    }

    public void setDiaInicial(Date diaInicial) {
        this.diaInicial = diaInicial;
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
