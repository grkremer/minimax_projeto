public class App {
    public static void main(String[] args) throws Exception {
        TicTackle5 t = new TicTackle5();
        new JanelaJogo(t);
        int vitoriasBranco = 0;
        int vitoriasPreto = 0;
        for(int i=0; i<10; i++){
            t.inicializaTabuleiro();
            while(!t.verificaVitoria(1) && !t.verificaVitoria(2)){
                t.maquinaJoga(1);
                t.repaint();
                Thread.sleep(150);
                t.maquinaJoga(2);
                t.repaint();
                Thread.sleep(150);
            }
            Thread.sleep(150);
            if(t.verificaVitoria(1)) vitoriasBranco++;
            else vitoriasPreto++;
            System.out.println("Branco "+vitoriasBranco+" x "+vitoriasPreto+" Preto ("+(i+1)+" jogos)");
        }
    }
}
