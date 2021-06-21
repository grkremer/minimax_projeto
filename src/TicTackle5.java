import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

public class TicTackle5 extends Jogo {
    private static final int ALTURA_TABULEIRO = 5;
    private static final int LARGURA_TABULEIRO = 5;
    private static final int SEM_PECA = 0;
    private static final int PECA_BRANCA = 1;
    private static final int PECA_PRETA = 2;
    private static final int TAMANHO_PECA = (int)LARGURA_TELA/(2*LARGURA_TABULEIRO);
    TicTackle5() {
        super();
        setNome("Tic Tackle 5");
        setTabuleiro(new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO]);
        inicializaTabuleiro();
        setBackground(Color.white);
    }
    public void inicializaTabuleiro() {
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                getTabuleiro()[x][0] = PECA_BRANCA;
            }
            else {
                getTabuleiro()[x][0] = PECA_PRETA;
            }   
        }
        for(int y=1; y < ALTURA_TABULEIRO-1; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                getTabuleiro()[x][y] = SEM_PECA;
            }
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                getTabuleiro()[x][ALTURA_TABULEIRO-1] = PECA_PRETA;
            }
            else {
                getTabuleiro()[x][ALTURA_TABULEIRO-1] = PECA_BRANCA;
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
    public void fazJogada(int xInicial, int yInicial, int xFinal, int yFinal) {
        getTabuleiro()[xFinal][yFinal] = getTabuleiro()[xInicial][yInicial];
        getTabuleiro()[xInicial][yInicial] = SEM_PECA;
    } 
    public int[][] fazJogada(int xInicial, int yInicial, int xFinal, int yFinal, int[][] tabuleiro) {
        tabuleiro[xFinal][yFinal] = tabuleiro[xInicial][yInicial];
        tabuleiro[xInicial][yInicial] = SEM_PECA;
        return tabuleiro;
    } 
    public boolean estaNosLimites(int x, int y) {
        if(x>=0 && x<LARGURA_TABULEIRO && y>=0 && y<ALTURA_TABULEIRO)
            return true;
        else
            return false;
    }
    public boolean verificaJogada(int xInicial, int yInicial, int xFinal, int yFinal) {
        if(estaNosLimites(xInicial, yInicial) && estaNosLimites(xFinal, yFinal)) {
            if (getTabuleiro()[xFinal][yFinal] == SEM_PECA && getTabuleiro()[xInicial][yInicial] != SEM_PECA) {
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
    public boolean verificaJogada(int xInicial, int yInicial, int xFinal, int yFinal, int[][] tabuleiro) {
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
    public void printaPossiveisJogadas(int corPeca) {
        ArrayList<int[]> possiveisJogadas = listaPossiveisJogadas(corPeca,getTabuleiro());
        for(int i=0; i < possiveisJogadas.size(); i++) {
            System.out.println("("+possiveisJogadas.get(i)[0]+", "+possiveisJogadas.get(i)[1]+") -> ("+possiveisJogadas.get(i)[2]+", "+possiveisJogadas.get(i)[3]+")");
        }
    }
    public ArrayList<int[]> listaPossiveisJogadas(int corPeca, int[][] tabuleiro) {
        ArrayList<int[]> possiveisJogadas = new ArrayList<int[]>();
        int[][] regioes = {{1,0},{0,1},{1,1},{-1,-1},{-1,1},{1,-1},{-1,0},{0,-1}};
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    for(int i=0; i < regioes.length; i++) {
                        int novoX = x+regioes[i][0];
                        int novoY = y+regioes[i][1];
                        if(verificaJogada(x,y,novoX,novoY,tabuleiro)) {
                            int[] possibilidade = new int[4];
                            possibilidade[0] = x;
                            possibilidade[1] = y;
                            possibilidade[2] = novoX;
                            possibilidade[3] = novoY;
                            possiveisJogadas.add(possibilidade);
                        }
                    }
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
    
    private int geraCustoPeca(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        int maxAlinhado =  maximoAlinhado(corPeca, tabuleiro);
        double maxDistancia = geraMaiorDistanciaMenor(corPeca, tabuleiro);
        float custo;
        if(maxAlinhado >= 4) {
            custo = maxPontos;
        }
        else {
            int custoLinha = normalizaPontuacao(1.0, 4.0, (double)minPontos, (double)maxPontos, maxAlinhado);
            int custoDistancia = normalizaPontuacao(1.0, Math.sqrt(20), (double)minPontos, (double)maxPontos, maxDistancia);
            int custoDistancia2 = normalizaPontuacao(1.0, Math.sqrt(20), (double)minPontos, (double)maxPontos, geraMaiorDistanciaSegundaMenor(corPeca, tabuleiro));
            custo = custoLinha * 0.5f + custoDistancia * -0.4f + custoDistancia2 * -0.1f;
        }
        return (int)custo;
    }


    private int geraCustoPeca2(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        int maxAlinhado =  maximoAlinhado(corPeca, tabuleiro);
        double maxDistancia = geraMaiorDistanciaMenor(corPeca, tabuleiro);
        float custo;
        if(maxAlinhado >= 4) {
            custo = maxPontos;
        }
        else {
            int custoLinha = normalizaPontuacao(1.0, 4.0, (double)minPontos, (double)maxPontos, maxAlinhado);
            int custoDistancia = normalizaPontuacao(1.0, Math.sqrt(20), (double)minPontos, (double)maxPontos, maxDistancia);
            //int custoDistancia2 = normalizaPontuacao(1.0, Math.sqrt(20), (double)minPontos, (double)maxPontos, geraMaiorDistanciaSegundaMenor(corPeca, tabuleiro));
            custo = custoLinha * 0.5f + custoDistancia * -0.5f;
        }
        return (int)custo;
    }

    public int geraCusto(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        if(corPeca == PECA_PRETA) {
            ArvoreDeJogadas j = new ArvoreDeJogadas();
            return j.geraPontosAleatorios();
        }
        else {
            if(verificaVitoria(PECA_BRANCA, tabuleiro)) {
                return maxPontos;
            }
            else if(verificaVitoria(PECA_PRETA, tabuleiro)) {
                return minPontos;
            }
            else{
                return (int)(geraCustoPeca(PECA_BRANCA, tabuleiro, minPontos, maxPontos)*0.3f + geraCustoPeca(PECA_PRETA, tabuleiro, minPontos, maxPontos)*-0.7f);
            }
        }
        
    }
    public void constroiArvoreDeJogadas(int corPecaJogador, int corPecaAtual, ArvoreDeJogadas jogadas, int profundidadeMax) {
        ArrayList<int[]> possiveisJogadas = listaPossiveisJogadas(corPecaAtual,jogadas.getCopiaTabuleiro());
        if(profundidadeMax == 0 || possiveisJogadas.size() == 0 || verificaFimDeJogo(jogadas.getCopiaTabuleiro())) {
            jogadas.setPontos(geraCusto(corPecaJogador, jogadas.getCopiaTabuleiro(), jogadas.MIN_PONTOS, jogadas.MAX_PONTOS));
            jogadas.setProfundidade(0);
        }
        else {
            for(int i=0; i < possiveisJogadas.size(); i++) {
                ArvoreDeJogadas proximasJogadas = new ArvoreDeJogadas();
                int[][] novoTabuleiro = criaCopiaTabuleiro(jogadas.getCopiaTabuleiro());
                novoTabuleiro = fazJogada(possiveisJogadas.get(i)[0], possiveisJogadas.get(i)[1], possiveisJogadas.get(i)[2], possiveisJogadas.get(i)[3], novoTabuleiro);
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
        jogadas.setCopiaTabuleiro(criaCopiaTabuleiro(getTabuleiro()));
        constroiArvoreDeJogadas(corPeca, corPeca, jogadas, profundidadeMax);
        return jogadas;
    }
    public void maquinaJoga(int corPeca) {
        ArvoreDeJogadas jogadas = constroiArvoreDeJogadas(corPeca, 4);
        jogadas.minimaxAlphaBeta();
        int pontuacaoMaxima = Integer.MIN_VALUE;
        for(int i=0; i < jogadas.getFilhos().size(); i++) {
            if(jogadas.getFilho(i).isAcessado()) {
                if(pontuacaoMaxima < jogadas.getFilho(i).getPontos()) {
                    setTabuleiro(jogadas.getFilho(i).getCopiaTabuleiro());
                    pontuacaoMaxima = jogadas.getFilho(i).getPontos();
                }
            }
        }
    }
    public double geraMaiorDistanciaMenor(int corPeca, int tabuleiro[][]) {
        int pecas[][] = new int[LARGURA_TABULEIRO][2];
        int i = 0;
        int j = 0;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    pecas[i][0] = x;
                    pecas[i][1] = y;
                    i++;
                }
            }
        }
        double[] distancias = new double[LARGURA_TABULEIRO];
        double distancia = 0;
        
        for (i =0; i < LARGURA_TABULEIRO; i++){
            distancias[i] = Double.POSITIVE_INFINITY;
            for (j = 0; j < LARGURA_TABULEIRO; j++){
                if (i!= j) {
                    distancia =  Math.sqrt(Math.pow((pecas [i][0] - pecas [j][0]),2) + Math.pow((pecas [i][1] - pecas [j][1]),2));
                    distancias[i] = Math.min(distancias[i], distancia);                  
                }
            }
        }
        Arrays.sort(distancias);
        return distancias[LARGURA_TABULEIRO-1];
    }

    public double geraMaiorDistanciaSegundaMenor(int corPeca, int tabuleiro[][]) {
        int pecas[][] = new int[LARGURA_TABULEIRO][2];
        int i = 0;
        int j = 0;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    pecas[i][0] = x;
                    pecas[i][1] = y;
                    i++;
                }
            }
        }
        double[] distancias = new double[LARGURA_TABULEIRO];
        double distancia = 0;
        
        for (i =0; i < LARGURA_TABULEIRO; i++){
            distancias[i] = Double.POSITIVE_INFINITY;
            double menor = Double.POSITIVE_INFINITY;
            
            for (j = 0; j < LARGURA_TABULEIRO; j++){
                if (i!= j) {
                    distancia =  Math.sqrt(Math.pow((pecas [i][0] - pecas [j][0]),2) + Math.pow((pecas [i][1] - pecas [j][1]),2));
                    if (distancia <= menor) {
                        distancias[i] = menor;
                        menor = distancia;
                    }               
                }
            }
        }
        Arrays.sort(distancias);
        return distancias[LARGURA_TABULEIRO-1];
        
    }

    private int normalizaPontuacao(double minimoAntigo, double maximoAntigo, double minimoNovo, double maximoNovo, double valor){
        return (int)((valor-minimoAntigo)/(maximoAntigo-minimoAntigo) * (maximoNovo-minimoNovo) + minimoNovo);
    }
}
    
