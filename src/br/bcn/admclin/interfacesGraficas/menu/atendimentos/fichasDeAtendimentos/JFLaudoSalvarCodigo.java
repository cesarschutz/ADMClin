package br.bcn.admclin.interfacesGraficas.menu.atendimentos.fichasDeAtendimentos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import br.bcn.admclin.dao.db.CODIGOS;

public class JFLaudoSalvarCodigo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtCodigo;
    private JTextArea txtrTexto;

    /**
     * Create the frame.
     */
    public JFLaudoSalvarCodigo(String texto) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(getToolkit().createImage(getClass().getResource("/br/bcn/admclin/imagens/imagemIconePrograma.png")));
        this.setTitle("Gravar Código");
        setBounds(100, 100, 683, 482);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    botaoSalvar();
                }
            }
        });
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                botaoSalvar();
            }
        });
        btnSalvar.setIcon(new ImageIcon(JFLaudoSalvarCodigo.class.getResource("/br/bcn/admclin/imagens/salvar.png")));
        
        txtCodigo = new JTextField();
        txtCodigo.setColumns(10);
        txtCodigo.setDocument(new br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcentoMinusculas(24));
        
        txtrTexto = new JTextArea();
        txtrTexto.setDocument(new br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcentoMinusculas(2048));
        txtrTexto.setColumns(20);
        txtrTexto.setLineWrap(true);
        txtrTexto.setRows(5);
        txtrTexto.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(txtrTexto);
        txtrTexto.setText(texto);
        
        JLabel lblCdigo = new JLabel("Código:");
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                        .addComponent(scroll, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addComponent(lblCdigo)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(txtCodigo, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnSalvar, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnSalvar)
                        .addComponent(lblCdigo)
                        .addComponent(txtCodigo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        contentPane.setLayout(gl_contentPane);
        
      //focus no texto do laudo
        SwingUtilities.invokeLater(new Runnable() {   
            public void run() {   
                txtCodigo.requestFocus();   
            }   
        });
    }
    
    private void botaoSalvar(){
        String texto = createCodigoText(txtrTexto.getText());
        if(CODIGOS.setCadastrarCodigo(txtCodigo.getText(), texto)){
            this.dispose();
        }
    }
    
    private String createCodigoText(String codigoText){
        try {
            if (codigoText.length() > 0) {
                String clines = codigoText.replaceAll("\\n","\\[\\]");
                return clines;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        } 
    }
}
