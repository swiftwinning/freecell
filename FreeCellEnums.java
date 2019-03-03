/************
 *
 *    Enumerated Types for FreeCell game, corresponding to the Rank, Suit, and Color of
 *    a playing card and related information.
 *
 *    @author  Jeff Winning
 *    @version   Last modified 5.1.16
 *   
 */

/**
 *   Rank has a constructor with an integer value and a String representing the card's
 *   abbreviation as its 2 parameters.  Rank stores these values in instance variables,
 *   and has a getter method for each.
 */
enum Rank
{
    ACE( 1, "A" ), TWO( 2, "2" ), THREE( 3, "3" ), FOUR( 4, "4" ), FIVE( 5, "5" ), 
    SIX( 6, "6" ), SEVEN( 7, "7" ), EIGHT( 8, "8" ), NINE( 9, "9" ), TEN( 10, "10" ), 
    JACK( 11, "J" ), QUEEN( 12, "Q" ), KING( 13, "K" );
    /**
     *   Constructor for Rank enumerated type
     *
     *   @param      value       integer value of rank
     *   @param      abbr        String abbreviation for easier display
     */
    Rank( int value, String abbr )
    {
        this.value = value;
        this.abbr = abbr;
    }
    private final int value;
    private final String abbr;
    /**
     *   Getter method for Rank.value
     *
     *   @return              integer value of Rank
     */
    public int value()
    {
        return value;
    }
    /**
     *   Getter method for Rank.abbr
     *
     *   @return              String abbreviation of Rank
     */
    public String abbr()
    {
        return abbr;
    }       
}
    
/**
 *   Suit has a constructor with a single String as an input parameter, which uses
 *   Unicode representations of the 4 suits of cards.  Stores the String in an instance
 *   variable, and has a getter method to return it.
 */
enum Suit
{
    SPADES( "\u2660" ), DIAMONDS( "\u2666" ), CLUBS( "\u2663" ) , HEARTS( "\u2665" );
    /**
     *   Constructor for Suit enumerated type
     *
     *   @param      abbr        String abbreviation for easier display
     */
    Suit( String abbr )
    {
        this.abbr = abbr;
    }
    private final String abbr;
    /**
     *   Getter method for Suit.abbr
     *
     *   @return              String abbreviation of Rank
     */
    public String abbr()
    {
        return abbr;
    }
}
    
/**
 *   Clr (to distinguish from Color class in java.awt) has 2 values, red and black
 */
enum Clr
{
    RED, BLACK;
}