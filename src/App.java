import jogos.*;
import jogos.util.Jogo;

import org.junit.runner.manipulation.Alphanumeric;

import agentes.*;
import agentes.Trees.ABPruneTree;
import agentes.Trees.MinimaxTree;
import interfaces.*;
import tests.*;
public class App {
    public static void main(String[] args) throws Exception {
        //Jogo jogo = new TicTackle5();  // new Rastros(obj player 1, obj player 2);
        //JanelaJogo janela = new JanelaJogo(jogo);
        //jogo.jogar(new MinimaxTree(Jogo.PECA_BRANCA, 5), new newMCTS(Jogo.PECA_PRETA, 10000, 1/Math.sqrt(2), 0.6, "STND", "AVG", true));
        

        // *****  MUDANÇAS PARA FAZER ******
        // botar o shuffle no ABPRUNETree
        // colocar simetria opcional por parâmetro
        // tentar otimizar Jogo da velha 4 e Tick tackle
        
        // ****** RODAR TESTES *******
        
        // TESTE 1
        // Simulador s1 = new Simulador(new JogoDaVelha4(), new MinimaxTree(Jogo.PECA_BRANCA, 5), new newMCTS(Jogo.PECA_PRETA, 20000, 1/Math.sqrt(2), 0.9, "STND", "AVG", true));
        // s1.Simular("JogoDaVelha-teste1", 100);

        // TESTE 2
        // Simulador s2 = new Simulador(new JogoDaVelha4(), new ABPruneTree(Jogo.PECA_BRANCA, 5), new newMCTS(Jogo.PECA_PRETA, 20000, 1/Math.sqrt(2), 0.9, "STND", "AVG", true));
        // s2.Simular("JogoDaVelha-teste2", 100);
        
        // TESTE 3
        // Simulador s1 = new Simulador(new TicTackle5(), new MinimaxTree(Jogo.PECA_BRANCA, 5), new newMCTS(Jogo.PECA_PRETA, 20000, 1/Math.sqrt(2), 0.9, "STND", "AVG", true));
        // s1.Simular("JogoDaVelha-teste1", 100);

        // TESTE 4
        // Simulador s2 = new Simulador(new TicTackle5(), new ABPruneTree(Jogo.PECA_BRANCA, 5), new newMCTS(Jogo.PECA_PRETA, 20000, 1/Math.sqrt(2), 0.9, "STND", "AVG", true));
        // s2.Simular("JogoDaVelha-teste2", 100);
        
        
        //jogo.carregaLog("logs/log.txt");
        //janela.replayHistoricoJogadas();
    }
}