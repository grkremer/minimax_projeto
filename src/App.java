public class App {
    public static void main(String[] args) throws Exception {
        Jogo jogo = new JogoDaVelha5Variante();
        new JanelaJogo(jogo);
        
        jogo.partidaBotXPlayer();
    }
}
