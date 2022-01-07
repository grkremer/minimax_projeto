package interfaces;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import agentes.backup.ArvoreDeJogadas;

public class JanelaGrafo extends JFrame {
    private mxGraph grafo;

    public JanelaGrafo() {
        super("Jgraph teste");
        setSize(1920,1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public mxGraph getGrafo() {
        return grafo;
    }
    public void setGrafo(mxGraph grafo) {
        this.grafo = grafo;
    }
    private void desenhaJogadas(ArvoreDeJogadas arvore, ArvoreDeJogadas raiz, Object nodoPai, int nivel, int[] posicoes) {
        Object PAI_DEFAULT = getGrafo().getDefaultParent();
        posicoes[nivel] += 1;
        int largura = 40;
        int altura = 20;
        int posicao = ((getWidth()/(raiz.numeroNosNivel(nivel)+1)) *posicoes[nivel]) - (largura/2);
        Object nodo = getGrafo().insertVertex(PAI_DEFAULT, null, Float.toString(arvore.getPontos()), posicao, (nivel+1)*80, largura, altura);
        if(nivel > 0) {
            getGrafo().insertEdge(PAI_DEFAULT, null, "", nodoPai, nodo);
        }
        for(int i=0; i<arvore.getFilhos().size(); i++) {
            desenhaJogadas(arvore.getFilho(i), raiz, nodo, nivel+1, posicoes);
        }
    }
    /**
     * Desenha na Janela um grafo que representa uma dada árvore de jogadas 
     *
     * @param arvore  A {@code ArvoreDeJogadas} que será desenhada.
     */
    public void desenhaArvoreDeJogadas(ArvoreDeJogadas arvore) {
        setGrafo(new mxGraph());
        mxGraphComponent componenteGrafo = new mxGraphComponent(getGrafo());
        componenteGrafo.setPreferredSize(new Dimension(getWidth(),getHeight()));
        getContentPane().add(componenteGrafo);

        int[] posicoes = new int[arvore.calculaProfundidade()+1];
        getGrafo().getModel().beginUpdate();
        desenhaJogadas(arvore, arvore, null, 0, posicoes);
        getGrafo().getModel().endUpdate();
        setVisible(true);
    }
}
