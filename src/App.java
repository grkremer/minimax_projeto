import jogos.*;
import jogos.util.Jogo;
import agentes.*;
import interfaces.*;
import tests.*;
public class App {
    public static void main(String[] args) throws Exception {
        
        //Jogo jogo = new JogoDaVelha4();  // new Rastros(obj player 1, obj player 2);
        //JanelaJogo janela = new JanelaJogo(jogo);
        //jogo.jogar(new ABPruneTT(Jogo.PECA_PRETA, 6), new ABPrune(Jogo.PECA_BRANCA, 6));
        
        Simulador s = new Simulador("JOGODAVELHA4",new ABPruneTT(Jogo.PECA_BRANCA, 6), new ABPrune(Jogo.PECA_PRETA, 2));
        s.Simular(10);

        //jogo.carregaLog("logs/log.txt");
        //janela.replayHistoricoJogadas();
    }
}