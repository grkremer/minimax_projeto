package agentes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import logging.LogArvoreMinimax;
import agentes.util.*;
import jogos.util.Jogada;
import jogos.util.Jogo;

public class MinimaxTree implements Agente{
    int profundidadeMax;
    int COR_PECA;
    NodoMinimax raiz;
    public final int MAX_PONTOS = 100;
    public final int MIN_PONTOS = -100;
    LogArvoreMinimax log;
    public MinimaxTree(int COR_PECA, int profundadeMax){
        
        this.profundidadeMax = profundadeMax;
        this.COR_PECA = COR_PECA;
    }

    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        log = new LogArvoreMinimax();
        Jogada j = Decide(jogo, tabuleiro, COR_PECA);
        log.AvaliaArvore(raiz);
        return j;
    }

    Jogada Decide(Jogo jogo, int[][] tabuleiro, int corPecaJogador) throws InterruptedException{
        
        raiz = new NodoMinimax(tabuleiro, corPecaJogador, 0);

        ArrayList<Jogada> possiveisJogadas = jogo.listaPossiveisJogadas(raiz.getCorPeca(), tabuleiro);
        //Collections.shuffle(possiveisJogadas);
        
        int corPecaAdversario = jogo.invertePeca(raiz.getCorPeca());
        int proxProfundidade = raiz.getProfundidade() + 1;
        for(Jogada j:possiveisJogadas){
        
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            NodoMinimax novoNodo = new NodoMinimax(novoTabuleiro, j, corPecaAdversario, proxProfundidade);
            raiz.insereNovoFilho(j, novoNodo);
            Min(jogo, novoNodo);
            
        }
        
        return raiz.getMelhorJogada();
    }

    private void Max(Jogo jogo, NodoMinimax nodoAtual) throws InterruptedException{
        if(nodoAtual.getProfundidade() == profundidadeMax ||jogo.verificaFimDeJogo(nodoAtual.getEstado())){
            float recompensa = jogo.geraCusto(nodoAtual.getCorPeca(), nodoAtual.getEstado(), MIN_PONTOS, MAX_PONTOS);
            nodoAtual.setRecompensa(recompensa);
            return;
        }
        
        int corPecaAdversario = jogo.invertePeca(nodoAtual.getCorPeca());
        int proxProfundidade = nodoAtual.getProfundidade()+1;

        for(Jogada j:jogo.listaPossiveisJogadas(nodoAtual.getCorPeca(), nodoAtual.getEstado())){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(nodoAtual.getCopiaEstado());
            jogo.fazJogada(j, novoTabuleiro, false);
            NodoMinimax novoNodo = new NodoMinimax(novoTabuleiro, j, corPecaAdversario, proxProfundidade);
            nodoAtual.insereNovoFilho(j, novoNodo);
            Min(jogo, novoNodo);
            
        }
        
        float RecompensaMaxima = nodoAtual.getMaiorRecompensaFilhos();
        nodoAtual.setRecompensa(RecompensaMaxima);
    }

    private void Min(Jogo jogo, NodoMinimax nodoAtual) throws InterruptedException{
        if(nodoAtual.getProfundidade() == profundidadeMax ||jogo.verificaFimDeJogo(nodoAtual.getEstado())){
            float recompensa = jogo.geraCusto(nodoAtual.getCorPeca(), nodoAtual.getEstado(), MIN_PONTOS, MAX_PONTOS);
            nodoAtual.setRecompensa(recompensa);
            return;
        }
        
        int corPecaAdversario = jogo.invertePeca(nodoAtual.getCorPeca());
        int proxProfundidade = nodoAtual.getProfundidade()+1;

        for(Jogada j:jogo.listaPossiveisJogadas(nodoAtual.getCorPeca(), nodoAtual.getEstado())){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(nodoAtual.getCopiaEstado());
            jogo.fazJogada(j, novoTabuleiro, false);
            NodoMinimax novoNodo = new NodoMinimax(novoTabuleiro, j, corPecaAdversario, proxProfundidade);
            nodoAtual.insereNovoFilho(j, novoNodo);
            Max(jogo, novoNodo);
        }
        
        float RecompensaMinima = nodoAtual.getMenorRecompensaFilhos();
        nodoAtual.setRecompensa(RecompensaMinima);

    }

    public int getCorPeca(){
        return COR_PECA;
    }

    @Override
    public String toString()
    {
        return log.toString();
    }
}
