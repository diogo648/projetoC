/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Diogo
 */
public class GeracaoCodigo {
    
    PrintWriter escrever = null;
    
    public GeracaoCodigo() throws IOException{
    
        
        escrever = new PrintWriter("programa_assembly.txt", "UTF-8");

    }
    
   
    public void addComando(String comando){
        
        escrever.println(comando);
            
    }
    
}
