package com.lopuch.projects.poker;

import java.util.List;

public interface IPlayer {

    String showName();

    void setCards(Card first, Card second);

    String getCards();

    void calculateValue(List<Card> table);

    int getVal();

    void setVal(int val);

    int getMoney();

    void setMoney(int money);

    String getName();

    void setName(String name);

    int getPosition();

    void setPosition(int position);

    List<Card> bestComb(List<Card> all);

    void combinations(List<Card> all, int k, int offset);

    int evaluate(List<Card> five);

    int straightFlush(List<Card> five);

    int fourOfAKind(List<Card> five);

    int fullHouse(List<Card> five);

    int flush(List<Card> five);

    int straight(List<Card> five);

    int triple(List<Card> five);

    int twoPair(List<Card> five);

    int pair(List<Card> five);

    int highCard(List<Card> five);

    List<Card> sortHand(List<Card> five);

    String hand();

}