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
public class examesDAO {
    /**
     * Consulta Todos os Exames existentes no Banco de Dados do convenio selecionado
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con, int HANDLE_CONVENIO, String modalidade){
        
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select distinct a.nome, a.duracao, a.handle_exame, b.cofch1, b.cofch2, b.coeffilme, b.vai_materiais_por_padrao from exames a inner join tabelas b on a.handle_exame = b.handle_exame where b.handle_convenio = ? and modalidade = ?");
        stmtQuery.setInt(1, HANDLE_CONVENIO);
        stmtQuery.setString(2, modalidade);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Exames deste Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
}
