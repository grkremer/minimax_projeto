public class App {
    public static void main(String[] args) throws Exception {
        ArvoreDeJogadas arvore = new ArvoreDeJogadas();
        arvore.geraArvoreAleatoria(6);
        arvore.minimaxAlphaBeta();  
        JanelaGrafo janela1 = new JanelaGrafo();
        janela1.desenhaArvoreDeJogadas(arvore);
        System.out.println("Com alpha-beta: "+arvore.numeroNosTotal());

        arvore.minimax();
        JanelaGrafo janela2 = new JanelaGrafo();
        janela2.desenhaArvoreDeJogadas(arvore);
        System.out.println("Sem alpha-beta: "+arvore.numeroNosTotal());

        new JanelaJogo(new TicTackle5());
    }
}
