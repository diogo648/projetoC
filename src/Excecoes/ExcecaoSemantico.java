/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excecoes;

/**
 *
 * @author Diogo
 */
public class ExcecaoSemantico extends Exception {
    
    public ExcecaoSemantico (String mensagemErro){
    
         super("Erro Semantico:" + mensagemErro);
    }
    
}
