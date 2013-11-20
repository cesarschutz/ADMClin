/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.model;

/**
 *
 * @author CeSaR
 */
public class Atendimento_Exames {

    private int HANDLE_AT, HANDLE_EXAME, DURACAO;
    private String LADO, MATERIAL, LISTA_MATERIAIS, NUMERO_SEQUENCIA;
    private double VALOR_EXAME, VALOR_PACIENTE, VALOR_CONVENIO, FILME_CONVENIO, CH_CONVENIO, CH1_EXAME, CH2_EXAME, FILME_EXAME, REDUTOR, DESCONTO_PACIENTE, PORCENTAGEM_CONVENIO, PORCENTAGEM_PACIENTE;
    private double VALOR_CORRETO_EXAME, VALOR_CORRETO_CONVENIO, VALOR_CORRETO_PACIENTE, VALOR_DESCONTO;

    public int getDURACAO() {
        return DURACAO;
    }

    public void setDURACAO(int DURACAO) {
        this.DURACAO = DURACAO;
    }

    public int getHANDLE_AT() {
        return HANDLE_AT;
    }

    public void setHANDLE_AT(int HANDLE_AT) {
        this.HANDLE_AT = HANDLE_AT;
    }

    public int getHANDLE_EXAME() {
        return HANDLE_EXAME;
    }

    public void setHANDLE_EXAME(int HANDLE_EXAME) {
        this.HANDLE_EXAME = HANDLE_EXAME;
    }

    public String getLADO() {
        return LADO;
    }

    public void setLADO(String LADO) {
        this.LADO = LADO;
    }

    public String getMATERIAL() {
        return MATERIAL;
    }

    public void setMATERIAL(String MATERIAL) {
        this.MATERIAL = MATERIAL;
    }

    public double getVALOR_CONVENIO() {
        return VALOR_CONVENIO;
    }

    public void setVALOR_CONVENIO(double VALOR_CONVENIO) {
        this.VALOR_CONVENIO = VALOR_CONVENIO;
    }

    public double getVALOR_EXAME() {
        return VALOR_EXAME;
    }

    public void setVALOR_EXAME(double VALOR_EXAME) {
        this.VALOR_EXAME = VALOR_EXAME;
    }

    public double getVALOR_PACIENTE() {
        return VALOR_PACIENTE;
    }

    public void setVALOR_PACIENTE(double VALOR_PACIENTE) {
        this.VALOR_PACIENTE = VALOR_PACIENTE;
    }

    public double getFILME_CONVENIO() {
        return FILME_CONVENIO;
    }

    public void setFILME_CONVENIO(double FILME_CONVENIO) {
        this.FILME_CONVENIO = FILME_CONVENIO;
    }

    public double getCH_CONVENIO() {
        return CH_CONVENIO;
    }

    public void setCH_CONVENIO(double CH_CONVENIO) {
        this.CH_CONVENIO = CH_CONVENIO;
    }

    public double getCH1_EXAME() {
        return CH1_EXAME;
    }

    public void setCH1_EXAME(double CH1_EXAME) {
        this.CH1_EXAME = CH1_EXAME;
    }

    public double getCH2_EXAME() {
        return CH2_EXAME;
    }

    public void setCH2_EXAME(double CH2_EXAME) {
        this.CH2_EXAME = CH2_EXAME;
    }

    public double getFILME_EXAME() {
        return FILME_EXAME;
    }

    public void setFILME_EXAME(double FILME_EXAME) {
        this.FILME_EXAME = FILME_EXAME;
    }

    public String getLISTA_MATERIAIS() {
        return LISTA_MATERIAIS;
    }

    public void setLISTA_MATERIAIS(String LISTA_MATERIAIS) {
        this.LISTA_MATERIAIS = LISTA_MATERIAIS;
    }

    public double getREDUTOR() {
        return REDUTOR;
    }

    public void setREDUTOR(double REDUTOR) {
        this.REDUTOR = REDUTOR;
    }

    public double getDESCONTO_PACIENTE() {
        return DESCONTO_PACIENTE;
    }

    public void setDESCONTO_PACIENTE(double DESCONTO_PACIENTE) {
        this.DESCONTO_PACIENTE = DESCONTO_PACIENTE;
    }

    public double getPORCENTAGEM_CONVENIO() {
        return PORCENTAGEM_CONVENIO;
    }

    public void setPORCENTAGEM_CONVENIO(double PORCENTAGEM_CONVENIO) {
        this.PORCENTAGEM_CONVENIO = PORCENTAGEM_CONVENIO;
    }

    public double getPORCENTAGEM_PACIENTE() {
        return PORCENTAGEM_PACIENTE;
    }

    public void setPORCENTAGEM_PACIENTE(double PORCENTAGEM_PACIENTE) {
        this.PORCENTAGEM_PACIENTE = PORCENTAGEM_PACIENTE;
    }

    public double getVALOR_CORRETO_EXAME() {
        return VALOR_CORRETO_EXAME;
    }

    public void setVALOR_CORRETO_EXAME(double VALOR_CORRETO_EXAME) {
        this.VALOR_CORRETO_EXAME = VALOR_CORRETO_EXAME;
    }

    public double getVALOR_CORRETO_CONVENIO() {
        return VALOR_CORRETO_CONVENIO;
    }

    public void setVALOR_CORRETO_CONVENIO(double VALOR_CORRETO_CONVENIO) {
        this.VALOR_CORRETO_CONVENIO = VALOR_CORRETO_CONVENIO;
    }

    public double getVALOR_CORRETO_PACIENTE() {
        return VALOR_CORRETO_PACIENTE;
    }

    public void setVALOR_CORRETO_PACIENTE(double VALOR_CORRETO_PACIENTE) {
        this.VALOR_CORRETO_PACIENTE = VALOR_CORRETO_PACIENTE;
    }

    public double getVALOR_DESCONTO() {
        return VALOR_DESCONTO;
    }

    public void setVALOR_DESCONTO(double VALOR_DESCONTO) {
        this.VALOR_DESCONTO = VALOR_DESCONTO;
    }

    public String getNUMERO_SEQUENCIA() {
        return NUMERO_SEQUENCIA;
    }

    public void setNUMERO_SEQUENCIA(String NUMERO_SEQUENCIA) {
        this.NUMERO_SEQUENCIA = NUMERO_SEQUENCIA;
    }
}
