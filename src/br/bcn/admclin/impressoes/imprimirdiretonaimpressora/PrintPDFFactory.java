package br.bcn.admclin.impressoes.imprimirdiretonaimpressora;

import java.io.File;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class PrintPDFFactory {

    private static PrintService impressora;  

    public void printPDF(File f){

        try{
            DocFlavor dflavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintService[] impressoras = PrintServiceLookup.lookupPrintServices(dflavor, null);
            for(PrintService ps : impressoras){
                System.out.println("Impressora Encontrada: "+ps.getName());
                if(ps.getName().contains("printer001")){
                    System.out.println("Impressora Selecionada: "+ps.getName());
                    impressora = ps;
                    break;
                }
            }
            /*
            DocPrintJob dpj = impressora.createPrintJob();  
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[fis.available()];
            int buff = 0;
            while((buff = fis.available()) != 0){
                fis.read(buffer, 0, buff);
            }
            System.out.println(new String(buffer));
            InputStream stream = new ByteArrayInputStream(buffer);  
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;  
            Doc doc = new SimpleDoc(stream, flavor, null);  
            dpj.print(doc, null);
            */
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        PrintPDFFactory pdffactory = new PrintPDFFactory();
        pdffactory.printPDF(new File("c:/teste.pdf"));
    }
}