import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

public class WordApp {

  // shared variables
  static int noWords = 4;
  static int totalWords;
  static int speedMod = 5;
  static int frameX = 1000;
  static int frameY = 600;
  static int yLimit = 480;

  static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

  static WordRecord[] words;
  static volatile boolean done; //must be volatile
  static volatile boolean awaitingRestart = false;
  static volatile boolean paused = false;
  static Score score = new Score();

  static WordPanel w;
  static JFrame frame;
  static JPanel graphic;
  static JPanel txt;
  static JLabel speed, caught, missed, scr;
  static final JTextField textEntry = new JTextField("", 20);
  static JPanel b;
  static JButton speedUp, speedDown, startB, endB, quitB;
  static String textFromUser = "";

  public static void setupGUI(int frameX, int frameY, int yLimit) {
    // Frame init and dimensions
    frame = new JFrame("WordGame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(frameX, frameY);

    graphic = new JPanel();
    graphic.setLayout(new BoxLayout(graphic, BoxLayout.PAGE_AXIS));
    graphic.setSize(frameX, frameY);

    w = new WordPanel(words, yLimit);
    w.setSize(frameX, yLimit + 100);
    graphic.add(w);

    txt = new JPanel();
    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS));
    speed = new JLabel("Speed: " + speedMod + "    ");
    caught = new JLabel("Caught: " + score.getCaught() + "    ");
    missed = new JLabel("Missed:" + score.getMissed() + "    ");
    scr = new JLabel("Score:" + score.getScore() + "    ");
    speedDown = new JButton("-");
    txt.add(speedDown);
    speedUp = new JButton("+");
    txt.add(speedUp);

    txt.add(speed);
    txt.add(caught);
    txt.add(missed);
    txt.add(scr);

    textEntry.addActionListener(
      evt -> {
        textFromUser = textEntry.getText();
        textEntry.setText("");
        textEntry.requestFocus();
      }
    );

    txt.add(textEntry);
    textEntry.setEnabled(false);
    txt.setMaximumSize(txt.getPreferredSize());
    graphic.add(txt);

    b = new JPanel();
    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));

    speedUp.addActionListener(
      e -> {
        if (speedMod < 9) {
          speedMod++;
        }
        speed.setText("Speed: " + speedMod + "    ");
      }
    );
    speedDown.addActionListener(
      e -> {
        if (1 < speedMod) {
          speedMod--;
        }
        speed.setText("Speed: " + speedMod + "    ");
      }
    );

    startB = new JButton("Start");
    synchronized (score) {
      startB.addActionListener(
        e -> {
          System.out.println("START");
          textEntry.setText("");
          textFromUser="";
          textEntry.setEnabled(true);
          startB.setText("Restart");
          done = false;
          awaitingRestart = false;
          paused = false;
          endB.setText("Pause");
          w.start();
          score.resetScore();
          textEntry.requestFocus(); //return focus to the text entry field
        }
      );
    }
    endB = new JButton("Pause");
    endB.addActionListener(
      e -> {
        if (awaitingRestart) {
          return;
        }
        if (paused) {
          paused = false;
          endB.setText("Pause");
          textEntry.setEnabled(true);
          textEntry.requestFocus();
        } else {
          paused = true;
          endB.setText("Unpause");
          textEntry.setEnabled(false);
        }
      }
    );

    quitB = new JButton("Quit");
    quitB.addActionListener(e -> System.exit(0));
    b.add(startB);
    b.add(endB);
    b.add(quitB);
    graphic.add(b);
    frame.setLocationRelativeTo(null);
    frame.add(graphic);
    frame.setContentPane(graphic);
    frame.setVisible(true);
  }

  public static String[] getDictFromFile(String filename) {
    String[] dictStr = null;
    try {
      Scanner dictReader = new Scanner(new FileInputStream(filename));
      int dictLength = dictReader.nextInt();
      dictStr = new String[dictLength];
      for (int i = 0; i < dictLength; i++) {
        dictStr[i] = dictReader.next();
      }
      dictReader.close();
    } catch (IOException e) {
      System.err.println(
        "Problem reading file " + filename + " default dictionary will be used"
      );
    }
    return dictStr;
  }

  public static void main(String[] args) {
    //deal with command line arguments
    totalWords = Integer.parseInt(args[0]); //total words to fall
    noWords = Integer.parseInt(args[1]); // total words falling at any point
    assert (totalWords >= noWords); // this could be done more neatly

    String[] tmpDict = getDictFromFile(args[2]); //file of words
    if (tmpDict != null) dict = new WordDictionary(tmpDict);
    WordRecord.dict = dict; //set the class dictionary for the words.
    words = new WordRecord[noWords]; //shared array of current words
    int x_inc = frameX / noWords;

    //initialize shared array of current words
    for (int i = 0; i < noWords; i++) {
      words[i] = new WordRecord(dict.getNewWord(), i * x_inc, yLimit);
    }
    setupGUI(frameX, frameY, yLimit);
  }
}
