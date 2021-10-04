public class App {
    public static void main(String[] args) throws Exception {
        //JanelaJogo janela = new JanelaJogo(new JogoDaVelha4());
        //janela.partidaBotXPlayer();
        Jogo jogo = new Alquerque();
        JanelaJogo janela = new JanelaJogo(jogo);
        jogo.monteCarloXMinimax();
        
        //jogo.carregaLog("logs/log.txt");
        janela.replayHistoricoJogadas();
    }
}