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

public class MCTS implements IAgent{
    private final String ID = "MCTS";
    private final double DISCOUNT_COEF_STND = 0.99;

    /* USEFUL PARAMETERS */
    protected int episodes;
    protected double explorationCoeficient; //(0...1)
    protected double discountCoef; // (0...1)
    protected Boolean timeBased; // INSTEAD NUMBER OF EPISODES, GENERATE THE TREE USING TIME.
    protected double timeLimit;
    protected int agentColor;
    
    private LogArvoreMonteCarlo log;
    
    /* SPECIFIC PARAMETERS */
    private Boolean treeReuse;
    private NodeMCTS root;
    
    protected float runningTime;
    protected long startTime;
    protected long endTime;
    protected int nodeCounter;

    public MCTS(int agentColor, int episodes, double expCoeficient, Boolean timeBased, Boolean treeReuse){
        this.agentColor = agentColor;
        this.episodes   = episodes;
        this.discountCoef = DISCOUNT_COEF_STND;
        this.timeBased    = timeBased;
        this.treeReuse    = treeReuse;
        this.explorationCoeficient = expCoeficient;
        
    }

    @Override
    public Jogada Move(Jogo enviroment, int[][] board, String[] args) throws InterruptedException{ //tem que adicionar a ply
        runningTime = 0;
        startTime   = System.currentTimeMillis();  
        nodeCounter = 0;

        if(treeReuse && root != null){
            root = findRoot(enviroment, board);
        }else{
            root = new NodeMCTS(enviroment.criaCopiaTabuleiro(board), null, agentColor, 0, null);
            root.setAvailableActions(enviroment.listaPossiveisJogadas(root.getPlayerColor(), board));
        }
        
        
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
                NodeMCTS newNode     = expand(enviroment, currentNode);
                double reward        = rollout(enviroment, newNode);
                backpropagate(newNode, reward);
            }
            nodeCounter = episodes;
        }
        

        endTime = System.currentTimeMillis();
        runningTime = (endTime - startTime)/1000f;
        System.out.println("nodesNEW: " + String.valueOf(nodeCounter) + "\ttime: " + String.valueOf(runningTime) + "s");
        
        return maxChild();//bestAction;
    }

    public Jogada maxChild(){
        Jogada bestAction = null;
        double maxValue   = Integer.MIN_VALUE;

        //select best action
        for(NodeMCTS s:root.getChildren()){
            double rw = s.getQValue()/s.getNValue();
            
            if(maxValue < rw){
                maxValue   = rw;
                bestAction = s.getAction();
            }
        }
        return bestAction;
    }

    public NodeMCTS search(Jogo env, NodeMCTS node){
        if(!node.getAvailableActions().isEmpty()){
            return node;
        }
        
        
        NodeMCTS nextNode = null;
        double maxUCB1    = -100;
        
        for (NodeMCTS n: node.getChildren()){
            double UCBValue = UCB1(node.getNValue(), n.getNValue(), n.getQValue(), node.getPlayerColor());
        
            if(UCBValue > maxUCB1){
                maxUCB1  = UCBValue;
                nextNode = n;
            }
        }
        
        
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

        Jogada action    = avActions.get(0);
        node.removeAvailableAction(action);
        int playerCollor = env.invertePeca(node.getPlayerColor());
        int ply          = node.getPly() + 1;
        int[][] state    = env.criaCopiaTabuleiro(node.getState());
        env.fazJogada(action, state, false);
        
       
        NodeMCTS newNode = new NodeMCTS(state, action, playerCollor, ply, node);
        newNode.setAvailableActions(env.listaPossiveisJogadas(newNode.getPlayerColor(), state));
        node.addChild(action, newNode);
        return newNode;
    }

    public double rollout(Jogo env, NodeMCTS node) throws InterruptedException{
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

        return rewardValue(env, state, agentColor); //* discountFactor(plys);
        
    }

    public void backpropagate(NodeMCTS node, double reward){
        NodeMCTS current  = node;
        double discReward = reward;
        while(!(current == null)){
            current.incrementNValue();
            double qValue = current.getQValue();
            current.updateQValue(qValue + discReward);
            current     = current.getParent();
            discReward *= discountCoef;
        }
    }

    private double UCB1(int nValueParent, int nValue, double qValue, int parentColor){
        double exploitation = qValue/nValue;;
        double exploration  = Math.sqrt( (2*Math.log(nValueParent))/nValue );
        
        if(parentColor != agentColor){
            exploitation *= -1;
        }
        return exploitation + (explorationCoeficient * exploration);
    }

    private double rewardValue(Jogo env, int[][] state, int playerColor){
        if(env.verificaVitoria(playerColor, state)){
            return 1;
        }else if(env.verificaVitoria(env.invertePeca(playerColor), state)){
            return -1;
        }
        return 0;
        
    }

    private double discountFactor(int plys){
        return Math.pow(discountCoef, plys);
    }

    private NodeMCTS findRoot(Jogo env, int[][] state){
        ArrayList<NodeMCTS> nodes = new ArrayList<NodeMCTS>();
        nodes.add(root);
        while(!nodes.isEmpty()){
            NodeMCTS currentNode = nodes.remove(0);

            //Check if matrixes are equivalend: Java stinks
            Boolean eq = true;
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    if(currentNode.getState()[i][j]!=state[i][j]){
                        eq=false;
                        break;
                    }
                }
            }

            if( eq && currentNode.getPlayerColor() == agentColor) {
                currentNode.removeParent();
                return currentNode;
            }
            for (NodeMCTS n: ((currentNode.getChildren()))){nodes.add(n);}
        }
        
        //Not found:
        NodeMCTS newNode = new NodeMCTS(env.criaCopiaTabuleiro(state), null, agentColor, 0, null);
        newNode.setAvailableActions(env.listaPossiveisJogadas(agentColor, state));
        return newNode;
    }



    @Override
    public int getCorPeca(){ return agentColor;}
    
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
