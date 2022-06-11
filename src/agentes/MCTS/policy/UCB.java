package agentes.MCTS.policy;
import java.util.ArrayList;

public class UCB extends Policy{
    int agentColor;
    double explorationCoeficient;

    public UCB(int agentColor, double explorationCoeficient){
        this.agentColor = agentColor;
        this.explorationCoeficient = explorationCoeficient;
    }

    @Override
    public double select(int nValueParent, int nValue, ArrayList<Double> totalQValues, int parentColor)
    {
        double qValue = totalQValues.stream()
                        .mapToDouble(a -> a)
                        .sum();
        double exploitation = qValue/nValue;;
        double exploration  = Math.sqrt( (2*Math.log(nValueParent))/nValue );
        
        if(parentColor != agentColor){
            exploitation *= -1;
        }
        return exploitation + (explorationCoeficient * exploration);
    }
}
