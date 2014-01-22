package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Tb_ClassesDeExames;

/**
 * Classe DAO da classe JIFCClassesDeExames
 * 
 * @author BCN
 */
public class TB_CLASSESDEEXAMES {
    public static boolean conseguiuConsulta;

    /**
     * Verifica se Classe De Exame já existe antes de fazer um update no Banco de Dados.
     * 
     * @param Connection
     * @param ClasseDeExameMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaAtualizarRegistro(Connection con, Tb_ClassesDeExames model) {
        boolean existe = true;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from tb_classesexames where (nome=?) and (cod!=?)");
            stmtQuery.setString(1, model.getDescricao());
            stmtQuery.setInt(2, model.getCod());
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar se Classe de Exame já existe. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Verifica se Classe De Exame já existe antes de cadastra-lo no Banco de Dados.
     * 
     * @param Connection
     * @param ClasseDeExameMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, Tb_ClassesDeExames model) {
        boolean existe = true;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from tb_classesexames where nome=?");
            stmtQuery.setString(1, model.getDescricao());
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar se Classe de Exame já existe. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Consulta Todas as Clases De Exames existentes no Banco de Dados.
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultar(Connection con) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from tb_classesexames order by nome");
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Classes De Exames. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta todas as Modalidades existentes no Banco de Dados.
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarModalidades(Connection con) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from modalidades");
            resultSet = stmtQuery.executeQuery();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Modalidades. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Cadastra uma nova Classe De Exame no Banco de Dados.
     * 
     * @param Connection
     * @param ClasseDeExameModel
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrar(Connection con, Tb_ClassesDeExames model) {
        boolean cadastro = false;
        String sql = "insert into TB_CLASSESEXAMES (nome,usuarioid,dat,modidx) values(?,?,?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, model.getDescricao());
            stmt.setInt(2, model.getUsuarioid());
            stmt.setDate(3, model.getData());
            stmt.setInt(4, model.getModIdx());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Classe de Exame. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * Atualiza uma Classe De Exame no Banco De Dados.
     * 
     * @param Connection
     * @param ClasseDeExameMODEL
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdate(Connection con, Tb_ClassesDeExames model) {
        boolean atualizo = false;
        String sql = "update tb_classesexames set nome=?, usuarioid=?, dat=?, modidx=? where cod=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, model.getDescricao());
            stmt.setInt(2, model.getUsuarioid());
            stmt.setDate(3, model.getData());
            stmt.setInt(4, model.getModIdx());
            stmt.setInt(5, model.getCod());
            stmt.executeUpdate();
            stmt.close();
            atualizo = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Classe de Exame. Procure o Administrador." + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return atualizo;
        }
    }

    /**
     * Deleta uma Classe De Exame no Banco de Dados.
     * 
     * @param Connection
     * @param ClasseDeExameMODEL
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletar(Connection con, Tb_ClassesDeExames model) {
        boolean deleto = false;
        String sql = "delete from tb_classesexames where cod=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getCod());
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Classe de Exame. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return deleto;
        }
    }

    /**
     * Verifica se Classe De Exame está sendo utilizada por algum Exame, antes de apagar a Classe De Exame no Banco de
     * Dados.
     * 
     * @param Connection
     * @param String
     *            codigo
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarSeClasseEstaSendoUtilizada(Connection con, int codTabela) {
        boolean utilizada = true;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from exames where HANDLE_CLASSEDEEXAME=" + codTabela);
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                utilizada = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null,
                "Erro ao verficar se Classe De Exame está sendo utilizada por algum Exame.\nProcure o Administrador",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return utilizada;
        }
    }
}
