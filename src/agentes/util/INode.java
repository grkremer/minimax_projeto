package agentes.util;
import jogos.util.Jogada;
import jogos.util.Jogo;
import java.util.HashMap;
public interface INode {

    public int[][] getState();
    public Jogada getAction();
    public int getPlayerColor();
    public int getPly();

}
