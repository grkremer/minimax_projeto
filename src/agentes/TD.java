package agentes;

import java.util.ArrayList;
import java.util.HashMap;
import agentes.util.Agente;
import jogos.util.Jogada;
import jogos.util.Jogo;
public class TD implements Agente{
    private HashMap<String, Double> stateValue;
    private HashMap<Pair<Jogada,String>,String> stateAction; //(state,action)-> state
    private int agentMark;
    private Jogo game;

    public Jogada Mover(Jogo game, int[][] board){ //move
        return null;
    }

    public void Learn(int[][] state, int maxEpisodes)
    {
        for(int i = 0; i < maxEpisodes; i++){
            int[][] currentState = new int[5][5];
            int playerTurn = agentMark;
            while(!game.verificaFimDeJogo(currentState)){
                
                Policy(currentState);
            }

        }
    }

    private int[][] Policy(int[][] prevState){
        Boolean eGreedy = true;

        if(eGreedy){
            
        }
    } 

    

    
}
