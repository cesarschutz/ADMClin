package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.model.A_intervalosDiarios;
import br.bcn.admclin.model.A_intervalosPorPeriodo;

/**
 *
 * @author BCN
 */
public class A_INTERVALOSPORPERIODO {
    
    public  static boolean conseguiuConsulta;
    
    /**
     * Consulta os dados de um intervalo por periodo
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarAgendasDeUmIntervaloDiario(Connection con, int intervaloPorPeriodoNId){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select distinct a.agendaid, b.nome from a_intervalosporperiodo as a inner join agendas as b on a.agendaid = b.agendaid  where a_intervaloporperiodonid = ?");
        stmtQuery.setInt(1, intervaloPorPeriodoNId);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Intervalo por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Consulta Todos os intervalos por periodo existentes no Banco de Dados  de uma agenda
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarIntervalosPorPeriodoPorAgenda(Connection con, int agendaId){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select A_INTERVALOSPORPERIODO.agendaid, A_INTERVALOSPORPERIODO.A_INTERVALOPORPERIODONID, A_INTERVALOSPORPERIODON.A_INTERVALOPORPERIODONID, A_INTERVALOSPORPERIODON.HORARIOINICIAL, "
                + "A_INTERVALOSPORPERIODON.HORARIOFINAL, A_INTERVALOSPORPERIODON.DIAINICIAL, A_INTERVALOSPORPERIODON.DIAFINAL "
                + "from A_INTERVALOSPORPERIODO inner join A_INTERVALOSPORPERIODON on A_INTERVALOSPORPERIODO.A_INTERVALOPORPERIODONID = A_INTERVALOSPORPERIODON.A_INTERVALOPORPERIODONID where A_INTERVALOSPORPERIODO.agendaid ="+agendaId);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao verificar os Intervalos por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
        
    
    /**
     * Cadastra um novo intervalo por período no Banco de Dados.
     * @param Connection 
     * @param A_intervalosPorPeriodo
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, A_intervalosPorPeriodo model){
        boolean cadastro = false;
        String sql = "insert into a_intervalosporperiodo (a_intervaloporperiodonid, agendaid) values(?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getA_intervaloPorPeriodoNId());
            stmt.setInt(2, model.getAgendaId());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Agenda no Intervalo por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um Intervalo por periodo do Banco De Dados
     * @param Connection
     * @param intervaloDiarioNId 
     * @return boolean
     */
    public static boolean setDeletar(Connection con, int intervaloPorPeriodoNId){
        boolean deleto = false; 
        String sql="delete from a_intervalosporperiodo where a_intervaloporperiodonid=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, intervaloPorPeriodoNId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Intervalo por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
}
