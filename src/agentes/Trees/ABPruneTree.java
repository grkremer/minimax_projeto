package agentes.Trees;

import java.util.ArrayList;

import agentes.ABPrune;
import agentes.util.*;
import jogos.util.Jogada;
import jogos.util.Jogo;
import logging.LogMinimax;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ABPruneTree extends ABPrune{
    NodoMinimax root;
    LogMinimax log;
    
    private final String ID = "MINIMAX_T";
    private long startTime;
    private long endTime;
    private float runningTime;
    private int numberNodes;
    
    public ABPruneTree(int COR_PECA, int profundadeMax){
        super(COR_PECA, profundadeMax);
    }

    @Override
    public Jogada Move(Jogo jogo, int[][] tabuleiro, String[] args) throws InterruptedException{
        initializeVariables();
        super.numberNodes=1;
        super.cutoffs=0;
        
        
        root = new NodoMinimax(tabuleiro, COR_PECA, 0);
        int nextDepth = root.getProfundidade() + 1;
        
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        int opponentPiece = jogo.invertePeca(COR_PECA);
        
        for(Jogada j:jogo.listaPossiveisJogadas(COR_PECA, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            NodoMinimax novoNodo = new NodoMinimax(novoTabuleiro, j, opponentPiece, nextDepth);
            jogo.fazJogada(j, novoTabuleiro, false);
            float value = Min(jogo, novoTabuleiro, opponentPiece, maxDepth-1, Integer.MIN_VALUE, Integer.MAX_VALUE, novoNodo); 
            if(value > max)
            {
                melhorJogada = j;
                max = value;
            }

            root.insereNovoFilho(j, novoNodo);
            novoNodo.setRecompensa(value);
            
        }
        
        closeVariables();
        log.AvaliaArvore(root);
        System.out.println("nodesTree: " + String.valueOf(numberNodes) + "\ttime" + String.valueOf(runningTime)  + "\ncutoffsTree: " + String.valueOf(cutoffs));
        return melhorJogada;
        
    }

    private float Max(Jogo game, int[][] board, int currentPiece, int depth, float alpha, float beta, NodoMinimax currentNode) throws InterruptedException{
        int nextDepth = currentNode.getProfundidade()+1;
        

        super.numberNodes++;
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, MIN_PONTOS, MAX_PONTOS);
        }
        
        float value = Integer.MIN_VALUE;
        int opponentPiece = game.invertePeca(currentPiece);
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            NodoMinimax newNode = new NodoMinimax(newBoard, j, opponentPiece, nextDepth);
            game.fazJogada(j, newBoard, false);
            
            float tmpValue = Min(game, newBoard, opponentPiece, depth-1, alpha, beta, newNode);
            value = Math.max(value, tmpValue);
            
            
            currentNode.insereNovoFilho(j, newNode);
            newNode.setRecompensa(value);
            
            if(value >= beta){
                super.cutoffs++;
                return value;
            }
            alpha = Math.max(alpha, value);
        }
        return value;
    }

    private float Min(Jogo game, int[][] board, int currentPiece, int depth, float alpha, float beta, NodoMinimax currentNode) throws InterruptedException{
        int nextDepth = currentNode.getProfundidade()+1;
        
        super.numberNodes++;
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, MIN_PONTOS, MAX_PONTOS);
        }
        
        
        float value = Integer.MAX_VALUE;
        int opponentPiece = game.invertePeca(currentPiece);
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            NodoMinimax newNode = new NodoMinimax(newBoard, j, opponentPiece, nextDepth);
            game.fazJogada(j, newBoard, false);
            float tmpValue = Max(game, newBoard, opponentPiece, depth-1, alpha, beta, newNode);
            value = Math.min(value, tmpValue);
            
            
            currentNode.insereNovoFilho(j, newNode);
            newNode.setRecompensa(value);
            
            if(value <= alpha) {
                super.cutoffs++;
                return value;
            }
            beta = Math.min(beta, value);
        }

        currentNode.setRecompensa(alpha);
        
        return value;
    }    

    @Override
    public String[] ComputeStatistics(){
        
        String[] thisArgs = getArgs(); //nome_agente, cor_peça 
        String[] arr = new String[]{this.ID, String.valueOf(COR_PECA), String.valueOf(runningTime)};
        String[] result = Arrays.copyOf(arr, arr.length + thisArgs.length);
        System.arraycopy(thisArgs, 0, result, arr.length, thisArgs.length);
        return result;
    }
    
    @Override
    protected void initializeVariables(){
        super.initializeVariables();
        log = new LogMinimax();
        runningTime = 0;
        startTime = System.currentTimeMillis(); 
    
    }

    @Override
    protected void closeVariables(){
        endTime = System.currentTimeMillis();
        runningTime = (endTime - startTime)/1000f;
    }

    @Override
    public String[] getArgs(){
        return new String[]{String.valueOf(log.numeroNodos), String.valueOf(log.maxBranching), String.valueOf(log.mediaBranching), String.valueOf(this.cutoffs)};
    }

    public int getCorPeca(){
        return COR_PECA;
    }

    @Override
    public String toString()
    {
        return "";
    }
}
