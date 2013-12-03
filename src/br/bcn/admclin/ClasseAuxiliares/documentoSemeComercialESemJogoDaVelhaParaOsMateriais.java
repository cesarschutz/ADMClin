/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.ClasseAuxiliares;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * classe feita exclusivamente para os materiais!! nao pode ir & nem # pq na lista de materiais nos exames dos
 * atendimentos Se for nessa lsta da problemas quando formos criar relatorios & dividi as informações do material #
 * dividi os materiais na lista
 * @author Cesar Schutz
 */
public class documentoSemeComercialESemJogoDaVelhaParaOsMateriais extends PlainDocument {
    private static final long serialVersionUID = 1L;
    private int iMaxLength;

    public documentoSemeComercialESemJogoDaVelhaParaOsMateriais(int maxCaracteres) {
        super();
        iMaxLength = maxCaracteres;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

        str = str.toUpperCase();

        str = str.replaceAll("[&#%'\"\\\\/*]", "");

        if (iMaxLength <= 0) // aceitara qualquer no. de caracteres
        {
            super.insertString(offset, str, attr);
            return;
        }

        int ilen = (getLength() + str.length());
        if (ilen <= iMaxLength) // se o comprimento final for menor...
            super.insertString(offset, str, attr); // ...aceita str
    }
}
