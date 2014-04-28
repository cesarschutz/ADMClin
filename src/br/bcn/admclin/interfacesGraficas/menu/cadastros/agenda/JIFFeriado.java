/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCadastrarEditarIntervaloPorHorario.java
 *
 * Created on 15/08/2012, 15:35:09
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.dao.dbris.A_FERIADOS;
import br.bcn.admclin.dao.dbris.A_FERIADOSN;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.A_feriados;
import br.bcn.admclin.dao.model.A_feriadosN;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFFeriado extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;

    public List<Integer> listaHandleAgendas = new ArrayList<Integer>();

    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;
    String novoOuEditar = null;
    public static int handleFeriadoN = 0;

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

    /** Creates new form JIFCadastrarEditarferiado */
    @SuppressWarnings("static-access")
    public JIFFeriado(String novoOuEditar, int handleFeriadoN) {
        initComponents();

        this.novoOuEditar = novoOuEditar;
        pegandoDataDoSistema();

        if ("novo".equals(novoOuEditar)) {
            jBEditar.setVisible(false);
            jBDeletar.setVisible(false);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar novo Feriado",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }
        if ("editar".equals(novoOuEditar)) {
            jBSalvar.setVisible(false);
            this.handleFeriadoN = handleFeriadoN;
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Feriado",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            preenchendoDadosDoIntervalo();
        }

        jTFDiaDoIntervalo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tirandoBarraDeTitulo();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void preenchendoDadosDoIntervalo() {
        // colocando os valores
        con = Conexao.fazConexao();
        ResultSet resultSet = A_FERIADOSN.getConsultarDadosDeUmFeriado(con, handleFeriadoN);
        try {
            while (resultSet.next()) {
                // colocando dados na nos campos
                jTFNome.setText(resultSet.getString("nome"));
                jTADescricao.setText(resultSet.getString("descricao"));
                jTFDiaDoIntervalo.setText(resultSet.getString("diaDoferiado"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher dados do Feriado. Procure o administrador",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    public boolean verificandoSeTudoFoiPreenchido() {
        boolean nomeOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 2, jTFMensagemParaUsuario);
        boolean diaDoIntervaloOk =
            MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFDiaDoIntervalo, jTFMensagemParaUsuario, "  /  ");

        // verificar se dia do feriado foi preenchido corretamente
        if (diaDoIntervaloOk) {
            boolean mesOk = false;
            String[] diaEMes = jTFDiaDoIntervalo.getText().split("/");

            int dia = Integer.valueOf(diaEMes[0]);
            int mes = Integer.valueOf(diaEMes[1]);

            // verificando se mes esta ok
            if (mes < 1 || mes > 12) {
                jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Data Inválida");
            } else {
                mesOk = true;
            }

            // se mes esta ok agora vamos verificar o dia
            if (mesOk) {
                if (mes == 1 && (dia < 1 || dia > 31)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                // mes fevereiro
                if (mes == 2 && (dia < 1 || dia > 29)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                // mes março
                if (mes == 3 && (dia < 1 || dia > 31)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                if (mes == 4 && (dia < 1 || dia > 30)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                if (mes == 5 && (dia < 1 || dia > 31)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                if (mes == 6 && (dia < 1 || dia > 30)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                if (mes == 7 && (dia < 1 || dia > 31)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                if (mes == 8 && (dia < 1 || dia > 31)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                if (mes == 9 && (dia < 1 || dia > 30)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                if (mes == 10 && (dia < 1 || dia > 31)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                if (mes == 11 && (dia < 1 || dia > 30)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }

                if (mes == 12 && (dia < 1 || dia > 31)) {
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
            } else {
                diaDoIntervaloOk = false;
            }

        }

        if (nomeOk && diaDoIntervaloOk) {
            return true;
        } else {
            return false;
        }
    }

    
    public void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFrameFeriado = null;

        janelaPrincipal.internalFrameFeriadoVisualizar = new JIFFeriadoVisualizar();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameFeriadoVisualizar);
        janelaPrincipal.internalFrameFeriadoVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameFeriadoVisualizar.getWidth();
        int aIFrame = janelaPrincipal.internalFrameFeriadoVisualizar.getHeight();

        janelaPrincipal.internalFrameFeriadoVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }

    public void botaoSalvar() {
        if (verificandoSeTudoFoiPreenchido()) {
                con = Conexao.fazConexao();
                A_feriadosN feriadoNModel = new A_feriadosN();
                feriadoNModel.setNome(jTFNome.getText().toUpperCase());
                boolean existe = A_FERIADOSN.getConsultarParaSalvarRegistro(con, feriadoNModel);
                Conexao.fechaConexao(con);
                if (A_FERIADOSN.conseguiuConsulta) {
                    if (existe) {
                        JOptionPane.showMessageDialog(null, "Feriado já existe", "ATENÇÃO",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // fazer a inserção no banco
                        con = Conexao.fazConexao();
                        feriadoNModel.setDat(dataDeHojeEmVariavelDate);
                        feriadoNModel.setUsuarioId(USUARIOS.usrId);
                        feriadoNModel.setDescricao(jTADescricao.getText());
                        feriadoNModel.setDiaDoFeriado(jTFDiaDoIntervalo.getText());

                        boolean cadastro = A_FERIADOSN.setCadastrar(con, feriadoNModel);
                        Conexao.fechaConexao(con);
                        if (cadastro) {
                            botaoCancelar();
                        }
                    }
                }
        }
    }

    public void botaoDeletar() {
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Feriado?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            con = Conexao.fazConexao();
            A_FERIADOSN.setDeletar(con, handleFeriadoN);
            Conexao.fechaConexao(con);
            botaoCancelar();
        }

    }

    public void botaoAtualizar() {
        if (verificandoSeTudoFoiPreenchido()) {
                con = Conexao.fazConexao();
                A_feriadosN feriadoNModel = new A_feriadosN();
                feriadoNModel.setNome(jTFNome.getText().toUpperCase());
                feriadoNModel.setHandleFeriadoN(handleFeriadoN);
                boolean existe = A_FERIADOSN.getConsultarParaAtualizarRegistro(con, feriadoNModel);
                Conexao.fechaConexao(con);
                if (A_FERIADOSN.conseguiuConsulta) {
                    if (existe) {
                        JOptionPane.showMessageDialog(null, "Feriado já existe", "ATENÇÃO",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // fazer a inserção no banco
                        con = Conexao.fazConexao();
                        feriadoNModel.setDat(dataDeHojeEmVariavelDate);
                        feriadoNModel.setUsuarioId(USUARIOS.usrId);
                        feriadoNModel.setDescricao(jTADescricao.getText());
                        feriadoNModel.setDiaDoFeriado(jTFDiaDoIntervalo.getText());

                        boolean cadastro = A_FERIADOSN.setAtualizar(con, feriadoNModel);
                        Conexao.fechaConexao(con);
                        if (cadastro) {
                            botaoCancelar();
                        }
                    }
                }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jBSalvar = new javax.swing.JButton();
        jBEditar = new javax.swing.JButton();
        jBDeletar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTFNome = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jLabel1 = new javax.swing.JLabel();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFDiaDoIntervalo = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##/##"));
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTADescricao = new javax.swing.JTextArea(new DocumentoSemAspasEPorcento(500));
        jLabel5 = new javax.swing.JLabel();

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jBSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBSalvar.setText("Salvar");
        jBSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarActionPerformed(evt);
            }
        });
        jBSalvar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBSalvarFocusLost(evt);
            }
        });
        jBSalvar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarKeyReleased(evt);
            }
        });

        jBEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBEditar.setText("Atualizar");
        jBEditar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEditarActionPerformed(evt);
            }
        });
        jBEditar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBEditarFocusLost(evt);
            }
        });
        jBEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBEditarKeyReleased(evt);
            }
        });

        jBDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/deletar.png"))); // NOI18N
        jBDeletar.setText("Apagar");
        jBDeletar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDeletarActionPerformed(evt);
            }
        });
        jBDeletar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBDeletarKeyReleased(evt);
            }
        });

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "aaa",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFNomeFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNomeFocusLost(evt);
            }
        });

        jLabel1.setText("Dia do Feriado");

        jTFDiaDoIntervalo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFDiaDoIntervaloFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFDiaDoIntervaloFocusLost(evt);
            }
        });

        jLabel3.setText("Nome");

        jTADescricao.setColumns(20);
        jTADescricao.setRows(5);
        jScrollPane2.setViewportView(jTADescricao);

        jLabel5.setText("Descrição");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18)
                            .addComponent(jTFDiaDoIntervalo, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5))
                            .addGap(41)
                            .addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTFNome, GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE))))
                    .addGap(170))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTFDiaDoIntervalo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jTFNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGap(18)
                    .addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        jPanel2.setLayout(jPanel2Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jBCancelar)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jBSalvar)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jBEditar)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jBDeletar))
                        .addComponent(jTFMensagemParaUsuario)
                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, 453, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jTFMensagemParaUsuario, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE, false)
                            .addComponent(jBSalvar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBEditar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBDeletar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jBCancelar, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarActionPerformed
        botaoSalvar();
    }// GEN-LAST:event_jBSalvarActionPerformed

    private void jBSalvarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvar();
        }
    }// GEN-LAST:event_jBSalvarKeyReleased

    private void jBSalvarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 255, 255)); // TODO add your handling code here:
    }// GEN-LAST:event_jBSalvarFocusLost

    private void jBDeletarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBDeletarActionPerformed
        botaoDeletar();
    }// GEN-LAST:event_jBDeletarActionPerformed

    private void jBDeletarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBDeletarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoDeletar();
        }
    }// GEN-LAST:event_jBDeletarKeyReleased

    private void jBEditarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBEditarActionPerformed
        botaoAtualizar();
    }// GEN-LAST:event_jBEditarActionPerformed

    private void jBEditarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBEditarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizar();
        }
    }// GEN-LAST:event_jBEditarKeyReleased

    private void jBEditarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBEditarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBEditarFocusLost

    private void jTFNomeFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNomeFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 2 caracteres");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jTFNomeFocusGained

    private void jTFNomeFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNomeFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 2, jTFMensagemParaUsuario);
        jTFMensagemParaUsuario.setText("");
        if (ok) {
            jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        }
    }// GEN-LAST:event_jTFNomeFocusLost

    private void jTFDiaDoIntervaloFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFDiaDoIntervaloFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("dd/mm");
        jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jTFDiaDoIntervaloFocusGained

    private void jTFDiaDoIntervaloFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFDiaDoIntervaloFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok =
            MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFDiaDoIntervalo, jTFMensagemParaUsuario, "  /  ");
        jTFMensagemParaUsuario.setText("");
        if (ok) {
            jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 255, 255));
        }
    }// GEN-LAST:event_jTFDiaDoIntervaloFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jBCancelar;
    public static javax.swing.JButton jBDeletar;
    public static javax.swing.JButton jBEditar;
    public static javax.swing.JButton jBSalvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTADescricao;
    public static javax.swing.JTextField jTFDiaDoIntervalo;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFNome;
    // End of variables declaration//GEN-END:variables
}
