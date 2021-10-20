import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;

public class JanelaJogo extends JFrame {
    private Jogo jogo;
    private JPanel telaJogo = new TelaJogo();

    public static final int LARGURA_TELA = 600;
    public static final int ALTURA_TELA = 600;
    private static final int TAMANHO_PECA = (int)LARGURA_TELA/(2*Jogo.LARGURA_TABULEIRO);
    private static final int DELAY_TIMER = 75;
    public static final int DELAY_JOGADA = 500;
    private boolean vezDoPlayer = false;
    private Movimento.Acao acaoDoPlayer;
    private Jogada jogadaDoPlayer;
    private boolean selecionado = false;
    private int[] posSelecionado = {0, 0};
    private boolean assistindoReplay = false;
    private boolean proximaJogadaReplay = false;
    private boolean jogadaAnteriorReplay = false;

    public Jogo getJogo() {
        return jogo;
    }
    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }
    public JPanel getTelaJogo() {
        return telaJogo;
    }
    public void setTelaJogo(JPanel telaJogo) {
        this.telaJogo = telaJogo;
    }
    public boolean isVezDoPlayer() {
        return vezDoPlayer;
    }
    public void setVezDoPlayer(boolean vezDoPlayer) {
        this.vezDoPlayer = vezDoPlayer;
    }
    public Movimento.Acao getAcaoDoPlayer() {
        return acaoDoPlayer;
    }
    public void setAcaoDoPlayer(Movimento.Acao acaoDoPlayer) {
        this.acaoDoPlayer = acaoDoPlayer;
    }
    public Jogada getJogadaDoPlayer() throws InterruptedException {
        setVezDoPlayer(true);
        while(isVezDoPlayer()) {
            Thread.sleep(1);
        }
        return jogadaDoPlayer;
    }
    public void setJogadaDoPlayer(Jogada jogadaDoPlayer) {
        this.jogadaDoPlayer = jogadaDoPlayer;
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
    public boolean isAssistindoReplay() {
        return assistindoReplay;
    }
    public void setAssistindoReplay(boolean assistindoReplay) {
        this.assistindoReplay = assistindoReplay;
    }
    public boolean isProximaJogadaReplay() {
        return proximaJogadaReplay;
    }
    public void setProximaJogadaReplay(boolean proximaJogadaReplay) {
        this.proximaJogadaReplay = proximaJogadaReplay;
    }
    public boolean isJogadaAnteriorReplay() {
        return jogadaAnteriorReplay;
    }
    public void setJogadaAnteriorReplay(boolean jogadaAnteriorReplay) {
        this.jogadaAnteriorReplay = jogadaAnteriorReplay;
    }

    JanelaJogo(Jogo jogo) {
        setJogo(jogo);

        this.add(getTelaJogo());
        this.setTitle(jogo.getNome());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
    private int calculaPosicaoFila(int posicaoObjeto, int tamanhoObjeto, int pixelsFila, int tamanhoFila) {
        return ((pixelsFila/(tamanhoFila+1)) *(posicaoObjeto+1)) - (tamanhoObjeto/2);
    }
    private void desenhaPecas(Graphics g) {
        int posicaoX;
        int posicaoY;
        for(int y=0; y < Jogo.ALTURA_TABULEIRO; y++) {
            for(int x=0; x < Jogo.LARGURA_TABULEIRO; x++) {
                switch(getJogo().getTabuleiro()[x][y]) {
                    case Jogo.SEM_PECA: 
                        posicaoX = calculaPosicaoFila(x, (int)(TAMANHO_PECA/1.5), LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, (int)(TAMANHO_PECA/1.5), ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
                        g.setColor(Color.lightGray);
                        g.fillOval(posicaoX, posicaoY, (int)(TAMANHO_PECA/1.5), (int)(TAMANHO_PECA/1.5));
                        break;
                    case Jogo.PECA_BRANCA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
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
                    case Jogo.PECA_PRETA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
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
        for(int x=0; x<Jogo.LARGURA_TABULEIRO; x++) {
            inicioX = calculaPosicaoFila(x, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
            inicioY = calculaPosicaoFila(0, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
            fimX = inicioX;
            fimY = calculaPosicaoFila(Jogo.ALTURA_TABULEIRO-1, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
            g.drawLine(inicioX, inicioY, fimX, fimY);
        }
        for(int y=0; y<Jogo.LARGURA_TABULEIRO; y++) {
            inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
            inicioY = calculaPosicaoFila(y, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
            fimX = calculaPosicaoFila(Jogo.LARGURA_TABULEIRO-1, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
            fimY = inicioY;
            g.drawLine(inicioX, inicioY, fimX, fimY);
        }
        inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        inicioY = calculaPosicaoFila(0, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        fimX = calculaPosicaoFila(Jogo.LARGURA_TABULEIRO-1, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(Jogo.ALTURA_TABULEIRO-1, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(Jogo.LARGURA_TABULEIRO-1, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        fimX = calculaPosicaoFila(0, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        inicioY = calculaPosicaoFila((int)Math.ceil(Jogo.ALTURA_TABULEIRO/2), 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        fimX = calculaPosicaoFila((int)Math.ceil(Jogo.LARGURA_TABULEIRO/2), 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(0, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
        fimY = calculaPosicaoFila(Jogo.ALTURA_TABULEIRO-1, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(Jogo.LARGURA_TABULEIRO-1, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(0, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
        fimY = calculaPosicaoFila(Jogo.ALTURA_TABULEIRO-1, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
    }
    public void desenhaTabuleiro(Graphics g) {
        desenhaLinhas(g);
        desenhaPecas(g);
    }

    public void fazJogadaComDelay(Jogada jogada) throws InterruptedException {
        for(Movimento movimento : jogada.getMovimentos()) {
            getJogo().fazMovimento(movimento, getJogo().getTabuleiro());
            Thread.sleep(DELAY_JOGADA);
        }
    } 
    public void desfazJogadaComDelay(Jogada jogada) throws InterruptedException {
        for(int i=jogada.getMovimentos().size()-1; i>=0; i--) {
            Movimento movimento = jogada.getMovimentos().get(i);
            getJogo().desfazMovimento(movimento, getJogo().getTabuleiro());
            Thread.sleep(DELAY_JOGADA);
        }
    }

    /*
    private void playerFazJogada() throws InterruptedException {
        setAcaoDoPlayer(getJogo().proximaAcao(getJogo().getPecaPlayer(), getJogo().getTabuleiro()));
        Jogada jogada = getJogadaDoPlayer();
        fazJogadaComDelay(jogada);  
        System.out.println(jogada.toString());  
        getJogo().getHistoricoJogadas().add(jogada);
    }
    */

    public Jogada playerFazMovimento(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual) throws InterruptedException {
        setAcaoDoPlayer(jogo.proximaAcao(corPecaJogador, tabuleiro));
        return getJogadaDoPlayer();
        
        /* 
        fazJogadaComDelay(jogada);  
        System.out.println(jogada.toString());  
        getJogo().getHistoricoJogadas().add(jogada);
        */
    
    }

    /** 
    private void botFazJogadaComDelay(int peca) throws InterruptedException {
        //Jogada jogada = getJogo().jogadaDaMaquina2(peca, getJogo().getProfundidade());
        Jogada jogada = getJogo().monteCarlo(peca);
        fazJogadaComDelay(jogada); 
        System.out.println(jogada.toString());
        getJogo().getHistoricoJogadas().add(jogada);
    }
    public void partidaBotXPlayer() throws InterruptedException {
        getJogo().setHistoricoJogadas(new ArrayList<Jogada>());
        getJogo().inicializaTabuleiro();
        while(!getJogo().verificaFimDeJogo(getJogo().getTabuleiro())) {
            System.out.println("Vez das peças brancas");
            if(getJogo().getPecaPlayer() == Jogo.PECA_BRANCA) {
                playerFazJogada();
                if(!getJogo().verificaFimDeJogo(getJogo().getTabuleiro())) {
                    System.out.println("Vez das peças pretas");
                    botFazJogadaComDelay(Jogo.PECA_PRETA);
                }
            }
            else {
                botFazJogadaComDelay(Jogo.PECA_BRANCA);
                if(!getJogo().verificaFimDeJogo(getJogo().getTabuleiro())) {
                    System.out.println("Vez das peças pretas");
                    playerFazJogada();
                }
            }
        }
        getJogo().salvaLogPartida();
    }
    */
    private Jogada jogadaDaLista(int xInicial, int yInicial, int xFinal, int yFinal, ArrayList<Jogada> possiveisJogadas) {
        for(Jogada jogada : possiveisJogadas) {
            if(jogada.getMovimentos().get(0).getPosicao1()[0] == xInicial && jogada.getMovimentos().get(0).getPosicao1()[1] == yInicial) {
                int i=0;
                boolean move = true;
                while(move && i < jogada.getMovimentos().size()) {
                    move = jogada.getMovimentos().get(i).getAcao() == Movimento.Acao.MOVE;
                    if(move) i++;
                }               
                i--;
                if(jogada.getMovimentos().get(i).getPosicao2()[0] == xFinal && jogada.getMovimentos().get(i).getPosicao2()[1] == yFinal) {
                    return jogada;
                }
            }
        }
        return null;
    }
    public void interpretaMovePecaPlayer(int[] posClick) {
        if(!isSelecionado()) {
            setPosSelecionado(posClick);
            if(getJogo().getTabuleiro()[posClick[0]][posClick[1]] == getJogo().getPecaPlayer()) {
                setSelecionado(true);
            }
        }
        else {
            ArrayList<Jogada> possiveisJogadas = getJogo().listaPossiveisJogadas(getJogo().getPecaPlayer(), getJogo().getTabuleiro());
            Jogada jogada = jogadaDaLista(getPosSelecionado()[0], getPosSelecionado()[1], posClick[0], posClick[1], possiveisJogadas);
            if(!Objects.isNull(jogada)) {
                setJogadaDoPlayer(jogada);
                setVezDoPlayer(false);
                setSelecionado(false);
            }
            else if(getJogo().getTabuleiro()[posClick[0]][posClick[1]] == getJogo().getPecaPlayer()) {
                setPosSelecionado(posClick);
            }
            else {
                setSelecionado(false);
            }
        }
    }
    
    public void interpretaIserePecaPlayer(int[] posClick) {
        Jogada jogada = new Jogada(getJogo().getPecaPlayer(), posClick);
        if(getJogo().verificaJogada(jogada, getJogo().getTabuleiro())) {
            setJogadaDoPlayer(jogada);
            setVezDoPlayer(false);
        }
    }
    public void interpretaJogadaPlayer(int[] posClick) {
        switch(getAcaoDoPlayer()) {
            case MOVE:
                interpretaMovePecaPlayer(posClick);
                break;
            case INSERE:
                interpretaIserePecaPlayer(posClick);
                break;
            default:
                break;
        }
    }

    public void replayHistoricoJogadas() throws InterruptedException {
        setAssistindoReplay(true);
        getJogo().inicializaTabuleiro();
        int posicaoReplay = -1;
        while(isAssistindoReplay()) {
            if(isProximaJogadaReplay()) {
                if(posicaoReplay < getJogo().getHistoricoJogadas().size()-1) {
                    posicaoReplay++;
                    fazJogadaComDelay(getJogo().getHistoricoJogadas().get(posicaoReplay));
                }
                setProximaJogadaReplay(false);
            }
            if(isJogadaAnteriorReplay()) {
                if(posicaoReplay >= 0) {
                    desfazJogadaComDelay(getJogo().getHistoricoJogadas().get(posicaoReplay));
                    posicaoReplay--;
                }
                setJogadaAnteriorReplay(false);
            }
            Thread.sleep(1);
        }
    }
    public void carregaLog(String caminho) {
        try {
            File arquivo = new File(caminho);
            FileReader leitorDeArquivo = new FileReader(arquivo);
            BufferedReader leitorDeBuffer = new BufferedReader(leitorDeArquivo);   
            String textoJogada;  
            getJogo().setHistoricoJogadas(new ArrayList<Jogada>());
            while((textoJogada = leitorDeBuffer.readLine()) != null) {
                Jogada jogada = new Jogada(textoJogada);
                getJogo().getHistoricoJogadas().add(jogada);
            }
            leitorDeBuffer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public class TelaJogo extends JPanel implements ActionListener {
        private Timer timer;

        public Timer getTimer() {
            return timer;
        }
        public void setTimer(Timer timer) {
            this.timer = timer;
        }

        TelaJogo() {
            setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
            setFocusable(true);
            setBackground(Color.white);
            addMouseListener(new EventosMouse());
            addKeyListener(new EventosTeclado());
            iniciaTimer();
        }
            //  funções da engine
        public void iniciaTimer() {
            setTimer(new Timer(DELAY_TIMER, this));
            getTimer().start();
        }
        public void paraTimer() {
            getTimer().stop();
            repaint();
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            desenhaTabuleiro(g);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }  
    }

    // classe de que captura eventos do mouse
    public class EventosMouse extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int[] posClick = {(int)(e.getX()*Jogo.LARGURA_TABULEIRO)/LARGURA_TELA, (int)(e.getY()*Jogo.ALTURA_TABULEIRO)/ALTURA_TELA};
            if(isVezDoPlayer()) {
                interpretaJogadaPlayer(posClick);
            }
        }
    }

    public class EventosTeclado extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(isAssistindoReplay())
                        setJogadaAnteriorReplay(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    if(isAssistindoReplay())
                        setProximaJogadaReplay(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    setAssistindoReplay(false);
                    break;
                default:
                    break;
            }
        }
    }

}
