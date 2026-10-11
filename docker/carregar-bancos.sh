#!/bin/bash
# Sobe o Firebird 2.5 e carrega os bancos dentro do volume Docker (os arquivos do repositorio nao sao alterados):
#   DBRIS_DEMO.FDB       banco de demonstracao (dados ficticios, usado nos prints do README) - restaurado de demo/
#   PAC.FDB              banco de laudos de demonstracao - restaurado de demo/
#   DBRIS_V7.FDB         "DBRIS VERSAO 7.FDB" + ajustes de esquema (sql/01 e 03)
#   DBRIS_V7_DECARLI.FDB "BDRIS VERSAO 7 DE CARLI" + ajustes de esquema (sql/01, 02 e 03)
#   BD_V6.FDB            "BD VERSAO 6.FDB" (esquema antigo, so para consulta)
# Uso: ./carregar-bancos.sh [--reset]   (--reset recarrega tudo, apagando o que foi alterado nos bancos)
set -e
cd "$(dirname "$0")"
RAIZ=..
FB=/usr/local/firebird/bin
[ "$1" = "--reset" ] && RESET=1

docker compose up -d firebird
until docker exec admclin-firebird sh -c "echo 'select 1 from rdb\$database;' | $FB/isql -q -u SYSDBA -p masterkey employee" >/dev/null 2>&1; do sleep 1; done

existe() { docker exec admclin-firebird test -f "/firebird/data/$1"; }
precisa() { [ "$RESET" = 1 ] || ! existe "$1"; }
dono() { docker exec -u root admclin-firebird chown firebird:firebird "/firebird/data/$1"; }

copiar() { # $1 = arquivo de origem no repositorio, $2 = nome no servidor
  docker cp "$RAIZ/$1" "admclin-firebird:/firebird/data/$2"
  dono "$2"
  echo "carregado: $1 -> $2"
}
aplicar() { # $1 = script sql, $2 = banco
  docker cp "sql/$1" "admclin-firebird:/tmp/$1"
  docker exec admclin-firebird $FB/isql -q -u SYSDBA -p masterkey -i "/tmp/$1" "localhost:/firebird/data/$2"
}
restaurar() { # $1 = backup em demo/, $2 = nome no servidor
  docker cp "demo/$1" "admclin-firebird:/tmp/$1"
  docker exec admclin-firebird sh -c "gunzip -c /tmp/$1 > /tmp/restore.fbk && rm -f /firebird/data/$2 && \
    $FB/gbak -c -fix_fss_metadata ISO8859_1 -user SYSDBA -password masterkey /tmp/restore.fbk localhost:/firebird/data/$2 && rm /tmp/restore.fbk"
  dono "$2"
  echo "restaurado: demo/$1 -> $2"
}

if precisa DBRIS_DEMO.FDB; then restaurar ADMCLIN_DEMO.FBK.gz DBRIS_DEMO.FDB; else echo "ja existe (mantido): DBRIS_DEMO.FDB"; fi
if precisa PAC.FDB; then restaurar PAC_DEMO.FBK.gz PAC.FDB; else echo "ja existe (mantido): PAC.FDB"; fi

if precisa DBRIS_V7.FDB; then
  copiar "DBRIS VERSAO 7.FDB" DBRIS_V7.FDB
  aplicar 01_v7_duracao_2.sql DBRIS_V7.FDB
  aplicar 03_colunas_codigo_atual.sql DBRIS_V7.FDB
fi
if precisa DBRIS_V7_DECARLI.FDB; then
  copiar "BDRIS VERSAO 7 DE CARLI" DBRIS_V7_DECARLI.FDB
  aplicar 01_v7_duracao_2.sql DBRIS_V7_DECARLI.FDB
  aplicar 02_decarli_alinhar_v7.sql DBRIS_V7_DECARLI.FDB
  aplicar 03_colunas_codigo_atual.sql DBRIS_V7_DECARLI.FDB
fi
if precisa BD_V6.FDB; then copiar "BD VERSAO 6.FDB" BD_V6.FDB; fi
echo "ok"
