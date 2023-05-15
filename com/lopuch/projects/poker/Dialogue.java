package com.lopuch.projects.poker;

import java.util.Scanner;
import java.io.PrintStream;
import java.util.List;


import java.util.Locale;

public class Dialogue implements IDialogue {

    private final Scanner scanner;
    private final PrintStream output;

    public Dialogue(PrintStream outfile) {
        this.scanner = new Scanner(System.in);
        this.output = outfile;
    }

    @Override
    public void welcome() {
        /*
        creating opening
        */
        out("***********************************\n");
        out("Welcome to the Texas Hold'em Poker!\n");
        out("***********************************\n");
        out("\n");
    }

    @Override
    public void option() {
        /*
        creating option menu
        */
        out("For playing game type 'play'\n");
        out("For simulation type 'sim'\n");
        out("for quit type 'quit'\n");
        out("\n");
    }

    @Override
    public String getName() {
        out("What is your name?\n");
        return getAnswer();
    }

    @Override
    public String lower(String data) {
        /*
        change all letters to lower case
        */
        return data.toLowerCase(Locale.ROOT);
    }

    @Override
    public String getAnswer() {
        /*
        communicating with user
        */
        String answer = scanner.next();
        return lower(answer);
    }

    @Override
    public void showCards(Player player) {
        out(player.showName() + "'s cards are:");
        out(player.getCards()+" \n");
    }

    @Override
    public void showTable(List<Card> table) {
        out("Cards on table:");
        for (ICard card : table) {
            out(card.showCard() + " ");
        }
        out("\n");
    }

    @Override
    public void whatToDo(int[] bank, int money) {
        /*
        giving options what player can do
        */
        out("Your options are:");
        if (money == 0) {
            out("Check\n");
        } else if (bank[0] >= bank[1]) {
            out("Raise   Fold   Check\n");
        } else {
            out("Call   Raise    Fold\n");
        }
    }

    @Override
    public void howMuch(int[] bank, int money) {
        /*
        giving options how much to bet
        */
        out("How much do you want to bet?\n");
        out("From " + (bank[1] - bank[0]) + " to " + money + "\n");
    }

    @Override
    public void out(String text) {
        /*
        output
        */
            output.print(text);
    }

    @Override
    public void winner(boolean win) {
        /*
        announcing winner
        */
        if (win) {
            out("You won! Congratulation!\n");
        } else {
            out("You lost! Try again!\n");
        }
    }

    @Override
    public boolean is_digits(String str) {
        /*
        checking if argument consists only digits
        */
        return str.chars().allMatch(Character::isDigit);
    }

}

