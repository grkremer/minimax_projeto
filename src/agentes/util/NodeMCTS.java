package agentes.util;
import jogos.util.Jogada;
import java.util.ArrayList;
import java.util.HashMap;
public class NodeMCTS implements INode {
    private NodeMCTS parent;
    private ArrayList<NodeMCTS> children;
    private int[][] state;
    private Jogada action;
    private int playerColor;
    private int ply;
    
    
    
    private ArrayList<Jogada> availableActions;
    private int nValue;
    private double qValue;
    


    public NodeMCTS(){}

    public NodeMCTS(int[][] state, Jogada action, int playerColor, int ply, NodeMCTS parent){
        this.state = state;
        this.action = action;
        this.playerColor = playerColor;
        this.ply = ply;
        this.parent = parent;
        this.availableActions = new ArrayList<Jogada>();
        this.children = new ArrayList<NodeMCTS>();
        this.nValue = 0;
        this.qValue = 0;
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

    public void updateQValue(double newQvalue){ qValue = newQvalue;}
    
    // GETTERS
    public Jogada getAction(){return action; }

    public int[][] getState(){ return state; }

    public ArrayList<NodeMCTS> getChildren(){ return children; }

    public int getPly(){ return ply; }

    public int getPlayerColor(){ return playerColor; }

    public NodeMCTS getParent(){ return parent; }
    
    public ArrayList<Jogada> getAvailableActions(){ return availableActions; }
    
    public int getNValue(){ return nValue; }
    
    public double getQValue(){ return qValue; }

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
