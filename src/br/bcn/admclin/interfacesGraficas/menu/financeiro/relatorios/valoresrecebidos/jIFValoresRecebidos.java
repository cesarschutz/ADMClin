/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.valoresrecebidos;

import br.bcn.admclin.dao.dbris.CONVENIO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.atendimentos.porClassesDeExames.relatorioTodosConveniosTodasClassesAnaliticoValoresEspecificos;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.atendimentos.porClassesDeExames.relatorioUmConvenioTodasClassesAnaliticoValoresEspecificos;

import java.awt.Dimension;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * 
 * @author Cesar Schutz
 */
public class jIFValoresRecebidos extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private Connection con = null;
    public List<Integer> listaHandleConvenio = new ArrayList<Integer>();

    /**
     * Creates new form jIFFinanceiroAtendimentos
     */
    public jIFValoresRecebidos() {
        initComponents();
        tirandoBarraDeTitulo();

        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker2.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Ir para data atual");
        jXDatePicker2.setLinkDate(System.currentTimeMillis(), "Ir para data atual");

        preencherComboBoxComConvenios();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    @SuppressWarnings("unchecked")
    private void preencherComboBoxComConvenios() {
        con = Conexao.fazConexao();
        ResultSet resultSet = CONVENIO.getConsultar(con);
        listaHandleConvenio.removeAll(listaHandleConvenio);
        jCBConvenio.removeAllItems();

        jCBConvenio.addItem("Todos os Convênios");
        listaHandleConvenio.add(0);
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("handle_convenio") > 0) {
                    jCBConvenio.addItem(resultSet.getString("nome"));
                    int handle_convenio = resultSet.getInt("handle_convenio");
                    listaHandleConvenio.add(handle_convenio);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível preencher os Convênios. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        Conexao.fechaConexao(con);
    }

    int dataInicial, dataFinal;

    // metodo que verfiica se as datas estão corretas
    private boolean verificarDatas() {
        try {

            // criando um formato de data
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            // pegando data inicial

            Date dataSelecionada = jXDatePicker1.getDate();
            String diaInicial = format.format(dataSelecionada);
            dataInicial = Integer.valueOf(diaInicial);

            // pegando data final
            Date dataSelecionada2 = jXDatePicker2.getDate();
            String diaFinal = format.format(dataSelecionada2);
            dataFinal = Integer.valueOf(diaFinal);

            // verificando se primeira data eh menor que a segunda!
            if (dataInicial > dataFinal) {
                JOptionPane.showMessageDialog(null, "Verifique as datas e tente novamente.");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // todos os convenios - relatorio por classe de exame - analitico - valores especidicos
    @SuppressWarnings("rawtypes")
    private void relatorioAnaliticoPorClasseDeExamesValoresEspecificosDeTodosOsConvenios() {
        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                java.sql.Date diaInicialSql = null, diaFinalSql = null;
                if (verificarDatas()) {
                    // pegando data inicial
                    Date dataSelecionada = jXDatePicker1.getDate();
                    // criando um formato de data
                    SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
                    // colocando data selecionado no formato criado acima
                    String diaDoIntervalo = dataFormatada.format(dataSelecionada);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        diaInicialSql = new java.sql.Date(format.parse(diaDoIntervalo).getTime());
                    } catch (ParseException e) {
                        JOptionPane.showMessageDialog(null, "Erro com a data inicial. Procure o Administrador.");
                    }

                    // pegando data Final
                    Date dataSelecionada2 = jXDatePicker2.getDate();
                    // criando um formato de data
                    SimpleDateFormat dataFormatada2 = new SimpleDateFormat("dd/MM/yyyy");
                    // colocando data selecionado no formato criado acima
                    String diaDoIntervalo2 = dataFormatada2.format(dataSelecionada2);
                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        diaFinalSql = new java.sql.Date(format2.parse(diaDoIntervalo2).getTime());
                    } catch (ParseException e) {
                        JOptionPane.showMessageDialog(null, "Erro com a data Final. Procure o Administrador.");
                    }

                    relatorioTodosConveniosTodasClassesAnaliticoValoresEspecificos relatorio =
                        new relatorioTodosConveniosTodasClassesAnaliticoValoresEspecificos(diaInicialSql, diaFinalSql);
                    relatorio.gerarRelatorio();
                } else {
                    JOptionPane.showMessageDialog(null, "Verifique as datas e tente novamente.");
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

    // Um convenio - relatorio por classe de exame - analitico - valores especidicos
    @SuppressWarnings("rawtypes")
    private void relatorioAnaliticoPorClasseDeExamesValoresEspecificosDeUmConvenio() {
        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                java.sql.Date diaInicialSql = null, diaFinalSql = null;
                if (verificarDatas()) {
                    // pegando data inicial
                    Date dataSelecionada = jXDatePicker1.getDate();
                    // criando um formato de data
                    SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
                    // colocando data selecionado no formato criado acima
                    String diaDoIntervalo = dataFormatada.format(dataSelecionada);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        diaInicialSql = new java.sql.Date(format.parse(diaDoIntervalo).getTime());
                    } catch (ParseException e) {
                        JOptionPane.showMessageDialog(null, "Erro com a data inicial. Procure o Administrador.");
                    }

                    // pegando data Final
                    Date dataSelecionada2 = jXDatePicker2.getDate();
                    // criando um formato de data
                    SimpleDateFormat dataFormatada2 = new SimpleDateFormat("dd/MM/yyyy");
                    // colocando data selecionado no formato criado acima
                    String diaDoIntervalo2 = dataFormatada2.format(dataSelecionada2);
                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        diaFinalSql = new java.sql.Date(format2.parse(diaDoIntervalo2).getTime());
                    } catch (ParseException e) {
                        JOptionPane.showMessageDialog(null, "Erro com a data Final. Procure o Administrador.");
                    }

                    int handle_convenio = listaHandleConvenio.get(jCBConvenio.getSelectedIndex());
                    relatorioUmConvenioTodasClassesAnaliticoValoresEspecificos relatorio =
                        new relatorioUmConvenioTodasClassesAnaliticoValoresEspecificos(diaInicialSql, diaFinalSql,
                            handle_convenio);
                    relatorio.gerarRelatorio();
                } else {
                    JOptionPane.showMessageDialog(null, "Verifique as datas e tente novamente.");
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

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jLabel2 = new javax.swing.JLabel();
        jXDatePicker2 = new org.jdesktop.swingx.JXDatePicker();
        jBGerarRelatorio = new javax.swing.JButton();
        jCBConvenio = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();

        jPanel1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Relat\u00F3rio Financeiro dos Valores Recebidos", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, null));

        jLabel1.setText("Data");

        jLabel2.setText("à");

        jBGerarRelatorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemBotaoOk.png"))); // NOI18N
        jBGerarRelatorio.setText("Gerar Relatório");
        jBGerarRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGerarRelatorioActionPerformed(evt);
            }
        });

        jCBConvenio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Convênios", "Modalidades",
            "Classes de Exames", "Médicos" }));

        jLabel4.setText("Convênio:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(Alignment.TRAILING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jBGerarRelatorio, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jXDatePicker1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jLabel2)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jXDatePicker2, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                        .addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jCBConvenio, GroupLayout.PREFERRED_SIZE, 316, GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(5)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jXDatePicker1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(jXDatePicker2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jCBConvenio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jBGerarRelatorio)
                    .addGap(73))
        );
        jPanel1.setLayout(jPanel1Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 145, Short.MAX_VALUE)
                    .addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBGerarRelatorioActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBGerarRelatorioActionPerformed
        if (jCBConvenio.getSelectedIndex() == 0) {

        } else {
            
        }
    }// GEN-LAST:event_jBGerarRelatorioActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBGerarRelatorio;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBConvenio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
    // End of variables declaration//GEN-END:variables
}
