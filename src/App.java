public class App {
    public static void main(String[] args) throws Exception {
        Jogo jogo = new Alquerque();
        new JanelaJogo(jogo);
        
        jogo.partidaBotXPlayer();
    }
}
