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

*/
public class NegTT extends Negamax{
    private final String ID = "NEGAMAXTT";
    
    protected HashMap<String, TTEntry> transpositionTable;
    protected int transpositions;
    public NegTT (int COR_PECA, int depth) {
        super(COR_PECA, depth);
    }
    
    @Override
    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
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
        String boardHash = getHash(board);
        TTEntry ttEntry = transpositionTable.get(boardHash);
        
        //para usar o valor da tabela ela deve ser igual ou mais profunda que a avaliação atual
        if (ttEntry != null && ttEntry.depth >= depth){ 
            transpositions++;
            return ttEntry.value;
            
        }

        if(depth == 0 || game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, -100, +100) * sign;
        }
        
        float max = Integer.MIN_VALUE;
        int opponentPiece = game.invertePeca(currentPiece);
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            game.fazJogada(j, newBoard, false);
            max = Math.max(max, -negamax(game, newBoard, opponentPiece, depth-1, sign*-1) * 0.99f);
        }

        ttEntry = new TTEntry();
        ttEntry.value = max;
        ttEntry.depth = depth;
        transpositionTable.put(boardHash, ttEntry);

        return max;
    }

    /*
    public float NegamaxTT(Jogo game, int[][] board, int currentPiece, int depth, float alpha, float beta, int sign) throws InterruptedException{ 
        totalNodes++;
        
        // Transposition Table Lookup; node is the lookup key for ttEntry 
        String boardHash = getHash(board);
        TTEntry ttEntry = transpositionTable.get(boardHash);
        double alphaOrig = alpha;
        //para usar o valor da tabela ela deve ser igual ou mais profunda que a avaliação atual
        if (ttEntry != null && ttEntry.depth >= depth){ 
            
            if(ttEntry.flag == Flag.EXACT){
                transpositions++;
                return ttEntry.value;
            }
        
            else if(ttEntry.flag == Flag.LOWERBOUND){
                alpha = Math.max(alpha, ttEntry.value);
            }
        
            else if(ttEntry.flag == Flag.UPPERBOUND){
                beta = Math.min(beta, ttEntry.value);
            }
        }

        if(alpha >= beta){
            cutoffs++;
            return ttEntry.value;
        }

        if(depth == 0 ||game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, -100, +100) * sign;
        }
        
        float value = Integer.MIN_VALUE;
        int opponentPiece = game.invertePeca(currentPiece);
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            game.fazJogada(j, newBoard, false);
            value = Math.max(alpha, -NegamaxTT(game, newBoard, opponentPiece, depth-1, -beta, -alpha, sign*-1) * 0.99f);
            alpha = Math.max(alpha, value);
            if (alpha >= beta) {
                cutoffs++;
                break;
            }
        }

        // Transposition Table Store; node is the lookup key for ttEntry 
        if(ttEntry == null)
            ttEntry = new TTEntry();
        
        ttEntry.value = value;
        if(value <= alphaOrig) {
            ttEntry.flag = Flag.UPPERBOUND;
        }
        else if(alpha >= beta){
            ttEntry.flag = Flag.LOWERBOUND;
        }
        else{
            ttEntry.flag = Flag.EXACT;
        }
        ttEntry.depth = depth;
        transpositionTable.put(boardHash, ttEntry);

        return value;
    }
    */

    protected String getHash(int[][] board){
        String hash = "";
        for(int i =0 ; i < 5; i++){
            for(int j = 0; j < 5; j++){
                hash += String.valueOf(board[i][j]);
            }
        }
        return hash;
    }

    public String[] ComputeStatistics(){
        NegTTTree logTree = new NegTTTree(COR_PECA, maxDepth);
        
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
