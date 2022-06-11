package agentes.Minimax;

import java.util.Collections;
import java.util.ArrayList;

import jogos.util.Jogada;
import jogos.util.Jogo;

import java.util.Arrays;

import agentes.Trees.ABPruneTree;



public class ABPrune extends Minimax{
    
    private final String ID = "ABPRUNE";
    private Jogo lastGamePlayed;
    private int[][] lastBoardEvaluated;
    
    protected int cutoffs;

    public ABPrune(int COR_PECA, int profundadeMax){
        super(COR_PECA, profundadeMax);
    }

    @Override
    public Jogada Move(Jogo jogo, int[][] tabuleiro, String[] args) throws InterruptedException{
        super.initializeVariables();
        super.numberNodes=1;
        cutoffs=0;
        
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        float alpha = Float.NEGATIVE_INFINITY; 
        float beta  = Float.POSITIVE_INFINITY;
        ArrayList<Jogada> possiveisJogadas = jogo.listaPossiveisJogadas(COR_PECA, tabuleiro);
        
        for(Jogada j:possiveisJogadas){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            float valor = Min(jogo, novoTabuleiro, COR_PECA, jogo.invertePeca(COR_PECA), maxDepth-1, alpha, beta);
            if(valor > max)
            {
                melhorJogada = j;
                max = valor;
            }
        }
        super.closeVariables();
        lastGamePlayed = jogo;
        lastBoardEvaluated = jogo.criaCopiaTabuleiro(tabuleiro);
        System.out.println("nodes: " + String.valueOf(numberNodes) + "\ncutoffs: " + String.valueOf(cutoffs));
       
        return melhorJogada;
    }

    private float Max(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade, float alpha, float beta) throws InterruptedException{
        super.numberNodes++;
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
        }
        
        float valor = Integer.MIN_VALUE;
        int corPecaAdversario = jogo.invertePeca(corPecaAtual);
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            
            valor = Math.max(valor, Min(jogo, novoTabuleiro, corPecaJogador, corPecaAdversario, profundidade-1, alpha, beta));
            if(valor >= beta){
                cutoffs++;
                return valor;
            }
            alpha = Math.max(alpha, valor);
        }
        return valor;
    }

    private float Min(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade, float alpha, float beta) throws InterruptedException{
        super.numberNodes++;
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
        }
        
        float valor = Integer.MAX_VALUE;
        int corPecaAdversario = jogo.invertePeca(corPecaAtual);
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            
            valor = Math.min(valor, Max(jogo, novoTabuleiro, corPecaJogador, corPecaAdversario, profundidade-1, alpha, beta));
            if(valor <= alpha) {
                cutoffs++;
                return valor;
            }
            beta = Math.min(beta, valor);
        }
        return valor;
    }    


    public String[] ComputeStatistics(){
        ABPruneTree logTree = new ABPruneTree(COR_PECA, maxDepth);
        
        try{
            logTree.Move(lastGamePlayed, lastBoardEvaluated, null);
        }catch(InterruptedException e){

        }

        String[] logArgs = logTree.getArgs();
        String[] thisArgs = getArgs();
        String[] result = Arrays.copyOf(logArgs, logArgs.length + thisArgs.length);
        System.arraycopy(thisArgs, 0, result, logArgs.length, thisArgs.length);
        return result;
    }

    @Override
    public String[] getArgs(){
         return new String[]{String.valueOf(COR_PECA), this.ID, lastGamePlayed.getNome(), String.valueOf(executionTime)};
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
