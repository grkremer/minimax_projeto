import jogos.*;
import jogos.util.Jogo;
import agentes.*;
import interfaces.*;

public class App {
    public static void main(String[] args) throws Exception {
        //JanelaJogo janela = new JanelaJogo(new JogoDaVelha4());
        
        Jogo jogo = new Rastros();
        JanelaJogo janela = new JanelaJogo(jogo);
        
        
        //jogo.jogar(new ArvoreMonteCarlo(Jogo.PECA_BRANCA, 10000, 1), new Humano(Jogo.PECA_PRETA, janela));
        jogo.jogar(new Minimax(Jogo.PECA_BRANCA, 16), new ArvoreMonteCarlo(Jogo.PECA_PRETA, 10000, 1));

        //jogo.carregaLog("logs/log.txt");
        janela.replayHistoricoJogadas();
    }
}