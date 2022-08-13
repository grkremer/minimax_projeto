package agentes.MCTS;
import jogos.util.Jogada;
import java.util.ArrayList;

import agentes.util.INode;
public class NodeMCTS implements INode {
    private NodeMCTS parent;
    private ArrayList<NodeMCTS> children;
    private int[][] state;
    private Jogada action;
    private int    playerColor;
    private int    ply;
    private ArrayList<Jogada> availableActions;
    private int    nValue;
    private double qValue;
    private ArrayList<Double> totalQValues;
    
    /* AMAF VALUES FOR RAPID VALUE ESTIMATION ALGORITHM */ 
    private ArrayList<Double> totalAMAFQValues;
    private int AMAFNValue;
    
    public NodeMCTS(){}

    public NodeMCTS(int[][] state, Jogada action, int playerColor, int ply, NodeMCTS parent){
        this.state       = state;
        this.action      = action;
        this.playerColor = playerColor;
        this.ply    = ply;
        this.parent = parent;
        this.availableActions = new ArrayList<Jogada>();
        this.children         = new ArrayList<NodeMCTS>();
        this.nValue = 0;
        this.qValue = 0;
        totalQValues     = new ArrayList<Double>();
        totalAMAFQValues = new ArrayList<Double>();

    }

    public void removeAvailableAction(Jogada j){
        availableActions.remove(j);
    }

    public String getHashBoard(){
        String output = "";
        for(int i =0; i<5; i++){
            for(int j =0; j <5; j++){
                output+= state[i][j];
            }
            output+='\t';
        }
        return output;
    }

    public void addChild(Jogada action, NodeMCTS newChild){ children.add(newChild);}
    
    public void incrementNValue(){nValue+=1;}

    //TODO: OLHAR ISSO
    public void updateQValue(double newQvalue){ 
        totalQValues.add(newQvalue);
        qValue = newQvalue;
    }

    public void updateAMAFValues(double reward)
    {
        totalAMAFQValues.add(reward);
        AMAFNValue+=1;
    }

    public void updateNValue(int newNvalue){ 
        nValue = newNvalue;
    }
    
    public Boolean isEqual(NodeMCTS node){
        //Check if matrixes are equivalend: Java stinks
        Boolean eq = true;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(node.getState()[i][j]!=state[i][j]){
                    eq=false;
                    break;
                }
            }
        }

        if( eq && node.getPlayerColor() == this.getPlayerColor()) {
            return true;
        }else{
            return false;
        }
    }
    // GETTERS
    public Jogada getAction(){return action; }

    public int[][] getState(){ return state; }

    public ArrayList<NodeMCTS> getChildren(){ return children; }

    public int getPly(){ return ply; }

    public int getPlayerColor(){ return playerColor; }

    public NodeMCTS getParent(){ return parent; }
    
    public ArrayList<Jogada> getAvailableActions(){ return availableActions; }
    
    public int getNValue(){ return nValue; }

    public int getAMAFNValue(){ return AMAFNValue; }
    
    public double getQValue(){ return qValue; }

    public ArrayList<Double> getTotalQValues(){ return totalQValues;}

    public ArrayList<Double> getTotalAMAFQValues(){ return totalAMAFQValues; }

    // SETTERS
    public void setAvailableActions(ArrayList<Jogada> avActions){ availableActions = avActions; }

    public void setQValue(double qValue){ this.qValue = qValue;}

    public void removeParent(){ this.parent = null; }

    @Override
    public String toString(){
        String out = "";
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                out += Integer.toString(state[i][j]) + "";
            }
            out += "|";
        }
        return out;
    }
    
}
