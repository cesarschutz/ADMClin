
package br.bcn.admclin.dao.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class Agenda {
    
    private int seg,ter,qua,qui,sex,sab,dom,handle_agenda,usuarioId,horarioInicialTurno1,horarioFinalTurno1,horarioInicialTurno2,horarioFinalTurno2, horarioInicialTurno3, horarioFinalTurno3, horarioInicialTurno4, horarioFinalTurno4, duracaoTurno1, duracaoTurno2, duracaoTurno3, duracaoTurno4;
    private int ativa, MODALIDADE_CR, MODALIDADE_CT, MODALIDADE_DR, MODALIDADE_DX, MODALIDADE_MG, MODALIDADE_MR, MODALIDADE_NM, MODALIDADE_OT, MODALIDADE_RF, MODALIDADE_DO, MODALIDADE_US, MODALIDADE_OD, MODALIDADE_TR, ID_AREAS_ATENDIMENTO;
    private String nome,descricao;
    private Date data;

    public int getMODALIDADE_CR() {
        return MODALIDADE_CR;
    }

    public void setMODALIDADE_CR(int MODALIDADE_CR) {
        this.MODALIDADE_CR = MODALIDADE_CR;
    }

    public int getMODALIDADE_CT() {
        return MODALIDADE_CT;
    }

    public void setMODALIDADE_CT(int MODALIDADE_CT) {
        this.MODALIDADE_CT = MODALIDADE_CT;
    }

    public int getMODALIDADE_DR() {
        return MODALIDADE_DR;
    }

    public void setMODALIDADE_DR(int MODALIDADE_DR) {
        this.MODALIDADE_DR = MODALIDADE_DR;
    }

    public int getMODALIDADE_DX() {
        return MODALIDADE_DX;
    }

    public void setMODALIDADE_DX(int MODALIDADE_DX) {
        this.MODALIDADE_DX = MODALIDADE_DX;
    }

    public int getMODALIDADE_MG() {
        return MODALIDADE_MG;
    }

    public void setMODALIDADE_MG(int MODALIDADE_MG) {
        this.MODALIDADE_MG = MODALIDADE_MG;
    }

    public int getMODALIDADE_MR() {
        return MODALIDADE_MR;
    }

    public void setMODALIDADE_MR(int MODALIDADE_MR) {
        this.MODALIDADE_MR = MODALIDADE_MR;
    }

    public int getMODALIDADE_NM() {
        return MODALIDADE_NM;
    }

    public void setMODALIDADE_NM(int MODALIDADE_NM) {
        this.MODALIDADE_NM = MODALIDADE_NM;
    }

    public int getMODALIDADE_OT() {
        return MODALIDADE_OT;
    }

    public void setMODALIDADE_OT(int MODALIDADE_OT) {
        this.MODALIDADE_OT = MODALIDADE_OT;
    }

    public int getMODALIDADE_RF() {
        return MODALIDADE_RF;
    }

    public void setMODALIDADE_RF(int MODALIDADE_RF) {
        this.MODALIDADE_RF = MODALIDADE_RF;
    }

    public int getMODALIDADE_US() {
        return MODALIDADE_US;
    }

    public void setMODALIDADE_US(int MODALIDADE_US) {
        this.MODALIDADE_US = MODALIDADE_US;
    }

    public int getAtiva() {
        return ativa;
    }

    public void setAtiva(int ativa) {
        this.ativa = ativa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDom() {
        return dom;
    }

    public void setDom(int dom) {
        this.dom = dom;
    }

    public int getDuracaoTurno1() {
        return duracaoTurno1;
    }

    public void setDuracaoTurno1(int duracaoTurno1) {
        this.duracaoTurno1 = duracaoTurno1;
    }

    public int getDuracaoTurno2() {
        return duracaoTurno2;
    }

    public void setDuracaoTurno2(int duracaoTurno2) {
        this.duracaoTurno2 = duracaoTurno2;
    }

    public int getDuracaoTurno3() {
        return duracaoTurno3;
    }

    public void setDuracaoTurno3(int duracaoTurno3) {
        this.duracaoTurno3 = duracaoTurno3;
    }

    public int getDuracaoTurno4() {
        return duracaoTurno4;
    }

    public void setDuracaoTurno4(int duracaoTurno4) {
        this.duracaoTurno4 = duracaoTurno4;
    }

    public int getHandle_agenda() {
        return handle_agenda;
    }

    public void setHandle_agenda(int handle_agenda) {
        this.handle_agenda = handle_agenda;
    }

    public int getHorarioFinalTurno1() {
        return horarioFinalTurno1;
    }

    public void setHorarioFinalTurno1(int horarioFinalTurno1) {
        this.horarioFinalTurno1 = horarioFinalTurno1;
    }

    public int getHorarioFinalTurno2() {
        return horarioFinalTurno2;
    }

    public void setHorarioFinalTurno2(int horarioFinalTurno2) {
        this.horarioFinalTurno2 = horarioFinalTurno2;
    }

    public int getHorarioFinalTurno3() {
        return horarioFinalTurno3;
    }

    public void setHorarioFinalTurno3(int horarioFinalTurno3) {
        this.horarioFinalTurno3 = horarioFinalTurno3;
    }

    public int getHorarioFinalTurno4() {
        return horarioFinalTurno4;
    }

    public void setHorarioFinalTurno4(int horarioFinalTurno4) {
        this.horarioFinalTurno4 = horarioFinalTurno4;
    }

    public int getHorarioInicialTurno1() {
        return horarioInicialTurno1;
    }

    public void setHorarioInicialTurno1(int horarioInicialTurno1) {
        this.horarioInicialTurno1 = horarioInicialTurno1;
    }

    public int getHorarioInicialTurno2() {
        return horarioInicialTurno2;
    }

    public void setHorarioInicialTurno2(int horarioInicialTurno2) {
        this.horarioInicialTurno2 = horarioInicialTurno2;
    }

    public int getHorarioInicialTurno3() {
        return horarioInicialTurno3;
    }

    public void setHorarioInicialTurno3(int horarioInicialTurno3) {
        this.horarioInicialTurno3 = horarioInicialTurno3;
    }

    public int getHorarioInicialTurno4() {
        return horarioInicialTurno4;
    }

    public void setHorarioInicialTurno4(int horarioInicialTurno4) {
        this.horarioInicialTurno4 = horarioInicialTurno4;
    }

    public int getMODALIDADE_DO() {
        return MODALIDADE_DO;
    }

    public void setMODALIDADE_DO(int MODALIDADE_DO) {
        this.MODALIDADE_DO = MODALIDADE_DO;
    }

    public int getMODALIDADE_OD() {
        return MODALIDADE_OD;
    }

    public void setMODALIDADE_OD(int MODALIDADE_OD) {
        this.MODALIDADE_OD = MODALIDADE_OD;
    }

    public int getMODALIDADE_TR() {
        return MODALIDADE_TR;
    }

    public void setMODALIDADE_TR(int MODALIDADE_TR) {
        this.MODALIDADE_TR = MODALIDADE_TR;
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

    public int getID_AREAS_ATENDIMENTO() {
        return ID_AREAS_ATENDIMENTO;
    }

    public void setID_AREAS_ATENDIMENTO(int iD_AREAS_ATENDIMENTO) {
        ID_AREAS_ATENDIMENTO = iD_AREAS_ATENDIMENTO;
    }
    
    
    
    
}
