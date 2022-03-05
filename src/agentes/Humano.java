package agentes;
import interfaces.JanelaJogo;

import agentes.util.IAgent;
import jogos.util.*;

public class Humano implements IAgent{
    private JanelaJogo janelaJogo;
    private int corPeca = 0;

    public JanelaJogo getJanelaJogo() {
        return janelaJogo;
    }
    public void setJanelaJogo(JanelaJogo janelaJogo) {
        this.janelaJogo = janelaJogo;
    }
    public int getCorPeca(){
        return corPeca;
    }
    public void setCorPeca(int corPeca) {
        this.corPeca = corPeca;
    }

    public Humano(int corPeca, JanelaJogo janelaJogo){
        setJanelaJogo(janelaJogo);
        setCorPeca(corPeca);
        getJanelaJogo().setCorPecaHumano(corPeca);
    }

    public Jogada Move(Jogo jogo, int[][] tabuleiro, String[] args) throws InterruptedException {
        getJanelaJogo().setHumanoJogando(true);
        while(getJanelaJogo().isHumanoJogando()) {
            Thread.sleep(1);
        }
        return getJanelaJogo().getJogadaDoHumano();
    }

    
}
