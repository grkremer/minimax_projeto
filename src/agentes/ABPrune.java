package agentes;

import java.util.ArrayList;
import java.util.HashMap;

import agentes.util.Agente;
import jogos.util.Jogada;
import jogos.util.Jogo;


public class ABPrune implements Agente{
    int numeroNodos;
    int profundidadeMax;
    int COR_PECA;
    public final int MAX_PONTOS = 100;
    public final int MIN_PONTOS = -100;
    int totalNodes = 0;
    public ABPrune(int COR_PECA, int profundadeMax){
        
        this.profundidadeMax = profundadeMax;
        this.COR_PECA = COR_PECA;
    }

    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        totalNodes= 0;
        return Poda(jogo, tabuleiro, COR_PECA, COR_PECA);
    }

    Jogada Poda(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual) throws InterruptedException{
        int max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        float alpha = Float.NEGATIVE_INFINITY; 
        float beta  = Float.POSITIVE_INFINITY;
        ArrayList<Jogada> possiveisJogadas = jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro);
        
        for(Jogada j:possiveisJogadas){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            int valor = Min(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidadeMax-1, alpha, beta);
            if(valor > max)
            {
                melhorJogada = j;
                max = valor;
            }
        }
        
        return melhorJogada;
    }

    private int Max(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade, float alpha, float beta) throws InterruptedException{
        totalNodes++;
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return (int)jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
        }
        
        int valor = Integer.MIN_VALUE;
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            
            valor = Math.max(valor, Min(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidade-1, alpha, beta));
            if(valor >= beta){ 
                return valor;
            }
            alpha = Math.max(alpha, valor);
        }
        return valor;
    }

    private int Min(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade, float alpha, float beta) throws InterruptedException{
        totalNodes++;
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return (int)jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
        }
        
        int valor = Integer.MAX_VALUE;
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            
            valor = Math.min(valor, Max(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidade-1, alpha, beta));
            if(valor <= alpha) {
                return valor;
            }
            beta = Math.min(beta, valor);
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
       
        return "totalNodes: " + totalNodes;
    }
}
