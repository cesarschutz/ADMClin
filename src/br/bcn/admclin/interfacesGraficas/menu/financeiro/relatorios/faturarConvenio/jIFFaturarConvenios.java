/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio;

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

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.CONVENIO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * 
 * @author Cesar Schutz
 */
public class jIFFaturarConvenios extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    public List<Double> listaPorcentagemPaciente = new ArrayList<Double>();
    public List<Double> listaPorcentagemConvenio = new ArrayList<Double>();
    public List<Integer> listaGrupoId = new ArrayList<Integer>();

    private Connection con = null;

    public List<Integer> listaHandleConvenio = new ArrayList<Integer>();

    public ArrayList<atendimentoModel> listaDeAtendimentos = new ArrayList<atendimentoModel>();

    /**
     * Creates new form jIFFaturarConvenios
     */
    public jIFFaturarConvenios() {
        initComponents();
        tirandoBarraDeTitulo();
        preenchendoOsGruposNoComboBox();
        preenchendoOsConveniosNoComboBox();

        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker3.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Ir para data atual");
        jXDatePicker3.setLinkDate(System.currentTimeMillis(), "Ir para data atual");
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    // metodo que preenche os convenios no comboBox
    @SuppressWarnings("unchecked")
    private void preenchendoOsGruposNoComboBox() {
        // preenchendo as Classes de Exames
        con = Conexao.fazConexao();
        ResultSet resultSet = br.bcn.admclin.dao.dbris.CONVENIO.getConsultarGruposDeConvenios(con);
        listaGrupoId.removeAll(listaGrupoId);

        jCBGrupos.addItem("Selecione um Grupo");
        listaGrupoId.add(0);
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("grupo_id") > 0) {
                    jCBGrupos.addItem(resultSet.getString("nome"));
                    int handle_grupo = resultSet.getInt("grupo_id");
                    listaGrupoId.add(handle_grupo);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os Grupos de Convênio. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    // buscamos todos os atendimentos, e dps com eles buscamos os exames daquele atendimento e valores!
    private void buscandoOsAtendimentos(String tp, java.sql.Date dataInicial, java.sql.Date dataFinal, int handle)
        throws SQLException {
        listaDeAtendimentos.removeAll(listaDeAtendimentos);
        ResultSet resultSet;
        if (tp.equals("grupo")) {
            resultSet = atendimentoDAO.getConsultarAtendimentosPorPeriodoEGrupo(con, dataInicial, dataFinal, handle);
        } else {
            resultSet = atendimentoDAO.getConsultarAtendimentosPorPeriodoEConvenio(con, dataInicial, dataFinal, handle);
        }
        while (resultSet.next()) {
            atendimentoModel atendimento = new atendimentoModel();
            atendimento.setHandle_at(resultSet.getInt("handle_at"));
            atendimento.setNomePaciente(resultSet.getString("nomePaciente"));
            atendimento.setMatricula_convenio(resultSet.getString("matricula_convenio"));
            atendimento.setData_atendimento(resultSet.getDate("data_atendimento"));
            atendimento.setCrmMedico(resultSet.getString("crm"));
            if (tp.equals("grupo")) {
                atendimento.setHandle_convenio(resultSet.getInt("handle_convenio"));
            }
            atendimento.setHora(MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento")));
            atendimento.setFlag_laudo(resultSet.getInt("flag_laudo"));
            atendimento.setFlag_faturado(resultSet.getInt("flag_faturado"));
            atendimento.setNomeMedico(resultSet.getString("nomeMedico"));
            atendimento.setID_AREAS_ATENDIMENTO(resultSet.getInt("id_areas_atendimento"));

            listaDeAtendimentos.add(atendimento);
        }
    }

    // metodo que preenche os convenios no comboBox
    @SuppressWarnings("unchecked")
    private void preenchendoOsConveniosNoComboBox() {
        // preenchendo as Classes de Exames
        con = Conexao.fazConexao();
        ResultSet resultSet = CONVENIO.getConsultar(con);
        listaHandleConvenio.removeAll(listaHandleConvenio);
        listaPorcentagemConvenio.removeAll(listaPorcentagemConvenio);
        listaPorcentagemPaciente.removeAll(listaPorcentagemPaciente);

        jCBConvenios.addItem("Selecione um Convênio");
        listaHandleConvenio.add(0);
        listaPorcentagemConvenio.add(0.0);
        listaPorcentagemPaciente.add(0.0);
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("handle_convenio") > 0) {
                    jCBConvenios.addItem(resultSet.getString("nome"));
                    int handle_convenio = resultSet.getInt("handle_convenio");
                    listaHandleConvenio.add(handle_convenio);

                    double porcentagemPaciente = Double.valueOf(resultSet.getString("porcentPaciente"));
                    double porcentagemConvenio = Double.valueOf(resultSet.getString("porcentConvenio"));

                    listaPorcentagemConvenio.add(porcentagemConvenio);
                    listaPorcentagemPaciente.add(porcentagemPaciente);
                }
            }
        } catch (SQLException e) {
            jCBConvenios.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher os Convênios. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
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
            Date dataSelecionada2 = jXDatePicker3.getDate();
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

    // public evento disparado ao apaertar o botao gerar fatura
    private void botaoGerarFaturaConvenio() {
        java.sql.Date diaInicialSql = null, diaFinalSql = null;
        con = Conexao.fazConexao();
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
            Date dataSelecionada2 = jXDatePicker3.getDate();
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

            try {
                buscandoOsAtendimentos("convenio", diaInicialSql, diaFinalSql,
                    listaHandleConvenio.get(jCBConvenios.getSelectedIndex()));
                janelaPrincipal.internalFrameAtendimentosParaFaturar =
                    new jIFListaAtendimentosParaFaturar("convenio", diaInicialSql, diaFinalSql,
                        String.valueOf(jCBConvenios.getSelectedItem()), listaHandleConvenio.get(jCBConvenios
                            .getSelectedIndex()), listaDeAtendimentos);

                janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimentosParaFaturar);
                janelaPrincipal.internalFrameAtendimentosParaFaturar.setVisible(true);

                int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
                int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
                int lIFrame = janelaPrincipal.internalFrameAtendimentosParaFaturar.getWidth();
                int aIFrame = janelaPrincipal.internalFrameAtendimentosParaFaturar.getHeight();

                janelaPrincipal.internalFrameAtendimentosParaFaturar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
                    - aIFrame / 2);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao gerar Fatura.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Verifique as datas e tente novamente.");
        }

    }

    public void botaoGerarFaturaGrupo() {
        java.sql.Date diaInicialSql = null, diaFinalSql = null;
        con = Conexao.fazConexao();
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
            Date dataSelecionada2 = jXDatePicker3.getDate();
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

            try {
                buscandoOsAtendimentos("grupo", diaInicialSql, diaFinalSql,
                    listaGrupoId.get(jCBGrupos.getSelectedIndex()));
                janelaPrincipal.internalFrameAtendimentosParaFaturar =
                    new jIFListaAtendimentosParaFaturar("grupo", diaInicialSql, diaFinalSql, String.valueOf(jCBGrupos
                        .getSelectedItem()), listaGrupoId.get(jCBGrupos.getSelectedIndex()), 0, listaDeAtendimentos);

                janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimentosParaFaturar);
                janelaPrincipal.internalFrameAtendimentosParaFaturar.setVisible(true);

                int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
                int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
                int lIFrame = janelaPrincipal.internalFrameAtendimentosParaFaturar.getWidth();
                int aIFrame = janelaPrincipal.internalFrameAtendimentosParaFaturar.getHeight();

                janelaPrincipal.internalFrameAtendimentosParaFaturar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
                    - aIFrame / 2);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao gerar Fatura.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Verifique as datas e tente novamente.");
        }

    }

    public void abrirJanelaListaDeAtendimentosParaFaturar(final JInternalFrame internalFrame) {

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    @SuppressWarnings("rawtypes")
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jXDatePicker3 = new org.jdesktop.swingx.JXDatePicker();
        jLabel3 = new javax.swing.JLabel();
        jCBConvenios = new javax.swing.JComboBox();
        jBGerarFatura = new javax.swing.JButton();
        jCBGrupos = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jRBGrupo = new javax.swing.JRadioButton();
        jRBConvenio = new javax.swing.JRadioButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Faturamento de Convênios",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Data");

        jLabel2.setText("à");

        jLabel3.setText("Convênio");

        jCBConvenios.setEnabled(false);

        jBGerarFatura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemBotaoOk.png"))); // NOI18N
        jBGerarFatura.setText("Preencher Lista");
        jBGerarFatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGerarFaturaActionPerformed(evt);
            }
        });

        jLabel4.setText("Grupo");

        jLabel5.setText("Gerar fatura por");

        buttonGroup1.add(jRBGrupo);
        jRBGrupo.setSelected(true);
        jRBGrupo.setText("Grupo");
        jRBGrupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBGrupoActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRBConvenio);
        jRBConvenio.setText("Convênio");
        jRBConvenio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBConvenioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jXDatePicker1, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jLabel2)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jXDatePicker3, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jRBGrupo)
                            .addGap(18)
                            .addComponent(jRBConvenio)
                            .addContainerGap())
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addGap(248))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                                .addComponent(jCBConvenios, 0, 410, Short.MAX_VALUE)
                                .addComponent(jCBGrupos, 0, 410, Short.MAX_VALUE)))
                        .addComponent(jBGerarFatura, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jXDatePicker1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jXDatePicker3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jRBGrupo)
                        .addComponent(jRBConvenio))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jCBGrupos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jCBConvenios, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jBGerarFatura)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1.setLayout(jPanel1Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(28, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("rawtypes")
    private void jBGerarFaturaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBGerarFaturaActionPerformed

        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    if (jRBGrupo.isSelected()) {
                        if (jCBGrupos.getSelectedIndex() != 0) {
                            botaoGerarFaturaGrupo();
                        } else {
                            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal,
                                "Selecione um Grupo.");
                        }
                    } else {
                        if (jCBConvenios.getSelectedIndex() != 0) {
                            botaoGerarFaturaConvenio();
                        } else {
                            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal,
                                "Selecione um Convênio.");
                        }
                    }

                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void done() {
                janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
            }
        };
        worker.execute();

    }// GEN-LAST:event_jBGerarFaturaActionPerformed

    private void jRBGrupoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRBGrupoActionPerformed
        jCBGrupos.setEnabled(true);
        jCBConvenios.setEnabled(false);
    }// GEN-LAST:event_jRBGrupoActionPerformed

    private void jRBConvenioActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRBConvenioActionPerformed
        jCBGrupos.setEnabled(false);
        jCBConvenios.setEnabled(true);
    }// GEN-LAST:event_jRBConvenioActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jBGerarFatura;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBConvenios;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBGrupos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRBConvenio;
    private javax.swing.JRadioButton jRBGrupo;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker3;
    // End of variables declaration//GEN-END:variables
}
