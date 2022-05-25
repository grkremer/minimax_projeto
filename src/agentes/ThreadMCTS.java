package agentes;

import agentes.util.NodeMCTS;
import jogos.util.Jogada;
import jogos.util.Jogo;

public class ThreadMCTS extends Thread{
    private final int id;
    private NodeMCTS personalRoot;
    
    private ParallelMCTS reference;
    public ThreadMCTS(final int id, NodeMCTS personalRoot, ParallelMCTS reference){ 
        this.id = id; 
        this.personalRoot = personalRoot;
        this.reference = reference;
    }

    public void run(){

        int episodes = reference.getEpisodes();
        Jogo environment = reference.getEnvironment();

        for(int e=0; e<episodes; e++){
            try{
                NodeMCTS currentNode = reference.search(environment, personalRoot);
                NodeMCTS newNode     = reference.expand(environment, currentNode);
                double reward        = reference.rollout(environment, newNode);
                reference.backpropagate(newNode, reward);
            }catch(InterruptedException err){
                System.out.println("GAMBIARRA BRABA PAE");
            }
        }
        
        
        System.out.println("FIM THREAD " + Integer.toString(id));
    }
    
    public NodeMCTS getRoot(){
        return personalRoot;
    }

}
