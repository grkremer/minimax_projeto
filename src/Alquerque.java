import java.util.ArrayList;
import java.util.Objects;

public class Alquerque extends Jogo {
    Alquerque() {
        super();
        setNome("Alquerque");
        setProfundidade(6);
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
    
    private boolean comeuPeca(int[][] movimento) {
        int xInicial = movimento[0][0];
        int yInicial = movimento[0][1];
        int xFinal = movimento[1][0];
        int yFinal = movimento[1][1];
        return Math.abs(xFinal - xInicial) == 2 || Math.abs(yFinal - yInicial) == 2;
    }
    
    private int pecaEntrePecas(int x1, int y1, int x2, int y2, int[][] tabuleiro) {
        int[] posNovo = posPecaEntrePecas(x1, y1, x2, y2);
        return tabuleiro[posNovo[0]][posNovo[1]];
    }
    private int[] posPecaEntrePecas(int x1, int y1, int x2, int y2) {
        int xNovo, yNovo;
        int[] posNovo = new int[2];

        if(x1 > x2) xNovo = x2 + 1;
        else if(x2 > x1) xNovo = x1 + 1;
        else xNovo = x1;

        if(y1 > y2) yNovo = y2 + 1;
        else if(y2 > y1) yNovo = y1 + 1;
        else yNovo = y1;

        posNovo[0] = xNovo;
        posNovo[1] = yNovo;
        return posNovo;
    }
    private boolean andandoPraTras(int yInicial, int yFinal, int corPeca) {
        if(corPeca == PECA_PRETA) {
            if(yFinal < yInicial) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if(yFinal > yInicial) {
                return true;
            }
            else {
                return false;
            }
        }
    }
    @Override
    public boolean verificaMovimento(int[][] movimento, int[][] tabuleiro) {
        int xInicial = movimento[0][0];
        int yInicial = movimento[0][1];
        int xFinal = movimento[1][0];
        int yFinal = movimento[1][1];
        if(estaNosLimites(xInicial, yInicial) && estaNosLimites(xFinal, yFinal)) {
            if (tabuleiro[xFinal][yFinal] == SEM_PECA && tabuleiro[xInicial][yInicial] != SEM_PECA) {
                //Se quer se mover na diagonal
                if((Math.abs(xInicial - xFinal) == 1 ) && (Math.abs(yInicial - yFinal) == 1) && !andandoPraTras(yInicial, yFinal, tabuleiro[xInicial][yInicial])) {
                    //Se x e y têm a mesma paridade
                    if(((xInicial % 2 == 0) && (yInicial % 2 == 0)) || ((xInicial % 2 == 1) && (yInicial % 2 == 1))) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                //Se quer se mover na vertical/horizontal
                else if((Math.abs(xInicial - xFinal) <= 1 ) && (Math.abs(yInicial - yFinal) <= 1) && !andandoPraTras(yInicial, yFinal, tabuleiro[xInicial][yInicial])) {
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
    private ArrayList<ArrayList<int[][]>> listaPossiveisMovimentosPeca(int x, int y, int regiao, int[][] tabuleiro) {
        ArrayList<ArrayList<int[][]>> possiveisMovimentos = new ArrayList<ArrayList<int[][]>>();
        int[][] regioes = {{1,0},{0,1},{1,1},{-1,-1},{-1,1},{1,-1},{-1,0},{0,-1}};
        if(regiao == 2) {
            for(int i=0; i < regioes.length; i++)
                for(int j=0; j < regioes[0].length; j++)
                    regioes[i][j] *= 2;
        }
        
        for(int i=0; i < regioes.length; i++) {
            int novoX = x+regioes[i][0];
            int novoY = y+regioes[i][1];
            int[][] movimento = {{x, y}, {novoX, novoY}};
            if(verificaMovimento(movimento, tabuleiro)) {
                ArrayList<int[][]> possibilidade = new ArrayList<int[][]>();
                possibilidade.add(movimento);
                if(regiao == 2) {
                    int[][] novoTabuleiro = criaCopiaTabuleiro(tabuleiro);
                    fazMovimento(movimento, novoTabuleiro);
                    ArrayList<int[]> pecasEliminadas = new ArrayList<int[]>();
                    pecasEliminadas.add(posPecaEntrePecas(movimento[0][0], movimento[0][1], movimento[1][0], movimento[1][1]));
                    retiraPecas(pecasEliminadas, novoTabuleiro);
                    ArrayList<ArrayList<int[][]>> novosPossiveisMovimentos = listaPossiveisMovimentosPeca(novoX, novoY, 2, novoTabuleiro);

                    if(novosPossiveisMovimentos.isEmpty()) {
                        possiveisMovimentos.add(possibilidade);
                    }
                    else {
                        for(int j=0; j<novosPossiveisMovimentos.size(); j++) {
                            ArrayList<int[][]> novaPossibilidade = new ArrayList<int[][]>();
                            novaPossibilidade.addAll(possibilidade);
                            novaPossibilidade.addAll(novosPossiveisMovimentos.get(j));
                            possiveisMovimentos.add(novaPossibilidade);
                        }
                    }
                }
                else {
                    possiveisMovimentos.add(possibilidade);
                }
            }
        }
        return possiveisMovimentos;
    }
    private ArrayList<Jogada> listaPossiveisJogadasRegiao(int corPeca, int regiao, int[][] tabuleiro) {
        ArrayList<ArrayList<int[][]>> possiveisMovimentos = new ArrayList<ArrayList<int[][]>>();
        ArrayList<Jogada> possiveisJogadas = new ArrayList<Jogada>();
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    possiveisMovimentos.addAll(listaPossiveisMovimentosPeca(x, y, regiao, tabuleiro));
                }
            }
        }
        for(ArrayList<int[][]> movimentos : possiveisMovimentos) {
            ArrayList<int[]> pecasEliminadas = new ArrayList<int[]>();
            for(int[][] movimento : movimentos) {
                if(comeuPeca(movimento)) {
                    pecasEliminadas.add(posPecaEntrePecas(movimento[0][0], movimento[0][1], movimento[1][0], movimento[1][1]));
                }
            }
            possiveisJogadas.add(new Jogada(corPeca, movimentos, pecasEliminadas));
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
    
    private Jogada jogadaDaLista(int xInicial, int yInicial, int xFinal, int yFinal, ArrayList<Jogada> possiveisJogadas) {
        for(int i=0; i< possiveisJogadas.size(); i++) {
            if(possiveisJogadas.get(i).getMovimentos().get(0)[0][0] == xInicial && 
            possiveisJogadas.get(i).getMovimentos().get(0)[0][1] == yInicial && 
            possiveisJogadas.get(i).getMovimentos().get(possiveisJogadas.get(i).getMovimentos().size()-1)[1][0] == xFinal && 
            possiveisJogadas.get(i).getMovimentos().get(possiveisJogadas.get(i).getMovimentos().size()-1)[1][1] == yFinal) {
                return possiveisJogadas.get(i);
            }
        }
        return null;
    }
    @Override
    public void interpretaJogadaPlayer(int[] posClick) {
        if(!isSelecionado()) {
            setPosSelecionado(posClick);
            if(getTabuleiro()[posClick[0]][posClick[1]] == getPecaPlayer()) {
                setSelecionado(true);
            }
        }
        else {
            ArrayList<Jogada> possiveisJogadas = listaPossiveisJogadas(getPecaPlayer(), getTabuleiro());
            Jogada jogada = jogadaDaLista(getPosSelecionado()[0], getPosSelecionado()[1], posClick[0], posClick[1], possiveisJogadas);
            if(!Objects.isNull(jogada)) {
                setJogadaDoPlayer(jogada);
                setVezDoPlayer(false);
                setSelecionado(false);
            }
            else if(getTabuleiro()[posClick[0]][posClick[1]] == getPecaPlayer()) {
                setPosSelecionado(posClick);
            }
            else {
                setSelecionado(false);
            }
        }
    }
}
    
