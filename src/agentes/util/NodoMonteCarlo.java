package agentes.util;
import java.util.HashMap;

import jogos.util.Jogada;

import java.util.ArrayList;

public class NodoMonteCarlo{
    private Estado estado;
    private NodoMonteCarlo pai;
    private Jogada action; //ação que levou a este estado
    
    private int valorN;
    private double valorQ;
    
    ArrayList<Jogada> possiveisMovimentos; //todas as possibilidades de ação a partir desse estado
    private HashMap<Jogada, NodoMonteCarlo> filhos;
    
    public NodoMonteCarlo(Estado estado, NodoMonteCarlo pai,  Jogada acao, ArrayList<Jogada> possiveisMovimentos){
        valorN = 0;
        valorQ = 0;
        filhos = new HashMap<Jogada, NodoMonteCarlo>();
        this.pai = pai;
        this.action = acao;
        this.possiveisMovimentos = possiveisMovimentos;
        this.estado = estado;
    }


    public void UpdateValorN(){ valorN += 1;}
    
    public void Learn(double sampledReward){
        double learningRate = 0.12;
        double error = sampledReward - this.valorQ;
        this.valorQ = this.valorQ + learningRate * (error);
    }


    public void UpdateValorQ(double valorQ){  this.valorQ += valorQ; }
    public HashMap<Jogada, NodoMonteCarlo> getFilhos(){ return filhos; }
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
    public void insereNovoFilho(Jogada acao, NodoMonteCarlo novoFilho){
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
    public NodoMonteCarlo getPai() {
        return pai;
    }

    public String getHashBoard(){
        int[][] board = estado.getCopiaTabuleiro();
        String output = "";
        for(int i =0; i<5; i++){
            for(int j =0; j <5; j++){
                output+= board[i][j];
            }
            output+='\t';
        }
        return output;
    }

}