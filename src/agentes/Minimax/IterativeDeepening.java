package agentes.Minimax;

import java.util.Collections;
import java.util.ArrayList;

import jogos.util.Jogada;
import jogos.util.Jogo;

import java.util.Arrays;
import java.util.HashMap;

import agentes.util.IAgent;
import agentes.util.TTEntry;

import agentes.util.TTEntry.Flag;


public class IterativeDeepening implements IAgent{

    private final String ID = "Iterative Deepening";
    
    private int nodes;
    private int cutoffs;
    private int player;
    
    private int maxDepth;
    private HashMap<String, TTEntry> TTable;


    public IterativeDeepening(int player, int maxDepth){
        this.player = player;
        this.maxDepth = maxDepth;
        TTable = new HashMap<String, TTEntry>();
    }

    @Override
    public Jogada Move(Jogo environment, int[][] state, String[] args){
        
        cutoffs=0;
        

        float max = Integer.MIN_VALUE;;
        Jogada bestMove = null;
        float alpha = Float.NEGATIVE_INFINITY; 
        float beta  = Float.POSITIVE_INFINITY;
        ArrayList<Jogada> possibleMoves = environment.listaPossiveisJogadas(player, state);
        
        int currentDepth = 1;
        while(currentDepth < maxDepth){
            cutoffs = 0;
            if(bestMove != null){
                possibleMoves.remove(bestMove);
                possibleMoves.add(0, bestMove);
            }

            for(Jogada j:possibleMoves){
                int[][] nextState = environment.criaCopiaTabuleiro(state);
                environment.fazJogada(j, nextState, false);
                float value = Min(environment, nextState, environment.invertePeca(player), currentDepth-1, alpha, beta);
                if(value > max)
                {
                    bestMove = j;
                    max = value;
                }
            }
            currentDepth++;
        }

        System.out.println("nodes: " + String.valueOf(nodes) + "\tcutoffs: " + String.valueOf(cutoffs));
        
        return bestMove;
    }



    private float Max(Jogo environment, int[][] state, int currentPlayer, int depth, float alpha, float beta){
        
        nodes++;
        
        if(depth == 0 ||environment.verificaFimDeJogo(state)){
            return environment.geraCusto(this.player, state, -100, +100);
        }
        
        float value = Integer.MIN_VALUE;
        Jogada bestMove = null;
        int nextPlayer = environment.invertePeca(currentPlayer);
        
        String hash = getHash(state, player, depth);
        TTEntry entry = TTable.get(hash);
        ArrayList<Jogada> moves = environment.listaPossiveisJogadas(currentPlayer, state);
        
        // 1 check if state + player its in TTable and try it first
        if(entry != null){
            bestMove = entry.bestMove;
            moves.remove(bestMove);
            moves.add(0, bestMove);
        }
        
        // 2 iterate the rest of moves
        for(Jogada j:moves){
            int[][] nextState = environment.criaCopiaTabuleiro(state);
            environment.fazJogada(j, nextState, false);
            
            float newValue = Min(environment, nextState, nextPlayer, depth-1, alpha, beta);
            if(newValue > value){
                value = newValue;
                bestMove = j;
            }
           
            
            if(value >= beta){
                TTable.put(hash, new TTEntry(value, Flag.EXACT, depth, j));
                cutoffs++;
                return value;
            }
            alpha = Math.max(alpha, value);
        }
        
        TTable.put(hash, new TTEntry(value, Flag.EXACT, depth, bestMove));
        return value;
    }

    private float Min(Jogo environment, int[][] state, int currentPlayer, int depth, float alpha, float beta){
        
        nodes++;
        if(depth == 0 ||environment.verificaFimDeJogo(state)){
            return environment.geraCusto(this.player, state, -100, +100);
        }

        float value = Integer.MAX_VALUE;
        Jogada bestMove = null;
        int nextPlayer = environment.invertePeca(currentPlayer);


        String hash = getHash(state, player, depth);
        TTEntry entry = TTable.get(hash);
        ArrayList<Jogada> moves = environment.listaPossiveisJogadas(currentPlayer, state);
        
        // 1 check if state + player its in TTable and try it first
        if(entry != null){
            bestMove = entry.bestMove;
            moves.remove(bestMove);
            moves.add(0, bestMove);
        }

        for(Jogada j:moves){
            int[][] nextState = environment.criaCopiaTabuleiro(state);
            environment.fazJogada(j, nextState, false);
            
            float newValue = Max(environment, nextState,  nextPlayer, depth-1, alpha, beta);
            if(newValue < value){
                value = newValue;
                bestMove = j;
            }
            
            
            if(value <= alpha) {
                cutoffs++;
                TTable.put(hash, new TTEntry(value, Flag.EXACT, depth, j));
                return value;
            }
            beta = Math.min(beta, value);
        }
        
        TTable.put(hash, new TTEntry(value, Flag.EXACT, depth, bestMove));
        
        return value;
    }    

    private String getHash(int[][] state, int player, int depth){
        String hash = "";
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                hash += Integer.toString(state[i][j]);
            }
        }
        hash+=Integer.toString(player) + Integer.toString(depth);
        return hash;
    }

    @Override
    public int getCorPeca(){
        return this.player;
    }
    
    @Override
    public String toString()
    {
        return "";
    }

    @Override
    public String getID(){
        return ID;
    }
}
