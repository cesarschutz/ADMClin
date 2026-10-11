#!/bin/bash
# Substitui gnome-open/lpr/cmd (Windows) no container: guarda uma copia de tudo que o app manda abrir ou imprimir
mkdir -p /trabalho/abertos
for f in "$@"; do [ -f "$f" ] && cp "$f" "/trabalho/abertos/$(date +%H%M%S)-$(basename "$f")"; done
exit 0
