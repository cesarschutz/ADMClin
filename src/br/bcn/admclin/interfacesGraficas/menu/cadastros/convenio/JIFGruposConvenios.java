/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.dao.dbris.CONVENIO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.DADOS_EMPRESA;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JComboBox;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFGruposConvenios extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    Connection con;
	public List<Integer> lista_id_empresa = new ArrayList<Integer>();


    /**
     * Creates new form JIFGruposConvenios
     */
    public JIFGruposConvenios() {
        initComponents();
        tirandoBarraDeTitulo();
        jBEditar.setVisible(false);
        jBDeletar.setVisible(false);
        jTFNome.setDocument(new DocumentoSemAspasEPorcento(64));
        preencherEmpresas();
    }

    int grupo_id;

    public JIFGruposConvenios(int grupo_id, int gera_arquivo_texto, String nome, int id_empresa) {
        initComponents();
        tirandoBarraDeTitulo();
        jBSalvar.setVisible(false);
        jTFNome.setDocument(new DocumentoSemAspasEPorcento(64));
        this.grupo_id = grupo_id;
        jTFNome.setText(nome);
        jCBGeraArquivoTexto.setSelectedIndex(gera_arquivo_texto);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Grupo de Convênios",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        preencherEmpresas();
        for (int x = 0; x < lista_id_empresa.size(); x++) {
			if (lista_id_empresa.get(x) == id_empresa) {
				jCBEmpresa.setSelectedIndex(x);
			}
		}
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    private boolean tudoPreenchido() {
        if (jTFNome.getText().length() >= 3) {
            return true;
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Nome deve ter no mínimo 3 caracteres.");
            return false;
        }
    }
    
    public void preencherEmpresas() {
		con = Conexao.fazConexao();
		try {
			ResultSet resultSet = DADOS_EMPRESA.getConsultarEmpresa(con);
			while (resultSet.next()) {
				if (resultSet.getInt("dados_empresa_id") != 0) {
					lista_id_empresa.add(resultSet
							.getInt("dados_empresa_id"));
					jCBEmpresa.addItem(resultSet.getString("nome"));

				}

			}
			Conexao.fechaConexao(con);
		} catch (Exception e) {
			this.dispose();
			janelaPrincipal.internalFrameConvenios = null;
		}
	}

    private void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFrameGruposDeConvenios = null;

        janelaPrincipal.internalFrameGruposConveniosVisualizar = new JIFGruposConveniosVisualizar();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameGruposConveniosVisualizar);
        janelaPrincipal.internalFrameGruposConveniosVisualizar.setVisible(true);

        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameGruposConveniosVisualizar.getWidth();
        int aIFrame = janelaPrincipal.internalFrameGruposConveniosVisualizar.getHeight();

        janelaPrincipal.internalFrameGruposConveniosVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame
            / 2);
    }

    private void botaoSalvar() {
        if (tudoPreenchido()) {
            con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
            int id_empresa = lista_id_empresa.get(jCBEmpresa.getSelectedIndex());
            boolean cadastro =
                CONVENIO.setCadastrarGrupoDeConvenio(con, jTFNome.getText(), jCBGeraArquivoTexto.getSelectedIndex(), id_empresa);
            br.bcn.admclin.dao.dbris.Conexao.fechaConexao(con);
            if (cadastro) {
                botaoCancelar();
            }
        }
    }

    private void botaoAtualizar() {
        if (tudoPreenchido()) {
            con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
            int id_empresa = lista_id_empresa.get(jCBEmpresa.getSelectedIndex());
            boolean cadastro =
                CONVENIO.setUpdateGrupoDeConvenio(con, jTFNome.getText(), jCBGeraArquivoTexto.getSelectedIndex(),
                    grupo_id, id_empresa);
            br.bcn.admclin.dao.dbris.Conexao.fechaConexao(con);
            if (cadastro) {
                botaoCancelar();
            }
        }
    }

    private void botaoDeletar() {
        int resposta =
            JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Grupo de Convênio?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
            boolean deleto = CONVENIO.setDeletarGrupoDeConveio(con, grupo_id);
            br.bcn.admclin.dao.dbris.Conexao.fechaConexao(con);
            if (deleto) {
                botaoCancelar();
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFNome = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jCBGeraArquivoTexto = new javax.swing.JComboBox();
        jBDeletar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jBEditar = new javax.swing.JButton();
        jBSalvar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar novo Grupo de Convênios",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Nome");

        jLabel2.setText("Gera arquivo texto?");

        jCBGeraArquivoTexto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Não", "Sim" }));
        
        JLabel lblEmpresa = new JLabel("Empresa:");
        
        jCBEmpresa = new JComboBox();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel1)
        				.addComponent(jTFNome, GroupLayout.PREFERRED_SIZE, 485, GroupLayout.PREFERRED_SIZE)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel2)
        						.addComponent(lblEmpresa))
        					.addGap(18)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jCBEmpresa, GroupLayout.PREFERRED_SIZE, 291, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jCBGeraArquivoTexto, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))))
        			.addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addComponent(jLabel1)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jTFNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel2)
        				.addComponent(jCBGeraArquivoTexto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblEmpresa)
        				.addComponent(jCBEmpresa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1.setLayout(jPanel1Layout);

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

        jBEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBEditar.setText("Atualizar");
        jBEditar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEditarActionPerformed(evt);
            }
        });
        jBEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBEditarKeyReleased(evt);
            }
        });

        jBSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBSalvar.setText("Salvar");
        jBSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarActionPerformed(evt);
            }
        });
        jBSalvar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarKeyReleased(evt);
            }
        });

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jTFMensagemParaUsuario, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
        			.addContainerGap())
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jBCancelar)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBSalvar)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBEditar)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBDeletar)
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
        			.addGap(13)
        			.addComponent(jTFMensagemParaUsuario, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(jBSalvar)
        					.addComponent(jBEditar)
        					.addComponent(jBDeletar))
        				.addComponent(jBCancelar, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
        			.addGap(58))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBDeletarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBDeletarActionPerformed
        botaoDeletar(); // TODO add your handling code here:
    }// GEN-LAST:event_jBDeletarActionPerformed

    private void jBDeletarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBDeletarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoDeletar();
        }
    }// GEN-LAST:event_jBDeletarKeyReleased

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jBEditarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBEditarActionPerformed
        botaoAtualizar();
    }// GEN-LAST:event_jBEditarActionPerformed

    private void jBEditarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBEditarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizar();
        }
    }// GEN-LAST:event_jBEditarKeyReleased

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarActionPerformed
        botaoSalvar();
    }// GEN-LAST:event_jBSalvarActionPerformed

    private void jBSalvarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvar();
        }
    }// GEN-LAST:event_jBSalvarKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBDeletar;
    private javax.swing.JButton jBEditar;
    private javax.swing.JButton jBSalvar;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBGeraArquivoTexto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFNome;
    private JComboBox<String> jCBEmpresa;
}
