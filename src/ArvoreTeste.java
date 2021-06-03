import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class ArvoreTeste {
    private int[][] tabuleiro;
    private int pontos;
    private List<ArvoreTeste> children;

    public ArvoreTeste() {
        this.children = new ArrayList<ArvoreTeste>();
    }
    public int[][] getTabuleiro() {
        return tabuleiro;
    }
    public void setTabuleiro(int[][] tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    public int getPontos() {
        return pontos;
    }
    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
    public List<ArvoreTeste> getChildren() {
        return children;
    }
    public void setChildren(List<ArvoreTeste> children) {
        this.children = children;
    }
    public ArvoreTeste getChild(int index) {
        return children.get(index);
    }
    public void setChild(int index, ArvoreTeste children) {
        this.children.set(index, children);
    }
    public void addChild(ArvoreTeste children) {
        this.children.add(children);
    }
    private int randomInt(int min, int max){
        Random random = new Random();
        return random.ints(min,(max+1)).findFirst().getAsInt();
    }
    public void calculaPontos(){
        setPontos(randomInt(-10, 10));
    }

    public void printaArvore(){
        System.out.println(pontos+" ");
        for(int i=0; i<getChildren().size(); i++){
            getChild(i).printaArvore();
        }
    }
}
