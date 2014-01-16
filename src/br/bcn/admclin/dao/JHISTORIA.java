package br.bcn.admclin.dao;

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
            PreparedStatement stmtQuery = con.prepareStatement("select historia from jhistoriaa where handle_at = ?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
            System.out.println("vai tentar");
            String historia = "vazio";
            while (resultSet.next()){
                System.out.println("entrou no while");
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
}
