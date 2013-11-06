package menu.cadastros.pessoal.dao;

import ClasseAuxiliares.OSvalidator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import menu.cadastros.pessoal.model.usuariosMODEL;

/**
 * Classe DAO para tabela USUARIOS
 * @author BCN
 */
public class usuariosDAO {
    public static String statusUsuario = null, nomeUsuario = null, impressora_ficha = null, impressora_nota_fiscal = null, impressora_etiqueta_envelope = null, impressora_codigo_de_barras=null, pasta_raiz = "-";
    public static boolean conseguiuConsulta;
    public static int usrId;
    /**
     * Consulta todos os usuários exitentes no Banco De Dados.
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from USUARIOS order by NM_USUARIO");
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Usuários. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
        
    }
    /**
     * Verifica se Usuário já existe antes de cadastra-lo no Banco de Dados.
     * @param Connection
     * @param UsuarioMODEL
     * @return boolean
     */
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, usuariosMODEL usuario){
        boolean existe = true;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from USUARIOS where NM_USUARIO=? and PW_SENHA=?");
            stmtQuery.setString(1, usuario.getUsuario());
            stmtQuery.setString(2, usuario.getSenha());
            ResultSet resultSet = stmtQuery.executeQuery();
            if(!resultSet.next()){
                existe = false;
            }
            conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Usuário existe. Procure o Administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    /**
     * Verifica se usuário já existe antes de atualiza-lo no Banco De Dados.
     * @param Connection
     * @param UsuarioModel
     * @return boolean
     */
    public static boolean getConsultarParaAtualizarRegistro(Connection con, usuariosMODEL usuario){
        boolean existe = true;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from USUARIOS where (NM_USUARIO=? and PW_SENHA=?) and (USRID!=?)");
            stmtQuery.setString(1, usuario.getUsuario());
            stmtQuery.setString(2, usuario.getSenha());
            stmtQuery.setInt(3, usuario.getUsrid());
            ResultSet resultSet = stmtQuery.executeQuery();
            if(!resultSet.next()){
                existe = false;
            }
            conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Usuário existe. Procure o Administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
           return existe; 
        }
    }
    /**
     * Salva um novo Usuário no Banco De Dados.
     * @param Connection
     * @param UsuarioModel 
     * @return  boolean
     */
    public static boolean setCadastrar(Connection con, usuariosMODEL usuario){
        boolean cadastro = false;
        String sql = "insert into USUARIOS (DS_UNIDADE,NM_USUARIO,PW_SENHA,FG_ESTATUS,EMAIL,ENVIA_EMAILS, usuarioid, dat, IMPRESSORA_FICHA, IMPRESSORA_NOTA_FISCAL, IMPRESSORA_ETIQUETA_ENVELOPE, PASTA_RAIZ, IMPRESSORA_CODIGO_DE_BARRAS) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, usuario.getDescricao());
            stmt.setString(2, usuario.getUsuario());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getEstatus());
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getEnvia_email());
            stmt.setInt(7, usuario.getUsuarioId());
            stmt.setDate(8, usuario.getDat());
            stmt.setString(9, usuario.getImpressora_ficha());
            stmt.setString(10, usuario.getImpressora_nota_fiscal());
            stmt.setString(11, usuario.getImpressora_etiqueta_envelope());
            stmt.setString(12, usuario.getPasta_raiz());
            stmt.setString(13, usuario.getImpressora_codigo_de_barras());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Usuário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    /**
     * Atualiza um contato no Banco De Dados.
     * @param Connection
     * @param UsuarioModel 
     * @return boolean
     */
    public static boolean setUpdate(Connection con, usuariosMODEL usuario){
        boolean atualizo = false;
        String sql = "update USUARIOS set DS_UNIDADE=?, NM_USUARIO=?, PW_SENHA=?, FG_ESTATUS=?, EMAIL=?, ENVIA_EMAILS=?, USUARIOID=?, dat=?, IMPRESSORA_FICHA=?, IMPRESSORA_NOTA_FISCAL=?, IMPRESSORA_ETIQUETA_ENVELOPE=?, PASTA_RAIZ=?, IMPRESSORA_CODIGO_DE_BARRAS=? where USRID=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, usuario.getDescricao());
            stmt.setString(2, usuario.getUsuario());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getEstatus());
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getEnvia_email());
            stmt.setInt(7, usuario.getUsuarioId());
            stmt.setDate(8, usuario.getDat());
            stmt.setString(9, usuario.getImpressora_ficha());
            stmt.setString(10, usuario.getImpressora_nota_fiscal());
            stmt.setString(11, usuario.getImpressora_etiqueta_envelope());
            stmt.setString(12, usuario.getPasta_raiz());
            stmt.setString(13, usuario.getImpressora_codigo_de_barras());
            stmt.setInt(14, usuario.getUsrid());
            stmt.executeUpdate();
            stmt.close();
            atualizo = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Usuário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return atualizo;
        }
    }
    /**
     * Deleta um Usuário do Banco de Dados.
     * @param Connection
     * @param UsuarioModel 
     */
    public static boolean setDeletar(Connection con, usuariosMODEL usuario){
        boolean deleto = false;
        String sql="delete from USUARIOS where USRID=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, usuario.getUsrid());
            stmt.executeUpdate();
            stmt.close();    
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Usuário. Procure o Administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    /**
     * Verifica se Usuário está cadastrado para entrar no sistema.
     * @param Connection
     * @param UsuarioMODEL
     * @return boolean
     */
    public static boolean getLogin(Connection con, usuariosMODEL usuario){
        boolean existe = false;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from USUARIOS where NM_USUARIO=? and PW_SENHA=?");
            stmtQuery.setString(1, usuario.getUsuario());
            stmtQuery.setString(2, usuario.getSenha());
            ResultSet resultSet = stmtQuery.executeQuery();
            if(resultSet.next()){
                statusUsuario = resultSet.getString("FG_ESTATUS");
                nomeUsuario = resultSet.getString("NM_USUARIO");
                usrId = resultSet.getInt("USRID");
                impressora_ficha = resultSet.getString("IMPRESSORA_FICHA");
                impressora_nota_fiscal = resultSet.getString("IMPRESSORA_NOTA_FISCAL");
                impressora_etiqueta_envelope = resultSet.getString("IMPRESSORA_ETIQUETA_ENVELOPE");
                impressora_codigo_de_barras = resultSet.getString("impressora_codigo_de_barras");
                pasta_raiz = verificarPastaRaiz(resultSet.getString("PASTA_RAIZ")) + nomeUsuario;
                existe = true;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao verificar Login. Procure o Administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /*
     * esse metodo erve para colocar a barra no final caso nao tenha.
     * */
    private static String verificarPastaRaiz(String pastaRaiz){
        if (pastaRaiz.equals("") || pastaRaiz == null) {
            JOptionPane.showMessageDialog(null, "Este usuário não tem pasta raiz definida. Defina uma pasta para este usuário no cadastro de usuários.", "Erro", JOptionPane.ERROR_MESSAGE);
        }else{
            //aqui trataremos a barra (caso nao tenha barra no fim da String)
            int tamanho = pastaRaiz.length(); 
            char ultimaLetra = pastaRaiz.charAt(tamanho-1);
            
            //pegamos a ultima letra, agora vamos verificar se é \ ou /. se nao for teremos que colocar de acordo com o sistema operacional
            
            if (ultimaLetra != '\\' || ultimaLetra != '/') {
                //aqui vamos colocar a barra pois nao tem
                if (OSvalidator.isWindows()) {
                    return  pastaRaiz + "\\";
                }else{
                    return  pastaRaiz + "/";
                }
            }else{
                return pastaRaiz;
            }
            
        }
        return "";
    }
    
}
