package agentes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import agentes.util.Agente;
import jogos.util.Jogada;
import jogos.util.Jogo;

public class Minimax implements Agente{
    int numeroNodos;
    int profundidadeMax;
    int COR_PECA;
    public final int MAX_PONTOS = 100;
    public final int MIN_PONTOS = -100;
    
    public Minimax(int COR_PECA, int profundadeMax){
        
        this.profundidadeMax = profundadeMax;
        this.COR_PECA = COR_PECA;
    }

    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        return Decide(jogo, tabuleiro, COR_PECA, COR_PECA);
    }

    Jogada Decide(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual) throws InterruptedException{
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
        
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            float valor = Min(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidadeMax-1);
            if(valor > max)
            {
                melhorJogada = j;
                max = valor;
            }
        }
        
        return melhorJogada;
    }

    private float Max(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade) throws InterruptedException{
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS); //* fatorDesconto;
        }
        
        float valor = Integer.MIN_VALUE;
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            valor = Math.max(valor, Min(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidade-1));
            
        }
        return valor;
    }

    private float Min(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade) throws InterruptedException{
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS); //* fatorDesconto;
        }
        
        float valor = Integer.MAX_VALUE;
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            valor = Math.min(valor, Max(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidade-1));
            
        }
        return valor;
    }

    public int getCorPeca(){
        return COR_PECA;
    }

    public int getProfundidade(){
        return profundidadeMax;
    }

    @Override
    public String toString()
    {
        return "";
        
    }
}
