package agentes.Trees;

import jogos.util.Jogada;
import jogos.util.Jogo;
import logging.LogMinimax;
import agentes.Negamax;
import agentes.util.NodoMinimax;
/* 
    The purpose of negamaxTree is to save the tree in memory
        in order to get information about the tree.
*/
public class NegamaxTree extends Negamax{
    LogMinimax log;
    NodoMinimax root;
    
    private int numberNodes;
    public NegamaxTree(int COR_PECA, int profundidadeMax) {
        super(COR_PECA, profundidadeMax);
    }
    
    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        initializeVariables();
        root = new NodoMinimax(tabuleiro, COR_PECA, 0);
        numberNodes=1;
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        int opponentPiece = jogo.invertePeca(COR_PECA);
        int proxProfundidade = root.getProfundidade() + 1;
        for(Jogada j:jogo.listaPossiveisJogadas(COR_PECA, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro, false);
            NodoMinimax novoNodo = new NodoMinimax(novoTabuleiro, j, opponentPiece, proxProfundidade);
            root.insereNovoFilho(j, novoNodo);
            float value = -negamax(jogo, novoTabuleiro, opponentPiece, maxDepth-1, -1, novoNodo); 
            novoNodo.setRecompensa(value);
            if(value > max)
            {
                melhorJogada = j;
                max = value;
            }
        }

        closeVariables();
        log.AvaliaArvore(root);
        
        System.out.println("nodes: " + String.valueOf(numberNodes));
        return melhorJogada;
        
    }
    
    public float negamax(Jogo game, int[][] board, int currentPiece, int depth, int sign, NodoMinimax currentNode) throws InterruptedException{ 
        numberNodes++;
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, -100, +100) * sign;
        }
        
        float max = Integer.MIN_VALUE;
        int opponentPiece = game.invertePeca(currentPiece);
        int nextDepth = currentNode.getProfundidade()+1;
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            NodoMinimax newNode = new NodoMinimax(newBoard, j, opponentPiece, nextDepth);
            currentNode.insereNovoFilho(j, newNode);
            game.fazJogada(j, newBoard, false);
            float value = -negamax(game, newBoard, opponentPiece, depth-1, sign*-1, newNode);
            newNode.setRecompensa(value);
            max = Math.max(max, value * 0.99f);
        }
        
        currentNode.setRecompensa(max);
        return max;
    }

    @Override
    protected void initializeVariables(){
        super.initializeVariables();
        log = new LogMinimax();
    }
    
    @Override
    public String[] getArgs(){
        return new String[]{String.valueOf(log.numeroNodos), String.valueOf(log.maxBranching), String.valueOf(log.mediaBranching)};
    }

    @Override
    public String toString(){
        return "";
    }
}
