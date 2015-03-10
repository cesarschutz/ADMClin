/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.demed;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 * 
 * @author Cesar Schutz
 */
public class GerarDmedDAO {

    /**
     * Consulta as informações necessarias para o DMED
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDmed(Connection con, Date diaInicial, Date diaFinal) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select e.handle_at, a.data_atendimento, e.valor_correto_paciente, p.nome, p.cpf, p.responsavel, p.cpfresponsavel from atendimento_exames e "
                    + "inner join atendimentos a on e.handle_at = a.handle_at "
                    + "inner join pacientes p on a.handle_paciente = p.pacienteid "
                    + "where(a.data_atendimento > ? or a.data_atendimento = ?)  and  (a.data_atendimento < ? or a.data_atendimento = ?) and e.valor_correto_paciente > 0 and a.paciente_pagou = 1"
                    + "order by a.data_atendimento, p.nome, e.handle_at asc");
            stmtQuery.setDate(1, diaInicial);
            stmtQuery.setDate(2, diaInicial);
            stmtQuery.setDate(3, diaFinal);
            stmtQuery.setDate(4, diaFinal);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar os Atendimentos Para gerar DMED. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }
}
