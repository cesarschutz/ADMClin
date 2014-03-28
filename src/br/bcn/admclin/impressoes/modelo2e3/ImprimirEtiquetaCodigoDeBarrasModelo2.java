/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.impressoes.modelo2e3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import br.bcn.admclin.ClasseAuxiliares.ESCPrinter;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;


/**
 *
 * @author BCN
 */
public class ImprimirEtiquetaCodigoDeBarrasModelo2 {

    private String handle_at;
    private String caminhoImpressora;
    private String nomeDoArquivo = janelaPrincipal.internalFrameJanelaPrincipal.codigoParaImpressoesLinux + "CODIGOBARRAS";
    
    public ImprimirEtiquetaCodigoDeBarrasModelo2(int handle_at) {
        this.handle_at = arrumaHandle(handle_at);
    }
    
    private void instanciarImpressora(){
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
            caminhoImpressora = nomeDoArquivo;
        }else{
            caminhoImpressora = USUARIOS.impressora_codigo_de_barras;
        }
    }
    
    private void imprimirNotaCasoSejaLinux() throws IOException{
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
            Runtime.getRuntime().exec("lpr -P " + USUARIOS.impressora_codigo_de_barras + " " + nomeDoArquivo);  
        }
    }
    
    private String arrumaHandle(int handle_at){
        String handleAt = String.valueOf(handle_at);
        if(handleAt.length() < 9){
            handleAt = "000000000".substring(handleAt.length())+handleAt;
        }
        return handleAt;
    }
    
    public boolean writeFile(){
        try{ 
            instanciarImpressora();
            PrintWriter fo = new PrintWriter(new FileOutputStream(new File(caminhoImpressora)));
            fo.print((char) 2);
            fo.print("n");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print((char) 2);
            fo.print("M0600");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("K170");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("O0220");
            fo.print("V0");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print((char) 2);
            fo.print("f320");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print((char) 2);
            fo.print("c0000");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print((char) 2); 
            fo.print("r");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print((char) 1);
            fo.print("D");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print((char) 2);
            fo.print("L");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("D11");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("H10");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("PC");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("A2");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("1E0308000100024"+handle_at);
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("1E0308000100232"+handle_at);
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("Q0001");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.print("E");
            fo.print((char) 13);
            fo.print((char) 10);
            fo.close();
            
            imprimirNotaCasoSejaLinux();
            return true;
        }catch(Exception e){
            System.out.println("Erro: " + e);
            return false;
        }
    }
    
    
}
