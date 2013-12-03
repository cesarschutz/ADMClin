/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.dao.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class Usuario {
    private int usrid;
    private int usuarioId;
    private String usuario;
    private String descricao;
    private String senha;
    private String estatus;
    private String email;
    private String envia_email;
    private String impressora_ficha;
    private String impressora_nota_fiscal;
    private String impressora_etiqueta_envelope;
    private String impressora_codigo_de_barras;
    private String pasta_raiz;
    private Date dat;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnvia_email() {
        return envia_email;
    }

    public void setEnvia_email(String envia_email) {
        this.envia_email = envia_email;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getUsrid() {
        return usrid;
    }

    public void setUsrid(int usrid) {
        this.usrid = usrid;
    }

    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getImpressora_ficha() {
        return impressora_ficha;
    }

    public void setImpressora_ficha(String impressora_ficha) {
        this.impressora_ficha = impressora_ficha;
    }

    public String getImpressora_nota_fiscal() {
        return impressora_nota_fiscal;
    }

    public void setImpressora_nota_fiscal(String impressora_nota_fiscal) {
        this.impressora_nota_fiscal = impressora_nota_fiscal;
    }

    public String getImpressora_etiqueta_envelope() {
        return impressora_etiqueta_envelope;
    }

    public void setImpressora_etiqueta_envelope(String impressora_etiqueta_envelope) {
        this.impressora_etiqueta_envelope = impressora_etiqueta_envelope;
    }

    public String getPasta_raiz() {
        return pasta_raiz;
    }

    public void setPasta_raiz(String pasta_raiz) {
        this.pasta_raiz = pasta_raiz;
    }

    public String getImpressora_codigo_de_barras() {
        return impressora_codigo_de_barras;
    }

    public void setImpressora_codigo_de_barras(String impressora_codigo_de_barras) {
        this.impressora_codigo_de_barras = impressora_codigo_de_barras;
    }
}
