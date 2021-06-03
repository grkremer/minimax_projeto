//import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        ArvoreDeJogadas arvore = new ArvoreDeJogadas();
        arvore.setPontos(-11);
        for(int i=0; i<7; i++){
            ArvoreDeJogadas j = new ArvoreDeJogadas();
            j.calculaPontos();
            arvore.addFilho(j);
        }
        arvore.printaArvore();
        System.out.println("Menor: "+arvore.getMenorFolha()); 

        /*ArrayList<Integer> l = new ArrayList<Integer>();
        l.add(1);
        l.set(0, 3);
        System.out.println(l);
*/
    }
}
