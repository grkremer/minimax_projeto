package agentes;

import java.util.Collections;
import agentes.util.Agente;
import jogos.util.Jogada;
import jogos.util.Jogo;
import agentes.Trees.MinimaxTree;
import java.util.Arrays;
import java.util.List;
public class Minimax implements Agente{
    
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

    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
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

    private float Max(Jogo jogo, int[][] tabuleiro, int corPecaAtual, int profundidade) throws InterruptedException{
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
            valor = Math.max(valor, Min(jogo, novoTabuleiro, opponentPiece, profundidade-1) * 0.99f) ;
             
        }
        return valor;
    }

    private float Min(Jogo jogo, int[][] tabuleiro, int corPecaAtual, int profundidade) throws InterruptedException{
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
            valor = Math.min(valor, Max(jogo, novoTabuleiro, opponentPiece, profundidade-1) * 0.99f);
             
        }
        return valor;
    }

    public String[] ComputeStatistics(){
        Minimax logTree = new MinimaxTree(COR_PECA, maxDepth);
        
        try{
            logTree.Mover(this.lastGamePlayed, lastBoardEvaluated);
        }catch(InterruptedException e){

        }

        String[] logArgs = logTree.getArgs();
        String[] thisArgs = getArgs();
        String[] result = Arrays.copyOf(logArgs, logArgs.length + thisArgs.length);
        System.arraycopy(thisArgs, 0, result, logArgs.length, thisArgs.length);
        return result;
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
    public int getCorPeca(){
        return COR_PECA;
    }

    public int getProfundidade(){
        return maxDepth;
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
}
