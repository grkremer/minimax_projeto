package agentes.MCTS.policy;
import agentes.MCTS.NodeMCTS;

public class UCB extends Policy{
    int agentColor;
    double explorationCoeficient;

    public UCB(int agentColor, double explorationCoeficient){
        this.agentColor = agentColor;
        this.explorationCoeficient = explorationCoeficient;
    }

    @Override
    public double select(NodeMCTS node)
    {
        double qValue = node.getTotalQValues().stream()
                        .mapToDouble(a -> a)
                        .sum();
        double exploitation = qValue/node.getNValue();
        double exploration  = Math.sqrt( (2*Math.log(node.getParent().getNValue()))/node.getNValue() );
        
        if(node.getParent().getPlayerColor() != agentColor){
            exploitation *= -1;
        }
        return exploitation + (explorationCoeficient * exploration);
    }
}
