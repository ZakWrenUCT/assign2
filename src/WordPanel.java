import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {

  public static volatile boolean done;
  private final WordRecord[] words;
  private final int noWords;
  private final int maxY;
  private Thread anime;

  public void paintComponent(Graphics g) {
    int width = getWidth();
    int height = getHeight();
    g.clearRect(0, 0, width, height);
    g.setColor(Color.red);
    g.fillRect(0, maxY - 10, width, height);

    g.setColor(Color.black);
    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
    // Draw the words.
    // Animation must be added
    if (!done) {
      for (int i = 0; i < noWords; i++) {
        g.drawString(words[i].getWord(), words[i].getX(), words[i].getY());
      }
    }
  }

  WordPanel(WordRecord[] wds, int maxY) {
    this.words = new WordRecord[wds.length];
    for (int i = 0; i < wds.length; i++) {
      this.words[i] = new WordRecord(wds[i]);
    }

    noWords = words.length;
    done = false;
    this.maxY = maxY;
  }

  public void start() {
    done = false;
    for (WordRecord w : words) {
      w.resetWord();
    }
    WordApp.score.resetScore();
    if (anime == null) {
      anime = new Thread(this);
      anime.start();
    }
  }

  public boolean endGame() {
    return WordApp.score.getTotal() >= WordApp.totalWords;
  }

  public void run() {
    // Code to animate this.
    System.out.println('a');
    while (!WordApp.done || !endGame()) {
      while (!WordApp.paused) {
        WordThread[] WordThreads = new WordThread[noWords];
        for (int i = 0; i < noWords; i++) {
          WordThreads[i] = new WordThread(words[i], WordApp.textFromUser);
          WordThreads[i].start();
          try {
            WordThreads[i].join();
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
          repaint();
        }
        if (WordApp.done || endGame()) {
          break;
        }
      }
      if ((WordApp.done || endGame()) && !WordApp.awaitingRestart) {
        WordApp.textEntry.setEnabled(false);
        WordApp.textEntry.setText("GAME OVER");
        System.out.println("Game Over!");
        System.out.println("Caught: " + WordApp.score.getCaught());
        System.out.println("Missed:" + WordApp.score.getMissed());
        System.out.println("Score:" + WordApp.score.getScore());
        WordApp.awaitingRestart = true;
        WordApp.paused = true;
        WordApp.score.resetScore();
      }
    }
  }
}
