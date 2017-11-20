/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import Excecoes.ExcecaoSemantico;
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
 
 //Variável do Lexico
 private Lexico lex;
 
 //Variável que contará o número de labels
 private int controlaLabel=0;
 
 //Tipo das expressões
 String tipoPrincipal;

 

    
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
         
         controlaLabel++;
         simbolos.add(new Funcao(lexema, tipo, escopo, "L"+controlaLabel));
     
         
     }
     
     
     if("procedimento".equals(tipo)){
     
         String labelProc="";
         simbolos.add(new Funcao(lexema, tipo, escopo, labelProc));
     
     }
 }
 
 //Como parâmetro utiliza o lexema para fazer a consulta na Tabela de Símbolos 
 public int consultaTabela(String lexema){
     

    for(int i=0; i<simbolos.size(); i++){
    
         Object obj = simbolos.get(i);
         
         if(lexema.equals(simbolos.get(i).getLexema())){
             
             Variavel var = (Variavel) obj;
             return var.getEndMem();
         
         }
    }
    
     return 0;
}
 
  //Como parâmetro utiliza o lexema para fazer a consulta na Tabela de Símbolos 
 public String consultaTipo(String lexema){
     

    for(int i=0; i<simbolos.size(); i++){
    
        
         
         if(lexema.equals(simbolos.get(i).getLexema())){
     
             return simbolos.get(i).getTipo();
         
         }
    }
    
     return null;
}
 
    /**
     *
     * @param tipo
     * @return
     */
    public int removeTabela(){
     
     int variaveisRetiradas =0;
     
     int i=simbolos.size()-1;
     
     while(!simbolos.get(i).getTipo().equals("nomedeprograma")){
         
         
         if(simbolos.get(i).getEscopo() == false){
         
                variaveisRetiradas++;
         }
         
          simbolos.remove(i);
          i--;
     }
     
     return variaveisRetiradas;
}
 
   
 
 //Percorre a tabela do final para o começo substituindo todos os campos tipo que possuem o valor variável pelo tipo agora localizado
 public void colocaTipoVariavel(String tipo){
 
     for(int i=0; i<simbolos.size(); i++){
        
        //Se for variável/funcao coloca o tipo passado no método
        if("variavel".equals(simbolos.get(i).getTipo())){
         
           Object obj = simbolos.get(i);
      
           Variavel var = (Variavel) obj;
              
            var.setTipo(tipo);
             
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
 
 
 //Consulta se variável está declarada e o seu tipo
 public String validaExpresao(ArrayList<String> listaExpressao) throws Exception{
     
     ArrayList<String> tipos = new ArrayList();
     
     Boolean entrou;
   
     
     for(int i=0; i<listaExpressao.size(); i++){
         
          entrou = false;
         
         if(Character.isLetter(listaExpressao.get(i).charAt(0))){
         
             //Teste
             if("div".equals(listaExpressao.get(i))){
                 
                //Não acontece nada, por o div é palavra reservada
             }
             
             else if("e".equals(listaExpressao.get(i)) || "ou".equals(listaExpressao.get(i)) ){
             
                 tipos.add(listaExpressao.get(i));
                 
             }
             
            
             else{
       
                //Começa pelo topo da tabela de símbolos
                int z =simbolos.size()-1; 

                 while(simbolos.get(z).getEscopo() == false){



                     if(listaExpressao.get(i).equals(simbolos.get(z).getLexema())){

                        entrou = true; 
                        Object obj = simbolos.get(z);

                        Variavel var = (Variavel) obj;

                        tipos.add(var.getTipoVar());
                     }

                     z--;

                 }

                 if(entrou == false){
                  
                     throw new ExcecaoSemantico("Variável " + "'" +listaExpressao.get(i) + "'" + " não declarada!" );
                      
                 }

             }
         }
             
         if(Character.isDigit(listaExpressao.get(i).charAt(0))){
         
             tipos.add("inteiro");
             
         }
         
     }
     
     for(int i=0; i<tipos.size(); i++ ){
     
      
         tipoPrincipal = tipos.get(0);
         
         if(!tipoPrincipal.equals(tipos.get(i))){
         
             throw new ExcecaoSemantico("O operador lógico " + "'" +tipos.get(i) + "'" + " deve ser utilizado com Booleano!" );
             
         }
        
     }
     
      return tipoPrincipal;
 }
 
 public ArrayList<String> posFixa(ArrayList<String> listaExpressao){
 
     
     Stack<String> pilha = new Stack<>();
     
     ArrayList<String> listaSaida = new ArrayList<> ();
   
     //Inicia a pilha com vazio
     pilha.push(" ");
     
     for(int i=0; i<listaExpressao.size(); i++){
     
            
         // Se o topo da pilha estiver vazia ou com '(', pode inserir qualquer coisa na pilha
         if((" ".equals(pilha.peek()) || "(".equals(pilha.peek()) || "e".equals(pilha.peek())) &&
             ("*".equals(listaExpressao.get(i)) || "div".equals(listaExpressao.get(i)) || 
             "+".equals(listaExpressao.get(i)) || "-".equals(listaExpressao.get(i))   ||
             ">".equals(listaExpressao.get(i)) || "<".equals(listaExpressao.get(i))   ||
             ">=".equals(listaExpressao.get(i))|| "<=".equals(listaExpressao.get(i))  ||
             "=".equals(listaExpressao.get(i)) || "!=".equals(listaExpressao.get(i)) ||
             "(".equals(listaExpressao.get(i)) || "e".equals(listaExpressao.get(i)))){
         
             
                pilha.push(listaExpressao.get(i));
             
         }     
         
          else if(("+".equals(pilha.peek()) || "-".equals(pilha.peek())) &&
            ("(".equals(listaExpressao.get(i)) || "*".equals(listaExpressao.get(i)) || 
             "div".equals(listaExpressao.get(i)) || "+".equals(listaExpressao.get(i)) ||
             "-".equals(listaExpressao.get(i)))){
         
                pilha.push(listaExpressao.get(i));
             
         }
          
         else if((">=".equals(pilha.peek()) || "<=".equals(pilha.peek()) ||
                  "=".equals(pilha.peek()) || "!=".equals(pilha.peek()) ||
                  ">".equals(pilha.peek()) || "<".equals(pilha.peek())) && 
                  (">=".equals(listaExpressao.get(i)) || ">=".equals(listaExpressao.get(i)) ||
                   "=".equals(listaExpressao.get(i)) || "!=".equals(listaExpressao.get(i)) ||
                   ">".equals(listaExpressao.get(i)) || "<".equals(listaExpressao.get(i)) ||
                   "+".equals(listaExpressao.get(i)) || "-".equals(listaExpressao.get(i)) ||
                   ">=".equals(listaExpressao.get(i)) || ">=".equals(listaExpressao.get(i)) ||
                   "(".equals(listaExpressao.get(i)))){
                      
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
     


    
