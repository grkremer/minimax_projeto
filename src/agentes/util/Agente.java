package agentes.util;
import jogos.util.Jogada;
import jogos.util.Jogo;

public interface Agente {
    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException;
    public int getCorPeca();

}
