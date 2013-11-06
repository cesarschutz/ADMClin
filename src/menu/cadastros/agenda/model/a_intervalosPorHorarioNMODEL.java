
package menu.cadastros.agenda.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class a_intervalosPorHorarioNMODEL {
    
    private int a_intervaloPorHorarioNId, horarioInicial, horarioFinal, seg, ter, qua, qui, sex, sab, dom, usuarioId;
    Date dat;
    String nome;

    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }

    public int getDom() {
        return dom;
    }

    public void setDom(int dom) {
        this.dom = dom;
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

    public int getA_intervaloPorHorarioNId() {
        return a_intervaloPorHorarioNId;
    }

    public void setA_intervaloPorHorarioNId(int a_intervaloPorHorarioNId) {
        this.a_intervaloPorHorarioNId = a_intervaloPorHorarioNId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQua() {
        return qua;
    }

    public void setQua(int qua) {
        this.qua = qua;
    }

    public int getQui() {
        return qui;
    }

    public void setQui(int qui) {
        this.qui = qui;
    }

    public int getSab() {
        return sab;
    }

    public void setSab(int sab) {
        this.sab = sab;
    }

    public int getSeg() {
        return seg;
    }

    public void setSeg(int seg) {
        this.seg = seg;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getTer() {
        return ter;
    }

    public void setTer(int ter) {
        this.ter = ter;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    
}
