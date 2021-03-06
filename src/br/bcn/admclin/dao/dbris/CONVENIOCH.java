/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.model.ConvenioCh;

/**
 * 
 * @author Cesar Schutz
 */
public class CONVENIOCH {
    public static boolean conseguiuConsulta;

    /**
     * Consulta Todas as Especialidades Medicas existentes no Banco de Dados.
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultar(Connection con, int handle_convenio) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from convenioch where handle_convenio=? order by dataavaler Desc");
            stmtQuery.setInt(1, handle_convenio);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar Valores de CH's do Convênio. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * consulta se data digitada eh maior que a ultima data cadastrada naquele CH
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarSeDataEhMenorQueAultimaCadastrada(Connection con, int handle_convenio,
        String dataAcadastrar) {
        boolean ok = false;
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from convenioch where handle_convenio=? order by dataavaler");
            stmtQuery.setInt(1, handle_convenio);
            resultSet = stmtQuery.executeQuery();
            while (resultSet.next()) {
                // verificando data
                String dataBanco = MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("dataavaler"));
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date dataDigitada = format.parse(dataAcadastrar);
                java.util.Date dataBancoCorreta = format.parse(dataBanco);

                int x = dataDigitada.compareTo(dataBancoCorreta);

                if (x > 0) {
                    ok = true;
                } else {
                    ok = false;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao verificar Datas para Cadastrar Novo Valor. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            ok = false;
        } finally {
            if (!ok) {
                JOptionPane.showMessageDialog(null,
                    "Data menor que a última cadastrada. Verifique a data e tente novamente.", "ATENÇÃO",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
            return ok;
        }
    }

    /**
     * Cadastra um novo valor de ch de acordo com o seu ID.
     * 
     * @param con
     * @param convenioCH
     * @return se cadastrou ou nao
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrar(Connection con, ConvenioCh convenioCHMODEL) {
        boolean cadastro = false;
        String sqlInsrtValor =
            "insert into convenioCH (usuarioid,dat,handle_convenio,valor, dataavaler) values(?,?,?,?,?)";
        try {
            // inserindo valor no material
            PreparedStatement stmtInsertValor = con.prepareStatement(sqlInsrtValor);
            stmtInsertValor.setInt(1, convenioCHMODEL.getUsuarioId());
            stmtInsertValor.setDate(2, convenioCHMODEL.getDat());
            stmtInsertValor.setInt(3, convenioCHMODEL.getHandle_convenio());
            stmtInsertValor.setDouble(4, convenioCHMODEL.getValor());
            stmtInsertValor.setDate(5, convenioCHMODEL.getDataAValer());
            stmtInsertValor.executeUpdate();
            stmtInsertValor.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Valor de CH. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * Deleta Valores de CH de um convenio do Banco De Dados
     * 
     * @param Connection
     * @param ExameMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletar(Connection con, int handle_convenio) {
        boolean deleto = false;
        String sql = "delete from convenioch where handle_convenio=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, handle_convenio);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Valores de CH. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return deleto;
        }
    }

    /**
     * Deleta um Valor de CH do Banco De Dados
     * 
     * @param Connection
     * @param ExameMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletarUmValor(Connection con, int convenioChId) {
        boolean deleto = false;
        String sql = "delete from convenioch where convenioChId=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, convenioChId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Valor de CH. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return deleto;
        }
    }
}
