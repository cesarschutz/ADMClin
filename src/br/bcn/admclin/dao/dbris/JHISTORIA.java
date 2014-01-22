package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class JHISTORIA {
    
    /**
     * Consulta a historia clinica de um atendimento
     * @return retorna ERRO se der erro e VAZIO se nao tiver nada
     */
    public static String getConsultar(int handle_at) {
        Connection con = Conexao.fazConexao();
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select historia from jhistoria where handle_at = ?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
            String historia = "vazio";
            while (resultSet.next()){
                historia = resultSet.getString("HISTORIA");
            }
            Conexao.fechaConexao(con);
            return historia;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar História Clinica. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            Conexao.fechaConexao(con);
            return "erro";
        }
    }
    
    /**
     * Salva a historia clinica de um atendimento
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrar(int handle_at, String historia) {
        boolean cadastro = false;
        Connection con = Conexao.fazConexao();
        String sql = "update or insert into jhistoria (handle_at, historia) values(?,?) matching (handle_at)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1,handle_at);
            stmt.setString(2, historia);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar História Clinica. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
            Conexao.fechaConexao(con);
        } finally {
            return cadastro;
        }
    }
}
