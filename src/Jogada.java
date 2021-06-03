import java.util.Random;

public class Jogada {
    private int[][] tabuleiro;
    private int pontos;

    public Jogada(int[][] tabuleiro){
        setTabuleiro(tabuleiro);
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

    private int randomInt(int min, int max){
        Random random = new Random();
        return random.ints(min,(max+1)).findFirst().getAsInt();
    }
    public void calculaPontos(){
        setPontos(randomInt(-10, 10));
    }

    public void print(){
        System.out.println(pontos+" ");
    }
}
