package agentes.util;
import jogos.util.Jogada;

public class TTEntry {
    
    public enum Flag{LOWERBOUND, UPPERBOUND, EXACT};
    
    public float value; //zobrist (?)
    public Flag flag;
    public int depth;
    public Jogada bestMove;

    public TTEntry(){}
    public TTEntry(float value, Flag flag, int depth){
        this.value = value;
        this.flag = flag;
        this.depth = depth;
        this.bestMove = null;
    }

    public TTEntry(float value, Flag flag, int depth, Jogada bestMove){
        this.value = value;
        this.flag = flag;
        this.depth = depth;
        this.bestMove = bestMove;
    }
    
}
