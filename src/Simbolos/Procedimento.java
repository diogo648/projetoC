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
public class Procedimento extends Simbolo {
    
    String label;
    
    public Procedimento(){
    }
    
    public Procedimento(String lexema, String tipo, boolean escopo, String label){
        
        this.lexema = lexema;
        this.tipo = tipo;
        this.escopo = escopo;
        this.label = label;
        
   
    }
    
     public String getLabel(){
    
        return label;
    }
    
    
    /*
    @Override   
    public String toString() {
        
        return ("Lexema:"          + " " + this.lexema + "\n" +
                "Tipo:"            + " " + this.tipo);
                   
   }
    */
}
