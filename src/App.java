public class App {
    public static void main(String[] args) throws Exception {
        //JanelaJogo janela = new JanelaJogo(new JogoDaVelha4());
        
        Jogo jogo = new JogoDaVelha4();
        JanelaJogo janela = new JanelaJogo(jogo);
        janela.partidaBotXPlayer();
        //jogo.monteMinimaxXMinimax();
        
        //jogo.carregaLog("logs/log.txt");
        janela.replayHistoricoJogadas();
    }
}