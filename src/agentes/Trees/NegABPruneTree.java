package agentes.Trees;

import agentes.NegABPrune;
import jogos.util.Jogada;
import jogos.util.Jogo;
import agentes.util.NodoMinimax;
import logging.LogMinimax;


public class NegABPruneTree extends NegABPrune{
    LogMinimax log;
    NodoMinimax root;


    public NegABPruneTree (int COR_PECA, int maxDepth) {
        super(COR_PECA, maxDepth);
    }
    
    @Override
    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        initializeVariables();
        super.numberNodes=1;
        super.cutoffs=0;
        
        
        root = new NodoMinimax(tabuleiro, COR_PECA, 0);
        int proxProfundidade = root.getProfundidade() + 1;
        
        float max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        int opponentPiece = jogo.invertePeca(COR_PECA);
        
        for(Jogada j:jogo.listaPossiveisJogadas(COR_PECA, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            NodoMinimax novoNodo = new NodoMinimax(novoTabuleiro, j, opponentPiece, proxProfundidade);
            jogo.fazJogada(j, novoTabuleiro, false);
            float value = -negamax(jogo, novoTabuleiro, opponentPiece, maxDepth-1, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, novoNodo); 
            if(value > max)
            {
                melhorJogada = j;
                max = value;
            }

            root.insereNovoFilho(j, novoNodo);
            novoNodo.setRecompensa(value);
            
        }
        
        super.closeVariables();
        log.AvaliaArvore(root);
        System.out.println("nodesTree: " + String.valueOf(numberNodes) + "\tcutoffsTree: " + String.valueOf(cutoffs));
        return melhorJogada;
        
    }
    
    
    public float negamax(Jogo game, int[][] board, int currentPiece, int depth, float alpha, float beta, int sign, NodoMinimax currentNode) throws InterruptedException{ 
        int nextDepth = currentNode.getProfundidade()+1;
        
        numberNodes++;
        if(depth == 0 ||game.verificaFimDeJogo(board)){
            return game.geraCusto(COR_PECA, board, -100, +100) * sign;
        }
        
        int opponentPiece = game.invertePeca(currentPiece);
        for(Jogada j:game.listaPossiveisJogadas(currentPiece, board)){
            int[][] newBoard = game.criaCopiaTabuleiro(board);
            NodoMinimax newNode = new NodoMinimax(newBoard, j, opponentPiece, nextDepth);
            
            game.fazJogada(j, newBoard, false);
            float value = -negamax(game, newBoard, opponentPiece, depth-1, -beta, -alpha, sign*-1, newNode);
            alpha = Math.max(alpha, value * 0.99f);
            

            currentNode.insereNovoFilho(j, newNode);
            newNode.setRecompensa(value);
            
            if (alpha >= beta) {
                this.cutoffs++;
                break;
            }
        }
        
        currentNode.setRecompensa(alpha);
        return alpha;
    }

    
    @Override
    protected void initializeVariables(){
        super.initializeVariables();
        log = new LogMinimax();
    } 

    @Override
    public String[] getArgs(){
        return new String[]{String.valueOf(log.numeroNodos), String.valueOf(log.maxBranching), String.valueOf(log.mediaBranching), String.valueOf(this.cutoffs)};
    }

    @Override
    public String toString(){
        return "";
    }
}
