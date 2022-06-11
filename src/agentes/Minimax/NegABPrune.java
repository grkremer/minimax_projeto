package agentes.Minimax;

import jogos.util.Jogada;
import jogos.util.Jogo;
import java.util.Arrays;
/* 
    Negamax OK
    negamaxtree OK
    logs INCOMPLETO
*/
public class NegABPrune extends Negamax{
    private final String ID = "NEGAMAXABPRUNE";
  
    protected int cutoffs;

    public NegABPrune (int COR_PECA, int profundidadeMax) {
        super(COR_PECA, profundidadeMax);
        
    }
    
    @Override
    public Jogada Move(Jogo jogo, int[][] tabuleiro, String[] args){
        super.initializeVariables();
        super.numberNodes=1;
        cutoffs=0;
        
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        int opponentPiece = jogo.invertePeca(COR_PECA);
        
        for(Jogada j:jogo.listaPossiveisJogadas(COR_PECA, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            float value = -negamax(jogo, novoTabuleiro, opponentPiece, maxDepth-1, Integer.MIN_VALUE, Integer.MAX_VALUE, -1); 
            if(value > max)
            {
                melhorJogada = j;
                max = value;
            }
        }
        
        super.closeVariables();
        lastGamePlayed = jogo;
        lastBoardEvaluated = jogo.criaCopiaTabuleiro(tabuleiro);
        System.out.println("nodes: " + String.valueOf(numberNodes) + "\tcutoffs: " + String.valueOf(cutoffs));
        return melhorJogada;
        
    }
    
    public float negamax(Jogo game, int[][] board, int currentPiece, int depth, float alpha, float beta, int sign){ 
        super.numberNodes++;
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, -100, +100) * sign;
        }
        
        int opponentPiece = game.invertePeca(currentPiece);
        float value = Integer.MIN_VALUE;
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            game.fazJogada(j, newBoard, false);
            value  = Math.max(value, -negamax(game, newBoard, opponentPiece, depth-1, -beta, -alpha, sign*-1));
            alpha  = Math.max(alpha, value);
            if (alpha >= beta) {
                cutoffs++;
                break;
            }
        }
        return value;
    }

    @Override
    public String[] ComputeStatistics(){
        return null;
    }

    @Override
    public String[] getArgs(){
        return new String[]{String.valueOf(COR_PECA), this.ID, lastGamePlayed.getNome(), String.valueOf(executionTime)+"s"};
    }

}
