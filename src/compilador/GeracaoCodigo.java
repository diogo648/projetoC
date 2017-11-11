/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Diogo
 */
public class GeracaoCodigo {
    
    PrintWriter escrever = null;
    
    public GeracaoCodigo() throws IOException{
   
        

    }
    
   
    public void addComando(String comando, String v1, String v2) throws IOException{
        
        String nomeArquivo= "C:\\Users\\Diogo\\Desktop\\saida.txt";
        try (FileWriter fw = new FileWriter(nomeArquivo,true)) {
            fw.write(comando + " " + v1 + " " + v2 + "\n");
        }
          
    }
    
}
