package agentes.util;


public class TTEntry {
    
    public enum Flag{LOWERBOUND, UPPERBOUND, EXACT};
    
    public float value; //zobrist (?)
    public Flag flag;
    public int depth;
    public TTEntry(){}
    public TTEntry(float value, Flag flag, int depth){
        this.value = value;
        this.flag = flag;
        this.depth = depth;
    }
    
}
