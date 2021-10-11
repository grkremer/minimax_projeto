import java.util.ArrayList;

public class Alquerque extends Jogo {
    Alquerque() {
        setNome("Alquerque");
        setProfundidade(5);
    }

    @Override
    public void inicializaTabuleiro() {
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            getTabuleiro()[x][0] = PECA_PRETA;
            getTabuleiro()[x][1] = PECA_PRETA;

            if(x < (LARGURA_TABULEIRO/2)) {
                getTabuleiro()[x][2] = PECA_PRETA;
            }
            else if(x > (LARGURA_TABULEIRO/2)) {
                getTabuleiro()[x][2] = PECA_BRANCA;
            }
            else {
                getTabuleiro()[x][2] = SEM_PECA;
            }

            getTabuleiro()[x][3] = PECA_BRANCA;
            getTabuleiro()[x][4] = PECA_BRANCA;
        }
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
                //Se quer se comer na diagonal
                else if((Math.abs(xInicial - xFinal) == 2 ) && (Math.abs(yInicial - yFinal) == 2) && pecaEntrePecas(xInicial, yInicial, xFinal, yFinal,tabuleiro) == invertePeca(tabuleiro[xInicial][yInicial])) {
                    //Se x e y têm a mesma paridade
                    if(((xInicial % 2 == 0) && (yInicial % 2 == 0)) || ((xInicial % 2 == 1) && (yInicial % 2 == 1))) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                //Se quer comer na vertical/horizontal
                else if((Math.abs(xInicial - xFinal) <= 2 ) && (Math.abs(yInicial - yFinal) <= 2) && pecaEntrePecas(xInicial, yInicial, xFinal, yFinal,tabuleiro) == invertePeca(tabuleiro[xInicial][yInicial])) {
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
    private ArrayList<ArrayList<Movimento>> listaPossiveisMovimentosPeca(int x, int y, int regiao, int[][] tabuleiro) {
        ArrayList<ArrayList<Movimento>> possiveisSequenciasDeMovimentos = new ArrayList<ArrayList<Movimento>>();
        int[][] regioes = {{1,0},{0,1},{1,1},{-1,-1},{-1,1},{1,-1},{-1,0},{0,-1}};
        if(regiao == 2) {
            for(int i=0; i < regioes.length; i++)
                for(int j=0; j < regioes[0].length; j++)
                    regioes[i][j] *= 2;
        }
        
        for(int i=0; i < regioes.length; i++) {
            int novoX = x+regioes[i][0];
            int novoY = y+regioes[i][1];
            int[] posInicial = {x, y};
            int[] posFinal = {novoX, novoY};
            Movimento movimento = new Movimento(tabuleiro[x][y], posInicial, posFinal, Movimento.Acao.MOVE);
            if(verificaMovimento(movimento, tabuleiro)) {
                ArrayList<Movimento> possibilidade = new ArrayList<Movimento>();
                possibilidade.add(movimento);
                if(regiao == 2) {
                    int[][] novoTabuleiro = criaCopiaTabuleiro(tabuleiro);
                    movePeca(posInicial, posFinal, novoTabuleiro);
                    ArrayList<int[]> pecasEliminadas = new ArrayList<int[]>();
                    pecasEliminadas.add(posPecaEliminada(posInicial, posFinal));
                    removePecas(pecasEliminadas, novoTabuleiro);
                    ArrayList<ArrayList<Movimento>> novasPossiveisSequencias = listaPossiveisMovimentosPeca(novoX, novoY, 2, novoTabuleiro);

                    if(novasPossiveisSequencias.isEmpty()) {
                        possiveisSequenciasDeMovimentos.add(possibilidade);
                    }
                    else {
                        for(int j=0; j<novasPossiveisSequencias.size(); j++) {
                            ArrayList<Movimento> novaPossibilidade = new ArrayList<Movimento>();
                            novaPossibilidade.addAll(possibilidade);
                            novaPossibilidade.addAll(novasPossiveisSequencias.get(j));
                            possiveisSequenciasDeMovimentos.add(novaPossibilidade);
                        }
                    }
                }
                else {
                    possiveisSequenciasDeMovimentos.add(possibilidade);
                }
            }
        }
        return possiveisSequenciasDeMovimentos;
    }
    private ArrayList<Jogada> listaPossiveisJogadasRegiao(int corPeca, int regiao, int[][] tabuleiro) {
        ArrayList<ArrayList<Movimento>> possiveisSequenciasDeMovimentos = new ArrayList<ArrayList<Movimento>>();
        ArrayList<Jogada> possiveisJogadas = new ArrayList<Jogada>();
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    possiveisSequenciasDeMovimentos.addAll(listaPossiveisMovimentosPeca(x, y, regiao, tabuleiro));
                }
            }
        }
        
        for(ArrayList<Movimento> movimentos : possiveisSequenciasDeMovimentos) {
            ArrayList<Movimento> eliminacoes = new ArrayList<Movimento>();
            for(Movimento movimento : movimentos) {
                if(comeuPeca(movimento.getPosicao1(), movimento.getPosicao2())) {
                    int[] posicaoEliminacao = posPecaEliminada(movimento.getPosicao1(), movimento.getPosicao2());
                    eliminacoes.add(new Movimento(corPeca, posicaoEliminacao, posicaoEliminacao, Movimento.Acao.REMOVE));
                }
            }
            movimentos.addAll(eliminacoes);
            possiveisJogadas.add(new Jogada(movimentos));
        }
        return possiveisJogadas;
    }
    @Override
    public ArrayList<Jogada> listaPossiveisJogadas(int corPeca, int[][] tabuleiro) {
        ArrayList<Jogada> possiveisJogadas;
        
        possiveisJogadas = listaPossiveisJogadasRegiao(corPeca, 2, tabuleiro);

        if(possiveisJogadas.isEmpty()) {
            possiveisJogadas = listaPossiveisJogadasRegiao(corPeca, 1, tabuleiro);
        }
        return possiveisJogadas;
    }
    
    @Override
    public boolean verificaVitoria(int corPeca, int[][] tabuleiro) {
        if(contaPecas(invertePeca(corPeca), tabuleiro) == 0) {
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public boolean verificaFimDeJogo(int[][] tabuleiro) {
        return verificaVitoria(PECA_BRANCA, tabuleiro) || verificaVitoria(PECA_PRETA, tabuleiro);
    }
        
    private float geraCustoPeca(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        float custo = normalizaPontuacao(0f, 12f, (float)minPontos, (float)maxPontos, 12f - (float)contaPecas(invertePeca(corPeca), tabuleiro));
        return custo;
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
            return geraCustoPeca(corPeca, tabuleiro, minPontos, maxPontos)*0.5f + geraCustoPeca(invertePeca(corPeca), tabuleiro, minPontos, maxPontos)*-0.5f;
        }  
    }
    
    @Override
    public Movimento.Acao proximaAcao(int corPeca, int[][] tabuleiro) {
        return Movimento.Acao.MOVE;
    }
}
    
