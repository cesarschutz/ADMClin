/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.atendimentos;

import br.bcn.admclin.dao.CONVENIO;
import br.bcn.admclin.dao.Conexao;
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

/**
 * 
 * @author Cesar Schutz
 */
public class jIFFinanceiroAtendimentos extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private Connection con = null;
    public List<Integer> listaHandleConvenio = new ArrayList<Integer>();

    /**
     * Creates new form jIFFinanceiroAtendimentos
     */
    public jIFFinanceiroAtendimentos() {
        initComponents();
        tirandoBarraDeTitulo();

        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker2.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Ir para data atual");
        jXDatePicker2.setLinkDate(System.currentTimeMillis(), "Ir para data atual");

        preencherComboBoxComConvenios();

        // aqui vamos sumir as coisas or enquanto!
        jCBTipoDeRelatorio.setSelectedIndex(1);
        jCBValor.setSelectedIndex(1);

        jLabel3.setVisible(false);
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);

        jCBRelatorioPor.setVisible(false);
        jCBTipoDeRelatorio.setVisible(false);
        jCBValor.setVisible(false);
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
        jLabel3 = new javax.swing.JLabel();
        jCBRelatorioPor = new javax.swing.JComboBox();
        jBGerarRelatorio = new javax.swing.JButton();
        jCBConvenio = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jCBTipoDeRelatorio = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jCBValor = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Relatório Financeiro dos Atendimentos",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Data");

        jLabel2.setText("à");

        jLabel3.setText("Relatório por:");

        jCBRelatorioPor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Classes de Exames", "Médicos" }));

        jBGerarRelatorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/imagemBotaoOk.png"))); // NOI18N
        jBGerarRelatorio.setText("Gerar Relatório");
        jBGerarRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGerarRelatorioActionPerformed(evt);
            }
        });

        jCBConvenio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Convênios", "Modalidades",
            "Classes de Exames", "Médicos" }));

        jLabel4.setText("Convênio:");

        jCBTipoDeRelatorio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sintético", "Analítico" }));

        jLabel5.setText("Tipo de Relatório:");

        jCBValor
            .setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Valores Totais", "Valores Específicos" }));

        jLabel6.setText("Valor:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBGerarRelatorio, javax.swing.GroupLayout.Alignment.TRAILING,
                javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel1Layout
                    .createSequentialGroup()
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jCBValor, javax.swing.GroupLayout.PREFERRED_SIZE, 274,
                        javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel1Layout
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(
                        javax.swing.GroupLayout.Alignment.LEADING,
                        jPanel1Layout
                            .createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jCBTipoDeRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 274,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel2)
                                    .addGap(18, 18, 18)
                                    .addComponent(jXDatePicker2, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3).addComponent(jLabel4))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jCBRelatorioPor, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                            .addComponent(jCBConvenio, javax.swing.GroupLayout.PREFERRED_SIZE, 299,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))))));
        jPanel1Layout
            .setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel1Layout
                        .createSequentialGroup()
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(jXDatePicker2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jCBConvenio, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jCBRelatorioPor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jCBTipoDeRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jCBValor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBGerarRelatorio)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
            javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
            javax.swing.GroupLayout.PREFERRED_SIZE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBGerarRelatorioActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBGerarRelatorioActionPerformed
        if (jCBConvenio.getSelectedIndex() == 0) {
            // todos os convenios
            if (jCBRelatorioPor.getSelectedIndex() == 0) {
                // por classes de exames
                if (jCBTipoDeRelatorio.getSelectedIndex() == 0) {
                    // relatorio sintetico
                    if (jCBValor.getSelectedIndex() == 0) {
                        // valores totais
                        JOptionPane.showMessageDialog(null,
                            "todos os convenios - relatorio por classe de exame - sintetico - valores totais");
                    } else if (jCBValor.getSelectedIndex() == 1) {
                        // valores especificos
                        JOptionPane.showMessageDialog(null,
                            "todos os convenios - relatorio por classe de exame - sintetico - valores especificos");
                    }
                } else if (jCBTipoDeRelatorio.getSelectedIndex() == 1) {
                    // relatorio analitico
                    if (jCBValor.getSelectedIndex() == 0) {
                        // valores totais
                        JOptionPane.showMessageDialog(null,
                            "todos os convenios - relatorio por classe de exame - analitico - valores totais");
                    } else if (jCBValor.getSelectedIndex() == 1) {
                        // valores especificos
                        // todos os convenios - relatorio por classe de exame - analitico - valores especidicos
                        relatorioAnaliticoPorClasseDeExamesValoresEspecificosDeTodosOsConvenios();
                    }
                }
            } else if (jCBRelatorioPor.getSelectedIndex() == 1) {
                // por medicos
                if (jCBTipoDeRelatorio.getSelectedIndex() == 0) {
                    // relatorio sintetico
                    if (jCBValor.getSelectedIndex() == 0) {
                        // valores totais
                        JOptionPane.showMessageDialog(null,
                            "todos os convenios - relatorio por medicos - sintetico - valores totais");
                    } else if (jCBValor.getSelectedIndex() == 1) {
                        // valores especificos
                        JOptionPane.showMessageDialog(null,
                            "todos os convenios - relatorio por medicos - sintetico - valores especificos");
                    }
                } else if (jCBTipoDeRelatorio.getSelectedIndex() == 1) {
                    // relatorio analitico
                    if (jCBValor.getSelectedIndex() == 0) {
                        // valores totais
                        JOptionPane.showMessageDialog(null,
                            "todos os convenios - relatorio por medicos - analitico - valores totais");
                    } else if (jCBValor.getSelectedIndex() == 1) {
                        // valores especificos
                        JOptionPane.showMessageDialog(null,
                            "todos os convenios - relatorio por medicos - analitico - valores especificos");
                    }
                }

            }
        } else {
            // um convenio especifico
            if (jCBRelatorioPor.getSelectedIndex() == 0) {
                // por classes de exames
                if (jCBTipoDeRelatorio.getSelectedIndex() == 0) {
                    // relatorio sintetico
                    if (jCBValor.getSelectedIndex() == 0) {
                        // valores totais
                        JOptionPane.showMessageDialog(null,
                            "convenio especifico - relatorio por classe de exame - sintetico - valores totais");
                    } else if (jCBValor.getSelectedIndex() == 1) {
                        // valores especificos
                        JOptionPane.showMessageDialog(null,
                            "convenio especifico - relatorio por classe de exame - sintetico - valores especificos");
                    }
                } else if (jCBTipoDeRelatorio.getSelectedIndex() == 1) {
                    // relatorio analitico
                    if (jCBValor.getSelectedIndex() == 0) {
                        // valores totais
                        JOptionPane.showMessageDialog(null,
                            "convenio especifico - relatorio por classe de exame - analitico - valores totais");
                    } else if (jCBValor.getSelectedIndex() == 1) {
                        // valores especificos
                        // convenio especifico - relatorio por classe de exame - analitico - valores especificos
                        relatorioAnaliticoPorClasseDeExamesValoresEspecificosDeUmConvenio();
                    }
                }
            } else if (jCBRelatorioPor.getSelectedIndex() == 1) {
                // por medicos
                if (jCBTipoDeRelatorio.getSelectedIndex() == 0) {
                    // relatorio sintetico
                    if (jCBValor.getSelectedIndex() == 0) {
                        // valores totais
                        JOptionPane.showMessageDialog(null,
                            "convenio especifico - relatorio por medicos - sintetico - valores totais");
                    } else if (jCBValor.getSelectedIndex() == 1) {
                        // valores especificos
                        JOptionPane.showMessageDialog(null,
                            "convenio especifico - relatorio por medicos - sintetico - valores especificos");
                    }
                } else if (jCBTipoDeRelatorio.getSelectedIndex() == 1) {
                    // relatorio analitico
                    if (jCBValor.getSelectedIndex() == 0) {
                        // valores totais
                        JOptionPane.showMessageDialog(null,
                            "convenio especifico - relatorio por medicos - analitico - valores totais");
                    } else if (jCBValor.getSelectedIndex() == 1) {
                        // valores especificos
                        JOptionPane.showMessageDialog(null,
                            "convenio especifico - relatorio por medicos - analitico - valores especificos");
                    }
                }

            }
        }
    }// GEN-LAST:event_jBGerarRelatorioActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBGerarRelatorio;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBConvenio;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBRelatorioPor;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBTipoDeRelatorio;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBValor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
    // End of variables declaration//GEN-END:variables
}
