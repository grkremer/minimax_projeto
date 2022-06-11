package agentes.MCTS.policy;
import java.util.ArrayList;

public class EGreedy extends Policy{

    private int agentColor;
    private double epsilon;
    public EGreedy(int agentColor, double epsilon){

    }

    public double run(int nValueParent, int nValue, ArrayList<Double> totalQValues, int parentColor){
        double sort = 0;
        if(sort >= epsilon) return 1;
        return 0;
    }
}
