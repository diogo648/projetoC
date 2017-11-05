/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Diogo
 */
public class Token {
    
    private String simbolo;
    private String lexema;
    
    public Token(){ //Construtor
    
    }
    
    public Token(String sim, String lex){
        
        this.simbolo = sim;
        this.lexema = lex;
   
    }
    
    public String getSimbolo(){
    
        return simbolo;
    }
    
    public String getLexema(){
    
        return lexema;
    }
    
    public void setSimbolo(String sim){
        
        this.simbolo = sim;
    
    }
    
    public void setLexema(String lex){
    
        this.lexema = lex;
    }
    
    
    @Override
    public String toString(){
    
        return lexema + "\n" + simbolo; 
    }
    
}
