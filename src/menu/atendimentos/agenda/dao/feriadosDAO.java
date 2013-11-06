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

/**
 *
 * @author BCN
 */
public class feriadosDAO {
    /**
     * Consulta Todos os feriados existentes no Banco de Dados 
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarFeriados(Connection con, int handle_agenda){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select a_feriados.handle_agenda, a_feriados.handle_feriadon, a_feriadosn.handle_feriadon, a_feriadosn.diadoferiado "
                + "from a_feriados inner join a_feriadosn on a_feriados.handle_feriadon = a_feriadosn.handle_feriadon where a_feriados.handle_agenda ="+handle_agenda);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao verificar os Feriados. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
}
