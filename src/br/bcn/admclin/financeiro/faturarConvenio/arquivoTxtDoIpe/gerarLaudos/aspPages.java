/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.financeiro.faturarConvenio.arquivoTxtDoIpe.gerarLaudos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 *
 * @author BCN1
 */
public class aspPages {
    
    public static String[] getModeloLaudoDaEmpresa(String domain,String portal,String type) {

        HttpURLConnection connection = null;
        //OutputStreamWriter wr = null;
        BufferedReader rd = null;
        StringBuilder sb = null;
        String line = null;
        String decodedRetorno = "";
        String encodedRetorno = "";
        URL serverAddress = null;

        try {

            ////////System.out.println("TRY....");
            String url = "http://" + domain + "/" + portal + "/pags/jnreadparametroslaudos"+type+".asp";
            serverAddress = new URL(url);
            System.out.println("http://" + domain + "/" + portal + "/pags/jnreadparametroslaudos"+type+".asp");
            //set up out communications stuff
            connection = null;
            ////////System.out.println("SQL...."+url+"study="+estudoenc);
            //Set up the initial connection
            connection = (HttpURLConnection) serverAddress.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);

            connection.connect();

            //read the result from the server
            rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "ISO-8859-1"));

            sb = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                sb = sb.append(line);
            }
            ////System.out.println("retorno="+sb.toString());
        } catch (Exception e) {
            //return null;
        } finally {
            try {
                connection.disconnect();
                rd = null;
                connection = null;

                //aqui vamos criar o array list para retorno

                String retornoAsp[] = sb.toString().split("\\|\\|");
                retornoAsp[5] = retornoAsp[5].replaceAll("\\|\\#\\|", "");
                ////System.out.println("0 - " + retornoAsp[0]);
                ////System.out.println("1 - " + retornoAsp[1]);
                ////System.out.println("2 - " + retornoAsp[2]);
                ////System.out.println("3 - " + retornoAsp[3]);
                ////System.out.println("4 - " + retornoAsp[4]);
                ////System.out.println("5 - " + retornoAsp[5]);
                return retornoAsp;
                
            } catch(Exception e){
                return null;
            }
        }

    }
    public static String getInformacoesDoLaudo(String dirName,String typedoasp){
        //System.out.println("Estudo = "+estudo+" accnum = "+accession);
      // TODO code application logic here
      HttpURLConnection connection = null;
      //OutputStreamWriter wr = null;
      BufferedReader rd  = null;
      StringBuilder sb = null;
      String line = null;
      String decodedRetorno = "";
      String encodedRetorno = "";
      URL serverAddress = null;
    
      try {
          
          //////System.out.println("TRY....");
          String url = "http://"+MakeLaudoPdf.domain+"/"+MakeLaudoPdf.portalname+"/pags/jndadoslaudo"+typedoasp+".asp?";
          String dirNam = URLEncoder.encode(dirName,"ISO-8859-1"); 
          serverAddress = new URL(url+"dirname="+dirNam);
          //JOptionPane.showMessageDialog(null,url+"dirname="+dirNam);
          //set up out communications stuff
          connection = null;
          //////System.out.println("SQL...."+url+"study="+estudoenc);
          //Set up the initial connection
          connection = (HttpURLConnection)serverAddress.openConnection();
          connection.setRequestMethod("GET");
          connection.setDoOutput(true);
          connection.setReadTimeout(10000);
                    
          connection.connect();

          //read the result from the server
          rd  = new BufferedReader(new InputStreamReader(connection.getInputStream(),"ISO-8859-1"));
          
          sb = new StringBuilder();
          
          while ((line = rd.readLine()) != null)
          {
                sb = sb.append(line);
          }
         System.out.println("certo retorno="+sb.toString());
      } catch (Exception e) {
          return "NOT|";
      }
      //finally
      //{
          connection.disconnect();
          rd = null;
          connection = null;
          String decodedRet = "";
        try {
            decodedRet = URLDecoder.decode(sb.toString(), "ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(aspPages.class.getName()).log(Level.SEVERE, null, ex);
        }
          return decodedRet;
    }
}
