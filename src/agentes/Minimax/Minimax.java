package agentes.Minimax;

import java.util.Collections;
import agentes.util.IAgent;
import jogos.util.Jogada;
import jogos.util.Jogo;
import agentes.Trees.MinimaxTree;
import java.util.Arrays;
import java.util.List;
public class Minimax implements IAgent{
    
    private final String ID = "MINIMAX";
    private Jogo lastGamePlayed;
    private int[][] lastBoardEvaluated;

    protected int maxDepth;
    protected int COR_PECA;
    public final int MAX_PONTOS = 100;
    public final int MIN_PONTOS = -100;
    
    
    protected float executionTime;
    protected long startTime;
    protected long endTime;
    protected int numberNodes;
    
    public Minimax(int COR_PECA, int profundadeMax){
        
        this.maxDepth = profundadeMax;
        this.COR_PECA = COR_PECA;
    }

    public Jogada Move(Jogo jogo, int[][] tabuleiro, String[] args){
        initializeVariables();
        numberNodes = 1;
        
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        int opponentPiece = jogo.invertePeca(COR_PECA);   
        List<Jogada> actions = jogo.listaPossiveisJogadas(COR_PECA, tabuleiro);
        Collections.shuffle(actions);

        for(Jogada j: actions ){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            float valor = Min(jogo, novoTabuleiro, opponentPiece, maxDepth-1);
            if(valor > max)
            {
                melhorJogada = j;
                max = valor;
            }
        }
        closeVariables();
        lastGamePlayed = jogo;
        lastBoardEvaluated = tabuleiro;
        System.out.println("nodes: " + String.valueOf(numberNodes) + "\ttime: " + executionTime + "s");
        return melhorJogada;
    }

    private float Max(Jogo jogo, int[][] tabuleiro, int corPecaAtual, int profundidade){
        numberNodes++;
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return jogo.geraCusto(COR_PECA, tabuleiro, MIN_PONTOS, MAX_PONTOS); //* fatorDesconto;
        }
        
        float valor = Integer.MIN_VALUE;
        int opponentPiece = jogo.invertePeca(corPecaAtual);
        List<Jogada> actions = jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro);
        Collections.shuffle(actions);
        
        for(Jogada j: actions ){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            valor = Math.max(valor, Min(jogo, novoTabuleiro, opponentPiece, profundidade-1)) ;
             
        }
        return valor;
    }

    private float Min(Jogo jogo, int[][] tabuleiro, int corPecaAtual, int profundidade){
        numberNodes++;
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return jogo.geraCusto(COR_PECA, tabuleiro, MIN_PONTOS, MAX_PONTOS); //* fatorDesconto;
        }
        
        float valor = Integer.MAX_VALUE;
        int opponentPiece = jogo.invertePeca(corPecaAtual);
        List<Jogada> actions = jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro);
        Collections.shuffle(actions);
        
        for(Jogada j: actions ){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            valor = Math.min(valor, Max(jogo, novoTabuleiro, opponentPiece, profundidade-1));
             
        }
        return valor;
    }

    public String[] ComputeStatistics(){
       return null;
    }

    protected void initializeVariables(){
        executionTime = 0;
        startTime = System.currentTimeMillis();  
    }

    protected void closeVariables(){
        endTime = System.currentTimeMillis();
        executionTime = (endTime - startTime)/1000f;
    }


    @Override
    public String getID(){
        return ID;
    } 

    @Override
    public int getCorPeca(){
        return COR_PECA;
    }

    public int getProfundidade(){
        return maxDepth;
    }

    @Override
    public String[] getArgs(){
        //return new String[]{String.valueOf(COR_PECA), this.ID, lastGamePlayed.getNome(), String.valueOf(executionTime)};
        return new String[]{this.ID, String.valueOf(COR_PECA), String.valueOf(executionTime)};
    }

    @Override
    public String toString()
    {
        return "";
        
    }
}
