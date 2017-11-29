/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import Excecoes.ExcecaoSemantico;
import Simbolos.Funcao;
import Simbolos.Procedimento;
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

    
 public void insereTabela(String lexema, String tipo, boolean escopo, String label) {
     
     
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
     
         simbolos.add(new Procedimento(lexema, tipo, escopo, label));
     
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
     
     if(simbolos.get(i).getEscopo() == true){
     
          simbolos.remove(i);
          i--;
         
     }
     
    while(simbolos.get(i).getEscopo() == false){
         
        variaveisRetiradas++;
        
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
 
     int i = simbolos.size()-1;
     
     //A condição de parada é a até achar o 'nomedeprograma'
     while(!"nomedeprograma".equals(simbolos.get(i).getTipo()) ){
    
         if(simbolos.get(i).getLexema().equals(lexema)){
    
            return true;
        }
       
         i--;
     }
     
     return false;
 }
 
 public String retornaLabelProc(String lexema){
 
    int i = simbolos.size()-1;
     
     //A condição de parada é a até achar o 'nomedeprograma'
     while(!"nomedeprograma".equals(simbolos.get(i).getTipo()) ){
    
         if(simbolos.get(i).getLexema().equals(lexema)){
    
             Object obj = simbolos.get(i);
      
            Procedimento proc = (Procedimento) obj;
              
            return proc.getLabel();
        }
       
         i--;
     }
          
     return null;
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
             
             if("nao".equals(listaExpressao.get(i))){
                 
                //tipos.add(listaExpressao.get(i));
             }
             
             else if("verdadeiro".equals(listaExpressao.get(i))){
                 
                tipos.add("booleano");
             }
              
             else if("falso".equals(listaExpressao.get(i))){
                 
                tipos.add("booleano");
             }
              
             
             else if("e".equals(listaExpressao.get(i)) || "ou".equals(listaExpressao.get(i)) ){
             
                 //tipos.add(listaExpressao.get(i));
                 
             }
             
            
             else{
       
                //Começa pelo topo da tabela de símbolos
                int z =simbolos.size()-1; 

                 while(!"nomedeprograma".equals(simbolos.get(z).getTipo())){



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
 

 public void validaExpresaoSe(ArrayList<String> listaExpressao) throws Exception{
     
     Boolean valida = false;
     String tipoVar="";
     
     for(int i=0; i<listaExpressao.size(); i++){
         
         if("e".equals(listaExpressao.get(i)) || "ou".equals(listaExpressao.get(i)) ){
         
             valida = false;
         }
         
         else if("!=".equals(listaExpressao.get(i)) || "=".equals(listaExpressao.get(i)) ||
            ">".equals(listaExpressao.get(i)) || "<".equals(listaExpressao.get(i)) ||     
            "<=".equals(listaExpressao.get(i)) || ">=".equals(listaExpressao.get(i))){
             
             valida = true;
             
         }
        
         else if(Character.isLetter(listaExpressao.get(i).charAt(0)) && !"e".equals(listaExpressao.get(i)) && !"ou".equals(listaExpressao.get(i)) ){
            
            tipoVar = retornaTipo(listaExpressao.get(i));
        
        }
        
         else if(valida == true && "booleano".equals(tipoVar)){
        
            throw new ExcecaoSemantico("Variável do tipo: " + "'" + tipoVar + "'" + " incompatível na expressão!");
        
        }
        
     
     }
     
     
     
}
     
 
 public void ValidaExpressaoEnquanto(ArrayList<String> listaExpressao) throws Exception{
 
     Boolean valida = false;
     String tipoVar="";
     
     
     for(int i=0; i<listaExpressao.size(); i++){
         
         if("!=".equals(listaExpressao.get(i)) || "=".equals(listaExpressao.get(i)) ||
            ">".equals(listaExpressao.get(i)) || "<".equals(listaExpressao.get(i)) ||     
            "<=".equals(listaExpressao.get(i)) || ">=".equals(listaExpressao.get(i))){
             
             valida = true;
             
         }
    
    }
     
     for(int i=0; i<listaExpressao.size(); i++){
         
        if(Character.isLetter(listaExpressao.get(i).charAt(0))){
            tipoVar =  retornaTipo(listaExpressao.get(i));

            if("inteiro".equals(tipoVar) && valida == false){

                throw new ExcecaoSemantico("Expressão não é compatível com o comando 'Enquanto'!");
            }
        }
        
     }
     
     
 }
 
 
  
 public String retornaTipo(String variavel){
     
    //Começa pelo topo da tabela de símbolos
    int z =simbolos.size()-1; 

    while(simbolos.get(z).getEscopo() == false){


        if(simbolos.get(z).getLexema().equals(variavel)){
            
            //Returna o tipo da variavel
            return simbolos.get(z).getTipo();  
        }
        
        z--;
    }
 
    return null;
 
 }
 
 
 public ArrayList<String> posFixa(ArrayList<String> listaExpressao){
 
     
     //Teste Imprimir listaExpressao
     
     for(int i=0; i<listaExpressao.size(); i++){
     
        System.out.println(listaExpressao.get(i));
     }
     
     System.out.println("\n \n");
     
     Stack<String> pilha = new Stack<>();
     
     ArrayList<String> listaSaida = new ArrayList<> ();
   
     pilha.push(" ");
     
     for(int i=0; i<listaExpressao.size(); i++){
     
         
         //Determina as prioridades
         int prioridadePilha=66;
         int prioridadeLista=66;
         
         int index = pilha.size();
         
       if("(".equals(listaExpressao.get(i))){
             prioridadeLista = 0;
        }
        
        if("*".equals(pilha.peek()) || "div".equals(pilha.peek()) ){
             prioridadePilha = 1;
        }
        if("*".equals(listaExpressao.get(i)) || "div".equals(listaExpressao.get(i))){
             prioridadeLista = 1;
        }
        
        
        if("+".equals(pilha.peek()) || "-".equals(pilha.peek()) ){
             prioridadePilha = 2;
        }
         if("+".equals(listaExpressao.get(i)) || "-".equals(listaExpressao.get(i))){
             prioridadeLista = 2;
        }
        
        
        
        if(">".equals(pilha.peek()) || "<".equals(pilha.peek()) ||
           ">=".equals(pilha.peek()) || "<=".equals(pilha.peek()) || 
           "=".equals(pilha.peek()) || "!=".equals(pilha.peek())) {
            prioridadePilha = 3;
        }
        if(">".equals(listaExpressao.get(i)) || "<".equals(listaExpressao.get(i)) ||
           ">=".equals(listaExpressao.get(i)) || "<=".equals(listaExpressao.get(i)) || 
           "=".equals(listaExpressao.get(i)) || "!=".equals(listaExpressao.get(i))) {
            prioridadeLista = 3;
        }
        
        
        if("e".equals(pilha.peek())){
            prioridadePilha = 4;
        }
        if("e".equals(listaExpressao.get(i))){
            prioridadeLista = 4;
        }
        
        
        if("ou".equals(pilha.peek())){
            prioridadePilha = 5;
        }
        if("ou".equals(listaExpressao.get(i))){
            prioridadeLista = 5;
        } 
        
        
         if(")".equals(listaExpressao.get(i))){
             prioridadeLista = 7;
        }
        
        
       
        if(prioridadeLista == 66){
            
            listaSaida.add(listaExpressao.get(i));
        
        }
        
        //Trata unário
        else if(prioridadeLista == 2 && "(".equals(pilha.get(index-1))){
        
            pilha.push(listaExpressao.get(i)+"^unario");
            
        
        }
        
         //Se o topo da pilha estiver vazio, incluir qualquer coisa
        else if(" ".equals(pilha.peek()) || "(".equals(pilha.peek()) ){
        
            pilha.push(listaExpressao.get(i));
        
        }
         
      
        
        else if(prioridadeLista == 7){
            
            while(!"(".equals(pilha.peek())){
     
                listaSaida.add(pilha.peek());
                pilha.pop();
            }
            
            //Tira o '(' do topo da pilha
            pilha.pop();
        }
        
        else if(prioridadePilha > prioridadeLista  ){
           
            pilha.push(listaExpressao.get(i));
            
        }
     
        else if(prioridadePilha <= prioridadeLista){
            
            listaSaida.add(pilha.peek());
            pilha.pop();
            pilha.push(listaExpressao.get(i));
     
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
     


    
