public class App {
    public static void main(String[] args) throws Exception {
        //JanelaJogo janela = new JanelaJogo(new JogoDaVelha4());
        
        Jogo jogo = new TicTackle5();
        JanelaJogo janela = new JanelaJogo(jogo);
        
        
        jogo.jogar(new ArvoreMonteCarlo(jogo.PECA_BRANCA, 10000, 1), new Humano(jogo.PECA_PRETA, janela));
        //jogo.jogar(new ArvoreMonteCarlo(jogo.PECA_PRETA, 10000, 1), );

        //jogo.carregaLog("logs/log.txt");
        janela.replayHistoricoJogadas();
    }
}