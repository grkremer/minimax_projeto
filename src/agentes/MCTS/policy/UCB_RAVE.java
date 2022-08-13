package agentes.MCTS.policy;
import java.util.ArrayList;
import agentes.MCTS.NodeMCTS;
public class UCB_RAVE extends Policy{
    int agentColor;
    double explorationCoeficient;
    final int VVALUE = 10;

    public UCB_RAVE(int agentColor, double explorationCoeficient){
        this.agentColor = agentColor;
        this.explorationCoeficient = explorationCoeficient;
    }

    private double UCB(final int nValueParent, final int nValue, final ArrayList<Double> totalQValues, final int parentColor){
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

    private double AMAF(final int AMAFNValue, final ArrayList<Double> totalAMAFQValues, final int parentColor){
        double sumAMAFQValue = totalAMAFQValues.stream().mapToDouble(a->a).sum();
        return sumAMAFQValue/AMAFNValue;
    }

    @Override
    public double select(NodeMCTS node)
    {
        double alpha = Math.max(0, this.VVALUE - node.getNValue())/Math.max(1, this.VVALUE);
        double A = AMAF(node.getAMAFNValue(), node.getTotalAMAFQValues(), node.getParent().getPlayerColor());
        double U = UCB(node.getParent().getNValue(), node.getNValue(), node.getTotalQValues(), node.getParent().getPlayerColor());
        return alpha * A + (1-alpha) * U;
    }
}
