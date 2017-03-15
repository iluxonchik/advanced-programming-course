package pt.ist.ap.labs;

import java.util.Scanner;
import java.lang.Class;

public class Main {
  public static void main(String[] args) {
    Scanner reader = new Scanner(System.in);
    System.out.println("Enter the FQN of the class to run: ");
    String fqn = reader.next();

    try {
      Class cls = Class.forName(fqn);
      Object instance = cls.newInstance();
      Message msg = (Message) instance;
      msg.say();
    } catch (ClassNotFoundException e) {
        System.err.println("Class \"" + fqn + "\" could not be found.");
        System.exit(1);
    } catch (InstantiationException e) {
        System.err.println("Error while instantiating object of class \"" + fqn + "\".");
        System.exit(1);
    } catch (IllegalAccessException e) {
          System.err.println(e.getMessage());
          System.exit(1);
    }
  }
}
