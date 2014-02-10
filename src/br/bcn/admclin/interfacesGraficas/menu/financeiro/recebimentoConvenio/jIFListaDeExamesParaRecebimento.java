package br.bcn.admclin.interfacesGraficas.menu.financeiro.recebimentoConvenio;

import java.util.Date;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class jIFListaDeExamesParaRecebimento extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    
    JPanel panel;
    private String tipo;
    private Date data1;
    private Date data2;
    private int handle_convenio;
    private int handle_grupo;

    /**
     * Create the frame.
     */
    public jIFListaDeExamesParaRecebimento(String tipo, Date data1, Date data2, int hanle_convenio_grupo, String nomeGrupoConvenio) {
        
        this.tipo = tipo;
        this.data1 = data1;
        this.data2 = data2;
        
        
        setBounds(100, 100, 832, 509);
        getContentPane().setLayout(null);
        
        panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Recebimento de Conv\u00EAnio", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, null));
        panel.setBounds(0, 0, 816, 479);
        getContentPane().add(panel);
        
        if (tipo.equals("convenio")) {
            this.handle_convenio = hanle_convenio_grupo;
            panel.setBorder(new TitledBorder(null, "Recebimento de Convênio: " + nomeGrupoConvenio, TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, null));
        }
        else {
            this.handle_grupo = hanle_convenio_grupo; 
            panel.setBorder(new TitledBorder(null, "Recebimento de Grupo: " + nomeGrupoConvenio, TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, null));
        }
        
        iniciarClasse();

    }
    
    private void iniciarClasse(){
        
    }
}
