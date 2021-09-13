import java.util.ArrayList;

public class Jogada {
    private int[] posicao;
    private ArrayList<int[][]> movimentos = new ArrayList<int[][]>();
    private int corPeca; 
    private ArrayList<int[]> pecasEliminadas = new ArrayList<int[]>();
    
    public int[] getPosicao() {
        return posicao;
    }
    public void setPosicao(int[] posicao) {
        this.posicao = posicao;
    }
    public ArrayList<int[][]> getMovimentos() {
        return movimentos;
    }
    public void setMovimentos(ArrayList<int[][]> movimentos) {
        this.movimentos = movimentos;
    }
    public int getCorPeca() {
        return corPeca;
    }
    public void setCorPeca(int corPeca) {
        this.corPeca = corPeca;
    }
    public ArrayList<int[]> getPecasEliminadas() {
        return pecasEliminadas;
    }
    public void setPecasEliminadas(ArrayList<int[]> pecasEliminadas) {
        this.pecasEliminadas = pecasEliminadas;
    }

    Jogada(int corPeca, int[] posicao) {
        setPosicao(posicao);
        setCorPeca(corPeca);
    }
    Jogada(int corPeca, int[][] movimento) {
        setCorPeca(corPeca);
        getMovimentos().add(movimento);
    }
    Jogada(int corPeca, ArrayList<int[][]> movimentos, ArrayList<int[]> pecasEliminadas) {
        setCorPeca(corPeca);
        setMovimentos(movimentos);
        setPecasEliminadas(pecasEliminadas);
    }

    public void print() {
        String log;
        if(getMovimentos().isEmpty()) {
            log = Jogo.pecaParaString(getCorPeca()) + ": (" + getPosicao()[0] + ", " + getPosicao()[1] + ")";
        }
        else {
            log = Jogo.pecaParaString(getCorPeca()) + ":";
            for(int[][] movimento : getMovimentos()) {
                log += " ("+ movimento[0][0] + ", " + movimento[0][1] + ") → (" + movimento[1][0] + ", " + movimento[1][1] + ") ";
            }
             
        }
        System.out.println(log);
    }
}
