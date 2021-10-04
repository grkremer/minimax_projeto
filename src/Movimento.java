public class Movimento {
    public enum Acao {
        INSERE, MOVE, REMOVE;
    }

    private int corPeca;
    private int[] posicao1 = new int[2];
    private int[] posicao2 = new int[2];;
    private Acao acao;

    public int getCorPeca() {
        return corPeca;
    }
    public void setCorPeca(int corPeca) {
        this.corPeca = corPeca;
    }
    public int[] getPosicao1() {
        return posicao1;
    }
    public void setPosicao1(int[] posicao1) {
        this.posicao1 = posicao1;
    }
    public int[] getPosicao2() {
        return posicao2;
    }
    public void setPosicao2(int[] posicao2) {
        this.posicao2 = posicao2;
    }
    public Acao getAcao() {
        return acao;
    }
    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    Movimento(int corPeca, int[] posicao1, int[] posicao2, Acao acao) {
        setCorPeca(corPeca);
        setPosicao1(posicao1);
        setPosicao2(posicao2);
        setAcao(acao);
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
    private String logInsere() {
        return pecaParaString(getCorPeca()) + ": (" + getPosicao1()[0] + "," + getPosicao1()[1] + ")";
    }
    private String logMove() {
        return pecaParaString(getCorPeca()) + ": ("+ posicao1[0] + "," + posicao1[1] + ")→(" + posicao2[0] + "," + posicao2[1] + ")";
    }
    @Override
    public String toString() {
        switch(getAcao()) {
            case INSERE:
                return logInsere();
            case MOVE:
                return logMove();
            default:
                return "";
        }
    }
}
