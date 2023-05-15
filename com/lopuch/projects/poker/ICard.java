package com.lopuch.projects.poker;

public interface ICard {
    enum Color{
        clubs,
        hearts,
        diamonds,
        spades
    }
    String showCard();

    Color getColor();

    void setColor(Color color);

    int getValue();

    void setValue(int value);

}