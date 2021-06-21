public class App {
    public static void main(String[] args) throws Exception {
        final int TEMPO = 1;
        TicTackle5 t = new TicTackle5();
        new JanelaJogo(t);
        int vitoriasBranco = 0;
        int vitoriasPreto = 0;
        for(int i=0; i<1000; i++){
            t.inicializaTabuleiro();
            while(!t.verificaVitoria(1) && !t.verificaVitoria(2)){
                t.maquinaJoga(1);
                t.repaint();
                Thread.sleep(TEMPO);
                t.maquinaJoga(2);
                t.repaint();
                Thread.sleep(TEMPO);
            }
            Thread.sleep(TEMPO);
            if(t.verificaVitoria(1)) vitoriasBranco++;
            else {
                //Thread.sleep(6000);
                vitoriasPreto++;
            }
            System.out.println("Branco "+vitoriasBranco+" x "+vitoriasPreto+" Preto ("+(i+1)+" jogos)");
        }
    }
}
