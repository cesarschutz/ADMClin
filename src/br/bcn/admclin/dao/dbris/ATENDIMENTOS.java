/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.dao.dbris;

import java.sql.*;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Atendimentos;

/**
 * 
 * @author Cesar Schutz
 */
public class ATENDIMENTOS {

    // variavel utilizada para quando SALVAR veriricar o handle_at que foi feito naquele agendamento
    public static int handle_aTDoAgendamentoCadastrado;

    // recuperando o handle_at que sera utilizado para salvar (isso quando eh feito uma reserva em algum horario)
    public static int getHandleAP(Connection con) throws SQLException {
        ResultSet resultSet = null;

        String sql = "select gen_id(GEN_ATENDIMENTOS_HANDLE_AT,0) from rdb$database";
        PreparedStatement stmt = con.prepareStatement(sql);
        resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            handle_aTDoAgendamentoCadastrado = resultSet.getInt("gen_id");
        }
        return handle_aTDoAgendamentoCadastrado;
    }

    // metodo para somar 1 no handle_at
    public static void setSomarHandleAP(Connection con) throws SQLException {
        ResultSet resultSet = null;
        String sql = "select gen_id(GEN_ATENDIMENTOS_HANDLE_AT,1) from rdb$database";

        PreparedStatement stmt = con.prepareStatement(sql);
        resultSet = stmt.executeQuery();
        while (resultSet.next()) {

        }

    }

    /**
     * Consulta Todos os atendimentos desta agenda existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentosAgenda(Connection con, int handle_agenda, Date dia) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select p.nome, a.handle_at, a.hora_atendimento, a.duracao_atendimento from atendimentos A inner join pacientes p on a.handle_paciente = p.handle_paciente where handle_agenda =? and data_atendimento=? order by hora_atendimento");
            stmtQuery.setInt(1, handle_agenda);
            stmtQuery.setDate(2, dia);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar os Atendimentos. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta Todos os atendimentos desta agenda existentes no Banco de Dados que sejam reservas (o atendente ainda
     * esta cadastrando o atendimento, esta com a janela atendimento daquele horario aberta)
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentosAgendaReservados(Connection con, int handle_agenda, Date dia) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from atendimentos where handle_paciente=? and handle_agenda=? and data_atendimento=?");
            stmtQuery.setInt(1, 0);
            stmtQuery.setInt(2, handle_agenda);
            stmtQuery.setDate(3, dia);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao verificar os Atendimentos Reservados. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta Todos os atendimentos desta agenda existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentos(Connection con, Date dia) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select p.nome as nomePaciente, m.nome as nomeMedico, m.crm as crmMedico, a.handle_at, a.data_atendimento, a.hora_atendimento, a.status1, a.status2, a.EXAME_ENTREGUE_AO_PACIENTE, a.LAUDO_ENTREGUE_AO_PACIENTE from atendimentos A "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoId "
                    + "inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "where data_atendimento=? order by hora_atendimento");
            stmtQuery.setDate(1, dia);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta Todos os dados de um atendimento agenda existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDadosDeUmAtendimento(Connection con, int handle_at) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select P.nome as nomepac, p.handle_paciente as handle_paciente, p.peso as peso, p.cidade as cidade, p.endereco as endereco_paciente, p.altura as altura, p.nascimento as nascimento_paciente, "
                		+ "p.sexo as sexo_paciente, p.telefone as telefone_paciente, p.celular as celular_paciente, p.cpf as cpf_paciente, M.nome as nomemed, m.crm as crmMedico, c.nome as nomeconv,A.data_atendimento,a.hora_atendimento, "
                		+ "a.duracao_atendimento, a.handle_paciente, a.handle_medico_sol, a.observacao,a.matricula_convenio, a.complemento, a.handle_convenio, a.flag_imprimiu, a.data_exame_pronto, a.hora_exame_pronto, a.observacao, "
                		+ "a.hora_exame_pronto, a.EXAME_ENTREGUE_AO_PACIENTE, a.LAUDO_ENTREGUE_AO_PACIENTE, a.FLAG_LAUDO "
                    + "from atendimentos as A "
                    + "inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "inner join medicos M on a.handle_medico_sol = m.medicoid "
                    + "inner join convenio c on c.handle_convenio = a.handle_convenio " + "where a.handle_at = ?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Dados do Atendimento. Procure o Administrador." + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Salva um novo atendimento no Banco De Dados.
     * 
     * @param Connection
     * @param Atendimentos
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrar(Connection con, Atendimentos atendimento) {
        boolean cadastro = false;
        String sql =
            "insert into atendimentos (data_atendimento,dat,data_exame_pronto,"
                + "handle_at, handle_paciente, handle_medico_sol, handle_convenio, hora_atendimento, duracao_atendimento,usuarioid,"
                + "observacao, matricula_convenio, COMPLEMENTO, hora_exame_pronto,flag_imprimiu, flag_laudo, flag_faturado) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, atendimento.getDATA_ATENDIMENTO());
            stmt.setDate(2, atendimento.getDAT());
            stmt.setDate(3, atendimento.getDATA_EXAME_PRONTO());

            stmt.setInt(4, atendimento.getHANDLE_AT());
            stmt.setInt(5, atendimento.getHANDLE_PACIENTE());
            stmt.setInt(6, atendimento.getHANDLE_MEDICO_SOL());
            stmt.setInt(7, atendimento.getHANDLE_CONVENIO());
            stmt.setInt(8, atendimento.getHORA_ATENDIMENTO());
            stmt.setInt(9, atendimento.getDURACAO_ATENDIMENTO());
            stmt.setInt(10, atendimento.getUSUARIOID());

            stmt.setString(11, atendimento.getOBSERVACAO());
            stmt.setString(12, atendimento.getMATRICULA_CONVENIO());
            stmt.setString(13, atendimento.getCOMPLEMENTO());
            stmt.setInt(14, atendimento.getHORA_EXAME_PRONTO());
            stmt.setString(15, atendimento.getFLAG_IMPRIMIU());
            stmt.setInt(16, 0);
            stmt.setInt(17, 0);

            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao registrar Atendimento. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * edita um atendimento no Banco De Dados.
     * 
     * @param Connection
     * @param Atendimentos
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdate(Connection con, Atendimentos atendimento) {
        boolean cadastro = false;
        String sql =
            "update atendimentos set data_atendimento=?, dat=?, data_exame_pronto=?, "
                + "handle_paciente=?, handle_medico_sol=?, handle_convenio=?, hora_atendimento=?, duracao_atendimento=?, usuarioid=?, "
                + "observacao=?, matricula_convenio=?, COMPLEMENTO=?, hora_exame_pronto=?, ID_AREAS_ATENDIMENTO = ?, nagenid = ? where handle_at=?";
        try {

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, atendimento.getDATA_ATENDIMENTO());
            stmt.setDate(2, atendimento.getDAT());
            stmt.setDate(3, atendimento.getDATA_EXAME_PRONTO());

            stmt.setInt(4, atendimento.getHANDLE_PACIENTE());
            stmt.setInt(5, atendimento.getHANDLE_MEDICO_SOL());
            stmt.setInt(6, atendimento.getHANDLE_CONVENIO());
            stmt.setInt(7, atendimento.getHORA_ATENDIMENTO());
            stmt.setInt(8, atendimento.getDURACAO_ATENDIMENTO());
            stmt.setInt(9, atendimento.getUSUARIOID());

            stmt.setString(10, atendimento.getOBSERVACAO());
            stmt.setString(11, atendimento.getMATRICULA_CONVENIO());
            stmt.setString(12, atendimento.getCOMPLEMENTO());
            stmt.setInt(13, atendimento.getHORA_EXAME_PRONTO());
            stmt.setInt(14, atendimento.getID_AREAS_ATENDIMENTO());
            stmt.setInt(15, atendimento.getId_agendamento());
            
            stmt.setInt(16, atendimento.getHANDLE_AT());

            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao registrar Atendimento. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }
    
    @SuppressWarnings("finally")
    public static boolean setUpdateQuandoForAtualizar(Connection con, Atendimentos atendimento) {
        boolean cadastro = false;
        String sql =
            "update atendimentos set data_atendimento=?, dat=?, data_exame_pronto=?, "
                + "handle_paciente=?, handle_medico_sol=?, handle_convenio=?, hora_atendimento=?, duracao_atendimento=?, usuarioid=?, "
                + "observacao=?, matricula_convenio=?, COMPLEMENTO=?, hora_exame_pronto=?, ID_AREAS_ATENDIMENTO = ? where handle_at=?";
        try {

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, atendimento.getDATA_ATENDIMENTO());
            stmt.setDate(2, atendimento.getDAT());
            stmt.setDate(3, atendimento.getDATA_EXAME_PRONTO());

            stmt.setInt(4, atendimento.getHANDLE_PACIENTE());
            stmt.setInt(5, atendimento.getHANDLE_MEDICO_SOL());
            stmt.setInt(6, atendimento.getHANDLE_CONVENIO());
            stmt.setInt(7, atendimento.getHORA_ATENDIMENTO());
            stmt.setInt(8, atendimento.getDURACAO_ATENDIMENTO());
            stmt.setInt(9, atendimento.getUSUARIOID());

            stmt.setString(10, atendimento.getOBSERVACAO());
            stmt.setString(11, atendimento.getMATRICULA_CONVENIO());
            stmt.setString(12, atendimento.getCOMPLEMENTO());
            stmt.setInt(13, atendimento.getHORA_EXAME_PRONTO());
            stmt.setInt(14, atendimento.getID_AREAS_ATENDIMENTO());
            
            stmt.setInt(15, atendimento.getHANDLE_AT());

            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao registrar Atendimento. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * Deleta um Atendimento do Banco De Dados
     * 
     * @param Connection
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletar(Connection con, int handle_at) {
        boolean deleto = false;
        String sql = "delete from atendimentos where HANDLE_AT=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, handle_at);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Atendimento. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return deleto;
        }
    }

    /**
     * marca flag_imprimiu como "S" para nao poder mais mecher nos valores do atendimento
     * 
     * @param Connection
     * @param PacienteMODEL
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdateFlagImprimiu(Connection con, int handle_at) {
        boolean cadastro = false;
        String sql = "update atendimentos set flag_imprimiu='S' where handle_at=" + handle_at;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao marcar flag de Impressão. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * muda o status do atendimento
     * 
     * @param Connection
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdateStatus1(Connection con, int handle_at, String status) {
        boolean cadastro = false;
        String sql = "update atendimentos set status1=? where handle_at=" + handle_at;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao mudar Status do Atendimento. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * buscando o status 1 quando for imprimir!! se tive passado de 1 é pq nao fez algo depois de imprimir ficha e entao
     * retorna false se for 1 retorna false tb pois ja esta o mesmo flag que iriamos marcar se for nulo ou vazio retonr
     * tru para ele marcar o flag como 1 que foi impresso a ficha (ou seja paciente esta em atendimento, ainda nao mudo
     * para exame ou laduo pronto!!
     * 
     * @param Connection
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean getMarcarStatus1(Connection con, int handle_at) {
        boolean marcarStatus1 = false;
        String sql = "select status1 from atendimentos where handle_at=" + handle_at;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    int status1 = Integer.valueOf(rs.getString("status1"));
                    if(status1 > 1){
                        marcarStatus1 = false;
                    }
                } catch (Exception e) {
                    marcarStatus1 = true;
                }
            }
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao mudar Status do Atendimento. Procure o Administrador." + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return marcarStatus1;
        }
    }

    /**
     * marca O CAMPO EXAME_ENTREGUE_AO_PACIENTE do banco de dados
     * 
     * @param Connection
     * @param PacienteMODEL
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setEntregouExameAoPaciente(Connection con, int handle_at) {
        boolean cadastro = false;
        String sql = "update atendimentos set exame_entregue_ao_paciente='S' where handle_at=" + handle_at;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao marcar Exame Entregue ao Paciente. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * marca O CAMPO LAUDO_ENTREGUE_AO_PACIENTE do banco de dados
     * 
     * @param Connection
     * @param PacienteMODEL
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setLaudoExameAoPaciente(Connection con, int handle_at) {
        boolean cadastro = false;
        String sql = "update atendimentos set laudo_entregue_ao_paciente='S' where handle_at=" + handle_at;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao marcar Laudo Entregue ao Paciente. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * se vier no parametro 1 - entregou somente o exame se vier no parametro 2 - entregou somente o laudo se vier no
     * parametro 3 - entregou os dois
     * 
     * @param Connection
     * @param PacienteMODEL
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setEntregaDeExame(Connection con, int parametro, int handle_at) {
        if (parametro == 1) {
            boolean cadastro = false;
            String sql =
                "update atendimentos set EXAME_ENTREGUE_AO_PACIENTE='S', LAUDO_ENTREGUE_AO_PACIENTE='N' where handle_at="
                    + handle_at;
            try {
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.executeUpdate();
                stmt.close();
                cadastro = true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                    "Erro ao marcar flag de Entrega ao Paciente. Procure o Administrador.", "ERRO",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                return cadastro;
            }
        } else if (parametro == 2) {
            boolean cadastro = false;
            String sql =
                "update atendimentos set LAUDO_ENTREGUE_AO_PACIENTE='S', EXAME_ENTREGUE_AO_PACIENTE='N' where handle_at="
                    + handle_at;
            try {
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.executeUpdate();
                stmt.close();
                cadastro = true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                    "Erro ao marcar flag de Entrega ao Paciente. Procure o Administrador.", "ERRO",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                return cadastro;
            }
        } else {
            boolean cadastro = false;
            String sql =
                "update atendimentos set LAUDO_ENTREGUE_AO_PACIENTE='S', EXAME_ENTREGUE_AO_PACIENTE='S'   where handle_at="
                    + handle_at;
            try {
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.executeUpdate();
                stmt.close();
                cadastro = true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                    "Erro ao marcar flag de Entrega ao Paciente. Procure o Administrador.", "ERRO",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                return cadastro;
            }
        }

    }

    /**
     * Consulta o modelo de verificação do convenio
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarModeloDeValidacaoMatriculaDoConvenio(Connection con, int handle_convenio) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select validacao_matricula from convenio where handle_convenio=?");
            stmtQuery.setInt(1, handle_convenio);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar Modelo de Validação da Matrícula. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta a agenda de um atendimento
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAgendaDeUmAtendimento(Connection con, int handle_at) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select a.handle_agenda, p.nome as nomepac from atendimentos as a "
                    + "inner join agendas p on a.handle_agenda = p.handle_agenda " + "where a.handle_at=?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Agenda do Atendimento. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /*
     * 
     * @param handle_at
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDadosEtiqueta(Connection con, int handle_at) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select a.data_atendimento, "
                    + "p.nome, "
                    + "t.nome as nomeAreaAtendimento "
                    + "from atendimentos a "
                    + "inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "inner join areas_atendimento t on a.id_areas_atendimento = t.id_areas_atendimento "
                    + "where a.handle_at = ?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar informações para a Etiqueta. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }
    
    /**
     * Consulta Todos exames para ser colocado no arquivo TXT do faturamento
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentosPorPeriodoEConvenio(Connection con, Date diaInicial, Date diaFinal,
        int handle_convenio, int handle_at) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select distinct a.handle_at, a.matricula_convenio, a.data_atendimento, c.porcentconvenio, c.handle_convenio, e.VALOR_CORRETO_CONVENIO, e.atendimento_exame_id, e.lista_materiais, p.nome, m.crm, t.cod_exame from atendimento_exames e "
                    + "inner join atendimentos a on a.handle_at = e.handle_at "
                    + "inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoid "
                    + "inner join tabelas t on e.handle_exame = t.handle_exame and t.handle_convenio = ? "
                    + "inner join convenio c on c.handle_convenio = t.handle_convenio "
                    + "where a.handle_at=? and (data_atendimento > ?  or data_atendimento = ?) and (data_atendimento < ?  or data_atendimento = ?) and a.handle_convenio = ? order by handle_at");
            stmtQuery.setInt(1, handle_convenio);
            stmtQuery.setInt(2, handle_at);
            stmtQuery.setDate(3, diaInicial);
            stmtQuery.setDate(4, diaInicial);
            stmtQuery.setDate(5, diaFinal);
            stmtQuery.setDate(6, diaFinal);
            stmtQuery.setInt(7, handle_convenio);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }
    
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentosPorPeriodoEGrupo(Connection con, Date diaInicial, Date diaFinal,
        int grupo_id, int handle_at) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select distinct a.handle_at, a.matricula_convenio, a.data_atendimento, c.porcentconvenio, c.handle_convenio, e.VALOR_CORRETO_CONVENIO, e.atendimento_exame_id, e.lista_materiais, p.nome, m.crm, t.cod_exame from atendimento_exames e "
                    + "inner join atendimentos a on a.handle_at = e.handle_at "
                    + "inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoid "
                    + "inner join tabelas t on e.handle_exame = t.handle_exame "
                    + "inner join convenio c on c.handle_convenio = t.handle_convenio "
                    + "where e.handle_at=? and (data_atendimento > ? or data_atendimento = ?) and (data_atendimento < ? or data_atendimento = ?) and c.grupoid = ? and a.handle_convenio = c.handle_convenio order by c.handle_convenio, handle_at");
            stmtQuery.setInt(1, handle_at);
            stmtQuery.setDate(2, diaInicial);
            stmtQuery.setDate(3, diaInicial);
            stmtQuery.setDate(4, diaFinal);
            stmtQuery.setDate(5, diaFinal);
            stmtQuery.setInt(6, grupo_id);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }
}
