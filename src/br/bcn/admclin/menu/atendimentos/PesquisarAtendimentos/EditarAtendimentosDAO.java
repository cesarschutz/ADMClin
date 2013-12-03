/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.menu.atendimentos.PesquisarAtendimentos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author BCN
 */
public class EditarAtendimentosDAO {
    /*
     * consulta atendimentos de um paciente
     */
    public static ResultSet getConsultarAtendimentosPorNome(Connection con, String nome){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select p.nome as nomePaciente, m.nome as nomeMedico, m.crm as crmMedico, a.handle_at, a.data_atendimento, a.hora_atendimento, a.status1, a.modalidade from atendimentos A "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoId "
                    + "inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "where p.nome like ? order by a.data_atendimento");
            stmtQuery.setString(1, nome);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /*
     * consulta atendimentos de um paciente
     */
    public static ResultSet getConsultarAtendimentosPorHandleAt(Connection con, int handle_at){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select p.nome as nomePaciente, m.nome as nomeMedico, m.crm as crmMedico, a.handle_at, a.data_atendimento, a.hora_atendimento, a.status1, a.modalidade from atendimentos A "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoId "
                    + "inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "where a.handle_at=? order by a.data_atendimento");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
}
