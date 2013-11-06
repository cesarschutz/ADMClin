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
public class intervalosPorHorarioDAO {
    
    
    /**
     * Consulta Todos os intervalos por horario existentes no Banco de Dados 
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarIntervalosPorHorario(Connection con, int agendaId){
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
    
}
