/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.impressoes.modelo2.notaFiscal;

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
public class NotaFiscalDAO {
    
    /**
     * Consulta os dados do paciente do atendimento que esta sendo impresso a nota
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarPaciente(Connection con, int handle_at){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select a.handle_paciente, p.nome, p.endereco, p.bairro, p.cidade, p.cep, p.uf, p.cpf, p.rg from atendimentos a " +
                "inner join pacientes p on a.handle_paciente = p.pacienteid " +
                "where handle_at=?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar os dados do Paciente. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Consulta os exames do atendimentos
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarExames(Connection con, int handle_at){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select distinct a.atendimento_exame_id, a.valor_correto_paciente, b.nome, b.modalidade, c.cod_exame, d.handle_convenio from atendimento_exames a " +
                    "inner join exames b on a.handle_exame = b.handle_exame " +
                    "inner join atendimentos d on a.handle_at = d.handle_at " +
                    "inner join tabelas c on d.handle_convenio = c.handle_convenio and a.handle_exame = c.handle_exame " +
                    "where a.handle_at=?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Exames do Paciente. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
}
