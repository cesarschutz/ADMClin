/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.impressoes.modelo2.etiquetaEnvelope;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.ATENDIMENTOS;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.impressoes.modelo2e3.fichaEBoletoDeRetirada.ESCPrinter;

/**
 *
 * @author BCN
 */
public class ImprimirEtiquetaEnvelopeModelo2 {

    private int handle_at;
    private Connection con = null;
    private ESCPrinter imprimir = new ESCPrinter(USUARIOS.impressora_etiqueta_envelope, true);
    private String nomePaciente, modalidade, dataAtendimento;
    
    public ImprimirEtiquetaEnvelopeModelo2(int handle_at) {
        this.handle_at = handle_at;
    }

    
    public boolean imprimir(){
        boolean imprimiu = false;
        con = br.bcn.admclin.dao.Conexao.fazConexao();
            try {
                
                imprimir.initialize();
                getDadosDaEtiqueta();
                imprimirEtiquetaEnvelope();
                imprimiu = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao imprimir Etiqueta. Procure o Administrador.");
            } finally{
                br.bcn.admclin.dao.Conexao.fechaConexao(con);
            }
        return imprimiu;
    }
    
    private void getDadosDaEtiqueta() throws Exception{
        ResultSet rs = ATENDIMENTOS.getConsultarDadosEtiqueta(con, handle_at);
        while(rs.next()){
            nomePaciente = rs.getString("nome");
            modalidade = nomeModalidade(rs.getString("modalidade"));
            dataAtendimento = MetodosUteis.converterDataParaMostrarAoUsuario(rs.getString("data_atendimento"));
        }
    }
    
    private void imprimirEtiquetaEnvelope() throws Exception{
        imprimir.print(nomePaciente);
        imprimir.lineFeed();
        imprimir.print(modalidade);
        imprimir.lineFeed();
        imprimir.print(String.valueOf(handle_at) + "           " + dataAtendimento);
        imprimir.lineFeed();
    }
    
    private String nomeModalidade(String modalidade){
         if ("CR".equals(modalidade)) {
             return "RX";
             
         } else if ("CT".equals(modalidade)) {
             return "TC";
             
         } else if ("MG".equals(modalidade)) {
             return "MG";
             
         } else if ("DX".equals(modalidade)) {
             return "RX";
             
         } else if ("RF".equals(modalidade)) {
             return "FL";
             
         } else if ("NM".equals(modalidade)) {
             return "MN";
             
         } else if ("US".equals(modalidade)) {
             return "US";
             
         } else if ("DR".equals(modalidade)) {
             return "RX";
             
         } else if ("MR".equals(modalidade)) {
             return "RM";
             
         } else if ("OT".equals(modalidade)) {
             return "OT";
             
         } else if ("DO".equals(modalidade)) {
             return "DE";
             
         } else if ("OD".equals(modalidade)) {
             return "OD";
             
         } else if ("TR".equals(modalidade)) {
             return "TR";
         } else {
             return "OT";
         }
     }
}