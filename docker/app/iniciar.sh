#!/bin/bash
# Sobe a tela virtual, o VNC/noVNC e o ADMClin (compilado a partir de /projeto, montado somente leitura)
Xvfb :0 -screen 0 "$RESOLUCAO" -nolisten tcp &
sleep 1
fluxbox >/dev/null 2>&1 &
x11vnc -display :0 -forever -shared -nopw -quiet -bg
websockify --web /usr/share/novnc 6080 localhost:5900 >/dev/null 2>&1 &

cd /projeto
CP="$(ls /projeto/src/*.jar | grep -v -E 'sources|javadoc' | tr '\n' ':')/projeto/forms-1.3.0.jar:/projeto/miglayout15-swing.jar"
mkdir -p /tmp/build
find src -name '*.java' > /tmp/fontes.txt
javac -nowarn -encoding UTF-8 -cp "$CP" -d /tmp/build @/tmp/fontes.txt 2>&1 | grep -v -E '^Note|warning'
cp -R src/br/bcn/admclin/imagens /tmp/build/br/bcn/admclin/

# o app le tahoma.ttf da pasta atual quando roda em Linux, e grava os PDFs (fichas, relatorios) nela
mkdir -p /trabalho && cp /projeto/tahoma.ttf /trabalho/ && cd /trabalho
while true; do
  java -cp "/tmp/build:$CP" br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal
  sleep 2   # se fechar a janela, abre de novo
done
