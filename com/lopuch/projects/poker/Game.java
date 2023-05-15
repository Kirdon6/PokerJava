package com.lopuch.projects.poker;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private boolean singleplayer = true; // true if playing singleplayer otherwise simulation
	public boolean isSingleplayer() {
        return singleplayer;
    }

    public void setSingleplayer(boolean singleplayer) {
        this.singleplayer = singleplayer;
    }

    //flags for fold (someone didn't want to call) and flop (creating opening 3 cards)
	private boolean fold = false ;
	private boolean flop = true ;
	private Deck deck;
	private Player player1;
    private Player player2;
	private AI opponent1;
	private AI opponent2;
	private Dialogue dialogue;
	private List<Card> table;
	private int[] bank = {0,0}; // how much money is in the bank from each player
	private int handle; // flag for who's turn it is
	private int round = 0; //round counter
    private Server server;

    public Game(Dialogue dialogue) {
        this.dialogue = dialogue;
        handle = 1;
        player1 = new Player();
        player2 = new Player();
        opponent1 = new AI(1000,10);
        opponent2 =  new AI(1000, 10);
        table = new ArrayList<>();
    }

    public Game (Server server)
    {
        this.server = server;
        handle = 1;
        deck.shuffleDeck();
    }

    public void createPlayer(boolean single)
    {
        if (single == true)
        {
            player1.setName(dialogue.getName());
        }
        else
        {
            player1.setName(dialogue.getName());
            player2.setName(dialogue.getName());
        }

    }

    public void dealCards()
    {
        Card first = deck.dealCard();
        Card second = deck.dealCard();

        if (singleplayer)
        {
            player1.setCards(first, second);
            first = deck.dealCard();
            second = deck.dealCard();
            opponent1.setCards(first, second);
        }
        else
        {
            opponent1.setCards(first, second);
            first = deck.dealCard();
            second = deck.dealCard();
            opponent2.setCards(first, second);
        }
    }

    public void simulation()
    {
        setMode(false);

        opponent1.setName("Jim");
        opponent2.setName("Joe");
        opponent1.setPosition(0);
        opponent2.setPosition(1);
        opponent1.setHard(true);
        opponent2.setHard(false);

        while(true)
        {
            startingSetting();

            opponent1.setVal(0);
            opponent2.setVal(0);
            simulationLoop();
            if (!fold)
            {
                determineWinner();
            }

            if(opponent1.getMoney() == 0)
            {
                dialogue.out("*** Game over! Player 2 won! ***\n");
                break;
            }
            else if (opponent2.getMoney() == 0)
            {
                dialogue.out("*** Game over! Player 1 won! ***\n");
                break;
            }

            table.clear();
            round++;

            if (round == 10)
            {
                if (opponent1.getMoney() > opponent2.getMoney())
                {
                    dialogue.out("*** Game over! Player 1 won! **\n");
                    break;//koniec hry 
                }
                else if (opponent1.getMoney() < opponent2.getMoney())
                {
                    dialogue.out("*** Game over! Player 2 won! ***\n");
                    break;//koniec hry 
                }
                else
                {
                    dialogue.out("Tie\n");
                    break;//koniec hry 
			    }
            }
            dialogue.out("Player 1: " + opponent1.getMoney() + "\n");
            dialogue.out("Player 2: " + opponent2.getMoney() + "\n");
            dialogue.out("*** NEW ROUND ***\n");
        }
        return;
    }

    public void startingSetting()
    {
        //creating new deck
        Deck new_deck = new Deck();
        new_deck.shuffleDeck();
        deck = new_deck;
        // crating flags for fold and flop
        flop = true;
        fold = false;
    }

    public int getBet()
    {
        //returns how much money player wants to bet
        int bet;
        while (true)
        {
            dialogue.howMuch(bank, player1.getMoney());
            String answer = dialogue.getAnswer();
            if (dialogue.is_digits(answer))
            {
                bet = Integer.parseInt(answer);

                if (bet <= player1.getMoney() && bet > bank[1]- bank[0])
                {
                    return bet;
                }
            }
            dialogue.out("WRONG INPUT!\n");
        }
    }

    public void announceWinner(Boolean win)
    {
            dialogue.winner(win);
    }

    public void setMode(Boolean player)
    {
        if (player)
        {
            singleplayer = true;
        }
        else
        {
            singleplayer = false;
        }
    }
    
    public void singlePlayer()
    {
        setMode(true);
        difficultyDialogue();
        createPlayer(true);

        opponent1.setName("Jim");
        opponent1.setMoney(1000);
        player1.setPosition(0);
        opponent1.setPosition(1);

        while(true)
        {
            startingSetting();

            singleplayerLoop();

            if (!fold)
            {
                determineWinner();

            }
            if (player1.getMoney() == 0)
            {
                announceWinner(false);
                break;
            }
            else if (opponent1.getMoney() == 0)
            {
                announceWinner(true);
            }
            table.clear();
            dialogue.out("Your money: " + player1.getMoney() + "\n");
            dialogue.out("Opponent's money:" + opponent1.getMoney() + "\n");
            dialogue.out("*** NEW ROUND ***\n");
        }
    }


    /*public void networkGame()
    {
        setMode(true);
        createPlayer(false);
        player1.setPosition(0);
        player2.setPosition(1);
        player1.connectToServer();
        player2.connectToServer();

        while(true)
        {

        }
    } */

    public void setDifficulty(String dif)
    {
        if (dif.equals("easy"))
        {
            opponent1.setHard(false);
        }
        else
        {
            opponent1.setHard(true);
        }
    }

    public void determineWinner()
    {
        /*
        decides who is winner by best combination of five cards
        */
        if (singleplayer)
        {
            player1.calculateValue(table);
            opponent1.calculateValue(table);
            dialogue.out("Your best five:\n");
            String text = "";
            for (int i = 0; i < player1.best.size(); i++)
            {
                text = text + player1.best.get(i).showCard() + " ";
            }
            dialogue.out(text);
            dialogue.out("\n");
            dialogue.out("Your opponent best five:\n");
            text = "";
            for (int i = 0; i < opponent1.best.size(); i++)
            {
                text = text + opponent1.best.get(i).showCard() + " ";
            }
            dialogue.out(text);
            dialogue.out("\n");		

            dialogue.out("You have " + player1.hand() + "\n");
            dialogue.out("Your opponent has " + opponent1.hand() + "\n");



            if (player1.getVal() > opponent1.getVal())
            {
                player1.setMoney(player1.getMoney() + bank[0] + bank[1]);
                dialogue.out("You Won !\n");
            }
            else if (player1.getVal() < opponent1.getVal())
            {
                opponent1.setMoney(opponent1.getMoney() + bank[0] + bank[1]);
                dialogue.out("You Lost!\n");
            }
            else
            {
                dialogue.out("Tie!\n");
                int win = bank[0] + bank[1];
                opponent1.setMoney(opponent1.getMoney() + win / 2);
                player1.setMoney(player1.getMoney() + (win / 2));

            }
            bank[0] = 0;
            bank[1] = 0;
        }
        else
        {
            opponent1.calculateValue(table);
            
            dialogue.out("Player 1 best five:");
            String text = "";
            for (int i = 0; i < opponent1.best.size(); i++)
            {
                text = text + opponent1.best.get(i).showCard() + " ";
                
            }
            dialogue.out(text);
            dialogue.out("\n");
            opponent2.calculateValue(table);
            text = "";
            dialogue.out("Player 2 best five:");
            for (int i = 0; i < opponent2.best.size(); i++)
            {
                text = text + opponent2.best.get(i).showCard() + " ";
            }
            dialogue.out(text);
            dialogue.out("\n");


            dialogue.out("Player 1 has " + opponent1.hand() + "\n");
            dialogue.out("Player 2 has " + opponent2.hand() + "\n");



            if (opponent1.getVal() > opponent2.getVal())
            {
                opponent1.setMoney(opponent1.getMoney() + bank[0] + bank[1]);
                dialogue.out("Player 1 won!\n");
            }
            else if (opponent1.getVal() < opponent2.getVal())
            {
                opponent2.setMoney(opponent2.getMoney() + bank[0] + bank[1]);
                dialogue.out("Player 2 won!\n");
            }
            else
            {
                dialogue.out("Tie!\n");
                int win = bank[0] + bank[1];
                opponent2.setMoney(opponent2.getMoney() + win / 2);
                opponent1.setMoney(opponent1.getMoney() + (win / 2));

            }
            bank[0] = 0;
            bank[1] =  0;
        }
    }

    public void announceWinner(boolean win)
    {
            dialogue.winner(win);
    }

    public void difficultyDialogue()
    {
        //dialogue for setting difficulty in singleplayer
        while (true)
        {
            dialogue.out("What difficulty do you want?\n");
            dialogue.out("Easy or Hard\n");

            String difficulty = dialogue.getAnswer();
            if ((difficulty.equals("easy")) || (difficulty.equals("hard")))
            {
                setDifficulty(difficulty);
                break;
            }
            dialogue.out("WRONG INPUT!\n");
        }
    }
    public void singleplayerLoop()
    {
        //one round of game
        while (true) 
        {
            
            if (flop)
            {
                createFlop();
            }
            if (handle == 1) //players turn
            {
                options();	 //*** just for debugging***		
                String response = dialogue.getAnswer();
                if (response.equals("call") ) //call ->  opponent's turn
                {
                    if (player1.getMoney() < bank[1] - bank[0])
                    {
                        bank[0] = bank[0] + player1.getMoney();
                        player1.setMoney(0);
                        opponent1.setMoney(opponent1.getMoney() + (bank[1] - bank[0]));
                        bank[1] = bank[0];
                    }
                    else
                    {
                        player1.setMoney(player1.getMoney() - (bank[1] - bank[0]));
                        bank[0] = bank[1];
                    }
                    handle = 0;

                }
                else if (response.equals("raise")) //raise -> opponent's turn
                {
                    int bet = getBet();
                    player1.setMoney(player1.getMoney() - bet);
                    bank[0] = bank[0] + bet;
                    handle = 0;
                }
                else if (response.equals("check")) //check -> opponent's turn
                {
                    handle = 0;
                    continue;
                }
                else if (response.equals("fold")) //fold -> opponent's win
                {
                    opponent1.setMoney(opponent1.getMoney() + bank[1] + bank[0]); //giving money to winner
                    bank[0] = 0;
                    bank[1] = 0;
                    fold = true;
                    break;
                }
                else
                {
                    dialogue.out("WRONG INPUT\n");
                }

            }
            else if (handle == 0) //opponent's turn
            {
                //dialogue.showCards(opponent1);
                //dialogue.showTable(table);
                String response = opponent1.decision(player1.getMoney(), bank); //AI decision making
                dialogue.out("**********\n");
                if (response == "call") //call ->  adding card
                {
                    dialogue.out("Opponent decided to call\n");
                    dialogue.out("**********\n");

                    if (opponent1.getMoney() < bank[0] - bank[1])
                    {
                        bank[1] =  bank[1] + opponent1.getMoney();
                        opponent1.setMoney(0);
                        player1.setMoney(player1.getMoney() + (bank[0] - bank[1]));
                        bank[0] = bank[1];
                    }
                    else
                    {
                        opponent1.setMoney(opponent1.getMoney() - (bank[0] - bank[1]));
                        bank[1] =  bank[0];
                    }
                    handle = -1;

                }
                else if (response == "raise") //raise -> player's turn
                {

                    Boolean over = false;
                    if (bank[0] > bank[1])
                    {
                        over = true;
                    }
                    int bet = opponent1.raise(player1.getMoney() + bank[0], over, bank[0]); //kolko raisne
                    dialogue.out("Opponent decided to raise " + bet + " dollars\n");
                    dialogue.out("**********\n");
                    opponent1.setMoney(opponent1.getMoney() - bet);
                    bank[1] = bank[1] + bet;
                    handle = 1;
                }
                else if (response == "check") //check -> adding card
                {
                    dialogue.out("Opponent decided to check\n");
                    dialogue.out("**********\n");
                    handle = -1;
                    continue;
                }
                else if (response == "fold") //fold -> player's win
                {
                    dialogue.out("Opponent decided to fold\n");
                    dialogue.out("**********\n");
                    player1.setMoney(player1.getMoney() + bank[1] + bank[0]);
                    bank[0] = 0;
                    bank[1] = 0;
                    fold = true;
                    break;
                }
            }
            else if (handle == -1) //adding new card
            {
                if (table.size() < 5)
                {
                    table.add(deck.dealCard());
                    handle = 1;
                }
                else
                {
                    handle = 1;
                    break;
                }

            }

        }
    }

    public void simulationLoop()
    {
        while(true) //one round -> 5 cards on table
        {

            if (flop)
            {
                createFlop();
            }
            if (handle == 1) //turn: Player 1
            {
                dialogue.showCards(opponent1);
                dialogue.showTable(table);
                String response = opponent1.decision(opponent2.getMoney(), bank); //AI decision
                if (response == "call") //call ->  turn: Player 2
                {
                    dialogue.out("Player 1 decided to call\n");

                    if (opponent1.getMoney() < bank[1] - bank[0])
                    {
                        bank[0] = bank[0] + opponent1.getMoney();
                        opponent1.setMoney(0);
                        opponent2.setMoney(opponent2.getMoney() + (bank[1] - bank[0]));
                        bank[1] = bank[0];
                    }
                    else
                    {
                        opponent1.setMoney(opponent1.getMoney() - (bank[1] - bank[0]));
                        bank[0] = bank[1];
                    }
                    handle = 0;

                }
                else if (response == "raise") //raise -> turn: Player 2
                {
                    Boolean over = false;
                    if (bank[1] > bank[0])
                    {
                        over = true;
                    }
                    int bet = opponent1.raise(opponent2.getMoney() + bank[1], over, bank[1]); //kolko raisne
                    dialogue.out("Player 1 decided to raise " + Integer.toString(bet) + " dollars\n");
                    opponent1.setMoney(opponent1.getMoney() - bet);
                    bank[0] = bank[0] + bet;
                    handle = 0;
                }
                else if (response == "check") //check -> turn: Player 2
                {
                    dialogue.out("Player 1 decided to check\n");
                    handle = 0;
                    continue;
                }
                else if (response == "fold") //fold -> Player 2 won round
                {
                    dialogue.out("Player 1 decided to fold\n");
                    opponent2.setMoney(opponent2.getMoney() + bank[1] + bank[0]);
                    bank[0] = 0;
                    bank[1] = 0;
                    fold = true;
                    break;
                }

            }
            else if (handle == 0) //turn: Player 2
            {
                dialogue.showCards(opponent2);
                dialogue.showTable(table);
                String response = opponent2.decision(opponent1.getMoney(), bank); //AI decision
                if (response == "call") //call ->  adding card
                {
                    dialogue.out("Player 2 decided to call\n");

                    if (opponent2.getMoney() < bank[0] - bank[1])
                    {
                        bank[1] = bank[1] + opponent2.getMoney();
                        opponent2.setMoney(0);
                        opponent1.setMoney(opponent1.getMoney() + (bank[0] - bank[1]));
                        bank[0] = bank[1];
                    }
                    else
                    {
                        opponent2.setMoney(opponent2.getMoney() - (bank[0] - bank[1]));
                        bank[1] = bank[0];
                    }
                    handle = -1;

                }
                else if (response == "raise") //raise -> turn: Player 1
                {

                    Boolean over = false;
                    if (bank[0] > bank[1])
                    {
                        over = true;
                    }
                    int bet = opponent2.raise(opponent1.getMoney() + bank[0], over, bank[0]); //kolko raisne
                    dialogue.out("Player 2 decided to raise " + Integer.toString(bet) + " dollars\n");
                    opponent2.setMoney(opponent2.getMoney() - bet);
                    bank[1] = bank[1] + bet;
                    handle = 1;
                }
                else if (response == "check") //check -> adding card
                {
                    dialogue.out("Player 2 decided to check\n");
                    handle = -1;
                    continue;
                }
                else if (response == "fold") //fold -> Player 1 won
                {
                    dialogue.out("Player 2 decided to fold\n");
                    opponent1.setMoney(opponent1.getMoney() + bank[1] + bank[0]);
                    bank[0] = 0;
                    bank[1] = 0;
                    fold = true;
                    break;
                }
            }
            else if (handle == -1) //adding cards
            {
                if (table.size() < 5)
                {
                    table.add(deck.dealCard());
                    handle = 1;
                }
                else
                {
                    handle = 1;
                    break;
                }

            }

        }
    }


    public void createFlop()
    {
        //setting table with three cards
        handle = 1;
        dealCards();
        opponent1.setAggression(opponent1.aggressiveness());
        for (int i = 0; i < 3; i++)
        {
            table.add(deck.dealCard());
        }
        flop = false;
    }

    public void options()
    {
        //dialogue for options
        dialogue.showCards(player1);
        dialogue.showTable(table);
        dialogue.out("You have " + player1.getMoney() + " dollars\n");
        dialogue.whatToDo(bank, player1.getMoney());
    }

    
}
