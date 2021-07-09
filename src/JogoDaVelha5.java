import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class JogoDaVelha5 extends Jogo {
    private static final int ALTURA_TABULEIRO = 5;
    private static final int LARGURA_TABULEIRO = 5;
    private static final int SEM_PECA = 0;
    private static final int PECA_BRANCA = 1;
    private static final int PECA_PRETA = 2;
    private static final int TAMANHO_PECA = (int)LARGURA_TELA/(2*LARGURA_TABULEIRO);
    private boolean vezDoPlayer = false;
    private int pecaPlayer = PECA_BRANCA;

    JogoDaVelha5() {
        super();
        setNome("Jogo da Velha 5");
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
    public int getPecaPlayer() {
        return pecaPlayer;
    }
    public void setPecaPlayer(int pecaPlayer) {
        this.pecaPlayer = pecaPlayer;
    }

    public void partidaBotXPlayer() throws InterruptedException {
        inicializaTabuleiro();
        iniciaTimer();
        float chance;
        while(!verificaVitoria(PECA_BRANCA) && !verificaVitoria(PECA_PRETA)) {
            setVezDoPlayer(true);
            System.out.println("Vez das peças brancas");
            while(isVezDoPlayer()) {
                Thread.sleep(1);
            }
            if(!verificaVitoria(PECA_BRANCA)) {
                System.out.println("Vez das peças pretas");
                chance = maquinaJoga(PECA_PRETA,5);
                System.out.println("Chance de vitória das peças pretas: "+chance+"%");
            }
        }
        paraTimer();
    }
    public void partidaBotXBot() throws InterruptedException {
        inicializaTabuleiro();
        iniciaTimer();
        float chance;
        while(!verificaVitoria(PECA_BRANCA) && !verificaVitoria(PECA_PRETA)) {
            System.out.println("Vez das peças brancas");
            chance = maquinaJoga(PECA_BRANCA,5);
            System.out.println("Chance de vitória das peças brancas: "+chance+"%");
            Thread.sleep(500);
            if(!verificaVitoria(PECA_BRANCA)) {
                System.out.println("Vez das peças pretas");
                chance = maquinaJoga(PECA_PRETA,5);
                System.out.println("Chance de vitória das peças pretas: "+chance+"%");
                Thread.sleep(250);
            }
        }
        getTimer().stop();
        repaint();
    }
    public void inicializaTabuleiro() {
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                getTabuleiro()[x][y] = SEM_PECA;
            }
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
                        g.setColor(Color.black);
                        g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        break;
                    case PECA_PRETA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, ALTURA_TABULEIRO);
                        g.setColor(Color.black);
                        g.fillOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        break;
                }
            }
        }
    }
    @Override
    public void desenhaTabuleiro(Graphics g) {
        desenhaPecas(g);
    }
    public void fazJogada(int x, int y, int peca) {
        getTabuleiro()[x][y] = peca;
    } 
    public int[][] fazJogada(int x, int y, int peca, int[][] tabuleiro) {
        tabuleiro[x][y] = peca;
        return tabuleiro;
    } 
    public boolean estaNosLimites(int x, int y) {
        if(x>=0 && x<LARGURA_TABULEIRO && y>=0 && y<ALTURA_TABULEIRO)
            return true;
        else
            return false;
    }
    public boolean verificaJogada(int x, int y) {
        if (estaNosLimites(x, y) && getTabuleiro()[x][y] == SEM_PECA) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean verificaJogada(int x, int y, int[][] tabuleiro) {
        if (estaNosLimites(x, y) && tabuleiro[x][y] == SEM_PECA) {
            return true;
        }
        else {
            return false;
        }
    }
    public void printaPossiveisJogadas() {
        ArrayList<int[]> possiveisJogadas = listaPossiveisJogadas(getTabuleiro());
        for(int i=0; i < possiveisJogadas.size(); i++) {
            System.out.println("("+possiveisJogadas.get(i)[0]+", "+possiveisJogadas.get(i)[1]+") -> ("+possiveisJogadas.get(i)[2]+", "+possiveisJogadas.get(i)[3]+")");
        }
    }
    public ArrayList<int[]> listaPossiveisJogadas(int[][] tabuleiro) {
        ArrayList<int[]> possiveisJogadas = new ArrayList<int[]>();
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(verificaJogada(x,y,tabuleiro)) {
                    int[] possibilidade = new int[2];
                    possibilidade[0] = x;
                    possibilidade[1] = y;
                    possiveisJogadas.add(possibilidade);
                }
            }
        }
        return possiveisJogadas;
    }
    public int maximoAlinhado(int corPeca) {
        int maximo = 0;
        int contagem;
        
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            contagem = 0;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(getTabuleiro()[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            contagem = 0;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(getTabuleiro()[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(getTabuleiro()[x][x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(getTabuleiro()[x][x-1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }

        if (maximo >= 4) {
            return maximo;
        }

        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(getTabuleiro()[x][x+1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }

        if (maximo >= 4) {
            return maximo;
        }

        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(getTabuleiro()[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(getTabuleiro()[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(getTabuleiro()[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        return maximo;
    }
    public int maximoAlinhado(int corPeca, int[][] tabuleiro) {
        int maximo = 0;
        int contagem;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            contagem = 0;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            contagem = 0;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(tabuleiro[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x-1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(tabuleiro[x][x+1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        } 
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(tabuleiro[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(tabuleiro[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(tabuleiro[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        return maximo;
    }
    public boolean verificaVitoria(int corPeca) {
        if(maximoAlinhado(corPeca) >= 4) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean verificaVitoria(int corPeca, int[][] tabuleiro) {
        if(maximoAlinhado(corPeca, tabuleiro) >= 4) {
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
        ArvoreDeJogadas j = new ArvoreDeJogadas();
        return j.geraPontosAleatorios();
    }

    public float geraCusto(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        if(corPeca == PECA_PRETA) {
            if(verificaVitoria(PECA_PRETA, tabuleiro)) {
                return maxPontos;
            }
            else if(verificaVitoria(PECA_BRANCA, tabuleiro)) {
                return minPontos;
            }
            else{
                return geraCustoPeca(PECA_PRETA, tabuleiro, minPontos, maxPontos)*0.3f + geraCustoPeca(PECA_BRANCA, tabuleiro, minPontos, maxPontos)*-0.7f;
            }
        }
        else {
            if(verificaVitoria(PECA_BRANCA, tabuleiro)) {
                return maxPontos;
            }
            else if(verificaVitoria(PECA_PRETA, tabuleiro)) {
                return minPontos;
            }
            else{
                return geraCustoPeca(PECA_BRANCA, tabuleiro, minPontos, maxPontos)*0.3f + geraCustoPeca(PECA_PRETA, tabuleiro, minPontos, maxPontos)*-0.7f;
            }
        }
        
    }
    public void constroiArvoreDeJogadas(int corPecaJogador, int corPecaAtual, ArvoreDeJogadas jogadas, int profundidadeMax) {
        ArrayList<int[]> possiveisJogadas = listaPossiveisJogadas(jogadas.getCopiaTabuleiro());
        if(profundidadeMax == 0 || possiveisJogadas.size() == 0 || verificaFimDeJogo(jogadas.getCopiaTabuleiro())) {
            jogadas.setPontos((int)geraCusto(corPecaJogador, jogadas.getCopiaTabuleiro(), jogadas.getMinPontos(), jogadas.getMaxPontos()));
            jogadas.setProfundidade(0);
        }
        else {
            for(int i=0; i < possiveisJogadas.size(); i++) {
                ArvoreDeJogadas proximasJogadas = new ArvoreDeJogadas();
                proximasJogadas.setDificulty(3);
                int[][] novoTabuleiro = criaCopiaTabuleiro(jogadas.getCopiaTabuleiro());
                novoTabuleiro = fazJogada(possiveisJogadas.get(i)[0], possiveisJogadas.get(i)[1], corPecaAtual, novoTabuleiro);
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
        @Override
        public void mouseClicked(MouseEvent e) {
            int[] pos = {e.getX(), e.getY()};
            setPosClick(pos);
            if(isVezDoPlayer()) {
                int[] posJogada = {(int)(getPosClick()[0]*LARGURA_TABULEIRO)/LARGURA_TELA,
                                 (int)(getPosClick()[1]*ALTURA_TABULEIRO)/ALTURA_TELA};
                if(verificaJogada(posJogada[0], posJogada[1])) {
                    fazJogada(posJogada[0], posJogada[1],getPecaPlayer());
                    setVezDoPlayer(false);
                }
            }
        }
    }

    public boolean verificaIgualdade(int [][] tabuleiro){
        for(int x = 0; x < LARGURA_TABULEIRO; x++){
            for(int y = 0; y < ALTURA_TABULEIRO; y++){
                if(getTabuleiro()[x][y] != tabuleiro[x][y]){
                    return false;
                }
            }
        }
        return true;
    }

    public int[][] criaCopiaHorizontalTabuleiro(){
        int tabuleiro[][] = new int [LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        for(int x = 0; x < LARGURA_TABULEIRO; x++){
            for(int y = 0; y < ALTURA_TABULEIRO; y++){
                tabuleiro[x][y] = getTabuleiro()[x][ALTURA_TABULEIRO-y];
            }
        }
        return tabuleiro;
    }

    public int[][] criaCopiaVerticalTabuleiro(){
        int tabuleiro[][] = new int [LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        for(int x = 0; x < LARGURA_TABULEIRO; x++){
            for(int y = 0; y < ALTURA_TABULEIRO; y++){
                tabuleiro[x][y] = getTabuleiro()[LARGURA_TABULEIRO-x][y];
            }
        }
        return tabuleiro;
    }
    public int[][] criaCopiaDiagonalTabuleiro(){
        int tabuleiro[][] = new int [LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        for(int x = 0; x < LARGURA_TABULEIRO; x++){
            for(int y = 0; y < ALTURA_TABULEIRO; y++){
                tabuleiro[x][y] = getTabuleiro()[y][x];
            }
        }
        return tabuleiro;
    }

    public int[][] criaCopiaOutraDiagonalTabuleiro(){
        int tabuleiro[][] = new int [LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        for(int x = 0; x < LARGURA_TABULEIRO; x++){
            for(int y = ALTURA_TABULEIRO-1; y >= 0; y--){
                tabuleiro[x][y] = getTabuleiro()[y][LARGURA_TABULEIRO-x];
            }
        }
        return tabuleiro;
    }

}
