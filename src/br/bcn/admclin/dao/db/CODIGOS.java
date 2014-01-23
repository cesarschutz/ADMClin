package br.bcn.admclin.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.dbris.Conexao;

public class CODIGOS {

    /**
     * Salva o laudo em um atendimento
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrarCodigo(String codigo, String texto) {
        boolean cadastro = false;
        Connection con = Conexao.fazConexaoPAC();
        String sql = "update or insert into codigos (codigo, codtxt) values(?,?) matching (codigo)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,codigo);
            stmt.setString(2, texto);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Código. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
            Conexao.fechaConexao(con);
        } finally {
            return cadastro;
        }
    }
    
    public static String getConsultarLaudo(String codigo) {
        Connection con = Conexao.fazConexaoPAC();
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select codtxt from codigos where codigo = ?");
            stmtQuery.setString(1, codigo);
            resultSet = stmtQuery.executeQuery();
            String texto = "vz";
            while (resultSet.next()){
                texto = resultSet.getString("codtxt");
            }
            Conexao.fechaConexao(con);
            return texto;
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            return "err";
        }
    }
    
}
