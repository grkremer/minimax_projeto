import jogos.*;
import jogos.util.Jogo;
import agentes.*;
import interfaces.*;

public class App {
    public static void main(String[] args) throws Exception {
        //JanelaJogo janela = new JanelaJogo(new JogoDaVelha4());
        
        Jogo jogo = new JogoDaVelha4();  // new Rastros(obj player 1, obj player 2);
        JanelaJogo janela = new JanelaJogo(jogo);
        
        
        //jogo.jogar(new ArvoreMonteCarlo(Jogo.PECA_BRANCA, 10000, 1), new Humano(Jogo.PECA_PRETA, janela));
        //jogo.jogar(new Humano(Jogo.PECA_BRANCA, janela), new MinimaxTree(Jogo.PECA_PRETA, 3));
        jogo.jogar(new Humano(Jogo.PECA_BRANCA, janela), new MinimaxTree(Jogo.PECA_PRETA, 4));

        //jogo.carregaLog("logs/log.txt");
        janela.replayHistoricoJogadas();
    }
}