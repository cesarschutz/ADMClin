/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.valoresrecebidos;

import java.sql.Date;

/**
 * 
 * @author BCN
 */
public class relatorioValoresRecebidossMODEL {
    private String data;
    private int codigo;
    private String Paciente;
    private String exame;
    private String valorPaciente;
    private String valorFaturado;
    private String valorPagoConvenio;
    private String diferenca;
    private String convenio;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getPaciente() {
        return Paciente;
    }

    public void setPaciente(String paciente) {
        Paciente = paciente;
    }

    public String getExame() {
        return exame;
    }

    public void setExame(String exame) {
        this.exame = exame;
    }

    public String getValorPaciente() {
        return valorPaciente;
    }

    public void setValorPaciente(String valorPaciente) {
        this.valorPaciente = valorPaciente;
    }

    public String getValorFaturado() {
        return valorFaturado;
    }

    public void setValorFaturado(String valorFaturado) {
        this.valorFaturado = valorFaturado;
    }

    public String getValorPagoConvenio() {
        return valorPagoConvenio;
    }

    public void setValorPagoConvenio(String valorPagoConvenio) {
        this.valorPagoConvenio = valorPagoConvenio;
    }

    public String getDiferenca() {
        return diferenca;
    }

    public void setDiferenca(String diferenca) {
        this.diferenca = diferenca;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

}
