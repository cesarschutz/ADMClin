
package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Convenio;


/**
 *
 * @author Cesar Schutz
 */
public class CONVENIO {
    public static boolean conseguiuConsulta;
    
    /**
     * Consulta os dados de um convenio
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDadosDeUmConvenio(Connection con, int handle_convenio){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from convenio where handle_convenio=?");
        stmtQuery.setInt(1, handle_convenio);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Convênios. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    /**
     * Consulta Todos os Convênios existentes no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select handle_convenio, nome, porcentPaciente, porcentConvenio from convenio order by nome");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Convênios. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Consulta o ID de um convenio atraves do nome!
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarIdDeUmNomeCadastrado(Connection con, Convenio model){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select handle_convenio from convenio where nome=?");
        stmtQuery.setString(1, model.getNome());
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar handle_convenio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Verifica se Convenio já existe antes de cadastra-lo no Banco de Dados.
     * @param Connection
     * @param Convenio
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, Convenio model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from convenio where nome=?");
          stmtQuery.setString(1, model.getNome());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Convenio já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /**
     * Verifica se nome de convenio ja existe sem se o ID q estamos trabalhando!
     * @param Connection
     * @param Convenio
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaSalvarAtualizarRegistro(Connection con, Convenio model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from convenio where nome=? and handle_convenio!=?");
          stmtQuery.setString(1, model.getNome());
          stmtQuery.setInt(2, model.getHandle_convenio());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Convenio já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /**
     * Cadastra um convenio no Banco de Dados (somente o nome para termos o id)
     * @param Connection 
     * @param Convenio
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrarSomenteNome(Connection con, Convenio model){
        boolean cadastro = false;
        String sql = "insert into convenio (nome) values(?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, model.getNome());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Atualiza um Convênio no Banco De Dados.
     * @param Connection 
     * @param especialidades_medicasMODEL
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdate(Connection con, Convenio model){
        boolean atualizo = false;
        String sql = "update convenio set sigla=?, cgc=?, regans=?, nome=?, endereco=?, cidade=?, cep=?, uf=?, telefone=?, contato=?, email=?"
                + ", codprestador=?, tipo=?, remessa=?, numextra=?, numextra2=?, porcentpaciente=?, porcentconvenio=?, porcenttabela=?, irmaodoconv=?, "
                + "diasparanota=?, faturarjuntoconv=?, nummaxexameporficha=?, temdoc=?, validarmedico=?, tipovalidacao=?, arquivo=?, usuarioid=?, dat=?, redutor=?, validacao_matricula=?, IMPRIMI_ARQUIVO_TXT_COM_FATURA=?, grupoid=? where handle_convenio=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, model.getSigla());
            stmt.setString(2, model.getCgc());
            stmt.setInt(3, model.getRegAns());
            stmt.setString(4, model.getNome());
            stmt.setString(5, model.getEndereco());
            stmt.setString(6, model.getCidade());
            stmt.setString(7, model.getCep());
            stmt.setString(8, model.getUf());
            stmt.setString(9, model.getTelefone());
            stmt.setString(10, model.getContato());
            stmt.setString(11, model.getEmail());
            
            stmt.setString(12, model.getCodPrestador());
            stmt.setString(13, model.getTipo());
            stmt.setInt(14, model.getRemessa());
            stmt.setInt(15, model.getNumExtra());
            stmt.setInt(16, model.getNumExtra2());
            stmt.setString(17, model.getPorcentPaciente());
            stmt.setString(18, model.getPorcentConvenio());
            stmt.setString(19, model.getPorcentTabela());
            stmt.setInt(20, model.getIrmaoDoConv());
            
            stmt.setInt(21, model.getDiasParaNota());
            stmt.setString(22, model.getFaturarJuntoConv());
            stmt.setInt(23, model.getNumMaxExamePorFicha());
            stmt.setString(24, model.getTemDoc());
            stmt.setString(25, model.getValidarMedico());
            stmt.setString(26, model.getTipoValidacao());
            stmt.setString(27, model.getArquivo());
            stmt.setInt(28, model.getUsuarioId());
            stmt.setDate(29, model.getDat());
            stmt.setString(30, model.getRedutor());
            stmt.setInt(31, model.getVALIDACAO_MATRICULA());
            stmt.setInt(32, model.getIMPRIMI_ARQUIVO_TXT_COM_FATURA());
            stmt.setInt(33, model.getGrupoid());
            stmt.setInt(34, model.getHandle_convenio());
            
            stmt.executeUpdate();
            stmt.close();
            atualizo = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Convênio. Procure o Administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return atualizo;
        }
    }
    
    /**
     * Deleta um Convenio do Banco De Dados
     * @param Connection
     * @param ExameMODEL 
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletar(Connection con, int handle_convenio){
        boolean deleto = false; 
        String sql="delete from convenio where handle_convenio=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, handle_convenio);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
        /**
     * Consulta Todos os valores de ch existentes no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    public static boolean conseguiuConsultaCH = false;
    @SuppressWarnings("finally")
    public static String getConsultarCh(Connection con, int handle_convenio){
        String valorCh = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select valor from convenioCh where handle_convenio=? order by dataAValer");
            stmtQuery.setInt(1, handle_convenio);
            ResultSet resultSet = stmtQuery.executeQuery();
            while(resultSet.next()){
                valorCh = resultSet.getString("valor");
            }
            conseguiuConsultaCH = false;
        }catch(SQLException e){
            conseguiuConsultaCH = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar Valores de CH do Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return valorCh;
        }
    }
    

    /**
     * Consulta Todos os valores de filme no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    public static boolean conseguiuConsultaFilme = false;
    @SuppressWarnings("finally")
    public static String getConsultarFilme(Connection con, int handle_convenio){
        String valorFilme = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select valor from convenioFilme where handle_convenio=? order by dataAValer");
            stmtQuery.setInt(1, handle_convenio);
            ResultSet resultSet = stmtQuery.executeQuery();
            while(resultSet.next()){
                valorFilme = resultSet.getString("valor");
            }
            conseguiuConsultaFilme = false;
        }catch(SQLException e){
            conseguiuConsultaFilme = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar Valores de Filme do Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return valorFilme;
        }
    }
    
    
    /**
     * Consulta Todos os Grupos de Convênios existentes no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarGruposDeConvenios(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from convenios_grupos order by grupo_id");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Grupos de Convênios. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    
    /**
     * Consulta os dados de um grupo
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDadosDeUmGrupo(Connection con, int grupo_id){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from convenios_grupos where grupo_id=?");
        stmtQuery.setInt(1, grupo_id);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Grupo de Convênio. Procure o Administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    
    /**
     * atualiza o numero da nota dos arquivos texto de faturas
     * @param Connection 
     * @param Convenio
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdateNumeroNotaGrupo(Connection con, int numero_nota, int grupo_id){
        boolean cadastro = false;
        String sql = "update convenios_grupos set  numero_nota=? where grupo_id=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, numero_nota);
            stmt.setInt(2, grupo_id);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
        }finally{
            return cadastro;
        }
    }
    
    /**
     * atualiza o numero da nota dos arquivos texto de faturas
     * @param Connection 
     * @param Convenio
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdateNumeroNotaConvenio(Connection con, int numero_nota, int handle_convenio){
        boolean cadastro = false;
        String sql = "update convenio set  numero_nota=? where handle_convenio=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, numero_nota);
            stmt.setInt(2, handle_convenio);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Cadastra um grupo de convenio no Banco de Dados
     * @param Connection 
     * @param Convenio
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrarGrupoDeConvenio(Connection con, String nome, int geraTxt){
        boolean cadastro = false;
        String sql = "insert into convenios_grupos (nome, gera_arquivo_texto, numero_nota) values(?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setInt(2, geraTxt);
            stmt.setInt(3, 0);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            cadastro = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Grupo de Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * atualiza um grupo de convenio
     * @param Connection 
     * @param Convenio
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdateGrupoDeConvenio(Connection con, String nome, int gera_txt, int grupo_id){
        boolean cadastro = false;
        String sql = "update convenios_grupos set nome=?, gera_arquivo_texto=? where grupo_id=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setInt(2, gera_txt);
            stmt.setInt(3, grupo_id);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            cadastro = false;
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Grupo de Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um Convenio do Banco De Dados
     * @param Connection
     * @param ExameMODEL 
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletarGrupoDeConveio(Connection con, int grupo_id){
        boolean deleto = false; 
        String sql="delete from convenios_grupos where grupo_id=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, grupo_id);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Grupo de Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
    /**
     * atualiza o numero de fatura de um grupo de convenio
     * @param Connection 
     * @param Convenio
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdateNumeroFaturoGrupoConvenios(Connection con, int numero_fatura, int grupo_id){
        boolean cadastro = false;
        String sql = "update convenios_grupos set numero_fatura=? where grupo_id=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, numero_fatura);
            stmt.setInt(2, grupo_id);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            cadastro = false;
        }finally{
            return cadastro;
        }
    }
    
    /**
     * atualiza o numero de fatura de um convenio
     * @param Connection 
     * @param Convenio
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdateNumeroFaturaConvenio(Connection con, int numero_fatura, int handle_convenio){
        boolean cadastro = false;
        String sql = "update convenio set numero_fatura=? where handle_convenio=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, numero_fatura);
            stmt.setInt(2, handle_convenio);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            cadastro = false;
        } finally {
            return cadastro;
        }
    }
}
