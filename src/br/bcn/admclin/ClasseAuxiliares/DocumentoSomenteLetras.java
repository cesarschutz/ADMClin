/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.ClasseAuxiliares;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 
 * @author BCN
 */
public class DocumentoSomenteLetras extends PlainDocument {

    private static final long serialVersionUID = 1L;
    private int iMaxLength;

    public DocumentoSomenteLetras(int maxCaracteres) {
        super();
        iMaxLength = maxCaracteres;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

        str = str.toUpperCase();

        str = str.replaceAll("[^a-zA-Z ]", "");
        str = str.replaceAll("  ", " ");

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
