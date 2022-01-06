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
        transpositionTable = new HashMap<String, TTEntry>();
        this.COR_PECA = COR_PECA;
        this.maxDepth = profundidadeMax;
    }
    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        totalNodes = 0; 
        double max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        float alpha = -1000; //Float.NEGATIVE_INFINITY; 
        float beta  = 1000; //Float.POSITIVE_INFINITY;
        ArrayList<Jogada> possiveisJogadas = jogo.listaPossiveisJogadas(COR_PECA, tabuleiro);
        
        for(Jogada j:possiveisJogadas){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            double valor = -1*Negamax(jogo, novoTabuleiro, jogo.invertePeca(COR_PECA), maxDepth-1, -beta, -alpha);
            if(valor > max)
            {
                melhorJogada = j;
                max = valor;
            }
        }
        
        return melhorJogada;
        
    }
    
    public double Negamax(Jogo game, int[][] board, int currentPiece, int depth, double alpha, double beta) throws InterruptedException{ 
        totalNodes+=1;
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            
            return (int)game.geraCusto(COR_PECA, board, -100, +100);
        }
        
        double bestValue = Integer.MIN_VALUE;
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            game.fazJogada(j, newBoard, false);
            double tmpValue = -1*Negamax(game, newBoard, game.invertePeca(currentPiece), depth-1, -beta, -alpha);
            
            if(tmpValue > bestValue){ bestValue = tmpValue; }
            if(bestValue > alpha){ alpha = bestValue; }
            
            if (alpha >= beta) {
                cutoffs++;
                break;
            }
        }
        return alpha;
    }

    /* 
    public double Negamax(Jogo game, int[][] board, int currentPiece, int depth, double alpha, double beta) throws InterruptedException{ 
        
        // Transposition Table Lookup; node is the lookup key for ttEntry 
        String boardHash = getHash(board);
        TTEntry ttEntry = transpositionTable.get(boardHash);
        double alphaOrig = alpha;
        totalNodes++;
        //para usar o valor da tabela ela deve ser igual ou mais profunda que a avaliação atual
        if (ttEntry != null && ttEntry.depth >= depth){ 
            
            if(ttEntry.flag == Flag.EXACT){
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
            transpositions++;
            return ttEntry.value;
        }

        // negasearch
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            
            return (int)game.geraCusto(COR_PECA, board, -100, +100);
        }
        
        double value = Integer.MIN_VALUE;
         
        
        
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            game.fazJogada(j, newBoard, false);
            double tmp = -Negamax(game, newBoard, game.invertePeca(currentPiece), depth-1, -beta, -alpha);
            value = Math.max(value, tmp);
            alpha = Math.max(alpha, value);
            if (alpha >= beta) {
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
        else if(value >= beta){
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
