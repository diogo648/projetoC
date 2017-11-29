/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simbolos;

/**
 *
 * @author Diogo
 */
public class Simbolo  {
    
    String lexema;
    String tipo;
    boolean escopo;
    
    public Simbolo(){}
    
    public Simbolo(String lexema, String tipo, boolean escopo){
        
        this.lexema = lexema;
        this.tipo = tipo;
        this.escopo = escopo;
   
    }
    
    public String getLexema(){
    
        return lexema;
    }
    
    public String getTipo(){
    
        return tipo;
    }
    
    public boolean getEscopo(){
    
        return escopo;
    }

    public void setEscopo(Boolean escopo){
    
        this.escopo = escopo;
    }
   
    
}
