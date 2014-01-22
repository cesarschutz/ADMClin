package br.bcn.admclin.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.dbris.Conexao;

public class JLAUDOS {

    /**
     * Consulta o laudo de um atendimento
     * @return retorna ERRO se der erro e VAZIO se nao tiver nada
     */
    public static String getConsultarLaudo(int handle_at) {
        Connection con = Conexao.fazConexaoPAC();
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select laudo from jlaudos where handle_at = ?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
            String laudo = "vazio";
            while (resultSet.next()){
                laudo = resultSet.getString("laudo");
            }
            Conexao.fechaConexao(con);
            return laudo;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Laudo. Procure o Administrador." + e, "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
            Conexao.fechaConexao(con);
            return "erro";
        }
    }
    
}
