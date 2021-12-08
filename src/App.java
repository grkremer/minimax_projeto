import jogos.*;
import jogos.util.Jogo;
import agentes.*;
import interfaces.*;

public class App {
    public static void main(String[] args) throws Exception {
        //JanelaJogo janela = new JanelaJogo(new JogoDaVelha4());
        
        Jogo jogo = new TicTackle5();  // new Rastros(obj player 1, obj player 2);
        JanelaJogo janela = new JanelaJogo(jogo);
        
        //lembrete: atualmente alphabetaTree tem mais nodos que alphabeta
        jogo.jogar(new Humano(Jogo.PECA_BRANCA, janela), new MinimaxTree(Jogo.PECA_PRETA, 5));

        //jogo.carregaLog("logs/log.txt");
        janela.replayHistoricoJogadas();
    }
}