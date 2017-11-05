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
public class Programa extends Simbolo  {
    
    
    Programa(){
    }
    
    public Programa(String lexema, String tipo, boolean escopo){
        
        this.lexema = lexema;
        this.tipo = tipo;
        this.escopo = escopo;
   
    }
    
    /*
    @Override   
    public String toString() {
        
        return ("Lexema:"          + " " + this.lexema + "\n" +
                "Tipo:"            + " " + this.tipo);
   }
    */
}
