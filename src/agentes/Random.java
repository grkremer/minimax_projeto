package agentes;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import agentes.util.IAgent;
import jogos.util.Jogada;
import jogos.util.Jogo;

public class Random implements IAgent{
    
    protected int COR_PECA;
    private int branch;
    private int maxBranch;
    private int nodesVisited;
    private final String ID = "RANDOM";
    private Jogo lastGamePlayed;
    private int[][] lastBoardEvaluated;
    private String maxBoard;

    public Random(int COR_PECA){
        this.COR_PECA = COR_PECA;
        branch = 0;
        maxBranch = 0;
        nodesVisited = 0;
        maxBoard="";
    }
    
    @Override
    public Jogada Move(Jogo jogo, int[][] tabuleiro, String[] args) throws InterruptedException
    {
        List<Jogada> actions = jogo.listaPossiveisJogadas(COR_PECA, tabuleiro);
        branch = actions.size();
        nodesVisited+=branch;
        
        if(branch > maxBranch) {
            maxBranch = branch;
            maxBoard="";
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    maxBoard+=tabuleiro[i][j];
                }
                maxBoard+="\t";
            }
        }
        Collections.shuffle(actions);
        return actions.get(0);
        
    }
    
    @Override
    public int getCorPeca()
    { return COR_PECA; }
    
    @Override
    public String[] getArgs()
    {
        return new String[]{this.ID, String.valueOf(COR_PECA), String.valueOf(0), String.valueOf(0), String.valueOf(branch), String.valueOf(nodesVisited), String.valueOf(maxBranch), maxBoard};
    }
    
    @Override
    public String[] ComputeStatistics()
    {
        return getArgs();
    }
    
}
