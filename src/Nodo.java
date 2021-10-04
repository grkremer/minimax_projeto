import java.util.HashMap;
import java.util.ArrayList;

public class Nodo{
    private Estado estado;
    private Nodo pai;
    private Jogada action; //ação que levou a este estado
    private int valorN;
    private double valorQ;
    
    ArrayList<Jogada> possiveisMovimentos; //todas as possibilidades de ação a partir desse estado
    private HashMap<Jogada, Nodo> filhos;
    
    Nodo(Estado estado, Nodo pai,  Jogada acao, ArrayList<Jogada> possiveisMovimentos){
        valorN = 0;
        valorQ = 0;
        filhos = new HashMap<Jogada, Nodo>();
        this.pai = pai;
        this.action = acao;
        this.possiveisMovimentos = possiveisMovimentos;
        this.estado = estado;
    }


    public void UpdateValorN(){ valorN += 1;}
    public void UpdateValorQ(double valorQ){  this.valorQ += valorQ; }
    public HashMap<Jogada, Nodo> getFilhos(){ return filhos; }
    public Estado getEstado(){ return estado; }
    public int[][] getCopiaTabuleiro(){ 
        return estado.getCopiaTabuleiro();
    }
    public Boolean filhosIsEmpty() { return filhos.isEmpty(); }
    public int quantidadeJogadas(){ return possiveisMovimentos.size(); }
    public void removePossivelAcao(Jogada acao){
        for(Jogada j: possiveisMovimentos){
            if(acao == j){
                possiveisMovimentos.remove(j);
                break;
            }
        }
    }
    public void insereNovoFilho(Jogada acao, Nodo novoFilho){
        filhos.put(acao, novoFilho);
    }
    
    public Jogada getAction() {
        return action;
    }
    public ArrayList<Jogada> getPossiveisMovimentos() {
        return possiveisMovimentos;
    }
    public Boolean possiveisMovimentosIsEmpty(){ return possiveisMovimentos.size() == 0;}
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