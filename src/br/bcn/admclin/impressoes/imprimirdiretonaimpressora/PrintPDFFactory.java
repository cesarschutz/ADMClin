package br.bcn.admclin.impressoes.imprimirdiretonaimpressora;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JOptionPane;

public class PrintPDFFactory {

    private static PrintService impressora;  

    public void printPDF(File f, String impressoraParaSelecionar){

    	try{
            DocFlavor dflavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintService[] impressoras = PrintServiceLookup.lookupPrintServices(dflavor, null);
            for(PrintService ps : impressoras){
                System.out.println("Impressora Encontrada: "+ps.getName());
                if(ps.getName().contains(impressoraParaSelecionar)){
                    System.out.println("Impressora Selecionada: "+ps.getName());
                    impressora = ps;
                    break;
                }
            }

            
            DocPrintJob dpj = impressora.createPrintJob();  
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[fis.available()];
            int buff = 0;
            while((buff = fis.available()) != 0){
                fis.read(buffer, 0, buff);
            }
            //System.out.println(new String(buffer));
            InputStream stream = new ByteArrayInputStream(buffer);  
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;  
            Doc doc = new SimpleDoc(stream, flavor, null);  
            dpj.print(doc, null);
            //System.out.println(((char) 27) + ((char) 69) + "fimmmmmmm");
            
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao Imprimir. Procure o Administrador.");
        }

    }

    /*
    public static void main(String[] args) {
        PrintPDFFactory pdffactory = new PrintPDFFactory();
        pdffactory.printPDF(new File("c:/teste.pdf"), "VOX");
    }
    */
}