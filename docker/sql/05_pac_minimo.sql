/* REFERENCIA: estrutura do banco do PAC (laudos) MINIMO usado no demo (docker/demo/PAC_DEMO.FBK.gz).
   O original nao esta no repositorio.
   So as tabelas que o ADMClin usa (dao/db/JLAUDOS.java e CODIGOS.java). LAUDO/CODTXT em charset NONE (ver README antigo). */
CREATE DATABASE 'localhost:/firebird/data/PAC.FDB' USER 'SYSDBA' PASSWORD 'masterkey' PAGE_SIZE 16384 DEFAULT CHARACTER SET NONE;

CREATE TABLE JLAUDOS (
  HANDLE_AT     INTEGER NOT NULL PRIMARY KEY,
  LAUDO         BLOB SUB_TYPE TEXT CHARACTER SET NONE,
  FLAGSIGN      INTEGER,
  FLAGRISUPDATE INTEGER,
  DATESIGN      VARCHAR(30),
  USR           VARCHAR(64)
);
CREATE TABLE CODIGOS (
  CODIGO VARCHAR(20) CHARACTER SET NONE NOT NULL PRIMARY KEY,
  CODTXT BLOB SUB_TYPE TEXT CHARACTER SET NONE
);
CREATE TABLE STUDY_DONE (
  STDID         INTEGER NOT NULL PRIMARY KEY,
  STD_ACCESSION VARCHAR(20),
  HANDLE_AT     INTEGER,
  FLAG2         VARCHAR(2),
  RADIOLOGISTA  VARCHAR(64)
);
COMMIT;

/* textos-padrao ficticios: no editor de laudo, digitar o codigo expande o texto */
INSERT INTO CODIGOS VALUES ('ABD', 'ECOGRAFIA ABDOMINAL TOTAL

Figado de dimensoes normais, contornos regulares e ecotextura homogenea.
Vesicula biliar normodistendida, de paredes finas, sem calculos.
Vias biliares intra e extra-hepaticas nao dilatadas.
Pancreas e baco sem alteracoes.
Rins topicos, de dimensoes normais, sem sinais de hidronefrose ou calculos.

IMPRESSAO: Exame dentro dos limites da normalidade.');
INSERT INTO CODIGOS VALUES ('TX', 'RADIOGRAFIA DE TORAX PA E PERFIL

Campos pleuropulmonares livres.
Seios costofrenicos livres.
Area cardiaca dentro dos limites da normalidade.
Mediastino centrado.

IMPRESSAO: Torax sem alteracoes radiologicas.');
COMMIT;
