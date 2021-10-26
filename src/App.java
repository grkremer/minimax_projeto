import jogos.*;
import jogos.util.Jogo;
import agentes.*;
import interfaces.*;

public class App {
    public static void main(String[] args) throws Exception {
        //JanelaJogo janela = new JanelaJogo(new JogoDaVelha4());
        
        Jogo jogo = new FiveFieldKono();  // new Rastros(obj player 1, obj player 2);
       
        JanelaJogo janela = new JanelaJogo(jogo);
        
        
        //jogo.jogar(new ArvoreMonteCarlo(Jogo.PECA_BRANCA, 10000, 1), new Humano(Jogo.PECA_PRETA, janela));
        jogo.jogar(new AlphaBeta(Jogo.PECA_BRANCA, 5), new Humano(Jogo.PECA_PRETA, janela));

        //jogo.carregaLog("logs/log.txt");
        janela.replayHistoricoJogadas();
    }
}