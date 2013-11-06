package conexao;

import janelaPrincipal.janelaPrincipal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * Classe responsavel por conexões com Banco de Dados e fechamento de conexão.
 * @author Cesar Schutz
 */
public class Conexao {
    
    public static Connection con = null;
    
    
    /**
     * Faz conexao com o banco de dados FireBird.
     * @return Connection
     */
    public static Connection fazConexao(){
        
        try{
            
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            
            //setando propriedades para que pegue as informaçoes do banco com ascento e caracteres especais e etc
            Properties props = new Properties();  
            props.put("user", "SYSDBA");  
            props.put("password", "masterkey");  
            props.put("charset", "UTF8");  
            props.put("lc_ctype", "ISO8859_1");

            con = DriverManager.getConnection("jdbc:firebirdsql:"+janelaPrincipal.RISIP+"/3050:"+janelaPrincipal.RISDB, props);
        }catch(ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "Erro no drive de conexão. Procure o administrador");
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Erro na conexao! Procure o administrador");
        }finally{
            return con;
        }
    }
    /**
     * Metodo para fechar uma conexão.
     * @param Connection 
     */
    public static void fechaConexao(Connection conn){
        try {
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar conexao. Procure o administrador");
        }
    }
    
}
