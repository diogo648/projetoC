/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

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
        
        public Sintatico(String caminhoArquivo) throws Exception{
          
          lex = new Lexico(caminhoArquivo);
          
          tok = lex.retornaToken();
          
          if("sprograma".equals(tok.getSimbolo())){
              
              geracao.addComando("START");
            
              tok = lex.retornaToken();
              
              if("sidentificador".equals(tok.getSimbolo())){
                  
                  sem.insereTabela(tok.getLexema(), "nomedeprograma", true);
                  //sem.imprimirTabelaSimbolos();
                  
                  tok = lex.retornaToken();
                  
                  if("sponto_virgula".equals(tok.getSimbolo())){
                      
                      analisaBloco();
                      
                      if("sponto".equals(tok.getSimbolo())){
                      
                          geracao.addComando("HLT");
    
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
                    //sem.imprimirTabelaSimbolos();
                    
                    tok = lex.retornaToken();
            
                    if("svirgula".equals(tok.getSimbolo())|| "sdois_pontos".equals(tok.getSimbolo())){
            
                        if("svirgula".equals(tok.getSimbolo())){
                
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
        
        geracao.addComando("ALLOC");
        
        tok = lex.retornaToken();
        
        analisaTipo();
    }
    
    
    private void analisaTipo() throws Exception{ 
        
        if(!"sinteiro".equals(tok.getSimbolo()) && !"sbooleano".equals(tok.getSimbolo())){
            
            throw new Exception("Espera-se o(s) tipo(s) 'inteiro' e/ou 'booleano' para declarar uma variável." + "Linha:" + lex.retornaLinhaErro());
        }
    
        //Insere o tipo da variável na tabela de símbolos
       // sem.colocaTipoVariavel(tok.getLexema());
       
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
    
        tok = lex.retornaToken();
        
        if("satribuicao".equals(tok.getSimbolo())){
        
            analisaAtribuicao();
        }
        
        else{
        
            analisaChamadaProcedimento();
        }
        
    }
    
    private void analisaLeia() throws Exception{
    
        tok = lex.retornaToken();
        
        if("sabre_parenteses".equals(tok.getSimbolo())){
        
            tok = lex.retornaToken();
            
            if("sidentificador".equals(tok.getSimbolo())){
                
                tok = lex.retornaToken();
                
                if("sfecha_parenteses".equals(tok.getSimbolo())){
                
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
    
        tok = lex.retornaToken();
        
        if("sabre_parenteses".equals(tok.getSimbolo())){
            
            tok = lex.retornaToken();
            
            if("sidentificador".equals(tok.getSimbolo())){
                
                tok = lex.retornaToken();
                
                if("sfecha_parenteses".equals(tok.getSimbolo())){
                    
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
        
        //Lê o próximo Token
        tok = lex.retornaToken();
        
        analisaExpressao();
        
       //Manda a expressao para ser avaliada no posFixa e salva o resulta em outro arrayList
       listaExpressaoSaida = sem.posFixa(listaExpressao);
       
        if("sfaca".equals(tok.getSimbolo())){
            
            tok = lex.retornaToken();
            
            analisaComandoSimples();
        
        }
        
        else{
        
            throw new Exception("Espera-se a palavra reservada 'faca'." + "Linha:" + lex.retornaLinhaErro());
        }
    
    }
    
    
    private void analisaSe() throws Exception{
    
        tok = lex.retornaToken();
        
        analisaExpressao();
        
       //Manda a expressao para ser avaliada no posFixa e salva o resulta em outro arrayList
       listaExpressaoSaida = sem.posFixa(listaExpressao);
       
       //for(int i=0; i<listaExpressaoSaida.size(); i++){
       
            //System.out.println(listaExpressaoSaida.get(i));
           
       //}
       
        
        if("sentao".equals(tok.getSimbolo())){
        
            tok = lex.retornaToken();
            
            analisaComandoSimples();
            
            if("ssenao".equals(tok.getSimbolo())){
                
                tok = lex.retornaToken();
                
                analisaComandoSimples();
            
            }
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
            
                tok = lex.retornaToken();
 
            }
            
            else{
            
                throw new Exception("Espera-se ';' (ponto e vírgula)" + "Linha:" + lex.retornaLinhaErro());
            }
          
        }
    
    }
    
    
    private void analisaDeclaracaoProcedimento() throws Exception{
    
        tok = lex.retornaToken();
        
        if("sidentificador".equals(tok.getSimbolo())){
            
            if(sem.pesquisaDeclProc(tok.getLexema()) == false){
            
            //Insere o procedimento na tabela de símbolos
            sem.insereTabela(tok.getLexema(),"procedimento",true);
            //sem.imprimirTabelaSimbolos();
            
            tok = lex.retornaToken();
            
                if("sponto_virgula".equals(tok.getSimbolo())){
                
                    analisaBloco();
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
  
      tok = lex.retornaToken();
      
      if("sidentificador".equals(tok.getSimbolo())){
      
          tok = lex.retornaToken();
          
          if("sdois_pontos".equals(tok.getSimbolo())){
              
              tok = lex.retornaToken();
              
              if("sinteiro".equals(tok.getSimbolo()) || "sinteiro".equals(tok.getSimbolo())){
                  
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
  
       tok = lex.retornaToken();
       
       analisaExpressao();
  
  }
  
  
}
