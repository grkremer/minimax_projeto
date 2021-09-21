public class ArvoreMonteCarlo {
    private int vitorias = 0;
    private int jogadas = 0;
    private Jogada jogada;

    public int getVitorias() {
        return vitorias;
    }
    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }
    public int getJogadas() {
        return jogadas;
    }
    public void setJogadas(int jogadas) {
        this.jogadas = jogadas;
    }
    public Jogada getJogada() {
        return jogada;
    }
    public void setJogada(Jogada jogada) {
        this.jogada = jogada;
    }

    /*public ArvoreMonteCarlo(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int maximoJogadas) {
        for(int i=0; i < maximoJogadas; i++) {
            ArvoreMonteCarlo nodo = selecionaNodo();
            expandeNodo();
            simulaJogo();
            propagaResultado();
        }
    }

    private float calculaUCB() {

    }
    private float calculaMCTS() {
        return getVitorias()/getJogadas();
    }
    private float calculaUCT() {
        return calculaMCTS() + calculaUCB(); 
    }

    private void buscaMonteCarlo(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual) {
        if()
    }*/
}
