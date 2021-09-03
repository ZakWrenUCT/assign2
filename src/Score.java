import java.util.concurrent.atomic.AtomicInteger;

public class Score {

  private AtomicInteger missedWords;
  private AtomicInteger caughtWords;
  private AtomicInteger gameScore;

  Score() {
    missedWords = new AtomicInteger(0);
    caughtWords = new AtomicInteger(0);
    gameScore = new AtomicInteger(0);
  }

  // all getters and setters must be synchronized

  public synchronized int getMissed() {
    return missedWords.get();
  }

  public synchronized int getCaught() {
    return caughtWords.get();
  }

  public synchronized int getTotal() {
    return (missedWords.get() + caughtWords.get());
  }

  public synchronized int getScore() {
    return gameScore.get();
  }

  public synchronized void missedWord() {
    missedWords.getAndIncrement();
    WordApp.missed.setText("Missed:" + WordApp.score.getMissed() + "    ");
  }

  public synchronized void caughtWord(int length) {
    caughtWords.getAndIncrement();
    gameScore.getAndAdd(length);
    updateScore();
  }

  public synchronized void resetScore() {
    caughtWords = new AtomicInteger(0);
    missedWords = new AtomicInteger(0);
    gameScore = new AtomicInteger(0);
    updateScore();
  }
  public synchronized void resetScoreNoUp() {
    caughtWords = new AtomicInteger(0);
    missedWords = new AtomicInteger(0);
    gameScore = new AtomicInteger(0);
  }

  public static synchronized void updateScore() {
    WordApp.caught.setText("Caught: " + WordApp.score.getCaught() + "    ");
    WordApp.scr.setText("Score:" + WordApp.score.getScore() + "    ");
    WordApp.missed.setText("Missed:" + WordApp.score.getMissed() + "    ");
  }
}
