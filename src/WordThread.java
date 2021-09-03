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
        //set absolute fall rate
        word.drop(word.getSpeed() / (((5 - WordApp.speedMod) * 20) + 200));
      } else {
        WordApp.score.caughtWord(input.length());
        word.resetWord();
      }
      try {
        Thread.sleep(15);//length of each cycle
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } else {
      word.resetWord();
      WordApp.score.missedWord();
    }
  }
}
