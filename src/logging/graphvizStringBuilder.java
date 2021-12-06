package logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/* 
compile dot:
dot -Tsvg output.dot >> output.svg
*/

public class graphvizStringBuilder {
    
    
    String nodes = "";
    String edges = "";
    public graphvizStringBuilder(){};
    public void addNode(int[][] board, Float reward){

        nodes += getSignature(board) + "[label=<<FONT POINT-SIZE=\"5\">";
        
        for(int i = 0; i < 5; i++){
            String aux = "";
            for(int j = 0; j < 5; j++){
                aux += String.valueOf(board[i][j]) + " ";

            }
            aux += "<BR/>";
            nodes += aux;
        }
        nodes += "</FONT><FONT POINT-SIZE=\"6\">" + reward.toString() +  "</FONT>>];";

    }
    public void addNode(int[][] board, String output){

        nodes += getSignature(board) + "[label=<<FONT POINT-SIZE=\"5\">";
        
        for(int i = 0; i < 5; i++){
            String aux = "";
            for(int j = 0; j < 5; j++){
                aux += String.valueOf(board[i][j]) + " ";

            }
            aux += "<BR/>";
            nodes += aux;
        }
        nodes += "</FONT><FONT POINT-SIZE=\"6\">" + output +  "</FONT>>];";

    }

    public void addEdge(int[][] from, int[][] to){
        edges += getSignature(from) + "->" + getSignature(to) + " ";
    }

    private String getSignature(int[][] input){
        String o = "";
        for(int i = 0; i < 5; i++){
            for(int j = 0; j< 5;j++){
                o+= String.valueOf(input[i][j]);
            }
        }

        
        return o;
    }

    
    public String output(){
        try {
            File myObj = new File("minimaxTree-4.dot");
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
              System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("minimaxTree-4.dot");
            myWriter.write("digraph{" + nodes + "\n" + edges + "}");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        return "ok";
    }
}
