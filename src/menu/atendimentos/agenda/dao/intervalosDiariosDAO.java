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
 * @author CeSaR
 *
 * TROCA OS ID POR HANDLE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * 
 * 
 * */
public class intervalosDiariosDAO {
    
    /**
     * Consulta Todos os intervalos diarios existentes no Banco de Dados 
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarIntervalosDiarios(Connection con, int handle_agenda){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select A_INTERVALOSDIARIOS.handle_agenda, A_INTERVALOSDIARIOS.A_INTERVALODIARIONID, A_INTERVALOSDIARIOSN.A_INTERVALODIARIONID, A_INTERVALOSDIARIOSN.HORARIOINICIAL, "
                
                + "A_INTERVALOSDIARIOSN.HORARIOFINAL, A_INTERVALOSDIARIOSN.DIADOINTERVALO "
                
                + "from A_INTERVALOSDIARIOS inner join A_INTERVALOSDIARIOSN on A_INTERVALOSDIARIOS.A_INTERVALODIARIONID = A_INTERVALOSDIARIOSN.A_INTERVALODIARIONID where A_INTERVALOSDIARIOS.handle_agenda ="+handle_agenda);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao verificar os Intervalos por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
}
