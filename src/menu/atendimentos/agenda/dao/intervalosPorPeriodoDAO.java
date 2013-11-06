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
 * TROCA OS ID POR HANDLE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * 
 * 
 * */
public class intervalosPorPeriodoDAO {
    
    
    /**
     * Consulta Todos os intervalos por periodo existentes no Banco de Dados 
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarIntervalosPorPeriodo(Connection con, int agendaId){
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
    
}
