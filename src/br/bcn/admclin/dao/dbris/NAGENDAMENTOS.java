package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Atendimentos;
import br.bcn.admclin.dao.model.Nagendamentos;
import br.bcn.admclin.dao.model.NagendamentosExames;

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
                                 + "EXAMES.\"DURACAO\" AS EXAMES_DURACAO, "
                                 + "NAGENDASDESC.\"NAME\" AS nomeAgenda, "
                                 + "AREAS_ATENDIMENTO.\"ID_AREAS_ATENDIMENTO\" AS AREAS_ATENDIMENTO_ID, "
                                 + "AREAS_ATENDIMENTO.\"NOME\" AS AREAS_ATENDIMENTO_NOME "
                                 + "FROM "
                                 + "\"EXAMES\" EXAMES INNER JOIN \"NAGENDAMENTOSEXAMES\" NAGENDAMENTOSEXAMES ON EXAMES.\"EXMID\" = NAGENDAMENTOSEXAMES.\"HANDLE_EXAME\" "
                                 + "INNER JOIN \"AREAS_ATENDIMENTO\" AREAS_ATENDIMENTO ON EXAMES.\"ID_AREAS_ATENDIMENTO\" = AREAS_ATENDIMENTO.\"ID_AREAS_ATENDIMENTO\" "
                                 + "INNER JOIN \"NAGENDASDESC\" NAGENDASDESC ON NAGENDAMENTOSEXAMES.\"NAGDID\" = NAGENDASDESC.\"NAGDID\" "
                                 + "WHERE NAGENID = ? "
                                 + "ORDER BY AREAS_ATENDIMENTO_ID, HORA");
                       stmt.setInt(1, agendamento.getNAGENID());
                       ResultSet resultSet2 = stmt.executeQuery();
                       while (resultSet2.next()) {
                          NagendamentosExames exame = new NagendamentosExames();
                          exame.setNAGENEID(resultSet2.getInt("NAGENEID"));
                          exame.setNAGDID(resultSet2.getInt("NAGDID"));
                          exame.setHORA(resultSet2.getInt("hora"));
                          exame.setDURACAO(resultSet2.getInt("EXAMES_DURACAO"));
                          exame.setNAGENID(resultSet2.getInt("NAGENID"));
                          exame.setHANDLE_EXAME(resultSet2.getInt("HANDLE_EXAME"));
                          exame.setNomeExame(resultSet2.getString("EXAMES_NOME"));
                          exame.setID_AREAS_ATENDIMENTO(resultSet2.getInt("AREAS_ATENDIMENTO_ID"));
                          exame.setNomeAreaAtendimento(resultSet2.getString("AREAS_ATENDIMENTO_NOME"));
                          exame.setNomeAgenda(resultSet2.getString("nomeAgenda"));
                          agendamento.getListaExames().add(exame);
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
    
    /*
     * metodo que coloca o flag como virou atendimento e atualiza o nome do paciente
     */
    public static boolean atualizarAgendamentoAposVirarAtendimento(Connection con, Nagendamentos agendamento, String nomePaciente) {
        boolean cadastro = false;
        String sql =
            "update NAGENDAMENTOS set virou_atendimento = ?, nomePaciente = ? where NAGENID=?";
        try {

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, 1);
            stmt.setString(2, nomePaciente);
            stmt.setInt(3, agendamento.getNAGENID());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro" + e, "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }
}
