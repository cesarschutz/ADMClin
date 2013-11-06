
package menu.cadastros.convenio.model;

import java.sql.Date;

/**
 * Modelo de Convenios.
 * @author BCN
 */
public class conveniosMODEL {
    
    private int handle_convenio, regAns, remessa, numExtra, numExtra2, irmaoDoConv, diasParaNota, numMaxExamePorFicha, usuarioId, VALIDACAO_MATRICULA, IMPRIMI_ARQUIVO_TXT_COM_FATURA,grupoid;
    private String cgc, sigla, nome, endereco, cep, cidade, uf, telefone, contato, email, codPrestador, tipo, valorCh, valorFilme, porcentPaciente, porcentConvenio, porcentTabela,
            faturarJuntoConv, temDoc, validarMedico, tipoValidacao, arquivo, dataAValerCh, dataAValerFilme,redutor;
    private Date dat;

    public String getCgc() {
        return cgc;
    }

    public void setCgc(String cgc) {
        this.cgc = cgc;
    }

    public String getDataAValerCh() {
        return dataAValerCh;
    }

    public void setDataAValerCh(String dataAValerCh) {
        this.dataAValerCh = dataAValerCh;
    }

    public String getDataAValerFilme() {
        return dataAValerFilme;
    }

    public void setDataAValerFilme(String dataAValerFilme) {
        this.dataAValerFilme = dataAValerFilme;
    }
    
    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCodPrestador() {
        return codPrestador;
    }

    public void setCodPrestador(String codPrestador) {
        this.codPrestador = codPrestador;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public int getHandle_convenio() {
        return handle_convenio;
    }

    public void setHandle_convenio(int handle_convenio) {
        this.handle_convenio = handle_convenio;
    }

    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }

    public int getDiasParaNota() {
        return diasParaNota;
    }

    public void setDiasParaNota(int diasParaNota) {
        this.diasParaNota = diasParaNota;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFaturarJuntoConv() {
        return faturarJuntoConv;
    }

    public void setFaturarJuntoConv(String faturarJuntoConv) {
        this.faturarJuntoConv = faturarJuntoConv;
    }

    public int getIrmaoDoConv() {
        return irmaoDoConv;
    }

    public void setIrmaoDoConv(int irmaoDoConv) {
        this.irmaoDoConv = irmaoDoConv;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumExtra() {
        return numExtra;
    }

    public void setNumExtra(int numExtra) {
        this.numExtra = numExtra;
    }

    public int getNumExtra2() {
        return numExtra2;
    }

    public void setNumExtra2(int numExtra2) {
        this.numExtra2 = numExtra2;
    }

    public int getNumMaxExamePorFicha() {
        return numMaxExamePorFicha;
    }

    public void setNumMaxExamePorFicha(int numMaxExamePorFicha) {
        this.numMaxExamePorFicha = numMaxExamePorFicha;
    }

    public String getPorcentConvenio() {
        return porcentConvenio;
    }

    public void setPorcentConvenio(String porcentConvenio) {
        this.porcentConvenio = porcentConvenio;
    }

    public String getPorcentPaciente() {
        return porcentPaciente;
    }

    public void setPorcentPaciente(String porcentPaciente) {
        this.porcentPaciente = porcentPaciente;
    }

    public String getPorcentTabela() {
        return porcentTabela;
    }

    public void setPorcentTabela(String porcentTabela) {
        this.porcentTabela = porcentTabela;
    }

    public int getRegAns() {
        return regAns;
    }

    public void setRegAns(int regAns) {
        this.regAns = regAns;
    }

    public int getRemessa() {
        return remessa;
    }

    public void setRemessa(int remessa) {
        this.remessa = remessa;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTemDoc() {
        return temDoc;
    }

    public void setTemDoc(String temDoc) {
        this.temDoc = temDoc;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoValidacao() {
        return tipoValidacao;
    }

    public void setTipoValidacao(String tipoValidacao) {
        this.tipoValidacao = tipoValidacao;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getValidarMedico() {
        return validarMedico;
    }

    public void setValidarMedico(String validarMedico) {
        this.validarMedico = validarMedico;
    }

    public String getValorCh() {
        return valorCh;
    }

    public void setValorCh(String valorCh) {
        this.valorCh = valorCh;
    }

    public String getValorFilme() {
        return valorFilme;
    }

    public void setValorFilme(String valorFilme) {
        this.valorFilme = valorFilme;
    }

    public String getRedutor() {
        return redutor;
    }

    public void setRedutor(String redutor) {
        this.redutor = redutor;
    }

    public int getVALIDACAO_MATRICULA() {
        return VALIDACAO_MATRICULA;
    }

    public void setVALIDACAO_MATRICULA(int VALIDACAO_MATRICULA) {
        this.VALIDACAO_MATRICULA = VALIDACAO_MATRICULA;
    }

    public int getIMPRIMI_ARQUIVO_TXT_COM_FATURA() {
        return IMPRIMI_ARQUIVO_TXT_COM_FATURA;
    }

    public void setIMPRIMI_ARQUIVO_TXT_COM_FATURA(int IMPRIMI_ARQUIVO_TXT_COM_FATURA) {
        this.IMPRIMI_ARQUIVO_TXT_COM_FATURA = IMPRIMI_ARQUIVO_TXT_COM_FATURA;
    }

    public int getGrupoid() {
        return grupoid;
    }

    public void setGrupoid(int grupoid) {
        this.grupoid = grupoid;
    }
}
