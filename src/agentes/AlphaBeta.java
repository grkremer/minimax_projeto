package agentes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import agentes.util.Agente;
import jogos.util.Jogada;
import jogos.util.Jogo;
import logging.graphvizStringBuilder;

public class AlphaBeta implements Agente{
    int numeroNodos;
    HashMap<Integer, Integer> nodosPorNivel;
    int profundidadeMax;
    int cortes;
    int COR_PECA;
    public final int MAX_PONTOS = 100;
    public final int MIN_PONTOS = -100;
    graphvizStringBuilder graphBuilder =  new graphvizStringBuilder();

    public AlphaBeta(int COR_PECA, int profundadeMax){
        
        this.profundidadeMax = profundadeMax;
        numeroNodos = 0;
        cortes = 0;
        nodosPorNivel = new HashMap<Integer,Integer>();
        this.COR_PECA = COR_PECA;
    }

    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        return Poda(jogo, tabuleiro, COR_PECA, COR_PECA);
    }

    Jogada Poda(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual) throws InterruptedException{
        numeroNodos+=1;
        //inicializar variaveis de log
        for(int i = 0; i < profundidadeMax+1; i++){
            nodosPorNivel.put(i,0);
        }
        nodosPorNivel.put(0, 1) ;
        
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        float alpha = Float.NEGATIVE_INFINITY; 
        float beta  = Float.POSITIVE_INFINITY;
        ArrayList<Jogada> possiveisJogadas = jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro);
        Collections.shuffle(possiveisJogadas);
        for(Jogada j:possiveisJogadas){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            float valor = Min(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidadeMax-1, alpha, beta);
            graphBuilder.addEdge(tabuleiro, novoTabuleiro);
            
            if(valor > max)
            {
                melhorJogada = j;
                max = valor;
            }
        }
        
        graphBuilder.addNode(tabuleiro, max);
        System.out.println(graphBuilder.output());

        return melhorJogada;
    }

    private float Max(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade, float alpha, float beta) throws InterruptedException{
        numeroNodos+=1;
        nodosPorNivel.put(profundidadeMax - profundidade, nodosPorNivel.get(profundidadeMax - profundidade)+1) ;
        
        
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            //return (int)jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
            float custo = jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
            graphBuilder.addNode(tabuleiro, custo);
            return custo;
        }
        
        float valor = Integer.MIN_VALUE;
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            float tmpValor = Min(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidade-1, alpha, beta);
            valor = Math.max(valor, tmpValor);
            
            graphBuilder.addEdge(tabuleiro, novoTabuleiro);
            if(valor >= beta){ 
                cortes+=1;
                graphBuilder.addNode(tabuleiro, valor);
                return valor;
            }
            
            alpha = Math.max(alpha, valor);
        }
        
        graphBuilder.addNode(tabuleiro, valor);
        return valor;
    }

    private float Min(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade, float alpha, float beta) throws InterruptedException{
        numeroNodos+=1;
        nodosPorNivel.put(profundidadeMax - profundidade, nodosPorNivel.get(profundidadeMax - profundidade)+1) ;
        
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            //return (int)jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
            float custo = jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
            graphBuilder.addNode(tabuleiro, custo);
            return custo;//* fatorDesconto;
        }
        
        float valor = Integer.MAX_VALUE;
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            float tmpValor = Max(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidade-1, alpha, beta);
            valor = Math.min(valor, tmpValor);
            graphBuilder.addEdge(tabuleiro, novoTabuleiro);
            
            if(valor <= alpha) {
                cortes += 1;
                graphBuilder.addNode(tabuleiro, valor);
        
                return valor;
            }
            beta = Math.min(beta, valor);
        }
        
        graphBuilder.addNode(tabuleiro, valor);
        return valor;
    }

    public int getCorPeca(){
        return COR_PECA;
    }

    @Override
    public String toString()
    {
        String filhosPorNivel = "";
        
        for(int i=0; i < nodosPorNivel.size(); i++)
        {
            int n = nodosPorNivel.get(i);
            filhosPorNivel += "(nvl " + i + ":" +  n + ") ";
        }
        return "\nALPHABETA\nnumero de nodos: " + numeroNodos + "\n" + filhosPorNivel + "\ncortes: " + cortes + "\n";
    }
}
