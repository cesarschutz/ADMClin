package br.bcn.admclin.interfacesGraficas.menu.financeiro.recebimentoConvenio;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import br.bcn.admclin.ClasseAuxiliares.JTextFieldDinheiroReais;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.ATENDIMENTO_EXAMES;
import br.bcn.admclin.dao.model.Atendimento_Exames;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class jFDefinirValorRecebido extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField jTFValorRecebido;
    private JTextField jTFDataRecebido;
    private JTextField jTFData;
    private JTextField jTFPaciente;
    private JTextField jTFExame;
    private int atendimento_exame_id;
    private int handle_at;
    
    
    /**
     * Create the frame.
     */
    public jFDefinirValorRecebido(String dataExame, String nomePaciente, String nomeExame, String valorRecebimento, String dataRecebimento, int atendimento_exame_id, int handle_at) {
        this.atendimento_exame_id = atendimento_exame_id;
        this.handle_at = handle_at;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1020, 151);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblValorRecebido = new JLabel("Valor Recebido:");
        lblValorRecebido.setBounds(754, 6, 87, 14);
        contentPane.add(lblValorRecebido);
        
        jTFValorRecebido = new JTextFieldDinheiroReais(new DecimalFormat("0.00")) {
            private static final long serialVersionUID = 1L;

            {// limita a 8
             // caracteres
                setLimit(8);
            }
        };
        jTFValorRecebido.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                jTFValorRecebido.setSelectionStart(0);
                // setar a posição final na string, neste caso até o tamanho do texto  
                jTFValorRecebido.setSelectionEnd(jTFValorRecebido.getText().length());
            }
        });
        jTFValorRecebido.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botaoSalvar();
                }
            }
        });
        jTFValorRecebido.setBounds(754, 31, 122, 28);
        contentPane.add(jTFValorRecebido);
        jTFValorRecebido.setColumns(10);
        
        jTFDataRecebido = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##/##/####"));
        jTFDataRecebido.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jTFDataRecebido.setSelectionStart(0);
                // setar a posição final na string, neste caso até o tamanho do texto  
                jTFDataRecebido.setSelectionEnd(jTFDataRecebido.getText().length());
            }
        });
        jTFDataRecebido.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botaoSalvar();
                }
            }
        });
        jTFDataRecebido.setColumns(10);
        jTFDataRecebido.setBounds(886, 31, 122, 28);
        contentPane.add(jTFDataRecebido);
        
        JLabel lblDataRecebido = new JLabel("Data Recebido:");
        lblDataRecebido.setBounds(886, 6, 87, 14);
        contentPane.add(lblDataRecebido);
        
        JButton jBSalvar = new JButton("Salvar");
        jBSalvar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botaoSalvar();
                }
            }
        });
        jBSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                botaoSalvar();
            }
        });
        jBSalvar.setIcon(new ImageIcon(jFDefinirValorRecebido.class.getResource("/br/bcn/admclin/imagens/imagemBotaoOk.png")));
        jBSalvar.setBounds(10, 71, 481, 42);
        contentPane.add(jBSalvar);
        
        JButton jBCancelar = new JButton("Cancelar");
        jBCancelar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botaoSalvar();
                }
            }
        });
        jBCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                fechar();
            }
        });
        jBCancelar.setIcon(new ImageIcon(jFDefinirValorRecebido.class.getResource("/br/bcn/admclin/imagens/cancelar.png")));
        jBCancelar.setBounds(503, 71, 505, 42);
        contentPane.add(jBCancelar);
        
        jTFData = new JTextField();
        jTFData.setBackground(new Color(204, 204, 204));
        jTFData.setEditable(false);
        jTFData.setText("15/09/1987");
        jTFData.setColumns(10);
        jTFData.setBounds(10, 31, 79, 28);
        contentPane.add(jTFData);
        
        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(10, 6, 35, 14);
        contentPane.add(lblData);
        
        JLabel lblPaciente = new JLabel("Paciente:");
        lblPaciente.setBounds(101, 6, 59, 14);
        contentPane.add(lblPaciente);
        
        jTFPaciente = new JTextField();
        jTFPaciente.setBackground(new Color(204, 204, 204));
        jTFPaciente.setEditable(false);
        jTFPaciente.setText("15/09/1987");
        jTFPaciente.setColumns(10);
        jTFPaciente.setBounds(101, 31, 298, 28);
        contentPane.add(jTFPaciente);
        
        jTFExame = new JTextField();
        jTFExame.setBackground(new Color(204, 204, 204));
        jTFExame.setEditable(false);
        jTFExame.setText("15/09/1987");
        jTFExame.setColumns(10);
        jTFExame.setBounds(414, 31, 328, 28);
        contentPane.add(jTFExame);
        
        JLabel lblExame = new JLabel("Exame:");
        lblExame.setBounds(414, 6, 59, 14);
        contentPane.add(lblExame);
        
        iniciarClasse(dataExame, nomePaciente, nomeExame, valorRecebimento, dataRecebimento);
    }
    
    private void iniciarClasse(String dataExame, String nomePaciente, String nomeExame, String valorRecebimento, String dataRecebimento){
        jTFData.setText(dataExame);
        jTFPaciente.setText(nomePaciente);
        jTFExame.setText(nomeExame);
        jTFValorRecebido.setText(valorRecebimento);
        jTFDataRecebido.setText(dataRecebimento);
        
        jTFValorRecebido.setHorizontalAlignment(javax.swing.JTextField.CENTER);        
        jTFDataRecebido.setHorizontalAlignment(javax.swing.JTextField.CENTER);        
        
        setAlwaysOnTop(true);
        setResizable(false);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                fechar();
            }
        });

        // arrumando a localização
        int x = janelaPrincipal.internalFrameJanelaPrincipal.getX();
        int y = janelaPrincipal.internalFrameJanelaPrincipal.getY();
        this.setLocation(x, y+150);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTFValorRecebido.requestFocus();
            }
        });
        
        setIconImage(getToolkit().createImage(getClass().getResource("/br/bcn/admclin/imagens/imagemIconePrograma.png")));

        this.setTitle("ADMClin - Defina o valor e a data do Recebimento");
    }
    
    private void fechar(){
        this.dispose();
        janelaPrincipal.internalFrameJanelaPrincipal.setEnabled(true);
        janelaPrincipal.internalFrameJanelaPrincipal.setAlwaysOnTop(true);
        janelaPrincipal.internalFrameJanelaPrincipal.setAlwaysOnTop(false);
    }
    
    private void botaoSalvar(){
        //faz o que tem que fazer
        try{
            if(MetodosUteis.verificarSeDataDeNascimentoEValidaSemMensagem(jTFDataRecebido)){
                Double valorRecebido = Double.valueOf(jTFValorRecebido.getText().replaceAll(",", "." )); 
                java.sql.Date dataRecebido = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(jTFDataRecebido.getText()).getTime());
                
                ATENDIMENTO_EXAMES.registrarRecebimentoDeConvenio(valorRecebido, dataRecebido, atendimento_exame_id, handle_at);
                
                jIFListaDeExamesParaRecebimento.jTable1.setValueAt(jTFValorRecebido.getText(), jIFListaDeExamesParaRecebimento.jTable1.getSelectedRow(), 5);
                jIFListaDeExamesParaRecebimento.jTable1.setValueAt(jTFDataRecebido.getText(), jIFListaDeExamesParaRecebimento.jTable1.getSelectedRow(), 6);
                jIFListaDeExamesParaRecebimento.jTable1.setValueAt("1", jIFListaDeExamesParaRecebimento.jTable1.getSelectedRow(), 7);
                janelaPrincipal.internalFrameJanelaPrincipal.internalFrameRecebimentoDeConvenios.jIFListaDeExamesParaRecebimento.colocarIconeDeConciliado();
                jIFListaDeExamesParaRecebimento.ultimaDataDigitada = jTFDataRecebido.getText();
                
                // troca a seleção para proxima linha
                int selectedRow = jIFListaDeExamesParaRecebimento.jTable1.getSelectedRow();
                try {
                    jIFListaDeExamesParaRecebimento.jTable1.addRowSelectionInterval(selectedRow+1,selectedRow+1);
                } catch (Exception e) {
                    //cai aqui se estiver na ultima linha, ae continua a ultima selecionada
                }
                fechar();
            } else{
                JOptionPane.showMessageDialog(null, "Data Inválida!");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro ao salvar valores.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
