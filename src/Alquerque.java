import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Alquerque extends Jogo {
    Alquerque() {
        super();
        setNome("Alquerque");
        setProfundidade(6);
        addMouseListener(new EventosMouse());
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
    public int[][] fazMovimento(ArrayList<Integer> jogada, int[][] tabuleiro) {
        int xInicial = jogada.get(0);
        int yInicial = jogada.get(1);
        int xFinal = jogada.get(2);
        int yFinal = jogada.get(3);
        tabuleiro[xFinal][yFinal] = tabuleiro[xInicial][yInicial];
        tabuleiro[xInicial][yInicial] = SEM_PECA;
        if(Math.abs(xFinal - xInicial) == 2 || Math.abs(yFinal - yInicial) == 2) {
            int[] posPecaEntrePecas = posPecaEntrePecas(xInicial, yInicial, xFinal, yFinal, tabuleiro);
            tabuleiro[posPecaEntrePecas[0]][posPecaEntrePecas[1]] = SEM_PECA;
        }
        return tabuleiro;
    }
    
    private int pecaEntrePecas(int x1, int y1, int x2, int y2, int[][] tabuleiro) {
        int[] posNovo = posPecaEntrePecas(x1, y1, x2, y2, tabuleiro);
        return tabuleiro[posNovo[0]][posNovo[1]];
    }
    private int[] posPecaEntrePecas(int x1, int y1, int x2, int y2, int[][] tabuleiro) {
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
    public boolean verificaJogada(int xInicial, int yInicial, int xFinal, int yFinal, int[][] tabuleiro) {
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
    private ArrayList<ArrayList<Integer>> testaPossiveisJogadaPeca(int x, int y, int regiao, int[][] tabuleiro) {
        ArrayList<ArrayList<Integer>> possiveisJogadas = new ArrayList<ArrayList<Integer>>();
        int[][] regioes = {{1,0},{0,1},{1,1},{-1,-1},{-1,1},{1,-1},{-1,0},{0,-1}};
        if(regiao == 2) {
            for(int i=0; i < regioes.length; i++)
                for(int j=0; j < regioes[0].length; j++)
                    regioes[i][j] *= 2;
        }
        
        for(int i=0; i < regioes.length; i++) {
            int novoX = x+regioes[i][0];
            int novoY = y+regioes[i][1];
            ArrayList<Integer> jogada = new ArrayList<>(List.of(x, y, novoX, novoY));
            if(verificaJogada(x,y,novoX,novoY,tabuleiro)) {
                ArrayList<Integer> possibilidade = new ArrayList<Integer>();
                possibilidade.add(x);
                possibilidade.add(y);
                possibilidade.add(novoX);
                possibilidade.add(novoY);
                if(regiao == 2) {
                    int[][] novoTabuleiro = criaCopiaTabuleiro(tabuleiro);
                    novoTabuleiro = fazJogada(jogada, novoTabuleiro);
                    ArrayList<ArrayList<Integer>> novasPossiveisJogadas = new ArrayList<ArrayList<Integer>>();
                    novasPossiveisJogadas = testaPossiveisJogadaPeca(novoX, novoY, 2, novoTabuleiro);

                    if(novasPossiveisJogadas.isEmpty()) {
                        possiveisJogadas.add(possibilidade);
                    }
                    else {
                        for(int j=0; j<novasPossiveisJogadas.size(); j++) {
                            novasPossiveisJogadas.get(j).remove(0);
                            novasPossiveisJogadas.get(j).remove(0);
                            possibilidade.addAll(novasPossiveisJogadas.get(j));
                            possiveisJogadas.add(possibilidade);

                            possibilidade = new ArrayList<Integer>();
                            possibilidade.add(x);
                            possibilidade.add(y);
                            possibilidade.add(novoX);
                            possibilidade.add(novoY);
                        }
                    }
                }
                else {
                    possiveisJogadas.add(possibilidade);
                }
            }
        }
        return possiveisJogadas;
    }
    private ArrayList<ArrayList<Integer>> testaPossiveisJogadas(int corPeca, int regiao, int[][] tabuleiro) {
        ArrayList<ArrayList<Integer>> possiveisJogadas = new ArrayList<ArrayList<Integer>>();
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    possiveisJogadas.addAll(testaPossiveisJogadaPeca(x, y, regiao, tabuleiro));
                }
            }
        }
        return possiveisJogadas;
    }
    @Override
    public ArrayList<ArrayList<Integer>> listaPossiveisJogadas(int corPeca, int[][] tabuleiro) {
        ArrayList<ArrayList<Integer>> possiveisJogadas;
        
        possiveisJogadas = testaPossiveisJogadas(corPeca, 2, tabuleiro);

        if(possiveisJogadas.isEmpty()) {
            possiveisJogadas = testaPossiveisJogadas(corPeca, 1, tabuleiro);
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
    public ArrayList<Integer> jogadaDaMaquina(int corPeca, int profundidade) {
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas(this, getTabuleiro(), corPeca, corPeca, profundidade);
        Collections.shuffle(jogadas.getFilhos());
        jogadas.minimaxAlphaBeta();

        int pontuacaoMaxima = Integer.MIN_VALUE;
        int profundidadeMinima = Integer.MAX_VALUE;
        ArrayList<Integer> melhorJogada = jogadas.getFilho(0).getJogada();
        for(int i=0; i < jogadas.getFilhos().size(); i++) {
            if(jogadas.getFilho(i).isAcessado()) {
                if(pontuacaoMaxima < jogadas.getFilho(i).getPontos()) {
                    melhorJogada = jogadas.getFilho(i).getJogada();
                    pontuacaoMaxima = jogadas.getFilho(i).getPontos();
                    if(pontuacaoMaxima == jogadas.getMaxPontos()) {
                        profundidadeMinima = jogadas.getFilho(i).getProfundidade();
                    }
                }
                else if(jogadas.getFilho(i).getPontos() == jogadas.getMaxPontos()) {
                    if(jogadas.getFilho(i).getProfundidade() < profundidadeMinima) {
                        melhorJogada = jogadas.getFilho(i).getJogada();
                        profundidadeMinima = jogadas.getFilho(i).getProfundidade();
                    }
                }
            }
        }
        float chance = normalizaPontuacao(jogadas.getMinPontos(), jogadas.getMaxPontos(), 0, 100, (float)pontuacaoMaxima);
        System.out.println("Chance de vitória: "+chance+"%");
        return melhorJogada;
    }

    public class EventosMouse extends MouseAdapter {
        private int[] posClick;
        public int[] getPosClick() {
            return posClick;
        }
        public void setPosClick(int[] posClick) {
            this.posClick = posClick;
        }
        private ArrayList<Integer> jogadaDaLista(int xInicial, int yInicial, int xFinal, int yFinal, ArrayList<ArrayList<Integer>> possiveisJogadas) {
            for(int i=0; i< possiveisJogadas.size(); i++) {
                if(possiveisJogadas.get(i).get(0) == xInicial && 
                possiveisJogadas.get(i).get(1) == yInicial && 
                possiveisJogadas.get(i).get(possiveisJogadas.get(i).size()-2) == xFinal && 
                possiveisJogadas.get(i).get(possiveisJogadas.get(i).size()-1) == yFinal) {
                    return possiveisJogadas.get(i);
                }
            }
            return new ArrayList<Integer>();
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            int[] pos = {e.getX(), e.getY()};
            setPosClick(pos);
            if(isVezDoPlayer()) {
                if(!isSelecionado()) {
                    int[] posPeca = {(int)(getPosClick()[0]*LARGURA_TABULEIRO)/LARGURA_TELA,
                                    (int)(getPosClick()[1]*ALTURA_TABULEIRO)/ALTURA_TELA};
                    setPosSelecionado(posPeca);
                    if(getTabuleiro()[posPeca[0]][posPeca[1]] == PECA_BRANCA) {
                        setSelecionado(true);
                    }
                }
                else {
                    int[] posJogada = {(int)(getPosClick()[0]*LARGURA_TABULEIRO)/LARGURA_TELA,
                                    (int)(getPosClick()[1]*ALTURA_TABULEIRO)/ALTURA_TELA};
                    ArrayList<ArrayList<Integer>> possiveisJogadas = listaPossiveisJogadas(PECA_BRANCA, getTabuleiro());
                    ArrayList<Integer> jogada = jogadaDaLista(getPosSelecionado()[0], getPosSelecionado()[1], posJogada[0], posJogada[1], possiveisJogadas);
                    if(!jogada.isEmpty()) {
                        setJogadaDoPlayer(jogada);
                        setVezDoPlayer(false);
                        setSelecionado(false);
                    }
                    else if(getTabuleiro()[posJogada[0]][posJogada[1]] == PECA_BRANCA) {
                        setPosSelecionado(posJogada);
                    }
                    else {
                        setSelecionado(false);
                    }
                }
            }
        }
    }
}
    
