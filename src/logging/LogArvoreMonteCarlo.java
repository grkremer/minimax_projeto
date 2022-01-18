package logging;

import java.util.HashMap;

import agentes.util.NodoMonteCarlo;
// número de nodos
// número de nodos por filho (Média)
// número máximo de filhos por nodo (Max)
// tempo de execução
public class LogArvoreMonteCarlo {
   public  int numeroNodos;
    public int nodosInternos;
    public int sumFilhos;
    public float mediaBranching;
    public int maxBranching;
    //ArrayList<Integer> nodosPorNivel;
    HashMap<Integer, Integer> nodosPorNivel;
    public LogArvoreMonteCarlo(){
        //nodosPorNivel = new ArrayList<Integer>();
        nodosPorNivel = new HashMap<Integer,Integer>();
        numeroNodos = 0;
        nodosInternos = 1;
        sumFilhos = 1;
        mediaBranching = 0;
        maxBranching = 0;
        
    };

    public void AvaliaArvore(NodoMonteCarlo raiz)
    {
        ProcessarArvore(raiz, 0);
        mediaBranching = sumFilhos/nodosInternos;
    }

    private void ProcessarArvore(NodoMonteCarlo nodo, int profundidade)
    {
        
        numeroNodos+=1;
        

        if(nodo == null) return;
        
        Integer numeroFilhos = nodo.getFilhos().size();
        
        if(numeroFilhos>0){
            if(numeroFilhos > 1)
            
                nodosInternos += 1; //VER ISSO AQUI MONTE-CARLO TEM MUITOS NODOS COM APENAS 1 FILHO
                sumFilhos += numeroFilhos;
        }

        //atualiza maxBranching
        if(maxBranching < numeroFilhos) maxBranching = numeroFilhos;

        //atualiza nodos por nível
        //if(nodosPorNivel.size() < profundidade) nodosPorNivel.add(numeroFilhos);
        if(!nodosPorNivel.containsKey(profundidade)) nodosPorNivel.put(profundidade,1);
        else nodosPorNivel.put(profundidade, nodosPorNivel.get(profundidade)+1) ;
        //pega os filhos
        for (NodoMonteCarlo filho : (nodo.getFilhos()).values()) ProcessarArvore(filho, profundidade+1);
        
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
        return "\nMONTE-CARLO\nnumero de nodos: " + numeroNodos + "\nmax filho: " + maxBranching + "\nmedia filhos: " + mediaBranching + "\n" + filhosPorNivel + "\n";
    }
}
