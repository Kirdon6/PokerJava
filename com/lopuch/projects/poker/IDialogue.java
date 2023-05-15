package com.lopuch.projects.poker;

import java.util.List;

public interface IDialogue {

    void welcome();

    void option();

    String getName();

    String lower(String data);

    String getAnswer();

    void showCards(Player player);

    void showTable(List<Card> table);

    void whatToDo(int[] bank, int money);

    void howMuch(int[] bank, int money);

    void out(String text);

    void winner(boolean win);

    boolean is_digits(String str);

}