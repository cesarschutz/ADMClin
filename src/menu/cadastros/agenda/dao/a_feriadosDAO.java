package menu.cadastros.agenda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import menu.cadastros.agenda.model.a_feriadosMODEL;

/**
 *
 * @author BCN
 */
public class a_feriadosDAO {
    
    public  static boolean conseguiuConsulta;
    
    /**
     * Consulta os dados de um feriado
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarAgendasDeUmFeriado(Connection con, int feriadoNId){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select distinct a.handle_agenda, b.nome from a_feriados as a inner join agendas as b on a.handle_agenda = b.handle_agenda  where handle_feriadon = ?");
        stmtQuery.setInt(1, feriadoNId);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Feriado. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
        
    
    /**
     * Cadastra um novo feriados no Banco de Dados.
     * @param Connection 
     * @param a_feriadosMODEL
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, a_feriadosMODEL model){
        boolean cadastro = false;
        String sql = "insert into a_feriados (handle_feriadon, handle_agenda) values(?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getHandleFeriadoN());
            stmt.setInt(2, model.getHandleAgenda());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Agenda no Feriado. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um feriado do Banco De Dados
     * @param Connection
     * @param feriadoNId 
     * @return boolean
     */
    public static boolean setDeletar(Connection con, int feriadoNId){
        boolean deleto = false; 
        String sql="delete from a_feriados where handle_feriadon=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, feriadoNId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Feriado. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
}
