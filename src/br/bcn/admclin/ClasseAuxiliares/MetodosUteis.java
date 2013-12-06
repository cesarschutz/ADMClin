package br.bcn.admclin.ClasseAuxiliares;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

/**
 * 
 * @author Cesar Schutz
 */
public class MetodosUteis {
    /**
     * Metodo para verificar se um ComboBox foi selecionado. Ou seja, Index deve ser diferente de 0 para considerar que
     * foi preenchido. Se não foi selecionado, imprimi no Jtextfield passado como argumento: "Selecione o(s) Combo Box".
     * 
     * @param JComboBox
     *            que será testado
     * @param JTextField
     *            será informado ao usuario nesete TextField
     * @return VERDADEIRO se foi selecionado e FALSO se nao foi selecionado.
     */
    public static boolean VerificarSeComboBoxFoiSelecionado(JComboBox<?> comboBox, JTextField mensagemParaUsuario) {
        boolean selecionado = false;
        if (comboBox.getSelectedIndex() != 0) {
            selecionado = true;
        } else {
            mensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            mensagemParaUsuario.setText("Selecione o(s) Combo Box");
        }
        return selecionado;
    }

    /**
     * Metodo que verifica se jTextField tem o minimo de caracteres exigidos pelo parametro passado. Se não tiver o
     * minimo exigido pelo parametro, imprimi na tela: "Preencha corretamente os campos demarcados." E tambem pinta de
     * vermelho o JTextField q foi feita a verificação.
     * 
     * @param textField
     *            a ser verificado
     * @param qtdCaracteres
     *            caracteres exigidos como minimo
     * @param mensagemParaUsuario
     *            imprimi o erro
     * @return VERDADEIRO se tiver os caracteres exigidos e FALSE se nao tiver o minimo de caracteres exigidos.
     */
    public static boolean VerificarSeTextFieldContemMinimoDeCarcteres(JTextField textField, int qtdCaracteres,
        JTextField mensagemParaUsuario) {
        boolean preenchido = false;
        String semEspaco = textField.getText().replace(" ", "");
        if (semEspaco.length() >= qtdCaracteres) {
            preenchido = true;
        } else {
            textField.setBackground(new java.awt.Color(255, 170, 170));
            mensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            mensagemParaUsuario.setText("Preencha corretamente os campos demarcados");
        }
        return preenchido;
    }

    /**
     * Veririfica se campo com mascara foi preenchido. Se nao foi preenchido, pinta de vermelho o JTextField verificado
     * e imprimi o erro: "Preencha corretamente os campos demarcados".
     * 
     * @param JFormattedTextField
     *            que será verificado
     * @param mensagemParaUsuario
     *            que imprimi o erro
     * @return VERDADEIRO se campo foi preenchido e FALSO se nao foi preenchido.
     */
    public static boolean verificarSeCampoComMascaraFoiPrenchido(JTextField textfield, JTextField mensagemParaUsuario,
        String mascara) {
        boolean preenchido = false;
        if (textfield.getText().equals(mascara)) {
            textfield.setBackground(new java.awt.Color(255, 170, 170));
            mensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            mensagemParaUsuario.setText("Preencha corretamente os campos demarcados");
        } else {
            preenchido = true;
        }
        return preenchido;
    }

    /**
     * Veririfica se data com mascara é valida. dd/mm/aaaa valida se dia mes e ano estiverem corretos E a data nao for
     * maior que hoje!! Se nao foi preenchido, pinta de vermelho o JTextField verificado e imprimi o erro:
     * "Preencha corretamente os campos demarcados".
     * 
     * @param JFormattedTextField
     *            que será verificado
     * @param mensagemParaUsuario
     *            textfield que imprimi o erro
     * @return VERDADEIRO se data é valida e FALSO se data não é valida.
     */
    public static boolean verificarSeDataDeNascimentoEValida(JTextField textfield, JTextField mensagemParaUsuario) {

        String[] dataDividida = textfield.getText().split("/");

        // verificando ano
        boolean ano = false;
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat formataData = new SimpleDateFormat("yyyy");
        String anoAtual = formataData.format(hoje.getTime());
        int minimo = (Integer.parseInt(anoAtual)) - (150);
        if ((Integer.parseInt(dataDividida[2])) <= (Integer.parseInt(anoAtual))
            && (Integer.parseInt(dataDividida[2])) > minimo) {
            ano = true;
        }

        // verificando mes
        boolean mes = false;
        if ((Integer.parseInt(dataDividida[1])) <= 12 && (Integer.parseInt(dataDividida[1])) > 0) {
            mes = true;
        }

        // verificando dia
        boolean dia = false;
        if ((Integer.parseInt(dataDividida[1])) == 1) {
            if ((Integer.parseInt(dataDividida[0])) <= 31 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 2) {

            if (Integer.parseInt(dataDividida[2]) % 4 == 0) {
                if ((Integer.parseInt(dataDividida[0])) <= 29 && (Integer.parseInt(dataDividida[0])) > 0) {
                    dia = true;
                }
            } else {
                if ((Integer.parseInt(dataDividida[0])) <= 28 && (Integer.parseInt(dataDividida[0])) > 0) {
                    dia = true;
                }
            }

        } else if ((Integer.parseInt(dataDividida[1])) == 3) {
            if ((Integer.parseInt(dataDividida[0])) <= 31 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 4) {
            if ((Integer.parseInt(dataDividida[0])) <= 30 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 5) {
            if ((Integer.parseInt(dataDividida[0])) <= 31 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 6) {
            if ((Integer.parseInt(dataDividida[0])) <= 30 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 7) {
            if ((Integer.parseInt(dataDividida[0])) <= 31 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 8) {
            if ((Integer.parseInt(dataDividida[0])) <= 31 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 9) {
            if ((Integer.parseInt(dataDividida[0])) <= 30 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 10) {
            if ((Integer.parseInt(dataDividida[0])) <= 31 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 11) {
            if ((Integer.parseInt(dataDividida[0])) <= 30 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        } else if ((Integer.parseInt(dataDividida[1])) == 12) {
            if ((Integer.parseInt(dataDividida[0])) <= 31 && (Integer.parseInt(dataDividida[0])) > 0) {
                dia = true;
            }
        }

        // verificando se data é menor que hoje
        boolean menorQueDataAtual = false;
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setLenient(false);
            java.util.Date dataDigitada = format.parse(textfield.getText());
            String dataAtualString = format.format(hoje.getTime());
            java.util.Date dataAtual = format.parse(dataAtualString);

            int x = dataDigitada.compareTo(dataAtual);

            if (x > 0) {

            } else {
                menorQueDataAtual = true;
            }

        } catch (ParseException ex) {

        }

        // ok
        if (dia && mes && ano && menorQueDataAtual) {
            return true;
        } else {
            textfield.setBackground(new java.awt.Color(255, 170, 170));
            mensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            mensagemParaUsuario.setText("Preencha corretamente os campos demarcados");
            return false;
        }
    }

    /**
     * Verifica se e-mail é válido. para ser valido é necessario ser no minimo ssim #@#.# onde # = conjunto de
     * caracteres. Se nao foi preenchido, pinta de vermelho o JTextField verificado e imprimi o erro: "E-mail invalido".
     * 
     * @param textField
     * @return VERDADEIRO se email é valido e FALSO se email não é valido.
     */
    public static boolean verificarSeEmailEValido(JTextField textField, JTextField mensagemParaUsuario) {
        boolean valido = false;
        if (textField.getText().length() > 0) {
            if (textField.getText().indexOf("@") > 0
                && (textField.getText().indexOf(".") > 0 && textField.getText().length() - 1 > textField.getText()
                    .indexOf(".")) && textField.getText().indexOf("@.") == -1) {
                valido = true;
            } else {
                textField.setBackground(new java.awt.Color(255, 170, 170));
                mensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                mensagemParaUsuario.setText("E-mail Inválido");
            }
        } else {
            textField.setBackground(new java.awt.Color(255, 170, 170));
            mensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            mensagemParaUsuario.setText("E-mail Inválido");
        }
        return valido;
    }

    /**
     * Metodo utilizado na criação de mascara para campos JFormattedTextField. ex: JFormattedTextField campo = new
     * JFormattedTextField(new MetodosUteis.mascara("##:##"))
     * 
     * @param Mascara
     *            a ser aplicada no campo.
     * @return Formato da mascara
     */
    public static MaskFormatter mascaraParaJFormattedTextField(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return formatter;
    }

    /**
     * inverte uma data para salvar no banco ou para mostrar ao usuario
     * 
     * @param dataCorreta
     * @return dataBanco
     */
    public static String converterDataParaMostrarAoUsuario(String dataAMudar) {
        try {
            String dataArrumada = "";
            String[] camposDataCorreta = dataAMudar.split("-");
            dataArrumada = camposDataCorreta[2] + "/" + camposDataCorreta[1] + "/" + camposDataCorreta[0];
            return dataArrumada;
        } catch (Exception e) {
            return "";
        }

    }

    // trasformar o horario em minutos
    public static int transformarHorarioEmMinutos(String horario) {
        int minutos = 0;
        String[] horarioDeInicioQuebrado = horario.split(":");
        minutos = (Integer.valueOf(horarioDeInicioQuebrado[0]) * 60) + Integer.valueOf(horarioDeInicioQuebrado[1]);
        return minutos;
    }

    // verificar se hora esta correta (00:00 ateh 24:00 e sem minutos passarem de 60)
    public static boolean verificarSeHoraEstaCorreta(JTextField textField, JTextField mensagemParaUsuario) {
        String[] horarioDeInicioQuebrado = textField.getText().split(":");
        if (Integer.valueOf(horarioDeInicioQuebrado[0]) <= 23 && Integer.valueOf(horarioDeInicioQuebrado[0]) >= 0
            && Integer.valueOf(horarioDeInicioQuebrado[1]) >= 0 && Integer.valueOf(horarioDeInicioQuebrado[1]) <= 59) {
            return true;
        } else {
            textField.setBackground(new java.awt.Color(255, 170, 170));
            mensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            mensagemParaUsuario.setText("Hora Inválida");
            return false;
        }
    }

    // verificar se hora esta correta (00:00 ateh 24:00 e sem minutos passarem de 60)
    public static boolean verificarSeHoraEstaCorreta(String x) {
        String[] horarioDeInicioQuebrado = x.split(":");
        if (Integer.valueOf(horarioDeInicioQuebrado[0]) <= 23 && Integer.valueOf(horarioDeInicioQuebrado[0]) >= 0
            && Integer.valueOf(horarioDeInicioQuebrado[1]) >= 0 && Integer.valueOf(horarioDeInicioQuebrado[1]) <= 59) {
            return true;
        } else {
            return false;
        }
    }

    // transformar minutos em horario
    public static String transformarMinutosEmHorario(int minutos) {
        String hora = String.valueOf(minutos / 60);
        String minuto = String.valueOf(minutos % 60);
        if (minuto.length() < 2) {
            minuto = "0" + minuto;
        }
        if (hora.length() < 2) {
            hora = "0" + hora;
        }
        return hora + ":" + minuto;
    }

    /*
     * Metodo que retorna um array com os nome para pesquisar o q ele faz eh tirar os espaços e verifica se cada parte
     * do nome tem no minimo 2 caracteres, se nao tiver 2 ele eh "apagado" se retorna NULO, nao pesquiso se retornar com
     * uma posição, faço o select de um nome se retorna duas posições, faço select de dois nomes se retornar 3 ?
     */
    public static String[] formatarParaPesquisarNome(String nome) {
        String[] nomes = nome.split(" ");
        String nomesv = "";
        // se nomes separados eh maior que 0
        if (nomes.length > 0) {
            for (int i = 0; i < nomes.length; i++) {
                if (nomes[i].length() > 2)
                    nomesv += nomes[i].trim() + (i + 1 < nomes.length ? " " : "");
            }

            String[] nomesf = nomesv.split(" ");
            if ((nomesf.length == 1 && nomesf[0].length() > 2) || nomesf.length > 1)
                return nomesf;
            else
                return null;
        } else {
            return null;
        }
    }

    /*
     * Metodo que verfica se o nome deuma pessoa é valido
     */
    public static boolean veriricarSeNomeDePessaoEhValido(JTextField textField, JTextField mensagemParaUsuario) {
        String[] nomes = textField.getText().split(" ");

        if (nomes.length == 1 || nomes.length == 0) {
            textField.setBackground(new java.awt.Color(255, 170, 170));
            mensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            mensagemParaUsuario.setText("Nome inválido");
            return false;
        } else {
            for (int i = 0; i < nomes.length; i++) {
                if (nomes[i].trim().length() < 2) {

                    textField.setBackground(new java.awt.Color(255, 170, 170));
                    mensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    mensagemParaUsuario.setText("Nome inválido");
                    return false;
                }
            }
        }
        return true;
    }

    // colocando mais um zero caso de 0,90
    // ele mostrava 0,9
    // aqui estamos colocando 0 que faltou
    public static String colocarZeroEmCampoReais(String valor) {
        String[] valores = valor.split("\\.");
        valores[1].length();
        if (valores[1].length() < 2) {
            valores[1] = valores[1] + "0";
        }

        String retorno;
        retorno = valores[0] + "." + valores[1];

        return retorno;
    }

    // colocando mais um zero caso de 0,90
    // ele mostrava 0,9
    // aqui estamos colocando 0 que faltou
    public static String colocarZeroEmCampoReais(Double valor) {
        String valorString = String.valueOf(valor);
        String[] valores = valorString.split("\\.");
        valores[1].length();
        if (valores[1].length() < 2) {
            valores[1] = valores[1] + "0";
        }

        String retorno;
        retorno = valores[0] + "." + valores[1];

        return retorno;
    }

    // metodo que calcula a idade a partir de uma data
    public static String calculaIdade(String dataNasc, String pattern) {
        DateFormat sdf = new SimpleDateFormat(pattern);
        Date dataNascInput = null;
        try {
            dataNascInput = sdf.parse(dataNasc);
        } catch (Exception e) {
        }

        Calendar dateOfBirth = new GregorianCalendar();
        dateOfBirth.setTime(dataNascInput);

        // Cria um objeto calendar com a data atual
        Calendar today = Calendar.getInstance();

        // Obtém a idade baseado no ano
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        dateOfBirth.add(Calendar.YEAR, age);

        if (today.before(dateOfBirth)) {
            age--;
        }
        return String.valueOf(age);

    }

    @SuppressWarnings("finally")
    public static String imprimir(String comando, String tipo, String handleat) {

        boolean gerouErro = false;

        String temp[] = comando.split(" ");
        if (temp.length < 1)
            return "NOT";

        String urlhttp = temp[0];
        String caminho = temp[1];

        // JOptionPane.showMessageDialog(null, temp[0]);
        // JOptionPane.showMessageDialog(null, temp[1]);

        HttpURLConnection connection = null;
        BufferedReader rd = null;
        StringBuilder sb = null;
        String line = null;

        URL serverAddress = null;

        try {

            String url = urlhttp + "?tipo=" + tipo + "&caminho=" + caminho + "&codigo=" + handleat;

            serverAddress = new URL(url);

            connection = null;

            // Set up the initial connection
            connection = (HttpURLConnection) serverAddress.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);

            connection.connect();

            // read the result from the server
            rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            sb = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                sb = sb.append(line);
            }

        } catch (Exception e) {
            gerouErro = true;
        } finally {
            connection.disconnect();
            rd = null;
            connection = null;
            if (gerouErro) {
                return "NOT";
            } else {
                return sb.toString();
            }

        }
    }
}
