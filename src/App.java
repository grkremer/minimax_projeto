public class App {
    public static void main(String[] args) throws Exception {
        ArvoreDeJogadas arvore = new ArvoreDeJogadas();
        arvore.geraArvoreAleatoria(3);
        arvore.minimax();
        
        JanelaGrafo janela = new JanelaGrafo();
        janela.desenhaArvoreDeJogadas(arvore);
        janela.setVisible(true);
    }
}
