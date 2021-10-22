package jogos;
import java.util.ArrayList;
import java.util.List;

import jogos.util.*;

public class Rastros extends Jogo {
    public Rastros() {
        setNome("Rastros");
    }

    @Override
    public void inicializaTabuleiro() {
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                getTabuleiro()[x][y] = SEM_PECA;
            }
        }
        getTabuleiro()[3][1] = PECA_BRANCA; 
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
                if(tabuleiro[x][y] == PECA_BRANCA) {
                    for(int i=0; i < regioes.length; i++) {
                        int novoX = x+regioes[i][0];
                        int novoY = y+regioes[i][1];
                        int[][] posicoes = {{x, y}, {novoX, novoY}};
                        Movimento movimento = new Movimento(PECA_BRANCA, posicoes[0], posicoes[1], Movimento.Acao.MOVE);
                        if(verificaMovimento(movimento, tabuleiro)) {
                            Movimento rastro = new Movimento(PECA_PRETA, posicoes[0], posicoes[0], Movimento.Acao.INSERE);
                            Jogada possibilidade = new Jogada(new ArrayList<Movimento>(List.of(movimento, rastro)));
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
        return (corPeca == PECA_BRANCA && tabuleiro[0][ALTURA_TABULEIRO-1] == PECA_BRANCA) ||
                (corPeca == PECA_PRETA && tabuleiro[LARGURA_TABULEIRO-1][0] == PECA_BRANCA);
    }
    @Override
    public boolean verificaFimDeJogo(int[][] tabuleiro) {
        return verificaVitoria(PECA_BRANCA, tabuleiro) || verificaVitoria(PECA_PRETA, tabuleiro) || listaPossiveisJogadas(SEM_PECA, tabuleiro).isEmpty();
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
}