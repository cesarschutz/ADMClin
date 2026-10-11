# ADMClin rodando em Linux com tela virtual (Xvfb) acessivel pelo navegador (noVNC na porta 6080)
FROM eclipse-temurin:8-jdk-jammy
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
      xvfb x11vnc novnc websockify fluxbox xdotool x11-utils imagemagick poppler-utils fonts-dejavu-core \
    && rm -rf /var/lib/apt/lists/*
COPY iniciar.sh print.sh abrir-arquivo.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/*.sh \
    && ln -s /usr/local/bin/abrir-arquivo.sh /usr/local/bin/gnome-open \
    && ln -s /usr/local/bin/abrir-arquivo.sh /usr/local/bin/lpr \
    && ln -s /usr/local/bin/abrir-arquivo.sh /usr/local/bin/cmd
ENV DISPLAY=:0 RESOLUCAO=1280x800x24
EXPOSE 6080
CMD ["/usr/local/bin/iniciar.sh"]
