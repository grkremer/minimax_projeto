import jogos.*;
import jogos.util.Jogo;

import org.junit.runner.manipulation.Alphanumeric;

import agentes.*;
import agentes.MCTS.EnsembleMCTS;
import agentes.MCTS.ParallelLeafMCTS;
import agentes.MCTS.MCTS;
import agentes.Trees.ABPruneTree;
import agentes.Trees.MinimaxTree;
import interfaces.*;
import tests.*;
public class App {
    public static void main(String[] args) throws Exception {
        //Jogo jogo = new TicTackle5();  // new Rastros(obj player 1, obj player 2);
        //JanelaJogo janela = new JanelaJogo(jogo);
        //jogo.jogar(new MinimaxTree(Jogo.PECA_BRANCA, 5), new newMCTS(Jogo.PECA_PRETA, 10000, 1/Math.sqrt(2), 0.6, "STND", "AVG", true));
        
        Simulador s = new Simulador(new Alquerque(), new MCTS(0, Jogo.PECA_BRANCA, 5000, 1, false, false), new MCTS(1, Jogo.PECA_PRETA, 5000, 1, false, false));
        s.Simular("teste-mMCTS", 30);
        
        
    }
}