package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio;

import static br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameFinanceiroRelatorioFaturarConvenios;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.bcn.admclin.ClasseAuxiliares.ColunaAceitandoIcone;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.financeiro.faturarConvenio.arquivoTxtDoIpe.GerarArquivoTxtDaFatura;

/**
 * 
 * @author Cesar Schutz
 */
public class jIFListaAtendimentosParaFaturar extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    // pode ser fatura por GRUPO ou por CONVENIO
    private Connection con = null;
    private int handle_convenio = 0, grupo_id = 0;
    private String tipo = null, nome;
    private Date dataInicial = null, dataFinal = null;
    public ArrayList<atendimentoModel> listaDeAtendimentos = new ArrayList<atendimentoModel>();

    ImageIcon iconeSim = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/sim.png"));
    ImageIcon iconeSimPretoBranco = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/simPretoBranco.png"));
    ImageIcon iconeNao = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/nao.png"));

    /*
     * metodo construtor por convenio
     */
    /**
     * @wbp.parser.constructor
     */
    public jIFListaAtendimentosParaFaturar(String tp, Date dataInicial, Date dataFinal, String nome,
        int handle_convenio, ArrayList<atendimentoModel> listaAtendimentos) {
        initComponents();
        iniciarClasse();
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.handle_convenio = handle_convenio;
        this.tipo = tp;
        this.nome = nome;
        this.listaDeAtendimentos = listaAtendimentos;
        jLabel1.setText("Convênio: " + nome);
        preenchendoTabela();

    }

    /*
     * metodo construtor por grupo de convenio
     */
    public jIFListaAtendimentosParaFaturar(String tp, Date dataInicial, Date dataFinal, String nome, int grupo_id,
        int x, ArrayList<atendimentoModel> listaAtendimentos) {
        initComponents();
        iniciarClasse();
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.grupo_id = grupo_id;
        this.tipo = tp;
        this.nome = nome;
        this.listaDeAtendimentos = listaAtendimentos;
        jLabel1.setText("Grupo de Convênio: " + nome);
        preenchendoTabela();
    }

    private void iniciarClasse() {
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse();
        tirandoBarraDeTitulo();
        jTable1.setRowHeight(30);

        // coluna de status aceitando icones
        TableCellRenderer tcrColuna4 = new ColunaAceitandoIcone();
        TableColumn column4 = jTable1.getColumnModel().getColumn(4);
        column4.setCellRenderer(tcrColuna4);

        TableCellRenderer tcrColuna5 = new ColunaAceitandoIcone();
        TableColumn column5 = jTable1.getColumnModel().getColumn(5);
        column5.setCellRenderer(tcrColuna5);

        TableCellRenderer tcrColuna6 = new ColunaAceitandoIcone();
        TableColumn column6 = jTable1.getColumnModel().getColumn(6);
        column6.setCellRenderer(tcrColuna6);

        // definindo o tamanho das colunas
        jTable1.getColumnModel().getColumn(0).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(45);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(45);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(90);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(60);

        // sumindo coluna
        jTable1.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(7).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(7).setMinWidth(0);

        // alinhando conteudo da coluna de uma tabela
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        jTable1.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(direita);

    }

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
                
                int linhaClicada = jTable1.rowAtPoint(e.getPoint());
                if (MouseEvent.BUTTON3 == e.getButton() && linhaClicada == linhaSelecionada) {
                    //abrirPopUpDoAtendimento(arg0);
                    abrirPopUp(e, linhaSelecionada);
                }
            }
        });
    }
    
    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    // preenche a tabela com a lista
    private void preenchendoTabela() {
        // limpa a tabela
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

        // preenche a tabela com os atendimentos
        for (atendimentoModel atendimento : listaDeAtendimentos) {
            modelo.addRow(new Object[] { atendimento.getHandle_at(),
                MetodosUteis.converterDataParaMostrarAoUsuario(atendimento.getData_atendimento().toString()),
                atendimento.getHora(), atendimento.getNomePaciente(), String.valueOf(atendimento.getFlag_laudo()),
                String.valueOf(atendimento.getFlag_faturado()), "0", "1" });
        }
        colocarIcones();
        colocarIconesFaturar();
    }

    /*
     * coloca icones na coluna 6 (faturar) dependendo do flag da coluna 7
     */
    private void colocarIconesFaturar() {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            // colocando icone laudo
            if (jTable1.getValueAt(i, 7).equals("1")) {
                jTable1.setValueAt(iconeSim, i, 6);
            } else {
                jTable1.setValueAt(iconeNao, i, 6);
            }
        }
    }

    /*
     * Coloca icones na coluna 4(laudo) e 5(ja faturado)
     */
    private void colocarIcones() {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            // colocando icone laudo
            if (jTable1.getValueAt(i, 4).equals("1")) {
                jTable1.setValueAt(iconeSimPretoBranco, i, 4);
            } else if(jTable1.getValueAt(i, 4).equals("0")) {
                jTable1.setValueAt("", i, 4);
            }
            // colocando icone ja faturado
            if (jTable1.getValueAt(i, 5).equals("1")) {
                jTable1.setValueAt(iconeSimPretoBranco, i, 5);
            } else if (jTable1.getValueAt(i, 5).equals("0")){
                jTable1.setValueAt("", i, 5);
            }
        }
    }

    private void verificaSeTodosForamSelecionados() {
        boolean todos = true;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if (jTable1.getValueAt(i, 7).equals("0")) {
                todos = false;
            }
        }
        if (todos) {
            jCheckBox1.setSelected(true);
        }
    }

    private void abrirFinanceiroRelatorioFaturarConvenios() {
        this.dispose();
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameAtendimentosParaFaturar = null;
        internalFrameFinanceiroRelatorioFaturarConvenios.setVisible(true);
    }

    /**
     * remove da lista os atendimentos que não devem ser faturados
     */
    private void apagarAtendimentosDaLista() {
        for (int i = jTable1.getRowCount() - 1; i >= 0; i--) {
            if (jTable1.getValueAt(i, 7).equals("0")) {
                listaDeAtendimentos.remove(i);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void gerarArquivoTextoGrupo() {
        // criando o arquivo txt caso o convenio exiga
        con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
        ResultSet resultSet = br.bcn.admclin.dao.dbris.CONVENIO.getConsultarDadosDeUmGrupo(con, grupo_id);
        try {
            int geraArquivoTXT = 0;
            while (resultSet.next()) {
                geraArquivoTXT = resultSet.getInt("GERA_ARQUIVO_TEXTO");
            }
            if (geraArquivoTXT == 1) {
                Object[] options = { "Sim", "Não" };
                int n =
                    JOptionPane.showOptionDialog(null, "Deseja gerar arquivo TXT?", "Arquivo texto",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    String data1 = String.valueOf(dataInicial.getMonth());
                    String mes1 = data1;
                    String data2 = String.valueOf(dataFinal.getMonth());
                    String mes2 = data2;
                    if (mes1.equals(mes2)) {
                        GerarArquivoTxtDaFatura gerar =
                            new GerarArquivoTxtDaFatura("grupo", dataInicial, dataFinal, nome, grupo_id, 0,
                                listaDeAtendimentos);
                        gerar.criarTxt();
                    } else {
                        GerarArquivoTxtDaFatura gerar =
                            new GerarArquivoTxtDaFatura("grupo", dataInicial, dataFinal, nome, grupo_id, 0,
                                listaDeAtendimentos);
                        gerar.criarTxt();
                        JOptionPane
                            .showMessageDialog(
                                null,
                                "Atenção, você selecionou meses diferentes. O Arquivo TXT pode conter erros, para solucionar gere a fatura de um único mês.");
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar arquivo TXT. Procure o Administrador");
        }
    }

    @SuppressWarnings("deprecation")
    private void geraArquivoTextoConvenio() {
        // criando o arquivo txt caso o convenio exiga
        con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
        ResultSet resultSet = br.bcn.admclin.dao.dbris.CONVENIO.getConsultarDadosDeUmConvenio(con, handle_convenio);
        try {
            int geraArquivoTXT = 0;
            while (resultSet.next()) {
                geraArquivoTXT = resultSet.getInt("IMPRIMI_ARQUIVO_TXT_COM_FATURA");
            }
            if (geraArquivoTXT == 1) {
                Object[] options = { "Sim", "Não" };
                int n =
                    JOptionPane.showOptionDialog(null, "Deseja gerar arquivo TXT?", "Arquivo texto",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    String data1 = String.valueOf(dataInicial.getMonth());
                    String mes1 = data1;
                    String data2 = String.valueOf(dataFinal.getMonth());
                    String mes2 = data2;
                    if (mes1.equals(mes2)) {
                        GerarArquivoTxtDaFatura gerar =
                            new GerarArquivoTxtDaFatura("convenio", dataInicial, dataFinal, nome, handle_convenio,
                                listaDeAtendimentos);
                        gerar.criarTxt();
                    } else {
                        GerarArquivoTxtDaFatura gerar =
                            new GerarArquivoTxtDaFatura("convenio", dataInicial, dataFinal, nome, handle_convenio,
                                listaDeAtendimentos);
                        gerar.criarTxt();
                        JOptionPane
                            .showMessageDialog(
                                null,
                                "Atenção, você selecionou meses diferentes. O Arquivo TXT pode conter erros, para solucionar gere a fatura de um único mês.");
                    }
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar arquivo TXT. Procure o Administrador");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jBGerarFatura = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selecione os exames para Faturar",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1
            .setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] { { null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null } }, new String[] { "Código", "Data", "Hora",
                    "Paciente", "Laudo", "Já Faturado", "Faturar", "faturar_numero" }) {
                private static final long serialVersionUID = 1L;
                boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jBGerarFatura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemBotaoOk.png"))); // NOI18N
        jBGerarFatura.setText("Gerar Fatura");
        jBGerarFatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGerarFaturaActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Selecionar Todos");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 948, Short.MAX_VALUE)
            .addComponent(jBGerarFatura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel1Layout
                    .createSequentialGroup()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jCheckBox1)
                    .addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1).addComponent(jLabel1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBGerarFatura)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBGerarFaturaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBGerarFaturaActionPerformed

        apagarAtendimentosDaLista();

        if (listaDeAtendimentos.size() > 0) {
            // gera a fatura PDF
            if (tipo.equals("grupo")) {
                faturaConvenio faturaConvenio =
                    new faturaConvenio("grupo", dataInicial, dataFinal, nome, grupo_id, 0, listaDeAtendimentos);
                if (faturaConvenio.gerarFatura()) {
                    gerarArquivoTextoGrupo();
                }
            } else {
                faturaConvenio faturaConvenio =
                    new faturaConvenio("convenio", dataInicial, dataFinal, nome, handle_convenio, listaDeAtendimentos);
                if (faturaConvenio.gerarFatura()) {
                    geraArquivoTextoConvenio();
                }
            }

            abrirFinanceiroRelatorioFaturarConvenios();
        } else {
            JOptionPane.showMessageDialog(br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal,
                "Selecione um Atendimento.");
        }
    }// GEN-LAST:event_jBGerarFaturaActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckBox1ActionPerformed
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if (jCheckBox1.isSelected()) {
                jTable1.setValueAt("1", i, 7);
            } else {
                jTable1.setValueAt("0", i, 7);
            }
        }
        colocarIconesFaturar();
    }// GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTable1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            int[] linhasSelecionadas = jTable1.getSelectedRows();

            for (int i = 0; i < linhasSelecionadas.length; i++) {
                if (jTable1.getValueAt(linhasSelecionadas[i], 7).equals("1")) {
                    jTable1.setValueAt("0", linhasSelecionadas[i], 7);
                    jCheckBox1.setSelected(false);
                } else {
                    jTable1.setValueAt("1", linhasSelecionadas[i], 7);
                }
            }
            colocarIconesFaturar();
            verificaSeTodosForamSelecionados();
        }

    }// GEN-LAST:event_jTable1KeyReleased

    private void abrirPopUp(MouseEvent evt, int linhaSelecionada){
        ImageIcon iconeLaudoEExameEntregue = new javax.swing.ImageIcon(getClass().getResource(
                        "/br/bcn/admclin/imagens/popUpCancelar.png"));
     // Paciente recebeu o exame
        JMenuItem definirNaoFaturado = new JMenuItem("Definir como não Faturado", iconeLaudoEExameEntregue);
        definirNaoFaturado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //aqui muda o flag_faturado para 0 e tira a imagem
                con = Conexao.fazConexao();
                int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
                atendimentoDAO.setAtualizarFlagFaturado(con, handle_at, 0);
                jTable1.setValueAt("0", jTable1.getSelectedRow(), 5);
                colocarIcones();
            }
        });
        
        JPopupMenu popup = new JPopupMenu();
        popup.add(definirNaoFaturado);

        // mostra na tela
        int x = evt.getX();
        int y = evt.getY();
        popup.show(jTable1, x, y);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBGerarFatura;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
