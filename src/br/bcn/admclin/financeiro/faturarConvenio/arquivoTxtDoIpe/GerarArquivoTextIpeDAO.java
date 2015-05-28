package br.bcn.admclin.financeiro.faturarConvenio.arquivoTxtDoIpe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.bcn.admclin.dao.dbris.Conexao;

public class GerarArquivoTextIpeDAO {
	@SuppressWarnings("finally")
	public static boolean setUpdate(ExameModel model) {
        boolean atualizo = false;
        Connection con = Conexao.fazConexao();
        String sql = "update atendimento_exames set NUMERO_NOTA_IPE=?, NUMERO_REF_NOTA_IPE=?  where ATENDIMENTO_EXAME_ID=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, model.getNn());
            stmt.setString(2, model.getRef());
            stmt.setInt(3, model.getATENDIMENTO_EXAME_ID());
            stmt.executeUpdate();
            stmt.close();
            atualizo = true;
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            //JOptionPane.showMessageDialog(null, "Erro ao atualizar Área de Atendimento. Procure o Administrador.",
            //    "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return atualizo;
        }
    }
}
