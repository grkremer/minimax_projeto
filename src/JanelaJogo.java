import javax.swing.JFrame;

public class JanelaJogo extends JFrame {
    JanelaJogo(Jogo jogo) {
        this.add(jogo);
        this.setTitle(jogo.getNome());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
