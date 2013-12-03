/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Atendimento_Exames;

/**
 *
 * @author CeSaR
 */
public class ATENDIMENTO_EXAMES {
    /**
     * Salva os exames de um atendimento no Banco De Dados.
     * @param Connection
     * @param Atendimento_Exames
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, Atendimento_Exames atendimento_exame ){
        boolean cadastro = false;
        String sql = "insert into atendimento_exames (handle_at, handle_exame, duracao,"
                + "lado, material, valor_exame, valor_paciente, valor_convenio,"
                + "ch_convenio, filme_convenio, ch1_exame, ch2_exame, filme_exame, lista_materiais,redutor,desconto_paciente,porcentagem_convenio,porcentagem_paciente,valor_correto_exame,valor_correto_convenio,valor_correto_paciente,VALOR_DESCONTO, NUMERO_SEQUENCIA) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try{

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, atendimento_exame.getHANDLE_AT());
            stmt.setInt(2, atendimento_exame.getHANDLE_EXAME());
            stmt.setInt(3, atendimento_exame.getDURACAO());
            
            stmt.setString(4, atendimento_exame.getLADO());
            stmt.setString(5, atendimento_exame.getMATERIAL());
            stmt.setDouble(6, atendimento_exame.getVALOR_EXAME());
            stmt.setDouble(7, atendimento_exame.getVALOR_PACIENTE());
            stmt.setDouble(8, atendimento_exame.getVALOR_CONVENIO());
            
            stmt.setDouble(9, atendimento_exame.getCH_CONVENIO());
            stmt.setDouble(10, atendimento_exame.getFILME_CONVENIO());
            stmt.setDouble(11, atendimento_exame.getCH1_EXAME());
            stmt.setDouble(12, atendimento_exame.getCH2_EXAME());
            stmt.setDouble(13, atendimento_exame.getFILME_EXAME());
            stmt.setString(14, atendimento_exame.getLISTA_MATERIAIS());
            
            stmt.setDouble(15, atendimento_exame.getREDUTOR());
            stmt.setDouble(16, atendimento_exame.getDESCONTO_PACIENTE());
            stmt.setDouble(17, atendimento_exame.getPORCENTAGEM_CONVENIO());
            stmt.setDouble(18, atendimento_exame.getPORCENTAGEM_PACIENTE());
            stmt.setDouble(19, atendimento_exame.getVALOR_CORRETO_EXAME());
            stmt.setDouble(20, atendimento_exame.getVALOR_CORRETO_CONVENIO());
            stmt.setDouble(21, atendimento_exame.getVALOR_CORRETO_PACIENTE());
            stmt.setDouble(22, atendimento_exame.getVALOR_DESCONTO());
            stmt.setString(23, atendimento_exame.getNUMERO_SEQUENCIA());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao registrar Exame no Atendimento. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um Exame de um atendimento do Banco De Dados
     * @param Connection
     * @return boolean
     */
    public static boolean setDeletarTodosDeUmAtendimento(Connection con, int handle_at){
        boolean deleto = false; 
        String sql="delete from atendimento_exames where HANDLE_AT=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, handle_at);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Exames. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }    
    /**
     * Consulta Todos os exames de um atendimento agenda existentes no Banco de Dados 
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarExamesDeUmAtendimento(Connection con, int handle_at){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select E.nome as nomeExame, a.handle_exame, A.duracao,  a.valor_exame, a.valor_paciente, a.valor_convenio, a.ch_convenio, a.filme_convenio, a.ch1_exame, a.ch2_exame, a.filme_exame, a.lista_materiais, a.lado, a.material, a.redutor, a.desconto_paciente, a.porcentagem_convenio, a.porcentagem_paciente, a.valor_correto_exame, a.valor_correto_convenio, a.valor_correto_paciente, a.valor_desconto,a.atendimento_exame_id from atendimento_exames A "
                    + "inner join exames e on a.handle_exame = e.handle_exame "
                    + "where a.handle_at = ? order by numero_sequencia");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Exames do Atendimento. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
}
