import java.util.ArrayList;

public class Jogada {
    private int[] posicao;
    private ArrayList<int[][]> movimentos = new ArrayList<int[][]>();
    private int corPeca; 
    private ArrayList<int[]> pecasEliminadas = new ArrayList<int[]>();
    private String log;
    
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
    public String getLog() {
        return log;
    }
    public void setLog(String log) {
        this.log = log;
    }

    Jogada(int corPeca, int[] posicao) {
        setPosicao(posicao);
        setCorPeca(corPeca);
        setLog(logPosicao());
    }
    Jogada(int corPeca, int[][] movimento) {
        setCorPeca(corPeca);
        getMovimentos().add(movimento);
        setLog(logMovimentos());
    }
    Jogada(int corPeca, ArrayList<int[][]> movimentos, ArrayList<int[]> pecasEliminadas) {
        setCorPeca(corPeca);
        setMovimentos(movimentos);
        setPecasEliminadas(pecasEliminadas);
        setLog(logMovimentos());
    }

    private String logPosicao() {
        return Jogo.pecaParaString(getCorPeca()) + ": (" + getPosicao()[0] + ", " + getPosicao()[1] + ")";
    }

    private String logMovimentos() {
        String log;
        log = Jogo.pecaParaString(getCorPeca()) + ":";
        for(int[][] movimento : getMovimentos()) {
            log += " ("+ movimento[0][0] + ", " + movimento[0][1] + ") → (" + movimento[1][0] + ", " + movimento[1][1] + ") ";
        }
        return log;
    }

    public void printLog() {
        System.out.println(getLog());
    }
}
