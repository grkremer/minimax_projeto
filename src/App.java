public class App {
    public static void main(String[] args) throws Exception {
        TicTackle5 t = new TicTackle5();
        new JanelaJogo(t);
        while(!t.verificaVitoria(1) && !t.verificaVitoria(2)){
            t.maquinaJoga(1);
            t.repaint();
            Thread.sleep(150);
            t.maquinaJoga(2);
            t.repaint();
            Thread.sleep(150);
        }
    }
}
