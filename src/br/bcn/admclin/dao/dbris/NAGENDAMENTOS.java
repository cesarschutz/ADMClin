package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Nagendamentos;

public class NAGENDAMENTOS {

    private static Connection con;
    
    @SuppressWarnings("finally")
    public static ArrayList<Nagendamentos> getConsultar(Date dia) {
        ArrayList<Nagendamentos> listaAgendamentos = new ArrayList<Nagendamentos>();
        listaAgendamentos.clear();
        ResultSet resultSet = null;
        con = Conexao.fazConexao();
        try {
            PreparedStatement stmtQuery = con.prepareStatement("SELECT "
                     + "NAGENDAMENTOS.\"NAGENID\" AS NAGENID, "
                     + "NAGENDAMENTOS.\"DIA\" AS DIA,"
                     + "NAGENDAMENTOS.\"NOMEPACIENTE\" AS NOMEPACIENTE, "
                     + "NAGENDAMENTOS.\"TELEFONE\" AS TELEFONE, "
                     + "NAGENDAMENTOS.\"CELULAR\" AS CELULAR, "
                     + "NAGENDAMENTOS.\"HANDLE_CONVENIO\" AS HANDLE_CONVENIO, "
                     + "CONVENIO.\"NOME\" AS NOME_CONVENIO "
                + "FROM "
                + "     \"CONVENIO\" CONVENIO "
                + "INNER JOIN "
                + "    \"NAGENDAMENTOS\" NAGENDAMENTOS ON CONVENIO.\"CONVENIOID\" = NAGENDAMENTOS.\"HANDLE_CONVENIO\" "
                + "WHERE DIA = ? "
                + "ORDER BY NAGENID ");
            stmtQuery.setDate(1, dia);
            resultSet = stmtQuery.executeQuery();
            while (resultSet.next()) {
               Nagendamentos agendamento = new Nagendamentos();
               agendamento.setNAGENID(resultSet.getInt("NAGENID"));
               agendamento.setDIA(resultSet.getDate("DIA"));
               agendamento.setPACIENTE(resultSet.getString("NOMEPACIENTE")); 
               agendamento.setTELEFONE(resultSet.getString("TELEFONE")); 
               agendamento.setCELULAR(resultSet.getString("CELULAR")); 
               agendamento.setHANDLE_CONVENIO(resultSet.getInt("HANDLE_CONVENIO")); 
               agendamento.setNOME_CONVENIO(resultSet.getString("NOME_CONVENIO")); 
               listaAgendamentos.add(agendamento);
            }
            
            
            
            //agora vamos buscar os exames dos atendimentos
            for (Nagendamentos agendamento : listaAgendamentos) {
                PreparedStatement stmt = con.prepareStatement("SELECT "
                                 + "NAGENDAMENTOSEXAMES.\"NAGENEID\" AS NAGENEID, "
                                 + "NAGENDAMENTOSEXAMES.\"NAGDID\" AS NAGDID, "
                                 + "NAGENDAMENTOSEXAMES.\"HORA\" AS HORA, "
                                 + "NAGENDAMENTOSEXAMES.\"NAGENID\" AS NAGENID, "
                                 + "NAGENDAMENTOSEXAMES.\"HANDLE_EXAME\" AS HANDLE_EXAME, "
                                 + "EXAMES.\"NOME\" AS EXAMES_NOME, "
                                 + "AREAS_ATENDIMENTO.\"NOME\" AS AREAS_ATENDIMENTO_NOME "
                                 + "FROM "
                                 + "\"EXAMES\" EXAMES INNER JOIN \"NAGENDAMENTOSEXAMES\" NAGENDAMENTOSEXAMES ON EXAMES.\"EXMID\" = NAGENDAMENTOSEXAMES.\"HANDLE_EXAME\" "
                                 + "INNER JOIN \"AREAS_ATENDIMENTO\" AREAS_ATENDIMENTO ON EXAMES.\"ID_AREAS_ATENDIMENTO\" = AREAS_ATENDIMENTO.\"ID_AREAS_ATENDIMENTO\" "
                                 + "WHERE NAGENID = ?");
                       stmtQuery.setDate(1, dia);
                       resultSet = stmtQuery.executeQuery();
                       while (resultSet.next()) {
                          Nagendamentos agendamento = new Nagendamentos();
                          agendamento.setNAGENID(resultSet.getInt("NAGENID"));
                          agendamento.setDIA(resultSet.getDate("DIA"));
                          agendamento.setPACIENTE(resultSet.getString("NOMEPACIENTE")); 
                          agendamento.setTELEFONE(resultSet.getString("TELEFONE")); 
                          agendamento.setCELULAR(resultSet.getString("CELULAR")); 
                          agendamento.setHANDLE_CONVENIO(resultSet.getInt("HANDLE_CONVENIO")); 
                          agendamento.setNOME_CONVENIO(resultSet.getString("NOME_CONVENIO")); 
                          listaAgendamentos.add(agendamento);
                       }
            }
            
            
            
            
            
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            listaAgendamentos.clear();
            JOptionPane.showMessageDialog(null, "Erro ao consultar Agendamentos. Procure o Administrador." + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return listaAgendamentos;
        }
    }
    
}
