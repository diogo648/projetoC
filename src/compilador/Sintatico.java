/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import Excecoes.ExcecaoSemantico;
import java.util.ArrayList;

/**
 *
 * @author Diogo
 */
public class Sintatico{
    
    private Token tok;
    private Lexico lex;
    private Semantico sem = new Semantico();
    private GeracaoCodigo geracao = new GeracaoCodigo();
    
    //Responsável por receber os lexemas das expressões
    ArrayList<String> listaExpressao = new ArrayList<> ();
    ArrayList<String> listaExpressaoSaida = new ArrayList<> ();
    
    //Responsável por contar as variáveis declaradas no programa
    int contaVar=0;
    int endInicial=0;
    
    //Responsável por controlar a identificação dos labels
    int controlaLabel=1;
    
    //Variável que receberá o novo valor na atribuição
    String varAtrib;
    
    //Resposável por indicar se a expressão está integra ou não
    String tipoFinal;
        
        public Sintatico(String caminhoArquivo) throws Exception{
          
          lex = new Lexico(caminhoArquivo);
          
          //Lê o próximo Token
          tok = lex.retornaToken();
          
          if("sprograma".equals(tok.getSimbolo())){
              
              //Adiciona no arquivo de saída o comando 'START'
              geracao.addComando("START","","");
            
              tok = lex.retornaToken();
              
              if("sidentificador".equals(tok.getSimbolo())){
                  
                  sem.insereTabela(tok.getLexema(), "nomedeprograma", true);
            
                  //Lê o próximo Token
                  tok = lex.retornaToken();
                  
                  if("sponto_virgula".equals(tok.getSimbolo())){
                      
                      analisaBloco();
                      
                      //Desaloca as variáveis do procedimento
                      int variaveisRetiradas = sem.removeTabela();
                      
                      //Adiciona no código de saída o comando 'DALLOC'
                      geracao.addComando("DALLOC",Integer.toString(variaveisRetiradas),"");
                      
                      if("sponto".equals(tok.getSimbolo())){
                          
                          //Adiciona no código de saída o comando 'HLT'
                          geracao.addComando("HLT","","");
    
                      }
                      
                      else{
               
                            throw new Exception("Espera-se '.' (ponto final) no fim do programa." + "Linha:" + lex.retornaLinhaErro());
                  
                      }
                  }
                  
                  else{
                  
                        throw new Exception("Espera-se ';' (ponto e vírgula)." + "Linha:" + lex.retornaLinhaErro());
                     
                  }
              
              }
             
              else{
              
                  throw new Exception("Espera-se um identificador após a palavra programa." + "Linha:" + lex.retornaLinhaErro());
                  
              }
           }
          
          else{
             
              throw new Exception("Espera-se a palavra 'programa' no início do código." + "Linha:" + lex.retornaLinhaErro());
           
          }
           
        }
  

    private void analisaBloco() throws Exception{
        
        tok = lex.retornaToken();
        analisaEtVariaveis();
        analisaSubrotinas();
        analisaComandos();
      
    }

    private void analisaEtVariaveis() throws Exception{
    
        if("svar".equals(tok.getSimbolo())){
            
            tok = lex.retornaToken();
            
            if("sidentificador".equals(tok.getSimbolo())){
                
                if(tok == null){
                    throw new Exception("Erro" + "Linha:" + lex.retornaLinhaErro());
                
                }
                
                while("sidentificador".equals(tok.getSimbolo())){
                
                    analisaVariaveis();
                    
                    if("sponto_virgula".equals(tok.getSimbolo())){
              
                        tok = lex.retornaToken();
                    
                    }
                    
                    else{
                    
                        throw new Exception("Espera-se ';' (ponto e vírgula, após declarar variáveis." + "Linha:" + lex.retornaLinhaErro());
                    }
                }
                
            }
            
            else{
                
                throw new Exception("Espera-se um identificador para declarar uma variável" + "Linha:" + lex.retornaLinhaErro());
            }
         
        }
            
    
    }
    
    
    private void analisaVariaveis() throws Exception{
        
        do{
            if("sidentificador".equals(tok.getSimbolo())){
                
                //Chama a o método para verificar se há variáveis duplicadas, caso negativo continua com o fluxo
                if(!sem.pesquisaDuplicVar(tok.getLexema())){
                    
                    //Insere a variável na tabela de símbolos
                    sem.insereTabela(tok.getLexema(), "variavel", false);
                    
                    //Conta o número de variáveis declaradas
                    contaVar++;
                    
                    //Lê o próximo Token
                    tok = lex.retornaToken();
            
                    if("svirgula".equals(tok.getSimbolo())|| "sdois_pontos".equals(tok.getSimbolo())){
            
                        if("svirgula".equals(tok.getSimbolo())){
                            
                            //Lê o próximo Token
                            tok = lex.retornaToken();
                    
                            if("sdois_pontos".equals(tok.getSimbolo())){
                            
                                throw new Exception("Caracter ':' (dois pontos) inválido." + "Linha:" + lex.retornaLinhaErro());
                       
                            }
                        }
                    }
                }
                
                else{
                    
                    throw new Exception("Varíavel" + " '" + tok.getLexema() + "' " + "declarada mais de uma vez no escopo." + "Linha:" + lex.retornaLinhaErro());
                }
            }
            
            else{
            
                throw new Exception("Espera-se um identificador para declarar uma variável" + "Linha:" + lex.retornaLinhaErro());
            }
    
    
        }while(!"sdois_pontos".equals(tok.getSimbolo()));
        
        geracao.addComando("ALLOC",Integer.toString(endInicial),Integer.toString(contaVar));
        
        //Coloca na variável endInicial o novo endereço a partir da variável contaVar 
        endInicial = endInicial + contaVar;
        
        //Limpa a variável contaVar
        contaVar=0;
       
        //Lê o próximo Token
        tok = lex.retornaToken();
        
        analisaTipo();
    }
    
    
    private void analisaTipo() throws Exception{ 
        
        if(!"sinteiro".equals(tok.getSimbolo()) && !"sbooleano".equals(tok.getSimbolo())){
            
            throw new Exception("Espera-se o(s) tipo(s) 'inteiro' e/ou 'booleano' para declarar uma variável." + "Linha:" + lex.retornaLinhaErro());
        }
    
        //Insere o tipo da variável na tabela de símbolos
        sem.colocaTipoVariavel(tok.getLexema());
       
        //Lê o próximo Token
        tok = lex.retornaToken();
    
    }
    
    private void analisaComandos() throws Exception{
    
        if("sinicio".equals(tok.getSimbolo())){
            

            //Lê o próximo Token
            tok = lex.retornaToken();
            
            analisaComandoSimples();
            
            while(!"sfim".equals(tok.getSimbolo())){
                
                if("sponto_virgula".equals(tok.getSimbolo())){
                
                    tok = lex.retornaToken();
                    
                    if(!"sfim".equals(tok.getSimbolo())){
                        
                        analisaComandoSimples();
                    
                    }
                   
                }
                
                else if(!"sfim".equals(tok.getSimbolo())){
                    
                        throw new Exception("Espera-se ';' (ponto e vírgula)." + "Linha:" + lex.retornaLinhaErro());
                }
            
            }
            
             tok = lex.retornaToken();
               
        }
        
        else{
        
            throw new Exception("Espera-se o identificador reservado 'inicio'." + "Linha:" + lex.retornaLinhaErro());
        }
    
    }
    
    
    
    private void analisaComandoSimples() throws Exception{
        
        if("sidentificador".equals(tok.getSimbolo())){
        
            analisaAtribChprocedimento();
        }
        
        else{
    
            if("sse".equals(tok.getSimbolo())){
            
                analisaSe();
            }
            
            else if("senquanto".equals(tok.getSimbolo())){
            
                analisaEnquanto();
            }
            
            else if("sleia".equals(tok.getSimbolo())){
            
                analisaLeia();
            }
            
            else if("sescreva".equals(tok.getSimbolo())){
            
                analisaEscreva();
            }
            
            else{
                
                analisaComandos();
            }
          
           
        }
    }
   
    private void analisaAtribChprocedimento() throws Exception{
        
        //Salva a variável que receberá a atribuição
        varAtrib = tok.getLexema();
        
        //Lê o próximo Token
        tok = lex.retornaToken();
        
        if("satribuicao".equals(tok.getSimbolo())){
        
            analisaAtribuicao();
        }
        
        else{
        
            analisaChamadaProcedimento();
        }
        
    }
    
    private void analisaLeia() throws Exception{
        
        //Adiciona no arquivo de saída o comando 'RD'
        geracao.addComando("RD","","");
        
        //Lê o próximo Token
        tok = lex.retornaToken();
        
        if("sabre_parenteses".equals(tok.getSimbolo())){
            
            //Lê o próximo Token
            tok = lex.retornaToken();
            
            if("sidentificador".equals(tok.getSimbolo())){
                
                //Consulta na tabela de símbolos o endereço da variável
                int endMem = sem.consultaTabela(tok.getLexema());
                
                //Adiciona no arquivo de saída o comando 'STR' com o seu valor da posição de memória
                geracao.addComando("STR",Integer.toString(endMem),"");
                
                //Lê o próximo Token
                tok = lex.retornaToken();
                
                if("sfecha_parenteses".equals(tok.getSimbolo())){
                    
                    //Lê o próximo Token
                    tok = lex.retornaToken();

                }
                
                     
                else{
                
                    throw new Exception("Espera-se ')' (fecha parênteses)." + "Linha:" + lex.retornaLinhaErro());
                }
            }
            
            else{
            
                throw new Exception("Espera-se um identificador." + "Linha:" + lex.retornaLinhaErro());
            }
            
        }
        
        else{
            
            throw new Exception("Espera-se '(' (abre parênteses)." + "Linha:" + lex.retornaLinhaErro());
        }
    
    }
    
    
    private void analisaEscreva() throws Exception{
        
        //Lê o próximo Token
        tok = lex.retornaToken();
        
        if("sabre_parenteses".equals(tok.getSimbolo())){
            
            //Lê o próximo Token
            tok = lex.retornaToken();
            
            if("sidentificador".equals(tok.getSimbolo())){
                
                //Consulta na tabela de símbolos o endereço da variável
                int endMem = sem.consultaTabela(tok.getLexema());
                
                //Adiciona no arquivo de saída o comando 'LDV' com o seu valor da posição de memória
                geracao.addComando("LDV",Integer.toString(endMem),"");
                
                //Adiciona no arquivo de saída o comando 'PRN'
                geracao.addComando("PRN","","");
                
                //Lê o próximo Token
                tok = lex.retornaToken();
                
                if("sfecha_parenteses".equals(tok.getSimbolo())){
                    
                    //Lê o próximo Token
                    tok = lex.retornaToken();
                
                }
                
                else{
                
                    throw new Exception("Espera-se ')' (fecha parênteses)." + "Linha:" + lex.retornaLinhaErro());
                }
            
            }
            
            else{
            
                throw new Exception("Espera-se um identificador." + "Linha:" + lex.retornaLinhaErro());
            }
        }
        
        else{
        
            throw new Exception("Espera-se '(' (abre parênteses)." + "Linha:" + lex.retornaLinhaErro());
        }
        
    }
    
    
    private void analisaEnquanto() throws Exception{
        
        //Retira os itens adicionados no arrayList
        listaExpressao.clear();
        
        //Lê o próximo Token
        tok = lex.retornaToken();
        
        analisaExpressao();
        
       //Manda a expressao para ser avaliada no posFixa e salva o resulta em um arrayList
       listaExpressaoSaida = sem.posFixa(listaExpressao);
       
       //Método para analisar os comandos que passaram pelo método posFixa
       avaliaListaExpressao(listaExpressaoSaida);
       
       //Adiciona 1 na variável controla label
       controlaLabel++;
            
       //Adiciona no arquivo de saída o comando 'JMP' com seu label
       geracao.addComando("JMP","L" + controlaLabel,"");
       
        if("sfaca".equals(tok.getSimbolo())){
            
            //Lê o próximo Token
            tok = lex.retornaToken();
            
            analisaComandoSimples();
        
        }
        
        else{
        
            throw new Exception("Espera-se a palavra reservada 'faca'." + "Linha:" + lex.retornaLinhaErro());
        }
    
    }
    
    
    private void analisaSe() throws Exception{
    
        //Retira os itens adicionados no arrayList
        listaExpressao.clear();
  
        //Lê o próximo Token
        tok = lex.retornaToken();
        
        analisaExpressao();
        
        //Manda a expressão para ser verificada (se as variáveis existem e se seus tipos são compatíveis)
        sem.validaExpresao(listaExpressao);
       
        
       //Manda a expressao para ser avaliada no posFixa e salva o resulta em um arrayList
       listaExpressaoSaida = sem.posFixa(listaExpressao);
       
       //Método para analisar os comandos que passaram pelo método posFixa
       avaliaListaExpressao(listaExpressaoSaida);
       
        if("sentao".equals(tok.getSimbolo())){
            
            
           //Adiciona no arquivo de saída o comando 'JMPF' com o labels
           geracao.addComando("JMPF","L"+controlaLabel,"");
           
           //Incrementa 1 na variável controlaLabel
           controlaLabel++;
           
            //Lê o próximo Token
            tok = lex.retornaToken();
            
            analisaComandoSimples();
            
            //Adiciona no arquivo de saída o comando 'JMP' com o label
           geracao.addComando("JMP","L"+controlaLabel,"");
           
            if("ssenao".equals(tok.getSimbolo())){
                
                //Adiciona no arquivo de saída o comando 'JMP' com seu label
                geracao.addComando("L"+(controlaLabel-1),"NULL","");
                
                //Lê o próximo Token
                tok = lex.retornaToken();
                
                analisaComandoSimples();
            
            }
         
           //Adiciona no arquivo de saída o comando 'JMP' com seu label
           geracao.addComando("L"+controlaLabel,"NULL","");
          
        }
        
        else{
        
            throw new Exception("Espera-se a palavra reservada 'entao'." + "Linha:" + lex.retornaLinhaErro());
        }
    }
    
    private void analisaSubrotinas() throws Exception{
      
        while("sprocedimento".equals(tok.getSimbolo()) || "sfuncao".equals(tok.getSimbolo()) ){
        
            if("sprocedimento".equals(tok.getSimbolo())){
            
                analisaDeclaracaoProcedimento();
            }
            
            else{
                
                analisaDeclaracaoFuncao();
                
            }
            
            if("sponto_virgula".equals(tok.getSimbolo())){
            
                //Lê o próximo Token
                tok = lex.retornaToken();
 
            }
            
            else{
            
                throw new Exception("Espera-se ';' (ponto e vírgula)" + "Linha:" + lex.retornaLinhaErro());
            }
          
        }
    
    }
    
    
    private void analisaDeclaracaoProcedimento() throws Exception{
        
        //Lê o próximo Token
        tok = lex.retornaToken();
        
        if("sidentificador".equals(tok.getSimbolo())){
            
            if(sem.pesquisaDeclProc(tok.getLexema()) == false){
            
            //Insere o procedimento na tabela de símbolos
            sem.insereTabela(tok.getLexema(),"procedimento",true);
            
            //Adiciona no arquivo de saída o comando 'JMP' com seu label
            geracao.addComando("JMP","L" + controlaLabel,"");
            
            //Adiciona 1 na variável controla label
            controlaLabel++;
            
            //Adiciona no arquivo de saída o comando 'JMP' com seu label
            geracao.addComando("L"+controlaLabel,"NULL","");
            
            //Lê o próximo Token
            tok = lex.retornaToken();
            
                if("sponto_virgula".equals(tok.getSimbolo())){
                
                    analisaBloco();
                    
                    //Desaloca as variáveis do procedimento
                    int variaveisRetiradas = 30;//sem.removeTabela("procedimento");
                      
                    //Adiciona no código de saída o comando 'DALLOC'
                    geracao.addComando("DALLOC",Integer.toString(variaveisRetiradas),"");
                    
                    //Adiciona no código de saída o comando 'DALLOC'
                    geracao.addComando("RETURN","","");
                    
                    
                }
            
                else{
            
                    throw new Exception("Espera-se ';' (ponto e vírgula)" + "Linha:" + lex.retornaLinhaErro());
                }
            }        
        }   
        
        else{
        
            throw new Exception("Espera-se um identificador para o procedimento." + "Linha:" + lex.retornaLinhaErro());
        }
    
    }

    
  private void analisaDeclaracaoFuncao() throws Exception{
      
      //Lê o próximo Token
      tok = lex.retornaToken();
      
      if("sidentificador".equals(tok.getSimbolo())){
          
          sem.insereTabela(tok.getLexema(), "funcao", true);
          
          //Adiciona no arquivo de saída o comando 'JMP' com seu label
          geracao.addComando("JMP","L" + controlaLabel,"");
            
          //Adiciona 1 na variável controla label
          controlaLabel++;
            
          //Adiciona no arquivo de saída o comando 'JMP' com seu label
          geracao.addComando("L"+controlaLabel,"NULL","");
          
          //Lê o próximo Token
          tok = lex.retornaToken();
          
          if("sdois_pontos".equals(tok.getSimbolo())){
              
              //Lê o próximo Token
              tok = lex.retornaToken();
              
              if("sinteiro".equals(tok.getSimbolo()) || "sinteiro".equals(tok.getSimbolo())){
                  
                  //Lê o próximo Token
                  tok = lex.retornaToken();
                  
                  if("sponto_virgula".equals(tok.getSimbolo())){
                      
                      analisaBloco();
                  
                  }
                  
                  else{
                  
                      System.out.print("Espera-se ';' (ponto e vírgula)." + "Linha:" + lex.retornaLinhaErro());
                  }
              
              }
              
              else{
                  
                  throw new Exception("Espera-se o(s) tipo(s) 'inteiro' e/ou 'booleano' para declarar uma variável." + "Linha:" + lex.retornaLinhaErro());
              }
          }
          
          else{
          
              System.out.print("Espera-se ':' (dois pontos)." + "Linha:" + lex.retornaLinhaErro());
          }
          
      }
      
      else{
      
          System.out.print("Espera-se um identificador para a função." + "Linha:" + lex.retornaLinhaErro());
      }
      
  }
     
  
  private void analisaExpressao() throws Exception{
  
      
      analisaExpressaoSimples();
      
      if("smaior".equals(tok.getSimbolo()) || "smaiorig".equals(tok.getSimbolo()) || 
         "sig".equals(tok.getSimbolo()) || "smenor".equals(tok.getSimbolo()) ||
          "smenorig".equals(tok.getSimbolo()) || "sdif".equals(tok.getSimbolo())){
     
          //Insere o lexema na lista
          listaExpressao.add(tok.getLexema());
          
          //Lê o próximo Token  
          tok = lex.retornaToken();
          
          analisaExpressaoSimples();
          
      
      }
    
  }
  
  
  private void analisaExpressaoSimples() throws Exception{
  
      if("smais".equals(tok.getSimbolo()) || "smenos".equals(tok.getSimbolo())){
          
          //Insere o lexema na pilha
          listaExpressao.add(tok.getLexema());
          
          //Lê o próximo Token
          tok = lex.retornaToken();

      }   
      
        analisaTermo();
      
      while("smais".equals(tok.getSimbolo()) || "smenos".equals(tok.getSimbolo()) || "sou".equals(tok.getSimbolo())){
          
           //Insere o lexema na pilha
           listaExpressao.add(tok.getLexema());
          
           //Lê o próximo Token
           tok = lex.retornaToken();
           
           analisaTermo();
          
       }
        
      
  }
  
  private void analisaTermo() throws Exception{
      
      analisaFator();
      
      while("smult".equals(tok.getSimbolo()) || "sdiv".equals(tok.getSimbolo()) || "se".equals(tok.getSimbolo())){
      
          //Insere o lexema na pilha
          listaExpressao.add(tok.getLexema());
          
          //Lê o próximo Token
          tok = lex.retornaToken();
          
          analisaFator();
      }
  
  }
  
  private void analisaFator() throws Exception{
  
      if("sidentificador".equals(tok.getSimbolo())){
      
          analisaChamadaFuncao();
          
          //Insere o lexema na lista
          listaExpressao.add(tok.getLexema());
          
          //Lê o próximo Token
          tok = lex.retornaToken();
      }
      
      else if("snumero".equals(tok.getSimbolo())){
      
          //Insere o lexema na lista
          listaExpressao.add(tok.getLexema());
          
          //Lê o próximo Token
          tok = lex.retornaToken();
      }
      
      else if("snao".equals(tok.getSimbolo())){
         
          //Insere o lexema na lista
          listaExpressao.add(tok.getLexema());
          
          //Lê o próximo Token
          tok = lex.retornaToken();
          
          analisaFator();
      }
      
      else if("sabre_parenteses".equals(tok.getSimbolo())){
           
           //Insere o lexema na lista
           listaExpressao.add(tok.getLexema());
          
           //Lê o próximo Token
           tok = lex.retornaToken();
              
           analisaExpressao();
              
              if("sfecha_parenteses".equals(tok.getSimbolo())){
              
                  //Insere o lexema na lista
                  listaExpressao.add(tok.getLexema());
                  
                  //Lê o próximo Token
                  tok = lex.retornaToken();
              }
              
              else{
              
                  throw new Exception("Espera-se ')' (fecha parênteses)." + "Linha:" + lex.retornaLinhaErro());
              }
       }  
           
      else if("sverdadeiro".equals(tok.getSimbolo()) || "sfalso".equals(tok.getSimbolo()) ){
            
            //Insere o lexema na lista
            listaExpressao.add(tok.getLexema());
            
            //Lê o próximo Token
            tok = lex.retornaToken();
        
       } 
      
      else{
              
            throw new Exception("Caracter inválido." + tok.getLexema() + "Linha:" + lex.retornaLinhaErro());
                  
      }
           
  }
  
  private void analisaChamadaProcedimento() throws Exception{
  
      
  }
  
  
  private void analisaChamadaFuncao() throws Exception{
  
      
  }
  
  private void analisaAtribuicao() throws Exception{
       
       //Retira os itens adicionados no arrayList
       listaExpressao.clear();

       //Lê o próximo Token
       tok = lex.retornaToken();
        
       analisaExpressao();
       
       
       //Manda a expressao para ser avaliada no posFixa e salva o resulta em outro arrayList
       listaExpressaoSaida = sem.posFixa(listaExpressao);
       
       //Método para analisar os comandos que passaram pelo método posFixa
       avaliaListaExpressao(listaExpressaoSaida);
          
       //Valida a expressao
       tipoFinal = sem.validaExpresao(listaExpressao);
       
       //Consulta o tipo
       String tipo = sem.consultaTipo(varAtrib);
       
       if(!tipoFinal.equals(tipo)){
       
           throw new ExcecaoSemantico("Tipos incompatíveis de variáveis!" + " Linha:" + lex.retornaLinhaErro() );
           
       }
       
          
       if("funcao".equals(tipo)){
           
           //Consulta na tabela de símbolos o endereço da variável
            int endMem = sem.consultaTabela(listaExpressaoSaida.get(0));
            
            //Adiciona no arquivo de saída o comando 'RETURNF' com os seus atributos
            geracao.addComando("RETURNF",Integer.toString(endMem),"1");
            
            //Adiciona no arquivo de saída o label da função
            geracao.addComando("L"+(controlaLabel-1),"NULL","");
            
       }
       
       else{
       
            //Consulta na tabela de símbolos o endereço da variável
            int endMem = sem.consultaTabela(varAtrib);

            //Adiciona no arquivo de saída o comando 'STR' com o valor da posição de memória
            geracao.addComando("STR",Integer.toString(endMem),"");

  
       
       }
  }
  
  //Método responsável por analisar a lista de expressão que passou pelo método posFixa e insere os comandos na geração de código
  private void avaliaListaExpressao(ArrayList<String> listaExpressao) throws Exception{
  
   
      //Resposável por guardar o endereço de memória da variável
      int endMem;
      
      String tipo;
      
      for(int i=0; i<listaExpressao.size(); i++){
        
        if("+".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'ADD'
            geracao.addComando("ADD","","");
        }
        
        else if("-".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'SUB'
            geracao.addComando("SUB","","");
        }
        
        else if("*".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'MULT'
            geracao.addComando("MULT","","");
        }
        
        else if("div".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'DIVI'
            geracao.addComando("DIVI","","");
        }
    
        else if(">".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'CMA'
            geracao.addComando("CMA","","");
        }
          
        else if("<".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'CME'
            geracao.addComando("CME","","");
        }
        
        else if("!=".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'CDIF'
            geracao.addComando("CDIF","","");
        }
        
        else if("<=".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'CMEQ'
            geracao.addComando("CMEQ","","");
        }
        
        else if(">=".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'CMEQ'
            geracao.addComando("CMAQ","","");
        }
         
        else if("e".equals(listaExpressao.get(i))){
            //Adiciona no arquivo de saída o comando 'AND'
            geracao.addComando("AND","","");
        }
         
        else if(Character.isLetter(listaExpressao.get(i).charAt(0))){
            
            tipo = sem.consultaTipo(listaExpressao.get(i));
            
            if("funcao".equals(tipo)){
                
                 //Adiciona no arquivo de saída o comando 'CALL' com o seu label
                geracao.addComando("CALL","L"+controlaLabel,"");    
            }
            
            else{
                //Consulta a posição de mémoria a variável na tabela de símbolos
                endMem = sem.consultaTabela(listaExpressao.get(i));

                //Adiciona no arquivo de saída o comando 'LDV' com o valor
                geracao.addComando("LDV",Integer.toString(endMem),"");
           }
        } 
         
        else if(Character.isDigit(listaExpressao.get(i).charAt(0))){
            //Adiciona no arquivo de saída o comando 'LDC' com a constante 
            geracao.addComando("LDC",listaExpressao.get(i),"");
        }
         
      
      }
      
  }
  
  
  
}
