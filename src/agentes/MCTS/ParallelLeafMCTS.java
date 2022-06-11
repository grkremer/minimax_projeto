package agentes.MCTS;

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
import jogos.util.Jogada;
import jogos.util.Jogo;
import logging.LogArvoreMonteCarlo;


public class ParallelLeafMCTS extends MCTS {
    /* Parallel atributes */
    private final int numberThreads;
    
    public ParallelLeafMCTS(int agentColor, int episodes, double expCoeficient, final int numberThreads){
        super(0, agentColor, episodes, expCoeficient, false, false);
        this.numberThreads = numberThreads;
    }

    @Override
    public double rollout(Jogo env, NodeMCTS node){
        double rewards = 0;
        
        ArrayList<ThreadMCTS> MyBeautifulThreads = new ArrayList<ThreadMCTS>();
        for(int i = 0; i < numberThreads;i++){
            ThreadMCTS t = new ThreadMCTS(env, node);
            t.start();
            MyBeautifulThreads.add(t);
        }

        for(ThreadMCTS tm : MyBeautifulThreads){
            try{
                tm.join();
                rewards += tm.getReward();
            }
            catch(InterruptedException e)
            {
                System.out.println("Preempted thread");
            }
        }

        return rewards/numberThreads;
    
    }

    private class ThreadMCTS extends Thread{
        
        Jogo env;
        NodeMCTS node;
        double result;
        public ThreadMCTS(Jogo env, NodeMCTS node){ 
            this.env = env;
            this.node = node;
            this.result = 0;    
        }
        
        public void run(){
    
            ArrayList<Jogada> actionsLst = node.getAvailableActions();
            int[][] state = env.criaCopiaTabuleiro(node.getState());
            result = 0;
            if(actionsLst.isEmpty()){
                result = rewardValue(env, node.getState(), node.getPlayerColor());
                
            }
            else{

                Collections.shuffle(actionsLst);
                Jogada action = actionsLst.get(0);
                int playerColor = env.invertePeca(node.getPlayerColor());
                env.fazJogada(action, state, false);

                int plys = 0;
                while(!env.verificaFimDeJogo(state) && plys < 100 ){
                    plys++;
                    actionsLst = env.listaPossiveisJogadas(playerColor, state);
                    if(actionsLst.isEmpty()){
                        result = rewardValue(env, state, playerColor);
                        break;
                    }

                    Collections.shuffle(actionsLst);
                    action = actionsLst.get(0);
                    env.fazJogada(action, state, false);
                    playerColor = env.invertePeca(playerColor);
                }
            }
        }

        public double getReward(){ return result; }
    }

    
    
}
