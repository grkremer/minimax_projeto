package logging;

import java.util.HashMap;

import agentes.util.NodoMinimax;

// número de nodos
// número de nodos por filho (Média)
// número máximo de filhos por nodo (Max)
// tempo de execução
public class LogMinimax {
    public int numeroNodos;
    public int nodosInternos;
    public int sumFilhos;
    public float mediaBranching;
    public int maxBranching;
    public int maxDepth;
    //ArrayList<Integer> nodosPorNivel;
    HashMap<Integer, Integer> nodosPorNivel;

    float tempoExecucao;
    long startTime;
    long endTime;
    public LogMinimax(){
        //nodosPorNivel = new ArrayList<Integer>();
        nodosPorNivel = new HashMap<Integer,Integer>();
        numeroNodos = 0;
        nodosInternos = 0;
        sumFilhos = 0;
        mediaBranching = 0;
        maxBranching = 0;
        tempoExecucao = 0;
        maxDepth = 0;
        startTime = System.currentTimeMillis();
    };

    public void AvaliaArvore(NodoMinimax raiz)
    {
        endTime = System.currentTimeMillis();
        ProcessarArvore(raiz, 0);
        mediaBranching = sumFilhos/nodosInternos;
        tempoExecucao = (endTime - startTime)/1000f;
    }

    private void ProcessarArvore(NodoMinimax nodo, int profundidade)
    {
        numeroNodos+=1;

        Integer numeroFilhos = nodo.getFilhos().size();
        
        if(numeroFilhos>0){
            nodosInternos += 1;
            sumFilhos += numeroFilhos;
        }

        //atualiza maxBranching
        if(maxBranching < numeroFilhos) maxBranching = numeroFilhos;

        //atualiza nodos por nível
        //if(nodosPorNivel.size() < profundidade) nodosPorNivel.add(numeroFilhos);
        if(!nodosPorNivel.containsKey(profundidade)) nodosPorNivel.put(profundidade,1);
        else nodosPorNivel.put(profundidade, nodosPorNivel.get(profundidade)+1) ;
        //pega os filhos
        for (NodoMinimax filho : nodo.getFilhos().values()) ProcessarArvore(filho, profundidade+1);

        if(profundidade > maxDepth) maxDepth = profundidade;
        
    }

    @Override
    public String toString()
    {
        String filhosPorNivel = "";
        
        for(int i=0; i < nodosPorNivel.size(); i++)
        {
            int n = nodosPorNivel.get(i);
            filhosPorNivel += "(nvl " + i + ":" +  n + ") ";
        }
        return "\nMINIMAX\nnumero de nodos: " + numeroNodos + "\nmax filho: " + maxBranching + "\nmedia filhos: " + mediaBranching + "\n" + filhosPorNivel + "\n" + tempoExecucao + "s";
    }
}
