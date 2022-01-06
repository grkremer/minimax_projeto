package agentes.util;


public class TTEntry {
    public double value;
    public Flag flag;
    public int depth;
    public TTEntry(){}
    public TTEntry(double value, Flag flag, int depth){
        this.value = value;
        this.flag = flag;
        this.depth = depth;
    }
}
