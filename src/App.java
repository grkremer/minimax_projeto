import jogos.*;
import jogos.util.Jogo;
import agentes.*;
import interfaces.*;
import tests.*;
public class App {
    /* 
        OBSERVAÇÃO: o MINIMAX ta com shuffle
        TODO: acompanhar as partidas, ABPRUNE ta estranho talvez seja a função custo no ticktackle não ta conseguindo finalizar a partida
        TODO2: acho que o NegTT tem coisa errada
    */
    public static void main(String[] args) throws Exception {
        
        //Jogo jogo = new JogoDaVelha4();  // new Rastros(obj player 1, obj player 2);
        //JanelaJogo janela = new JanelaJogo(jogo);
        //jogo.jogar(new ABPruneTT(Jogo.PECA_PRETA, 6), new ABPrune(Jogo.PECA_BRANCA, 6));
        
        Simulador s = new Simulador(new TicTackle5(),new ABPrune(Jogo.PECA_BRANCA, 6), new NegTT(Jogo.PECA_PRETA, 6));
        s.Simular(10);

        //jogo.carregaLog("logs/log.txt");
        //janela.replayHistoricoJogadas();
    }
}