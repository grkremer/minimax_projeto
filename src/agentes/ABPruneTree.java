package agentes;

import java.util.ArrayList;

import agentes.util.*;
import jogos.util.Jogada;
import jogos.util.Jogo;
import logging.LogMinimax;

public class ABPruneTree implements Agente{
    int profundidadeMax;
    int COR_PECA;
    NodoMinimax raiz;
    public final int MAX_PONTOS = 100;
    public final int MIN_PONTOS = -100;
    LogMinimax log;
    public ABPruneTree(int COR_PECA, int profundadeMax){
        
        this.profundidadeMax = profundadeMax;
        this.COR_PECA = COR_PECA;
    }

    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        log = new LogMinimax();
        Jogada j = Poda(jogo, tabuleiro, COR_PECA);
        log.AvaliaArvore(raiz);
        return j;
    }

    Jogada Poda(Jogo jogo, int[][] tabuleiro, int corPecaJogador) throws InterruptedException{
        raiz = new NodoMinimax(tabuleiro, corPecaJogador, 0);

        ArrayList<Jogada> possiveisJogadas = jogo.listaPossiveisJogadas(raiz.getCorPeca(), tabuleiro);
        
        int corPecaAdversario = jogo.invertePeca(raiz.getCorPeca());
        int proxProfundidade = raiz.getProfundidade() + 1;
        float alpha = Float.NEGATIVE_INFINITY; 
        float beta  = Float.POSITIVE_INFINITY;
        
        
        for(Jogada j:possiveisJogadas){
        
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            NodoMinimax novoNodo = new NodoMinimax(novoTabuleiro, j, corPecaAdversario, proxProfundidade);
            raiz.insereNovoFilho(j, novoNodo);
            Min(jogo, novoNodo, alpha, beta);
        }
        
        return raiz.getMelhorJogada();
    }

    private void Max(Jogo jogo, NodoMinimax nodoAtual, float alpha, float beta) throws InterruptedException{
        if(nodoAtual.getProfundidade() == profundidadeMax ||jogo.verificaFimDeJogo(nodoAtual.getEstado())){
            float recompensa = jogo.geraCusto(raiz.getCorPeca(), nodoAtual.getEstado(), MIN_PONTOS, MAX_PONTOS);
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
            Min(jogo, novoNodo, alpha, beta);
            float maiorValor = nodoAtual.getMaiorRecompensaFilhos();
            if(maiorValor >= beta){
                nodoAtual.setRecompensa(maiorValor);
                return;
            }  
            alpha = Math.max(alpha, maiorValor);
        }
        
        float RecompensaMaxima = nodoAtual.getMaiorRecompensaFilhos();
        nodoAtual.setRecompensa(RecompensaMaxima);
    }

    private void Min(Jogo jogo, NodoMinimax nodoAtual, float alpha, float beta) throws InterruptedException{
        if(nodoAtual.getProfundidade() == profundidadeMax ||jogo.verificaFimDeJogo(nodoAtual.getEstado())){
            float recompensa = jogo.geraCusto(raiz.getCorPeca(), nodoAtual.getEstado(), MIN_PONTOS, MAX_PONTOS);
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
            Max(jogo, novoNodo, alpha, beta);

            float menorValor =  nodoAtual.getMenorRecompensaFilhos();
            if(menorValor <= alpha){
                nodoAtual.setRecompensa(menorValor);
                return;
            }
            beta = Math.min(beta, menorValor);
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
