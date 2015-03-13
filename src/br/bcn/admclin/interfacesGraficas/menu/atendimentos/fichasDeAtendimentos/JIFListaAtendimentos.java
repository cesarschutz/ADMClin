/*
- * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 992 - 641 o tamanho maximo da janela!!
 * 
 * 
 * STATUS1 NO BANCO = STATUS DO ATENDIMENTO
 * status do atendimento vaizo / nulo - nao fez nada - ICONE CANETA
 * 1 - imprimiur ficha  - ICONE IMPRIMIR FICHA
 * 2 - ja fez o exame (quem marca eh o pac link)  - icone do menu exames
 * 3 - ja foi embora - procurar icone
 * 4 - ja tem aludo digitado (quem marca eh o pac link) - pegar com o theo
 * 5 - ja tem laudo assinado (quem marca eh o pac link) - pegar
 * 
 * 
 * STATUS2 NO BANCO = STATUS DO PACIENTE
 * status do atendimento vaizo / nulo - nao fez nada - sem icone
 * 1 - emergencia
 * 2 - deve ser avisado para complemento
 * 3 - ja foi avisado para complementação
 * 4 - deve ser avisado para trazer anteriores
 * 5 - ja foi avisado para trazer anteriores
 * 6 - deve ser avisado para realização do exame
 * 7 - ja foi avisado para a realizaçao do exame
 * 8 - medico solicitante deve ser contatado para procedimento
 * 10 - medico solicitante ja foi contatado para procedimento 
 * 11 - paciente com historia de hipersencibilidade medicamentosa
 * 
 * 
 * ENTREGUE
 * trabalha com os campso do banco exame_entregue_ao_paciente e laudo_entregue_ao_paciente
 * caso um dos dois seja entregue ele marcao icone correspondente!
 * 
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.fichasDeAtendimentos;

import br.bcn.admclin.ClasseAuxiliares.ColunaAceitandoIcone;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteNumerosELetras;
import br.bcn.admclin.dao.dbris.ATENDIMENTOS;
import br.bcn.admclin.dao.dbris.ATENDIMENTO_EXAMES;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.impressoes.modelo1.ImprimirBoletoDeRetiradaModelo1;
import br.bcn.admclin.impressoes.modelo1.ImprimirEtiquetaEnvelopeModelo1;
import br.bcn.admclin.impressoes.modelo1.ImprimirFichaDeAutorizacaoModelo1;
import br.bcn.admclin.impressoes.modelo2e3.ImprimirEtiquetaCodigoDeBarrasModelo2;
import br.bcn.admclin.impressoes.modelo2e3.ImprimirEtiquetaEnvelopeModelo2;
import br.bcn.admclin.impressoes.modelo2e3.ImprimirFichaEBoletoDeRetiradaModelo2;
import br.bcn.admclin.impressoes.modelo2e3.ImprimirFichaEBoletoDeRetiradaModelo3;
import br.bcn.admclin.impressoes.modelo4.ImprimirFichaDeAutorizacaoModelo4;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @author Cesar Schutz
 */
public class JIFListaAtendimentos extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    // lista para guardar as informações da tabela!! isso para quando pesquisar os pacientes ou mudar a modalidade
    // ae consultamos da lista, consultamos do banco somente quando muda a data
    private Object[][] atendimentos;
    private Connection con = null;

    /**
     * Creates new form JIFAtendimentos
     */
    public JIFListaAtendimentos() {
        initComponents();
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse();
        tirandoBarraDeTitulo();
        iniciarClasse();
        reescreverMetodoActionPerformanceDoDatePicker();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTFPesquisaPaciente.requestFocusInWindow();
            }
        });
    }

    // metodo construtor para chamar esta janela apartir da pesquisa de atendimentos
    public JIFListaAtendimentos(int handle_at, String dataDaVisualizacao) {
        initComponents();
        jXDatePicker1.setDate(formDataStrgToJava(dataDaVisualizacao));
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse();
        tirandoBarraDeTitulo();
        iniciarClasse();
        reescreverMetodoActionPerformanceDoDatePicker();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTFPesquisaPaciente.requestFocusInWindow();
            }
        });

        // selecionando a lnha do handleAT
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if (Integer.valueOf(String.valueOf(jTable1.getValueAt(i, 0))) == handle_at) {
                jTable1.addRowSelectionInterval(i, i);
            }
        }
    }

    public java.util.Date formDataStrgToJava(String data) {
        /*
         * Função de conversão de uma data do tipo STRING dd-MM-yyyy para formato date do java.util
         */
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");// Formato da data que virá do banco
        java.util.Date date = null;
        try {
            date = (java.util.Date) formatter.parse(data);// convertendo o formato para Date
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return date;
    }

    /*
     * metodo apra selecionar a linha se clicar com o botao direito
     */
    public void ativandoSelecaoDeLinhaComBotaoDireitoDoMouse() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3) {
                    int col = jTable1.columnAtPoint(e.getPoint());
                    int row = jTable1.rowAtPoint(e.getPoint());
                    if (col != -1 && row != -1) {
                        jTable1.setColumnSelectionInterval(col, col);
                        jTable1.setRowSelectionInterval(row, row);
                    }
                }

                // colocando a seleção na celula clicada
                int linhaSelecionada = jTable1.getSelectedRow();
                int colunaSelecionada = jTable1.getSelectedColumn();

                jTable1.editCellAt(linhaSelecionada, colunaSelecionada);
            }
        });
    }

    /*
     * metodo que pega a data do datapicker e retorna como uma sq.date
     */
    private Date pegandoDataDoDataPicker() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date data = null;
        java.util.Date dataSelecionada = jXDatePicker1.getDate();
        // criando um formato de data
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        // colocando data selecionado no formato criado acima
        String data2 = dataFormatada.format(dataSelecionada);

        try {
            data = new java.sql.Date(format.parse(data2).getTime());
            return data;
        } catch (ParseException ex) {
            return null;
        }
    }

    // metodo que soh serve para reescrever o evento ActionPerformance do DatePicker
    private void reescreverMetodoActionPerformanceDoDatePicker() {
        jXDatePicker1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                preenchendoTabela();
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        jTFPesquisaPaciente.requestFocusInWindow();
                    }
                });
            }
        });
    }

    private void iniciarClasse() {
    	// arrumando formatação do data picker
        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Hoje");

        // selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // aumentando tamanho da linha
        jTable1.setRowHeight(30);

        // pegamos a data do dataPicker e atualizamos a tabelas
        preenchendoTabela();

        // coluna de status aceitando icones
        TableCellRenderer tcrColuna6 = new ColunaAceitandoIcone();
        TableColumn column6 = jTable1.getColumnModel().getColumn(6);
        column6.setCellRenderer(tcrColuna6);

        TableCellRenderer tcrColuna7 = new ColunaAceitandoIcone();
        TableColumn column7 = jTable1.getColumnModel().getColumn(7);
        column7.setCellRenderer(tcrColuna7);

        TableCellRenderer tcrColuna8 = new ColunaAceitandoIcone();
        TableColumn column8 = jTable1.getColumnModel().getColumn(8);
        column8.setCellRenderer(tcrColuna8);
        
        TableCellRenderer tcrColuna9 = new ColunaAceitandoIcone();
        TableColumn column9 = jTable1.getColumnModel().getColumn(9);
        column9.setCellRenderer(tcrColuna9);

        // tamanho das colunas
        jTable1.getColumnModel().getColumn(0).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(38);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(45);
        // jTable1.getColumnModel().getColumn(3).setMaxWidth(65);
        // jTable1.getColumnModel().getColumn(4).setMaxWidth(55);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(105);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(105);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(7).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(8).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(9).setMaxWidth(65);

        // alinhando conteudo da coluna de uma tabela
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        jTable1.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(centralizado);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(direita);

    }

    /*
     * Tira a barra e o contorno do internal frame (this)
     */
    private void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    /*
     * metodo que preenche a tabela DE ACORDO COM O BANCO DE DADOS E PREENCHENDO O ARRAY COM AS INFORMAÇÕES
     */
    public void preenchendoTabela() {
        Date data = pegandoDataDoDataPicker();
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

        con = Conexao.fazConexao();
        ResultSet resultSet = ATENDIMENTOS.getConsultarAtendimentos(con, data);
        try {
            while (resultSet.next()) {
                int handle_at = resultSet.getInt("handle_at");
                //String modalidade = resultSet.getString("modalidade");
                String hora_atendimento =
                    MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento"));
                String nomePaciente = resultSet.getString("nomePaciente");
                String nomeMedico = resultSet.getString("nomeMedico");
                String crmMedico = resultSet.getString("crmMedico");
                String status1 = resultSet.getString("status1");
                String status2 = resultSet.getString("status2");
                String EXAME_ENTREGUE_AO_PACIENTE = resultSet.getString("EXAME_ENTREGUE_AO_PACIENTE");
                String LAUDO_ENTREGUE_AO_PACIENTE = resultSet.getString("LAUDO_ENTREGUE_AO_PACIENTE");
                String entrega = "";
                if ("S".equals(EXAME_ENTREGUE_AO_PACIENTE) && "S".equals(LAUDO_ENTREGUE_AO_PACIENTE)) {
                    entrega = "3";
                } else if (!"S".equals(EXAME_ENTREGUE_AO_PACIENTE) && "S".equals(LAUDO_ENTREGUE_AO_PACIENTE)) {
                    entrega = "2";
                } else if ("S".equals(EXAME_ENTREGUE_AO_PACIENTE) && !"S".equals(LAUDO_ENTREGUE_AO_PACIENTE)) {
                    entrega = "1";
                }
                int pacientePagou = resultSet.getInt("paciente_pagou");
                modelo.addRow(new Object[] { handle_at, "", hora_atendimento, nomePaciente, nomeMedico,
                    crmMedico, status1, status2, entrega, pacientePagou });
            }

            salvandoInformacoesDaTabelaNoArray();
            colocarIconesNoStatusA();
            colocarIconesNoEntrega();

            jCBTodas.setSelected(true);
            jCBcr.setSelected(true);
            jCBct.setSelected(true);
            jCBdr.setSelected(true);
            jCBdx.setSelected(true);
            jCBmg.setSelected(true);
            jCBmr.setSelected(true);
            jCBnm.setSelected(true);
            jCBot.setSelected(true);
            jCBrf.setSelected(true);
            jCBtr.setSelected(true);
            jCBus.setSelected(true);
            jCBdo.setSelected(true);
            jCBod.setSelected(true);
            jTFMensagemParaUsuario.setText("");
            jTFPesquisaPaciente.setText("");

        } catch (Exception e) {
            atendimentos = null;
            ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
            jTable1.updateUI();
            JOptionPane.showMessageDialog(null, "Não foi possivel consultar os Atendimentos. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * prenche a tabela de acordo com a pesquisa e a modalidade selecionada
     */
    private void preenchendoATabela() {
        boolean isNumber;
        // essa variavel fica false se ele preenche com todas as modalidades e nenhum pesquisa
        // pq dae preenche do banco e isso ja preenche os icones, se eu preencher novamente da erro
        boolean colocarIcone = true;
        // zerando a tabela e pegando o modelo
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

        // se for todas as modalidades
        if (jCBTodas.isSelected()) {
            // se nao tiver nada na pesquisa
            if ("".equals(jTFPesquisaPaciente.getText())) {
                // se for todas as modalidades e sem pesquisa ele preenche a tabela com o banco de dados
                preenchendoTabela();
                colocarIcone = false;
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
                jTFMensagemParaUsuario.setText("Digite um Código ou um Paciente para Pesquisar");
            } else {
                // CAI AQUI SE FOR TODAS AS MODALIDADE TIVER UMA PESQUISA
                try {
                    @SuppressWarnings("unused")
                    int x = Integer.valueOf(jTFPesquisaPaciente.getText());
                    isNumber = true;
                } catch (Exception e) {
                    isNumber = false;
                }

                // SE FOR NUMERO PESQUISA POR CODIGO
                if (isNumber) {
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
                    jTFMensagemParaUsuario.setText("Pesquisa por Código");
                    // vamos pesquisar pelo codigo
                    for (int i = 0; i < atendimentos.length; i++) {
                        // essas duas linhas tiram os 0 digitados antes do handle_at
                        int handleDigitado = Integer.valueOf(jTFPesquisaPaciente.getText());
                        String handleDigitadoString = String.valueOf(handleDigitado);
                        // se o codigo da lista começa com o codigo ditado
                        if (String.valueOf(atendimentos[i][0]).startsWith(handleDigitadoString)) {
                            // colocando na tabela
                            modelo.addRow(new Object[] { atendimentos[i][0], atendimentos[i][1], atendimentos[i][2],
                                atendimentos[i][3], atendimentos[i][4], atendimentos[i][5], atendimentos[i][6],
                                atendimentos[i][7], atendimentos[i][8] });
                        }
                    }

                    // se nao for numero pesquisa por nome
                } else {
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
                    jTFMensagemParaUsuario.setText("Pesquisa por Paciente");
                    // vamos pesquisar pelo codigo
                    for (int i = 0; i < atendimentos.length; i++) {
                        // se o pedaço de nome tiver em algum nome da tabela ele aparece na tabela
                        if (String.valueOf(atendimentos[i][3]).toUpperCase().contains(jTFPesquisaPaciente.getText())) {
                            // colocando na tabela
                            modelo.addRow(new Object[] { atendimentos[i][0], atendimentos[i][1], atendimentos[i][2],
                                atendimentos[i][3], atendimentos[i][4], atendimentos[i][5], atendimentos[i][6],
                                atendimentos[i][7], atendimentos[i][8] });
                        }
                    }

                }
            }
        } else {
            // CAI AQUI SE FOR UMA MODALIDADE
            String modalidade = null;
            if (jCBcr.isSelected()) {
                modalidade = "CR";
            }
            if (jCBct.isSelected()) {
                modalidade = "CT";
            }
            if (jCBdo.isSelected()) {
                modalidade = "DO";
            }
            if (jCBdr.isSelected()) {
                modalidade = "DR";
            }
            if (jCBdx.isSelected()) {
                modalidade = "DX";
            }
            if (jCBmg.isSelected()) {
                modalidade = "MG";
            }
            if (jCBmr.isSelected()) {
                modalidade = "MR";
            }
            if (jCBnm.isSelected()) {
                modalidade = "NM";
            }
            if (jCBod.isSelected()) {
                modalidade = "OD";
            }
            if (jCBot.isSelected()) {
                modalidade = "OT";
            }
            if (jCBrf.isSelected()) {
                modalidade = "RF";
            }
            if (jCBtr.isSelected()) {
                modalidade = "SC";
            }
            if (jCBus.isSelected()) {
                modalidade = "US";
            }

            // se nao tiver nada na pesquisa
            if ("".equals(jTFPesquisaPaciente.getText())) {
                // vamos pesquisar pelA MODALIDADE
                for (int i = 0; i < atendimentos.length; i++) {
                    // se o codigo da lista começa com o codigo ditado
                    if (String.valueOf(atendimentos[i][1]).equals(modalidade)) {
                        // colocando na tabela
                        modelo.addRow(new Object[] { atendimentos[i][0], atendimentos[i][1], atendimentos[i][2],
                            atendimentos[i][3], atendimentos[i][4], atendimentos[i][5], atendimentos[i][6],
                            atendimentos[i][7], atendimentos[i][8] });
                    }
                }
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
                jTFMensagemParaUsuario.setText("Digite um Código ou um Paciente para Pesquisar");
            } else {
                // cai aqui caso tenha algo na pesquisa
                try {
                    isNumber = true;
                } catch (Exception e) {
                    isNumber = false;
                }

                // SE FOR NUMERO PESQUISA POR CODIGO
                if (isNumber) {
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
                    jTFMensagemParaUsuario.setText("Pesquisa por Código");
                    // vamos pesquisar pelo codigo
                    for (int i = 0; i < atendimentos.length; i++) {
                        // essas duas linhas tiram os 0 digitados antes do handle_at
                        int handleDigitado = Integer.valueOf(jTFPesquisaPaciente.getText());
                        String handleDigitadoString = String.valueOf(handleDigitado);
                        // se o codigo da lista começa com o codigo ditado
                        if (String.valueOf(atendimentos[i][0]).startsWith(handleDigitadoString)
                            && String.valueOf(atendimentos[i][1]).equals(modalidade)) {
                            // colocando na tabela
                            modelo.addRow(new Object[] { atendimentos[i][0], atendimentos[i][1], atendimentos[i][2],
                                atendimentos[i][3], atendimentos[i][4], atendimentos[i][5], atendimentos[i][6],
                                atendimentos[i][7], atendimentos[i][8] });
                        }
                    }

                    // se nao for numero pesquisa por nome
                } else {
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
                    jTFMensagemParaUsuario.setText("Pesquisa por Paciente");
                    // vamos pesquisar pelo codigo
                    for (int i = 0; i < atendimentos.length; i++) {
                        // se o pedaço de nome tiver em algum nome da tabela ele aparece na tabela
                        if (String.valueOf(atendimentos[i][3]).toUpperCase().contains(jTFPesquisaPaciente.getText())
                            && String.valueOf(atendimentos[i][1]).equals(modalidade)) {
                            // colocando na tabela
                            modelo.addRow(new Object[] { atendimentos[i][0], atendimentos[i][1], atendimentos[i][2],
                                atendimentos[i][3], atendimentos[i][4], atendimentos[i][5], atendimentos[i][6],
                                atendimentos[i][7], atendimentos[i][8] });
                        }
                    }

                }
            }
        }

        // VAMOS COLOCAR OS ICONES se nao tiver atualizado com o banco
        if (colocarIcone) {
            try {
                // colocando icones
                colocarIconesNoStatusA();
                colocarIconesNoEntrega();
            } catch (Exception e) {
                // caso nao consiga coloca os icones ele zera a tabela
                jTFMensagemParaUsuario.setText("");
                jTFPesquisaPaciente.setText("");
                preenchendoTabela();
            }
        }

    }

    /*
     * metodo que guarda os atendimentos colocados na tabela em um array list
     */
    private void salvandoInformacoesDaTabelaNoArray() throws Exception {
        atendimentos = new Object[jTable1.getRowCount()][9];

        for (int i = 0; i < jTable1.getRowCount(); i++) {
            //como foi tirada a hora e a modalidade do atendimento, pode ser que esteja nulo, ae trata o erro.
            atendimentos[i][0] = jTable1.getValueAt(i, 0).toString();
            try {
                atendimentos[i][1] = jTable1.getValueAt(i, 1).toString();
            } catch (NullPointerException e) {
                atendimentos[i][1] = "";
            }
            try {
                atendimentos[i][2] = jTable1.getValueAt(i, 1).toString();
            } catch (NullPointerException e) {
                atendimentos[i][2] = "";
            }
            atendimentos[i][3] = jTable1.getValueAt(i, 3).toString();
            atendimentos[i][4] = jTable1.getValueAt(i, 4).toString();
            atendimentos[i][5] = jTable1.getValueAt(i, 5).toString();
            if (jTable1.getValueAt(i, 6) == null || "null".equals(jTable1.getValueAt(i, 6))
                || "".equals(jTable1.getValueAt(i, 6))) {
                atendimentos[i][6] = "";
            } else {
                atendimentos[i][6] = jTable1.getValueAt(i, 6).toString();
            }
            if (jTable1.getValueAt(i, 7) == null || "null".equals(jTable1.getValueAt(i, 7))
                || "".equals(jTable1.getValueAt(i, 7))) {
                atendimentos[i][7] = "";
            } else {
                atendimentos[i][7] = jTable1.getValueAt(i, 7).toString();
            }
            if (jTable1.getValueAt(i, 8) == null || "null".equals(jTable1.getValueAt(i, 8))
                || "".equals(jTable1.getValueAt(i, 8))) {
                atendimentos[i][8] = "";
            } else {
                atendimentos[i][8] = jTable1.getValueAt(i, 8).toString();
            }
        }
    }

    /*
     * coloca os icones dos atendimentos de acordo com o status
     */
    Icon iconeAtendimento = new ImageIcon(getToolkit().createImage(
        getClass().getResource("/br/bcn/admclin/imagens/menuAtendimento.png")));
    Icon iconeImprimuFicha = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/imprimirFicha.png"));
    Icon iconeLaudoDigitado = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/laudoDigitado.png"));
    Icon iconeLaudoAssinado = new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/laudoAssinado.png"));
    Icon iconeLaudoAssinadoE = new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/laudoAssinadoE.png"));
    Icon iconeJaFezOExame =
        new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/jaFezOExame.png"));

    private void colocarIconesNoStatusA() throws Exception {
        // icone atendimento
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            try {
                int status1 = Integer.valueOf(String.valueOf(jTable1.getValueAt(i, 6)));
                // ja imprimiu ficha
                if (status1 == 1) {
                    jTable1.setValueAt(iconeImprimuFicha, i, 6);
                }
                // ja fez o exame
                if (status1 == 2) {
                    jTable1.setValueAt(iconeJaFezOExame, i, 6);
                }
                // laudo digitado
                if (status1 == 4) {
                    jTable1.setValueAt(iconeLaudoDigitado, i, 6);
                }
                // laudo assinado
                if (status1 == 5) {
                    jTable1.setValueAt(iconeLaudoAssinado, i, 6);
                }
                // laudo assinado e nao enviado ao HIS
                if (status1 == 9) {
                    jTable1.setValueAt(iconeLaudoAssinadoE, i, 6);
                }

            } catch (Exception e) {
                // cai aqui se nao tive nada na coluna do bando status1 (como nao tem nada ainda nao foi feito nada
                // naquele atendimento)
                jTable1.setValueAt(iconeAtendimento, i, 6);
            }
        }
    }

    private void colocarIconesNoEntrega() throws Exception {
        // icone atendimento
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            try {
                int status1 = Integer.valueOf(String.valueOf(jTable1.getValueAt(i, 8)));
                // exame entregue
                if (status1 == 1) {
                    jTable1.setValueAt(iconExameEntregue, i, 8);
                }
                // laudo entregue
                if (status1 == 2) {
                    jTable1.setValueAt(iconeLaudoEntregue, i, 8);
                }
                // os dois entregue
                if (status1 == 3) {
                    jTable1.setValueAt(iconeLaudoEExameEntregue, i, 8);
                }
            } catch (Exception e) {
            }
        }
        
     // icone de pagamento do paciente
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            try {
                int status1 = Integer.valueOf(String.valueOf(jTable1.getValueAt(i, 9)));
                // nao pago
                if (status1 == 0) {
                	
                    jTable1.setValueAt(iconePacienteNaoPagou, i, 9);
                }
                // pago
                if (status1 == 1) {
                	
                    jTable1.setValueAt(iconePacientePagou, i, 9);
                }
            } catch (Exception e) {
            }
        }
    }
    ImageIcon iconePacienteNaoPagou = new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/nao.png"));
    ImageIcon iconePacientePagou = new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/menuRecebimentoDeConvenios.png"));

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jCBtr = new javax.swing.JCheckBox();
        jCBot = new javax.swing.JCheckBox();
        jCBrf = new javax.swing.JCheckBox();
        jCBnm = new javax.swing.JCheckBox();
        jCBdr = new javax.swing.JCheckBox();
        jCBct = new javax.swing.JCheckBox();
        jCBmg = new javax.swing.JCheckBox();
        jCBdx = new javax.swing.JCheckBox();
        jCBcr = new javax.swing.JCheckBox();
        jCBmr = new javax.swing.JCheckBox();
        jCBTodas = new javax.swing.JCheckBox();
        jCBus = new javax.swing.JCheckBox();
        jTFPesquisaPaciente = new javax.swing.JTextField(new DocumentoSomenteNumerosELetras(100), null, 0);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jCBdo = new javax.swing.JCheckBox();
        jCBod = new javax.swing.JCheckBox();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Atendimentos",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jXDatePicker1.setRequestFocusEnabled(false);

        jTable1.setModel(new DefaultTableModel(
        	new Object[][] {
        		{null, null, null, null, null, null, null, null, null, null},
        		{null, null, null, null, null, null, null, null, null, null},
        		{null, null, null, null, null, null, null, null, null, null},
        		{null, null, null, null, null, null, null, null, null, null},
        	},
        	new String[] {
        		"C\u00F3digo", "Mod.", "Hora", "Paciente", "M\u00E9dico Solicitante", "CRM", "Status A.", "Status P.", "Entrega", "Pac. Pag."
        	}
        ) {
        	boolean[] columnEditables = new boolean[] {
        		false, true, false, false, false, false, false, false, false, false
        	};
        	public boolean isCellEditable(int row, int column) {
        		return columnEditables[column];
        	}
        });
        jTable1.getColumnModel().getColumn(0).setResizable(false);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(15);
        jTable1.getColumnModel().getColumn(1).setMinWidth(0);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(38);
        jTable1.getColumnModel().getColumn(2).setResizable(false);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(45);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(45);
        jTable1.getColumnModel().getColumn(3).setResizable(false);
        jTable1.getColumnModel().getColumn(4).setResizable(false);
        jTable1.getColumnModel().getColumn(5).setResizable(false);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(105);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(105);
        jTable1.getColumnModel().getColumn(6).setResizable(false);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(7).setResizable(false);
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(7).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(8).setResizable(false);
        jTable1.getColumnModel().getColumn(8).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(8).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(9).setResizable(false);
        jTable1.getColumnModel().getColumn(9).setPreferredWidth(70);
        jTable1.getColumnModel().getColumn(9).setMaxWidth(70);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jCBtr.setSelected(true);
        jCBtr.setText("TR");
        jCBtr.setVisible(false);
        jCBtr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBtrActionPerformed(evt);
            }
        });

        jCBot.setSelected(true);
        jCBot.setText("OT");
        jCBot.setVisible(false);
        jCBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBotActionPerformed(evt);
            }
        });

        jCBrf.setSelected(true);
        jCBrf.setText("RF");
        jCBrf.setVisible(false);
        jCBrf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBrfActionPerformed(evt);
            }
        });

        jCBnm.setSelected(true);
        jCBnm.setText("NM");
        jCBnm.setVisible(false);
        jCBnm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBnmActionPerformed(evt);
            }
        });

        jCBdr.setSelected(true);
        jCBdr.setText("DR");
        jCBdr.setVisible(false);
        jCBdr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBdrActionPerformed(evt);
            }
        });

        jCBct.setSelected(true);
        jCBct.setText("CT");
        jCBct.setVisible(false);
        jCBct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBctActionPerformed(evt);
            }
        });

        jCBmg.setSelected(true);
        jCBmg.setText("MG");
        jCBmg.setVisible(false);
        jCBmg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBmgActionPerformed(evt);
            }
        });

        jCBdx.setSelected(true);
        jCBdx.setText("DX");
        jCBdx.setVisible(false);
        jCBdx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBdxActionPerformed(evt);
            }
        });

        jCBcr.setSelected(true);
        jCBcr.setText("CR");
        jCBcr.setVisible(false);
        jCBcr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBcrActionPerformed(evt);
            }
        });

        jCBmr.setSelected(true);
        jCBmr.setText("MR");
        jCBmr.setVisible(false);
        jCBmr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBmrActionPerformed(evt);
            }
        });

        jCBTodas.setSelected(true);
        jCBTodas.setText("Todas");
        jCBTodas.setVisible(false);
        jCBTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBTodasActionPerformed(evt);
            }
        });

        jCBus.setSelected(true);
        jCBus.setText("US");
        jCBus.setVisible(false);
        jCBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBusActionPerformed(evt);
            }
        });

        jTFPesquisaPaciente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFPesquisaPacienteFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFPesquisaPacienteFocusLost(evt);
            }
        });
        jTFPesquisaPaciente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFPesquisaPacienteKeyReleased(evt);
            }
        });

        jLabel1.setText("Data:");

        jLabel2.setText("Código / Paciente");

        jLabel3
            .setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemPesquisar.png"))); // NOI18N

        jButton5.setIcon(new javax.swing.ImageIcon(getClass()
            .getResource("/br/bcn/admclin/imagens/atualizarTabela.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/legenda.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jCBdo.setSelected(true);
        jCBdo.setText("DO");
        jCBdo.setVisible(false);
        jCBdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBdoActionPerformed(evt);
            }
        });

        jCBod.setSelected(true);
        jCBod.setText("OD");
        jCBod.setVisible(false);
        jCBod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBodActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 956, Short.MAX_VALUE)
            .addComponent(jTFPesquisaPaciente, javax.swing.GroupLayout.DEFAULT_SIZE, 956, Short.MAX_VALUE)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel1Layout
                    .createSequentialGroup()
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jCBcr)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBct)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBdo)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBdr)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBdx)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBmg)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBmr)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBnm)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBod)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBot)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBrf)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBtr)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBus)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBTodas))
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 145,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 660, Short.MAX_VALUE)
                    .addComponent(jButton6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton5)).addComponent(jTFMensagemParaUsuario));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel1Layout
                    .createSequentialGroup()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton5)
                            .addGroup(
                                jPanel1Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)).addComponent(jButton6))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jCBcr).addComponent(jCBct).addComponent(jCBdr)
                                            .addComponent(jCBdx).addComponent(jCBmg).addComponent(jCBmr)
                                            .addComponent(jCBnm).addComponent(jCBot).addComponent(jCBrf)
                                            .addComponent(jCBtr).addComponent(jCBus).addComponent(jCBTodas)
                                            .addComponent(jCBdo).addComponent(jCBod)).addGap(18, 18, 18))
                            .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3).addComponent(jLabel2))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                    .addComponent(jTFPesquisaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                        javax.swing.GroupLayout.PREFERRED_SIZE).addGap(12, 12, 12)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
        
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCBtrActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBtrActionPerformed

        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBmr.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBtr.setSelected(true);

        preenchendoATabela();
    }// GEN-LAST:event_jCBtrActionPerformed

    private void jCBotActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBotActionPerformed

        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBmr.setSelected(false);
        jCBnm.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBot.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBotActionPerformed

    private void jCBrfActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBrfActionPerformed

        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBmr.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBtr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBrf.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBrfActionPerformed

    private void jCBnmActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBnmActionPerformed

        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBmr.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBnm.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBnmActionPerformed

    private void jCBdrActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBdrActionPerformed

        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBmr.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBdr.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBdrActionPerformed

    private void jCBctActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBctActionPerformed

        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBmr.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBct.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBctActionPerformed

    private void jCBmgActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBmgActionPerformed

        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmr.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBmg.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBmgActionPerformed

    private void jCBdxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBdxActionPerformed

        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBmg.setSelected(false);
        jCBmr.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBdx.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBdxActionPerformed

    private void jCBcrActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBcrActionPerformed

        jCBTodas.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBmr.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBcr.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBcrActionPerformed

    private void jCBmrActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBmrActionPerformed

        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBmr.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBmrActionPerformed

    private void jCBTodasActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBTodasActionPerformed
        // marca todas as modalidades
        jCBcr.setSelected(true);
        jCBct.setSelected(true);
        jCBdr.setSelected(true);
        jCBdx.setSelected(true);
        jCBmg.setSelected(true);
        jCBmr.setSelected(true);
        jCBnm.setSelected(true);
        jCBot.setSelected(true);
        jCBrf.setSelected(true);
        jCBtr.setSelected(true);
        jCBus.setSelected(true);
        jCBdo.setSelected(true);
        jCBod.setSelected(true);

        jCBTodas.setSelected(true);

        preenchendoATabela();

    }// GEN-LAST:event_jCBTodasActionPerformed

    private void jCBusActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBusActionPerformed
        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBmr.setSelected(false);
        jCBdo.setSelected(false);
        jCBod.setSelected(false);

        jCBus.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBusActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        int colunaClicada = jTable1.columnAtPoint(evt.getPoint());
        int linhaClicada = jTable1.rowAtPoint(evt.getPoint());
        int linhaSelecionada = jTable1.getSelectedRow();

        if (MouseEvent.BUTTON3 == evt.getButton() && linhaClicada == linhaSelecionada) {
            if (colunaClicada == 6) {
                abrirPopUpDoAtendimento(evt);
            } else if (colunaClicada == 7) {

            } else if (colunaClicada == 8) {
                abrirPopUpEntregue(evt);
            }else if(colunaClicada == 9){
            	abrirPopUpPagamentoPaciente(evt);
            }else {
                abrirPopUpMenu(evt);
            }
        }

        jTFMensagemParaUsuario.requestFocusInWindow();
    }// GEN-LAST:event_jTable1MouseClicked

    private void jTFPesquisaPacienteKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFPesquisaPacienteKeyReleased
        preenchendoATabela();
    }// GEN-LAST:event_jTFPesquisaPacienteKeyReleased

    private void jTFPesquisaPacienteFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesquisaPacienteFocusGained
        if ("".equals(jTFPesquisaPaciente.getText())) {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
            jTFMensagemParaUsuario.setText("Digite um Código ou um Paciente para Pesquisar");
        }

    }// GEN-LAST:event_jTFPesquisaPacienteFocusGained

    private void jTFPesquisaPacienteFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesquisaPacienteFocusLost
        if ("".equals(jTFPesquisaPaciente.getText())) {
            jTFMensagemParaUsuario.setText("");
        }
    }// GEN-LAST:event_jTFPesquisaPacienteFocusLost

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
        preenchendoTabela();
    }// GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton6ActionPerformed
        JIFLlegendasAtendimentos legenda = new JIFLlegendasAtendimentos();
        legenda.setVisible(true);
    }// GEN-LAST:event_jButton6ActionPerformed

    private void jCBdoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBdoActionPerformed
        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBmr.setSelected(false);
        jCBus.setSelected(false);
        jCBod.setSelected(false);

        jCBdo.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBdoActionPerformed

    private void jCBodActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBodActionPerformed
        jCBTodas.setSelected(false);
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBtr.setSelected(false);
        jCBmr.setSelected(false);
        jCBus.setSelected(false);
        jCBdo.setSelected(false);

        jCBod.setSelected(true);
        preenchendoATabela();
    }// GEN-LAST:event_jCBodActionPerformed

    /*
     * metodo que abre um pop up com os dados do atendimento
     */
    public void abrirPopUpDoAtendimento(MouseEvent evt) {
        JPopupMenu popup = new JPopupMenu();

        // buscando as informações do atendimento
        con = Conexao.fazConexao();
        int handle_atendimento = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        ResultSet resultSet = ATENDIMENTOS.getConsultarDadosDeUmAtendimento(con, handle_atendimento);

        String data_atendimento = null, hora_atendimento = null, nome_paciente = null, nome_medico_sol = null, nome_convenio =
            null, nascimento_paciente = null, telefone_paciente = null, celular_paciente = null;
        ;
        int duracao_atendimento, finalDoAtendimento = 0;

        try {
            while (resultSet.next()) {
                data_atendimento =
                    MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_atendimento"));
                hora_atendimento = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento"));
                nome_paciente = resultSet.getString("nomepac");
                nome_medico_sol = resultSet.getString("nomemed");
                nome_convenio = resultSet.getString("nomeconv");
                duracao_atendimento = resultSet.getInt("duracao_atendimento");
                nascimento_paciente = resultSet.getString("nascimento_paciente");
                telefone_paciente = resultSet.getString("telefone_paciente");
                celular_paciente = resultSet.getString("celular_paciente");
                try {
                    finalDoAtendimento =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos(hora_atendimento))
                            + duracao_atendimento;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os dados deste Atendimento. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        // fechando a conexao
        Conexao.fechaConexao(con);

        // adicionando as informações do agendaento
        popup.add("Data                               : " + data_atendimento).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add("Horário                          : " + hora_atendimento).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add("Paciente                       : " + nome_paciente).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add("Nascimento                 : " + nascimento_paciente).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add("Telefone                       : " + telefone_paciente).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add("Celular                          : " + celular_paciente).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add("Médico Solicitante       : " + nome_medico_sol).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add("Convênio                      : " + nome_convenio).setEnabled(false);
        popup.add("").setEnabled(true);

        // exames
        con = Conexao.fazConexao();
        ResultSet resultSetExames = ATENDIMENTO_EXAMES.getConsultarExamesDeUmAtendimento(con, handle_atendimento);
        try {
            int j = 1;
            while (resultSetExames.next()) {
                // adicionando o exame
                popup.add(
                    "Exame " + j + "                       : " + resultSetExames.getString("nomeExame") + " - "
                        + resultSetExames.getString("lado") + " - " + resultSetExames.getString("material"))
                    .setEnabled(false);
                popup.add("").setEnabled(true);
                j++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os Exames deste Atendimento. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        // fechando a conexao
        Conexao.fechaConexao(con);

        // adicionando o final do atendimento
        popup.add("Final do Atendimento: " + MetodosUteis.transformarMinutosEmHorario(finalDoAtendimento)).setEnabled(
            false);

        // mostra na tela
        int x = evt.getX();
        int y = evt.getY();
        popup.show(jTable1, x, y);

    }

    /*
     * metodo para abrir o popUp de menu do Atendimento
     */
    ImageIcon iconeImprimir = new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imprimir.png"));
    ImageIcon iconeImprimirFicha = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/imprimirFicha.png"));
    ImageIcon iconeImprimirLaudo = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/imprimirLaudo.png"));
    ImageIcon iconeImprimirBoletoDeRetirada = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/imprimirBoletoDeRetirada.png"));
    ImageIcon iconeImrimirEtiqueta = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/imprimirEtiqueta.png"));
    ImageIcon iconeImprimirCodigoDeBarras = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/Barcode.png"));
    ImageIcon iconeHistoriaClinica = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/Historia.png"));
    ImageIcon iconeEscreverLaudo = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/EscreverLaudo.png"));

    private void abrirPopUpMenu(MouseEvent evt) {

        // menu imprimir
        JMenu imprimir = new JMenu("Imprimir");
        imprimir.setIcon(iconeImprimir);

        // imprimir laudo
        JMenuItem imprimirLaudo = new JMenuItem("Laudo", iconeImprimirLaudo);
        imprimirLaudo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // fazer o metodo
            }
        });

        // imprimir etiqueta
        JMenuItem imprimirEtiqueta = new JMenuItem("Etiqueta", iconeImrimirEtiqueta);
        imprimirEtiqueta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imprimirEiqueta();
            }
        });

        // imprimir ficha
        JMenuItem imprimirFicha = new JMenuItem("Ficha", iconeImprimirFicha);
        imprimirFicha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imprimirFicha();
            }
        });

        // imprimir ficha
        JMenuItem imprimirBoletoDeRetirada = new JMenuItem("Boleto de Retirada", iconeImprimirBoletoDeRetirada);
        imprimirBoletoDeRetirada.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imprimirBoletoDeRetirada();
            }
        });

        // imprimir codigo de barras
        JMenuItem imprimirCodigoDeBarras = new JMenuItem("Código de Barras", iconeImprimirCodigoDeBarras);
        imprimirCodigoDeBarras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imprimirCodigoDeBarras();
            }
        });
        
        

        // Historia Clinica
        JMenuItem historiaClinica = new JMenuItem("Historia Clínica", iconeHistoriaClinica);
        historiaClinica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                historiaClincia();
            }
        });

        // Laudo
        JMenuItem laudo = new JMenuItem("Laudo", iconeEscreverLaudo);
        laudo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                laudo();
            }
        });

        // adicionando os submenus no impimir

        // imprimir.add(imprimirLaudo);
        if (janelaPrincipal.modeloDeImpressao == 1) {
            imprimir.add(imprimirFicha);
            imprimir.add(imprimirBoletoDeRetirada);
            imprimir.add(imprimirEtiqueta);
        } else if (janelaPrincipal.modeloDeImpressao == 2 || janelaPrincipal.modeloDeImpressao == 3) {
            imprimir.add(imprimirFicha);
            imprimir.add(imprimirEtiqueta);
            imprimir.add(imprimirCodigoDeBarras);
        } else if (janelaPrincipal.modeloDeImpressao == 4){
            imprimir.add(imprimirFicha);
            imprimir.add(imprimirEtiqueta);
        }

        // cria o menu popup e adiciona os itens
        JPopupMenu popup = new JPopupMenu();
        popup.add(imprimir);
        popup.add(historiaClinica);
        popup.add(laudo);
        

        // mostra na tela
        // mostra na tela
        int x = evt.getX();
        int y = evt.getY();
        popup.show(jTable1, x, y);
    }

    private void abrirPopUpPagamentoPaciente(MouseEvent evt){
    	
    	final boolean pacientePagou;
        int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        
        //se paciente nao pagou abre popup
        if(ATENDIMENTOS.getPacientePagou(handle_at) == 0){
        	JMenuItem registrarPagamentoDePaciente = registrarPagamentoDePaciente = new JMenuItem("Definir como pago", iconePacientePagou);
            
            registrarPagamentoDePaciente.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	
                		registrarPagamentoDePaciente(1);
                		jTable1.setValueAt(iconePacientePagou, jTable1.getSelectedRow(), 9);
                    
                }
            });
        	
            JPopupMenu popup = new JPopupMenu();
            popup.add(registrarPagamentoDePaciente);
            

            // mostra na tela
            // mostra na tela
            int x = evt.getX();
            int y = evt.getY();
            popup.show(jTable1, x, y);
        }
        
        
    	
    }
    private void historiaClincia() {
        // pegando o handle_at
        int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        // pegando o nome do paciente
        String paciente = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3));
        // pegando a data
        java.util.Date dataSelecionada = jXDatePicker1.getDate();
        // criando um formato de data
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        // colocando data selecionado no formato criado acima
        String data2 = dataFormatada.format(dataSelecionada);

        JFHistoriaClinica his = new JFHistoriaClinica(handle_at, data2, paciente);
        his.setVisible(true);
    }

    private void registrarPagamentoDePaciente(int flag){
    	int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
    	ATENDIMENTOS.setPacientePagou(handle_at, flag);
    }
    
    private void laudo() {
        // pegando a data
        java.util.Date dataSelecionada = jXDatePicker1.getDate();
        // criando um formato de data
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        // colocando data selecionado no formato criado acima
        String data2 = dataFormatada.format(dataSelecionada);

        // pegando o nome do paciente
        String paciente = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3));

        // pegando o handle_at
        String handle_at = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0));

        // pegando o medico
        String medicoSolicitante = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4));

        // pegando o crm
        String crmMedico = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 5));

        // pegando a modalidade
        String mod = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1));

        // abrindo a janela de laudo
        JFLaudo laudo = new JFLaudo(data2, paciente, handle_at, medicoSolicitante, crmMedico, mod);
        laudo.setVisible(true);
    }

    @SuppressWarnings("rawtypes")
    private void imprimirFicha() {
        // pegando o handle_at
        final int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                boolean abriuFicha = false;

                // abrindo impressao de ficha de acordo com o modelo de impressao
                if (janelaPrincipal.modeloDeImpressao == 1) {
                    ImprimirFichaDeAutorizacaoModelo1 imprimirFicha = new ImprimirFichaDeAutorizacaoModelo1(handle_at);
                    abriuFicha = imprimirFicha.salvarEImprimirFicha();
                } else if (janelaPrincipal.modeloDeImpressao == 2) {
                    ImprimirFichaEBoletoDeRetiradaModelo2 imprimirFicha =
                        new ImprimirFichaEBoletoDeRetiradaModelo2(handle_at);
                    abriuFicha = imprimirFicha.imprimir();
                } else if (janelaPrincipal.modeloDeImpressao == 3) {
                    ImprimirFichaEBoletoDeRetiradaModelo3 imprimirFicha =
                        new ImprimirFichaEBoletoDeRetiradaModelo3(handle_at);
                    abriuFicha = imprimirFicha.imprimir();
                } else if (janelaPrincipal.modeloDeImpressao == 4){
                    ImprimirFichaDeAutorizacaoModelo4 imprimirFicha = new ImprimirFichaDeAutorizacaoModelo4(handle_at);
                    abriuFicha = imprimirFicha.salvarEImprimirFicha();
                }

                // se deu tudo certo na impressao entra nesse if
                if (abriuFicha) {
                    // aqui colocar o flag_imprimiu como "S"
                    con = Conexao.fazConexao();
                    ATENDIMENTOS.setUpdateFlagImprimiu(con, handle_at);
                    if (ATENDIMENTOS.getMarcarStatus1(con, handle_at)) {
                        // verifica no banco se tem algum numero ja no status um. se tiver retorna false e se nao tiver
                        // retorna true
                        // se for true marcamos o status1 como 1 que imprimiu se nao soh imprimi e pronto
                        ATENDIMENTOS.setUpdateStatus1(con, handle_at, "1");
                    }
                    Conexao.fechaConexao(con);
                }
                return null;
            }

            @Override
            protected void done() {
                preenchendoTabela();
                janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
            }
        };

        worker.execute();
    }

    @SuppressWarnings("rawtypes")
    private void imprimirBoletoDeRetirada() {
        final int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                ImprimirBoletoDeRetiradaModelo1 imprimirBoletoDeRetirada =
                    new ImprimirBoletoDeRetiradaModelo1(handle_at);
                boolean abriuBoletoDeRetirada = imprimirBoletoDeRetirada.salvarEIMprimirBoletoDeRetirada();
                if (abriuBoletoDeRetirada) {
                    // aqui colocar o flag_imprimiu como "S"
                    con = Conexao.fazConexao();
                    ATENDIMENTOS.setUpdateFlagImprimiu(con, handle_at);
                    Conexao.fechaConexao(con);
                }
                return null;
            }

            @Override
            protected void done() {
                preenchendoTabela();
                janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
            }
        };

        worker.execute();
    }

    @SuppressWarnings("rawtypes")
    private void imprimirEiqueta() {
        final int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                boolean abriuEtiqueta = false;
                if (janelaPrincipal.modeloDeImpressao == 1 || janelaPrincipal.modeloDeImpressao == 4) {
                    ImprimirEtiquetaEnvelopeModelo1 imprimirEtiqueta = new ImprimirEtiquetaEnvelopeModelo1(handle_at);
                    abriuEtiqueta = imprimirEtiqueta.salvarEIMprimirEtiqueta();
                } else if (janelaPrincipal.modeloDeImpressao == 2 || janelaPrincipal.modeloDeImpressao == 3) {
                    ImprimirEtiquetaEnvelopeModelo2 imprimir = new ImprimirEtiquetaEnvelopeModelo2(handle_at);
                    abriuEtiqueta = imprimir.imprimir();
                }

                if (abriuEtiqueta) {
                    // aqui colocar o flag_imprimiu como "S"
                    con = Conexao.fazConexao();
                    ATENDIMENTOS.setUpdateFlagImprimiu(con, handle_at);
                    Conexao.fechaConexao(con);
                }
                return null;
            }

            @Override
            protected void done() {
                janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
            }
        };

        worker.execute();
    }

    @SuppressWarnings("rawtypes")
    public void imprimirCodigoDeBarras() {
        final int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                boolean abriuEtiqueta = false;
                ImprimirEtiquetaCodigoDeBarrasModelo2 imprimir = new ImprimirEtiquetaCodigoDeBarrasModelo2(handle_at);
                abriuEtiqueta = imprimir.writeFile();

                if (abriuEtiqueta) {
                    // aqui colocar o flag_imprimiu como "S"
                    con = Conexao.fazConexao();
                    ATENDIMENTOS.setUpdateFlagImprimiu(con, handle_at);
                    Conexao.fechaConexao(con);
                }
                return null;
            }

            @Override
            protected void done() {
                janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
            }
        };

        worker.execute();
    }

    ImageIcon iconExameEntregue = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/ExameEntregue.png"));
    ImageIcon iconeLaudoEntregue = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/LaudoEntregue.png"));
    ImageIcon iconeLaudoEExameEntregue = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/LaudoEExameEntregue.png"));

    private void abrirPopUpEntregue(MouseEvent evt) {

        // Paciente recebeu o exame
        JMenuItem exameEntregue = new JMenuItem("Exame Entregue ao Paciente", iconExameEntregue);
        exameEntregue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                entregaDeExame(1);
                jTable1.setValueAt(iconExameEntregue, jTable1.getSelectedRow(), 8);
            }
        });

        // Paciente recebeu o laudo
        JMenuItem laudoEntregue = new JMenuItem("Laudo Entregue ao Paciente", iconeLaudoEntregue);
        laudoEntregue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                entregaDeExame(2);
                jTable1.setValueAt(iconeLaudoEntregue, jTable1.getSelectedRow(), 8);
            }
        });

        // Paciente recebeu o exame e laudo
        JMenuItem laudoEExameEntregue = new JMenuItem("Exame/Laudo Entregue ao Paciente", iconeLaudoEExameEntregue);
        laudoEExameEntregue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                entregaDeExame(3);
                jTable1.setValueAt(iconeLaudoEExameEntregue, jTable1.getSelectedRow(), 8);
            }
        });

        // cria o menu popup e adiciona os itens
        JPopupMenu popup = new JPopupMenu();
        popup.add(exameEntregue);
        popup.add(laudoEntregue);
        popup.addSeparator();
        popup.add(laudoEExameEntregue);

        // mostra na tela
        int x = evt.getX();
        int y = evt.getY();
        popup.show(jTable1, x, y);
    }

    // parametro 1 marca entregou o exame, parametro dois marca que entregou laudo, parametro 3 entregou os dois
    @SuppressWarnings("rawtypes")
    private void entregaDeExame(final int parametro) {
        final int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                // aqui colocar o flag correspondentes como "S"
                con = Conexao.fazConexao();
                ATENDIMENTOS.setEntregaDeExame(con, parametro, handle_at);
                Conexao.fechaConexao(con);
                return null;
            }

            @Override
            protected void done() {

            }
        };

        worker.execute();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    public static javax.swing.JCheckBox jCBTodas;
    public static javax.swing.JCheckBox jCBcr;
    public static javax.swing.JCheckBox jCBct;
    public static javax.swing.JCheckBox jCBdo;
    public static javax.swing.JCheckBox jCBdr;
    public static javax.swing.JCheckBox jCBdx;
    public static javax.swing.JCheckBox jCBmg;
    public static javax.swing.JCheckBox jCBmr;
    public static javax.swing.JCheckBox jCBnm;
    public static javax.swing.JCheckBox jCBod;
    public static javax.swing.JCheckBox jCBot;
    public static javax.swing.JCheckBox jCBrf;
    public static javax.swing.JCheckBox jCBtr;
    public static javax.swing.JCheckBox jCBus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFPesquisaPaciente;
    private javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    // End of variables declaration//GEN-END:variables
}
