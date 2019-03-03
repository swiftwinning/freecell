/************
 *
 *    @author  Jeff Winning
 *    @version   Last modified 5.3.16
 *   
 *    Class for FreeCell game
 *
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/*
 *   FreeCell class creates a template for a JFrame type Object which displays the GUI for
 *   a game of Free Cell solitaire.  It relies on a Card class and on a file of Enumerated
 *   types
 */
public class FreeCell extends JFrame
{
    private Card [] gameDeck = new Card [ 52 ];
    private JButton [] freeCellButtons = new JButton [ 4 ];
    private JButton [] removePileButtons = new JButton [ 4 ];
    private JButton [] colPlaceholderButtons = new JButton [ 8 ];
    private JButton [] freeCells = new JButton [ 4 ];
    private JButton [] removePiles = new JButton [ 4 ];
    private JButton newGameButton = new JButton( "Deal a new game" );
    private JButton rulesButton = new JButton( "Instructions" );
    private JPanel freeCellPanel = new JPanel( new GridLayout( 1, 4, 6, 6 ) );
    private JPanel removePilePanel = new JPanel( new GridLayout( 1, 4, 6, 6 ) );
    private JLabel displayLabel = new JLabel();
    private final String BLANK = " ";
    private final String IS = " is selected.";
    private final String NALM = "Not a legal move.";
    private final String NEFC = "Not enough free cells.";
    private final String WIN = "Congratulations, you win!";
    private ArrayList [] columns = new ArrayList [ 8 ];
    private JPanel [] colPanels = new JPanel [ 8 ];
    private Card selected = null;
    
    /*
     *   Constructor method
     */
    public FreeCell()
    {
        /**
         *   Set basic look and feel of JFrame
         */
        this.setSize( 1000, 1000 );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );       
        this.setLayout( new BorderLayout() );
        Font f = new Font( "Helvetica", Font.PLAIN, 30 );
        
        /**
         *   Populate deck Array with 52 Card objects.  Nested for each loops create
         *   each unique possibility with 4 suits and 13 ranks.
         */
        int counter = 0;
        for( Suit s : Suit.values() )
        {
            for( Rank r : Rank.values() )
            {
                Card myCard = new Card( s, r );
                gameDeck[ counter ] = myCard;
                counter ++;
            }
        }
        
        /**
         *   Populate freeCellButtons Array and removePileButtons Array with JButtons, 
         *   set font for each JButton, and add each to the freeCells and removePiles
         *   Arrays where they will act as placeholders until Cards are placed there.
         */
        for( int i = 0; i < 4; i++ )
        {
            freeCellButtons[ i ] = new JButton( "FREE" );
            freeCellButtons[ i ].setFont( f );
            freeCells[ i ] = freeCellButtons[ i ];
            removePileButtons[ i ] = new JButton( "ACE" );
            removePileButtons[ i ].setFont( f );
            removePiles[ i ] = removePileButtons[ i ];
        }  
        
        /**
         *   Populate columns array with ArrayLists that each represent columns of Cards.
         *   Populate colPanels array with JPanels that show these columns of Cards
         *   as buttons in GUI.
         *   Lastly, populate colPlaceholderButtons array with blank JButtons that will
         *   be pulled into GUI when a column is emptied of Cards.  These buttons
         *   giver user an element to interact with in order to put other Cards back into
         *   the empty columns.
         */
        for( int col = 0; col < 8; col++ )
        {
            columns[ col ] = new ArrayList< JButton >();
            colPanels[ col ] = new JPanel( new GridLayout( 19, 1, 3, 3 ) );
            colPlaceholderButtons[ col ] = new JButton( " " );
            colPlaceholderButtons[ col ].addActionListener( new Selector() );
        }
      
        /**
         *   Add Action Listener to each Card object in the deck, to each placeholder
         *   JButton in the free cells and remove piles, and to instructions and new
         *   game buttons
         */
        for( Card c : gameDeck )
        {
            c.addActionListener( new Selector() );
            c.setFont( f );
        }
        for( int i = 0; i < 4; i++ )
        {
            freeCells[ i ].addActionListener( new Selector() );
            removePiles[ i ].addActionListener( new Selector() );
        }
        rulesButton.addActionListener( new Selector() );
        newGameButton.addActionListener( new Selector() );
               
        /**
         *   Finish look and feel by setting arrangement of JPanels and their contents
         */
        JPanel playingAreaPanel = new JPanel( new GridLayout( 1, 8, 3, 3 ) );
        JPanel topPanel = new JPanel( new GridLayout( 2, 1, 3, 3 ) );
        JPanel topCardsPanel = new JPanel( new GridLayout( 1, 2, 3, 3 ) );
        JPanel bottomPanel = new JPanel( new GridLayout( 1, 2, 3, 3 ) );
        
        /**
         *   Add each column of Card buttons to the playing area.
         */
        for( JPanel column : colPanels )
        {
            playingAreaPanel.add( column );
        }   
        
        displayLabel.setFont( f );
        displayLabel.setText( "FreeCell" );
        displayLabel.setHorizontalAlignment( JTextField.CENTER );
        
        rulesButton.setFont( f );
        newGameButton.setFont( f );
            
        topPanel.add( topCardsPanel );
        topPanel.add( displayLabel );
        
        topCardsPanel.add( freeCellPanel );
        topCardsPanel.add( removePilePanel );
        
        bottomPanel.add( rulesButton );
        bottomPanel.add( newGameButton );
        
        this.add( topPanel, BorderLayout.NORTH );
        this.add( playingAreaPanel, BorderLayout.CENTER );
        this.add( bottomPanel, BorderLayout.SOUTH );
          
        /**
         *   Finish constructor by dealing the first hand of cards.
         */
        dealCards( shuffle( gameDeck ) );
        
        this.setVisible( true );
    }
    
    /*
     *   Since the game deck remains constant to ensure each new game has a consistent
     *   basis, this method creates a new deck array, copying the contents from the
     *   game deck and randomly generating a new order.
     *
     *   @param          deck       Card [] to copy the contents from
     *   @return         shuffled   new Card [] in shuffled order
     */
    public Card [] shuffle( Card [] deck )
    {
        /**
         *   Make an ArrayList of ordered integers from 0 - 51
         */
        ArrayList< Integer > oldOrder = new ArrayList< Integer >();
        for( int i = 0; i < 52; i++ )
        {
            oldOrder.add( i );
        }
        
        /**
         *   Make an array of integers in random order using Math.random multiplied by
         *   the ordered ArrayList's size, which decrements with each integer removed.
         */
        int [] newOrder = new int [ 52 ];
        for( int i = 0; i < 52; i++ )
        {
            int nextCard = oldOrder.remove( ( int ) ( Math.random() * oldOrder.size() ) );
            newOrder[ i ] = nextCard;
        }
        
        /**
         *   Use the randomly ordered array to make a new deck array that points to each
         *   value of the ordered game deck, but with a different order each time
         *   shuffle is called.
         */
        Card [] shuffled = new Card [ 52 ];
        for( int i = 0; i < 52; i++ )
        {
            shuffled[ i ] = deck[ newOrder[ i ] ];
        }
        
        return shuffled;
    }
    
    /*
     *   At the beginning of each new game, distribute the Cards to the columns using
     *   integer division and quotient as counters.  The quotient of dividing the column
     *   counter by 8 only has the result of incrementing the row when 8 columns are 
     *   dealt out.  The remainder of dividing the column counter by 8 has no effect on
     *   the counter until 8 is reached, at which point it resets.  In other words, a
     *   card is dealt out to the first row of each column, and then back to the
     *   beginning of the columns for the next row, and so on.
     *
     *   @ param         deck          Card [] to distribute across playing area
     */
    public void dealCards( Card [] deck )
    {
        /**
         *   Clear the GUI for all new games after original deal
         */
        for( int col = 0; col < 8; col++ )
        {
            columns[ col ].clear();
        }
        int colCounter = 0;
        int rowCounter = 0;
        for( Card c : deck )
        {
            columns [ colCounter ].add( c );
            c.setCol( colCounter );
            c.setRow( rowCounter );
            colCounter++;
            rowCounter += colCounter / 8;
            colCounter = colCounter % 8;
        }
        
        for( int i = 0; i < 4; i++ )
        {
            freeCells[ i ] = freeCellButtons[ i ];
            removePiles[ i ] = removePileButtons[ i ];
        }
        
        refreshDisplay();
    }
    
    /*
     *   This method is called when the game is first dealt, and after each successful
     *   move.  It updates the GUI to reflect the changes made to the Arrays and
     *   ArrayLists by clearing all the grids and newly adding all of the Cards and
     *   JButtons in the new order, including placeholders for empty columns.
     */
    public void refreshDisplay()
    {
        /**
         *   Clear the GUI of all moveable Cards and JButtons to prepare for reordered
         *   display
         */
        for( JPanel jp : colPanels )
        {
            jp.removeAll();
        }
        freeCellPanel.removeAll();
        removePilePanel.removeAll();
        
        /**
         *   Nested for loops based on the constant number of columns (8) and the varying
         *   number of cards in each column (determined by the size() method) fill each
         *   grid in the GUI with the Cards from the corresponding ArrayList in the
         *   columns Array
         */
        for( int col = 0; col < 8; col++ )
        {
            for( int row = 0; row < columns[ col ].size(); row++ )
            {
                colPanels[ col ].add( ( JButton )columns[ col ].get( row ) );
            }
        }
        
        /**
         *   For any columns that are empty, add the placeholder Button so user
         *   has a component to interact with when returning Cards to that column
         */
        for( int col = 0; col < 8; col++ )
        {
            if( columns[ col ].size() == 0 )
            {
                columns[ col ].add( colPlaceholderButtons[ col ] );
                colPanels[ col ].add( ( JButton ) columns[ col ].get( 0 ) );
            }
        }
        
        /**
         *   Add the Cards or Buttons in both the free cells and remove piles Arrays
         */
        for( int i = 0; i < 4; i++ )
        {
            freeCellPanel.add( freeCells[ i ] );
            removePilePanel.add( removePiles[ i ] );
        }
        
        /**
         *   Reset other game data,  After a move is complete and we update the position
         *   of components, there should be no message displayed and the selected variable
         *   should be cleared to fit the next selection.
         */
        displayLabel.setText( BLANK );
        selected = null;
        
        /**
         *   Revalidate and repaint so updates will be visible
         */
        revalidate();
        repaint();
        
        /**
         *   Because refresh is called after each move, this is a perfect time to check
         *   for a completed game.  If it returns true, then display WIN message.
         */
        if( gameWon() )
        {
            displayLabel.setText( WIN );
        }
    }
    
    /*
     *   Test playing area to see if game is won and return boolean
     *
     *   @return              boolean, true if game is won
     */
    public boolean gameWon()
    {
        for( int col = 0; col < 8; col++ )
        {
            /**
             *   If the first element in a column is not a Card object, then the column
             *   is clear and has no more cards that need to be revealed.
             */
            if( ! ( columns[ col ].get( 0 ) instanceof Card ) )
            {
                continue;
            }
            /**
             *   If each column is ordered all the way to the bottom Card, aka the first
             *   element, the game is won.  If there are any columns where this is
             *   not true, then the game is not won.
             */
            if( ! topIsOrdered( ( Card ) columns[ col ].get( 0 ) ) )
            {
                return false;
            } 
        }
        return true;
    }
    
     /*
     *   Display a JOptionPane with the rules of the game whenever user clicks
     *   rulesButton.
     */
    public void displayRules()
    {
        String theRules = "Welcome to FreeCell!\n\n" +
                          "Your goal is to remove all the cards in the 8 " + 
                          "columns of the playing area to the 4 piles\n" +
                          "on the top right of the frame.  Each pile can take " + 
                          "cards of a single suit, and the cards \n" +
                          "must be moved to the pile in directly ascending " + 
                          "order starting with the Ace (Ace, 2, 3...)\n\n" +
                          "To move a card within the playing area, click once" + 
                          " to select any card at the end of a \n" +
                          "column (closest to the bottom of the frame), and" + 
                          " click a second time on your desired \n" +
                          "destination.  You can only move onto the card at" + 
                          " the end of a column, and it has to be \n" +
                          "onto a card of the opposite color and exactly 1 " + 
                          "rank higher (a red 6 can move onto a \n" +
                          "black 7, or a black 10 can move onto a red Jack).\n\n" +
                          "The 4 spaces on the top left are your free cells." + 
                          "  You can move a single card to an empty\n" +
                          "free cell at any time, and select it again whenever" + 
                          " you're ready to move it somewhere else.\n" +
                          "The free cells also can allow you to move a stack" + 
                          " of cards that are already ordered (color\n" +
                          "alternating and rank descending toward end of column)." + 
                          "  Remember, you can only move \n" +
                          "1 card at a time, but if there are enough free cells" + 
                          " they will automatically move to the \n" +
                          "empty cells and return in order once you have moved" + 
                          " the card at the bottom of the stack.\n\n" +
                          "Keep an eye on the display above the 8 columns.  " + 
                          "It will let you know when you have\n" +
                          "selected a card, if you have accidentally attempted" + 
                          " to make an illegal move, or if you\n" +
                          "tried a legal move but there are not enough free " + 
                          "spaces at the moment to execute it.\n\n" +
                          "It will also tell you when you have won, either " + 
                          "by removing all the cards to the piles on\n" +
                          " top, or by organizing all the remaining cards in" + 
                          " alternating color and descending order.\n\n" +
                          "If you forget any rules, you can see them at any " + 
                          "time by pressing the Instructions button,\n" +
                          "and if you want to play again after winning or getting" + 
                          " stuck, you can Deal a new game\n" +
                          "whenever you want.\n\n" +
                          "Good luck!";
        JOptionPane.showMessageDialog( null, theRules );
    }
    
    /*
     *   The following 4 methods execute moves between different parts of the game (or
     *   moves within the same area of the game.)  All the most significant testing has
     *   been done in the source methods below, so these are called when moves are ready
     *   to be executed or have only final simple conditions remaining.  The methods are
     *   divided by destination: column in playing area, empty column in playing area, 
     *   free cell area, and remove pile area.
     */
        
    /*
     *   Execute a move to the playing area of a single card, or of multiple cards if 
     *   they are in proper order and color arrangement and if the amount of free cells 
     *   permits.
     *
     *   @param      destination   the Card on top of which the selected Card(s) will move
     */
    public void moveToPlayingArea( Card destination )
    {
        /**
         *   Get relevant indexes in easier to use variables.
         */
        int selCol = selected.getCol();
        int selRow = selected.getRow(); 
        int destCol = destination.getCol();
        int destRow = destination.getRow() + 1;
        /**
         *   First determine if selected card is currently in a free cell because if so 
         *   its indexes must be processed differently.
         */
        if( selRow == -1 )
        {
            columns[ destCol ].add( selected );
            freeCells[ selCol ] = freeCellButtons[ selCol ];
            selected.setCol( destCol );
            selected.setRow( destRow );
        }
        /**
         *   Selected card must be in the playing area so its indexes can be processed
         *   the conventional way.
         */
        else
        {
            /**
             *   Handle the possibility of multiple cards moving.  If this method was 
             *   called, the Cards on top must be ordered properly or the Card could not
             *   have been selected in the first place, and there must be enough free
             *   cells.  The Card in the selected variable is reassigned each time using
             *   the same coordinates because as the highest ranked cards are moved,
             *   the lower ones that were on top fall back to take that index.  Therefore
             *   the only variable that needs to be updated in this loop is the
             *   destination row so the Cards can properly save their index variables
             *   to higher stacked rows.
             */
            for( int i = cardsOnTop( selected ); i >= 0; i-- )
            {
                selected = ( Card ) columns[ selCol ].get( selRow );
        
                columns[ destCol ].add( columns[ selCol ].remove( selRow ) );
        
                selected.setCol( destCol );
                selected.setRow( destRow );
        
                destRow ++;
            }
        }
        refreshDisplay();
    }
    
    /*
     *   Similar to moveToPlayingArea in that it must be able to move multiple Cards.
     *   Must be handled differently because in this case we have to remove the
     *   placeholder JButton before doing anything else.   Separate cases for selected
     *   Cards coming from free cells or from playing area.
     *
     *   @param         col        integer of Array index of column chosen
     */    
    public void moveToEmptyColumn( int col )
    {    
        /**
         *   Get relevant indexes in easier to use variables.
         */
        int selCol = selected.getCol();
        int selRow = selected.getRow();
        int destRow = 0;
        /**
         *   Start by determining if selected Card is in free cells area.  If so, indexes 
         *   need to be processed differently.
         */
        if( selRow == -1 )
        {
            columns[ col ].remove( 0 );
            columns[ col ].add( selected );
            freeCells[ selCol ] = freeCellButtons[ selCol ];
            selected.setCol( col );
            selected.setRow( destRow );
        }
        /**
         *   Selected card must be in the playing area so its indexes can be processed
         *   the conventional way.
         */
        else
        {
            /**
             *   Handle the possibility of multiple cards moving.  The Card in the 
             *   selected variable is reassigned each time using the same coordinates 
             *   because as the highest ranked cards are moved, the lower ones that were 
             *   on top fall back to take that index.  Therefore the only variable that 
             *   needs to be updated in this loop is the destination row, initially set
             *   to 0 because we are building a new column, so the Cards can properly 
             *   save their index variables to higher stacked rows.
             */
            columns[ col ].remove( 0 );
            for( int i = cardsOnTop( selected ); i >= 0; i-- )
            {
                selected = ( Card ) columns[ selCol ].get( selRow );
                columns[ col ].add( columns[ selCol ].remove( selRow ) );
                selected.setCol( col );
                selected.setRow( destRow );
                destRow ++;
            }
        }
        refreshDisplay();
    }
    
    /*
     *   Any single selected Card can be moved to a free cell, so only need to test if
     *   selected Card has any on top.  Otherwise the selected card should be placed in 
     *   the freeCells Array at index i and removed from the column it was just in (or 
     *   from the free cell it was just in, which is a legal albeit pointless move.)
     *
     *   @param     destination     placeholder JButton chosen as destination for move
     *   @param     i               integer value of Array index of chosen column
     */
    public void moveToFreeCells( JButton destination, int i )
    {
        if( selected.getRow() == -1)
        {
            freeCells[ selected.getCol() ] = freeCellButtons[ selected.getCol() ];
            freeCells[ i ] = selected;
            selected.setCol( i );
        }
        else if( cardsOnTop( selected ) == 0 )
        {       
            int selCol = selected.getCol();
            int selRow = selected.getRow();
            freeCells[ i ] = ( Card ) columns[ selCol ].remove( selRow );
            selected.setCol( i );
            selected.setRow( -1 );
        }
        refreshDisplay();
    }
    
    /*
     *   Remove Cards from gameplay and free up space for more moves.  Separate cases
     *   for removing from free cell Array or from an ArrayList from columns.
     *
     *   @param      i        integer of Array index of chosen remove pile
     */
    public void removeToPile( int i )
    {
        int selCol = selected.getCol();
        int selRow = selected.getRow();
        if( selRow == -1 )
        {
            removePiles[ i ] = selected;
            freeCells[ selCol ] = freeCellButtons[ selCol ];
        }
        else
        {
            removePiles[ i ] = ( Card ) columns[ selCol ].remove( selRow );
        }
        refreshDisplay();
    }
    
    /*
     *   Determine the number of free cells available for a move of multiple
     *   ordered Cards.  Add empty columns to cells in free cell Array
     *
     *   @return           integer representing number of free spaces
     */
    public int getFreeCells()
    {
        int count = 0;
        for( int col = 0; col < 8; col++ )
        {
            /**
             *   Count empty columns.  If a column is empty, the ArrayList will have 
             *   exactly one element, and it will be a JButton, not a Card.
             */
            if( ( columns[ col ].size() == 1 ) && 
                ! ( columns[ col ].get( 0 ) instanceof Card ) )
            {
                count++;
            }
        }
        
        for( int i = 0; i < 4; i++ )
        {
            /**
             *   Count empty free cells.  A free cell is empty if it contains a 
             *   placeholder  JButton, as opposed to being filled by a Card.
             */
            if( ! ( freeCells[ i ] instanceof Card ) )
            {
                count++;
            }
        }
        return count;
    }
        
        
    
    /*
     *   Determine how many Cards are on top, useful for comparison to number of free
     *   cells.
     *
     *   @param     c       Card to be tested for how many other Cards are on top
     *   @return            an integer representing that number of Cards
     */
    public int cardsOnTop( Card c )
    {
        /**
         *   If Card is in free cell Array, automatically return 0 since the cells by
         *   default hold single Cards, not stacks
         */
        if( c.getRow() == -1 )
        {
            return 0;
        }
        /**
         *   If Card is in playing area, use stored index values and size of column to
         *   calculate how many are between this Cards's position and top of pile.
         */
        else
        {
            int col = c.getCol();
            int difference = columns[ col ].size() - c.getRow() -1;
            return difference;
        }
    }
    
    /*
     *   Method to find the Card immediately on top of an input Card.  Relies on saved
     *   index values in the Card object, and increments the row to find the next.
     *   Needed for the topIsOrdered method to judge the relationship between each
     *   successive pair of Cards.
     *
     *   @param     c        the Card which is reference point to the one above it.
     *   @return             the next Card directly on top of input Card
     */
    public Card nextCardUp( Card c )
    {
        int newCol = c.getCol();
        int newRow = c.getRow() + 1;
        Card nextCard = ( Card )( columns[ newCol ].get( newRow ) );
        return nextCard;
    }
    
    /*
     *   Test if input Card is covered only by ordered Cards.  Must be ordered both in
     *   descending numerical order, and alternating color order.  Recursive method tests
     *   one relationship at a time.  If any relationship between 2 cards does not match,
     *   the whole method returns false.  If we get to (or start at) a point where there
     *   are no cards on top, everything must be ordered and returns true.
     *
     *   @param    c      Card to be tested whether it is covered only by ordered Cards
     *   @return           boolean whether or not this condition of being ordered is true
     */
    public boolean topIsOrdered( Card c )
    {
        if( cardsOnTop( c ) == 0 )
        {
            return true;
        }
        else
        {
            Card top = nextCardUp( c );
            if( ( c.compareRank( top ) == 1 ) && ( ! c.sameColor( top ) ) )
            {
                return topIsOrdered( top );
            }
            else
            {
                return false;
            }
        }
    }
    
    /*
     *   The following 8 methods are called by the ActionListener and they separate how 
     *   to deal with each situation when a Card is or is not saved in the variable 
     *   selected, and whether the Card or JButton clicked is in the free cell area,
     *   remove pile area, or the playing area, and whether a selected column is empty.
     *   These test the conditions and call move methods when valid.
     */
    
    /*
     *   Source is in free cells area and a Card is already selected.
     *   If the Object is a Card, then that slot is already taken and it is an illegal 
     *   move, or if there are any Cards on top of the selected Card it is also illegal
     *   because a cell only has space for one.  
     *
     *   @param     o       Object (Card or JButton) clicked to be destination of move
     *   @param     i       integer index of free cell Array to be destination of move
     */
    public void sourceFreeCellsSelected( Object o, int i )
    {
        /**
         *   Make sure cell is not filled by a card and that selected has no cards on top.
         *   In order to test how many cards are on top, first make sure it is in
         *   playing area or otherwise Card's index variables will not work with
         *   cardsOnTop method.
         */
        if( o instanceof Card || cardsOnTop( selected ) > 0 )
        {
            notALegalMove();
        }
        /**
         *   If it passes both tests, execute the move to the empty cell (actually
         *   a placeholder JButton.)
         */
        else
        {
            moveToFreeCells( ( JButton ) o, i );
        }
    }
    
    /*
     *   Source is in remove piles area and a Card is already selected.  If Object is a
     *   Card, then Cards have already been moved to this pile and selected Card must
     *   be of same suit and valued exactly 1 rank higher to be added to the pile,
     *   otherwise it is an illegal move.  If Object is not a Card, it is one of the
     *   placeholder buttons that can be replaced by an Ace (aka a Card of value 1) of 
     *   any suit.
     *
     *   @param      o     the Object (Card or JButton) chosen to remove a Card to
     *   @param      i     the integer index value of remove piles Array chosen
     */
    public void sourceRemovePilesSelected( Object o, int i )
    {
        /**
         *   Start by seeing if pile has Cards already, and testing to see in selected
         *   Card matches the conditions to be added to pile.  If so, execute move.
         */
        if( o instanceof Card )
        {
            Card pile = ( Card ) o;
            if( selected.sameSuit( pile ) && ( selected.compareRank( pile ) == 1 ) )
            {
                removeToPile( i );
            }
            else
            {
                notALegalMove();
            }
        }
        /**
         *   If a pile has not been started, see if selected Card is an Ace (has a value
         *   of 1), and if so, execute move.
         */
        else
        {
            if( selected.getValue() == 1 )
            {
                removeToPile( i );
            }
            else
            {
                notALegalMove();
            }
        }
    }
    
    /*
     *   A card is selected and an empty column has been chosen as a destination.  There
     *   are none of the ordinary restrictions about destination Card being in the 
     *   matching order because there is no destination Card, it is blank.  However the
     *   number of free cells must be calculated slightly differently.
     *
     *   @param      col      the integer index of the empty column chosen as destination
     */
    public void sourceEmptyColumnSelected( int col )
    {
        /**
         *   In this case, the number of free cells must be strictly greater than the
         *   number of Cards on top of the selected Card, as opposed to greater that
         *   or equal to.  This is because, while the column is blank at the time of
         *   this test, it will immediately be filled by the selected Card and can no
         *   longer serve as an interim holding place for the cards on top.
         */
        if( getFreeCells() > cardsOnTop( selected ) )
        {
            moveToEmptyColumn( col );
        }
        else
        {
            notEnoughFreeCells();
        }
    }
    
    /*
     *   The most common move in gameplay, and also the one with the most restrictions.
     *   Tests them all before proceeding.  Selected Card must be covered only by Cards
     *   in descending number and alternating color order (or not covered), the clicked
     *   Card must be of opposite color from the one stored in selected, the clicked Card
     *   must be exactly 1 number higher in rank than the selected Card, and the
     *   Card clicked to be the destination must have no other Cards covering it.  If
     *   Any one of these conditions is not met the move is outright illegal.  Finally,
     *   the number of free cells is calculated and compared to the number of Cards on
     *   top of selected.  If this condition is not met, the move still cannot execute,
     *   but the user is notified that the move would be valid if not for this reason.
     *
     *   @param      destination  the Card chosen to be the destination of a move
     */
    public void sourcePlayingAreaSelected( Card destination )
    {
        if( ( ( topIsOrdered( selected ) ) && ( ! destination.sameColor( selected ) ) &&
            ( destination.compareRank( selected ) == 1 ) && 
            ( cardsOnTop( destination ) == 0 ) ) )
        {
            if( ( getFreeCells() >= cardsOnTop( selected ) ) || selected.getRow() == -1 )
            {
                moveToPlayingArea( destination );
            }
            else
            {
                notEnoughFreeCells();
                return;
            }
        }
        else
        {
            notALegalMove();
        }
    }
    
    /*
     *   Any Card that is temporarily stored in the free cells can be selected, as
     *   long as another Card is not already selected, and as long as the JButton
     *   clicked is in fact a Card.  If a placeholder button is clicked instead,
     *   update screen to BLANK to show that a Card is still not selected.
     *
     *   @param     o      the Object (Card or JButton) that was clicked to be selected
     *   @param     i      the integer index of the free cell Array clicked
     */
    public void sourceFreeCellsNotSelected( Object o, int i )
    {
        if( o instanceof Card )
        {
            selected = ( Card ) o;
            displayLabel.setText( selected.toString() + IS );
        }
        else
        {
            displayLabel.setText( BLANK );
        }
    }
    
    /*
     *   The remove piles are so called because the cards have been permanently removed
     *   from gameplay, therefore there is nothing to be done except update the display
     *   to BLANK to confirm no Card has been selected.
     */
    public void sourceRemovePilesNotSelected()
    {
        displayLabel.setText( BLANK );
    }
    
    /*
     *   There is similarly nothing to select from an empty column.  The only thing to do
     *   is update display to BLANK to confirm no Card is selected
     */
    public void sourceEmptyColumnNotSelected()
    {
        displayLabel.setText( BLANK );
    }
    
    /*
     *   If no Card is currently selected, then a Card from the playing area can fill
     *   the variable and have the display updated to reflect that, provided that all the 
     *   Cards on top are in descending number and alternating color order.  If not, 
     *   no card is selected and display is updated to BLANK.
     *
     *   @param      clicked    the Card clicked to be selected
     */
    public void sourcePlayingAreaNotSelected( Card clicked )
    {
        if( topIsOrdered( clicked ) )
        {
            selected = clicked;
            displayLabel.setText( selected.toString() + IS );
        }
        else
        {
            displayLabel.setText( BLANK );
        }
    }
    
    /*
     *   Clear selected Card, and notify user the move that was attempted is not valid
     */
    public void notALegalMove()
    {
        displayLabel.setText( NALM );
        selected = null;
    }
    
    /*
     *   Clear selected Card, and notify user that the move does not have enough free 
     *   cells to accomodate the number of Cards involved.  Distinct from not being a
     *   legal move, because all other conditions are met and it would be otherwise valid
     */
    public void notEnoughFreeCells()
    {
        displayLabel.setText( NEFC );
        selected = null;
    }
    
    /*
     *   Determine which method to call based on which button or Card was picked, whether 
     *   a Card is currently selected, and which area (playing area, free cells, or
     *   remove piles) the clicked button or Card is currently in.
     *
     *   @param         o       the Object (Card or JButton) that triggered an ActionEvent
     */
    public void determineSource( Object o )
    {
        /**
         *   New game and instructions are independent of whether Card is selected.
         */
        if( newGameButton == o )
        {
            dealCards( shuffle( gameDeck ) );
            return;
        }
        if( rulesButton == o )
        {
            displayRules();
            return;
        }
        /**
         *   Begin dividing situations based on whether a Card is currently saved in
         *   the selected variable
         */
        if( selected != null )
        {
            /**
             *   If user clicks same card that was selected immediately before, then
             *   deselect it.
             */
            if( selected == o )
            {
                displayLabel.setText( BLANK );
                selected = null;
                return;
            }
            /**
             *   See if source (Card or JButton) is in free cells area
             */
            for( int i = 0; i < 4; i++ )
            {
                if( freeCells[ i ] == o )
                {
                    sourceFreeCellsSelected( o, i );
                    return;
                }
            }
            /**
             *   See if source (Card or JButton) is in remove piles cells area
             */
            for( int i = 0; i < 4; i++ )
            {
                if( removePiles[ i ] == o )
                {
                    sourceRemovePilesSelected( o, i );
                    return;
                }
            }
            
            for( int col = 0; col < 8; col++ )
            {
                if( colPlaceholderButtons[ col ] == o )
                {
                    sourceEmptyColumnSelected( col );
                    return;
                }
            }
            
            /**
             *   The source must be in playing area, and must be a Card
             */
             Card clicked = ( Card ) o;
             sourcePlayingAreaSelected( clicked );
        }
        /**
         *   At this point we know a Card must not be currently selected
         */
        else
        {
            /**
             *   See if source (Card or JButton) is in free cells area
             */
            for( int i = 0; i < 4; i++ )
            {
                if( freeCells[ i ] == o )
                {
                    sourceFreeCellsNotSelected( o, i );
                    return;
                }
            }
            
            /**
             *   See if source (Card or JButton) is in remove piles area
             */
            for( int i = 0; i < 4; i++ )
            {
                if( removePiles[ i ] == o )
                {
                    sourceRemovePilesNotSelected();
                    return;
                }
            }
            
            /**
             *   See if source (Card or JButton) is an empty column placeholder
             */
            for( int col = 0; col < 8; col++ )
            {
                if( colPlaceholderButtons[ col ] == o )
                {
                    sourceEmptyColumnNotSelected();
                    return;
                }
            }
            /**
             *   The source must be in playing area, and must be a Card
             */
             Card clicked = ( Card ) o;
             sourcePlayingAreaNotSelected( clicked );
        }
    }
    
    /*
     *   Performs when a Button event is heard, calls determineSource method since the 
     *   action to be taken depends heavily on which area of the game was clicked
     */
    class Selector implements ActionListener
    {
        public void actionPerformed( ActionEvent ae )
           {
               JButton clicked = ( JButton ) ae.getSource();
               clicked.setFocusPainted( false );
               determineSource( ae.getSource() );
           }
    }
        
    /*
     *   Main method calls constructor method to start the game
     */
    public static void main( String [] args )
    {
        FreeCell thisGame = new FreeCell();
    }
}