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
            JOptionPane.showMessageDialog(null, "Erro ao consultar Laudo. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            Conexao.fechaConexao(con);
            return "erro";
        }
    }
    
    /**
     * Salva o laudo em um atendimento
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrarLaudo(int handle_at, String laudo, String dataExame, String usr) {
        boolean cadastro = false;
        Connection con = Conexao.fazConexaoPAC();
        String sql = "update or insert into jlaudos (handle_at, laudo, flagsign, flagrisupdate, DATESIGN, usr) values(?,?,?,?,?,?) matching (handle_at)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1,handle_at);
            stmt.setString(2, laudo);
            stmt.setInt(3, 0);
            stmt.setInt(4, 0);
            stmt.setString(5, dataExame);
            stmt.setString(6, usr);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Laudo. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
            Conexao.fechaConexao(con);
        } finally {
            return cadastro;
        }
    }
    
}
