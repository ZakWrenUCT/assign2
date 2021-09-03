public class WordThread extends Thread {

  private WordRecord word;
  private String input;

  public WordThread(WordRecord word, String input) {
    this.word = word;
    this.input = input;
  }

  public void run() {
    if (!word.dropped()) {
      if (!word.matchWord(input)) {
        word.drop(word.getSpeed() / (((5 - WordApp.speedMod) * 20) + 200));
      } else {
        WordApp.score.caughtWord(input.length());
        System.out.println(input);
        word.resetWord();
      }
      try {
        Thread.sleep(15);
      } catch (InterruptedException error) {
        System.out.println(error);
      }
    } else {
      WordApp.score.missedWord();
      word.resetWord();
    }
  }
}
