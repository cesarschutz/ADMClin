/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package impressoes.modelo2.etiquetaEnvelope;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author BCN
 */
public class ImprimirEtiquetaEnvelopeModelo2DAO {
    public static ResultSet getConsultarDadosEtiqueta(Connection con, int handle_at){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select data_atendimento, modalidade, p.nome from atendimentos a " +
                                                                    "inner join pacientes p on a.handle_paciente = p.handle_paciente " +
                                                                    "where a.handle_at = ?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar informações para a Etiqueta. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
}
