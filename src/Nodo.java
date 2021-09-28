import java.util.HashMap;
import java.util.List;

public class Nodo{
    private int[][] estado;
    private Nodo pai;
    private Jogada action; //ação que levou a este estado
    private int valorN;
    private double valorQ;
    
    List<Jogada> possiveisMovimentos; //todas as possibilidades de ação a partir desse estado
    private HashMap<Jogada, Nodo> filhos;
    
    Nodo(int[][] estado, Nodo pai,  Jogada acao, List<Jogada> possiveisMovimentos){
        valorN = 0;
        valorQ = 0;
        filhos = new HashMap<Jogada, Nodo>();
    }


    public void UpdateValorN(){ valorN += 1;}
    public void UpdateValorQ(double valorQ){  this.valorQ += valorQ; }
    public HashMap<Jogada, Nodo> getFilhos(){ return filhos; }
    public int[][] getEstado(){ return estado; }
    public int[][] getCopiaEstado(){ return new int[5][5]; }
    public Boolean filhosIsEmpty() { return filhos.isEmpty(); }
    public int quantidadeJogadas(){ return possiveisMovimentos.size(); }
    
    
    
    public Jogada getAction() {
        return action;
    }
    public List<Jogada> getPossiveisMovimentos() {
        return possiveisMovimentos;
    }
    public int getValorN() {
        return valorN;
    }
    public double getValorQ() {
        return valorQ;
    }
    public Nodo getPai() {
        return pai;
    }
}