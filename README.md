ADMClin
=======

Projeto ADMClin - BCN MEDICAL SYSTEM

=================================================

Versão 2.0 para a versão 2.1
Alterados os tipos de campos no banco de dados.

Cuidados:
Agenda deve ser cadastrada uma com ID = 0, isso para que quando forem todas as agendas a participar ed um feriado/intervalo marcar essa de ID 0.
Todos os generators devem estar como 1 quando o banco estiver zerado.

A tabela CONVENIO_GRUPOS deve ter o grupo com ID = 0, isso para que os convenios que não pertencerem a nenhum grupo vão pertencer a este grupo.

a tabela MATERIAIS tem o material 0, que significa que não tem material vinculado quano o exame for colocado nas tabelas

a tabela PACIENTE precisa ter um paciente com id 0 para quando for reservar um atendimento ou um agendamento

a tabela CONVENIOS precisa ter um convenio com id 0 para quando for reservar um atendimento ou um agendamento

a tabela EXAMES precisa ter um exame com id 0 para quando for reservar um atendimento ou um agendamento

a tabela MEDICOS precisa ter um medico com id 0 para quando for reservar um atendimento



NO BANCO DO PAC - o campo LAUDO na tabela JLAUDOS deve ser NONE ao inves de ASCII para aparecer os ascentos!!!!!!
NO BANCO PAC - o campo CODIGO e CODTXT na tabela CODIGOS deve ser NONE ao inves de ASCII!!
