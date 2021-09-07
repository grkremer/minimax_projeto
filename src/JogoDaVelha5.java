import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JogoDaVelha5 extends Jogo {
    private static final int MAXIMO_JOGADAS = 15000000;
    //private static final int MAXIMO_JOGADAS = Integer.MAX_VALUE;

    JogoDaVelha5() {
        super();
        setNome("Jogo da Velha 5");
        setProfundidade(5);
        addMouseListener(new EventosMouse());
    }
    
    @Override
    public void inicializaTabuleiro() {
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                getTabuleiro()[x][y] = SEM_PECA;
            }
        }
    }
    @Override
    public int[][] fazJogada(ArrayList<Integer> jogada) throws InterruptedException {
        fazJogada(jogada, getTabuleiro());
        Thread.sleep(DELAY_JOGADA);
        return getTabuleiro();
    } 
    @Override
    public int[][] fazJogada(ArrayList<Integer> jogada, int[][] tabuleiro) {
        tabuleiro[jogada.get(0)][jogada.get(1)] = jogada.get(2);
        return tabuleiro;
    } 
    
    public boolean verificaJogada(int x, int y, int[][] tabuleiro) {
        if (estaNosLimites(x, y) && tabuleiro[x][y] == SEM_PECA) {
            return true;
        }
        else {
            return false;
        }
    }
    
    private boolean condicaoLoopSimetria(int larguraMax, boolean temDiagonal, boolean temOutraDiagonal, int x, int y) {
        boolean condicaoLargura = x < larguraMax;
        boolean condicaoDiagonal = true;
        boolean condicaoOutraDiagonal = true;
        if(temOutraDiagonal) {
            condicaoDiagonal = (x+y) < LARGURA_TABULEIRO;
        }
        if(temDiagonal) {
            condicaoOutraDiagonal = x<=y;
        }
        return condicaoLargura && condicaoDiagonal && condicaoOutraDiagonal;
    }
    
    @Override
    public ArrayList<ArrayList<Integer>> listaPossiveisJogadas(int corPeca, int[][] tabuleiro) {
        ArrayList<ArrayList<Integer>> possiveisJogadas = new  ArrayList<ArrayList<Integer>>();
        boolean horizontal = verificaSimetriaHorizontal(tabuleiro);
        boolean vertical = verificaSimetriaVertical(tabuleiro);
        boolean diagonal = verificaSimetriaDiagonal(tabuleiro);
        boolean outraDiagonal = verificaSimetriaOutraDiagonal(tabuleiro);
        int alturaMax = ALTURA_TABULEIRO;
        int larguraMax = LARGURA_TABULEIRO;

        if(horizontal) {
            alturaMax = ALTURA_TABULEIRO-2;
        }
        if(vertical) {
            larguraMax = LARGURA_TABULEIRO-2;
        }
        for(int y=0; y < alturaMax; y++) {
            for(int x=0; condicaoLoopSimetria(larguraMax,diagonal,outraDiagonal,x,y); x++) {
                if(verificaJogada(x,y,tabuleiro)) {
                    ArrayList<Integer> possibilidade = new ArrayList<Integer>(List.of(x, y, corPeca));
                    possiveisJogadas.add(possibilidade);
                }
            }
        }
        return possiveisJogadas;
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

    public boolean tentaAcharTripla(int corPeca, int[][] tabuleiro) {

        for(int y=0; y < ALTURA_TABULEIRO; y++) //horizontal
            if(tabuleiro[0][y] == SEM_PECA && tabuleiro[4][y] == SEM_PECA)
                if (tabuleiro[1][y] == corPeca && tabuleiro[2][y] == corPeca && tabuleiro[3][y] == corPeca)
                    return true;

        for(int x=0; x < ALTURA_TABULEIRO; x++) //vertical
            if(tabuleiro[x][0] == SEM_PECA && tabuleiro[x][4] == SEM_PECA)
                if (tabuleiro[x][1] == corPeca && tabuleiro[x][2] == corPeca && tabuleiro[x][3] == corPeca)
                    return true;

        if (tabuleiro[0][0] == SEM_PECA && tabuleiro[1][1]== corPeca && tabuleiro [2][2] == corPeca && tabuleiro[3][3] == corPeca && tabuleiro [4][4] == SEM_PECA)
            return true;

        if (tabuleiro[0][4] == SEM_PECA && tabuleiro[1][3]== corPeca && tabuleiro [2][2] == corPeca && tabuleiro[3][1] == corPeca && tabuleiro [4][0] == SEM_PECA)
            return true;
        return false;
    }
    public int contaDuplas(int corPeca, int[][] tabuleiro) {
        int contagem = 0;
        for(int y=0; y < ALTURA_TABULEIRO; y++) //horizontal
            if(tabuleiro[0][y] == SEM_PECA && tabuleiro[3][y] == SEM_PECA)
                if (tabuleiro[1][y] == corPeca && tabuleiro[2][y] == corPeca)
                    contagem ++;
        for(int y=0; y < ALTURA_TABULEIRO; y++) //horizontal
            if(tabuleiro[1][y] == SEM_PECA && tabuleiro[4][y] == SEM_PECA)
                if (tabuleiro[2][y] == corPeca && tabuleiro[3][y] == corPeca)
                    contagem ++;
        //Horizontal

        for(int x=0; x < ALTURA_TABULEIRO; x++) //vertical
            if(tabuleiro[x][0] == SEM_PECA && tabuleiro[x][3] == SEM_PECA)
                if (tabuleiro[x][1] == corPeca && tabuleiro[x][2] == corPeca)
                    contagem ++;
        for(int x=0; x < ALTURA_TABULEIRO; x++) //vertical
            if(tabuleiro[x][1] == SEM_PECA && tabuleiro[x][4] == SEM_PECA)
                if (tabuleiro[x][2] == corPeca && tabuleiro[x][3] == corPeca)
                    contagem ++;
        //Vertical

        if (tabuleiro[0][0] == SEM_PECA && tabuleiro[1][1] == corPeca && tabuleiro [2][2] == corPeca && tabuleiro[3][3] == SEM_PECA)
            contagem ++;
        if (tabuleiro[1][1] == SEM_PECA && tabuleiro [2][2] == corPeca && tabuleiro[3][3] == corPeca && tabuleiro [4][4] == SEM_PECA)
            contagem ++;
        //Diagonal Principal
        
        if (tabuleiro[0][4] == SEM_PECA && tabuleiro[1][3] == corPeca && tabuleiro[2][2] == corPeca && tabuleiro[3][1] == SEM_PECA)
            contagem ++;
        if (tabuleiro[1][3] == SEM_PECA && tabuleiro[2][2] == corPeca && tabuleiro[3][1] == corPeca && tabuleiro [4][0] == SEM_PECA)
            contagem ++;
        //Diagonal Secundaria

        if (tabuleiro[1][0] == SEM_PECA && tabuleiro[2][1] == corPeca && tabuleiro [3][2] == corPeca && tabuleiro[4][3] == SEM_PECA)
            contagem ++;
        if (tabuleiro[0][1] == SEM_PECA && tabuleiro [1][2] == corPeca && tabuleiro[2][3] == corPeca && tabuleiro [3][4] == SEM_PECA)
            contagem ++;
        //Mais Diagonais Principais 
        
        if (tabuleiro[3][0] == SEM_PECA && tabuleiro[2][1] == corPeca && tabuleiro[1][2] == corPeca && tabuleiro[0][3] == SEM_PECA)
            contagem ++;
        if (tabuleiro[4][1] == SEM_PECA && tabuleiro[3][2] == corPeca && tabuleiro[2][3] == corPeca && tabuleiro [1][4] == SEM_PECA)
            contagem ++;
        //Mais Diagonais Secundarias

        return contagem;
    }

    @Override
    public boolean verificaVitoria(int corPeca, int[][] tabuleiro) {
        if(maximoAlinhado(corPeca, tabuleiro) >= 4) {
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
    
    

    public int numeroDeAlinhamentos(int corPeca, int[][] tabuleiro) {
        int contagem = 0;
        boolean encontrouPecaLinha;
        
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            encontrouPecaLinha = false;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    if(encontrouPecaLinha)  contagem++;
                    else encontrouPecaLinha = true;
                }
            }
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            encontrouPecaLinha = false;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(tabuleiro[x][y] == corPeca) {
                    if(encontrouPecaLinha)  contagem++;
                    else encontrouPecaLinha = true;
                }
            }
        }
        encontrouPecaLinha = false;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        encontrouPecaLinha = false;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x-1] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }

        encontrouPecaLinha = false;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(tabuleiro[x][x+1] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }

        encontrouPecaLinha = false;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(tabuleiro[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        encontrouPecaLinha = false;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(tabuleiro[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        encontrouPecaLinha = false;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(tabuleiro[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        return contagem;
    }

    public int numeroDeAlinhamentosComVazios(int corPeca, int[][] tabuleiro) {
        int pontos = 0;
        boolean encontrouPecaLinha;

        int nCorPeca;
        int nOutraCor;
        int nConsecutivos;
        
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            encontrouPecaLinha = false;
            nCorPeca = 0;
            nOutraCor = 0;
            nConsecutivos = 0;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    nCorPeca++;
                    if(encontrouPecaLinha) nConsecutivos++;
                    else encontrouPecaLinha = true;
                }
                else if(tabuleiro[x][y] == SEM_PECA) {
                    encontrouPecaLinha = false;
                }
                else {
                    nOutraCor++;
                    encontrouPecaLinha = false;
                }
            }
            if(nOutraCor == 0 || (nOutraCor == 1 && (tabuleiro[0][y] == invertePeca(corPeca) || tabuleiro[LARGURA_TABULEIRO-1][y] == invertePeca(corPeca))))
                if(nCorPeca > 0)
                    pontos+=(nCorPeca+nConsecutivos-nOutraCor);
        }

        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            encontrouPecaLinha = false;
            nCorPeca = 0;
            nOutraCor = 0;
            nConsecutivos = 0;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(tabuleiro[x][y] == corPeca) {
                    nCorPeca++;
                    if(encontrouPecaLinha) nConsecutivos++;
                    else encontrouPecaLinha = true;
                }
                else if(tabuleiro[x][y] == SEM_PECA) {
                    encontrouPecaLinha = false;
                }
                else {
                    nOutraCor++;
                    encontrouPecaLinha = false;
                }
            }
            if(nOutraCor == 0 || (nOutraCor == 1 && (tabuleiro[x][0] == invertePeca(corPeca) || tabuleiro[x][ALTURA_TABULEIRO-1] == invertePeca(corPeca))))
                if(nCorPeca > 0)
                    pontos+=(nCorPeca+nConsecutivos-nOutraCor);
        }

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x][x] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0 || (nOutraCor == 1 && (tabuleiro[0][0] == invertePeca(corPeca) || tabuleiro[LARGURA_TABULEIRO-1][ALTURA_TABULEIRO-1] == invertePeca(corPeca))))
                if(nCorPeca > 0)
                    pontos+=(nCorPeca+nConsecutivos-nOutraCor);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x-1] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x][x-1] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0)
            if(nCorPeca > 0)
                pontos+=(nCorPeca+nConsecutivos);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(tabuleiro[x][x+1] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x][x+1] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0)
            if(nCorPeca > 0)
                pontos+=(nCorPeca+nConsecutivos);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(tabuleiro[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x][(LARGURA_TABULEIRO-1)-x] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0 || (nOutraCor == 1 && (tabuleiro[4][0] == invertePeca(corPeca) || tabuleiro[0][4] == invertePeca(corPeca))))
                if(nCorPeca > 0)
                    pontos+=(nCorPeca+nConsecutivos-nOutraCor);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(tabuleiro[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x-1][LARGURA_TABULEIRO-1-x] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0)
            if(nCorPeca > 0)
                pontos+=(nCorPeca+nConsecutivos);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(tabuleiro[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x+1][LARGURA_TABULEIRO-1-x] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0)
            if(nCorPeca > 0)
                pontos+=(nCorPeca+nConsecutivos);

        return pontos;
    }

    private float geraCustoPeca(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        //ArvoreDeJogadas j = new ArvoreDeJogadas();
        //return j.geraPontosAleatorios();
        float pontosTripla = (float)minPontos;
        if(tentaAcharTripla(corPeca, tabuleiro)) {
            pontosTripla = (float)maxPontos;
        }
        float pontosDuplas = normalizaPontuacao(0.0f, 8.0f, (float)minPontos, (float)maxPontos, (float)contaDuplas(corPeca,tabuleiro));
        float pontosAlinhamentos = normalizaPontuacao(0.0f, 16.0f, (float)minPontos, (float)maxPontos, (float)numeroDeAlinhamentosComVazios(corPeca,tabuleiro));
        //float pontosMaximoAlinhado = normalizaPontuacao(0.0f, 4.0f, (float)minPontos, (float)maxPontos, (float)maximoAlinhado(corPeca,tabuleiro));
        return pontosAlinhamentos*0.5f + pontosDuplas*0.2f + pontosTripla*0.3f;
    }

    @Override
    public float geraCusto(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        //boolean tripla = tentaAcharTripla(corPeca,tabuleiro);
        //boolean triplaInimigo = tentaAcharTripla(invertePeca(corPeca),tabuleiro);
        //if(verificaVitoria(corPeca, tabuleiro)||(tripla && !triplaInimigo)) {
        //    return maxPontos;
        //}
        //else if(verificaVitoria(invertePeca(corPeca), tabuleiro)||(triplaInimigo && !tripla)) {
        //    return minPontos;
        //}
        if(verificaVitoria(corPeca, tabuleiro)) {
            return maxPontos;
        }
        else if(verificaVitoria(invertePeca(corPeca), tabuleiro)) {
            return minPontos;
        }
        else{
            return geraCustoPeca(corPeca, tabuleiro, minPontos, maxPontos)*0.3f + geraCustoPeca(invertePeca(corPeca), tabuleiro, minPontos, maxPontos)*-0.7f;
        }        
    }

    private ArrayList<Integer> minimizaDanos(int corPeca) {
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas(0);
        jogadas.setDificulty(3);
        ArrayList<ArrayList<Integer>> possiveisJogadas = listaPossiveisJogadas(invertePeca(corPeca), getTabuleiro());
        Collections.shuffle(possiveisJogadas);
        int pontuacaoMaxima = Integer.MIN_VALUE;
        int pontuacao;
        ArrayList<Integer> melhorJogada = new ArrayList<Integer>();
        for(int i=0; i < possiveisJogadas.size(); i++) {
            int[][] novoTabuleiro = criaCopiaTabuleiro(getTabuleiro());
            novoTabuleiro = fazJogada(possiveisJogadas.get(i), novoTabuleiro);
            pontuacao = (int)geraCusto(invertePeca(corPeca), novoTabuleiro, jogadas.getMinPontos(), jogadas.getMaxPontos());
            if(pontuacao > pontuacaoMaxima) {
                possiveisJogadas.get(i).set(2, corPeca);
                melhorJogada = possiveisJogadas.get(i);
                pontuacaoMaxima = pontuacao;
            }
        }
        return melhorJogada;
    }
    @Override
    public ArrayList<Integer> jogadaDaMaquina(int corPeca, int profundidade) {
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas(this, getTabuleiro(), corPeca, corPeca, profundidade, MAXIMO_JOGADAS);
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
        if(pontuacaoMaxima == jogadas.getMinPontos()) {
            melhorJogada =  minimizaDanos(corPeca);
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
        @Override
        public void mouseClicked(MouseEvent e) {
            int[] pos = {e.getX(), e.getY()};
            setPosClick(pos);
            if(isVezDoPlayer()) {
                int[] posJogada = {(int)(getPosClick()[0]*LARGURA_TABULEIRO)/LARGURA_TELA,
                                 (int)(getPosClick()[1]*ALTURA_TABULEIRO)/ALTURA_TELA};
                if(verificaJogada(posJogada[0], posJogada[1], getTabuleiro())) {
                    ArrayList<Integer> jogada = new ArrayList<Integer>(List.of(posJogada[0], posJogada[1], getPecaPlayer()));
                    setJogadaDoPlayer(jogada);
                    setVezDoPlayer(false);
                }
            }
        }
    }

    public boolean verificaSimetriaVertical(int tabuleiro [][]){
        for (int x = 0; x < 2; x++){
            for(int y = 0; y < 5; y++){
                if (tabuleiro[x][y] != tabuleiro[4-x][y]) return false;
            }
        }
        //System.out.println("Simetria Vertical");
        return true;
    }

    public boolean verificaSimetriaHorizontal(int tabuleiro [][]){
        for (int y = 0; y < 2; y++){
            for(int x = 0; x < 5; x++){
                if (tabuleiro[x][y] != tabuleiro[x][4-y]) return false;
            }
        }
        //System.out.println("Simetria Horizontal");
        return true;
    }

    public boolean verificaSimetriaDiagonal(int tabuleiro [][]){
        if (tabuleiro[0][1] != tabuleiro[1][0]) return false;
        if (tabuleiro[0][2] != tabuleiro[2][0]) return false;
        if (tabuleiro[0][3] != tabuleiro[3][0]) return false;
        if (tabuleiro[0][4] != tabuleiro[4][0]) return false;
        if (tabuleiro[1][2] != tabuleiro[2][1]) return false;
        if (tabuleiro[1][3] != tabuleiro[3][1]) return false;
        if (tabuleiro[1][4] != tabuleiro[4][1]) return false;
        if (tabuleiro[2][3] != tabuleiro[3][2]) return false;
        if (tabuleiro[2][4] != tabuleiro[4][2]) return false;
        if (tabuleiro[3][4] != tabuleiro[4][3]) return false;
        //System.out.println("Simetria Diagonal");
        return true;
    }

    public boolean verificaSimetriaOutraDiagonal(int tabuleiro [][]){
        if (tabuleiro[0][0] != tabuleiro[4][4]) return false;
        if (tabuleiro[0][1] != tabuleiro[3][4]) return false;
        if (tabuleiro[0][2] != tabuleiro[2][4]) return false;
        if (tabuleiro[0][3] != tabuleiro[1][4]) return false;
        if (tabuleiro[1][0] != tabuleiro[4][3]) return false;
        if (tabuleiro[1][1] != tabuleiro[3][3]) return false;
        if (tabuleiro[1][2] != tabuleiro[2][3]) return false;
        if (tabuleiro[2][0] != tabuleiro[4][2]) return false;
        if (tabuleiro[2][1] != tabuleiro[3][2]) return false;
        if (tabuleiro[3][0] != tabuleiro[4][1]) return false;
        //System.out.println("Simetria Outra Diagonal");
        return true;
    }

}
