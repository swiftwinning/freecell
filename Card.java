/************
 *
 *    Card class for FreeCell game or other games involving playing cards.  Relies
 *    on a file of 3 Enumerated types: Rank, Suit, and Clr (Color).  Contains constructor,
 *    basic getters and setters, and some methods specific to the game Free Cell.
 *
 *    @author  Jeff Winning
 *    @version   Last modified 5.2.16
 *  
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/*
 *   Card class is a template for a playing card Object, specialized for Free Cell
 *   Solitaire in this implementation, but could be used for other games and programs
 *   requiring playing cards with little or no
 */
public class Card extends JButton
{
    /*
     *     Instance variables.
     *     Think about whether these need to be in class level or constructor level
     *     If it will still work from constructor, that is better.
     */
    private Rank cardRank;   
    private Suit cardSuit;
    private Clr cardColor;
    private int row;
    private int col;
    
    /*
     *   Constructor method.  Takes Suit and Rank as parameters, and stores in instance
     *   variables.  Determine's Clr (color) based on Suit, and sets foreground color of
     *   Card accordingly.  Finally, calls toString() method as part of setText(), which
     *   is inherited from JButton.
     *
     *   @param        theSuit            Suit enum of the new Card
     *   @param        theRank            Rank enum of the new Card
     */
    public Card( Suit theSuit, Rank theRank )
    {
        cardRank = theRank;
        cardSuit = theSuit;
        if( cardSuit == Suit.SPADES || cardSuit == Suit.CLUBS )
        {
            cardColor = Clr.BLACK;
            setForeground( Color.BLACK );
        }
        else if( cardSuit == Suit.HEARTS || cardSuit == Suit.DIAMONDS )
        {
            cardColor = Clr.RED;
            setForeground( Color.RED );
        }
        this.setText( this.toString() );
    }
    
    /*
     *   Basic getter.  Returns row index of Card's current position
     *
     *   @return                     integer, current row index
     */
    public int getRow()
    {
        return row;
    }
    
    /*
     *   Basic getter.  Returns column index of Card's current position
     *
     *   @return                     integer, current column index
     */
    public int getCol()
    {
        return col;
    }
    
    /*
     *   Basic getter.  Returns value of Card's rank
     *
     *   @return                     integer value of Card rank
     */
    public int getValue()
    {
        return cardRank.value();
    }
    
    /*
     *   Basic getter.  Returns Suit of Card
     *
     *   @return                     card suit
     */
    public Suit getSuit()
    {
        return cardSuit;
    }
    
    /*
     *   Basic getter.  Returns Clr (color) of Card
     *
     *   @return                     card color
     */
    public Clr getClr()
    {
        return cardColor;
    }
    
    /*
     *   Basic setter.  Sets the stored row index of current position of Card in
     *   FreeCell game.
     *
     *   @param           newRow          integer, the new roe index to be stored
     */
    public void setRow( int newRow )
    {
        this.row = newRow;
    }
    
    /*
     *   Basic setter.  Sets the stored column index of current position of Card in
     *   FreeCell game.
     *
     *   @param           newCol           integer, the new column index to be stored
     */
    public void setCol( int newCol )
    {
        this.col = newCol;
    }
    
    /*
     *   Method used in FreeCell to calculate difference in rank between Card Object
     *   supplied in actual argument and the implicit argument.  Positive number if
     *   implicit argument if higher rank, negative number if actual argument is higher,
     *   and returns 0 if they are of equal rank.
     *
     *   @param         other          other Card Object to compare to
     *   @return                       integer, difference in rank between Cards
     */
    public int compareRank( Card other )
    {
        return this.getValue() - other.getValue();
    }
    
    /*
     *   Method used in FreeCell to determine if a Card specified in the actual argument
     *   is the same Suit as the implicit argument.
     *
     *   @param         other          other Card Object to compare to
     *   @return                       boolean, true if Cards are same suit.
     */
    public boolean sameSuit( Card other )
    {
        return this.getSuit() == other.getSuit();
    }
    
    /*
     *   Method used in FreeCell to determine if a Card specified in the actual argument
     *   is the same Clr (same color) as the implicit argument.
     *
     *   @param         other          other Card Object to compare to
     *   @return                       boolean, true if Cards are same color.
     */
    public boolean sameColor( Card other )
    {
        return this.getClr() == other.getClr();
    }
    
    /*
     *   Basic toString method returns concise label for Card using abbreviations
     *   for Rank and Suit, separated by a space.
     *
     *   @return                   String label for Card
     */
    public String toString()
    {
        return ( cardRank.abbr() + " " + cardSuit.abbr() );
    }
}