public class ArvoreDeJogadas extends Tree<Jogada> {
    public ArvoreDeJogadas() {
        super();
    }
    private void printaArvore(Tree<Jogada> a){
        getData().print();
        for(int i=0; i<getChildren().size(); i++){
            printaArvore(getChild(i));
        }
    }
    public void printaArvore(){
        printaArvore(this);
    }
}
