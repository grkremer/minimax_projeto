package agentes.MCTS.policy;
import java.util.ArrayList;

public class UCBTuned extends Policy{
    int agentColor;
    double explorationCoeficient;

    public UCBTuned(int agentColor, double explorationCoeficient){
        this.agentColor = agentColor;
        this.explorationCoeficient = explorationCoeficient;
    }

    //TODO
    @Override
    public double select(int nValueParent, int nValue, ArrayList<Double> totalQValues, int parentColor){
        double parentLog;
        double avgPowRewards; 
        double exploitation; 
        double sampleVariance;

        double sumAvgPowRewards = 0;
        double sumAvgRewards    = 0;
        for(Double nv : totalQValues){
            sumAvgPowRewards += (Math.pow(nv, 2));
            sumAvgRewards += nv;
        }

        avgPowRewards  = sumAvgPowRewards/nValue;
        exploitation   = sumAvgRewards/nValue;
        sampleVariance = Math.max(avgPowRewards - (exploitation*exploitation), 0);
        
        if(parentColor != agentColor){
            exploitation *= -1;
        }

        parentLog = Math.max(Math.log(nValueParent), nValue);
        double exploration  =  Math.sqrt(parentLog * Math.min(1/4, sampleVariance + Math.sqrt(parentLog/nValue)));

        return exploitation  + (explorationCoeficient * exploration);
    }
}
