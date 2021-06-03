import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class ArvoreDeJogadas {
    private int[][] tabuleiro;
    private int pontos;
    private List<ArvoreDeJogadas> filhos;

    public ArvoreDeJogadas() {
        this.filhos = new ArrayList<ArvoreDeJogadas>();
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
    public List<ArvoreDeJogadas> getFilhos() {
        return filhos;
    }
    public void setFilhos(List<ArvoreDeJogadas> filhos) {
        this.filhos = filhos;
    }
    public ArvoreDeJogadas getFilho(int index) {
        return filhos.get(index);
    }
    public void setFilho(int index, ArvoreDeJogadas filhos) {
        this.filhos.set(index, filhos);
    }
    public void addFilho(ArvoreDeJogadas filhos) {
        this.filhos.add(filhos);
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
        for(int i=0; i<getFilhos().size(); i++){
            getFilho(i).printaArvore();
        }
    }
    public int getMenorFolha(){
        int menor;
        if (this.filhos.isEmpty()){
            return this.pontos;
        }
        else{
            menor = getFilho(0).getMenorFolha();
            for(int i=1; i<getFilhos().size(); i++){
                menor = Math.min(menor,getFilho(i).getMenorFolha());
            }
            return menor;
        }
    }
}