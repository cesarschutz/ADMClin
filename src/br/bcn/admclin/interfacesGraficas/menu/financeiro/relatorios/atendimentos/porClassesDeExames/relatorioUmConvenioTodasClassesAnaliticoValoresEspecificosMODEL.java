/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.atendimentos.porClassesDeExames;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class relatorioUmConvenioTodasClassesAnaliticoValoresEspecificosMODEL {
    private String paciente, convenio, exame, ch_convenio, filme_convenio, ch1_exame, ch2_exame, filme_exame, lista_material, classeDeExame, redutor, desconto_paciente, valor_correto_exame, material;
    private int handle_at;
    private Date data;

    public String getRedutor() {
        return redutor;
    }

    public void setRedutor(String redutor) {
        this.redutor = redutor;
    }

    public String getDesconto_paciente() {
        return desconto_paciente;
    }

    public void setDesconto_paciente(String desconto_paciente) {
        this.desconto_paciente = desconto_paciente;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getPaciente() {
        if (paciente.length() > 30) {
            paciente = paciente.substring(0,30);
        }
        
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getExame() {
        if (exame.length() > 24) {
            exame = exame.substring(0,24);
        }
        return exame;
    }

    public void setExame(String exame) {
        this.exame = exame;
    }

    public String getCh_convenio() {
        return ch_convenio;
    }

    public void setCh_convenio(String ch_convenio) {
        this.ch_convenio = ch_convenio;
    }

    public String getFilme_convenio() {
        return filme_convenio;
    }

    public void setFilme_convenio(String filme_convenio) {
        this.filme_convenio = filme_convenio;
    }

    public String getCh1_exame() {
        return ch1_exame;
    }

    public void setCh1_exame(String ch1_exame) {
        this.ch1_exame = ch1_exame;
    }

    public String getCh2_exame() {
        return ch2_exame;
    }

    public void setCh2_exame(String ch2_exame) {
        this.ch2_exame = ch2_exame;
    }

    public String getFilme_exame() {
        return filme_exame;
    }

    public void setFilme_exame(String flme_exame) {
        this.filme_exame = flme_exame;
    }

    public String getLista_material() {
        return lista_material;
    }

    public void setLista_material(String lista_material) {
        this.lista_material = lista_material;
    }

    public String getClasseDeExame() {
        return classeDeExame;
    }

    public void setClasseDeExame(String classeDeExame) {
        this.classeDeExame = classeDeExame;
    }

    public int getHandle_at() {
        return handle_at;
    }

    public void setHandle_at(int handle_at) {
        this.handle_at = handle_at;
    }

    public String getValor_correto_exame() {
        return valor_correto_exame;
    }

    public void setValor_correto_exame(String valor_correto_exame) {
        this.valor_correto_exame = valor_correto_exame;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
