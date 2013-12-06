/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.consultaValorExames;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.calculoValorDeUmExame.calculoValorDeExame;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.TABELAS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Cesar Schutz
 */
public class consultaValorExames extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    String nomeConvenio;
    int handle_convenio;
    Connection con = null;

    /**
     * Creates new form consultaValorExames
     */
    public consultaValorExames(String nomeConvenio, String handle_convenio) {
        initComponents();
        this.nomeConvenio = nomeConvenio;
        this.handle_convenio = Integer.valueOf(handle_convenio);
        preencherModalidades();
        tirandoBarraDeTitulo();
        iniciarClasse();
        atualizarTabela();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void iniciarClasse() {

        // alinhando conteudo da coluna de uma tabela

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        jTable.setAutoCreateRowSorter(true);
        jTable.setRowHeight(20);
        jTFNomeDaTabela.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTFNomeDaTabela.setText(nomeConvenio);

        jTable.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable.getColumnModel().getColumn(0).setMinWidth(0);
        jTable.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

    }

    @SuppressWarnings("unchecked")
    public void preencherModalidades() {
        con = Conexao.fazConexao();
        ResultSet resultSet = TABELAS.getConsultarModalidades(con);
        try {
            while (resultSet.next()) {
                jCBModalidades.addItem(resultSet.getString("modalidade"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher as Modalidades. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    public void atualizarTabela() {
        ((DefaultTableModel) jTable.getModel()).setNumRows(0);
        jTable.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable.getModel();
        con = Conexao.fazConexao();
        String modalidade = String.valueOf(jCBModalidades.getSelectedItem());
        ResultSet resultSet = TABELAS.getConsultarExamesDaTabela(con, handle_convenio, modalidade);
        try {
            while (resultSet.next()) {
                // colocando dados na tabela
                modelo.addRow(new String[] { Integer.toString(resultSet.getInt("handle_exame")),
                    resultSet.getString("nome") });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar os Exames. Procure o administrador",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    public void botaoRetornar() {
        this.dispose();
        janelaPrincipal.internalFrameConsultaValorDeExames = null;

        janelaPrincipal.internalFrameListaConveniosConsultaValorDeExames = new listaConvenios();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameListaConveniosConsultaValorDeExames);
        janelaPrincipal.internalFrameListaConveniosConsultaValorDeExames.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameListaConveniosConsultaValorDeExames.getWidth();
        int aIFrame = janelaPrincipal.internalFrameListaConveniosConsultaValorDeExames.getHeight();

        janelaPrincipal.internalFrameListaConveniosConsultaValorDeExames.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
            - aIFrame / 2);
    }

    public void calcularValorDeExame() {
        if (jTable.getSelectedRow() >= 0) {
            jLabel1.setText("Modalide: " + String.valueOf(jCBModalidades.getSelectedItem()) + " - Exame: "
                + jTable.getValueAt(jTable.getSelectedRow(), 1));

            // pegando a data do sistema
            Calendar hoje = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String dataDeHoje = format.format(hoje.getTime());
            Date dataDeHojeEmVariavelDate = null;
            try {
                dataDeHojeEmVariavelDate = new java.sql.Date(format.parse(dataDeHoje).getTime());
            } catch (ParseException ex) {
            }

            // preenchendo os valores sem materiais
            calculoValorDeExame calcularValorExameSemMateriais =
                new calculoValorDeExame(Integer.valueOf(handle_convenio), Integer.valueOf(String.valueOf(jTable
                    .getValueAt(jTable.getSelectedRow(), 0))), dataDeHojeEmVariavelDate, false);

            double valorPacienteSemMaterial =
                (calcularValorExameSemMateriais.porcentPaciente / 100)
                    * Double.valueOf(calcularValorExameSemMateriais.valor_correto_exame);
            double valorConvenioSemMaterial =
                (calcularValorExameSemMateriais.porcentConvenio / 100)
                    * Double.valueOf(calcularValorExameSemMateriais.valor_correto_exame);

            jTFValorTotalSemMateriais.setText(MetodosUteis.colocarZeroEmCampoReais(new BigDecimal(
                calcularValorExameSemMateriais.valor_correto_exame).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
            jTFValorConvenioSemMateriais.setText(MetodosUteis.colocarZeroEmCampoReais(new BigDecimal(
                valorConvenioSemMaterial).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
            jTFValorPacienteSemMateriais.setText(MetodosUteis.colocarZeroEmCampoReais(new BigDecimal(
                valorPacienteSemMaterial).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));

            // preenchendo os valores Com materiais
            calculoValorDeExame calcularValorExameComMateriais =
                new calculoValorDeExame(Integer.valueOf(handle_convenio), Integer.valueOf(String.valueOf(jTable
                    .getValueAt(jTable.getSelectedRow(), 0))), dataDeHojeEmVariavelDate, true);

            double valorPacienteComMaterial =
                (calcularValorExameComMateriais.porcentPaciente / 100)
                    * Double.valueOf(calcularValorExameComMateriais.valor_correto_exame);
            double valorConvenioComMaterial =
                (calcularValorExameComMateriais.porcentConvenio / 100)
                    * Double.valueOf(calcularValorExameComMateriais.valor_correto_exame);

            jTFValorTotalComMateriais.setText(MetodosUteis.colocarZeroEmCampoReais(new BigDecimal(
                calcularValorExameComMateriais.valor_correto_exame).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
            jTFValorConvenioComMateriais.setText(MetodosUteis.colocarZeroEmCampoReais(new BigDecimal(
                valorConvenioComMaterial).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
            jTFValorPacienteComMateriais.setText(MetodosUteis.colocarZeroEmCampoReais(new BigDecimal(
                valorPacienteComMaterial).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));

        } else {
            JOptionPane.showMessageDialog(null, "Selecione um Exame!");
        }

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTFNomeDaTabela = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTFValorTotalSemMateriais = new javax.swing.JTextField();
        jTFValorTotalComMateriais = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTFValorConvenioSemMateriais = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTFValorConvenioComMateriais = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTFValorPacienteSemMateriais = new javax.swing.JTextField();
        jTFValorPacienteComMateriais = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jBCalcularValorDoExame = new javax.swing.JButton();
        jCBModalidades = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jBRetornar = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Valores dos Exames",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFNomeDaTabela.setEditable(false);
        jTFNomeDaTabela.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFNomeDaTabela.setText("NOME DA TABELA");
        jTFNomeDaTabela.setFocusable(false);

        jTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "handle_exame", "Exame(s)" }) {
            private static final long serialVersionUID = 1L;
            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane1.setViewportView(jTable);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Selecione um Exame e clique em Calcular!");

        jLabel2.setText("Valor Total S/ Materiais");

        jTFValorTotalSemMateriais.setFocusable(false);

        jTFValorTotalComMateriais.setFocusable(false);

        jLabel3.setText("Valor Total C/ Materiais");

        jTFValorConvenioSemMateriais.setFocusable(false);

        jLabel4.setText("Valor Convênio S/ Materiais");

        jLabel5.setText("Valor Convênio C/ Materiais");

        jTFValorConvenioComMateriais.setFocusable(false);

        jLabel6.setText("Valor Paciente S/ Materiais");

        jTFValorPacienteSemMateriais.setFocusable(false);

        jTFValorPacienteComMateriais.setFocusable(false);

        jLabel7.setText("Valor Paciente C/ Materiais");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jBCalcularValorDoExame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/calcular.png"))); // NOI18N
        jBCalcularValorDoExame.setText("Calcular");
        jBCalcularValorDoExame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCalcularValorDoExameActionPerformed(evt);
            }
        });

        jCBModalidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBModalidadesActionPerformed(evt);
            }
        });

        jLabel8.setText("Modalidade");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addComponent(jBCalcularValorDoExame)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE))
            .addComponent(jTFNomeDaTabela)
            .addComponent(jScrollPane1)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTFValorTotalSemMateriais))
                                    .addGap(18, 18, 18)
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTFValorTotalComMateriais))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTFValorConvenioSemMateriais))
                                    .addGap(18, 18, 18)
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTFValorConvenioComMateriais))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTFValorPacienteSemMateriais))
                                    .addGap(18, 18, 18)
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTFValorPacienteComMateriais)))
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jCBModalidades, javax.swing.GroupLayout.PREFERRED_SIZE, 79,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(0, 0, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addComponent(jTFNomeDaTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jCBModalidades, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jBCalcularValorDoExame).addComponent(jLabel1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFValorTotalSemMateriais, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFValorPacienteSemMateriais, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFValorTotalComMateriais, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(
                                jPanel1Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(
                                        jPanel1Layout
                                            .createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTFValorConvenioComMateriais,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(
                                        jPanel1Layout
                                            .createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTFValorConvenioSemMateriais,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFValorPacienteComMateriais, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator2)).addContainerGap()));

        jBRetornar.setBackground(new java.awt.Color(113, 144, 224));
        jBRetornar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBRetornar.setText("Voltar");
        jBRetornar.setAlignmentY(0.0F);
        jBRetornar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBRetornar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBRetornarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                layout
                    .createSequentialGroup()
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                            .addComponent(jBRetornar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            javax.swing.GroupLayout.Alignment.TRAILING,
            layout
                .createSequentialGroup()
                .addComponent(jBRetornar, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBRetornarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBRetornarActionPerformed
        botaoRetornar();
    }// GEN-LAST:event_jBRetornarActionPerformed

    private void jBCalcularValorDoExameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCalcularValorDoExameActionPerformed
        calcularValorDeExame();
    }// GEN-LAST:event_jBCalcularValorDoExameActionPerformed

    private void jCBModalidadesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBModalidadesActionPerformed
        atualizarTabela();// TODO add your handling code here:
    }// GEN-LAST:event_jCBModalidadesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCalcularValorDoExame;
    public static javax.swing.JButton jBRetornar;
    @SuppressWarnings("rawtypes")
    public static javax.swing.JComboBox jCBModalidades;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTFNomeDaTabela;
    private javax.swing.JTextField jTFValorConvenioComMateriais;
    private javax.swing.JTextField jTFValorConvenioSemMateriais;
    private javax.swing.JTextField jTFValorPacienteComMateriais;
    private javax.swing.JTextField jTFValorPacienteSemMateriais;
    private javax.swing.JTextField jTFValorTotalComMateriais;
    private javax.swing.JTextField jTFValorTotalSemMateriais;
    public static javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
}
