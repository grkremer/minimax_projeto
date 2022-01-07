package agentes;

import java.util.ArrayList;
import java.util.HashMap;

import agentes.util.Agente;
import agentes.util.TTEntry;
import agentes.util.Flag;
import jogos.util.Jogada;
import jogos.util.Jogo;
public class ABPruneTT implements Agente{
    HashMap<String, TTEntry> transpositionTable;
    int maxDepth;
    int COR_PECA;
    int transpositions = 0;
    int totalNodes = 0;
    int cutoffs = 0;
    public ABPruneTT (int COR_PECA, int profundidadeMax) {
        this.COR_PECA = COR_PECA;
        this.maxDepth = profundidadeMax;
    }
    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        totalNodes = 1; 
        transpositionTable = new HashMap<String, TTEntry>();
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        int opponentPiece = jogo.invertePeca(COR_PECA);
        for(Jogada j:jogo.listaPossiveisJogadas(COR_PECA, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            //float value = -Negamax(jogo, novoTabuleiro, opponentPiece, maxDepth-1, -1); 
            float value = -NegamaxTT(jogo, novoTabuleiro, opponentPiece, maxDepth-1, Integer.MIN_VALUE, Integer.MAX_VALUE, -1); 
            if(value > max)
            {
                melhorJogada = j;
                max = value;
            }
        }
        
        return melhorJogada;
        
    }
    
    public float Negamax(Jogo game, int[][] board, int currentPiece, int depth, int sign) throws InterruptedException{ 
        totalNodes+=1;
        
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, -100, +100) * sign;
        }
        
        float max = Integer.MIN_VALUE;
        int opponentPiece = game.invertePeca(currentPiece);
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            game.fazJogada(j, newBoard, false);
            max = Math.max(max, -Negamax(game, newBoard, opponentPiece, depth-1, sign*-1) * 0.99f);
        }
        return max;
    }

    public float Negamax(Jogo game, int[][] board, int currentPiece, int depth, float alpha, float beta, int sign) throws InterruptedException{ 
        totalNodes+=1;
        
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, -100, +100) * sign;
        }
        
        //float max = Integer.MIN_VALUE;
        int opponentPiece = game.invertePeca(currentPiece);
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            game.fazJogada(j, newBoard, false);
            alpha = Math.max(alpha, -Negamax(game, newBoard, opponentPiece, depth-1, -beta, -alpha, sign*-1) * 0.99f);
            
            if (alpha >= beta) {
                cutoffs++;
                break;
            }
        }
        return alpha;
    }

    public float NegamaxTT(Jogo game, int[][] board, int currentPiece, int depth, float alpha, float beta, int sign) throws InterruptedException{ 
        totalNodes+=1;
        // Transposition Table Lookup; node is the lookup key for ttEntry 
        String boardHash = getHash(board);
        TTEntry ttEntry = transpositionTable.get(boardHash);
        double alphaOrig = alpha;
        totalNodes++;
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

   public int getProfundidade(){ return maxDepth; }
    public int getCorPeca(){ return COR_PECA; }
    private String getHash(int[][] board){
        String hash = "";
        for(int i =0 ; i < 5; i++){
            for(int j = 0; j < 5; j++){
                hash += String.valueOf(board[i][j]);
            }
        }
        return hash;
    }
    
    @Override
    public String toString(){
        return "transpositions: " + transpositions + "\ncutoffs: " + cutoffs +  "\ntotal nodes: " + totalNodes;
    }
}
