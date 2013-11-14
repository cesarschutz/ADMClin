package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.model.A_intervalosDiarios;
import br.bcn.admclin.model.A_intervalosDiariosN;

/**
 *
 * @author BCN
 */
public class A_INTERVALOSDIARIOS {
    
    public  static boolean conseguiuConsulta;
    
    /**
     * Consulta os dados de um intervalo diario
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarAgendasDeUmIntervaloDiario(Connection con, int intervaloDiarioNId){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select distinct a.handle_agenda, b.nome from a_intervalosdiarios as a inner join agendas as b on a.handle_agenda = b.handle_agenda  where a_intervalodiarionid = ?");
        stmtQuery.setInt(1, intervaloDiarioNId);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Intervalo Diário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
        
    
    /**
     * Cadastra um novo intervalo diario no Banco de Dados.
     * @param Connection 
     * @param A_INTERVALOSDIARIOS
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, A_intervalosDiarios model){
        boolean cadastro = false;
        String sql = "insert into a_intervalosdiarios (a_intervalodiarionid, handle_agenda) values(?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getA_intervaloDiarioNId());
            stmt.setInt(2, model.getAgendaId());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Agenda no Intervalo Diário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um Intervalo Diario do Banco De Dados
     * @param Connection
     * @param intervaloDiarioNId 
     * @return boolean
     */
    public static boolean setDeletar(Connection con, int intervaloDiarioNId){
        boolean deleto = false; 
        String sql="delete from a_intervalosdiarios where a_intervalodiarionid=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, intervaloDiarioNId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Intervalo Diário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
}
