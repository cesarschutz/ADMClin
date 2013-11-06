package menu.cadastros.agenda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import menu.cadastros.agenda.model.a_intervalosPorHorarioMODEL;

/**
 *
 * @author BCN
 */
public class a_intervalosPorHorarioDAO {
    
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
     * Cadastra umnovo intervalo por horario no Banco de Dados.
     * @param Connection 
     * @param a_intervalosPorHorarioMODEL
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, a_intervalosPorHorarioMODEL model){
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
