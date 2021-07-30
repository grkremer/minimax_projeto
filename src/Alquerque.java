import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class Alquerque extends Jogo {
    private static final int ALTURA_TABULEIRO = 5;
    private static final int LARGURA_TABULEIRO = 5;
    private static final int SEM_PECA = 0;
    private static final int PECA_BRANCA = 1;
    private static final int PECA_PRETA = 2;
    private static final int TAMANHO_PECA = (int)LARGURA_TELA/(2*LARGURA_TABULEIRO);
    private boolean vezDoPlayer = false;
    private boolean selecionado = false;
    private int[] posSelecionado = {0, 0};

    Alquerque() {
        super();
        setNome("Alquerque");
        setTabuleiro(new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO]);
        setBackground(Color.white);
        addMouseListener(new EventosMouse());
    }
    public boolean isVezDoPlayer() {
        return vezDoPlayer;
    }
    public void setVezDoPlayer(boolean vezDoPlayer) {
        this.vezDoPlayer = vezDoPlayer;
    }
    public boolean isSelecionado() {
        return selecionado;
    }
    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
    public int[] getPosSelecionado() {
        return posSelecionado;
    }
    public void setPosSelecionado(int[] posSelecionado) {
        this.posSelecionado = posSelecionado;
    }

    public void partidaBotXPlayer() throws InterruptedException {
        inicializaTabuleiro();
        iniciaTimer();
        float chance;
        while(!verificaVitoria(PECA_BRANCA, getTabuleiro()) && !verificaVitoria(PECA_PRETA, getTabuleiro())) {
            setVezDoPlayer(true);
            System.out.println("Vez das peças brancas");
            while(isVezDoPlayer()) {
                Thread.sleep(1);
            }
            if(!verificaVitoria(PECA_BRANCA, getTabuleiro())) {
                System.out.println("Vez das peças pretas");
                chance = maquinaJoga(PECA_PRETA,6);
                System.out.println("Chance de vitória das peças pretas: "+chance+"%");
            }
        }
        paraTimer();
    }
    public void partidaBotXBot() throws InterruptedException {
        inicializaTabuleiro();
        iniciaTimer();
        float chance;
        while(!verificaVitoria(PECA_BRANCA, getTabuleiro()) && !verificaVitoria(PECA_PRETA, getTabuleiro())) {
            System.out.println("Vez das peças brancas");
            chance = maquinaJoga(PECA_BRANCA,1);
            System.out.println("Chance de vitória das peças brancas: "+chance+"%");
            Thread.sleep(500);
            if(!verificaVitoria(PECA_BRANCA, getTabuleiro())) {
                System.out.println("Vez das peças pretas");
                chance = maquinaJoga(PECA_PRETA,5);
                System.out.println("Chance de vitória das peças pretas: "+chance+"%");
                Thread.sleep(250);
            }
        }
        paraTimer();
    }
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
    private int calculaPosicaoFila(int posicaoObjeto, int tamanhoObjeto, int pixelsFila, int tamanhoFila) {
        return ((pixelsFila/(tamanhoFila+1)) *(posicaoObjeto+1)) - (tamanhoObjeto/2);
    }
    private void desenhaPecas(Graphics g) {
        int posicaoX;
        int posicaoY;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                switch(getTabuleiro()[x][y]) {
                    case SEM_PECA: 
                        posicaoX = calculaPosicaoFila(x, (int)(TAMANHO_PECA/1.5), LARGURA_TELA, LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, (int)(TAMANHO_PECA/1.5), ALTURA_TELA, ALTURA_TABULEIRO);
                        g.setColor(Color.lightGray);
                        g.fillOval(posicaoX, posicaoY, (int)(TAMANHO_PECA/1.5), (int)(TAMANHO_PECA/1.5));
                        break;
                    case PECA_BRANCA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, ALTURA_TABULEIRO);
                        g.setColor(Color.white);
                        g.fillOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        if(isSelecionado() && getPosSelecionado()[0] == x && getPosSelecionado()[1] == y) {
                            g.setColor(Color.blue);
                            g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        }
                        else {
                            g.setColor(Color.black);
                            g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        }
                        break;
                    case PECA_PRETA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, ALTURA_TABULEIRO);
                        g.setColor(Color.black);
                        g.fillOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        if(isSelecionado() && getPosSelecionado()[0] == x && getPosSelecionado()[1] == y) {
                            g.setColor(Color.blue);
                            g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        }
                        break;
                }
            }
        }
    }
    private void desenhaLinhas(Graphics g) {
        g.setColor(Color.black);
        int inicioX;
        int inicioY;
        int fimX;
        int fimY;
        for(int x=0; x<LARGURA_TABULEIRO; x++) {
            inicioX = calculaPosicaoFila(x, 1, LARGURA_TELA, LARGURA_TABULEIRO);
            inicioY = calculaPosicaoFila(0, 1, ALTURA_TELA, ALTURA_TABULEIRO);
            fimX = inicioX;
            fimY = calculaPosicaoFila(ALTURA_TABULEIRO-1, 1, ALTURA_TELA, ALTURA_TABULEIRO);
            g.drawLine(inicioX, inicioY, fimX, fimY);
        }
        for(int y=0; y<LARGURA_TABULEIRO; y++) {
            inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, LARGURA_TABULEIRO);
            inicioY = calculaPosicaoFila(y, 1, ALTURA_TELA, ALTURA_TABULEIRO);
            fimX = calculaPosicaoFila(LARGURA_TABULEIRO-1, 1, LARGURA_TELA, LARGURA_TABULEIRO);
            fimY = inicioY;
            g.drawLine(inicioX, inicioY, fimX, fimY);
        }
        inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        inicioY = calculaPosicaoFila(0, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        fimX = calculaPosicaoFila(LARGURA_TABULEIRO-1, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(ALTURA_TABULEIRO-1, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(LARGURA_TABULEIRO-1, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        fimX = calculaPosicaoFila(0, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        inicioY = calculaPosicaoFila((int)Math.ceil(ALTURA_TABULEIRO/2), 1, ALTURA_TELA, ALTURA_TABULEIRO);
        fimX = calculaPosicaoFila((int)Math.ceil(LARGURA_TABULEIRO/2), 1, LARGURA_TELA, LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(0, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
        fimY = calculaPosicaoFila(ALTURA_TABULEIRO-1, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(LARGURA_TABULEIRO-1, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(0, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
        fimY = calculaPosicaoFila(ALTURA_TABULEIRO-1, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
    }
    @Override
    public void desenhaTabuleiro(Graphics g) {
        desenhaLinhas(g);
        desenhaPecas(g);
    }
    public int[][] fazJogada(int xInicial, int yInicial, int xFinal, int yFinal, int[][] tabuleiro) {
        tabuleiro[xFinal][yFinal] = tabuleiro[xInicial][yInicial];
        tabuleiro[xInicial][yInicial] = SEM_PECA;
        if(Math.abs(xFinal - xInicial) == 2 || Math.abs(yFinal - yInicial) == 2) {
            int[] posPecaEntrePecas = posPecaEntrePecas(xInicial, yInicial, xFinal, yFinal, tabuleiro);
            tabuleiro[posPecaEntrePecas[0]][posPecaEntrePecas[1]] = SEM_PECA;
        }
        return tabuleiro;
    } 
    public boolean estaNosLimites(int x, int y) {
        if(x>=0 && x<LARGURA_TABULEIRO && y>=0 && y<ALTURA_TABULEIRO)
            return true;
        else
            return false;
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
    private int invertePeca(int peca) {
        if(peca == PECA_BRANCA) {
            return PECA_PRETA;
        }
        else {
            return PECA_BRANCA;
        }
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
    public void printaPossiveisJogadas(int corPeca) {
        ArrayList<ArrayList<Integer>> possiveisJogadas = listaPossiveisJogadas(corPeca,getTabuleiro());
        for(int i=0; i < possiveisJogadas.size(); i++) {
            System.out.println("("+possiveisJogadas.get(i).get(0)+", "+possiveisJogadas.get(i).get(1)+") -> ("+possiveisJogadas.get(i).get(possiveisJogadas.get(i).size()-2)+", "+possiveisJogadas.get(i).get(possiveisJogadas.get(i).size()-1)+")");
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
            if(verificaJogada(x,y,novoX,novoY,tabuleiro)) {
                ArrayList<Integer> possibilidade = new ArrayList<Integer>();
                possibilidade.add(x);
                possibilidade.add(y);
                possibilidade.add(novoX);
                possibilidade.add(novoY);
                if(regiao == 2) {
                    int[][] novoTabuleiro = criaCopiaTabuleiro(tabuleiro);
                    novoTabuleiro = fazJogada(x, y, novoX, novoY, novoTabuleiro);
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
    public ArrayList<ArrayList<Integer>> listaPossiveisJogadas(int corPeca, int[][] tabuleiro) {
        ArrayList<ArrayList<Integer>> possiveisJogadas;
        
        possiveisJogadas = testaPossiveisJogadas(corPeca, 2, tabuleiro);

        if(possiveisJogadas.isEmpty()) {
            possiveisJogadas = testaPossiveisJogadas(corPeca, 1, tabuleiro);
        }
        return possiveisJogadas;
    }
    private int nPecas(int corPeca, int[][] tabuleiro) {
        int cont = 0;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) cont++;
            }
        }
        return cont;
    }
    public boolean verificaVitoria(int corPeca, int[][] tabuleiro) {
        if(nPecas(invertePeca(corPeca), tabuleiro) == 0) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean verificaFimDeJogo(int[][] tabuleiro) {
        return verificaVitoria(PECA_BRANCA, tabuleiro) || verificaVitoria(PECA_PRETA, tabuleiro);
    }
    
    private int[][] criaCopiaTabuleiro(int[][] tabuleiro) {
        int[][] novoTabuleiro = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                novoTabuleiro[x][y] = tabuleiro[x][y];
            }
        }
        return novoTabuleiro;
    }
    
    private float geraCustoPeca(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        float custo = normalizaPontuacao(0f, 12f, (float)minPontos, (float)maxPontos, 12f - (float)nPecas(invertePeca(corPeca), tabuleiro));
        return custo;
    }

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
    public void constroiArvoreDeJogadas(int corPecaJogador, int corPecaAtual, ArvoreDeJogadas jogadas, int profundidadeMax) {
        ArrayList<ArrayList<Integer>> possiveisJogadas = listaPossiveisJogadas(corPecaAtual,jogadas.getCopiaTabuleiro());
        if(profundidadeMax == 0 || possiveisJogadas.size() == 0 || verificaFimDeJogo(jogadas.getCopiaTabuleiro())) {
            jogadas.setPontos((int)geraCusto(corPecaJogador, jogadas.getCopiaTabuleiro(), jogadas.getMinPontos(), jogadas.getMaxPontos()));
            jogadas.setProfundidade(0);
        }
        else {
            for(int i=0; i < possiveisJogadas.size(); i++) {
                ArvoreDeJogadas proximasJogadas = new ArvoreDeJogadas();
                proximasJogadas.setDificulty(3);

                int[][] novoTabuleiro = criaCopiaTabuleiro(jogadas.getCopiaTabuleiro());
                for(int j=0; (j+3)<possiveisJogadas.get(i).size(); j+=2){
                    novoTabuleiro = fazJogada(possiveisJogadas.get(i).get(j), possiveisJogadas.get(i).get(j+1), possiveisJogadas.get(i).get(j+2), possiveisJogadas.get(i).get(j+3), novoTabuleiro);
                }
            
                proximasJogadas.setCopiaTabuleiro(novoTabuleiro);
                if(corPecaAtual == PECA_BRANCA) {
                    constroiArvoreDeJogadas(corPecaJogador, PECA_PRETA, proximasJogadas, profundidadeMax-1);
                }
                else {
                    constroiArvoreDeJogadas(corPecaJogador, PECA_BRANCA, proximasJogadas, profundidadeMax-1);
                }
                jogadas.addFilho(proximasJogadas);
            }

            int maiorProfundidadeFilho = 0;
            for(int i=0; i < jogadas.getFilhos().size(); i++) {
                maiorProfundidadeFilho = Math.max(maiorProfundidadeFilho, jogadas.getFilho(i).getProfundidade());
            }
            jogadas.setProfundidade(maiorProfundidadeFilho+1);
        }
    }
    public ArvoreDeJogadas constroiArvoreDeJogadas(int corPeca, int profundidadeMax) {
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas();
        jogadas.setDificulty(3);
        jogadas.setCopiaTabuleiro(criaCopiaTabuleiro(getTabuleiro()));
        constroiArvoreDeJogadas(corPeca, corPeca, jogadas, profundidadeMax);
        return jogadas;
    }
    public float maquinaJoga(int corPeca, int profundidade) {
        ArvoreDeJogadas jogadas = constroiArvoreDeJogadas(corPeca, profundidade);
        Collections.shuffle(jogadas.getFilhos());
        jogadas.minimaxAlphaBeta();

        int pontuacaoMaxima = Integer.MIN_VALUE;
        int profundidadeMinima = Integer.MAX_VALUE;
        for(int i=0; i < jogadas.getFilhos().size(); i++) {
            if(jogadas.getFilho(i).isAcessado()) {
                if(pontuacaoMaxima < jogadas.getFilho(i).getPontos()) {
                    setTabuleiro(jogadas.getFilho(i).getCopiaTabuleiro());
                    pontuacaoMaxima = jogadas.getFilho(i).getPontos();
                    if(pontuacaoMaxima == jogadas.getMaxPontos()) {
                        profundidadeMinima = jogadas.getFilho(i).getProfundidade();
                    }
                }
                else if(jogadas.getFilho(i).getPontos() == jogadas.getMaxPontos()) {
                    if(jogadas.getFilho(i).getProfundidade() < profundidadeMinima) {
                        setTabuleiro(jogadas.getFilho(i).getCopiaTabuleiro());
                        profundidadeMinima = jogadas.getFilho(i).getProfundidade();
                    }
                }
            }
        }
        return normalizaPontuacao(jogadas.getMinPontos(), jogadas.getMaxPontos(), 0, 100, (float)pontuacaoMaxima);
    }

    private float normalizaPontuacao(float minimoAntigo, float maximoAntigo, float minimoNovo, float maximoNovo, float valor){
        return ((valor-minimoAntigo)/(maximoAntigo-minimoAntigo) * (maximoNovo-minimoNovo) + minimoNovo);
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
                        for(int j=0; (j+3)<jogada.size(); j+=2){
                            setTabuleiro(fazJogada(jogada.get(j), jogada.get(j+1), jogada.get(j+2), jogada.get(j+3), getTabuleiro()));
                        }
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
    
