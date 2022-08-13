package agentes.MCTS;

/* COMMUM DATA STRCTURES */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.lang.model.util.ElementScanner14;

/* GAME DATA STRCURES */
import agentes.MCTS.policy.UCB_RAVE;
import jogos.util.Jogada;
import jogos.util.Jogo;

public class MCTS_RAVE extends MCTS{
    private final String ID = "MCTS RAVE";
    private final int VVALUE = 10;
    
    private HashSet<Jogada> playoutMoves;

    public MCTS_RAVE(int agentColor, int episodes, double expCoeficient, Boolean timeBased, Boolean treeReuse){
        super(new UCB_RAVE(agentColor, expCoeficient), agentColor, episodes, expCoeficient, timeBased, treeReuse);
    }

    public MCTS_RAVE(int agentColor, int episodes){
        super(new UCB_RAVE(agentColor, 1), agentColor, episodes, 1, false, false);
    }

    public double rollout(Jogo env, NodeMCTS node){
        ArrayList<Jogada> actionsLst = node.getAvailableActions();
        int[][] state = env.criaCopiaTabuleiro(node.getState());
        if(actionsLst.isEmpty()){
            return rewardValue(env, node.getState(), node.getPlayerColor());
        }

        Collections.shuffle(actionsLst);
        Jogada action = actionsLst.get(0);
        int playerColor = env.invertePeca(node.getPlayerColor());
        env.fazJogada(action, state, false);

        playoutMoves.add(action); //
        
        int plys = 0; 
        while(!env.verificaFimDeJogo(state) && plys < 100 ){
            plys++;
            actionsLst = env.listaPossiveisJogadas(playerColor, state);
            if(actionsLst.isEmpty()){
                return rewardValue(env, state, playerColor);
            }
            
            Collections.shuffle(actionsLst);
            action = actionsLst.get(0);
            env.fazJogada(action, state, false);
            playerColor = env.invertePeca(playerColor);
        
            playoutMoves.add(action); //
            
        }

        return rewardValue(env, state, agentColor);
    }

    public void backpropagate(NodeMCTS node, double reward){
        ArrayList<NodeMCTS> pathNodes = new ArrayList<NodeMCTS>();
        HashSet<NodeMCTS> visitedNodes = new HashSet<NodeMCTS>();

        NodeMCTS current  = node;
        double discReward = reward;

        // NORMAL BACKPROB
        while(!(current.getParent() == null)){
            playoutMoves.add(current.getAction());
            
            current.incrementNValue();
            double qValue = current.getQValue();
            current.updateQValue(qValue + discReward);
            current     = current.getParent();
            discReward *= discountCoef;
        }

        pathNodes.add(current);
        // UPDATE AMAF VALUES
        while(pathNodes.size() < 0){
            current = pathNodes.get(0);
            pathNodes.remove(0);
            
            if(playoutMoves.contains(current.getAction())){
                current.updateAMAFValues(reward);
            }
            
            for(NodeMCTS ch:current.getChildren()){
                if(visitedNodes.contains(ch))
                    continue;
                pathNodes.add(ch);
            }
            
            visitedNodes.add(current);
        }

    }


}
