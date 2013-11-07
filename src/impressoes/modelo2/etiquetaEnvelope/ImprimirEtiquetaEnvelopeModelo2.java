/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package impressoes.modelo2.etiquetaEnvelope;

import ClasseAuxiliares.MetodosUteis;
import impressoes.modelo2e3.fichaEBoletoDeRetirada.ESCPrinter;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.naming.spi.DirStateFactory;
import javax.swing.JOptionPane;

import br.bcn.admclin.dao.DADOS_EMPRESA;
import menu.cadastros.pessoal.dao.usuariosDAO;

/**
 *
 * @author BCN
 */
public class ImprimirEtiquetaEnvelopeModelo2 {

    private int handle_at;
    private Connection con = null;
    private ESCPrinter imprimir = new ESCPrinter(usuariosDAO.impressora_etiqueta_envelope, true);
    private String nomePaciente, modalidade, dataAtendimento;
    
    public ImprimirEtiquetaEnvelopeModelo2(int handle_at) {
        this.handle_at = handle_at;
    }

    
    public boolean imprimir(){
        boolean imprimiu = false;
        con = conexao.Conexao.fazConexao();
            try {
                
                imprimir.initialize();
                getDadosDaEtiqueta();
                imprimirEtiquetaEnvelope();
                imprimiu = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao imprimir Etiqueta. Procure o Administrador.");
            } finally{
                conexao.Conexao.fechaConexao(con);
            }
        return imprimiu;
    }
    
    private void getDadosDaEtiqueta() throws Exception{
        ResultSet rs = ImprimirEtiquetaEnvelopeModelo2DAO.getConsultarDadosEtiqueta(con, handle_at);
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
