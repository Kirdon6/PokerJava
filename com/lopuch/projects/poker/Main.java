package com.lopuch.projects.poker;
import java.io.IOException;
import java.io.PrintStream;



public class Main {
    public static void main(String[] args) throws IOException {
        Dialogue dialogue = new Dialogue(new PrintStream(System.out));
        dialogue.welcome();
        while (true) { // menu loop
            dialogue.option();
            String answer = dialogue.getAnswer();
            if (answer.equals("play")) {
                Game game = new Game(dialogue);
                game.singlePlayer();
            } else if (answer.equals("sim")) {
                PrintStream outFile = new PrintStream("PokerSim.txt");
                Dialogue dialogueSim = new Dialogue(outFile);
                Game game = new Game(dialogueSim);
                game.simulation();
                outFile.close();
            }
            else if (answer.equals("network"))
            {
                Server server = new Server();
                Game game = new Game(server);
                //game.networkGame();
            }
            else if (answer.equals("quit")) {
                dialogue.out("Thanks for playing!\n");
                break;
            } else {
                dialogue.out("WRONG INPUT\n");
            }
        }
        
    }
}
