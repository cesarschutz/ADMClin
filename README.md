ADMClin
=======

Projeto ADMClin - BCN MEDICAL SYSTEM

=================================================

Cuidados:
Agenda deve ser cadastrada uma com ID = 0, isso para que quando forem todas as agendas a participar ed um feriado/intervalo marcar essa de ID 0.
Todos os generators devem estar como 1 quando o banco estiver zerado.

A tabela CONVENIO_GRUPOS deve ter o grupo com ID = 0, isso para que os convenios que não pertencerem a nenhum grupo vão pertencer a este grupo.

a tabela MATERIAIS tem o material 0, que significa que não tem material vinculado quano o exame for colocado nas tabelas

a tabela PACIENTE precisa ter um paciente com id 0 para quando for reservar um atendimento ou um agendamento

a tabela CONVENIOS precisa ter um convenio com id 0 para quando for reservar um atendimento ou um agendamento

a tabela EXAMES precisa ter um exame com id 0 para quando for reservar um atendimento ou um agendamento

a tabela ESPECIALIDADE MEDICA precisa ter uma especialidade com o hanlde 0 para caso queria salvar sem especialidade medica.

a tabela MEDICOS precisa ter um medico com id 0 para quando for reservar um atendimento
e o nome do medico deve ser NAO INFORMADO para nao sair o mmedico nos laudos




NO BANCO DO PAC:
- o campo LAUDO na tabela JLAUDOS deve ser NONE ao inves de ASCII para aparecer os ascentos!!!!!!
- o campo CODIGO e CODTXT na tabela CODIGOS deve ser NONE ao inves de ASCII!!

- deve ser criado o campo HANLDE_AT na tabela jlaudos





VARIAVEIS DE AMBIENTE

PACDB      - caminho do banco DB
RISDB      - caminho do banco DBRIS.FDB
RISIP      - ip do servidor
NOMEPORTAL - nome do portal (geralmente com nome de PORTAL)


MODELOS DE IMPRESSÃO!

MODELO 1 - unirad e digimax
MODELO 2 - conrad
MODELO 3 - de carli - COMO RODA EM LINUX, DEVE TER O ARQUIVO tahoma.ttf na mesma pasta do .jar
					  As definições ocorrem no metodo MAIN e no metodo contrutor da janela principal!
					  No caso da DeCarli como não temos variaveis de ambiente (linux), o ip é definido manualmente na hora da compilação
MODELO 4 - CIDI - tem somente a ficha pois é diferente, nela sai junto o boleto de retirada! Para a etiqueta é utilizada a mesma classe do modelo 1.



APOS A VERSÃO 6, NAS ALTERAÇÕES DA AGENDA.
-> NÃO APAGAR OS CAMPOS MODALIDADE E HORARIO NA TABELA ATENDIMENTOS.
-> SOMENTE ADICIONAR NA TABELA ATENDIMENTO_EXAMES ESSES DOIS CAMPOS.

-> criada tabela AREAS_ATENDIMENTO 
    nessa tabela temos o handle 0 que significa sem area de atendimento valor do campo SEM ÁREA
-> colocado o campo ID_AREAS_ATENDIMENTO na tabela AGENDAS com chave estrangeira apontando para PK da tabela AREAS_ATENDIMENTO
-> colocado o campo ID_AREAS_ATENDIMENTO na tabela EXAMES com chave estrangeira apontando para PK da tabela AREAS_ATENDIMENTO
