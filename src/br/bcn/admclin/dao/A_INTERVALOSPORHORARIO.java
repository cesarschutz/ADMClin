package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.model.A_intervalosPorHorario;

/**
 *
 * @author BCN
 */
public class A_INTERVALOSPORHORARIO {
    
    public  static boolean conseguiuConsulta;
    
    /**
     * Consulta os dados de umintervalo por horario
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarAgendasDeUmIntervaloPorHorario(Connection con, int intervaloPorHorarioId){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select distinct a.agendaid, b.nome from a_intervalosporhorario as a inner join agendas as b on a.agendaid = b.agendaid  where a_intervalosporhorarionid = ?");
        stmtQuery.setInt(1, intervaloPorHorarioId);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Intervalo por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Consulta Todos os intervalos por horario existentes no Banco de Dados por agenda
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarIntervalosPorHorarioPorAgenda(Connection con, int agendaId){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select A_INTERVALOSPORHORARIO.agendaid, A_INTERVALOSPORHORARIO.A_INTERVALOSPORHORARIONID, A_INTERVALOSPORHORARION.A_INTERVALOPORHORARIONID, A_INTERVALOSPORHORARION.HORARIOINICIAL, "
                + "A_INTERVALOSPORHORARION.HORARIOFINAL, A_INTERVALOSPORHORARION.SEG, A_INTERVALOSPORHORARION.TER, A_INTERVALOSPORHORARION.QUA, A_INTERVALOSPORHORARION.QUI, A_INTERVALOSPORHORARION.SEX, A_INTERVALOSPORHORARION.SAB, A_INTERVALOSPORHORARION.DOM "
                + "from A_INTERVALOSPORHORARIO inner join A_INTERVALOSPORHORARION on A_INTERVALOSPORHORARIO.A_INTERVALOSPORHORARIONID = A_INTERVALOSPORHORARION.A_INTERVALOPORHORARIONID where A_INTERVALOSPORHORARIO.agendaid ="+agendaId);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao verificar os Intervalos por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Cadastra umnovo intervalo por horario no Banco de Dados.
     * @param Connection 
     * @param A_intervalosPorHorario
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, A_intervalosPorHorario model){
        boolean cadastro = false;
        String sql = "insert into a_intervalosPorHorario (a_intervalosporhorarionid, agendaid) values(?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getA_intervaloPorHorarioNId());
            stmt.setInt(2, model.getAgendaId());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Agenda no Intervalo por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um Intervalo por Horario do Banco De Dados
     * @param Connection
     * @param intervaloPorHorarioId 
     * @return boolean
     */
    public static boolean setDeletar(Connection con, int intervaloPorHorarioNId){
        boolean deleto = false; 
        String sql="delete from A_INTERVALOSPORHORARIO where A_INTERVALOSPORHORARIONID=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, intervaloPorHorarioNId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Intervalo por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
}
