/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.impressoes.modelo2e3;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.ESCPrinter;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.ATENDIMENTOS;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 *
 * @author BCN
 */
public class ImprimirEtiquetaEnvelopeModelo2 {

    private int handle_at;
    private Connection con = null;
    private ESCPrinter imprimir;
    private String nomePaciente, nome_area_atendimento, dataAtendimento;
    private String nomeDoArquivo = janelaPrincipal.internalFrameJanelaPrincipal.codigoParaImpressoesLinux + "ETIQUETA";
    
    public ImprimirEtiquetaEnvelopeModelo2(int handle_at) {
        this.handle_at = handle_at;
    }
    
    private void instanciarImpressora(){
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
            imprimir = new ESCPrinter(nomeDoArquivo, true);
        }else{
            imprimir = new ESCPrinter(USUARIOS.impressora_etiqueta_envelope, true);
        }
    }
    
    private void imprimirNotaCasoSejaLinux() throws IOException{
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
            Runtime.getRuntime().exec("lpr -P " + USUARIOS.impressora_etiqueta_envelope + " " + nomeDoArquivo);  
        }
    }

    
    public boolean imprimir(){
        boolean imprimiu = false;
        con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
            try {
                instanciarImpressora();
                imprimir.initialize();
                getDadosDaEtiqueta();
                imprimirEtiquetaEnvelope();
                imprimirNotaCasoSejaLinux();
                imprimir.close();
                imprimiu = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao imprimir Etiqueta. Procure o Administrador.");
            } finally{
                br.bcn.admclin.dao.dbris.Conexao.fechaConexao(con);
            }
        return imprimiu;
    }
    
    private void getDadosDaEtiqueta() throws Exception{
        ResultSet rs = ATENDIMENTOS.getConsultarDadosEtiqueta(con, handle_at);
        while(rs.next()){
            nomePaciente = rs.getString("nome");
            nome_area_atendimento = rs.getString("nomeAreaAtendimento");
            dataAtendimento = MetodosUteis.converterDataParaMostrarAoUsuario(rs.getString("data_atendimento"));
        }
    }
    
    private void imprimirEtiquetaEnvelope() throws Exception{
        imprimir.print(nomePaciente);
        imprimir.lineFeed();
        imprimir.print(nome_area_atendimento);
        imprimir.lineFeed();
        imprimir.print(String.valueOf(handle_at) + "           " + dataAtendimento);
        imprimir.lineFeed();
        imprimir.lineFeed();
        imprimir.lineFeed();
    }
}
