#!/bin/bash
# Compila e abre o ADMClin apontando para o Firebird do Docker (docker/carregar-bancos.sh).
# Uso: ./rodar.sh [DBRIS_DEMO|DBRIS_V7_DECARLI|DBRIS_V7]   (padrao: DBRIS_DEMO)
set -e
cd "$(dirname "$0")"
BANCO=${1:-DBRIS_DEMO}
CP="$(ls src/*.jar | grep -v -E 'sources|javadoc' | tr '\n' ':')forms-1.3.0.jar:miglayout15-swing.jar"

rm -rf build && mkdir -p build
find src -name '*.java' > build/fontes.txt
javac -nowarn --release 8 -encoding UTF-8 -cp "$CP" -d build @build/fontes.txt 2>&1 | grep -v -E '^Note|warning' || true
cp -R src/br/bcn/admclin/imagens build/br/bcn/admclin/

export RISIP=localhost
export RISDB=/firebird/data/$BANCO.FDB
export PACDB=/firebird/data/PAC.FDB   # laudos (demo minimo)
export NOMEPORTAL=PORTAL
exec java -cp "build:$CP" br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal
