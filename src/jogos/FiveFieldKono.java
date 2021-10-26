package jogos;
import java.util.ArrayList;

import jogos.util.*;


import java.util.Collections;

public class FiveFieldKono extends Jogo {
    
    public FiveFieldKono() {
        setNome("Five Field Kono");
    }

    @Override
    public void inicializaTabuleiro() {
        /* 
            x x x x x 
            x o o o x
            o o o o o
            y o o o y
            y y y y y
        */

        //Insere peças na primeira e última linha
        for(int i = 0; i < LARGURA_TABULEIRO; i++){
            getTabuleiro()[i][0] = PECA_BRANCA;
            getTabuleiro()[i][4] = PECA_PRETA;
        }
        getTabuleiro()[0][1] = PECA_BRANCA;
        getTabuleiro()[4][1] = PECA_BRANCA;

        
        getTabuleiro()[0][3] = PECA_PRETA;
        getTabuleiro()[4][3] = PECA_PRETA;

    }

    @Override
    public boolean verificaMovimento(Movimento movimento, int[][] tabuleiro) {
        int xInicial = movimento.getPosicao1()[0];
        int yInicial = movimento.getPosicao1()[1];
        int xFinal = movimento.getPosicao2()[0];
        int yFinal = movimento.getPosicao2()[1];
        if(estaNosLimites(xInicial, yInicial) && estaNosLimites(xFinal, yFinal)) {
            if (tabuleiro[xFinal][yFinal] == SEM_PECA && tabuleiro[xInicial][yInicial] != SEM_PECA) {
                //Se quer se mover na diagonal
                if((Math.abs(xInicial - xFinal) == 1 ) && (Math.abs(yInicial - yFinal) == 1)) {
                    //Se x e y têm a mesma paridade
                    if(((xInicial % 2 == 0) && (yInicial % 2 == 0)) || ((xInicial % 2 == 1) && (yInicial % 2 == 1))) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                //Se quer se mover na vertical/horizontal
                else if((Math.abs(xInicial - xFinal) <= 1 ) && (Math.abs(yInicial - yFinal) <= 1)) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public ArrayList<Jogada> listaPossiveisJogadas(int corPeca, int[][] tabuleiro) {
        ArrayList<Jogada> possiveisJogadas = new ArrayList<Jogada>();
        int[][] regioes = {{1,0},{0,1},{1,1},{-1,-1},{-1,1},{1,-1},{-1,0},{0,-1}};
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    for(int i=0; i < regioes.length; i++) {
                        int novoX = x+regioes[i][0];
                        int novoY = y+regioes[i][1];
                        int[][] posicoes = {{x, y}, {novoX, novoY}};
                        Movimento movimento = new Movimento(corPeca, posicoes[0], posicoes[1], Movimento.Acao.MOVE);
                        if(verificaMovimento(movimento, tabuleiro)) {
                            Jogada possibilidade = new Jogada(movimento);
                            possiveisJogadas.add(possibilidade);
                        }
                    }
                }
            }
        }
        return possiveisJogadas;
    }
    
    @Override
    public boolean verificaVitoria(int corPeca, int[][] tabuleiro) {
        if(getHistoricoJogadas().size() < 2){
            //primeira e segunda rodada não faz avaliação de fim de jogo
            return false;
        }
        
        if(corPeca == PECA_BRANCA){
            for(int i = 0; i < LARGURA_TABULEIRO; i++){
                if(getTabuleiro()[i][4] == SEM_PECA)
                    return false;
            }
            return getTabuleiro()[0][3] != SEM_PECA && getTabuleiro()[4][3] != SEM_PECA;
        }else{
            for(int i = 0; i < LARGURA_TABULEIRO; i++){
                if(getTabuleiro()[i][0] == SEM_PECA)
                    return false;
            }
            return getTabuleiro()[0][1] != SEM_PECA && getTabuleiro()[4][1] != SEM_PECA; 
        }
    }

    @Override
    public boolean verificaFimDeJogo(int[][] tabuleiro) {
        
        return verificaVitoria(PECA_BRANCA, tabuleiro) || verificaVitoria(PECA_PRETA, tabuleiro);
    }

    @Override
    public float geraCusto(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        if(verificaVitoria(corPeca, tabuleiro)) {
            return maxPontos;
        }
        else if(verificaVitoria(invertePeca(corPeca), tabuleiro)) {
            return minPontos;
        }
        else{
            //return avaliaDistanciaObjetivo(corPeca, tabuleiro) - (0.4f * avaliaDistanciaObjetivo(invertePeca(corPeca), tabuleiro));//(maxPontos + minPontos)/2;
            return pecasNoObjetivo(corPeca, tabuleiro); //- (0.5f * pecasNoObjetivo(invertePeca(corPeca), tabuleiro));
        }
    }

    @Override
    public Movimento.Acao proximaAcao(int corPeca, int[][] tabuleiro) {
        return Movimento.Acao.MOVE;
    }

    public int pecasNoObjetivo(int corPeca, int[][] tabuleiro){
        int contador = 0;
        if(corPeca == PECA_PRETA){
            for(int i = 0; i < LARGURA_TABULEIRO; i++){
                if(getTabuleiro()[i][0] != SEM_PECA)
                {
                    contador+=1;
                }
            }
            if(getTabuleiro()[0][1] != SEM_PECA){ 
                contador+=1;
            }
            if(getTabuleiro()[4][1] != SEM_PECA){
                contador+=1;
            }
        }
        else{
            for(int i = 0; i < LARGURA_TABULEIRO; i++){
                if(getTabuleiro()[i][4] != SEM_PECA)
                {
                    contador+=1;
                }
            }
            if(getTabuleiro()[0][3] != SEM_PECA){ 
                contador+=1;
            }
            if(getTabuleiro()[4][3] != SEM_PECA){
                contador+=1;
            }
        }
        return contador * 10;
    }

    public int avaliaDistanciaObjetivo(int corPeca, int[][] tabuleiro){
        ArrayList<int[]> objetivosLivres = objetivosRestantes(corPeca); //randomize
        ArrayList<int[]> locPecasJogador = posPecasJogador(corPeca, tabuleiro); //randomize
        
        // criar um vetor para cada peça e a distancia dela para cada posição objetivo livre
        // exemplo
        /* 
            2 3 4 5 6 
            1 o o o 7
            o b c o o
            a o o o f
            o o o d e

            a = { (1,0):2, (0,0):3, (0,1):4, (0,2):5 ... }
        */
        ArrayList<ArrayList<Integer>> distanciasPecas = new ArrayList<ArrayList<Integer>>();
        //gera distancias para cada peça
        for(int[] lp : locPecasJogador){
            distanciasPecas.add(new ArrayList<Integer>());
            for(int[] ol : objetivosLivres){
                int distancia = Math.abs(lp[0] - ol[0]) + Math.abs(lp[1] - ol[1]); //manhattan dist
                distanciasPecas.get(distanciasPecas.size()-1).add(distancia);
            }
        }

        int somaMenorDistancia = 0;
        // para cada 1 das posições disponíveis
        for(int i = 0; i < objetivosLivres.size(); i++){
            
            int indiceVetor = -1;
            int menorDistanciaObj = Integer.MAX_VALUE;
            int indiceMenorDistanciaObj = -1;
            // para cada um dos vetores
            for(int j = 0; j < distanciasPecas.size(); j++){
               int menorDistancia = Integer.MAX_VALUE;
               int indiceMenorDistancia = -1;
               for(int k = 0; k < distanciasPecas.get(j).size(); k++){ 
                    int distancia = distanciasPecas.get(j).get(k);
                    // busca o menor número
                    // salva o índice do vetor e o índice da posição que se encontra a menor distância
                    if( distancia < menorDistancia){
                        menorDistancia = distancia;
                        indiceMenorDistancia = k;
                    }
                }

                if(menorDistancia < menorDistanciaObj){
                    menorDistanciaObj = menorDistancia;
                    indiceMenorDistanciaObj = indiceMenorDistancia;
                    indiceVetor = j;
                }
            }
           
            // soma a distância encontrada
            somaMenorDistancia+= menorDistanciaObj;
            // remove o vetor já usado
            distanciasPecas.remove(indiceVetor);
            // remove de todos os vetores o índice encontrado
            for(int j = 0; j < distanciasPecas.size();j++){
                distanciasPecas.get(j).remove(indiceMenorDistanciaObj);
            }

        }

        return 50 - somaMenorDistancia;
    }

    public ArrayList<int[]> posPecasJogador(int corPeca, int[][] tabuleiro){
        ArrayList<int[]> locPecasJogador = new ArrayList<int[]>();
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                
                // descarta peças que já chegaram ao objetivo
                if(tabuleiro[x][y] == corPeca){
                    
                    if(corPeca == PECA_BRANCA){
                        if(x != 4 || !(x == 0 && y == 3) || !( x == 4 && y == 3)){
                            int[] pos = {x,y};
                            locPecasJogador.add(pos);
                        }
                    }else{
                        if(x != 0 || !(x == 0 && y == 1) || !( x == 4 && y == 1)){
                            int[] pos = {x,y};
                            locPecasJogador.add(pos);
                        }
                    }
                    
                }
            }
        }
        return locPecasJogador;
    }
    
    public ArrayList<int[]> objetivosRestantes(int corPeca){
        ArrayList<int[]> objetivosLivres = new ArrayList<int[]>();

        if(corPeca == PECA_PRETA){
            for(int i = 0; i < LARGURA_TABULEIRO; i++){
                if(getTabuleiro()[i][0] == SEM_PECA)
                {
                    int[] pos = {i,0};
                    objetivosLivres.add(pos);
                }
            }
            if(getTabuleiro()[0][1] == SEM_PECA){ 
                int[] pos = {0,1};
                objetivosLivres.add(pos); 
            }
            if(getTabuleiro()[4][1] == SEM_PECA){
                int[] pos = {4,1}; 
                objetivosLivres.add(pos); 
            }
        }
        else{
            for(int i = 0; i < LARGURA_TABULEIRO; i++){
                if(getTabuleiro()[i][4] == SEM_PECA)
                {
                    int[] pos = {i,4};
                    objetivosLivres.add(pos);
                }
            }
            if(getTabuleiro()[0][3] == SEM_PECA){ 
                int[] pos = {0,3};
                objetivosLivres.add(pos); 
            }
            if(getTabuleiro()[4][3] == SEM_PECA){
                int[] pos = {4,3}; 
                objetivosLivres.add(pos); 
            }
        }
        return objetivosLivres;
    }

}
    

    
