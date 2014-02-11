package br.bcn.admclin.interfacesGraficas.menu.financeiro.recebimentoConvenio;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;

public class jFDefinirValorRecebido extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField jTFValorRecebido;
    private JTextField jTFDataRecebido;
    private JTextField jTFData;
    private JTextField jTFPaciente;
    private JTextField jTFExame;
    private int atendimento_exame_id;

    /**
     * Create the frame.
     */
    public jFDefinirValorRecebido(String dataExame, String nomePaciente, String nomeExame, String valorRecebimento, String dataRecebimento, int atendimento_exame_id) {
        this.atendimento_exame_id = atendimento_exame_id;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1020, 151);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblValorRecebido = new JLabel("Valor Recebido:");
        lblValorRecebido.setBounds(754, 6, 87, 14);
        contentPane.add(lblValorRecebido);
        
        jTFValorRecebido = new JTextField();
        jTFValorRecebido.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botaoSalvar();
                }
            }
        });
        jTFValorRecebido.setBounds(754, 31, 122, 28);
        contentPane.add(jTFValorRecebido);
        jTFValorRecebido.setColumns(10);
        
        jTFDataRecebido = new JTextField();
        jTFDataRecebido.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
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
            public void keyReleased(KeyEvent e) {
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
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    fechar();
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
        jTFPaciente.setEditable(false);
        jTFPaciente.setText("15/09/1987");
        jTFPaciente.setColumns(10);
        jTFPaciente.setBounds(101, 31, 298, 28);
        contentPane.add(jTFPaciente);
        
        jTFExame = new JTextField();
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
        
    }
    
    private void fechar(){
        this.dispose();
        janelaPrincipal.internalFrameJanelaPrincipal.setEnabled(true);
        janelaPrincipal.internalFrameJanelaPrincipal.setAlwaysOnTop(true);
        janelaPrincipal.internalFrameJanelaPrincipal.setAlwaysOnTop(false);
    }
    
    private void botaoSalvar(){
        //faz o que tem que fazer
        int selectedRow = jIFListaDeExamesParaRecebimento.jTable1.getSelectedRow();
        try {
            jIFListaDeExamesParaRecebimento.jTable1.addRowSelectionInterval(selectedRow+1,selectedRow+1);
        } catch (Exception e) {
            //cai aqui se estiver na ultima linha, ae continua a ultima selecionada
        }
        fechar();
        
    }
}
