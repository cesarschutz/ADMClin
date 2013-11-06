
package menu.cadastros.exame.model;

import java.sql.Date;

/**
 * Classe modelo da classe JIFCClassesDeExames
 * @author BCN
 */
public class tb_classesdeexamesMODEL {
    private int cod,modIdx,usuarioid;
    private String ref,descricao;
    private Date data;

    public int getModIdx() {
        return modIdx;
    }

    public void setModIdx(int modIdx) {
        this.modIdx = modIdx;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(int usuarioid) {
        this.usuarioid = usuarioid;
    }

    
}
