package eis.eis2javahelloworld;

import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;


/**
 * An entity in the hello world environment which is capable of printing text to
 * the system.out.
 *  
 * @author Lennard de Rijk
 */
public class Entity {

  private int printedTextAmount = 0;
  private String lastPrinted = "";

  /**
   * @return the amount of times a text has been printed.
   */
  @AsPercept(name = "printedText")
  public int getPrintedTextAmount() {
    return printedTextAmount;
  }

  /**
   * Prints a new line of text.
   * 
   * @param text the text to print.
   */
  @AsAction(name = "printText")
  public void printText(String text) {
    System.out.println(text);
    lastPrinted = text;
    printedTextAmount++;
  }

  /**
   * @return The line of text that was last printed.
   */
  @AsPercept(name = "lastPrintedText")
  public String getLastPrinted() {
    return lastPrinted;
  }
}
