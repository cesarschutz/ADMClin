<div align="center">

# ADMClin · BCN Medical System

**Sistema de gestão para clínicas de diagnóstico por imagem**
agenda, atendimento, laudos, faturamento de convênios e financeiro

![Java](https://img.shields.io/badge/Java-7%20%2F%20Swing-007396?logo=openjdk&logoColor=white)
![Firebird](https://img.shields.io/badge/Firebird-2.5-F40F02?logo=firebird&logoColor=white)
![iText](https://img.shields.io/badge/PDF-iText-4B8BBE)
![Docker](https://img.shields.io/badge/Docker-pronto%20para%20rodar-2496ED)
![Período](https://img.shields.io/badge/desenvolvido-2013--2016-555)

<img src="docs/screenshots/35-fichas-de-atendimento.png" width="820" alt="Fichas de atendimento do dia">

</div>

---

O **ADMClin** é um RIS (*Radiology Information System*) desktop, escrito em Java Swing, que desenvolvi entre 2013 e 2016 para clínicas de radiologia e ecografia. Ele cobre o fluxo inteiro: o paciente agenda, é atendido na recepção, faz o exame, o radiologista digita e assina o laudo, e o financeiro fatura o convênio e concilia o que recebeu.

Em produção, o sistema rodava em várias clínicas, cada uma com seu próprio **modelo de impressão** (fichas, boletos de retirada e etiquetas). Ele também se integrava a um PACS (banco do PAC, onde ficam os laudos) e a um sistema de agenda externo.

> Todos os prints abaixo foram tirados do sistema rodando de verdade, num banco de **demonstração com dados fictícios** (pacientes, médicos, CPFs e a empresa "CLINICA EXEMPLO" são inventados). Veja [Como rodar](#-como-rodar).

## Sumário

- [Visão geral e navegação](#-visão-geral-e-navegação)
- [Cadastros](#-cadastros): agendas, convênios e tabelas de preço, exames, pessoal
- [Atendimentos](#-atendimentos): agendamento → atendimento, fichas, história clínica, laudos
- [Financeiro](#-financeiro): relatórios, DMED, faturamento e recebimento de convênios
- [Documentos gerados](#-documentos-gerados-pdf)
- [Como rodar](#-como-rodar)
- [Tecnologia e arquitetura](#-tecnologia-e-arquitetura)
- [Bancos de dados do repositório](#-bancos-de-dados-do-repositório)
- [Notas técnicas originais](#-notas-técnicas-originais)

---

## 🧭 Visão geral e navegação

O sistema abre numa janela única com menu superior. Cada tela é um formulário interno, centralizado na área de trabalho. O acesso é por usuário e senha, e **o tipo de usuário define o que ele vê**: o radiologista, por exemplo, só enxerga o menu de Atendimentos.

<table>
<tr>
<td width="50%"><img src="docs/screenshots/01-login.png" alt="Login"><br><sub><b>Login</b>: a versão do sistema aparece no canto do painel.</sub></td>
<td width="50%"><img src="docs/screenshots/40-menu-radiologista.png" alt="Menu do radiologista"><br><sub><b>Perfil radiologista</b>: só o menu de atendimentos e laudos.</sub></td>
</tr>
</table>

<details>
<summary><b>Ver todos os menus</b></summary>
<br>
<table>
<tr>
<td width="50%"><img src="docs/screenshots/03-menu-cadastros-agendas.png" alt="Menu Cadastros > Agendas"><br><sub>Cadastros › Agendas</sub></td>
<td width="50%"><img src="docs/screenshots/04-menu-cadastros-convenios.png" alt="Menu Cadastros > Convênios"><br><sub>Cadastros › Convênios</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/05-menu-cadastros-exames.png" alt="Menu Cadastros > Exames"><br><sub>Cadastros › Exames</sub></td>
<td><img src="docs/screenshots/06-menu-cadastros-pessoal.png" alt="Menu Cadastros > Pessoal"><br><sub>Cadastros › Pessoal</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/07-menu-atendimentos.png" alt="Menu Atendimentos"><br><sub>Atendimentos (com atalhos F8 e F9)</sub></td>
<td><img src="docs/screenshots/08-menu-financeiro.png" alt="Menu Financeiro"><br><sub>Financeiro › Relatórios</sub></td>
</tr>
</table>
</details>

---

## 🗂 Cadastros

### Agendas

Cada agenda pertence a uma área de atendimento (Raio-X, Ecografia, Tomografia…) e tem **turnos por dia da semana** (manhã, tarde, noite e madrugada) e a **duração padrão de cada exame**. O botão *Replicar* copia a configuração de um dia para a semana toda. Horários específicos podem ser **bloqueados**, e os **feriados** fecham a agenda no dia.

<table>
<tr>
<td width="50%"><img src="docs/screenshots/10-agenda-cadastro.png" alt="Cadastro de agenda"><br><sub>Cadastro da agenda: área de atendimento e descrição.</sub></td>
<td width="50%"><img src="docs/screenshots/11-agenda-turnos.png" alt="Turnos da agenda"><br><sub>Turnos por dia da semana, com duração e início/fim de cada período.</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/12-agenda-bloqueios.png" alt="Bloqueios de horário"><br><sub>Bloqueio de horários pelo menu de contexto (clique direito).</sub></td>
<td><img src="docs/screenshots/13-feriados-lista.png" alt="Feriados"><br><sub>Feriados (dia/mês), aplicados a todas as agendas.</sub></td>
</tr>
</table>

### Convênios e tabelas de preço

O núcleo financeiro do sistema. Cada convênio tem **valores de CH e de Filme com vigência por data**, percentuais de cobertura (convênio × paciente), redutor, regra de validação de matrícula e a empresa que fatura. Os convênios podem ser agrupados (por exemplo, todas as categorias do IPÊ), e cada um tem sua **tabela de exames** com código, CH1, CH2 e coeficiente de filme. Na própria tabela dá para calcular o valor de um exame em qualquer data.

<table>
<tr>
<td width="50%"><img src="docs/screenshots/15-convenios-lista.png" alt="Lista de convênios"><br><sub>Convênios cadastrados.</sub></td>
<td width="50%"><img src="docs/screenshots/16-convenio-edicao.png" alt="Edição de convênio"><br><sub>Dados do convênio, percentuais, redutor, grupo e empresa.</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/17-convenio-valores-ch.png" alt="Valores de CH"><br><sub>Histórico de valores do CH, com data de vigência (o Filme funciona igual).</sub></td>
<td><img src="docs/screenshots/18-grupo-convenios.png" alt="Grupo de convênios"><br><sub>Grupo de convênios: define se gera arquivo texto na fatura.</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/19-tabela-precos-convenio.png" alt="Tabela de preços"><br><sub>Tabela de preços por modalidade, com materiais e cálculo do valor.</sub></td>
<td><img src="docs/screenshots/20-tabela-editar-coeficientes.png" alt="Coeficientes do exame"><br><sub>Código, sinônimo e coeficientes de um exame na tabela.</sub></td>
</tr>
</table>

### Exames e materiais

<table>
<tr>
<td width="50%"><img src="docs/screenshots/22-exame-cadastro.png" alt="Cadastro de exame"><br><sub>Exame: duas durações (para médicos mais rápidos ou mais lentos), área, classe, prazo em horas úteis e <b>dieta/preparo</b>, que sai impressa para o paciente.</sub></td>
<td width="50%"><img src="docs/screenshots/21-classes-de-exames.png" alt="Classes de exames"><br><sub>Classes de exames, ligadas às modalidades DICOM (CR, US, MG, CT…).</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/23-materiais-lista.png" alt="Materiais"><br><sub>Materiais e medicamentos (contraste, agulhas…).</sub></td>
<td><img src="docs/screenshots/24-material-valores.png" alt="Valores do material"><br><sub>Valores do material com vigência por data.</sub></td>
</tr>
</table>

### Pessoal

<table>
<tr>
<td width="50%"><img src="docs/screenshots/28-paciente-cadastro.png" alt="Cadastro de paciente"><br><sub>Paciente: documentos, contato, peso/altura, idade calculada e responsável.</sub></td>
<td width="50%"><img src="docs/screenshots/27-medico-cadastro.png" alt="Cadastro de médico"><br><sub>Médico solicitante: CRM/UF, especialidade e secretária.</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/26-medicos-pesquisa.png" alt="Pesquisa de médicos"><br><sub>Pesquisa por nome (as listas de pacientes e médicos funcionam assim).</sub></td>
<td><img src="docs/screenshots/25-especialidades-medicas.png" alt="Especialidades médicas"><br><sub>Especialidades médicas.</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/29-responsavel-tecnico.png" alt="Responsável técnico"><br><sub>Responsável técnico (conselho e registro na ANS).</sub></td>
<td><img src="docs/screenshots/30-usuario-cadastro.png" alt="Cadastro de usuário"><br><sub>Usuário: tipo/perfil e impressoras de cada estação (ficha, nota, etiqueta, código de barras).</sub></td>
</tr>
</table>

---

## 🩺 Atendimentos

### Do agendamento ao atendimento

Os agendamentos chegavam de um sistema de agenda externo. No ADMClin, a recepção vê o dia por área e agenda e, com um clique direito, **transforma o agendamento em atendimento**: paciente, convênio, área e exames já vêm preenchidos. O sistema calcula na hora o valor de cada exame, a duração total e a divisão entre paciente e convênio.

<table>
<tr>
<td width="50%"><img src="docs/screenshots/31-agendamentos.png" alt="Agendamentos do dia"><br><sub>Agendamentos do dia. O ✔ indica que o agendamento já virou atendimento.</sub></td>
<td width="50%"><img src="docs/screenshots/32-agendamento-registrar-atendimento.png" alt="Registrar a partir do agendamento"><br><sub>Clique direito: exames agendados e opção de registrar o atendimento.</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/33-registrar-atendimento.png" alt="Registro do atendimento"><br><sub>Registro do atendimento: valores calculados pela tabela do convênio, entrega do exame e desconto.</sub></td>
<td><img src="docs/screenshots/34-atendimento-selecionar-paciente.png" alt="Selecionar paciente"><br><sub>Seleção, edição ou cadastro rápido do paciente sem sair do atendimento.</sub></td>
</tr>
</table>

### Fichas de atendimento

A tela de trabalho do dia. Cada atendimento mostra o **status** (em atendimento → exame realizado → laudo digitado → laudo assinado), a **entrega** (exame e/ou laudo entregue) e se o paciente pagou. Pelo menu de contexto, imprime a ficha, o boleto de retirada e a etiqueta do envelope, e abre a história clínica e o laudo.

<table>
<tr>
<td width="50%"><img src="docs/screenshots/36-fichas-legendas.png" alt="Legenda dos status"><br><sub>Legenda dos ícones de status e de entrega.</sub></td>
<td width="50%"><img src="docs/screenshots/37-fichas-menu-imprimir.png" alt="Menu de impressão"><br><sub>Impressões disponíveis para o atendimento.</sub></td>
</tr>
<tr>
<td colspan="2"><img src="docs/screenshots/38-historia-clinica.png" alt="História clínica"><br><sub>História clínica do atendimento.</sub></td>
</tr>
</table>

### Laudos

O radiologista digita o laudo usando **textos-padrão**: basta escrever `==CODIGO` e apertar **F5** para o código virar o texto completo. Laudos novos também podem ser gravados como código. A **assinatura** exige uma senha pessoal, guardada criptografada. Depois de assinado, o laudo fica bloqueado para edição e pode ser exportado em PDF.

<table>
<tr>
<td width="50%"><img src="docs/screenshots/39-laudo-editor.png" alt="Editor de laudo"><br><sub>Editor de laudo, com o texto-padrão expandido.</sub></td>
<td width="50%"><img src="docs/screenshots/42-laudo-assinado.png" alt="Laudo assinado"><br><sub>Laudo assinado: só leitura e "Gerar PDF" habilitado.</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/41-laudo-definir-senha-assinatura.png" alt="Senha de assinatura"><br><sub>Na primeira assinatura, o radiologista define a própria senha.</sub></td>
<td><img src="docs/screenshots/43-pesquisar-atendimentos.png" alt="Pesquisar atendimentos"><br><sub>Pesquisa de atendimentos (histórico do paciente) por código ou nome (F9).</sub></td>
</tr>
</table>

### Caixa e consulta de valores

<table>
<tr>
<td width="50%"><img src="docs/screenshots/45-consultar-valores-exames.png" alt="Consultar valores"><br><sub>Consulta rápida do valor de um exame num convênio, com e sem materiais.</sub></td>
<td width="50%"><img src="docs/screenshots/44-relatorio-de-caixa-filtro.png" alt="Relatório de caixa"><br><sub>Relatórios de caixa e de movimento por período (os PDFs estão <a href="#-documentos-gerados-pdf">abaixo</a>).</sub></td>
</tr>
</table>

---

## 💰 Financeiro

O ciclo do convênio é **faturar** os atendimentos do período (o que gera a fatura em PDF e, para alguns convênios, um arquivo texto), **ajustar valores** se necessário e **registrar o recebimento** exame a exame. O relatório de valores recebidos mostra as diferenças entre o que foi faturado e o que foi pago.

<table>
<tr>
<td width="50%"><img src="docs/screenshots/48-faturamento-convenio.png" alt="Faturamento"><br><sub>Faturamento: seleção dos atendimentos por convênio ou grupo e período.</sub></td>
<td width="50%"><img src="docs/screenshots/49-alterar-valores-convenio.png" alt="Alterar valores"><br><sub>Ajuste dos valores faturados por exame.</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/51-recebimento-convenio-lista.png" alt="Recebimento"><br><sub>Recebimento de convênio: o ✔ marca os exames já conciliados.</sub></td>
<td><img src="docs/screenshots/50-recebimento-convenio-valor.png" alt="Valor recebido"><br><sub>Informar o valor e a data do recebimento (barra de espaço na linha).</sub></td>
</tr>
<tr>
<td><img src="docs/screenshots/46-financeiro-relatorio-atendimentos.png" alt="Relatório financeiro"><br><sub>Relatório financeiro analítico por período e convênio.</sub></td>
<td><img src="docs/screenshots/47-dmed.png" alt="DMED"><br><sub>DMED: declaração anual dos valores pagos pelos pacientes (Receita Federal).</sub></td>
</tr>
</table>

Além dessas telas, o sistema tem a **alteração de valores da nota do IPÊ**, com geração do arquivo texto do IPÊ na fatura.

---

## 📄 Documentos gerados (PDF)

Fichas, laudos e relatórios são gerados em PDF com iText (as imagens abaixo são os PDFs convertidos).

<table>
<tr>
<td width="50%" valign="top"><b>Ficha de atendimento</b><br><img src="docs/documentos/ficha-de-atendimento.png" alt="Ficha de atendimento"></td>
<td width="50%" valign="top"><b>Laudo assinado</b><br><img src="docs/documentos/laudo-assinado.png" alt="Laudo"><br><br><b>Fatura do convênio</b> (primeira página)<br><img src="docs/documentos/fatura-convenio.png" alt="Fatura"></td>
</tr>
</table>

**Relatório financeiro analítico**
<img src="docs/documentos/relatorio-atendimentos-analitico.png" alt="Relatório analítico">

<details>
<summary><b>Outros relatórios: movimento, caixa, valores recebidos e DMED</b></summary>
<br>

**Relatório de movimento**
<img src="docs/documentos/relatorio-de-movimento.png" alt="Relatório de movimento">

**Relatório de caixa**
<img src="docs/documentos/relatorio-de-caixa.png" alt="Relatório de caixa">

**Relatório de valores recebidos**
<img src="docs/documentos/relatorio-valores-recebidos.png" alt="Valores recebidos">

**DMED**
<img src="docs/documentos/dmed.png" alt="DMED">
</details>

---

## 🚀 Como rodar

O projeto é de 2013 e depende de um **Firebird 2.5** (os bancos são ODS 11, que o Firebird 3 ou mais novo não abre). Tudo sobe com Docker.

### Opção 1: tudo no Docker, abrindo no navegador (recomendado)

```bash
./docker/carregar-bancos.sh          # sobe o Firebird e carrega os bancos (use --reset para recomeçar do zero)
docker compose -f docker/docker-compose.yml up -d --build app
```

Depois abra **http://localhost:6080/vnc.html**. O app roda numa tela virtual Linux (Xvfb + noVNC) e é compilado dentro do container.

### Opção 2: Firebird no Docker e app nativo (precisa de JDK 8+)

```bash
./docker/carregar-bancos.sh
./rodar.sh                # padrão: banco de demonstração; ou ./rodar.sh DBRIS_V7
```

### Usuários do banco de demonstração

| Usuário | Senha | Perfil |
|---|---|---|
| `BCN` | `demo` | administrador (todos os menus) |
| `DRCARLOS` | `1234` | radiologista (atendimentos e assinatura de laudos; senha de assinatura `1234`) |

Os agendamentos e atendimentos do demo estão entre **14/09/2026 e 12/10/2026**. Na tela de fichas, use a data 12/10/2026.

> Em Apple Silicon, a imagem do Firebird 2.5 (só existe para amd64) roda emulada, o que funciona normalmente.

---

## 🛠 Tecnologia e arquitetura

| | |
|---|---|
| **Linguagem / UI** | Java 7, Swing (Nimbus), JInternalFrames num JDesktopPane, MigLayout, JGoodies Forms, SwingX (date picker) |
| **Banco** | Firebird 2.5 via Jaybird 2.1.6 (JDBC), com dois bancos: **RIS** (cadastros, atendimentos, financeiro) e **PAC** (laudos, códigos de texto e estudos do PACS) |
| **Documentos** | iText 2.1.7 / 5.3.3 (fichas, laudos, faturas e relatórios em PDF); impressão direta em matricial (ESC/P) e Argox |
| **Configuração** | variáveis de ambiente `RISIP`, `RISDB`, `PACDB` e `NOMEPORTAL`; o modelo de impressão vem da tabela `DADOS_EMPRESA` |
| **Tamanho** | 187 classes Java, cerca de 60 mil linhas |

```
src/br/bcn/admclin/
├── interfacesGraficas/        telas (janela principal, login e menus: cadastros, atendimentos, financeiro)
├── dao/dbris/  dao/db/        acesso aos bancos RIS e PAC
├── dao/model/                 modelos
├── calculoValorDeUmExame/     regra de cálculo (CH × coeficientes + filme + materiais, % convênio/paciente, redutor)
├── financeiro/                faturamento e arquivo texto do IPÊ
├── impressoes/modelo1..6/     fichas, boletos e etiquetas de cada clínica
└── ClasseAuxiliares/          utilitários (datas, máscaras, criptografia, impressão)
docker/                        Firebird 2.5 + app com tela virtual, scripts SQL e backup do banco demo
docs/                          prints usados neste README
```

### Ajustes feitos para rodar hoje

- `StringEncrypter`: troca de `sun.misc.BASE64Encoder` (removido no Java 9) por `java.util.Base64`. Com isso o projeto compila em JDKs atuais.
- [`docker/sql/`](docker/sql): o código ficou mais novo que os bancos versionados. Os scripts criam as colunas que faltavam (`EXAMES.DURACAO_2`, `MEDICOS.OBS`, `CONVENIO.ID_DADOS_EMPRESA`, campos de entrega e de nota IPÊ…) e corrigem `FLAG_DESATIVADO` nulo, que escondia exames e tabelas.
- No Linux do container, as chamadas `cmd /c start`, `gnome-open` e `lpr` são interceptadas e o arquivo gerado é salvo em `docker/saida/`.

---

## 🗄 Bancos de dados do repositório

| Arquivo | Conteúdo | Situação |
|---|---|---|
| `DBRIS VERSAO 7.FDB` | Banco zerado (só os registros-base) | Esquema mais completo; com os ajustes de `docker/sql` fica compatível com o código |
| `BDRIS VERSAO 7 DE CARLI` | 23 convênios, 177 exames, cerca de 4 mil itens de tabela de preço, 48 materiais | Base do banco de demonstração; precisa dos ajustes de esquema |
| `BD VERSAO 6.FDB` | Banco zerado, versão antiga | Esquema antigo (agenda antiga, sem áreas de atendimento) |
| `docker/demo/*.FBK.gz` | Banco de demonstração (dados fictícios) e PAC mínimo | Usado nos prints e restaurado pelo `carregar-bancos.sh` |

---

## 📝 Notas técnicas originais

<details>
<summary>Anotações do README original (registros-base, variáveis e modelos de impressão)</summary>

**Registros-base obrigatórios**
- Agenda com ID = 0: usada quando todas as agendas participam de um feriado ou intervalo.
- Todos os generators devem estar em 1 quando o banco estiver zerado.
- `CONVENIO_GRUPOS` deve ter o grupo ID = 0 (convênios sem grupo).
- `MATERIAIS` tem o material 0 (exame sem material vinculado).
- `PACIENTES`, `CONVENIOS` e `EXAMES` precisam do registro ID = 0 para reservar atendimento ou agendamento.
- `ESPECIALIDADES_MEDICAS` precisa do handle 0, para salvar sem especialidade.
- `MEDICOS` precisa do ID 0 com o nome "NAO INFORMADO", para o médico não sair nos laudos.

**No banco do PAC**
- `JLAUDOS.LAUDO` deve ser charset NONE (e não ASCII) para os acentos aparecerem.
- `CODIGOS.CODIGO` e `CODIGOS.CODTXT` também devem ser NONE.
- Deve existir o campo `HANDLE_AT` na tabela `JLAUDOS`.

**Variáveis de ambiente**
- `PACDB`: caminho do banco do PAC
- `RISDB`: caminho do banco DBRIS.FDB
- `RISIP`: IP do servidor
- `NOMEPORTAL`: nome do portal (geralmente "PORTAL")

**Modelos de impressão**
- Modelo 1: Unirad e Digimax
- Modelo 2: Conrad
- Modelo 3: De Carli. Como roda em Linux, precisa do `tahoma.ttf` na mesma pasta do `.jar`. As definições ficam no `main` e no construtor da janela principal; sem variáveis de ambiente, o IP é definido na compilação.
- Modelo 4: CIDI. Só a ficha é diferente (sai junto com o boleto de retirada); a etiqueta usa a classe do modelo 1.
- Modelos 5 e 6: adicionados depois (pacotes `impressoes/modelo5` e `modelo2e3`); não estavam documentados no README original.

</details>

---

<div align="center">
<sub>Desenvolvido por <b>Cesar Schutz</b> · BCN Medical System · 2013–2016</sub>
</div>
