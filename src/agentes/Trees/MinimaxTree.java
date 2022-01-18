package agentes.Trees;

import java.util.ArrayList;

import agentes.util.*;
import jogos.util.Jogada;
import jogos.util.Jogo;
import logging.LogMinimax;
import agentes.Minimax;

/* 
    The purpose of minimaxxTree is to save the tree in memory
        in order to compute information about it.
*/
public class MinimaxTree extends Minimax{
    LogMinimax log;
    NodoMinimax root;
    
    private int numberNodes;

    public MinimaxTree(int COR_PECA, int maxDepth){
        super(COR_PECA, maxDepth);
    }

    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        initializeVariables();
        numberNodes = 1;
        root = new NodoMinimax(tabuleiro, COR_PECA, 0);
        
        
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        int opponentPiece = jogo.invertePeca(COR_PECA);
        for(Jogada j:jogo.listaPossiveisJogadas(COR_PECA, tabuleiro)){
        
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            NodoMinimax newNode = new NodoMinimax(novoTabuleiro, j, opponentPiece, 0);
            root.insereNovoFilho(j, newNode);
            float valor = Min(jogo, novoTabuleiro, opponentPiece, maxDepth-1, newNode);
            if(valor > max)
            {
                melhorJogada = j;
                max = valor;
            }
        }
        closeVariables();
        log.AvaliaArvore(root);
        System.out.println("nodesT: " + String.valueOf(numberNodes));
        return melhorJogada;
        
    }

    private float Max(Jogo jogo, int[][] tabuleiro, int corPecaAtual, int profundidade, NodoMinimax nodoAtual) throws InterruptedException{
        numberNodes++;
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return jogo.geraCusto(COR_PECA, tabuleiro, MIN_PONTOS, MAX_PONTOS); //* fatorDesconto;
        }
        
        float max = Integer.MIN_VALUE;
        int opponentPiece = jogo.invertePeca(corPecaAtual);
        int nextDepth = nodoAtual.getProfundidade()+1;

        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            NodoMinimax newNode = new NodoMinimax(novoTabuleiro, j, opponentPiece, nextDepth);
            nodoAtual.insereNovoFilho(j, newNode);
            jogo.fazJogada(j, novoTabuleiro, false);
            float value  = Min(jogo, novoTabuleiro, opponentPiece, profundidade-1, newNode);
            newNode.setRecompensa(value);
            max = Math.max(max,  value * 0.99f) ;
        }

        nodoAtual.setRecompensa(max);
        return max;
    }

    private float Min(Jogo jogo, int[][] tabuleiro, int corPecaAtual, int profundidade, NodoMinimax nodoAtual) throws InterruptedException{
        numberNodes++;
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return jogo.geraCusto(COR_PECA, tabuleiro, MIN_PONTOS, MAX_PONTOS);
        }
        
        float min = Integer.MAX_VALUE;
        int opponentPiece = jogo.invertePeca(corPecaAtual);
        int nextDepth = nodoAtual.getProfundidade()+1;

        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            NodoMinimax newNode = new NodoMinimax(novoTabuleiro, j, opponentPiece, nextDepth);
            nodoAtual.insereNovoFilho(j, newNode);
            jogo.fazJogada(j, novoTabuleiro, false);
            float value  = Max(jogo, novoTabuleiro, opponentPiece, profundidade-1, newNode);
            min = Math.min(min,  value * 0.99f) ;
            newNode.setRecompensa(value);

        }
        
        nodoAtual.setRecompensa(min);
        return min;
    }

    @Override
    protected void initializeVariables(){
        super.initializeVariables();
        log = new LogMinimax();
    }

    @Override
    public String[] getArgs(){
        return new String[]{String.valueOf(log.numeroNodos), String.valueOf(log.maxBranching), String.valueOf(log.mediaBranching)};
    }
    @Override
    public String toString()
    {
        return "";
    }
}
