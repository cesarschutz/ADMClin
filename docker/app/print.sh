#!/bin/bash
# print.sh NOME  -> salva /prints/NOME.png recortado na janela do ADMClin (com a barra de titulo)
ID=$(xdotool search --name '^ADMClin - BCN Medical System$' | head -1)
eval $(xwininfo -id "$ID" -frame 2>/dev/null | awk '/Absolute upper-left X/{print "X="$4} /Absolute upper-left Y/{print "Y="$4} /Width/{print "W="$2} /Height/{print "H="$2}')
# a moldura do fluxbox fica em volta da janela do Java
import -window root -crop "$((W+2))x$((H+24))+$((X-1))+$((Y-22))" +repage "/prints/$1.png"
echo "/prints/$1.png"
