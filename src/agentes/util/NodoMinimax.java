package agentes.util;
import java.util.HashMap;
import java.util.Map;
import jogos.util.Jogada;

public class NodoMinimax{
    private int[][] estado;
    private Jogada action;
    private HashMap<Jogada, NodoMinimax> filhos;
    private int corPeca;
    private int profundidadeNodo;
    private float recompensa;
    
    public NodoMinimax(int[][] estado, int corPeca, int profundidadeNodo){
        this.estado = estado;
        this.corPeca = corPeca;
        this.profundidadeNodo  = profundidadeNodo;
        filhos = new HashMap<Jogada, NodoMinimax>();
    }
    public NodoMinimax(int[][] estado, Jogada action, int corPeca, int profundidadeNodo){
        this.estado = estado;
        this.action = action;
        this.corPeca = corPeca;
        this.profundidadeNodo = profundidadeNodo;
        filhos = new HashMap<Jogada, NodoMinimax>();
    }

    public HashMap<Jogada, NodoMinimax> getFilhos(){ return filhos; }
    public int[][] getCopiaEstado(){ 
        int[][] cpy = new int[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5;j++){
                cpy[i][j] = estado[i][j];
            }
        }
        return cpy; 
    }

    public float getMenorRecompensaFilhos(){
        float menorRecompensa = Integer.MAX_VALUE;
        for (Map.Entry<Jogada, NodoMinimax> pair : filhos.entrySet()) {
            float recompensa = pair.getValue().getRecompensa();
            if(menorRecompensa > recompensa){
                menorRecompensa = recompensa;
            }
        }

        return menorRecompensa;
    }
    
    public float getMaiorRecompensaFilhos(){
        float maiorRecompensa = Integer.MIN_VALUE;
        for (Map.Entry<Jogada, NodoMinimax> pair : filhos.entrySet()) {
            float recompensa = pair.getValue().getRecompensa();
            if(maiorRecompensa < recompensa){
                maiorRecompensa = recompensa;
            }
        }

        return maiorRecompensa;
    }

    public Jogada getMelhorJogada(){
        float maiorRecompensa = Integer.MIN_VALUE;
        Jogada melhorJogada = null;
        for (Map.Entry<Jogada, NodoMinimax> pair : filhos.entrySet()) {
            float recompensa = pair.getValue().getRecompensa();
            if(maiorRecompensa < recompensa){
                maiorRecompensa = recompensa;
                melhorJogada = pair.getKey();
            }
        }

        return melhorJogada;
    }

    public void insereNovoFilho(Jogada acao, NodoMinimax novoFilho){
        filhos.put(acao, novoFilho);
    }
    
    public void setRecompensa(float recompensa){
        this.recompensa = recompensa;
    }

    public Jogada getAction() {
        return action;
    }

    public int getCorPeca(){
        return corPeca;
    }
    
    public int[][] getEstado(){
        return estado;
    }

    public int getProfundidade(){
        return profundidadeNodo;
    }

    public float getRecompensa(){
        return recompensa;
    }
}