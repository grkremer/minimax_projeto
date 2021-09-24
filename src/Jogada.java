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
    Jogada(String textoJogada) {
        setCorPeca(stringParaPeca(textoJogada.substring(0, 1)));
        textoJogada = textoJogada.substring(3);
        String[] particoesTextoJogada = textoJogada.split(" ");
        if(textoJogada.contains("→")) {
            for(String textoMovimento : particoesTextoJogada) {
                int[][] movimento = stringParaMovimento(textoMovimento);
                getMovimentos().add(movimento);
                if(Jogo.comeuPeca(movimento)) {
                    int [] posPecaEliminada = Jogo.posPecaEliminada(movimento);
                    getPecasEliminadas().add(posPecaEliminada);
                }
            }
        }
        else {
            int[] posicao = stringParaPosicao(particoesTextoJogada[0]);
            setPosicao(posicao);
        }
    }

    private String pecaParaString(int peca) {
        switch(peca) {
            case Jogo.PECA_BRANCA:
                return "B";
            case Jogo.PECA_PRETA:
                return "P";
            case Jogo.SEM_PECA:
            default:
                return "_"; 
        }
    }
    private String logPosicao() {
        return pecaParaString(getCorPeca()) + ": (" + getPosicao()[0] + "," + getPosicao()[1] + ")";
    }
    private String logMovimentos() {
        String log;
        log = pecaParaString(getCorPeca()) + ":";
        for(int[][] movimento : getMovimentos()) {
            log += " ("+ movimento[0][0] + "," + movimento[0][1] + ")→(" + movimento[1][0] + "," + movimento[1][1] + ")";
        }
        return log;
    }
    public void printLog() {
        System.out.println(getLog());
    }

    private int stringParaPeca(String siglaPeca) {
        switch(siglaPeca) {
            case "B":
                return Jogo.PECA_BRANCA;
            case "P":
                return Jogo.PECA_PRETA;
            case "_":
            default:
                return Jogo.SEM_PECA; 
        }
    }
    private int[] stringParaPosicao(String textoPosicao) {
        int[] posicao = new int[2];
        posicao[0] = Character.getNumericValue(textoPosicao.charAt(1));
        posicao[1] = Character.getNumericValue(textoPosicao.charAt(3));
        return posicao;
    }
    private int[][] stringParaMovimento(String textoMovimento) {
        int[][] movimento = new int[2][2];
        movimento[0][0] = Character.getNumericValue(textoMovimento.charAt(1));
        movimento[0][1] = Character.getNumericValue(textoMovimento.charAt(3));
        movimento[1][0] = Character.getNumericValue(textoMovimento.charAt(7));
        movimento[1][1] = Character.getNumericValue(textoMovimento.charAt(9));
        return movimento;
    }

}
