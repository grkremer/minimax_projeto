public class App {
    public static void main(String[] args) throws Exception {
        Jogo jogo = new JogoDaVelha4();
        //jogo.partidaBotXBot();

        new JanelaJogo(jogo);
        jogo.carregaLog("log.txt");
        jogo.replayHistoricoJogadas();
    }
}
