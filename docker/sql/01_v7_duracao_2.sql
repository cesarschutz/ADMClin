/* Campo criado no commit 54415fe (fev/2016) e que nao existe em nenhum dos .FDB do repositorio */
ALTER TABLE EXAMES ADD DURACAO_2 INTEGER;
COMMIT;
UPDATE EXAMES SET DURACAO_2 = DURACAO WHERE DURACAO_2 IS NULL;
COMMIT;
