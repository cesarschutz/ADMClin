/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * flag de pintura 
 * 
 * 1 = agenda nao trabalha por qualquer motivo (sem agendamento) pinta de cinza escuro
 * 2 = linha de intervalo (sem agendamento) pinta de preto
 * 3 = pinta de azul (agendamento, ou atendimento)
 * 4 = pinta da cor de atendimento
 * 
 * 
 * 
 * flag de status
 * 
 * 1 = agendado
 * 2 = feito entrada de ficha
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agenda;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.bcn.admclin.ClasseAuxiliares.ColorirHorariosIndisponiveisNaAgendaELiberarIconesNaTabela;
import br.bcn.admclin.ClasseAuxiliares.ColorirLinhaJTableInicial;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.colorirIntervalosNaTabelaDeHorario;
import br.bcn.admclin.dao.AGENDAS;
import br.bcn.admclin.dao.ATENDIMENTOS;
import br.bcn.admclin.dao.ATENDIMENTO_EXAMES;
import br.bcn.admclin.dao.A_AGENDAMENTOS;
import br.bcn.admclin.dao.A_FERIADOS;
import br.bcn.admclin.dao.A_INTERVALOSDIARIOS;
import br.bcn.admclin.dao.A_INTERVALOSPORHORARIO;
import br.bcn.admclin.dao.A_INTERVALOSPORPERIODO;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.model.A_Agendamentos;
import br.bcn.admclin.dao.model.Atendimento_Exames;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 * 
 * @author Cesar Schutz
 */
public final class JIFUmaAgenda extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    public Icon iconeAgendado = new ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/menuAgendar.png"));
    public Icon iconeAgendadoExt = new ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/menuAgendarExtendido.png"));
    public Icon iconeAtendimento = new ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/menuAtendimento.png"));
    public Icon iconeAtendmentoExt = new ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/menuAtendimentoExtendido.png"));

    public static int handle_agenda;
    public static Connection con = null;
    int ultimoDiaDoMesSelecionado;
    // agendamento deleto (mais de um por causa dos exames, pode ser varias linhas
    public static List<A_Agendamentos> listaAgendamentoCanceladoPorUltimo = new ArrayList<A_Agendamentos>();

    /**
     * Creates new form JIFumaAgendaCorreto
     */
    @SuppressWarnings("static-access")
    public JIFUmaAgenda(int handle_agenda) {
        initComponents();
        this.handle_agenda = handle_agenda;
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse(jTable1);
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse(jTable2);
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse(jTable3);
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse(jTable4);
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse(jTable5);
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse(jTable6);

        definirEventoDaBarraDeRolagemDaTabela();

        tirandoBordaEBarraDeTitulo();

        montandoAsDatasNaAgenda();
        negritoNoDiaDeHoje();

        // arrumando tabelas
        // sumindo colunade flag de pintura da tabela de hroarios
        jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(1).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);

        arrumandoFormatoDasTabelas(jTable2);
        arrumandoFormatoDasTabelas(jTable3);
        arrumandoFormatoDasTabelas(jTable4);
        arrumandoFormatoDasTabelas(jTable5);
        arrumandoFormatoDasTabelas(jTable6);

        sumindoBarraDeRolagemDasTabelas(jScrollPane1);
        sumindoBarraDeRolagemDasTabelas(jScrollPane2);
        sumindoBarraDeRolagemDasTabelas(jScrollPane3);
        sumindoBarraDeRolagemDasTabelas(jScrollPane5);
        sumindoBarraDeRolagemDasTabelas(jScrollPane4);
        sumindoBarraDeRolagemDasTabelas(jScrollPane5);
        sumindoBarraDeRolagemDasTabelas(jScrollPane6);

        // aqui começamos a preencher de acordo com o bando de dados
        VerificandoHorariosEDiasDaAgenda();

        verificandoOsFeriadosDaAgenda(handle_agenda);
        verificandoOsFeriadosDaAgenda(0);

        verificandoOsIntervalosPorHorarioDaAgenda(handle_agenda);
        verificandoOsIntervalosPorHorarioDaAgenda(0);

        verificandoOsIntervalosDiarios(handle_agenda);
        verificandoOsIntervalosDiarios(0);

        verificarIntervalosPorPeriodo(handle_agenda);
        verificarIntervalosPorPeriodo(0);

        // pintando as tabelas
        pintandoAAgenda(jTable1);
        pintandoAAgenda(jTable2);
        pintandoAAgenda(jTable3);
        pintandoAAgenda(jTable4);
        pintandoAAgenda(jTable5);
        pintandoAAgenda(jTable6);

        preenchendoOsAtendimentosDeTodasAsTabelas();
        preenchendoOsAgendamentosDeTodasAsTabelas();
    }

    public void ativandoSelecaoDeLinhaComBotaoDireitoDoMouse(final JTable tabela) {
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3 && tabela != jTable1) {
                    int col = tabela.columnAtPoint(e.getPoint());
                    int row = tabela.rowAtPoint(e.getPoint());
                    if (col != -1 && row != -1) {
                        tabela.setColumnSelectionInterval(col, col);
                        tabela.setRowSelectionInterval(row, row);
                    }
                }

                if (tabela == jTable2) {
                    jTable3.clearSelection();
                    jTable4.clearSelection();
                    jTable5.clearSelection();
                    jTable6.clearSelection();

                    jTable1.setSelectionModel(jTable2.getSelectionModel());
                }

                if (tabela == jTable3) {
                    jTable2.clearSelection();
                    jTable4.clearSelection();
                    jTable5.clearSelection();
                    jTable6.clearSelection();

                    jTable1.setSelectionModel(jTable3.getSelectionModel());
                }

                if (tabela == jTable4) {
                    jTable2.clearSelection();
                    jTable3.clearSelection();
                    jTable5.clearSelection();
                    jTable6.clearSelection();

                    jTable1.setSelectionModel(jTable4.getSelectionModel());
                }

                if (tabela == jTable5) {
                    jTable2.clearSelection();
                    jTable3.clearSelection();
                    jTable4.clearSelection();
                    jTable6.clearSelection();

                    jTable1.setSelectionModel(jTable5.getSelectionModel());
                }

                if (tabela == jTable6) {
                    jTable2.clearSelection();
                    jTable3.clearSelection();
                    jTable4.clearSelection();
                    jTable5.clearSelection();

                    jTable1.setSelectionModel(jTable6.getSelectionModel());
                }

                // liberando para abrir o pop up
                liberarPopUp = true;

                // colocando a seleção na celula clicada
                int linhaSelecionada = tabela.getSelectedRow();
                int colunaSelecionada = tabela.getSelectedColumn();

                tabela.editCellAt(linhaSelecionada, colunaSelecionada);
            }
        });
    }

    private void definirEventoDaBarraDeRolagemDaTabela() {
        jScrollPane1.getVerticalScrollBar().setModel(jScrollBar1.getModel());
        jScrollPane2.getVerticalScrollBar().setModel(jScrollBar1.getModel());
        jScrollPane3.getVerticalScrollBar().setModel(jScrollBar1.getModel());
        jScrollPane4.getVerticalScrollBar().setModel(jScrollBar1.getModel());
        jScrollPane5.getVerticalScrollBar().setModel(jScrollBar1.getModel());
        jScrollPane6.getVerticalScrollBar().setModel(jScrollBar1.getModel());
    }

    public void tirandoBordaEBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void montandoAsDatasNaAgenda() {
        // verifica para saber ateh q dia vai o mes selecionado
        verificarAteQueDiaVaiOMesSelecionado();

        // pegando data selecionada
        java.util.Date dataSelecionada = JIFAgendaPrincipal.jXDatePicker1.getDate();
        // criando um formato de data
        SimpleDateFormat dataFormatada = new SimpleDateFormat("E dd/MM/yyyy");
        // pegando o mes selecionado
        String dataCorreta = dataFormatada.format(dataSelecionada);
        // colocando data na tabela 2
        jTable2.getColumnModel().getColumn(0).setHeaderValue(dataCorreta);

        // colocando data na tabela 3

        jTable3.getColumnModel().getColumn(0).setHeaderValue(verificandoProximaData(dataCorreta));

        // colocando data na tabela 4
        jTable4.getColumnModel().getColumn(0)
            .setHeaderValue(verificandoProximaData((String) jTable3.getColumnModel().getColumn(0).getHeaderValue()));

        // colocando data na tabela 5

        jTable5.getColumnModel().getColumn(0)
            .setHeaderValue(verificandoProximaData((String) jTable4.getColumnModel().getColumn(0).getHeaderValue()));

        // colocando data na tabela 6

        jTable6.getColumnModel().getColumn(0)
            .setHeaderValue(verificandoProximaData((String) jTable5.getColumnModel().getColumn(0).getHeaderValue()));

        jTable2.getTableHeader().resizeAndRepaint();
        jTable3.getTableHeader().resizeAndRepaint();
        jTable4.getTableHeader().resizeAndRepaint();
        jTable5.getTableHeader().resizeAndRepaint();
        jTable6.getTableHeader().resizeAndRepaint();
    }

    public void negritoNoDiaDeHoje() {
        try {
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");

            Date diaDaTabela2 =
                formatador.parse(String.valueOf(jTable2.getColumnModel().getColumn(0).getHeaderValue())
                    .substring(4, 14));
            Date diaDaTabela3 =
                formatador.parse(String.valueOf(jTable3.getColumnModel().getColumn(0).getHeaderValue())
                    .substring(4, 14));
            Date diaDaTabela4 =
                formatador.parse(String.valueOf(jTable4.getColumnModel().getColumn(0).getHeaderValue())
                    .substring(4, 14));
            Date diaDaTabela5 =
                formatador.parse(String.valueOf(jTable5.getColumnModel().getColumn(0).getHeaderValue())
                    .substring(4, 14));
            Date diaDaTabela6 =
                formatador.parse(String.valueOf(jTable6.getColumnModel().getColumn(0).getHeaderValue())
                    .substring(4, 14));

            Calendar hoje = Calendar.getInstance();
            String dataDeHojeString = formatador.format(hoje.getTime());
            Date dataDeHoje = formatador.parse(dataDeHojeString);

            if (dataDeHoje.compareTo(diaDaTabela2) == 0) {
                JTableHeader cabecalho = (JTableHeader) jTable2.getTableHeader();
                Font fonte = new Font("Tahoma", 1, 12);
                cabecalho.setFont(fonte);
            }

            if (dataDeHoje.compareTo(diaDaTabela3) == 0) {
                JTableHeader cabecalho = (JTableHeader) jTable3.getTableHeader();
                Font fonte = new Font("Tahoma", 1, 12);
                cabecalho.setFont(fonte);
            }

            if (dataDeHoje.compareTo(diaDaTabela4) == 0) {
                JTableHeader cabecalho = (JTableHeader) jTable4.getTableHeader();
                Font fonte = new Font("Tahoma", 1, 12);
                cabecalho.setFont(fonte);
            }

            if (dataDeHoje.compareTo(diaDaTabela5) == 0) {
                JTableHeader cabecalho = (JTableHeader) jTable5.getTableHeader();
                Font fonte = new Font("Tahoma", 1, 12);
                cabecalho.setFont(fonte);
            }

            if (dataDeHoje.compareTo(diaDaTabela6) == 0) {
                JTableHeader cabecalho = (JTableHeader) jTable6.getTableHeader();
                Font fonte = new Font("Tahoma", 1, 12);
                cabecalho.setFont(fonte);
            }
        } catch (ParseException e) {

        }

    }

    public void verificarAteQueDiaVaiOMesSelecionado() {

        java.util.Date dataSelecionada = JIFAgendaPrincipal.jXDatePicker1.getDate();
        // criando um formato de data
        SimpleDateFormat dataFormatada = new SimpleDateFormat("MM");
        // pegando o mes selecionado
        String mesSelecionado = dataFormatada.format(dataSelecionada);

        // pegando o ultimo dia do mes que foi selecionado
        if ("01".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 31;
        } else if ("02".equals(mesSelecionado)) {

            java.util.Date dataSelecionada1 = JIFAgendaPrincipal.jXDatePicker1.getDate();
            // criando um formato de data
            SimpleDateFormat anoFormatado = new SimpleDateFormat("yyyy");
            // pegando o mes selecionado
            int anoSelecionado = Integer.valueOf(anoFormatado.format(dataSelecionada1));

            double resto = anoSelecionado % 4;

            if (resto == 0) {
                ultimoDiaDoMesSelecionado = 29;
            } else {
                ultimoDiaDoMesSelecionado = 28;
            }

        } else if ("03".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 31;
        } else if ("04".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 30;
        } else if ("05".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 31;
        } else if ("06".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 30;
        } else if ("07".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 31;
        } else if ("08".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 31;
        } else if ("09".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 30;
        } else if ("10".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 31;
        } else if ("11".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 30;
        } else if ("12".equals(mesSelecionado)) {
            ultimoDiaDoMesSelecionado = 31;
        }

    }

    public String verificandoProximaData(String dataAnterior) {
        String diaDaSema = null;
        String dataNova = null;
        String[] diaEData = dataAnterior.split(" ");
        String[] dataSeparada = diaEData[1].split("/");
        int dia = Integer.valueOf(dataSeparada[0]);
        int mes = Integer.valueOf(dataSeparada[1]);
        int ano = Integer.valueOf(dataSeparada[2]);

        // montando data
        if (dia < ultimoDiaDoMesSelecionado) {
            dia++;
        } else {
            if (mes == 12) {
                dia = 01;
                mes = 01;
                ano++;
            } else {
                dia = 01;
                mes++;
            }
        }

        // concertando os numeros ( caso mes ou dia seja 1, ficara 01 ;)
        String diaString = String.valueOf(dia);
        String mesString = String.valueOf(mes);

        if (diaString.length() == 1) {
            diaString = "0" + diaString;
        }
        if (mesString.length() == 1) {
            mesString = "0" + mesString;
        }

        // colocando o dia da semana certo

        if ("Seg".equals(diaEData[0])) {
            diaDaSema = "Ter";
        } else if ("Ter".equals(diaEData[0])) {
            diaDaSema = "Qua";
        } else if ("Qua".equals(diaEData[0])) {
            diaDaSema = "Qui";
        } else if ("Qui".equals(diaEData[0])) {
            diaDaSema = "Sex";
        } else if ("Sex".equals(diaEData[0])) {
            diaDaSema = "Sáb";
        } else if ("Sáb".equals(diaEData[0])) {
            diaDaSema = "Dom";
        } else if ("Dom".equals(diaEData[0])) {
            diaDaSema = "Seg";
        } else if ("Mon".equals(diaEData[0])) {
            diaDaSema = "Tue";
        } else if ("Tue".equals(diaEData[0])) {
            diaDaSema = "Wed";
        } else if ("Wed".equals(diaEData[0])) {
            diaDaSema = "Thu";
        } else if ("Thu".equals(diaEData[0])) {
            diaDaSema = "Fri";
        } else if ("Fri".equals(diaEData[0])) {
            diaDaSema = "Sat";
        } else if ("Sat".equals(diaEData[0])) {
            diaDaSema = "Sun";
        } else if ("Sun".equals(diaEData[0])) {
            diaDaSema = "Mon";
        }

        // montando data nova
        dataNova = diaDaSema + " " + diaString + "/" + mesString + "/" + String.valueOf(ano);
        return dataNova;

    }

    public void arrumandoFormatoDasTabelas(JTable tabela) {
        // aumentando tamanho da linha
        tabela.setRowHeight(30);
        jTable1.setRowHeight(30);

        // selecionar somente uma linha na tabela
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // pintando as linhas
        TableCellRenderer tcr = new ColorirLinhaJTableInicial();

        TableColumn column = tabela.getColumnModel().getColumn(0);
        TableColumn columnTabela1 = jTable1.getColumnModel().getColumn(0);
        TableColumn column1 = tabela.getColumnModel().getColumn(1);

        column.setCellRenderer(tcr);
        columnTabela1.setCellRenderer(tcr);
        column1.setCellRenderer(tcr);

        // sumindo colunas de flags
        tabela.getColumnModel().getColumn(2).setMaxWidth(0);
        tabela.getColumnModel().getColumn(2).setMinWidth(0);
        tabela.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0);
        tabela.getTableHeader().getColumnModel().getColumn(2).setMinWidth(0);

        tabela.getColumnModel().getColumn(3).setMaxWidth(0);
        tabela.getColumnModel().getColumn(3).setMinWidth(0);
        tabela.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
        tabela.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);

        tabela.getColumnModel().getColumn(4).setMaxWidth(0);
        tabela.getColumnModel().getColumn(4).setMinWidth(0);
        tabela.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
        tabela.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);

        tabela.getColumnModel().getColumn(5).setMaxWidth(0);
        tabela.getColumnModel().getColumn(5).setMinWidth(0);
        tabela.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(0);
        tabela.getTableHeader().getColumnModel().getColumn(5).setMinWidth(0);

        // definindo tamaho das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(220);

        // aceitando icones na coluna 1
        // tabela.getColumn("Status").setCellRenderer(new jTableAceitandoIconesEmUmaColuna());

    }

    public void sumindoBarraDeRolagemDasTabelas(JScrollPane scrollPane) {
        // arrumando os scrollBars dos ScrollPanes
        Dimension vsb = scrollPane.getVerticalScrollBar().getPreferredSize();
        vsb.width = 0;
        scrollPane.getVerticalScrollBar().setPreferredSize(vsb);
    }

    public void VerificandoHorariosEDiasDaAgenda() {
        int horarioDeInicioTurno1 = 0, horarioDeInicioTurno2 = 0, horarioDeInicioTurno3 = 0, horarioDeInicioTurno4 = 0;
        int horarioFinalDaAgendaTurno1 = 0, horarioFinalDaAgendaTurno2 = 0, horarioFinalDaAgendaTurno3 = 0, horarioFinalDaAgendaTurno4 =
            0;
        int duracaoTurno1 = 0, duracaoTurno2 = 0, duracaoTurno3 = 0, duracaoTurno4 = 0;

        int seg = 0, ter = 0, qua = 0, qui = 0, sex = 0, sab = 0, dom = 0;

        con = Conexao.fazConexao();
        ResultSet resultSet = AGENDAS.getConsultarDadosDeUmaAgenda(con, handle_agenda);
        try {
            while (resultSet.next()) {
                // pegando horario de inicio
                horarioDeInicioTurno1 = resultSet.getInt("horarioinicialturno1");
                horarioFinalDaAgendaTurno1 = resultSet.getInt("horariofinalturno1");
                duracaoTurno1 = resultSet.getInt("duracaoturno1");

                horarioDeInicioTurno2 = resultSet.getInt("horarioinicialturno2");
                horarioFinalDaAgendaTurno2 = resultSet.getInt("horariofinalturno2");
                duracaoTurno2 = resultSet.getInt("duracaoturno2");

                horarioDeInicioTurno3 = resultSet.getInt("horarioinicialturno3");
                horarioFinalDaAgendaTurno3 = resultSet.getInt("horariofinalturno3");
                duracaoTurno3 = resultSet.getInt("duracaoturno3");

                horarioDeInicioTurno4 = resultSet.getInt("horarioinicialturno4");
                horarioFinalDaAgendaTurno4 = resultSet.getInt("horariofinalturno4");
                duracaoTurno4 = resultSet.getInt("duracaoturno4");

                jTextField1.setText(resultSet.getString("nome"));
                seg = resultSet.getInt("seg");
                ter = resultSet.getInt("ter");
                qua = resultSet.getInt("qua");
                qui = resultSet.getInt("qui");
                sex = resultSet.getInt("sex");
                sab = resultSet.getInt("sab");
                dom = resultSet.getInt("dom");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher a Agenda. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }

        construindoAsLinhasDaTabelaDeAcordoComAAgenda(jTable2, horarioDeInicioTurno1, horarioFinalDaAgendaTurno1,
            duracaoTurno1, horarioDeInicioTurno2, horarioFinalDaAgendaTurno2, duracaoTurno2, horarioDeInicioTurno3,
            horarioFinalDaAgendaTurno3, duracaoTurno3, horarioDeInicioTurno4, horarioFinalDaAgendaTurno4, duracaoTurno4);
        construindoAsLinhasDaTabelaDeAcordoComAAgenda(jTable3, horarioDeInicioTurno1, horarioFinalDaAgendaTurno1,
            duracaoTurno1, horarioDeInicioTurno2, horarioFinalDaAgendaTurno2, duracaoTurno2, horarioDeInicioTurno3,
            horarioFinalDaAgendaTurno3, duracaoTurno3, horarioDeInicioTurno4, horarioFinalDaAgendaTurno4, duracaoTurno4);
        construindoAsLinhasDaTabelaDeAcordoComAAgenda(jTable4, horarioDeInicioTurno1, horarioFinalDaAgendaTurno1,
            duracaoTurno1, horarioDeInicioTurno2, horarioFinalDaAgendaTurno2, duracaoTurno2, horarioDeInicioTurno3,
            horarioFinalDaAgendaTurno3, duracaoTurno3, horarioDeInicioTurno4, horarioFinalDaAgendaTurno4, duracaoTurno4);
        construindoAsLinhasDaTabelaDeAcordoComAAgenda(jTable5, horarioDeInicioTurno1, horarioFinalDaAgendaTurno1,
            duracaoTurno1, horarioDeInicioTurno2, horarioFinalDaAgendaTurno2, duracaoTurno2, horarioDeInicioTurno3,
            horarioFinalDaAgendaTurno3, duracaoTurno3, horarioDeInicioTurno4, horarioFinalDaAgendaTurno4, duracaoTurno4);
        construindoAsLinhasDaTabelaDeAcordoComAAgenda(jTable6, horarioDeInicioTurno1, horarioFinalDaAgendaTurno1,
            duracaoTurno1, horarioDeInicioTurno2, horarioFinalDaAgendaTurno2, duracaoTurno2, horarioDeInicioTurno3,
            horarioFinalDaAgendaTurno3, duracaoTurno3, horarioDeInicioTurno4, horarioFinalDaAgendaTurno4, duracaoTurno4);

        // marcando flag nos dias que a agenda nao funciona
        if (seg == 0) {
            marcandoFlagNosDiasQueAAgendaNaoFunciona("Seg");
        }

        if (ter == 0) {
            marcandoFlagNosDiasQueAAgendaNaoFunciona("Ter");
        }

        if (qua == 0) {
            marcandoFlagNosDiasQueAAgendaNaoFunciona("Qua");
        }

        if (qui == 0) {
            marcandoFlagNosDiasQueAAgendaNaoFunciona("Qui");
        }

        if (sex == 0) {
            marcandoFlagNosDiasQueAAgendaNaoFunciona("Sex");
        }

        if (sab == 0) {
            marcandoFlagNosDiasQueAAgendaNaoFunciona("Sáb");
        }

        if (dom == 0) {
            marcandoFlagNosDiasQueAAgendaNaoFunciona("Dom");
        }

        marcandoFlagNasLinhasDeIntervaloDaAgenda();

    }

    public void construindoAsLinhasDaTabelaDeAcordoComAAgenda(JTable tabela, int horarioDeInicioTurno1,
        int horarioFinalDaAgendaTurno1, int duracaoTurno1, int horarioDeInicioTurno2, int horarioFinalDaAgendaTurno2,
        int duracaoTurno2, int horarioDeInicioTurno3, int horarioFinalDaAgendaTurno3, int duracaoTurno3,
        int horarioDeInicioTurno4, int horarioFinalDaAgendaTurno4, int duracaoTurno4) {
        // contruindo turno 1
        if (horarioDeInicioTurno1 != 0) {
            // zerando linhas da tabela 1
            ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
            jTable1.updateUI();
            // pegando o modelo da tabela 1
            DefaultTableModel modelo1 = (DefaultTableModel) jTable1.getModel();
            // montando as linhas da tabela 1
            int i = horarioDeInicioTurno1;
            while (i < horarioFinalDaAgendaTurno1) {
                modelo1.addRow(new String[] { MetodosUteis.transformarMinutosEmHorario(i) });
                i = i + duracaoTurno1;
            }

            // zerando linhas da tabela enviada por parametro
            ((DefaultTableModel) tabela.getModel()).setNumRows(0);
            tabela.updateUI();
            // pegando o modelo da tabela enviada por parametro
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();

            // construindo linhas da tabela enviada por parametro
            int j = horarioDeInicioTurno1;
            while (j < horarioFinalDaAgendaTurno1) {
                modelo.addRow(new String[] {});
                j = j + duracaoTurno1;
            }

        }
        // contruindo turno 2
        if (horarioDeInicioTurno2 != 0) {
            // pegando o modelo da tabela 1
            DefaultTableModel modelo1 = (DefaultTableModel) jTable1.getModel();
            // criando linha vazia que sera para o intervalo
            modelo1.addRow(new String[] {});
            // montando as linhas da tabela 1
            int i = horarioDeInicioTurno2;
            while (i < horarioFinalDaAgendaTurno2) {
                modelo1.addRow(new String[] { MetodosUteis.transformarMinutosEmHorario(i) });
                i = i + duracaoTurno2;
            }

            // pegando o modelo da tabela enviada por parametro
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
            // criando linha vazia que sera para o intervalo
            modelo.addRow(new String[] {});
            // construindo linhas da tabela enviada por parametro
            int j = horarioDeInicioTurno2;
            while (j < horarioFinalDaAgendaTurno2) {
                modelo.addRow(new String[] {});
                j = j + duracaoTurno2;
            }

        }
        // construindo turno 3
        if (horarioDeInicioTurno3 != 0) {
            // pegando o modelo da tabela 1
            DefaultTableModel modelo1 = (DefaultTableModel) jTable1.getModel();
            // criando linha vazia que sera para o intervalo
            modelo1.addRow(new String[] {});
            // montando as linhas da tabela 1
            int i = horarioDeInicioTurno3;
            while (i < horarioFinalDaAgendaTurno3) {
                modelo1.addRow(new String[] { MetodosUteis.transformarMinutosEmHorario(i) });
                i = i + duracaoTurno3;
            }

            // pegando o modelo da tabela enviada por parametro
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
            // criando linha vazia que sera para o intervalo
            modelo.addRow(new String[] {});
            // construindo linhas da tabela enviada por parametro
            int j = horarioDeInicioTurno3;
            while (j < horarioFinalDaAgendaTurno3) {
                modelo.addRow(new String[] {});
                j = j + duracaoTurno3;
            }

        }
        // construindo turno 4
        if (horarioDeInicioTurno4 != 0) {
            // pegando o modelo da tabela 1
            DefaultTableModel modelo1 = (DefaultTableModel) jTable1.getModel();
            // criando linha vazia que sera para o intervalo
            modelo1.addRow(new String[] {});
            // montando as linhas da tabela 1
            int i = horarioDeInicioTurno4;
            while (i < horarioFinalDaAgendaTurno4) {
                modelo1.addRow(new String[] { MetodosUteis.transformarMinutosEmHorario(i) });
                i = i + duracaoTurno4;
            }

            // pegando o modelo da tabela enviada por parametro
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
            // criando linha vazia que sera para o intervalo
            modelo.addRow(new String[] {});
            // construindo linhas da tabela enviada por parametro
            int j = horarioDeInicioTurno4;
            while (j < horarioFinalDaAgendaTurno4) {
                modelo.addRow(new String[] {});
                j = j + duracaoTurno4;
            }

        }

    }

    public void marcandoFlagNosDiasQueAAgendaNaoFunciona(String diaDaSemana) {

        int numeroDeLinhasDaTabela = jTable1.getModel().getRowCount();

        String diaDaTabela2 = String.valueOf(jTable2.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);
        String diaDaTabela3 = String.valueOf(jTable3.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);
        String diaDaTabela4 = String.valueOf(jTable4.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);
        String diaDaTabela5 = String.valueOf(jTable5.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);
        String diaDaTabela6 = String.valueOf(jTable6.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);

        if (diaDaTabela2.equals(diaDaSemana)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // flag na coluna 3
                jTable2.setValueAt("1", i, 3);
                i++;
            }
        }

        if (diaDaTabela3.equals(diaDaSemana)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // flag na coluna 3
                jTable3.setValueAt("1", i, 3);
                i++;
            }
        }

        if (diaDaTabela4.equals(diaDaSemana)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // flag na coluna 3
                jTable4.setValueAt("1", i, 3);
                i++;
            }
        }

        if (diaDaTabela5.equals(diaDaSemana)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // flag na coluna 3
                jTable5.setValueAt("1", i, 3);
                i++;
            }
        }

        if (diaDaTabela6.equals(diaDaSemana)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // flag na coluna 3
                jTable6.setValueAt("1", i, 3);
                i++;
            }
        }
    }

    public void marcandoFlagNasLinhasDeIntervaloDaAgenda() {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String ValorDaCelula = (String) jTable1.getValueAt(i, 0);
            if (ValorDaCelula == null || "".equals(ValorDaCelula)) {
                jTable1.setValueAt("2", i, 1);
                jTable1.setRowHeight(i, 1);

                jTable2.setValueAt("2", i, 3);
                jTable2.setRowHeight(i, 1);

                jTable3.setValueAt("2", i, 3);
                jTable3.setRowHeight(i, 1);

                jTable4.setValueAt("2", i, 3);
                jTable4.setRowHeight(i, 1);

                jTable5.setValueAt("2", i, 3);
                jTable5.setRowHeight(i, 1);

                jTable6.setValueAt("2", i, 3);
                jTable6.setRowHeight(i, 1);
            }
        }
    }

    public void verificandoOsFeriadosDaAgenda(int handle_agenda) {
        // verificando feriados dessa agenda
        con = Conexao.fazConexao();
        ResultSet resultSet = A_FERIADOS.getConsultarFeriadosPorAgenda(con, handle_agenda);
        try {
            while (resultSet.next()) {
                marcandoFlagNosFeriados(resultSet.getString("diadoferiado"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel verificar Feriados desta Agenda. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    public void marcandoFlagNosFeriados(String diaDoFeriado) {

        int numeroDeLinhasDaTabela = jTable1.getModel().getRowCount();
        String feriado = diaDoFeriado;

        // aqui estamos pintando caso seja feriado
        String diaDaTabela2 = String.valueOf(jTable2.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 9);
        String diaDaTabela3 = String.valueOf(jTable3.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 9);
        String diaDaTabela4 = String.valueOf(jTable4.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 9);
        String diaDaTabela5 = String.valueOf(jTable5.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 9);
        String diaDaTabela6 = String.valueOf(jTable6.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 9);

        if (diaDaTabela2.equals(feriado)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // se nao for linha de intervalo
                if ((String) jTable1.getValueAt(i, 0) != null && !"".equals((String) jTable1.getValueAt(i, 0))) {
                    // flag na coluna 3
                    jTable2.setValueAt("1", i, 3);
                }
                i++;
            }
        }

        if (diaDaTabela3.equals(feriado)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // se nao for linha de intervalo
                if ((String) jTable1.getValueAt(i, 0) != null && !"".equals((String) jTable1.getValueAt(i, 0))) {
                    // flag na coluna 3
                    jTable3.setValueAt("1", i, 3);
                }
                i++;
            }
        }

        if (diaDaTabela4.equals(feriado)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // se nao for linha de intervalo
                if ((String) jTable1.getValueAt(i, 0) != null && !"".equals((String) jTable1.getValueAt(i, 0))) {
                    // flag na coluna 3
                    jTable4.setValueAt("1", i, 3);
                }
                i++;
            }
        }

        if (diaDaTabela5.equals(feriado)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // se nao for linha de intervalo
                if ((String) jTable1.getValueAt(i, 0) != null && !"".equals((String) jTable1.getValueAt(i, 0))) {
                    // flag na coluna 3
                    jTable5.setValueAt("1", i, 3);
                }
                i++;
            }
        }

        if (diaDaTabela6.equals(feriado)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {
                // se nao for linha de intervalo
                if ((String) jTable1.getValueAt(i, 0) != null && !"".equals((String) jTable1.getValueAt(i, 0))) {
                    // flag na coluna 3
                    jTable6.setValueAt("1", i, 3);
                }
                i++;
            }
        }

    }

    public void verificandoOsIntervalosPorHorarioDaAgenda(int idAgenda) {
        int horarioInicial = 0;
        int horarioFinal = 0;

        // verificando intervalos por horario dessa agenda
        con = Conexao.fazConexao();
        ResultSet resultSet = A_INTERVALOSPORHORARIO.getConsultarIntervalosPorHorarioPorAgenda(con, idAgenda);
        try {
            while (resultSet.next()) {

                horarioInicial = resultSet.getInt("HORARIOINICIAL");
                horarioFinal = resultSet.getInt("HORARIOFINAL");

                if (resultSet.getInt("SEG") == 1) {
                    marcandoFlagNosIntervalosPorHorario("Seg", horarioInicial, horarioFinal);
                }

                if (resultSet.getInt("TER") == 1) {
                    marcandoFlagNosIntervalosPorHorario("Ter", horarioInicial, horarioFinal);
                }

                if (resultSet.getInt("QUA") == 1) {
                    marcandoFlagNosIntervalosPorHorario("Qua", horarioInicial, horarioFinal);
                }

                if (resultSet.getInt("QUI") == 1) {
                    marcandoFlagNosIntervalosPorHorario("Qui", horarioInicial, horarioFinal);
                }

                if (resultSet.getInt("SEX") == 1) {
                    marcandoFlagNosIntervalosPorHorario("Sex", horarioInicial, horarioFinal);
                }

                if (resultSet.getInt("SAB") == 1) {
                    marcandoFlagNosIntervalosPorHorario("Sáb", horarioInicial, horarioFinal);
                }

                if (resultSet.getInt("DOM") == 1) {
                    marcandoFlagNosIntervalosPorHorario("Dom", horarioInicial, horarioFinal);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel verificar Intervalos por Horário desta Agenda. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    public void marcandoFlagNosIntervalosPorHorario(String diaDaSemanaDoIntervalo, int horarioInicialDoIntervalo,
        int horarioFinalDoIntervalo) {
        String dia = diaDaSemanaDoIntervalo;
        int horaInicial = horarioInicialDoIntervalo;
        int horaFinal = horarioFinalDoIntervalo;

        int numeroDeLinhasDaTabela = jTable1.getModel().getRowCount();

        String diaDaTabela2 = String.valueOf(jTable2.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);
        String diaDaTabela3 = String.valueOf(jTable3.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);
        String diaDaTabela4 = String.valueOf(jTable4.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);
        String diaDaTabela5 = String.valueOf(jTable5.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);
        String diaDaTabela6 = String.valueOf(jTable6.getColumnModel().getColumn(0).getHeaderValue()).substring(0, 3);

        if (diaDaTabela2.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable2.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }

        if (diaDaTabela3.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable3.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }

        if (diaDaTabela4.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable4.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }

        if (diaDaTabela5.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable5.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }

        if (diaDaTabela6.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable6.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }
    }

    public void verificandoOsIntervalosDiarios(int idAgenda) {
        int horarioInicial = 0, horarioFinal = 0;
        java.util.Date diaDoIntervalo = null;

        // verificando intervalos diarios dessa agenda
        con = Conexao.fazConexao();
        ResultSet resultSet = A_INTERVALOSDIARIOS.getConsultarIntervalosDiariosPorAgenda(con, idAgenda);
        try {
            while (resultSet.next()) {
                horarioInicial = resultSet.getInt("HORARIOINICIAL");
                horarioFinal = resultSet.getInt("HORARIOFINAL");
                diaDoIntervalo = resultSet.getDate("DIADOINTERVALO");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String diaDoIntervaloString = simpleDateFormat.format(diaDoIntervalo);

                marcandoFlagNosIntervalosDiarios(diaDoIntervaloString, horarioInicial, horarioFinal);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel verificar Intervalos Diários desta Agenda. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);

    }

    public void marcandoFlagNosIntervalosDiarios(String diaDoIntervalo, int horaInicialDoIntervalo,
        int horaFinalDoIntervalo) {

        String dia = diaDoIntervalo;
        int horaInicial = horaInicialDoIntervalo;
        int horaFinal = horaFinalDoIntervalo;

        int numeroDeLinhasDaTabela = jTable1.getModel().getRowCount();

        String diaDaTabela2 = String.valueOf(jTable2.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
        String diaDaTabela3 = String.valueOf(jTable3.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
        String diaDaTabela4 = String.valueOf(jTable4.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
        String diaDaTabela5 = String.valueOf(jTable5.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
        String diaDaTabela6 = String.valueOf(jTable6.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);

        if (diaDaTabela2.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable2.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }

        if (diaDaTabela3.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable3.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }

        if (diaDaTabela4.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable4.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }

        if (diaDaTabela5.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable5.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }

        if (diaDaTabela6.equals(dia)) {
            int i = 0;
            while (i < numeroDeLinhasDaTabela) {

                int minutosLinha0 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                int minutosLinha1 =
                    Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                int duracao = minutosLinha1 - minutosLinha0;

                String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                    int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                    if (horaDaLinhaEmMinutos + (duracao - 1) >= horaInicial && horaDaLinhaEmMinutos < horaFinal) {
                        // flag na coluna 3
                        jTable6.setValueAt("1", i, 3);
                    }
                }
                i++;
            }
        }

    }

    public void verificarIntervalosPorPeriodo(int idAgenda) {
        Date dataInicial = null, dataFinal = null;
        int horarioInicial, horarioFinal;

        // verificando intervalos diarios dessa agenda
        con = Conexao.fazConexao();
        ResultSet resultSet = A_INTERVALOSPORPERIODO.getConsultarIntervalosPorPeriodoPorAgenda(con, idAgenda);
        try {
            while (resultSet.next()) {
                // pegando informações do banco de dados
                horarioInicial = resultSet.getInt("HORARIOINICIAL");
                horarioFinal = resultSet.getInt("HORARIOFINAL");

                // pegando as datas do banco e formatando!
                SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
                String diaInicial = formatador.format(resultSet.getDate("DIAINICIAL"));
                String diaFinal = formatador.format(resultSet.getDate("DIAFINAL"));
                try {
                    dataInicial = formatador.parse(diaInicial);
                    dataFinal = formatador.parse(diaFinal);

                    // marcando os flag's
                    marcandoFlagNosIntervalosPorPeriodo(horarioInicial, horarioFinal, dataInicial, dataFinal);

                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null,
                        "Não foi possivel verificar Intervalos por Período desta Agenda. Procure o administrador",
                        "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                // marcandoFlagNosIntervalosDiarios(diaDoIntervaloString, horarioInicial, horarioFinal);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel verificar Intervalos por Período desta Agenda. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    public void marcandoFlagNosIntervalosPorPeriodo(int horarioInicial, int horarioFinal, Date dataInicial,
        Date dataFinal) {

        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");

        String diaDaTabela2 = String.valueOf(jTable2.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
        String diaDaTabela3 = String.valueOf(jTable3.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
        String diaDaTabela4 = String.valueOf(jTable4.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
        String diaDaTabela5 = String.valueOf(jTable5.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
        String diaDaTabela6 = String.valueOf(jTable6.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);

        // variavel para verificar datas
        // se valor for 1, primeiro dia é maior que o segundo
        // se for 0, datas iguais
        // se for -1 a primeira data eh menor que a segunda
        int comparaDataInicial;
        int comparaDataFinal;

        try {
            // colocando as dastas no formato certo
            Date diaDaTabela2Formatado = formatador.parse(diaDaTabela2);
            Date diaDaTabela3Formatado = formatador.parse(diaDaTabela3);
            Date diaDaTabela4Formatado = formatador.parse(diaDaTabela4);
            Date diaDaTabela5Formatado = formatador.parse(diaDaTabela5);
            Date diaDaTabela6Formatado = formatador.parse(diaDaTabela6);

            // marcando os falgs tabela 2
            comparaDataInicial = diaDaTabela2Formatado.compareTo(dataInicial);
            comparaDataFinal = diaDaTabela2Formatado.compareTo(dataFinal);

            if (comparaDataInicial == 0) {
                // marca flag em todos os horarios depois do horarioInicial
                for (int i = 0; i < jTable2.getRowCount(); i++) {
                    // vendo a duração da agenda
                    int minutosLinha0 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                    int minutosLinha1 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                    int duracao = minutosLinha1 - minutosLinha0;

                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos + (duracao - 1) >= horarioInicial) {
                            // flag na coluna 3
                            jTable2.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataFinal == 0) {
                // marca flag em todos os horarios antes do horarioFinal
                for (int i = 0; i < jTable2.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos < horarioFinal) {
                            // flag na coluna 3
                            jTable2.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataInicial == 1 && comparaDataFinal == -1) {
                // marca flag em todas as linhas
                for (int i = 0; i < jTable2.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        jTable2.setValueAt("1", i, 3);
                    }
                }
            }

            // marcando os falgs tabela 3
            comparaDataInicial = diaDaTabela3Formatado.compareTo(dataInicial);
            comparaDataFinal = diaDaTabela3Formatado.compareTo(dataFinal);

            if (comparaDataInicial == 0) {
                // marca flag em todos os horarios depois do horarioInicial
                for (int i = 0; i < jTable3.getRowCount(); i++) {
                    // vendo a duração da agenda
                    int minutosLinha0 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                    int minutosLinha1 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                    int duracao = minutosLinha1 - minutosLinha0;

                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos + (duracao - 1) >= horarioInicial) {
                            // flag na coluna 3
                            jTable3.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataFinal == 0) {
                // marca flag em todos os horarios antes do horarioFinal
                for (int i = 0; i < jTable3.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos < horarioFinal) {
                            // flag na coluna 3
                            jTable3.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataInicial == 1 && comparaDataFinal == -1) {
                // marca flag em todas as linhas
                for (int i = 0; i < jTable3.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        jTable3.setValueAt("1", i, 3);
                    }
                }
            }

            // marcando os falgs tabela 4
            comparaDataInicial = diaDaTabela4Formatado.compareTo(dataInicial);
            comparaDataFinal = diaDaTabela4Formatado.compareTo(dataFinal);

            if (comparaDataInicial == 0) {
                // marca flag em todos os horarios depois do horarioInicial
                for (int i = 0; i < jTable4.getRowCount(); i++) {
                    // vendo a duração da agenda
                    int minutosLinha0 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                    int minutosLinha1 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                    int duracao = minutosLinha1 - minutosLinha0;

                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos + (duracao - 1) >= horarioInicial) {
                            // flag na coluna 3
                            jTable4.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataFinal == 0) {
                // marca flag em todos os horarios antes do horarioFinal
                for (int i = 0; i < jTable4.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos < horarioFinal) {
                            // flag na coluna 3
                            jTable4.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataInicial == 1 && comparaDataFinal == -1) {
                // marca flag em todas as linhas
                for (int i = 0; i < jTable3.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        jTable4.setValueAt("1", i, 3);
                    }
                }
            }

            // marcando os falgs tabela 5
            comparaDataInicial = diaDaTabela5Formatado.compareTo(dataInicial);
            comparaDataFinal = diaDaTabela5Formatado.compareTo(dataFinal);

            if (comparaDataInicial == 0) {
                // marca flag em todos os horarios depois do horarioInicial
                for (int i = 0; i < jTable5.getRowCount(); i++) {
                    // vendo a duração da agenda
                    int minutosLinha0 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                    int minutosLinha1 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                    int duracao = minutosLinha1 - minutosLinha0;

                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos + (duracao - 1) >= horarioInicial) {
                            // flag na coluna 3
                            jTable5.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataFinal == 0) {
                // marca flag em todos os horarios antes do horarioFinal
                for (int i = 0; i < jTable5.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos < horarioFinal) {
                            // flag na coluna 3
                            jTable5.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataInicial == 1 && comparaDataFinal == -1) {
                // marca flag em todas as linhas
                for (int i = 0; i < jTable5.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        jTable5.setValueAt("1", i, 3);
                    }
                }
            }

            // marcando os falgs tabela 6
            comparaDataInicial = diaDaTabela6Formatado.compareTo(dataInicial);
            comparaDataFinal = diaDaTabela6Formatado.compareTo(dataFinal);

            if (comparaDataInicial == 0) {
                // marca flag em todos os horarios depois do horarioInicial
                for (int i = 0; i < jTable6.getRowCount(); i++) {
                    // vendo a duração da agenda
                    int minutosLinha0 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 0)));
                    int minutosLinha1 =
                        Integer.valueOf(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 0)));
                    int duracao = minutosLinha1 - minutosLinha0;

                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos + (duracao - 1) >= horarioInicial) {
                            // flag na coluna 3
                            jTable6.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataFinal == 0) {
                // marca flag em todos os horarios antes do horarioFinal
                for (int i = 0; i < jTable6.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        int horaDaLinhaEmMinutos = MetodosUteis.transformarHorarioEmMinutos(horaDaLinha);
                        if (horaDaLinhaEmMinutos < horarioFinal) {
                            // flag na coluna 3
                            jTable6.setValueAt("1", i, 3);
                        }
                    }
                }
            } else if (comparaDataInicial == 1 && comparaDataFinal == -1) {
                // marca flag em todas as linhas
                for (int i = 0; i < jTable6.getRowCount(); i++) {
                    String horaDaLinha = (String) jTable1.getValueAt(i, 0);
                    if (horaDaLinha != null && !"".equals(horaDaLinha)) {
                        jTable6.setValueAt("1", i, 3);
                    }
                }
            }

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel verificar Intervalos por Período desta Agenda. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    public void pintandoAAgenda(JTable tabela) {
        // utilizam classes de pinturas diferentes pois sao celulas diferentes do flag (flag utilizado para pintar)
        if (tabela == jTable1) {
            TableCellRenderer tcrColuna0 = new colorirIntervalosNaTabelaDeHorario();
            TableColumn column0 = tabela.getColumnModel().getColumn(0);
            column0.setCellRenderer(tcrColuna0);
        } else {
            // pintando primeiro dia
            TableCellRenderer tcrColuna0 = new ColorirHorariosIndisponiveisNaAgendaELiberarIconesNaTabela();
            TableColumn column0 = tabela.getColumnModel().getColumn(0);
            column0.setCellRenderer(tcrColuna0);

            TableCellRenderer tcrColuna1 = new ColorirHorariosIndisponiveisNaAgendaELiberarIconesNaTabela();
            TableColumn column1 = tabela.getColumnModel().getColumn(1);
            column1.setCellRenderer(tcrColuna1);
        }

    }

    public static List<A_Agendamentos> listaAgendamentosDaAgenda = new ArrayList<A_Agendamentos>();

    public static void preenchendoOsAgendamentosDestaAgenda(JTable tabelaSelecionada, Icon iconeAgendamento,
        Icon iconeAgendamentoExt, Connection con) {

        // pegando a data para verificar agendamentos
        java.sql.Date diaParaVerificarAgendamentos = null;
        try {
            String diaDaTabela =
                String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            diaParaVerificarAgendamentos = new java.sql.Date(format.parse(diaDaTabela).getTime());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Erro com a data. Procure o Administrador.");
        }

        ResultSet resultSet = A_AGENDAMENTOS.getConsultarAgendamentos(con, handle_agenda, diaParaVerificarAgendamentos);
        try {
            while (resultSet.next()) {
                // colocando esse agendamento na lista(para termos algumas informações em memoria)
                A_Agendamentos agendamento = new A_Agendamentos();
                agendamento.setHORA(resultSet.getInt("hora"));
                agendamento.setNomePaciente(resultSet.getString("nomePaciente"));
                agendamento.setNascimento(resultSet.getDate("nascimento"));
                agendamento.setTelefone(resultSet.getString("telefone"));
                agendamento.setCelular(resultSet.getString("celular"));
                agendamento.setNomeExame(resultSet.getString("nomeExame"));
                agendamento.setDURACAODOEXAME(resultSet.getInt("duracaoDoExame"));
                agendamento.setDia(resultSet.getDate("dia"));
                agendamento.setHANDLE_AP(resultSet.getInt("handle_ap"));
                agendamento.setDuracaoAgendamento(resultSet.getInt("duracaoDoAgendamento"));
                agendamento.setNomeConvenio(resultSet.getString("nomeConvenio"));
                agendamento.setLado(resultSet.getString("lado"));
                agendamento.setMaterial(resultSet.getString("material"));
                listaAgendamentosDaAgenda.add(agendamento);

                marcandoNomeNaLinhaDoAgendamento(tabelaSelecionada, resultSet.getString("nomePaciente"),
                    resultSet.getInt("handle_ap"), resultSet.getInt("hora"), iconeAgendamento, iconeAgendamentoExt,
                    resultSet.getInt("duracaoDoAgendamento"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher os Agendamentos. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void marcandoNomeNaLinhaDoAgendamento(JTable tabelaSelecionada, String nomePaciente, int handle_ap,
        int horarioDoAgendamento, Icon iconeAgendamento, Icon iconeAgendamentoExt, int duracaoDoAgendamento) {

        // precorrendo as linhas da tabela
        for (int i = 0; i < tabelaSelecionada.getRowCount(); i++) {
            try {
                // se linha for no mesmo horario inicial do agendamento && nao eh um atendimento "4", marca ele na linha
                // e percorre as proximas (para pintar se necessario)
                if (MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(i, 0)) == horarioDoAgendamento
                    && !"4".equals((String) tabelaSelecionada.getValueAt(i, 3))
                    && !"1".equals((String) tabelaSelecionada.getValueAt(i, 3))) {
                    // preenchendo nome e flag de status
                    tabelaSelecionada.setValueAt(nomePaciente.toUpperCase(), i, 0);
                    tabelaSelecionada.setValueAt("1", i, 2);
                    tabelaSelecionada.setValueAt(handle_ap, i, 4);
                    tabelaSelecionada.setValueAt(iconeAgendamento, i, 1);

                    // se nao for reservado pinta as proximas linhas de acordo com a necessidade E Pinta a linha de azul
                    if (!"*".equals(nomePaciente.toUpperCase())) {
                        marcandoFlagDePinturaNasProximasLinhas(tabelaSelecionada, duracaoDoAgendamento,
                            horarioDoAgendamento, i, iconeAgendamentoExt);
                        // pintamos aqui para nao pintar linha que seja reservada
                        tabelaSelecionada.setValueAt("3", i, 3);
                    } else {
                        tabelaSelecionada.setValueAt("", i, 1);
                    }

                }
            } catch (Exception e) {

            }
        }
    }

    public static void marcandoFlagDePinturaNasProximasLinhas(JTable tabelaSelecionada, int duracaoDoAgendamento,
        int horarioInicioDoAgendamento, int linhaDoAgendamento, Icon iconeAgendamentoExt) {
        for (int i = 0; i < tabelaSelecionada.getRowCount(); i++) {
            // try e catch para se a linha for intervalo passar para a proxima (pq dara erro)
            try {
                // se a duração do exame for atingir essa linha (de acordo como o horario dessa linha)
                int horarioDaLinhaEmminutos =
                    MetodosUteis.transformarHorarioEmMinutos((String) JIFUmaAgenda.jTable1.getValueAt(i, 0));
                int finalDoAgendamento = horarioInicioDoAgendamento + duracaoDoAgendamento;

                // if(se horario da linha for menor que o final do agendamento e maior que o inicio) entra pq a linha
                // afeta esse agendamento
                if ((horarioDaLinhaEmminutos < finalDoAgendamento && horarioDaLinhaEmminutos > horarioInicioDoAgendamento)) {
                    // se na linha tiverum um atendimento "4" para de verificar as linhas;
                    if ("4".equals((String) tabelaSelecionada.getValueAt(i, 3))) {
                        break;
                    }
                    // if linha nao for intervalo ou troca de turno pinta de azul
                    if (tabelaSelecionada.getValueAt(i, 3) != "2" && tabelaSelecionada.getValueAt(i, 3) != "1") {
                        // colocando flag de pintura azul "3"
                        tabelaSelecionada.setValueAt("3", i, 3);
                        // trocando o icone do agendamento para extendido
                        tabelaSelecionada.setValueAt(iconeAgendamentoExt, linhaDoAgendamento, 1);
                    }
                }
                // if horario da linha for maior que o horario final do agendamento && nao é um atendimento
                if (horarioDaLinhaEmminutos >= finalDoAgendamento && tabelaSelecionada.getValueAt(i, 3) != "4") {
                    tabelaSelecionada.setValueAt("", i, 3);
                }
            } catch (Exception e) {
            }
        }

        TableCellRenderer tcrColuna0 = new ColorirHorariosIndisponiveisNaAgendaELiberarIconesNaTabela();
        TableColumn column0 = tabelaSelecionada.getColumnModel().getColumn(0);
        column0.setCellRenderer(tcrColuna0);

        TableCellRenderer tcrColuna1 = new ColorirHorariosIndisponiveisNaAgendaELiberarIconesNaTabela();
        TableColumn column1 = tabelaSelecionada.getColumnModel().getColumn(1);
        column1.setCellRenderer(tcrColuna1);
    }

    public void preenchendoOsAgendamentosDeTodasAsTabelas() {
        listaAgendamentosDaAgenda.clear();
        con = Conexao.fazConexao();
        preenchendoOsAgendamentosDestaAgenda(jTable2, iconeAgendado, iconeAgendadoExt, con);
        preenchendoOsAgendamentosDestaAgenda(jTable3, iconeAgendado, iconeAgendadoExt, con);
        preenchendoOsAgendamentosDestaAgenda(jTable4, iconeAgendado, iconeAgendadoExt, con);
        preenchendoOsAgendamentosDestaAgenda(jTable5, iconeAgendado, iconeAgendadoExt, con);
        preenchendoOsAgendamentosDestaAgenda(jTable6, iconeAgendado, iconeAgendadoExt, con);
        Conexao.fechaConexao(con);
    }

    public static List<Atendimento_Exames> listaAtendimentoExamesDaAgenda = new ArrayList<Atendimento_Exames>();

    public static void preenchendoOsAtendimentosDestaAgenda(JTable tabelaSelecionada, Icon iconeAtendimento,
        Icon iconeAtendimentoExt, Connection con) {
        int numeroDeLinhasDaTabela = tabelaSelecionada.getRowCount();
        // limpando a tabela
        for (int i = 0; i < numeroDeLinhasDaTabela; i++) {
            tabelaSelecionada.setValueAt("", i, 0);
            tabelaSelecionada.setValueAt("", i, 1);
            tabelaSelecionada.setValueAt("", i, 2);
            // if para nao tirar o flag de pintura de intervalos!!!! "1" intervalo ou agenda nao funciona e "2" troca de
            // turno
            if (!"1".equals((String) tabelaSelecionada.getValueAt(i, 3))
                && !"2".equals((String) tabelaSelecionada.getValueAt(i, 3))) {
                tabelaSelecionada.setValueAt("", i, 3);
            }

            tabelaSelecionada.setValueAt("", i, 4);
            tabelaSelecionada.setValueAt("", i, 5);
        }

        // pegando a data para verificar atendimentos
        java.sql.Date diaParaVerificarAtendimentos = null;
        try {
            String diaDaTabela =
                String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            diaParaVerificarAtendimentos = new java.sql.Date(format.parse(diaDaTabela).getTime());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Erro com a data. Procure o Administrador.");
        }

        // consultando atendimentos
        ResultSet resultSet =
            ATENDIMENTOS.getConsultarAtendimentosAgenda(con, handle_agenda, diaParaVerificarAtendimentos);

        try {
            while (resultSet.next()) {

                for (int i = 0; i < tabelaSelecionada.getRowCount(); i++) {
                    try {
                        if (MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(i, 0)) == resultSet
                            .getInt("hora_atendimento")) {
                            // marcando flag de sttatus 2 pq eh um atendimento
                            tabelaSelecionada.setValueAt("2", i, 2);

                            // MARCANDO FLAG DE PINTURA
                            tabelaSelecionada.setValueAt("4", i, 3);
                            // colocando o handle_at
                            tabelaSelecionada.setValueAt(resultSet.getInt("handle_at"), i, 5);
                            // colocando nome do paciente
                            tabelaSelecionada.setValueAt(resultSet.getString("nome"), i, 0);

                            // colocando icone
                            int horaLinha =
                                MetodosUteis.transformarHorarioEmMinutos(String.valueOf(jTable1.getValueAt(i, 0)));
                            int finalDoAtendimento = resultSet.getInt("duracao_atendimento") + horaLinha;
                            try {
                                if (finalDoAtendimento <= MetodosUteis.transformarHorarioEmMinutos(String
                                    .valueOf(jTable1.getValueAt(i + 1, 0)))) {
                                    // se o final agendamento for menor ou igual a hora da proxima linha ele coloca
                                    // icone normal se nao coloca icone extendido
                                    tabelaSelecionada.setValueAt(iconeAtendimento, i, 1);
                                } else {
                                    tabelaSelecionada.setValueAt(iconeAtendimentoExt, i, 1);
                                }
                            } catch (Exception e) {
                                // coloca normal pois na proxima linha nao tem hora
                                tabelaSelecionada.setValueAt(iconeAtendimento, i, 1);
                            }

                        }
                    } catch (Exception e) {
                        // se der erro com a hora eh pq a linha nao tem hora, entao vamos para a proxima linha
                    }

                }
            }

            // preenchendo os atendimentos reservados
            ResultSet resultSetReservas =
                ATENDIMENTOS.getConsultarAtendimentosAgendaReservados(con, handle_agenda, diaParaVerificarAtendimentos);
            while (resultSetReservas.next()) {

                for (int i = 0; i < tabelaSelecionada.getRowCount(); i++) {
                    try {
                        if (MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(i, 0)) == resultSetReservas
                            .getInt("hora_atendimento")) {
                            // marcando flag de sttatus 2 pq eh um atendimento
                            tabelaSelecionada.setValueAt("2", i, 2);

                            // colocando o handle_at
                            tabelaSelecionada.setValueAt(resultSetReservas.getInt("handle_at"), i, 5);
                            // colocando nome do paciente
                            tabelaSelecionada.setValueAt("**", i, 0);

                        }
                    } catch (Exception e) {
                        // se der erro com a hora eh pq a linha nao tem hora, entao vamos para a proxima linha
                    }

                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher os Atendimentos. Procure o administrador."
                + e, "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    public void preenchendoOsAtendimentosDeTodasAsTabelas() {
        listaAgendamentosDaAgenda.clear();
        con = Conexao.fazConexao();
        preenchendoOsAtendimentosDestaAgenda(jTable2, iconeAtendimento, iconeAtendmentoExt, con);
        preenchendoOsAtendimentosDestaAgenda(jTable3, iconeAtendimento, iconeAtendmentoExt, con);
        preenchendoOsAtendimentosDestaAgenda(jTable4, iconeAtendimento, iconeAtendmentoExt, con);
        preenchendoOsAtendimentosDestaAgenda(jTable5, iconeAtendimento, iconeAtendmentoExt, con);
        preenchendoOsAtendimentosDestaAgenda(jTable6, iconeAtendimento, iconeAtendmentoExt, con);
        Conexao.fechaConexao(con);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField1.setFocusable(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "", "Flag Pintura" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, true };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTable1MouseDragged(evt);
            }
        });
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Dia", "Status", "Flag Status", "Flag Pintura", "HANDLE_AP", "HANDLE_AT" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Dia", "Status", "Flag Status", "Flag Pintura", "HANDLE_AP", "HANDLE_ATENDIMENTO" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Dia", "Status", "Flag Status", "Flag Pintura", "HANDLE_AP", "HANDLE_ATENDIMENTO" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable4.getTableHeader().setReorderingAllowed(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Dia", "Status", "Flag Status", "Flag Pintura", "HANDLE_AP", "HANDLE_ATENDIMENTO" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable5.getTableHeader().setReorderingAllowed(false);
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTable5);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Dia", "Status", "Flag Status", "Flag Pintura", "HANDLE_AP", "HANDLE_ATENDIMENTO" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable6.getTableHeader().setReorderingAllowed(false);
        jTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable6MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable6);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizarTabela.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(
                            layout
                                .createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 169,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 169,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 169,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 173,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jTextField1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                        .addComponent(jScrollBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING,
                            javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING,
                            javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE).addComponent(jScrollPane4))
                .addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void abrirPopUpDoAtendimento(MouseEvent evt, JTable tabelaSelecionada) {
        JPopupMenu popup = new JPopupMenu();

        // buscando as informações do atendimento
        con = Conexao.fazConexao();
        int handle_atendimento =
            Integer.valueOf(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 5)));
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
        popup.show(tabelaSelecionada, x, y);

    }

    public void abrirPopUpDadosDoAgendamento(MouseEvent evt, JTable tabelaSelecionada) {
        JPopupMenu popup = new JPopupMenu();
        String finalDoAgendamento = "";

        List<String> listaDeExamesDoAgendamento = new ArrayList<String>();
        String paciente = null, nascimento = null, telefone = null, celular = null, convenio = null, duracaoTotal =
            null, dia = null, hora = null;

        for (A_Agendamentos agendamento : listaAgendamentosDaAgenda) {
            if (agendamento.getHANDLE_AP() == Integer.valueOf(String.valueOf(tabelaSelecionada.getValueAt(
                tabelaSelecionada.getSelectedRow(), 4)))) {
                paciente = "Paciente                          : " + agendamento.getNomePaciente();
                try {
                    nascimento =
                        "Nascimento                    : "
                            + MetodosUteis.converterDataParaMostrarAoUsuario(agendamento.getNascimento().toString());
                } catch (Exception e) {
                    nascimento = "Nascimento                    : ";
                }
                telefone = "Telefone                          : " + agendamento.getTelefone();
                celular = "Celular                             : " + agendamento.getCelular();
                convenio = "Convênio                         : " + agendamento.getNomeConvenio();
                listaDeExamesDoAgendamento.add(agendamento.getNomeExame() + "   -   Lado: " + agendamento.getLado()
                    + "   -   Material: " + agendamento.getMaterial());
                finalDoAgendamento =
                    MetodosUteis.transformarMinutosEmHorario(agendamento.getHORA()
                        + agendamento.getDuracaoAgendamento());
                duracaoTotal =
                    "Duração Total                : "
                        + MetodosUteis.transformarMinutosEmHorario(agendamento.getDuracaoAgendamento());
                dia =
                    "Dia                                   : "
                        + MetodosUteis.converterDataParaMostrarAoUsuario(agendamento.getDia().toString());
                hora =
                    "Horário                            : "
                        + MetodosUteis.transformarMinutosEmHorario(agendamento.getHORA());
            }
        }

        popup.add(dia).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add(hora).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add(paciente).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add(nascimento).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add(telefone).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add(celular).setEnabled(false);
        popup.add("").setEnabled(true);

        popup.add(convenio).setEnabled(false);
        popup.add("").setEnabled(true);

        int i = 1;
        for (String exame : listaDeExamesDoAgendamento) {
            popup.add("Exame " + i + "                          : " + exame).setEnabled(false);
            popup.add("").setEnabled(true);
            i++;
        }
        popup.add(duracaoTotal).setEnabled(false);
        popup.add("").setEnabled(true);
        popup.add("Final do agendamento : " + finalDoAgendamento).setEnabled(false);

        // mostra na tela
        int x = evt.getX();
        int y = evt.getY();
        popup.show(tabelaSelecionada, x, y);
    }

    // variavel utilizada para saber se eh a primeira vez que a janela eh aberta, pq?
    // se for a primeira vez vai pegar este evento pq o focus vem para ca
    // entao alteramos o focus e na proxima ja nao sera a primeira vez e faço o q eu quero!
    private boolean primeiraVez = true;

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTable1FocusGained

        if (primeiraVez) {
            jScrollBar1.requestFocusInWindow();
            primeiraVez = false;
        } else {

            int linhaSelecionada = jTable1.getSelectedRow();

            jTable2.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
            jTable3.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
            jTable4.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
            jTable5.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
            jTable6.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);

            jScrollBar1.requestFocusInWindow();
        }
    }// GEN-LAST:event_jTable1FocusGained

    private void jTable1MouseDragged(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseDragged

        int linhaSelecionada = jTable1.getSelectedRow();

        jTable2.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
        jTable3.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
        jTable4.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
        jTable5.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
        jTable6.getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
    }// GEN-LAST:event_jTable1MouseDragged

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable2MouseClicked
        try {
            // inf para nao abrir o pop up caso seja a linha do intervalo ou a agenda nao esteja liberada nesta data e
            // hora
            String flagPintura = (String) jTable2.getValueAt(jTable2.getSelectedRow(), 3);

            // abrindo informações do agendamento ou atendimento
            int colunaClicada = jTable2.columnAtPoint(evt.getPoint());
            int linhaClicada = jTable2.rowAtPoint(evt.getPoint());
            int linhaSelecionada = jTable2.getSelectedRow();

            // abrindo popup de informações do atendimento ou agendamento
            if (evt.getButton() != MouseEvent.BUTTON3 && colunaClicada == 1 && (linhaClicada == linhaSelecionada)
                && !"*".equals(String.valueOf(jTable2.getValueAt(jTable2.getSelectedRow(), 0)))) {
                // se tiver handle_ap abre dados do agendamento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable2.getValueAt(jTable2.getSelectedRow(), 4))) > 0) {

                        abrirPopUpDadosDoAgendamento(evt, jTable2);
                    }
                } catch (Exception e) {
                }

                // se tiver handle_at abre dados do atendimento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable2.getValueAt(jTable2.getSelectedRow(), 5))) > 0
                        && !"".equals(String.valueOf(jTable2.getValueAt(jTable2.getSelectedRow(), 0)))) {

                        abrirPopUpDoAtendimento(evt, jTable2);
                    }
                } catch (Exception e) {
                }
            }

            // abrindo popup do botao direito
            if (!"1".equals(flagPintura) && !"2".equals(flagPintura) && linhaClicada == linhaSelecionada) {
                abrirPopUp(jTable2, evt);
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {

        }

    }// GEN-LAST:event_jTable2MouseClicked

    private void jTable6MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable6MouseClicked
        try {
            // inf para nao abrir o pop up caso seja a linha do intervalo ou a agenda nao esteja liberada nesta data e
            // hora
            String flagPintura = (String) jTable6.getValueAt(jTable6.getSelectedRow(), 3);

            // abrindo informações do agendamento ou atendimento
            int colunaClicada = jTable6.columnAtPoint(evt.getPoint());
            int linhaClicada = jTable6.rowAtPoint(evt.getPoint());
            int linhaSelecionada = jTable6.getSelectedRow();

            // abrindo popup de informações do atendimento ou agendamento
            if (evt.getButton() != MouseEvent.BUTTON3 && colunaClicada == 1 && (linhaClicada == linhaSelecionada)
                && !"*".equals(String.valueOf(jTable6.getValueAt(jTable6.getSelectedRow(), 0)))) {
                // se tiver handle_ap abre dados do agendamento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable6.getValueAt(jTable6.getSelectedRow(), 4))) > 0) {

                        abrirPopUpDadosDoAgendamento(evt, jTable6);
                    }
                } catch (Exception e) {
                }

                // se tiver handle_at abre dados do atendimento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable6.getValueAt(jTable6.getSelectedRow(), 5))) > 0
                        && !"".equals(String.valueOf(jTable6.getValueAt(jTable6.getSelectedRow(), 0)))) {

                        abrirPopUpDoAtendimento(evt, jTable6);
                    }
                } catch (Exception e) {
                }
            }

            if (!"1".equals(flagPintura) && !"2".equals(flagPintura)) {
                abrirPopUp(jTable6, evt);
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
        }
    }// GEN-LAST:event_jTable6MouseClicked

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable5MouseClicked
        try {
            // inf para nao abrir o pop up caso seja a linha do intervalo ou a agenda nao esteja liberada nesta data e
            // hora
            String flagPintura = (String) jTable5.getValueAt(jTable5.getSelectedRow(), 3);

            // abrindo informações do agendamento ou atendimento
            int colunaClicada = jTable5.columnAtPoint(evt.getPoint());
            int linhaClicada = jTable5.rowAtPoint(evt.getPoint());
            int linhaSelecionada = jTable5.getSelectedRow();

            // abrindo popup de informações do atendimento ou agendamento
            if (evt.getButton() != MouseEvent.BUTTON3 && colunaClicada == 1 && (linhaClicada == linhaSelecionada)
                && !"*".equals(String.valueOf(jTable5.getValueAt(jTable5.getSelectedRow(), 0)))) {
                // se tiver handle_ap abre dados do agendamento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable5.getValueAt(jTable5.getSelectedRow(), 4))) > 0) {

                        abrirPopUpDadosDoAgendamento(evt, jTable5);
                    }
                } catch (Exception e) {
                }

                // se tiver handle_at abre dados do atendimento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable5.getValueAt(jTable5.getSelectedRow(), 5))) > 0
                        && !"".equals(String.valueOf(jTable5.getValueAt(jTable5.getSelectedRow(), 0)))) {

                        abrirPopUpDoAtendimento(evt, jTable5);
                    }
                } catch (Exception e) {
                }
            }

            if (!"1".equals(flagPintura) && !"2".equals(flagPintura)) {
                abrirPopUp(jTable5, evt);
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
        }
    }// GEN-LAST:event_jTable5MouseClicked

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable4MouseClicked
        try {
            // inf para nao abrir o pop up caso seja a linha do intervalo ou a agenda nao esteja liberada nesta data e
            // hora
            String flagPintura = (String) jTable4.getValueAt(jTable4.getSelectedRow(), 3);

            // abrindo informações do agendamento ou atendimento
            int colunaClicada = jTable4.columnAtPoint(evt.getPoint());
            int linhaClicada = jTable4.rowAtPoint(evt.getPoint());
            int linhaSelecionada = jTable4.getSelectedRow();

            // abrindo popup de informações do atendimento ou agendamento
            if (evt.getButton() != MouseEvent.BUTTON3 && colunaClicada == 1 && (linhaClicada == linhaSelecionada)
                && !"*".equals(String.valueOf(jTable4.getValueAt(jTable4.getSelectedRow(), 0)))) {
                // se tiver handle_ap abre dados do agendamento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable4.getValueAt(jTable4.getSelectedRow(), 4))) > 0) {

                        abrirPopUpDadosDoAgendamento(evt, jTable4);
                    }
                } catch (Exception e) {
                }

                // se tiver handle_at abre dados do atendimento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable4.getValueAt(jTable4.getSelectedRow(), 5))) > 0
                        && !"".equals(String.valueOf(jTable4.getValueAt(jTable4.getSelectedRow(), 0)))) {

                        abrirPopUpDoAtendimento(evt, jTable4);
                    }
                } catch (Exception e) {
                }
            }

            if (!"1".equals(flagPintura) && !"2".equals(flagPintura)) {
                abrirPopUp(jTable4, evt);
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
        }
    }// GEN-LAST:event_jTable4MouseClicked

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable3MouseClicked
        try {
            // inf para nao abrir o pop up caso seja a linha do intervalo ou a agenda nao esteja liberada nesta data e
            // hora
            String flagPintura = (String) jTable3.getValueAt(jTable3.getSelectedRow(), 3);

            // abrindo informações do agendamento ou atendimento
            int colunaClicada = jTable3.columnAtPoint(evt.getPoint());
            int linhaClicada = jTable3.rowAtPoint(evt.getPoint());
            int linhaSelecionada = jTable3.getSelectedRow();

            // abrindo popup de informações do atendimento ou agendamento
            if (evt.getButton() != MouseEvent.BUTTON3 && colunaClicada == 1 && (linhaClicada == linhaSelecionada)
                && !"*".equals(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 0)))) {
                // se tiver handle_ap abre dados do agendamento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 4))) > 0) {

                        abrirPopUpDadosDoAgendamento(evt, jTable3);
                    }
                } catch (Exception e) {
                }

                // se tiver handle_at abre dados do atendimento
                // try para se nao tiver handle_ap nao fazer nada
                try {
                    if (Integer.valueOf(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 5))) > 0
                        && !"".equals(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 0)))) {

                        abrirPopUpDoAtendimento(evt, jTable3);
                    }
                } catch (Exception e) {
                }
            }

            if (!"1".equals(flagPintura) && !"2".equals(flagPintura)) {
                abrirPopUp(jTable3, evt);
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
        }
    }// GEN-LAST:event_jTable3MouseClicked

    @SuppressWarnings("rawtypes")
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                // atualizando a tabela
                preenchendoOsAtendimentosDeTodasAsTabelas();
                preenchendoOsAgendamentosDeTodasAsTabelas();
                return null;
            }

            @Override
            protected void done() {

                janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
            }
        };

        worker.execute();
    }// GEN-LAST:event_jButton1ActionPerformed

    // variavel para liberar o popup
    // pq isso?
    // se ele nao clicou na linha nao devo abrir o popup
    // isso para que se o pop up estiver aberto e o usuario clicar como botao direito em outra linha ele nao abre
    // soh abre dps queo ultimo aberto for fechado
    boolean liberarPopUp = false;

    public void abrirPopUp(final JTable tabelaSelecionada, final java.awt.event.MouseEvent evt) {

        // icones do popup
        ImageIcon iconeAgendar =
            new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/menuAgendar.png"));
        ImageIcon iconeRegistrarEntrada =
            new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/menuAtendimento.png"));

        if (evt.getButton() == MouseEvent.BUTTON3 && liberarPopUp) {

            // cria o primeiro item do menu e atribui uma ação pra ele
            JMenuItem menuAgendar = new JMenuItem("Agendar", iconeAgendar);
            // aqui mudamos o texto no menu caso seja um agendamento
            try {
                // se tive um agendamento e ele nao for um reservado *
                if (Integer
                    .valueOf(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 4))) > 0
                    && tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 0) != "*") {
                    menuAgendar.setText("Editar Agendamento");
                }
            } catch (Exception e) {
            }
            menuAgendar.addActionListener(new ActionListener() {

                @SuppressWarnings("rawtypes")
                @Override
                public void actionPerformed(ActionEvent e) {

                    janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();

                    SwingWorker worker = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            // salvando o flag de pintura
                            String flagPintura =
                                String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 3));
                            String nome =
                                String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 0));
                            // atualizando a tabela
                            preenchendoOsAtendimentosDeTodasAsTabelas();
                            preenchendoOsAgendamentosDeTodasAsTabelas();

                            // verificando se mudou os dados da linha
                            String flagPinturaAtualizado =
                                String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 3));
                            String nomeAtualizado =
                                String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 0));

                            // verifico a cor para se mudar de nada para agendamento ou para atendimento e o nome para
                            // caso tenha algo
                            // reservado, ae a cor continua a mesma porem muda o nome
                            if (!flagPintura.equals(flagPinturaAtualizado) || !nome.equals(nomeAtualizado)) {
                                // se mudou nao faz nada
                                JOptionPane.showMessageDialog(getRootPane(), "Sua agenda foi atualizada.");
                            } else {
                                // se nao mudou abre o o que foi selecionado
                                abrindoItemDoMenu1(tabelaSelecionada);
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
            });

            // cria o primeiro item do menu e atribui uma ação pra ele
            JMenuItem menuRegistrarEntrada = new JMenuItem("Registrar Atendimento", iconeRegistrarEntrada);
            // aqui mudamos o texto no menu caso seja um agendamento
            try {
                // se tive um agendamento e ele nao for um reservado *
                if (Integer
                    .valueOf(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 5))) > 0
                    && tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 0) != "") {
                    menuRegistrarEntrada.setText("Editar Atendimento");
                }
            } catch (Exception e) {
            }
            menuRegistrarEntrada.addActionListener(new ActionListener() {

                @SuppressWarnings("rawtypes")
                @Override
                public void actionPerformed(ActionEvent e) {
                    janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();

                    SwingWorker worker = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            // salvando o flag de pintura
                            String flagPintura =
                                String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 3));
                            String nome =
                                String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 0));
                            // atualizando a tabela
                            preenchendoOsAtendimentosDeTodasAsTabelas();
                            preenchendoOsAgendamentosDeTodasAsTabelas();

                            // verificando se mudou os dados da linha
                            String flagPinturaAtualizado =
                                String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 3));
                            String nomeAtualizado =
                                String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 0));

                            // verifico a cor para se mudar de nada para agendamento ou para atendimento e o nome para
                            // caso tenha algo
                            // reservado, ae a cor continua a mesma porem muda o nome
                            if (!flagPintura.equals(flagPinturaAtualizado) || !nome.equals(nomeAtualizado)) {
                                // se mudou nao faz nada
                                JOptionPane.showMessageDialog(getRootPane(), "Sua agenda foi atualizada.");
                            } else {
                                // se nao mudou abre o o que foi selecionado
                                abrindoItemDoMenu2(tabelaSelecionada);
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
            });

            // cria o menu popup e adiciona os 3 itens
            JPopupMenu popupMenuBotaoDireito = new JPopupMenu();
            popupMenuBotaoDireito.add(menuAgendar);
            popupMenuBotaoDireito.add(menuRegistrarEntrada);

            // if's para bloquear alum menu de acordo com o flag

            // if bloquear o menu 1 (agendar) se nao for nulo, vazio ou 1 o flag do status (pq significada q ja foi
            // feita entrada de ficha)
            String flagDaLinhaSelecionada =
                (String) tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 2);
            if (!"".equals(flagDaLinhaSelecionada) && flagDaLinhaSelecionada != null
                && !"1".equals(flagDaLinhaSelecionada)) {
                menuAgendar.setEnabled(false);
            }

            // if for horario reservado não abre o menu agendar
            if ("*".equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 0)))) {
                menuAgendar.setEnabled(false);
                menuAgendar.setText("Agendamento Reservado");
                menuRegistrarEntrada.setVisible(false);
            }

            // if for horario reservado um atendimento
            if ("**".equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 0)))) {
                menuRegistrarEntrada.setEnabled(false);
                menuRegistrarEntrada.setText("Atendimento Reservado");
                menuAgendar.setVisible(false);
            }

            try {
                // if for é um atendimento reservado deixa desativado o editar atendimento
                if ("".equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 0)))
                    && Integer.valueOf(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(),
                        5))) > 0) {
                    menuRegistrarEntrada.setEnabled(false);
                    menuRegistrarEntrada.setText("Atendimento Reservado");
                    menuAgendar.setVisible(false);
                }
            } catch (Exception e) {
            }

            // if se dia nao for hoje desativa o menu registrar e se for ontem ou antes desativa o menu agendar
            try {
                SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");

                Date diaDaTabelaSelecionada =
                    formatador.parse(String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue())
                        .substring(4, 14));

                Calendar hoje = Calendar.getInstance();
                String dataDeHojeString = formatador.format(hoje.getTime());
                Date dataDeHoje = formatador.parse(dataDeHojeString);

                // se nao for hoje desativa menu registrar entrada
                if (dataDeHoje.compareTo(diaDaTabelaSelecionada) != 0) {
                    menuRegistrarEntrada.setEnabled(false);
                }

                // se for ontem ou antes desativa menu agendar
                if (dataDeHoje.compareTo(diaDaTabelaSelecionada) == 1) {
                    menuAgendar.setEnabled(false);
                }
            } catch (ParseException e) {

            }

            // mostra na tela
            int x = evt.getX();
            int y = evt.getY();
            popupMenuBotaoDireito.show(tabelaSelecionada, x, y);

            // tranca abrir o pop up
            // para q nao posso abrir outro enquanto nao houver o evento pressed
            // no pressed liberamos o pop up para ser aberto
            liberarPopUp = false;

        }
    }

    public void abrindoItemDoMenu1(JTable tabela) {
        int handle_apDaLinhaSelecionada = 0;

        try {
            handle_apDaLinhaSelecionada =
                Integer.valueOf(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 4)));
        } catch (Exception e) {
        }

        String livreOuOcupado;

        if (handle_apDaLinhaSelecionada == 0) {
            livreOuOcupado = "livre";
        } else {
            livreOuOcupado = "ocupado";
        }

        // se estiver pintado de azul "3" informa q ja tem um agendamento e se deseja continuar
        if ("3".equals(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 3)))
            && "".equals(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 0)))) {
            int resposta =
                JOptionPane.showConfirmDialog(null, "Já existe um agendamento neste horário. Deseja continuar?",
                    "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            // se resposta for sim abre o agendamento
            if (resposta == JOptionPane.YES_OPTION) {
                // sumindo legenda da agenda
                JIFAgendaPrincipal.sumirLegenda(false);

                janelaPrincipal.internalFrameAgendamento =
                    new JIFAgendamento(livreOuOcupado, handle_apDaLinhaSelecionada, handle_agenda, tabela, this);
                JIFAgendaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAgendamento);
                janelaPrincipal.internalFrameAgendamento.setVisible(true);
                int lDesk = JIFAgendaPrincipal.jDesktopPane1.getWidth();
                int aDesk = JIFAgendaPrincipal.jDesktopPane1.getHeight();
                int lIFrame = janelaPrincipal.internalFrameAgendamento.getWidth();
                int aIFrame = janelaPrincipal.internalFrameAgendamento.getHeight();
                janelaPrincipal.internalFrameAgendamento.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

                this.setVisible(false);

                JIFAgendaPrincipal.jComboBox1.setEnabled(false);
                JIFAgendaPrincipal.jComboBox2.setEnabled(false);
                JIFAgendaPrincipal.jComboBox3.setEnabled(false);
                JIFAgendaPrincipal.jComboBox4.setEnabled(false);
                JIFAgendaPrincipal.jXDatePicker1.setEnabled(false);

            }
        } else {
            // se nao tiver um agendamento ali abre o agendamento
            // sumindo legenda da agenda
            JIFAgendaPrincipal.sumirLegenda(false);

            janelaPrincipal.internalFrameAgendamento =
                new JIFAgendamento(livreOuOcupado, handle_apDaLinhaSelecionada, handle_agenda, tabela, this);
            JIFAgendaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAgendamento);
            janelaPrincipal.internalFrameAgendamento.setVisible(true);
            int lDesk = JIFAgendaPrincipal.jDesktopPane1.getWidth();
            int aDesk = JIFAgendaPrincipal.jDesktopPane1.getHeight();
            int lIFrame = janelaPrincipal.internalFrameAgendamento.getWidth();
            int aIFrame = janelaPrincipal.internalFrameAgendamento.getHeight();
            janelaPrincipal.internalFrameAgendamento.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

            this.setVisible(false);

            JIFAgendaPrincipal.jComboBox1.setEnabled(false);
            JIFAgendaPrincipal.jComboBox2.setEnabled(false);
            JIFAgendaPrincipal.jComboBox3.setEnabled(false);
            JIFAgendaPrincipal.jComboBox4.setEnabled(false);
            JIFAgendaPrincipal.jXDatePicker1.setEnabled(false);

        }
    }

    public void abrindoItemDoMenu2(JTable tabela) {
        int handle_atDaLinhaSelecionada = 0;

        try {
            handle_atDaLinhaSelecionada =
                Integer.valueOf(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 5)));
        } catch (Exception e) {
        }

        String livreOuOcupado;

        if (handle_atDaLinhaSelecionada == 0) {
            livreOuOcupado = "livre";
        } else {
            livreOuOcupado = "ocupado";
        }

        // se estiver pintado de azul "3" informa q ja tem um agendamento e se deseja continuar
        if ("3".equals(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 3)))
            && "".equals(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 0)))) {
            int resposta =
                JOptionPane.showConfirmDialog(null, "Já existe um agendamento neste horário. Deseja continuar?",
                    "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            // se resposta for sim abre o agendamento
            if (resposta == JOptionPane.YES_OPTION) {
                // sumindo legenda da agenda
                JIFAgendaPrincipal.sumirLegenda(false);
                // deixando o jdesktop maior
                JIFAgendaPrincipal.jDesktopPane1.setSize(JIFAgendaPrincipal.jDesktopPane1.getWidth(),
                    JIFAgendaPrincipal.jDesktopPane1.getHeight() + 20);

                janelaPrincipal.internalFrameAtendimentoAgenda =
                    new JIFAtendimentoAgenda(livreOuOcupado, handle_atDaLinhaSelecionada, handle_agenda, tabela, this);
                janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimentoAgenda);
                janelaPrincipal.internalFrameAtendimentoAgenda.setVisible(true);
                int lDesk = JIFAgendaPrincipal.jDesktopPane1.getWidth();
                int aDesk = JIFAgendaPrincipal.jDesktopPane1.getHeight();
                int lIFrame = janelaPrincipal.internalFrameAtendimentoAgenda.getWidth();
                int aIFrame = janelaPrincipal.internalFrameAtendimentoAgenda.getHeight();
                janelaPrincipal.internalFrameAtendimentoAgenda.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame
                    / 2);

                this.setVisible(false);

                JIFAgendaPrincipal.jComboBox1.setEnabled(false);
                JIFAgendaPrincipal.jComboBox2.setEnabled(false);
                JIFAgendaPrincipal.jComboBox3.setEnabled(false);
                JIFAgendaPrincipal.jComboBox4.setEnabled(false);
                JIFAgendaPrincipal.jXDatePicker1.setEnabled(false);

            }
        } else {
            // se nao tiver um agendamento ali abre o agendamento
            // sumindo legenda da agenda
            JIFAgendaPrincipal.sumirLegenda(false);
            // deixando o jdesktoppane menor
            JIFAgendaPrincipal.jDesktopPane1.setSize(JIFAgendaPrincipal.jDesktopPane1.getWidth(),
                JIFAgendaPrincipal.jDesktopPane1.getHeight() + 20);

            janelaPrincipal.internalFrameAtendimentoAgenda =
                new JIFAtendimentoAgenda(livreOuOcupado, handle_atDaLinhaSelecionada, handle_agenda, tabela, this);
            JIFAgendaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimentoAgenda);
            janelaPrincipal.internalFrameAtendimentoAgenda.setVisible(true);
            int lDesk = JIFAgendaPrincipal.jDesktopPane1.getWidth();
            int aDesk = JIFAgendaPrincipal.jDesktopPane1.getHeight();
            int lIFrame = janelaPrincipal.internalFrameAtendimentoAgenda.getWidth();
            int aIFrame = janelaPrincipal.internalFrameAtendimentoAgenda.getHeight();
            janelaPrincipal.internalFrameAtendimentoAgenda
                .setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

            this.setVisible(false);

            JIFAgendaPrincipal.jComboBox1.setEnabled(false);
            JIFAgendaPrincipal.jComboBox2.setEnabled(false);
            JIFAgendaPrincipal.jComboBox3.setEnabled(false);
            JIFAgendaPrincipal.jComboBox4.setEnabled(false);
            JIFAgendaPrincipal.jXDatePicker1.setEnabled(false);

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable2;
    public static javax.swing.JTable jTable3;
    public static javax.swing.JTable jTable4;
    public static javax.swing.JTable jTable5;
    public static javax.swing.JTable jTable6;
    public static javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
