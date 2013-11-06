/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package impressoes.modelo2.etiquetaCodigoDeBarras;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;


/**
 *
 * @author BCN
 */
public class ImprimirEtiquetaCodigoDeBarrasModelo2 {

    private String handle_at;
    private String caminhoImpressora;
    
    public ImprimirEtiquetaCodigoDeBarrasModelo2(int handle_at, String caminhoImpressora) {
        this.handle_at = arrumaHandle(handle_at);
        this.caminhoImpressora = caminhoImpressora;
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
            
            
            return true;
        }catch(Exception e){
            System.out.println("Erro: " + e);
            return false;
        }
    }
    
    
}
