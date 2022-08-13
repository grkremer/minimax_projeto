package agentes.MCTS;

/* COMMUM DATA STRCTURES */
import java.util.ArrayList;

/* GAME DATA STRCURES */
import jogos.util.Jogada;
import jogos.util.Jogo;


public class EnsembleMCTS extends MCTS {
    /* Parallel atributes */
    private Jogo environment;
    private final int numberThreads;
    
    public EnsembleMCTS(int agentColor, int episodes, double expCoeficient, final int numberThreads){
        super(0, agentColor, episodes, expCoeficient, false, false);
        this.numberThreads = numberThreads;
    }

    @Override
    public Jogada Move(Jogo enviroment, int[][] board, String[] args){ //tem que adicionar a ply
        runningTime = 0;
        startTime   = System.currentTimeMillis();  
        nodeCounter = 0;

        
        this.environment = enviroment;
        ArrayList<ThreadMCTS> MyBeautifulThreads = new ArrayList<ThreadMCTS>();
        int chunkEpisodes = (int)Math.ceil(episodes/numberThreads);
        for(int i = 0; i < numberThreads;i++){
            NodeMCTS root = new NodeMCTS(enviroment.criaCopiaTabuleiro(board), null, super.agentColor, 0, null);
            root.setAvailableActions(enviroment.listaPossiveisJogadas(root.getPlayerColor(), board));
            ThreadMCTS t = new ThreadMCTS(root, chunkEpisodes);
            t.start();
            MyBeautifulThreads.add(t);
        } 
        
        //merge values
        NodeMCTS mergedRoot = new NodeMCTS(enviroment.criaCopiaTabuleiro(board), null, super.agentColor, 0, null);;
        for(ThreadMCTS tm : MyBeautifulThreads){
            try{
                tm.join();
            
                for(NodeMCTS ch : (tm.getRoot()).getChildren()){
                    Boolean foundCh = false;
                    for(NodeMCTS rootCh : mergedRoot.getChildren()){
                        if(rootCh.isEqual(ch)){
                            foundCh       = true;
                            double qValue = rootCh.getQValue();
                            int nValue    = rootCh.getNValue();
                            rootCh.updateQValue(qValue + ch.getQValue());
                            rootCh.updateNValue(nValue + ch.getNValue());
                            break;
                        }
                    }
                    if(!foundCh){
                        mergedRoot.addChild(ch.getAction(), ch);
                    }
                }
            }catch(InterruptedException e){
                System.out.println("Thread failed");
            }

        }
        endTime = System.currentTimeMillis();
        runningTime = (endTime - startTime)/1000f;
        System.out.println("nodesNEW: " + String.valueOf(nodeCounter) + "\ttime: " + String.valueOf(runningTime) + "s");
        

        return maxChild(mergedRoot);
    }

    public Jogo getEnvironment(){
        return environment;
    }

    public int getEpisodes(){
        return super.episodes;
    }

    private class ThreadMCTS extends Thread{
        private NodeMCTS personalRoot;
        private int chunkEpisodes;
        
        public ThreadMCTS(NodeMCTS personalRoot, int chunkEpisodes){ 
            this.personalRoot = personalRoot;
            this.chunkEpisodes = chunkEpisodes;
        }
    
        public void run(){
    
            Jogo environment = getEnvironment();
            System.out.println(String.valueOf(chunkEpisodes));
            for(int i = 0; i < chunkEpisodes; i++){
                NodeMCTS currentNode = search(environment, personalRoot);
                NodeMCTS newNode     = expand(environment, currentNode);
                double reward        = rollout(environment, newNode);
                backpropagate(newNode, reward);
            }
        }
        
        public NodeMCTS getRoot(){
            return personalRoot;
        }
    
    }
    
}
