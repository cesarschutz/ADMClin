/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.cadastros.convenio.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class tabelasMODEL {
    
    private int usuarioId, tabelaId, handle_convenio, handle_exame, handle_material, qtdMaterial,VAI_MATERIAIS_POR_PADRAO;
    private Date dat;
    private String cofFilme, cofCh1, cofCh2, cod_exame, sinonimo;

    public int gethandle_exame() {
        return handle_exame;
    }

    public void sethandle_exame(int handle_exame) {
        this.handle_exame = handle_exame;
    }

    public int gethandle_material() {
        return handle_material;
    }

    public void sethandle_material(int handle_material) {
        this.handle_material = handle_material;
    }

    public String getCod_exame() {
        return cod_exame;
    }

    public void setCod_exame(String cod_exame) {
        this.cod_exame = cod_exame;
    }


    public int gethandle_convenio() {
        return handle_convenio;
    }

    public void sethandle_convenio(int handle_convenio) {
        this.handle_convenio = handle_convenio;
    }

    public String getCofCh1() {
        return cofCh1;
    }

    public void setCofCh1(String cofCh1) {
        this.cofCh1 = cofCh1;
    }

    public String getCofCh2() {
        return cofCh2;
    }

    public void setCofCh2(String cofCh2) {
        this.cofCh2 = cofCh2;
    }

    public String getCofFilme() {
        return cofFilme;
    }

    public void setCofFilme(String cofFilme) {
        this.cofFilme = cofFilme;
    }

    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }

    public int getTabelaId() {
        return tabelaId;
    }

    public void setTabelaId(int tabelaId) {
        this.tabelaId = tabelaId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getQtdMaterial() {
        return qtdMaterial;
    }

    public void setQtdMaterial(int qtdMaterial) {
        this.qtdMaterial = qtdMaterial;
    }

    public int getHandle_convenio() {
        return handle_convenio;
    }

    public void setHandle_convenio(int handle_convenio) {
        this.handle_convenio = handle_convenio;
    }

    public int getHandle_exame() {
        return handle_exame;
    }

    public void setHandle_exame(int handle_exame) {
        this.handle_exame = handle_exame;
    }

    public int getHandle_material() {
        return handle_material;
    }

    public void setHandle_material(int handle_material) {
        this.handle_material = handle_material;
    }

    public String getSinonimo() {
        return sinonimo;
    }

    public void setSinonimo(String sinonimo) {
        this.sinonimo = sinonimo;
    }

    public int getVAI_MATERIAIS_POR_PADRAO() {
        return VAI_MATERIAIS_POR_PADRAO;
    }

    public void setVAI_MATERIAIS_POR_PADRAO(int VAI_MATERIAIS_POR_PADRAO) {
        this.VAI_MATERIAIS_POR_PADRAO = VAI_MATERIAIS_POR_PADRAO;
    }
    
}
