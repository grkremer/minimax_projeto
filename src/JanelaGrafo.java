import java.awt.Dimension;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class JanelaGrafo extends JFrame{
    private mxGraph grafo;
    private mxGraphComponent componenteGrafo;

    public JanelaGrafo(){
        super("Jgraph teste");
        setSize(1920,1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    private void desenhaJogadas(ArvoreDeJogadas arvore, ArvoreDeJogadas raiz, Object nodoPai, int nivel, int[] posicoes){
        if(arvore.isAcessado()){
            Object PAI_DEFAULT = grafo.getDefaultParent();
            posicoes[nivel] += 1;
            int largura = 40;
            int altura = 20;
            int posicao = ((getWidth()/(raiz.numeroNosNivel(nivel)+1)) *posicoes[nivel]) - (largura/2);
            Object nodo = grafo.insertVertex(PAI_DEFAULT, null, Integer.toString(arvore.getPontos()), posicao, (nivel+1)*80, largura, altura);
            if(nivel > 0){
                grafo.insertEdge(PAI_DEFAULT, null, "", nodoPai, nodo);
            }
            for(int i=0; i<arvore.getFilhos().size(); i++){
                desenhaJogadas(arvore.getFilho(i), raiz, nodo, nivel+1, posicoes);
            }
        }
    }
    public void desenhaArvoreDeJogadas(ArvoreDeJogadas arvore){
        grafo = new mxGraph();
        componenteGrafo = new mxGraphComponent(grafo);
        componenteGrafo.setPreferredSize(new Dimension(getWidth(),getHeight()));
        getContentPane().add(componenteGrafo);

        int[] posicoes = new int[arvore.getProfundidade()+1];
        grafo.getModel().beginUpdate();
        desenhaJogadas(arvore, arvore, null, 0, posicoes);
        grafo.getModel().endUpdate();
        setVisible(true);
    }
}
