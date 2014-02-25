/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.valoresrecebidos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.Conexao;

/**
 * 
 * @author Cesar Schutz
 */
public class relatorioValoresRecebidosDAO {

    private static ArrayList<relatorioValoresRecebidossMODEL> listaDosAtendimentos;

    /**
     * Consulta Todos os atendimentos existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     * @throws SQLException
     */
    @SuppressWarnings("finally")
    public static ArrayList<relatorioValoresRecebidossMODEL> getConsultarAtendimentosTodosOsConvenios(Date diaInicial,
        Date diaFinal, int handle_convenio) throws SQLException {
        ResultSet resultSet = null;
        listaDosAtendimentos = new ArrayList<relatorioValoresRecebidossMODEL>();
        Connection con = Conexao.fazConexao();

        String sql =
            "SELECT ATENDIMENTOS.\"HANDLE_AT\" AS ATENDIMENTOS_HANDLE_AT, ATENDIMENTOS.\"DATA_ATENDIMENTO\" AS ATENDIMENTOS_DATA_ATENDIMENTO, PACIENTES.\"NOME\" AS PACIENTES_NOME, EXAMES.\"NOME\" AS EXAMES_NOME, "
                + "ATENDIMENTO_EXAMES.\"VALOR_CORRETO_PACIENTE\" AS ATENDIMENTO_EXAMES_VALOR_PACIENTE, ATENDIMENTO_EXAMES.\"VALOR_CORRETO_CONVENIO\" AS ATENDIMENTO_EXAMES_VALOR_CORRETO_CONVENIO, "
                + "ATENDIMENTO_EXAMES.\"VALOR_RECEBIDO_CONVENIO\" AS ATENDIMENTO_EXAMES_VALOR_RECEBI, "
                + "CONVENIO.\"NOME\" AS CONVENIO_NOME, "
                + "CONVENIO.\"HANDLE_CONVENIO\" AS CONVENIO_HANDLE_CONVENIO "
                + "FROM \"PACIENTES\" PACIENTES "
                + "INNER JOIN \"ATENDIMENTOS\" ATENDIMENTOS ON PACIENTES.\"PACIENTEID\" = ATENDIMENTOS.\"HANDLE_PACIENTE\" "
                + "INNER JOIN \"ATENDIMENTO_EXAMES\" ATENDIMENTO_EXAMES ON ATENDIMENTOS.\"HANDLE_AT\" = ATENDIMENTO_EXAMES.\"HANDLE_AT\" "
                + "INNER JOIN \"CONVENIO\" CONVENIO ON ATENDIMENTOS.\"HANDLE_CONVENIO\" = CONVENIO.\"CONVENIOID\" "
                + "INNER JOIN \"EXAMES\" EXAMES ON ATENDIMENTO_EXAMES.\"HANDLE_EXAME\" = EXAMES.\"EXMID\" "
                + "where (ATENDIMENTOS.\"DATA_ATENDIMENTO\" > ? or ATENDIMENTOS.\"DATA_ATENDIMENTO\" = ?)  and  (ATENDIMENTOS.\"DATA_ATENDIMENTO\" < ? or ATENDIMENTOS.\"DATA_ATENDIMENTO\" = ?) and  CONVENIO.\"HANDLE_CONVENIO\" = ? "
                + "order by ATENDIMENTOS.\"HANDLE_AT\" ";

        PreparedStatement stmtQuery = con.prepareStatement(sql);
        stmtQuery.setDate(1, diaInicial);
        stmtQuery.setDate(2, diaInicial);
        stmtQuery.setDate(3, diaFinal);
        stmtQuery.setDate(4, diaFinal);
        stmtQuery.setInt(5, handle_convenio);
        resultSet = stmtQuery.executeQuery();

        while (resultSet.next()) {
            relatorioValoresRecebidossMODEL exameRealizado = new relatorioValoresRecebidossMODEL();
            exameRealizado.setData(MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getDate(
                "ATENDIMENTOS_DATA_ATENDIMENTO").toString()));
            exameRealizado.setCodigo(resultSet.getInt("ATENDIMENTOS_HANDLE_AT"));
            exameRealizado.setPaciente(resultSet.getString("PACIENTES_NOME"));
            exameRealizado.setExame(resultSet.getString("EXAMES_NOME"));
            exameRealizado.setValorPaciente(MetodosUteis.colocarZeroEmCampoReais(resultSet
                .getDouble("ATENDIMENTO_EXAMES_VALOR_PACIENTE")));
            exameRealizado.setValorFaturado(MetodosUteis.colocarZeroEmCampoReais(resultSet
                .getDouble("ATENDIMENTO_EXAMES_VALOR_CORRETO_CONVENIO")));
            exameRealizado.setValorPagoConvenio(MetodosUteis.colocarZeroEmCampoReais(resultSet
                .getDouble("ATENDIMENTO_EXAMES_VALOR_RECEBI")));
            exameRealizado.setConvenio(resultSet.getString("CONVENIO_NOME"));
            listaDosAtendimentos.add(exameRealizado);
        }
        resultSet.close();
        Conexao.fechaConexao(con);
        return listaDosAtendimentos;
    }
}
