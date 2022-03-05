package agentes.Trees;

import jogos.util.Jogada;

import agentes.util.TTEntry;
import jogos.util.Jogo;
import logging.LogMinimax;
import agentes.NegTT;
import agentes.util.NodoMinimax;
/* 
    The purpose of negamaxTree is to save the tree in memory
        in order to get information about the tree.
*/

public class NegTTTree extends NegTT{
    LogMinimax log;
    NodoMinimax root;
    public NegTTTree (int COR_PECA, int profundidadeMax) {
        super(COR_PECA, profundidadeMax);
    }
    
    @Override
    public Jogada Move(Jogo jogo, int[][] tabuleiro, String[] args) throws InterruptedException{
        initializeVariables();
        numberNodes = 1;
        transpositions=0;

        root = new NodoMinimax(tabuleiro, COR_PECA, 0);

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
        
        super.closeVariables();
        log.AvaliaArvore(root);
        System.out.println("nodesTT: " + String.valueOf(numberNodes) + "\ttranspositionsTT: " + String.valueOf(transpositions));
        
        return melhorJogada;
        
    }
    

    public float negamax(Jogo game, int[][] board, int currentPiece, int depth, int sign, NodoMinimax currentNode) throws InterruptedException{ 
        
        numberNodes++;
        
        String boardHash = super.getHash(board);
        TTEntry ttEntry = transpositionTable.get(boardHash);
        
        //para usar o valor da tabela ela deve ser igual ou mais profunda que a avaliação atual
        if (ttEntry != null && ttEntry.depth >= depth){ 
            transpositions++;
            return ttEntry.value;
            
        }
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, -100, +100) * sign;
        }
        
        //float max = Integer.MIN_VALUE;
        float max = Integer.MIN_VALUE;
        
        int opponentPiece = game.invertePeca(currentPiece);
        int nextDepth = currentNode.getProfundidade()+1;
        
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            NodoMinimax newNode = new NodoMinimax(newBoard, j, opponentPiece, nextDepth);
            game.fazJogada(j, newBoard, false);
            float value = -negamax(game, newBoard, opponentPiece, depth-1, sign*-1, newNode);
            max = Math.max(max, value* 0.99f);
            newNode.setRecompensa(value);
            currentNode.insereNovoFilho(j, newNode);
            
        }
        ttEntry = new TTEntry();
        ttEntry.value = max;
        ttEntry.depth = depth;
        transpositionTable.put(boardHash, ttEntry);
        currentNode.setRecompensa(max);
        return max;
    }

    @Override
    protected void initializeVariables(){
        super.initializeVariables();
        log = new LogMinimax();
        transpositions = 0;
    }
    
    public String[] getArgs(){
        return new String[]{String.valueOf(log.numeroNodos), String.valueOf(log.maxBranching), String.valueOf(log.mediaBranching), String.valueOf(transpositions)};
    }

    @Override
    public String toString(){
        return "";
    }
}
