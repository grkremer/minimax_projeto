package agentes;

import java.util.HashMap;
import agentes.util.TTEntry;
import jogos.util.Jogada;
import jogos.util.Jogo;
import agentes.Trees.NegTTTree;
import java.util.Arrays;
/* 
    Negamax w/ transposition table implementation.

    Negamax OK
    NegamaxTree OK
    logNegamax INCOMPLETE
    it seems like its not working *not exact like minimax

*/
public class NegTT extends Negamax{
    private final String ID = "NEGAMAXTT";
    
    protected HashMap<String, TTEntry> transpositionTable;
    protected int transpositions;
    public NegTT (int COR_PECA, int depth) {
        super(COR_PECA, depth);
    }
    
    @Override
    public Jogada Move(Jogo jogo, int[][] tabuleiro, String[] args) throws InterruptedException{
        initializeVariables();
        numberNodes = 1;
        transpositions=0;

        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        int opponentPiece = jogo.invertePeca(COR_PECA);
        
        for(Jogada j:jogo.listaPossiveisJogadas(COR_PECA, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            float value = -negamax(jogo, novoTabuleiro, opponentPiece, maxDepth-1, -1); 
            if(value > max)
            {
                melhorJogada = j;
                max = value;
            }
        }
        
        super.closeVariables();
        lastGamePlayed = jogo;
        lastBoardEvaluated = jogo.criaCopiaTabuleiro(tabuleiro);
        System.out.println("nodes: " + String.valueOf(numberNodes) + "\ttranspositions: " + String.valueOf(transpositions));
        return melhorJogada;
        
    }
    

    @Override
    public float negamax(Jogo game, int[][] board, int currentPiece, int depth, int sign) throws InterruptedException{ 
        numberNodes++;
        // Transposition Table Lookup; node is the lookup key for ttEntry 
        String boardHash = getHash(board, sign);
        TTEntry ttEntry = transpositionTable.get(boardHash);
        
        //para usar o valor da tabela ela deve ser igual ou mais profunda que a avaliação atual
        if (ttEntry != null && ttEntry.depth >= depth){ 
            transpositions++;
            return ttEntry.value;
        }

        if(depth == 0 || game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, -100, +100) * sign;
        }
        
        float max    = Integer.MIN_VALUE;
        int opponentPiece = game.invertePeca(currentPiece);
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            game.fazJogada(j, newBoard, false);
            max = Math.max(max, -negamax(game, newBoard, opponentPiece, depth-1, sign*-1));
        }

        ttEntry = new TTEntry();
        ttEntry.value = max;
        ttEntry.depth = depth;
        transpositionTable.put(boardHash, ttEntry);

        return max;
    }

    protected String getHash(int[][] board, int player){
        String hash = "";
        for(int i =0 ; i < 5; i++){
            for(int j = 0; j < 5; j++){
                hash += String.valueOf(board[i][j]);
            }
        }
        hash += String.valueOf(player);
        return hash;
    }

    
    @Override
    public String[] ComputeStatistics(){
        
        /* 
        NegTTTree logTree = new NegTTTree(COR_PECA, maxDepth);
        
        try{
            logTree.Move(this.lastGamePlayed, lastBoardEvaluated, null);
        }catch(InterruptedException e){

        }

        String[] logArgs = logTree.getArgs();
        String[] thisArgs = getArgs();
        String[] result = Arrays.copyOf(logArgs, logArgs.length + thisArgs.length);
        System.arraycopy(thisArgs, 0, result, logArgs.length, thisArgs.length);
        return result;
        */
        return null;
    }
    
    @Override
    public String[] getArgs(){
        return new String[]{String.valueOf(COR_PECA), this.ID, lastGamePlayed.getNome(), String.valueOf(executionTime)};
    }

    @Override
    protected void initializeVariables(){
        super.initializeVariables();
        this.transpositionTable = new HashMap<String, TTEntry>();
        
    }
}
