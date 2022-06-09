package agentes;

/* COMMUM DATA STRCTURES */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.lang.model.util.ElementScanner14;

/* GAME DATA STRCURES */
import agentes.util.IAgent;
import agentes.util.INode;
import agentes.util.NodeMCTS;
import jogos.util.Jogada;
import jogos.util.Jogo;
import logging.LogArvoreMonteCarlo;


public class ParallelMCTS extends MCTS {
    /* Parallel atributes */
    private Jogo environment;
    private final int numberThreads;
    private int testing = 0;
    public ParallelMCTS(int agentColor, int episodes, double expCoeficient, final int numberThreads){
        super(agentColor, episodes, expCoeficient, false, false);
        this.numberThreads = numberThreads;
    }

    @Override
    public Jogada Move(Jogo enviroment, int[][] board, String[] args) throws InterruptedException{ //tem que adicionar a ply
        this.environment = enviroment;
        
        ArrayList<Thread2MCTS> MyBeautifulThreads = new ArrayList<Thread2MCTS>();
        int i = numberThreads;
        testing = 0;
        while(i > 0){
            i--;
            NodeMCTS root = new NodeMCTS(enviroment.criaCopiaTabuleiro(board), null, super.agentColor, 0, null);
            root.setAvailableActions(enviroment.listaPossiveisJogadas(root.getPlayerColor(), board));
            Thread2MCTS t = new Thread2MCTS(i, root);
            t.start();
            MyBeautifulThreads.add(t);
        } 
        
        //merge values
        NodeMCTS mergedRoot = new NodeMCTS(enviroment.criaCopiaTabuleiro(board), null, super.agentColor, 0, null);;
        for(Thread2MCTS tm : MyBeautifulThreads){
            tm.join();
            for(NodeMCTS ch : (tm.getRoot()).getChildren()){
                
                Boolean foundCh = false;
                for(NodeMCTS rootCh : mergedRoot.getChildren()){
                    if(rootCh.isEqual(ch)){
                        foundCh = true;
                        double qValue = rootCh.getQValue();
                        int nValue = rootCh.getNValue();
                        rootCh.updateQValue(qValue + ch.getQValue());
                        rootCh.updateNValue(nValue + ch.getNValue());
                        break;
                    }
                }
                if(!foundCh){
                    mergedRoot.addChild(ch.getAction(), ch);
                }
            }
            //Jogada j = maxChild(tm.getRoot());
            //System.out.println(tm.getRoot().getQValue()/tm.getRoot().getNValue());
        }
        System.out.println("testing: " + String.valueOf(testing));
            
        return maxChild(mergedRoot);
    }

    public synchronized void incrementValue(){
        testing+=1;
    }

    public Jogada maxChild(NodeMCTS root){
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

    public Jogo getEnvironment(){
        return environment;
    }

    public int getEpisodes(){
        return super.episodes;
    }

    private class Thread2MCTS extends Thread{
        private final int id;
        private NodeMCTS personalRoot;
        
        public Thread2MCTS(final int id, NodeMCTS personalRoot){ 
            this.id = id; 
            this.personalRoot = personalRoot;
        }
    
        public void run(){
    
            int episodes = getEpisodes();
            Jogo environment = getEnvironment();
    
            for(int e=0; e<episodes; e++){
                try{
                    incrementValue();
    
                    
                    NodeMCTS currentNode = search(environment, personalRoot);
                    NodeMCTS newNode     = expand(environment, currentNode);
                    double reward        = rollout(environment, newNode);
                    backpropagate(newNode, reward);
                }catch(InterruptedException err){
                    System.out.println("GAMBIARRA BRABA PAE");
                }
            }
            
            
            //System.out.println("FIM THREAD " + Integer.toString(id));
        }
        
        public NodeMCTS getRoot(){
            return personalRoot;
        }
    
    }
    
}
