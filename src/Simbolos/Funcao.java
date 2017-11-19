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
public class Funcao extends Simbolo {
    
    String label;
    
    public Funcao(){
    }
    
    public Funcao(String lexema, String tipo, boolean escopo, String label){
        
        this.lexema = lexema;
        this.tipo = tipo;
        this.label = label;
        this.escopo = escopo;
   
    }
    
    @Override
    public String getTipo(){
    
        return tipo;
    }
    
    
}
