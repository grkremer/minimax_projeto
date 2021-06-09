import java.awt.Dimension;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class JanelaGrafo extends JFrame{
    private mxGraph grafo;
    private mxGraphComponent componenteGrafo;

    public JanelaGrafo(){
        super("Jgraph teste");
        setSize(1600,900);
        setLocationRelativeTo(null);
    }
    private void desenhaJogadas(ArvoreDeJogadas arvore, ArvoreDeJogadas raiz, Object nodoPai, int nivel, int[] posicoes){
        Object PAI_DEFAULT = grafo.getDefaultParent();
        posicoes[nivel] += 1;
        Object nodo = grafo.insertVertex(PAI_DEFAULT, 
                                        null, 
                                        Integer.toString(arvore.getPontos()), 
                                        ((1600/(raiz.getProfundidade()-nivel+1))/raiz.numeroNosNivel(nivel))*posicoes[nivel], 
                                        (nivel+1)*80, 
                                        100, 
                                        50);
        if(nivel > 0){
            grafo.insertEdge(PAI_DEFAULT, null, "", nodoPai, nodo);
        }
        for(int i=0; i<arvore.getFilhos().size(); i++){
            desenhaJogadas(arvore.getFilho(i), raiz, nodo, nivel+1, posicoes);
        }
    }
    public void desenhaArvoreDeJogadas(ArvoreDeJogadas arvore){
        grafo = new mxGraph();
        componenteGrafo = new mxGraphComponent(grafo);
        componenteGrafo.setPreferredSize(new Dimension(1600,900));
        getContentPane().add(componenteGrafo);

        int[] posicoes = new int[arvore.getProfundidade()+1];
        grafo.getModel().beginUpdate();
        desenhaJogadas(arvore, arvore, null, 0, posicoes);
        grafo.getModel().endUpdate();
    }
}
