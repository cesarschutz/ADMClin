/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.cadastros.pessoal.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class usuariosMODEL {
    private int usrid,usuarioId;
    private String usuario, descricao,senha,estatus,email,envia_email, impressora_ficha, impressora_nota_fiscal, impressora_etiqueta_envelope, impressora_codigo_de_barras, pasta_raiz;
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
