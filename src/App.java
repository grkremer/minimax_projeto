public class App {
    public static void main(String[] args) throws Exception {
        Jogo jogo = new TicTackle5();
        new JanelaJogo(jogo);
        jogo.partidaBotXBot();

        
        //jogo.carregaLog("logs/logFalhaTicTackle.txt");
        //jogo.replayHistoricoJogadas();
    }
}