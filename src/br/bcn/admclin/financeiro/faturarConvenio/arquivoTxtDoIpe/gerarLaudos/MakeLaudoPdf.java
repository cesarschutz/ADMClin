/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.financeiro.faturarConvenio.arquivoTxtDoIpe.gerarLaudos;

import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.firebirdsql.gds.impl.jni.JniGDSImpl;

import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 *
 * @author BCN1
 */
public class MakeLaudoPdf {
    
    public static String domain;
    public static String homeDir;
    public static String localDir = "PacLinkWeb10";
    public static String portalname;
    public static String accnum;
    public static String typeAsp;
    public static String DS;
    
    public void criaLaudo(int handle_at, String caminhoPDF) {

        if (OSvalidator.isWindows()) {
            DS = "\\";
        } else {
            DS = "/";
        }
        String DS = "";
        if (OSvalidator.isWindows()) {
           DS = "\\";
           homeDir = System.getenv("SystemDrive")+DS;
        } else {
           DS = "/";
           homeDir = System.getenv("HOME")+DS+"Documents";
        }
        //String curDir = System.getProperty("user.dir");
        //String curDir = "c:\\PacLinkWeb10";
        String caminhoParaSalvarLaudoPDF =caminhoPDF;

        try {
        	
            domain 		= janelaPrincipal.RISIP;
            portalname 	= "portal40";
            accnum 		= String.valueOf(handle_at);
            typeAsp     = "MR";
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Parâmetros insuficientes...");
            System.exit(-1);
        }

        String retornoPaginaAsp = aspPages.getInformacoesDoLaudo(accnum, typeAsp);
        
        String retorno[] = retornoPaginaAsp.split("\\|\\|");

            String modelo = "0";
            String numeroMaxDeLinhas = "30";
            String fontsize = "11";

            boolean colocarAssinatura = true;

            String nome_paciente = retorno[1];
            String radiologista = retorno[9];
            String nome_medico = retorno[5];
            String data_exame = retorno[0];
            String laudo = retorno[12];
            laudo = laudo.replaceAll("\\[\\]", "\n");

            try {
                switch (Integer.valueOf(modelo)) {
                    case 3:
                        //System.out.println("3");
                        CriandoLaudoPDFModeloTres.criarPDF(caminhoParaSalvarLaudoPDF + "\\" + accnum + ".pdf", accnum, data_exame, nome_paciente,
                                nome_medico, laudo, radiologista, numeroMaxDeLinhas, colocarAssinatura, Integer.valueOf(fontsize));
                        break;
                    default:
                        //System.out.println("Default");
                        CriandoLaudoPDFModeloTres.criarPDF(caminhoParaSalvarLaudoPDF + "\\" + accnum + ".pdf", accnum, data_exame, nome_paciente,
                                nome_medico, laudo, radiologista, numeroMaxDeLinhas, colocarAssinatura, Integer.valueOf(fontsize));
                        break;
                }
            } catch (Exception e) {
            }
        //}
    }
    public static String getHora() {

        // cria um StringBuilder  
        StringBuilder sb = new StringBuilder();

        // cria um GregorianCalendar que vai conter a hora atual  
        GregorianCalendar d = new GregorianCalendar();

        // anexa do StringBuilder os dados da hora  
        sb.append(d.get(GregorianCalendar.HOUR_OF_DAY));
        sb.append(d.get(GregorianCalendar.MINUTE));
        sb.append(d.get(GregorianCalendar.SECOND));

        // retorna a String do StringBuilder  
        return sb.toString();

    }
    
}
