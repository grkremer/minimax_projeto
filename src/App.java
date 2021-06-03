//import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        ArvoreDeJogadas j = new ArvoreDeJogadas();
        j.calculaPontos();

        /*ArrayList<Integer> l = new ArrayList<Integer>();
        l.add(1);
        l.set(0, 3);
        System.out.println(l);
*/
        System.out.println("Raiz: "+j.getPontos());
        j.printaArvore();
    }
}
