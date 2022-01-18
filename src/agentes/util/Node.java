package agentes.util;
import jogos.util.Jogada;
import jogos.util.Jogo;
import java.util.HashMap;
//AINDA NÃO ESTÁ SENDO USADO, SERÁ A INTERFACE PARA AS CLASSES NODOS
public interface Node {

    public int[][] getState();
    public int[][] getStateCopy();
    public HashMap<Jogada, Node> getSons();
    public double getReward();
    public Jogada getAction();
    public int getPlayerColor();
    public int getTurn();

}
