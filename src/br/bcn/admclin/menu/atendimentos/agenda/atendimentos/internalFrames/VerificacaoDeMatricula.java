/*
 * Modelos 
 * Nenhuma - 0
 * IPE - 1
 * 
 */
package br.bcn.admclin.menu.atendimentos.agenda.atendimentos.internalFrames;

/**
 *
 * @author Theodoro
 */
public class VerificacaoDeMatricula {
    /**
     * Metodo apra verificar matricula do IPE
     * Numero da Matrícula do Beneficiário no IPE. 
        Validação : > 999999999999                                         1ª verificação > 999999999999
        FORMATO : OOPPPPPPPXDDY 
        Se OO igual a 63 então X é digito de controle de PPPPPPP 
       ( módulo 10 ) .                                                     2ª verificação o digito X
        Se OO igual a 19, 20, 41 ou 73 então X é digito de controle 
       de PPPPPPP ( módulo 11 ) . 
        Y e dígito de controle de OOPPPPPPPXDD                             3ª verificação o digito y
        ( módulo 10 )
        * 
        * 
        * 
        * 
        * 
        * 
        * Módulo 11
            Conforme o esquema abaixo, para calcular o primeiro dígito verificador, cada dígito do número, começando da direita para a esquerda (do dígito menos significativo para o dígito mais significativo) é multiplicado, na ordem, por 2, depois 3, depois 4 e assim sucessivamente, até o primeiro dígito do número. O somatório dessas multiplicações dividido por 11. O resto desta divisão (módulo 11) é subtraido da base (11), o resultado é o dígito verificador. Para calcular o próximo dígito, considera-se o dígito anterior como parte do número e efetua-se o mesmo processo. No exemplo, foi considerado o número 261533:
              +---+---+---+---+---+---+   +---+
              | 2 | 6 | 1 | 5 | 3 | 3 | - | 6 |
              +---+---+---+---+---+---+   +---+
                |   |   |   |   |   |
               x7  x6  x5  x4  x3  x2
                |   |   |   |   |   |
               =14 =36  =5 =20  =9  =6 soma = 90
                +---+---+---+---+---+-> = (90 / 11) = 8,1818 , resto 2 => DV = (11 - 2) = 9

            Considerando o número 2615336, onde o 6 é o dígito verificador e a validação é através do "módulo 11". Não seria considerado um número válido, pois o número correto é 2615339, onde 9 é o dígito verificador gerado pelo módulo 11.
            O CPF utiliza o módulo 11 duas vezes seguidas (conforme o exemplo), obtendo dois dígitos verificadores.
            Observação: para o código de barras, sempre que o resto for 0, 1 ou 10, deverá ser utilizado o dígito 1.
            Cálculos variantes poderiam ocorrer, tal como substituir por uma letra, quando o cálculo do dígito final der 10 ou outro número escolhido. Exemplos são os casos da utilização do dígito verificador pelo Banco do Brasil, quando o cálculo resulta em 10, o dígito verificador utilizado é o X e do Bradesco, que quando o cálculo resulta em 10, o digito verificador utilizado é o 0 (ou P para poupanças).
            * 
            * 
            * 
            * Módulo 10
                Conforme o esquema abaixo, cada dígito do número, começando da direita para a esquerda (menos significativo para o mais significativo) é multiplicado, na ordem, por 2, depois 1, depois 2, depois 1 e assim sucessivamente.
                Número exemplo: 261533-4
                  +---+---+---+---+---+---+   +---+
                  | 2 | 6 | 1 | 5 | 3 | 3 | - | 4 |
                  +---+---+---+---+---+---+   +---+
                    |   |   |   |   |   |
                   x1  x2  x1  x2  x1  x2
                    |   |   |   |   |   |
                   =2 =12  =1 =10  =3  =6
                    +---+---+---+---+---+-> = (16 / 10) = 1, resto 6 => DV = (10 - 6) = 4
                Em vez de ser feito o somatório das multiplicações, será feito o somatório dos dígitos das multiplicações (se uma multiplicação der 12, por exemplo, será somado 1 + 2 = 3).
                O somatório será dividido por 10 e se o resto (módulo 10) for diferente de zero, o dígito será 10 menos este valor.
     */
    public static boolean verificarModelo1(String mat) {
        try {
            if (Double.parseDouble(mat) > 999999999999.0) {

                int modulo = 0;
                int soma = 0;
                int dig = 0;
                int XX;
                int fator11 = 8;

                String O = mat.substring(0,2);
                String X = mat.substring(9,10);
                String L = mat.substring(12,13);

                if (Integer.parseInt(O) == 63)     modulo = 10;
                else if(Integer.parseInt(O) == 19) modulo = 11;
                else if(Integer.parseInt(O) == 20) modulo = 11;
                else if(Integer.parseInt(O) == 41) modulo = 11;
                else if(Integer.parseInt(O) == 73) modulo = 11;
                else modulo = 0;


                    if (modulo == 10) {
                        for (int i = 2;i < mat.length()-4; i++) {
                            dig = Integer.parseInt(mat.substring(i,i+1));
                            dig = dig * (i % 2 == 0 ? 2 : 1);
                            soma += dig > 9 ? dig - 9 : dig;
                        }
                        dig = (soma % 10) > 0 ? 10 - (soma % 10) : 0;
                        XX = Integer.parseInt(X);
                        if (dig != XX) return false;
                    }
                    if (modulo == 11) {
                        soma = 0;
                        for (int i = 2;i < mat.length()-4; i++) {
                            dig = Integer.parseInt(mat.substring(i,i+1));
                            soma += dig * fator11--;
                        }
                        dig = (11 - (soma % 11));
                        
                        XX = Integer.parseInt(X);
                        if (dig != XX) return false;
                    }
                    //Last Digit
                    soma = 0;
                    for (int i = 0;i < mat.length()-1; i++) {
                        dig = Integer.parseInt(mat.substring(i,i+1));
                        dig = dig * (i % 2 == 0 ? 1 : 2);
                        soma += dig > 9 ? dig - 9 : dig;
                    }
                    dig = (soma % 10) > 0 ? 10 - (soma % 10) : 0;
                    XX = Integer.parseInt(L);
                    if (dig != XX) return false;

                    return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
