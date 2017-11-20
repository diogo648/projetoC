/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Diogo
 */
public class Lexico {
    
    private char caracter; //Declaração da variável caracter
    private BufferedReader in;
    private FileReader arquivo;
    public int linhaErro;
    
    Token tok = new Token();
 
            
        public Lexico(String caminhoArquivo) throws Exception{
     
            try{
            
                arquivo = new FileReader(caminhoArquivo);
                in = new BufferedReader(arquivo);
            
                caracter = (char)in.read(); //Ler Caracter
                
                linhaErro = 0;
                
                while(caracter == 10){ // Conta as linhas se no começo do arquivo se tiver varias linhas em branco
              
                   linhaErro++;
                  caracter = (char) in.read();
                   
                }
                
              } catch(IOException e){
        
                System.err.println("Error: " + e);
             }
        }    
        
        
        public Token retornaToken() throws Exception{
            
          if((int)caracter != -1 && caracter!='\uffff'){ //Ler caracter até o fim do arquivo \uffff está sendo utilizado porque ao fazer cast de int in.read() para char se perde metade do conteudo em binario, e o \uffff indica o fim
           // Valor 10 Line Feed e 13 Carriage Return 8 backspace da tabela asc
              while((caracter == '{' || caracter == 8 || caracter == 9 || caracter == 32 || caracter == 10 || caracter == 13) && ((int) caracter != -1 && caracter!='\uffff')){
                  
                  if(caracter == 10){
                  
                      linhaErro++;
                      caracter = (char) in.read();
                  }
                  
                  if(caracter == '{'){
                    
                        while(caracter != '}' && ((int)caracter != -1 && caracter!='\uffff')){
                            
                             if(caracter == 10){
                             
                                 linhaErro++;
                                  caracter = (char)in.read();
                             }
                           
                             caracter = (char)in.read(); //Ler caracter
                                
                        }
                   }
                        
                  if((int)caracter == 65535){ //modififcar o valor de 65535
                           
                        throw new Exception("Comentário não está sendo fechado! Linha:" + linhaErro);
                         
                
                   }
                       
                   caracter = (char)in.read(); //Ler caracter
                        
                    while(caracter == 10 || caracter == 13 || caracter == 8 || caracter == 9 || caracter == 32 && ((int)caracter != -1 && caracter!='\uffff')){
                            
                       if(caracter == 10){
                             
                                 linhaErro++;
                                 
                        }
                       
                       caracter = (char)in.read(); //Ler caracter
                             
                    }
                           
                }
                
                    if(((int) caracter) != -1 && caracter!='\uffff'){
                 
                           pegaToken();
                           return tok;
                     
                    }
                } 
           
          return null;
        }

    
    private void pegaToken() throws Exception{
        
        if(Character.isDigit(caracter)){
        
            trataDigito();
        }
        
        else if(Character.isLetter(caracter)){
        
            trataIdentificadorPalavraReservada();
        }
        
        else if(caracter == ':'){
            
            trataAtribuicao();
        }
        
        else if(caracter == '+' || caracter == '-' || caracter == '*' ){
            
            trataOpAritmetico();
        }
        
        else if(caracter == '<' || caracter == '>' || caracter == '=' || caracter == '!' ){
            
            trataOpRelacional();
        }
        
        else if(caracter == ';' || caracter == ',' || caracter == '(' || caracter == ')' || caracter == '.'){
            
            trataPontuacao();
        }
        
        else{
              
                throw new Exception("Caracter " + caracter + " Inválido. Linha:" + linhaErro);
         
        }
        
    }  
    
    private void trataDigito() throws Exception{
        
        String num;
        
        num = String.valueOf(caracter);
        caracter = (char)in.read(); //Ler caracter
        
        while(Character.isDigit(caracter)){
        
                num+= caracter; 
                caracter = (char)in.read(); //Ler caracter
        }
       
       
        tok.setSimbolo("snumero");
        tok.setLexema(num);
        
    
    }
    
    private void trataIdentificadorPalavraReservada() throws Exception{
        
        String id;
       
        id = String.valueOf(caracter);
       
        caracter = (char)in.read(); //Ler caracter
        
        while(Character.isLetterOrDigit(caracter) || "_".equals(caracter)){
        
            id = id + caracter;
            caracter = (char)in.read(); //Ler caracter;
        }
        
           tok.setLexema(id);
        
        if("programa".equals(id)){
        
            tok.setSimbolo("sprograma");
        }
        
        else if("se".equals(id)){
        
            tok.setSimbolo("sse");
        }
        
        else if("entao".equals(id)){
        
            tok.setSimbolo("sentao");
        }
        
          else if("senao".equals(id)){
        
            tok.setSimbolo("ssenao");
        }
        
        
        else if("enquanto".equals(id)){
        
            tok.setSimbolo("senquanto");
        }
        
        else if("faca".equals(id)){
        
            tok.setSimbolo("sfaca");
        }
        
        else if("inicio".equals(id)){
        
            tok.setSimbolo("sinicio");
        }
        
        else if("fim".equals(id)){
        
            tok.setSimbolo("sfim");
        }
        
        else if("escreva".equals(id)){
        
            tok.setSimbolo("sescreva");
        }
        
        else if("leia".equals(id)){
        
            tok.setSimbolo("sleia");
        }
        
        else if("var".equals(id)){
        
            tok.setSimbolo("svar");
        }
        
        else if("inteiro".equals(id)){
        
            tok.setSimbolo("sinteiro");
        }
        
        else if("booleano".equals(id)){
        
            tok.setSimbolo("sbooleano");
        }
        
        else if("verdadeiro".equals(id)){
        
            tok.setSimbolo("sverdadeiro");
        }
        
        else if("falso".equals(id)){
        
            tok.setSimbolo("sfalso");
        }
        
        else if("procedimento".equals(id)){
        
            tok.setSimbolo("sprocedimento");
        }
        
        else if("funcao".equals(id)){
        
            tok.setSimbolo("sfuncao");
        }
        
        else if("div".equals(id)){
        
            tok.setSimbolo("sdiv");
        }
        
        else if("e".equals(id)){
        
            tok.setSimbolo("se");
        }
        
        else if("ou".equals(id)){
        
            tok.setSimbolo("sou");
        }
        
        else if("nao".equals(id)){
        
            tok.setSimbolo("snao");
        }
        
        else{
        
           tok.setSimbolo("sidentificador");
        }
        
    }
    
    private void trataAtribuicao() throws Exception{
        
       String atribuicao;
       
       atribuicao = String.valueOf(caracter);
       
       caracter = (char)in.read(); //Ler caracter
      
       if(caracter == '='){
       
           atribuicao = atribuicao + caracter; 
           tok.setSimbolo("satribuicao");
           tok.setLexema(atribuicao);
           
           caracter = (char)in.read(); //Ler caracter;
       }
       
       else{
       
           tok.setSimbolo("sdois_pontos");
           tok.setLexema (atribuicao);
       }
      
       
    
    }
    
    private void trataOpAritmetico() throws Exception{
        
        String operadorAritmetico;
        
        operadorAritmetico = String.valueOf(caracter);
        
        caracter = (char)in.read(); //Ler caracter
        
        if("+".equals(operadorAritmetico)){
        
            tok.setSimbolo("smais");
            tok.setLexema(operadorAritmetico);
            
        }
        
        else if("-".equals(operadorAritmetico)){
        
            tok.setSimbolo("smenos");
            tok.setLexema(operadorAritmetico);
        }
        
        else if("*".equals(operadorAritmetico)){
        
            tok.setSimbolo("smult");
            tok.setLexema(operadorAritmetico);
        }
   
    }
    
    private void trataOpRelacional() throws Exception{
        
        String operadorRelacional;
        
        operadorRelacional = String.valueOf(caracter);
        
        caracter =(char)in.read();
        
        if("<".equals(operadorRelacional)){
            
            tok.setSimbolo("smenor");
            tok.setLexema(operadorRelacional);
            
            if(caracter == '='){
                
                operadorRelacional = operadorRelacional + caracter;
                tok.setSimbolo("smenorig");
                tok.setLexema(operadorRelacional);
                
                caracter =(char)in.read();
            }
        }
        
        if(">".equals(operadorRelacional)){
            
            tok.setSimbolo("smaior");
            tok.setLexema(operadorRelacional);
            
            if(caracter == '='){
                
                operadorRelacional = operadorRelacional + caracter;
                tok.setSimbolo("smaiorig");
                tok.setLexema(operadorRelacional); 
                
                caracter =(char)in.read();
            }
        
        }
        
        if("=".equals(operadorRelacional)){
            
            tok.setSimbolo("sig");
            tok.setLexema(operadorRelacional);
        
        }
        
        if("!".equals(operadorRelacional)){
            
            if(caracter == '='){
                
                operadorRelacional = operadorRelacional + caracter;
                tok.setSimbolo("sdif");
                tok.setLexema(operadorRelacional);
                
                caracter = (char)in.read();
            }
            
            else{
            
               throw new Exception("Caracter " + caracter + " Inválido. Linha:" + linhaErro);
            }
        }
    
    
    }
    
    private void trataPontuacao() throws Exception{
    
        String pontuacao;
       
        pontuacao = String.valueOf(caracter);
        caracter = (char) in.read(); //Ler caracter
        
       
        if(".".equals(pontuacao)){
        
            tok.setSimbolo("sponto");
            tok.setLexema(pontuacao);
            
        }
        
        else if (";".equals(pontuacao)){
        
            tok.setSimbolo("sponto_virgula");
            tok.setLexema(pontuacao);
            
        }
        
        else if(",".equals(pontuacao)){
        
            tok.setSimbolo("svirgula");
            tok.setLexema(pontuacao);
            
        }
        
        else if("(".equals(pontuacao)){
        
            tok.setSimbolo("sabre_parenteses");
            tok.setLexema(pontuacao);
            
        }
        
        else if(")".equals(pontuacao)){
        
            tok.setSimbolo("sfecha_parenteses");
            tok.setLexema(pontuacao);
            
        }
       
    
    }
    
    public int retornaLinhaErro(){

        return this.linhaErro;
            
    }
    
    
      
}
