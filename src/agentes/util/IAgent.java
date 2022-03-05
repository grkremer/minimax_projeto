package agentes.util;
import jogos.util.Jogada;
import jogos.util.Jogo;

public interface IAgent {
    public Jogada Move(Jogo jogo, int[][] tabuleiro, String[] args) throws InterruptedException;
    public int getCorPeca();
    public String[] getArgs();
    public String[] ComputeStatistics();
    public String getID();

}
