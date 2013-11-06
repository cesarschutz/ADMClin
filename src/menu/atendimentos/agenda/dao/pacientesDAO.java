/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.atendimentos.agenda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import menu.atendimentos.agenda.model.pacientesMODEL;

/**
 *
 * @author CeSaR

 * 
 * */
public class pacientesDAO {
    /**
     * Consulta todos os pacientes exitentes no Banco De Dados.
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from PACIENTES order by NOME");
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Pacientes. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        } 
    }
    
    //atualizar telefones do paciente
    public static boolean setUpdate(Connection con, pacientesMODEL paciente){
        boolean cadastro = false;
        String sql = "update PACIENTES set usuarioid=?, dat=?, telefone=?, celular=? where handle_paciente=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, paciente.getUsuarioId());
            stmt.setDate(2, paciente.getDat());
            stmt.setString(3, paciente.getTelefone());
            stmt.setString(4, paciente.getCelular());
            stmt.setInt(5, paciente.getHANDLE_PACIENTE());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao atualizar telefones do Paciente. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
}
