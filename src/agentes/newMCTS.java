package agentes;

/* COMMUM DATA STRCTURES */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.lang.model.util.ElementScanner14;

/* GAME DATA STRCURES */
import agentes.util.IAgent;
import agentes.util.INode;
import agentes.util.NodeMCTS;
import jogos.util.Jogada;
import jogos.util.Jogo;
import logging.LogArvoreMonteCarlo;

public class newMCTS implements IAgent{
    private final String ID = "MCTS";
    private final double LEARNING_RATE = 0.22;
    private final String DISCOUNT_TYPE_STND = "STND";
    private final String DISCOUNT_TYPE_LOG = "LOG";
    private final String LEARN_TYPE_AVG = "AVG"; 
    private final String LEARN_TYPE_TDE = "TDE"; // estimatica = estimativa + fator de aprendizado*(recompensa da simulação - estimativa)
    private final double DISCOUNT_COEF_STND = 0.99;

    private int episodes;
    private double explorationCoeficient; //(0...1)
    private double discountCoef; // (0...1)
    private String discType; // STND OR LOG OR NONE (STANDAR OR LOGARITHM OR NONE)
    private String learnType; // AVG OR TDE (AVERAGE OR TEMPORAL DIFFERENCE ERROR)
    private Boolean timeBased; // INSTEAD NUMBER OF EPISODES, GENERATE THE TREE USING TIME.
    private double timeLimit;


    private NodeMCTS root;
    private int agentColor;
    private LogArvoreMonteCarlo log;
    
    protected float runningTime;
    protected long startTime;
    protected long endTime;
    protected int nodeCounter;


    
    public newMCTS(int agentColor, int episodes, double expCoeficient, double discountCoef, String discType, String learnType, Boolean timeBased){
        this.agentColor = agentColor;
        this.episodes = episodes;
        this.explorationCoeficient = expCoeficient;
        this.discountCoef = discountCoef;
        this.discType = discType;
        this.learnType = learnType;
        this.timeBased = timeBased;
    }

    public newMCTS(int agentColor, int episodes, double expCoeficient, String learnType, Boolean timeBased){
        this.agentColor = agentColor;
        this.episodes = episodes;
        this.explorationCoeficient = expCoeficient;
        this.discountCoef = DISCOUNT_COEF_STND;
        this.discType =  DISCOUNT_TYPE_STND;
        this.learnType = learnType;
        this.timeBased = timeBased;

    }

    public newMCTS(int agentColor, int episodes, double expCoeficient, Boolean timeBased){
        this.agentColor = agentColor;
        this.episodes = episodes;
        this.explorationCoeficient = expCoeficient;
        this.discountCoef = DISCOUNT_COEF_STND;
        this.discType =  DISCOUNT_TYPE_STND;
        this.learnType = LEARN_TYPE_AVG;
        this.timeBased = timeBased;
    }

    public Jogada Move(Jogo enviroment, int[][] board, String[] args) throws InterruptedException{ //tem que adicionar a ply
        runningTime = 0;
        startTime = System.currentTimeMillis();  
    
        nodeCounter = 0;
        root = new NodeMCTS(enviroment.criaCopiaTabuleiro(board), null, agentColor, 0, null);
        root.setAvailableActions(enviroment.listaPossiveisJogadas(root.getPlayerColor(), board));
        //root.incrementNValue();

        Jogada bestAction = null;
        double maxValue = Integer.MIN_VALUE;

        // MCTS can use time or number of episodes as searching budget
        if(timeBased){
            try{
                Double timeLimit = Double.parseDouble(args[0]);
                double currentTime = System.currentTimeMillis();
                do{
                    NodeMCTS currentNode = search(enviroment, root);
                    NodeMCTS newNode = expand(enviroment, currentNode);
                    double reward = rollout(enviroment, newNode);
                    backpropagate(newNode, reward);
                    currentTime = System.currentTimeMillis();
                    nodeCounter++;
                }while((currentTime - startTime)/1000f <= timeLimit);
            }
            catch(Exception e){}
        }
        else{
            for(int e=0; e<episodes; e++){
                NodeMCTS currentNode = search(enviroment, root);
                NodeMCTS newNode = expand(enviroment, currentNode);
                double reward = rollout(enviroment, newNode);
                backpropagate(newNode, reward);
            }
            nodeCounter = episodes;
        }
        

        //select best action
        for(NodeMCTS s:root.getChildren().values()){
            double rw;
            if(learnType==LEARN_TYPE_AVG){
                rw = s.getQValue()/s.getNValue();
            }else{
                rw = s.getQValue();
            }

            if(rw > maxValue){
                maxValue = rw;
                bestAction = s.getAction();
            }
        }

        endTime = System.currentTimeMillis();
        runningTime = (endTime - startTime)/1000f;
        System.out.println("nodesNEW: " + String.valueOf(nodeCounter) + "\ttime: " + String.valueOf(runningTime) + "s");
        
        return bestAction;
    }

    public NodeMCTS search(Jogo env, NodeMCTS node){
        if(node.getChildren().isEmpty()){
            return node;
        }
        
        
        NodeMCTS nextNode = getNextStepUCB1(node);
        //if 'nextNode' equals null, the next expansion is at the current node
        if(nextNode == null){
            return node;
        }else{
            return search(env, nextNode);
        }
        
    }

    public NodeMCTS expand(Jogo env, NodeMCTS node) throws InterruptedException{
        ArrayList<Jogada> avActions = node.getAvailableActions();
        if(avActions.isEmpty()) return node;
        Collections.shuffle(avActions);

        Jogada action = avActions.get(0);
        node.removeAvailableAction(action);
        int playerCollor = env.invertePeca(node.getPlayerColor());
        int ply = node.getPly() + 1;
        int[][] state = env.criaCopiaTabuleiro(node.getState());
        env.fazJogada(action, state, false);
        
       
        NodeMCTS newNode = new NodeMCTS(state, action, playerCollor, ply, node);
        newNode.setAvailableActions(env.listaPossiveisJogadas(newNode.getPlayerColor(), state));
        node.addChild(action, newNode);
        return newNode;
    }

    private double rollout(Jogo env, NodeMCTS node) throws InterruptedException{
        ArrayList<Jogada> actionsLst = node.getAvailableActions();
        int[][] state = env.criaCopiaTabuleiro(node.getState());
        if(actionsLst.isEmpty()){
            return rewardValue(env, node.getState(), node.getPlayerColor());
        }
        Collections.shuffle(actionsLst);
        Jogada action = actionsLst.get(0);
        int playerColor = env.invertePeca(node.getPlayerColor());
        env.fazJogada(action, state, false);

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
        }

        return rewardValue(env, state, agentColor) * discountFactor(plys);
        

    }

    private void backpropagate(NodeMCTS node, double reward){
        NodeMCTS current = node;
        double discReward = reward;
        while(!(current == null)){
            //update valor N
            current.incrementNValue();
            //update valor Q
            double qValue = current.getQValue();
              // check how MCTS should learn with the new reward  
            if(learnType == LEARN_TYPE_TDE){
                double error =  reward - qValue;
                current.updateQValue(qValue + (error * LEARNING_RATE));
            }else{
                current.updateQValue(qValue + discReward);
            }
            //get parent
            current = current.getParent();
            discReward*=discountCoef;
        }
    }

    private NodeMCTS getNextStepUCB1(NodeMCTS currentNode){
        NodeMCTS nextNode = null;
        double maxUCB1 = -100;
        if(! (currentNode.getAvailableActions().isEmpty())){
            return null;
            //double UCB1NewAction = UCB1(currentNode.getNValue()+1, 1, 0);
            //maxUCB1 = UCB1NewAction;
        }
        //if there is no children to expand search the next step
        else{
            for (NodeMCTS n: ((currentNode.getChildren()).values())){
                double nUCB1 = UCB1(currentNode.getNValue(), n.getNValue(), n.getQValue());
                if(nUCB1 > maxUCB1){
                    maxUCB1 = nUCB1;
                    nextNode = n;
                }
            }
        }

        return nextNode;
    }

    private double UCB1(int nValueFather,int nValue, double qValue){
        double exploitation;
        if(learnType == LEARN_TYPE_TDE) exploitation = qValue;
        else exploitation = qValue/nValue;
        
        double exploration = explorationCoeficient * Math.sqrt( (2*Math.log(nValueFather))/nValue );
        return exploitation + exploration;
    }

    private double rewardValue(Jogo env, int[][] state, int playerColor){
        if(env.verificaVitoria(playerColor, state)){
            return 1;
        }else if(env.verificaVitoria(env.invertePeca(playerColor), state)){
            return -1;
        }else{
            return 0; // env.geraCusto(playerColor, state, -100, +100)/100; 
        }
    }

    private double discountFactor(int plys){
        if(discType == DISCOUNT_TYPE_STND) return Math.pow(discountCoef, plys);
        else if(discType == DISCOUNT_TYPE_LOG) {
            if(plys <= 1) return 1;
            return  Math.pow(discountCoef, Math.log(plys));
            
        }
        else return 1; 
    }

    public int getCorPeca(){ return 0;}
    
    @Override
    public String[] ComputeStatistics(){
        
        log = new LogArvoreMonteCarlo();
        
        log.AvaliaArvore(root);
        String[] thisArgs = getArgs();
        return thisArgs;
    }

    @Override
    public String[] getArgs(){

        return new String[]{this.ID, String.valueOf(agentColor), String.valueOf(runningTime), String.valueOf(log.maxDepth), String.valueOf(log.mediaBranching), String.valueOf(log.numeroNodos), String.valueOf(log.maxBranching), log.maxBoard };
    }

    @Override
    public String getID(){
        return ID;
    }
}
