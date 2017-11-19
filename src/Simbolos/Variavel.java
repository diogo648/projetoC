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
public class Variavel extends Simbolo  {
    
    int endMem;
    
   public Variavel(){
   }
    
   public Variavel(String lexema, String tipo, boolean escopo, int endMem){
        
        this.lexema = lexema;
        this.tipo = tipo;
        this.escopo = escopo;
        this.endMem = endMem;
   
    }
   
   
   public int getEndMem(){
   
       return endMem;
   }
   
       
    
    public void setTipo(String tipo){
    
        this.tipo = tipo;
    }
 
   
}
