
import java.util.HashMap;
// número de nodos
// número de nodos por filho (Média)
// número máximo de filhos por nodo (Max)
// tempo de execução
public class LogArvoreJogo {
    int numeroNodos;
    int nodosInternos;
    int sumFilhos;
    float mediaBranching;
    int maxBranching;
    //ArrayList<Integer> nodosPorNivel;
    HashMap<Integer, Integer> nodosPorNivel;

    float tempoExecucao;
    long startTime;
    long endTime;
    public LogArvoreJogo(){
        //nodosPorNivel = new ArrayList<Integer>();
        nodosPorNivel = new HashMap<Integer,Integer>();
        numeroNodos = 0;
        nodosInternos = 0;
        sumFilhos = 0;
        mediaBranching = 0;
        maxBranching = 0;
        tempoExecucao = 0;
        startTime = System.currentTimeMillis();
    };

    public void AvaliaArvore(ArvoreDeJogadas raiz)
    {
        endTime = System.currentTimeMillis();
        ProcessarArvore(raiz, 0);
        mediaBranching = sumFilhos/nodosInternos;
        tempoExecucao = (endTime - startTime)/1000f;
    }

    private void ProcessarArvore(ArvoreDeJogadas nodo, int profundidade)
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
        for (ArvoreDeJogadas filho : nodo.getFilhos()) ProcessarArvore(filho, profundidade+1);
        
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
