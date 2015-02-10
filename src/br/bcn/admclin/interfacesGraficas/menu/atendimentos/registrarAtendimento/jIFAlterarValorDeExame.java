/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import br.bcn.admclin.ClasseAuxiliares.JTextFieldDinheiroReais;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * 
 * @author Cesar Schutz
 */
public class jIFAlterarValorDeExame extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    /**
     * Creates new form jIFAlterarValorDeExame
     */

    java.sql.Date dataDeHojeEmVariavelDate = null;

    public void pegandoDataDoSistema() {
        // pegando data do sistema
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dataDeHoje = format.format(hoje.getTime());
        try {
            dataDeHojeEmVariavelDate = new java.sql.Date(format.parse(dataDeHoje).getTime());
        } catch (ParseException ex) {

        }
    }

    int linhaSelecionadaNaTabela = 0;
    double porcentagem_paciente;
    double porcentagem_convenio;

    public jIFAlterarValorDeExame(String nomeExame, int linhaDaTabelaSelecionada, double porcentagem_convenio,
        double porcentagem_paciente) {
        initComponents();
        tirandoBarraDeTitulo();
        jTFValorConvenio.setEnabled(false);
        jTFValorPaciente.setEnabled(false);
        this.porcentagem_convenio = porcentagem_convenio;
        this.porcentagem_paciente = porcentagem_paciente;
        this.linhaSelecionadaNaTabela = linhaDaTabelaSelecionada;
        String valorExame = JIFCadastroDeAtendimento.jTable1.getValueAt(linhaDaTabelaSelecionada, 3).toString().replaceAll("\\.", ",");
        String valorExameDividido[] = valorExame.split(",");
        if(valorExameDividido[1].length() < 2){
        	valorExame = valorExame + "0";
        }
        
        jTFValorExame.setText(valorExame);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Exame: " + nomeExame,
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void botaoCancelar() {
        // se ocorrer erro aqui, é pq esta editando um atendimento apartir da janela de pesquisa, entao vai dar erro
        // no JIFAgendaPrincipal, por isso fechamos o outro ;D
        janelaPrincipal.internalFrameAtendimento.setVisible(true);

        janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento.dispose();
        janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento = null;
    }

    private void botaoSalvar() {
        if (jComboBox1.getSelectedIndex() == 0) {
            salvarValorExame();
        } else {
            salvarValorConvenioPaciente();
        }
    }

    private void salvarValorExame() {
        double valorPaciente, valorConvenio, valorExame;
        valorExame =
            new BigDecimal(Double.valueOf(jTFValorExame.getText().replace(",", "."))).setScale(2,
                RoundingMode.HALF_EVEN).doubleValue();

        valorPaciente =
            new BigDecimal(valorExame * (porcentagem_paciente / 100)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        valorConvenio =
            new BigDecimal(valorExame * (porcentagem_convenio / 100)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        JIFCadastroDeAtendimento.jTable1.setValueAt(valorExame, linhaSelecionadaNaTabela, 3);// valor
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 6);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 7);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 8);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 9);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 10);
        JIFCadastroDeAtendimento.jTable1.setValueAt("", linhaSelecionadaNaTabela, 11);

        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 12);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 13);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 14);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 15);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorExame, linhaSelecionadaNaTabela, 16);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorConvenio, linhaSelecionadaNaTabela, 17);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorPaciente, linhaSelecionadaNaTabela, 18);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorConvenio, linhaSelecionadaNaTabela, 19);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorPaciente, linhaSelecionadaNaTabela, 20);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 21);

        botaoCancelar();

        JIFCadastroDeAtendimento.calcularValoresApartirDaTabela();

    }

    private void salvarValorConvenioPaciente() {

        double valorPaciente, valorConvenio, valorExame;
        valorPaciente =
            new BigDecimal(Double.valueOf(jTFValorPaciente.getText().replace(",", "."))).setScale(2,
                RoundingMode.HALF_EVEN).doubleValue();
        valorConvenio =
            new BigDecimal(Double.valueOf(jTFValorConvenio.getText().replace(",", "."))).setScale(2,
                RoundingMode.HALF_EVEN).doubleValue();

        valorExame = new BigDecimal(valorConvenio + valorPaciente).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        JIFCadastroDeAtendimento.jTable1.setValueAt(valorExame, linhaSelecionadaNaTabela, 3);// valor
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 6);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 7);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 8);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 9);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 10);
        JIFCadastroDeAtendimento.jTable1.setValueAt("", linhaSelecionadaNaTabela, 11);

        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 12);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 13);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 14);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 15);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorExame, linhaSelecionadaNaTabela, 16);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorConvenio, linhaSelecionadaNaTabela, 17);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorPaciente, linhaSelecionadaNaTabela, 18);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorConvenio, linhaSelecionadaNaTabela, 19);
        JIFCadastroDeAtendimento.jTable1.setValueAt(valorPaciente, linhaSelecionadaNaTabela, 20);
        JIFCadastroDeAtendimento.jTable1.setValueAt("0", linhaSelecionadaNaTabela, 21);

        botaoCancelar();

        JIFCadastroDeAtendimento.calcularValoresApartirDaTabela();

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
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTFValorExame = new JTextFieldDinheiroReais(new DecimalFormat("0.00")) {
            private static final long serialVersionUID = 1L;

            {// limita a 8
             // caracteres
                setLimit(8);
            }
        };
        jTFValorConvenio = new JTextFieldDinheiroReais(new DecimalFormat("0.00")) {
            private static final long serialVersionUID = 1L;

            {// limita a 8
             // caracteres
                setLimit(8);
            }
        };
        jTFValorConvenio.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusLost(FocusEvent arg0) {
        		double valorExame    = new BigDecimal(Double.valueOf(jTFValorExame.getText().replaceAll(",", "."))).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
        		double valorConvenio = new BigDecimal(Double.valueOf(jTFValorConvenio.getText().replaceAll(",", "."))).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
        		double valorPaciente = new BigDecimal(valorExame - valorConvenio).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
        		
        		if(valorPaciente >= 0){
        			String valorPacienteString = String.valueOf(valorPaciente).replaceAll("\\.", ",");
        			String valorPacienteDividido[] = valorPacienteString.split(",");
        	        if(valorPacienteDividido[1].length() < 2){
        	        	valorPacienteString = valorPacienteString + "0";
        	        }
        			jTFValorPaciente.setText(valorPacienteString);
        		}else{
        			jTFValorPaciente.setText("00,00");
        		}
        	}
        });
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTFValorPaciente = new JTextFieldDinheiroReais(new DecimalFormat("0.00")) {
            private static final long serialVersionUID = 1L;

            {// limita a 8
             // caracteres
                setLimit(8);
            }
        };
        jTFValorPaciente.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusLost(FocusEvent arg0) {
        		double valorExame    = new BigDecimal(Double.valueOf(jTFValorExame.getText().replaceAll(",", "."))).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
        		double valorPaciente = new BigDecimal(Double.valueOf(jTFValorPaciente.getText().replaceAll(",", "."))).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
        		double valorConvenio = new BigDecimal(valorExame - valorPaciente).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
        		
        		
        		if(valorConvenio >= 0){
        			String valorConvenioString = String.valueOf(valorConvenio).replaceAll("\\.", ",");
        			String valorConvenioDividido[] = valorConvenioString.split(",");
        	        if(valorConvenioDividido[1].length() < 2){
        	        	valorConvenioString = valorConvenioString + "0";
        	        }
        			jTFValorConvenio.setText(valorConvenioString);
        		}else{
        			jTFValorConvenio.setText("00,00");
        		}
        	}
        });
        jBCancelar = new javax.swing.JButton();
        jBOkExame3 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Alterar Valor de Exame",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
            java.awt.Color.black));

        jLabel1.setText("Alterar por");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Valor do Exame",
            "Valor do Convênio e do Paciente" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Valor Exame");

        jLabel3.setText("Valor Convênio");

        jLabel4.setText("Valor Paciente");

        jBCancelar.setBackground(new java.awt.Color(113, 144, 224));
        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBCancelar.setText("Voltar");
        jBCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });
        jBCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBCancelarKeyReleased(evt);
            }
        });

        jBOkExame3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBOkExame3.setText("Salvar");
        jBOkExame3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBOkExame3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBOkExame3ActionPerformed(evt);
            }
        });
        jBOkExame3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBOkExame3KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout
            .setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
                                            javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(
                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jBCancelar)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jBOkExame3))
                                                .addGroup(
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    jPanel1Layout
                                                        .createParallelGroup(
                                                            javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(
                                                            javax.swing.GroupLayout.Alignment.LEADING,
                                                            jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(
                                                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                    Short.MAX_VALUE)
                                                                .addComponent(jTFValorExame,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 96,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(
                                                            javax.swing.GroupLayout.Alignment.LEADING,
                                                            jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addComponent(jLabel4)
                                                                .addPreferredGap(
                                                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                    Short.MAX_VALUE)
                                                                .addComponent(jTFValorPaciente,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 96,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(
                                                            javax.swing.GroupLayout.Alignment.LEADING,
                                                            jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addComponent(jLabel3)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jTFValorConvenio,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 96,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTFValorExame, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTFValorConvenio, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTFValorPaciente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jBOkExame3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39,
                                javax.swing.GroupLayout.PREFERRED_SIZE))));

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

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox1ActionPerformed
        if (jComboBox1.getSelectedIndex() == 0) {
            jTFValorConvenio.setEnabled(false);
            jTFValorConvenio.setText("");
            jTFValorPaciente.setEnabled(false);
            jTFValorPaciente.setText("");

            jTFValorExame.setEnabled(true);
        } else {
            jTFValorExame.setEnabled(false);

            jTFValorPaciente.setEnabled(true);
            jTFValorConvenio.setEnabled(true);
        }
    }// GEN-LAST:event_jComboBox1ActionPerformed

    private void jBOkExame3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBOkExame3ActionPerformed
        botaoSalvar();
    }// GEN-LAST:event_jBOkExame3ActionPerformed

    private void jBOkExame3KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBOkExame3KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvar();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBOkExame3KeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBOkExame3;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFValorConvenio;
    private javax.swing.JTextField jTFValorExame;
    private javax.swing.JTextField jTFValorPaciente;
    // End of variables declaration//GEN-END:variables
}
