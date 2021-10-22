package jogos;
import java.util.ArrayList;

import jogos.util.*;

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
        if(getHistoricoJogadas().size() < 2){
            //primeira e segunda rodada não faz avaliação de fim de jogo
            return false;
        }
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
            return (maxPontos + minPontos)/2;
        }
    }

    @Override
    public Movimento.Acao proximaAcao(int corPeca, int[][] tabuleiro) {
        return Movimento.Acao.MOVE;
    }

    //public avaliaDistanciaObjetivo(){

    //}


}
    
