/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import Simbolos.Funcao;
import Simbolos.Programa;
import Simbolos.Simbolo;
import Simbolos.Variavel;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Diogo
 */
public class Semantico {
    
 // ArrayList do tipo 'Simbolo'  
 private ArrayList<Simbolo> simbolos = new ArrayList();
 private Simbolo sim;
 //Variavél que conterá o endereço de memória das variáveis declaradas no programa
 private int endVar=0;

    
 public void insereTabela(String lexema, String tipo, boolean escopo) {
     
     
     if("nomedeprograma".equals(tipo)){
     
         simbolos.add(new Programa(lexema, tipo, escopo));
     
     }
     
     if("variavel".equals(tipo)){
     
        simbolos.add(new Variavel(lexema, tipo, escopo, endVar));
        //Incrementa 1 para os próximos
        endVar++;
     }
     
     if("funcao".equals(tipo)){
     
         String labelFunc="";
         simbolos.add(new Funcao(lexema, tipo, escopo, labelFunc));
     
     }
     
     
     if("procedimento".equals(tipo)){
     
         String labelProc="";
         simbolos.add(new Funcao(lexema, tipo, escopo, labelProc));
     
     }
 }
 
 //Como parâmetro utiliza o lexema para fazer a consulta na Tabela de Símbolos 
 public Simbolo consultaTabela(String lexema){
     
    for(int i=0; i<simbolos.size(); i++){
     
         if(sim.getLexema().equals(lexema)){
         
             return sim;
         }
     
     }
     return null;
    
 }
 
 //Percorre a tabela do final para o começo substituindo todos os campos tipo que possuem o valor variável pelo tipo agora localizado
 public void colocaTipoVariavel(String tipo){
 
     for(int i=0; i<simbolos.size(); i++){
        
        //Se for variável/funcao coloca o tipo passado no método
        if(("variavel".equals(sim.getTipo()) || "funcao".equals(sim.getTipo()))  && ("inteiro".equals(tipo) || "booleano".equals(tipo))){
         
             sim.setTipo(tipo);
         }
         
     }
 }
 
 //Verifica se há variáveis duplicadas
 public boolean pesquisaDuplicVar(String lexema){
     
     int i = simbolos.size()-1;
     
     //A condição de parada é o escopo
     while(simbolos.get(i).getEscopo() != true ){
    
         if(simbolos.get(i).getLexema().equals(lexema)){
    
            return true;
        }
       
         i--;
     }
     
     return false;
   
 }
 
 public boolean pesquisaDeclProc(String lexema){
 
    for(int i=0; i<simbolos.size(); i++){
        
        if(simbolos.get(i).getLexema().equals(lexema)){
    
            return true;
        }     
    }
   return false;
 }
 
 //Métodos criado para testes no compilador
 public void imprimirTabelaSimbolos(){
 
     for(int i=0; i<simbolos.size(); i++){
     
        System.out.println(simbolos);
     }
 }
 
 
 public ArrayList<String> posFixa(ArrayList<String> listaExpressao){
 
     
     Stack<String> pilha = new Stack<>();
     
     ArrayList<String> listaSaida = new ArrayList<> ();
   
     //Inicia a pilha com vazio
     pilha.push(" ");
     
     for(int i=0; i<listaExpressao.size(); i++){
     
            
         // Se o topo da pilha estiver vazia ou com '(', pode inserir qualquer coisa na pilha
         if((" ".equals(pilha.peek()) || "(".equals(pilha.peek())) &&
             ("*".equals(listaExpressao.get(i)) || "div".equals(listaExpressao.get(i)) || 
             "+".equals(listaExpressao.get(i)) || "-".equals(listaExpressao.get(i))   ||
             ">".equals(listaExpressao.get(i)) || "<".equals(listaExpressao.get(i))   ||
             ">=".equals(listaExpressao.get(i))|| "<=".equals(listaExpressao.get(i))  ||
             "=".equals(listaExpressao.get(i)) || "!=".equals(listaExpressao.get(i)) ||
             "(".equals(listaExpressao.get(i)))){
         
             
                pilha.push(listaExpressao.get(i));
             
         }     
         
          else if("+".equals(pilha.peek()) || "-".equals(pilha.peek()) &&
            ("(".equals(listaExpressao.get(i)) || "*".equals(listaExpressao.get(i)) || 
             "div".equals(listaExpressao.get(i)) || "+".equals(listaExpressao.get(i)) ||
             "-".equals(listaExpressao.get(i)))){
         
                pilha.push(listaExpressao.get(i));
             
         }
         
          //Se o topo da pilha estiver com '*' ou 'div'
         else if(("*".equals(pilha.peek()) || "div".equals(pilha.peek())) && 
             ("+".equals(listaExpressao.get(i)) || "-".equals(listaExpressao.get(i)) ||
             ">".equals(listaExpressao.get(i)) || "<".equals(listaExpressao.get(i)) ||
             ">=".equals(listaExpressao.get(i))|| "<=".equals(listaExpressao.get(i)) ||
             "=".equals(listaExpressao.get(i)) || "!=".equals(listaExpressao.get(i)))){
         
                listaSaida.add(pilha.peek()); //Adiciona o topo da pilha na lista de saida (+,-,>,<,>=,<=,=,!=)
                pilha.pop(); //Desempilha o '*' ou 'div' do topo da pilha
                pilha.push(listaExpressao.get(i)); //Adiciona o simbolo menos prioritário na pilha
             
         }
         
         else if("*".equals(pilha.peek()) && "(".equals(listaExpressao.get(i)) ){
             
             pilha.push(listaExpressao.get(i));
         }
         
         //Se encontrar um fecha patrenteses  
         else if(")".equals(listaExpressao.get(i))){
         
            while(!"(".equals(pilha.peek())){
                
                listaSaida.add(pilha.peek());
                pilha.pop();
            
            }
            //Para tirar o abre parenteses da pilha
            pilha.pop();
         } 
         
         else{
         
             listaSaida.add(listaExpressao.get(i));
         }
        
    }
     
     // Desmpilha a pilha até encontrar o vazio
     while(!" ".equals(pilha.peek())){
     
         listaSaida.add(pilha.peek());
         pilha.pop();
         
     }
 
     
    for(int z=0; z< listaSaida.size(); z++){
         
      System.out.println(listaSaida.get(z));
      
    }
    
    System.out.println("\n \n");
          
    return listaSaida;
  

    
 }

}
     


    
