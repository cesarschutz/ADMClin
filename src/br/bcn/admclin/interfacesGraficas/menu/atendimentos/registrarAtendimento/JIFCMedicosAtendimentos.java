/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCMedicos.java
 *
 * Created on 07/05/2012, 13:52:58
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento;

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
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteLetras;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.ESPECIALIDADES_MEDICAS;
import br.bcn.admclin.dao.dbris.MEDICOS;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.Especialidades_Medicas;
import br.bcn.admclin.dao.model.Medicos;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.text.Document;

/**
 * 
 * @author Cesar Schutz
 */
@SuppressWarnings("unchecked")
public class JIFCMedicosAtendimentos extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    String novoOuEditar;
    int medicoId = 0;
    // criando lista para objetos do banco
    public List<Integer> listaCodEspecialidadesMedicas = new ArrayList<Integer>();
    private Connection con = null;
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

    /** Creates new form JIFCMedicos */
    public JIFCMedicosAtendimentos(String novoOuEditar, int medicoId) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        this.medicoId = medicoId;
        iniciarClasse();
        tirandoBarraDeTitulo();
        pegandoDataDoSistema();
        if (novoOuEditar == "novo") {
            jTFNome.setText(JIFAtendimentoSelecionarUmMedicoSolicitante.jTFNomeMedico.getText());
            jBAtualizarRegistro.setVisible(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar novo Médico",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        } else {
            jBSalvarRegistro.setVisible(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Médico",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            // preenchendo os campos
            con = Conexao.fazConexao();
            ResultSet resultSet = MEDICOS.getConsultarDadosDeUmMedico(con, medicoId);
            try {
                while (resultSet.next()) {
                    jTFNome.setText(resultSet.getString("nome"));
                    jTFCRM.setText(resultSet.getString("crm"));
                    jCBUfCRM.setSelectedItem(resultSet.getString("ufcrm"));
                    jTFNascimento.setText(resultSet.getString("nascimento"));
                    jTFTelefone.setText(resultSet.getString("telefone"));
                    jTFCelular.setText(resultSet.getString("celular"));
                    jTFEndereco.setText(resultSet.getString("endereco"));
                    jTFBairro.setText(resultSet.getString("bairro"));
                    jTFCep.setText(resultSet.getString("cep"));
                    jTFCidade.setText(resultSet.getString("cidade"));
                    jTFEmail.setText(resultSet.getString("email"));
                    
                    jTFTelefoneDois.setText(resultSet.getString("telefoneDois"));
                    jTFCpfCnpj.setText(resultSet.getString("cpfCnpj"));
                    jTFNomeSecretaria.setText(resultSet.getString("nome_secretaria"));

                    for (int x = 0; x < listaCodEspecialidadesMedicas.size(); x++) {
                        if (listaCodEspecialidadesMedicas.get(x) == resultSet.getInt("emid")) {
                            jCBEspecialidadeMedica.setSelectedIndex(x);
                        }
                    }
                }

                // colocando foco na referencia
                jTFNome.requestFocusInWindow();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Não foi possível preencher os dados do Médico. Procure o administrador.", "ATENÇÃO!",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            br.bcn.admclin.dao.dbris.Conexao.fechaConexao(con);

        }
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    /** Metodo que vai no construtor da classe. Eexecuta operações antes de abrir a janela. */

    public void iniciarClasse() {
        jTFEmail.setDocument(new DocumentoSemAspasEPorcento(64));
        jTFEndereco.setDocument(new DocumentoSemAspasEPorcento(80));
        jTFBairro.setDocument(new DocumentoSemAspasEPorcento(32));
        jTFCidade.setDocument(new DocumentoSemAspasEPorcento(32));
        // preenchendo as Especialidades Médicas
        preencherEspecialidadesMedicas();
        jCBEspecialidadeMedica.setSelectedItem("SEM ESPECIALIDADE");
    }
    
    private void preencherEspecialidadesMedicas(){
    	con = Conexao.fazConexao();
        ResultSet resultSet = ESPECIALIDADES_MEDICAS.getConsultar(con);
        listaCodEspecialidadesMedicas.removeAll(listaCodEspecialidadesMedicas);
        jCBEspecialidadeMedica.removeAllItems();
        //jCBEspecialidadeMedica.addItem("");
        //listaCodEspecialidadesMedicas.add(0);
        try {
            while (resultSet.next()) {
                jCBEspecialidadeMedica.addItem(resultSet.getString("descricao"));
                listaCodEspecialidadesMedicas.add(resultSet.getInt("emid"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher as Especialidades Médicas. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    /**
     * Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo. Atualiza a tabela de acordo com
     * os objetos.
     */
    public void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFrameAtendimentoCadastroMedicos = null;

        //if (JIFAtendimentoAgenda.veioDaPesquisa) {
        //    janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setVisible(true);
        //} else {
            janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setVisible(true);
        //}
    }

    public boolean verificarSetudoFoiPreenchidoCorretamente() {
        boolean nomeOk = MetodosUteis.veriricarSeNomeDePessaoEhValido(jTFNome, jTFMensagemParaUsuario);

        boolean nascimentoOk = true;
        if (!"  /  /    ".equals(jTFNascimento.getText())) {
            nascimentoOk = MetodosUteis.verificarSeDataDeNascimentoEValida(jTFNascimento, jTFMensagemParaUsuario);
        }

        boolean emailOk = true;
        if (!"".equals(jTFEmail.getText())) {
            nascimentoOk = MetodosUteis.verificarSeEmailEValido(jTFEmail, jTFMensagemParaUsuario);
        }
        
        boolean crmMedicoOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFCRM, 2, jTFMensagemParaUsuario);

        if (emailOk && nascimentoOk && nomeOk && crmMedicoOk) {
            return true;
        } else {
            return false;
        }
    }

    /** Salva um novo Médico na banco de dados. */
    public void botaoSalvarRegistro() {

        if (verificarSetudoFoiPreenchidoCorretamente()) {
            // salva no banco
            Medicos medicosMODEL = new Medicos();
            // fazer a inserção no banco
            con = Conexao.fazConexao();
            medicosMODEL.setUsuarioId(USUARIOS.usrId);
            medicosMODEL.setDat(dataDeHojeEmVariavelDate);
            medicosMODEL.setNome(jTFNome.getText());
            medicosMODEL.setCrm(jTFCRM.getText());
            medicosMODEL.setUfcrm((String) jCBUfCRM.getSelectedItem());
            medicosMODEL.setDat(dataDeHojeEmVariavelDate);
            medicosMODEL.setNascimento(jTFNascimento.getText());
            medicosMODEL.setEmail(jTFEmail.getText());
            medicosMODEL.setTelefone(jTFTelefone.getText());
            medicosMODEL.setTelefoneDois(jTFTelefoneDois.getText());
            medicosMODEL.setCelular(jTFCelular.getText());
            medicosMODEL.setCpfCnpj(jTFCpfCnpj.getText());
            medicosMODEL.setNomeSecretaria(jTFNomeSecretaria.getText());
            medicosMODEL.setEndereco(jTFEndereco.getText());
            medicosMODEL.setBairro(jTFBairro.getText());
            medicosMODEL.setCep(jTFCep.getText());
            medicosMODEL.setCidade(jTFCidade.getText());
            medicosMODEL.setUf((String) jCBUf.getSelectedItem());
            medicosMODEL.setEmId(listaCodEspecialidadesMedicas.get(jCBEspecialidadeMedica.getSelectedIndex()));
            boolean cadastro = MEDICOS.setCadastrar(con, medicosMODEL);
            Conexao.fechaConexao(con);
            // atualiza tabela
            if (cadastro) {
                con = Conexao.fazConexao();
                medicoId = MEDICOS.getConsultarMedicoId(con, jTFNome.getText());
                if (medicoId != 0) {
                    voltarATelaDeAtendimento();
                }
                Conexao.fechaConexao(con);
            }
        }
    }

    /** Atualiza umMédico no banco de dados. */
    public void botaoAtualizarRegistro() {

        if (verificarSetudoFoiPreenchidoCorretamente()) {
            // salva no banco
            Medicos medicosMODEL = new Medicos();
            // fazer a inserção no banco
            con = Conexao.fazConexao();
            medicosMODEL.setUsuarioId(USUARIOS.usrId);
            medicosMODEL.setDat(dataDeHojeEmVariavelDate);
            medicosMODEL.setNome(jTFNome.getText());
            medicosMODEL.setMedicoId(medicoId);
            medicosMODEL.setCrm(jTFCRM.getText());
            medicosMODEL.setUfcrm((String) jCBUfCRM.getSelectedItem());
            medicosMODEL.setNascimento(jTFNascimento.getText());
            medicosMODEL.setEmail(jTFEmail.getText());
            medicosMODEL.setTelefone(jTFTelefone.getText());
            medicosMODEL.setTelefoneDois(jTFTelefoneDois.getText());
            medicosMODEL.setCelular(jTFCelular.getText());
            medicosMODEL.setCpfCnpj(jTFCpfCnpj.getText());
            medicosMODEL.setNomeSecretaria(jTFNomeSecretaria.getText());
            medicosMODEL.setEndereco(jTFEndereco.getText());
            medicosMODEL.setBairro(jTFBairro.getText());
            medicosMODEL.setCep(jTFCep.getText());
            medicosMODEL.setCidade(jTFCidade.getText());
            medicosMODEL.setUf((String) jCBUf.getSelectedItem());
            medicosMODEL.setEmId(listaCodEspecialidadesMedicas.get(jCBEspecialidadeMedica.getSelectedIndex()));
            boolean cadastro = MEDICOS.setUpdate(con, medicosMODEL);
            Conexao.fechaConexao(con);
            // atualiza tabela
            if (cadastro) {
                voltarATelaDeAtendimento();
            }
        }
    }

    public void voltarATelaDeAtendimento() {
        this.dispose();
        janelaPrincipal.internalFrameAtendimentoCadastroMedicos = null;

        janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.dispose();
        janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante = null;

        //if (JIFAtendimentoAgenda.veioDaPesquisa) {
        //    janelaPrincipal.internalFrameAtendimentoAgenda.setVisible(true);
        //} else {
        //    janelaPrincipal.internalFrameAgendaPrincipal.setVisible(true);
            janelaPrincipal.internalFrameAtendimento.setVisible(true);
            
        //}

        JIFCadastroDeAtendimento.jTFMedicoSol.setText(jTFNome.getText());
        JIFCadastroDeAtendimento.jTFHANDLE_MEDICO_SOL.setText(String.valueOf(medicoId));

        // setando a variavel de hanle_paciente. para usar no cadastramento do atendimento
        JIFCadastroDeAtendimento.handle_medico_sol = Integer.valueOf(medicoId);
        
        janelaPrincipal.internalFrameAtendimento.verificaMedicoSemAlerta();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTFEmail = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jLabel20 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jCBUf = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFNascimento = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##/##/####"));
        jLabel3 = new javax.swing.JLabel();
        jTFNome = new javax.swing.JTextField(new DocumentoSomenteLetras(64), null, 0);
        jTFEndereco = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(80), null, 0);
        jTFCidade = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(32), null, 0);
        jLabel15 = new javax.swing.JLabel();
        jTFBairro = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(32), null, 0);
        jLabel13 = new javax.swing.JLabel();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFCep = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##.###-###"));
        jLabel14 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFTelefone = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFCelular = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jCBEspecialidadeMedica = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTFCRM = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(20), null, 0);
        jLabel7 = new javax.swing.JLabel();
        jCBUfCRM = new javax.swing.JComboBox();
        jBAtualizarRegistro = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        setTitle("Cadastro de Médicos");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Médico",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFEmailFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFEmailFocusLost(evt);
            }
        });

        jLabel20.setText("E-Mail");

        jLabel16.setText("UF");

        jCBUf.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE",
            "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC",
            "SP", "SE", "TO" }));

        jLabel17.setText("Cidade");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setText("Nascimento");

        jTFNascimento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFNascimentoFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNascimentoFocusLost(evt);
            }
        });

        jLabel3.setText("Nome");

        jLabel15.setText("CEP");

        jLabel13.setText("Endereço");

        jTFCep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCepFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCepFocusLost(evt);
            }
        });

        jLabel14.setText("Bairro");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTFTelefone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFTelefoneFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFTelefoneFocusLost(evt);
            }
        });

        jLabel11.setText("Telefone");

        jLabel12.setText("Celular");

        jTFCelular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCelularFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCelularFocusLost(evt);
            }
        });

        jLabel18.setText("Especialidade Medica");

        jLabel6.setText("CRM");

        jLabel7.setText("UF CRM");

        jCBUfCRM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE",
            "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC",
            "SP", "SE", "TO" }));
        
        btnCadastrarNovaEspecialidade = new JButton("Cadastrar Nova Especialidade");
        btnCadastrarNovaEspecialidade.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		botaoCadastrarEspecialidade();
        	}
        });
        btnCadastrarNovaEspecialidade.setIcon(new ImageIcon(JIFCMedicosAtendimentos.class.getResource("/br/bcn/admclin/imagens/menuPessoalEspecialidadesMedicas.png")));
        
        jTFTelefoneDois = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        
        JLabel label = new JLabel();
        label.setText("Telefone");
        
        jTFCpfCnpj = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(20), null, 0);
        
        JLabel lblCpfcnpj = new JLabel();
        lblCpfcnpj.setText("CPF/CNPJ");
        
        jTFNomeSecretaria = new javax.swing.JTextField(new DocumentoSomenteLetras(64), null, 0);
        
        lblNomeSecretaria = new JLabel();
        lblNomeSecretaria.setText("Nome Secretaria");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGap(551)
        			.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE))
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel3)
        				.addComponent(jLabel13)
        				.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
        					.addComponent(jTFNome)
        					.addComponent(jTFEndereco)
        					.addGroup(jPanel1Layout.createSequentialGroup()
        						.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        							.addComponent(jTFBairro, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
        							.addComponent(jLabel14))
        						.addPreferredGap(ComponentPlacement.RELATED)
        						.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        							.addComponent(jLabel15)
        							.addComponent(jTFCep, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE))))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jTFCRM, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel6))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel7)
        						.addComponent(jCBUfCRM, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(jLabel5)
        				.addComponent(jTFNascimento, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblNomeSecretaria, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jTFNomeSecretaria, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jTFEmail, 311, 311, 311)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel17)
        						.addComponent(jTFCidade, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel16)
        						.addComponent(jCBUf, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jTFTelefone, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel11))
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFCelular, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addGap(23)
        							.addComponent(jLabel12)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(label, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jTFTelefoneDois, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(jTFCpfCnpj, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblCpfcnpj, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel20, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jCBEspecialidadeMedica, 0, 343, Short.MAX_VALUE)
        				.addComponent(jLabel18)
        				.addComponent(btnCadastrarNovaEspecialidade))
        			.addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 313, Short.MAX_VALUE)
        					.addContainerGap())
        				.addComponent(jSeparator1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        								.addGroup(jPanel1Layout.createSequentialGroup()
        									.addComponent(jLabel17)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(jTFCidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        								.addGroup(jPanel1Layout.createSequentialGroup()
        									.addComponent(jLabel16)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(jCBUf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel20)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false)
        								.addGroup(jPanel1Layout.createSequentialGroup()
        									.addComponent(jLabel11)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(jTFTelefone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        								.addGroup(jPanel1Layout.createSequentialGroup()
        									.addComponent(jLabel12)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(jTFCelular, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        								.addGroup(jPanel1Layout.createSequentialGroup()
        									.addComponent(label)
        									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(jTFTelefoneDois, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(lblCpfcnpj)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFCpfCnpj, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(lblNomeSecretaria)
        							.addGap(6)
        							.addComponent(jTFNomeSecretaria, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel18)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jCBEspecialidadeMedica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(btnCadastrarNovaEspecialidade))
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addComponent(jLabel3)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        								.addComponent(jLabel6)
        								.addComponent(jLabel7))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        								.addComponent(jTFCRM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        								.addComponent(jCBUfCRM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel5)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFNascimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel13)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        								.addGroup(jPanel1Layout.createSequentialGroup()
        									.addComponent(jLabel14)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(jTFBairro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        								.addGroup(jPanel1Layout.createSequentialGroup()
        									.addComponent(jLabel15)
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(jTFCep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
        					.addContainerGap(19, Short.MAX_VALUE))))
        );
        jPanel1.setLayout(jPanel1Layout);

        jBAtualizarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBAtualizarRegistro.setText("Atualizar e Selecionar Médico");
        jBAtualizarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBAtualizarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAtualizarRegistroActionPerformed(evt);
            }
        });
        jBAtualizarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBAtualizarRegistroFocusLost(evt);
            }
        });
        jBAtualizarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBAtualizarRegistroKeyReleased(evt);
            }
        });

        jBSalvarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBSalvarRegistro.setText("Salvar e Selecionar Médico");
        jBSalvarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBSalvarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarRegistroActionPerformed(evt);
            }
        });
        jBSalvarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBSalvarRegistroFocusLost(evt);
            }
        });
        jBSalvarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarRegistroKeyReleased(evt);
            }
        });

        jBCancelar.setBackground(new java.awt.Color(113, 144, 224));
        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBCancelar.setText("Voltar");
        jBCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBCancelar.setPreferredSize(new java.awt.Dimension(89, 39));
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

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
        				.addComponent(jTFMensagemParaUsuario, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jBCancelar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jBAtualizarRegistro)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jBSalvarRegistro)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(Alignment.LEADING, layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jTFMensagemParaUsuario, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jBAtualizarRegistro)
        				.addComponent(jBSalvarRegistro)
        				.addComponent(jBCancelar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(59, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void botaoCadastrarEspecialidade(){
    	Object[] message = {"Descrição"}; 
    	String especialidade = JOptionPane.showInputDialog(janelaPrincipal.internalFrameJanelaPrincipal, message, "Adicionar Especialidade Médica", JOptionPane.INFORMATION_MESSAGE); 
    	if(especialidade != null){
    		cadastrarEspecialidadeMedica(especialidade.toUpperCase());
    	}
    }
    
    private void cadastrarEspecialidadeMedica(String especialidade){
    	if(especialidade.replaceAll(" ", "").length() < 3){
    		JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "A descrição deve conter no mínimo 3 caracteres");
    		botaoCadastrarEspecialidade();
    	}else{
    		con = Conexao.fazConexao();
            Especialidades_Medicas especialidadeMedicaMODELO = new Especialidades_Medicas();
            especialidadeMedicaMODELO.setDescricao(especialidade);
            boolean existe = ESPECIALIDADES_MEDICAS.getConsultarParaSalvarNovoRegistro(con, especialidadeMedicaMODELO);
            Conexao.fechaConexao(con);
            if (ESPECIALIDADES_MEDICAS.conseguiuConsulta) {
                if (existe) {
                    JOptionPane.showMessageDialog(null, "Especialidade Médica já existe", "ATENÇÃO",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // fazer a inserção no banco
                    con = Conexao.fazConexao();
                    especialidadeMedicaMODELO.setUsuarioId(USUARIOS.usrId);
                    especialidadeMedicaMODELO.setDat(dataDeHojeEmVariavelDate);
                    boolean cadastro = ESPECIALIDADES_MEDICAS.setCadastrar(con, especialidadeMedicaMODELO);
                    Conexao.fechaConexao(con);
                    // atualiza tabela
                    if (cadastro) {
                        //se cadastro atualizamos o combo box
                    	preencherEspecialidadesMedicas();
                    	jCBEspecialidadeMedica.setSelectedItem(especialidade);
                    	
                    }
                }
            }
    	}
    }

    private void jTFNascimentoFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNascimentoFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("dd/mm/aaaa");
    }// GEN-LAST:event_jTFNascimentoFocusGained

    private void jTFTelefoneFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFTelefoneFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("(##) ####-####");
    }// GEN-LAST:event_jTFTelefoneFocusGained

    private void jTFCelularFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCelularFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("(##) ####-####");
    }// GEN-LAST:event_jTFCelularFocusGained

    private void jTFCepFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCepFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("##.###-###");
    }// GEN-LAST:event_jTFCepFocusGained

    private void jTFEmailFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFEmailFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("xxx@xxx.xxx");
    }// GEN-LAST:event_jTFEmailFocusGained

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro();
    }// GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvarRegistro();
        }
    }// GEN-LAST:event_jBSalvarRegistroKeyReleased

    private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroActionPerformed
        botaoAtualizarRegistro();
    }// GEN-LAST:event_jBAtualizarRegistroActionPerformed

    private void jBAtualizarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizarRegistro();
        }
    }// GEN-LAST:event_jBAtualizarRegistroKeyReleased

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));
        jTFEmail.setBackground(new java.awt.Color(255, 255, 255));
        jTFTelefone.setBackground(new java.awt.Color(255, 255, 255));
        jTFCelular.setBackground(new java.awt.Color(255, 255, 255));
        jTFEndereco.setBackground(new java.awt.Color(255, 255, 255));
        jTFBairro.setBackground(new java.awt.Color(255, 255, 255));
        jTFCep.setBackground(new java.awt.Color(255, 255, 255));
        jTFCidade.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBAtualizarRegistroFocusLost

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));
        jTFEmail.setBackground(new java.awt.Color(255, 255, 255));
        jTFTelefone.setBackground(new java.awt.Color(255, 255, 255));
        jTFCelular.setBackground(new java.awt.Color(255, 255, 255));
        jTFEndereco.setBackground(new java.awt.Color(255, 255, 255));
        jTFBairro.setBackground(new java.awt.Color(255, 255, 255));
        jTFCep.setBackground(new java.awt.Color(255, 255, 255));
        jTFCidade.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jTFNascimentoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNascimentoFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFNascimentoFocusLost

    private void jTFTelefoneFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFTelefoneFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFTelefoneFocusLost

    private void jTFCelularFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCelularFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFCelularFocusLost

    private void jTFEmailFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFEmailFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFEmailFocusLost

    private void jTFCepFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCepFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFCepFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAtualizarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBSalvarRegistro;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBEspecialidadeMedica;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBUf;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBUfCRM;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTFBairro;
    private javax.swing.JTextField jTFCRM;
    private javax.swing.JTextField jTFCelular;
    private javax.swing.JTextField jTFCep;
    private javax.swing.JTextField jTFCidade;
    private javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFEndereco;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFNascimento;
    private javax.swing.JTextField jTFNome;
    private javax.swing.JTextField jTFTelefone;
    private JTextField jTFTelefoneDois;
    private JButton btnCadastrarNovaEspecialidade;
    private JTextField jTFCpfCnpj;
    private JTextField jTFNomeSecretaria;
    private JLabel lblNomeSecretaria;
}
