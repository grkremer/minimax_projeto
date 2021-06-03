import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        ArvoreDeJogadas j = new ArvoreDeJogadas();
        j.setData(new Jogada(new int[2][2]));
        j.getData().calculaPontos();

        ArrayList<Integer> l = new ArrayList<Integer>();
        l.add(2);
        l.set(0, 3);
        System.out.println(l);

        System.out.println("Raiz: "+j.getData().getPontos());
    }
}
