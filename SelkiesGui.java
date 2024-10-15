//imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;
import java.util.Random;
import java.io.*;
import java.io.File;
import java.io.IOException;

public class SelkiesGui implements ActionListener {
    
    //global variables
    private final static String defaultFile = "selkiesFile.txt";
    private int GRID_SIZE_X = 10, GRID_SIZE_Y = 10;
    private JButton [] buttonArray;
    static JFrame frame = new JFrame("SelkiesGUI");
    static String P2Text = "<html><font color='blue'> P2 </font>";
    static String P1Text = "<html><font color='red'> P1 </font>";
    private Player[] playerArray = new Player[2];
    private int currentPlayer = 0; 
    private Munro[] munrosArray = new Munro[7];
    private Selkie[] selkiesArray = new Selkie[7];
    private Nessie nessie;

    /**
     * creates the menu
     * @return menuBar
     */
    private JMenuBar createMenu()
    {
        JMenuBar menuBar = new JMenuBar();;
        JMenu menu = new JMenu("Game Menu");

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(this);
        menu.add(newGameItem);

        JMenuItem loadGameItem = new JMenuItem("Load Game");
        loadGameItem.addActionListener(this);
        menu.add(loadGameItem);

        JMenuItem saveGameItem = new JMenuItem("Save Game");
        saveGameItem.addActionListener(this);
        menu.add(saveGameItem);

        JMenuItem helpGameItem = new JMenuItem("Instructions");
        helpGameItem.addActionListener(this);
        menu.add(helpGameItem);

        menuBar.add(menu); 

        JMenu diceMenu = new JMenu("Roll Dice");
        JMenuItem newDiceItem = new JMenuItem("Roll Dice");
        newDiceItem.addActionListener(this);
        diceMenu.add(newDiceItem);

        menuBar.add(diceMenu);

        return menuBar;
    }

    /**
     * create the grid for the game
     * @return grid
     */
    private Container createContentPane()
    {
        int numButtons = GRID_SIZE_X * GRID_SIZE_Y;
        JPanel grid = new JPanel(new GridLayout(GRID_SIZE_Y, GRID_SIZE_X));
        buttonArray = new JButton[numButtons];

        for (int i=numButtons - 1; i >= 0; i--)
        {
            buttonArray[i] = new JButton(String.valueOf(i+1));

            buttonArray[i].setActionCommand("" + i);
            buttonArray[i].addActionListener(this);
            buttonArray[i].setBackground(Color.WHITE);

            grid.add(buttonArray[i]);
        }

        return grid;
    }

    /**
     * Registers and runs when an option in the menu is clicked
     * @param ActionEvent
     */
    public void actionPerformed (ActionEvent e)
    {
        JMenuItem menusource = (JMenuItem)(e.getSource());
        String menuText = menusource.getText();

        if (menuText.equals("Load Game"))
        {
            LoadGame();
        }
        else if (menuText.equals("Save Game"))
        {
            SaveGame();
        }
        else if (menuText.equals("New Game"))
        {
            NewGame();
        }
        else if (menuText.equals("Roll Dice"))
        {
            RollDice();
        }
        else if (menuText.equals("Instructions"))
        {
        displayInstructions();
        }
    }

    /**
     * Show the GUI
     */
    private static void createAndShowGUI()
    {
        SelkiesGui selkiesGUI = new SelkiesGui();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(selkiesGUI.createContentPane());
        frame.setJMenuBar(selkiesGUI.createMenu());

        frame.setSize(800, 600);
        frame.setVisible(true);
    }


    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

    /**
     * Start a new game
     */
    private void NewGame()
    {
        System.out.println("New Game Selected");
        setSelkieMunroNessie();
        setPlayer();
        buttonArray[0].setText(P1Text + P2Text);

        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rollDice");
        actionMap.put("rollDice", new AbstractAction() {
            public void actionPerformed(ActionEvent e)
            {
                RollDice();
            }
        });
    }

    /**
     * Load a previously saved game
     */
    private void LoadGame()
    {
        System.out.println("Load Game Selected");
        //Load Game

        File dirPath = new File("GameSaves/");
        String[] files = dirPath.list();
        String stringFiles = Arrays.toString(files);
        System.out.println(stringFiles);
        String fileName = JOptionPane.showInputDialog("Which game do you want to load: \n" + stringFiles);
        System.out.println(fileName);

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String nextLine;

        setPlayer();
        clearBoard();

        try
        {
            fileReader = new FileReader("GameSaves/"+fileName);
            bufferedReader = new BufferedReader(fileReader);
            nextLine = bufferedReader.readLine();

            String[] players = nextLine.split(" ");
            System.out.println(players[0]);
            System.out.println(players[1]);
            playerArray[0].setPosition(Integer.valueOf(players[0]));
            playerArray[1].setPosition(Integer.valueOf(players[1]));
            if (players[0].equals(players[1]))
            {
                buttonArray[Integer.valueOf(players[0])].setText(P1Text + P2Text);
            }
            else
            {
                buttonArray[Integer.valueOf(players[0])].setText(P1Text);
                buttonArray[Integer.valueOf(players[1])].setText(P2Text);
            }

            
            nextLine = bufferedReader.readLine();
            String[] selkies;
            for (int i=0; i < 7; i++)
            {
                selkies = nextLine.split(" ");
                selkiesArray[i].setHead(Integer.valueOf(selkies[0]));
                selkiesArray[i].setTail(Integer.valueOf(selkies[1]));
                buttonArray[Integer.valueOf(selkies[0])-1].setText("Head"); 
                buttonArray[Integer.valueOf(selkies[0])-1].setBackground(Color.CYAN);
                nextLine = bufferedReader.readLine();
            }

            String[] munros;
            for (int i=0; i < 7; i++)
            {
                munros = nextLine.split(" ");
                munrosArray[i].setTip(Integer.valueOf(munros[0]));
                munrosArray[i].setFoot(Integer.valueOf(munros[1]));
                buttonArray[Integer.valueOf(munros[1])-1].setText("Foot");
                buttonArray[Integer.valueOf(munros[1])-1].setBackground(Color.GREEN);
                nextLine = bufferedReader.readLine();
            }

            nessie = new Nessie();
            nessie.setSpace(Integer.valueOf(nextLine));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        catch (IOException e)
        {
            System.out.println("An error occured");
        }
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch(IOException e)
                {
                    System.out.println("An error occured closing the file");
                }
            }
        }

    }

    /**
     * Save a currently in progress game
     */
    private void SaveGame()
    {
        System.out.println("Save Game Selected");

        new File("GameSaves").mkdir();

        FileOutputStream outputStream = null;
        PrintWriter printWriter = null;

        String fileName = JOptionPane.showInputDialog("Enter file name: ");
        
        if (fileName == null)
        {
            createAndShowGUI();   
        }

        try
        {
            outputStream = new FileOutputStream("GameSaves/"+fileName+".txt");
            printWriter = new PrintWriter(outputStream);
            
            //save player positions
            printWriter.print(playerArray[0].getPosition() + " " + playerArray[1].getPosition() + "\n");

            //save selkie
            for (int i=0; i < 7; i++)
            {
                printWriter.print(selkiesArray[i].getHead() + " " + selkiesArray[i].getTail() + "\n");
            }

            //save munro
            for (int i=0; i < 7; i++)
            {
                printWriter.print(munrosArray[i].getTip() + " " + munrosArray[i].getFoot() + "\n");
            }

            //save nessie
            printWriter.print(nessie.getSpace());
        }
        catch (IOException e)
        {
            System.out.println("Sorry, an error occured");
        }
        finally
        {
            if (printWriter != null)
            {
                printWriter.close();
            }
        }
    }

    /**
     * Roll a dice - generate a random between between 1 and 6
     */
    private void RollDice()
    {
        Random rand = new Random();
        int diceRoll = rand.nextInt(1, 7);
        JOptionPane.showMessageDialog(frame, ("You rolled: " + diceRoll), ("Player " + (currentPlayer+1) + " Turn"), JOptionPane.INFORMATION_MESSAGE);
        movePlayer(diceRoll);
    }

    /**
     * Display the instructions 
     */
    private void displayInstructions()
    {
        System.out.println("Instructions displaying");
        JOptionPane.showMessageDialog(frame, ("Player 1 and Player 2 take it in turns to roll the dice, and then they are moved forward. \n If you land on a Head, you fall down to the selkie tail. \n If you land on a foot, you climb up the munro to its tip. \n There is a random nessie space on the board, that randomises both your positions. \n You can also press 'r' to roll the dice."), "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Set the selkies, munros and nessie and display the selkie and munro on the board
     */
    private void setSelkieMunroNessie()
    {
        int selkieHead;
        int selkieTail;
        Integer[] selkieHeadPos = new Integer[7];
        Integer[] selkieTailPos = new Integer[7];

        int munroFoot;
        int munroTip;
        Integer[] munroFootPos = new Integer[7];
        Integer[] munroTipPos = new Integer[7];

        int nessieSpace;

        clearBoard();

        nessie = new Nessie();
        nessieSpace = nessie.getSpace();
        System.out.println("Nessie Space: " + nessieSpace);

        for (int i=0; i < 7; i++)
        {
            do 
            {
                selkiesArray[i] = new Selkie();
                selkieHead = selkiesArray[i].getHead() - 1;
                selkieTail = selkiesArray[i].getTail() - 1;
            }
            while (Arrays.asList(selkieHeadPos).contains(selkieHead) || Arrays.asList(selkieHeadPos).contains(selkieTail) || Arrays.asList(selkieTailPos).contains(selkieHead) || Arrays.asList(selkieTailPos).contains(selkieTail) || selkieHead == nessieSpace-1 || selkieTail == nessieSpace-1);
            
            selkieHeadPos[i] = selkieHead;
            selkieTailPos[i] = selkieTail;

            buttonArray[selkieHead].setText("Head"); 
            buttonArray[selkieHead].setBackground(Color.CYAN);
            System.out.println("Selkie " + (i+1) + " + head: " + selkiesArray[i].getHead() + " and tail: " + selkiesArray[i].getTail());
        }

        for (int i=0; i < 7; i++)
        {
            do 
            {
                munrosArray[i] = new Munro();
                munroFoot = munrosArray[i].getFoot() - 1;
                munroTip = munrosArray[i].getTip() - 1;
            } 
            while (Arrays.asList(munroFootPos).contains(munroFoot) || Arrays.asList(munroFootPos).contains(munroTip) || Arrays.asList(munroTipPos).contains(munroFoot) || Arrays.asList(munroTipPos).contains(munroTip) || Arrays.asList(selkieHeadPos).contains(munroTip) || Arrays.asList(selkieHeadPos).contains(munroFoot) || Arrays.asList(selkieTailPos).contains(munroTip) || Arrays.asList(selkieTailPos).contains(munroFoot) || munroFoot == nessieSpace-1 || munroTip == nessieSpace-1);

            munroFootPos[i] = munroFoot;
            munroTipPos[i] = munroTip;

            buttonArray[munroFoot].setText("Foot");
            buttonArray[munroFoot].setBackground(Color.GREEN);
            System.out.println("Munro " + (i+1) + " + tip: " + munrosArray[i].getTip() + " and Foot: " + munrosArray[i].getFoot());
        }
    }

    /**
     * Clear the board of any previous games
     */
    private void clearBoard()
    {
        for (int i=99; i >= 0; i--)
        {
            buttonArray[i].setText(String.valueOf(i+1)); 
            buttonArray[i].setBackground(Color.WHITE);
        }
    }

    /**
     * Set up the players and display them on space 1 on the board
     */
    private void setPlayer()
    {
        for (int i=0; i < playerArray.length; i ++)
        {
            playerArray[i] = new Player();
        }
    }

    /**
     * Move the player who just rolled the dice
     * @param diceRoll
     */
    private void movePlayer(int diceRoll)
    { 
        int playerPos = playerArray[currentPlayer].getPosition();
        String tempText = buttonArray[playerPos].getText();

        if (tempText.contains("P2") && (currentPlayer == 0))
        {
            if (tempText.contains("Foot"))
            {
                buttonArray[playerPos].setText(String.valueOf(P2Text) + "Foot");
                
            }
            else if (tempText.contains("Head"))
            {
                buttonArray[playerPos].setText(String.valueOf(P2Text) + "Head");
            }
            else
            {
                buttonArray[playerPos].setText(String.valueOf(P2Text));
            }
        }
        else if (tempText.contains("P1") && (currentPlayer == 1))
        {
           if (tempText.contains("Foot"))
            {
                buttonArray[playerPos].setText(String.valueOf(P1Text) + "Foot"); 
            }
            else if (tempText.contains("Head"))
            {
                buttonArray[playerPos].setText(String.valueOf(P1Text) + "Head");
            }
            else
            {
                buttonArray[playerPos].setText(String.valueOf(P1Text));
            } 
        }
        else if (tempText.contains("Foot"))
        {
            buttonArray[playerPos].setText("Foot"); 
        }
        else if (tempText.contains("Head"))
        {
            buttonArray[playerPos].setText("Head");
        }
        else
        {
            buttonArray[playerPos].setText(String.valueOf(playerPos+1));
        }

        int index;
        int newPos;
        playerPos += diceRoll;

        playerArray[currentPlayer].setPosition(playerPos);
        String tempText2 = "";
        if (playerPos < 100)
        {
            tempText2 = buttonArray[playerPos].getText();
        }
        if (currentPlayer == 0)
        {
            if (tempText2.contains("Foot"))
            { 
                //buttonArray[playerPos].setText(P1Text + "Foot");
                index = getMunroFoot(playerPos+1);
                newPos = munrosArray[index].getTip() - 1;
                playerArray[0].setPosition(newPos);
                if (buttonArray[newPos].getText().contains("P2"))
                {
                    buttonArray[newPos].setText(P1Text + P2Text);
                }
                else
                {
                    buttonArray[newPos].setText(P1Text);
                }
                buttonArray[newPos].setBackground(Color.LIGHT_GRAY);
            }
            else if (tempText2.contains("Head"))
            { 
                //buttonArray[playerPos].setText(P1Text + "Head");
                index = getSelkieIndex(playerPos+1);
                newPos = selkiesArray[index].getTail() - 1;
                playerArray[0].setPosition(newPos);
                if (buttonArray[newPos].getText().contains("P2"))
                {
                    buttonArray[newPos].setText(P1Text + P2Text);
                }
                else
                {
                    buttonArray[newPos].setText(P1Text);
                }
                buttonArray[newPos].setBackground(Color.PINK);
            }
            else if (tempText2.contains(P2Text))
            {
                buttonArray[playerPos].setText(P1Text + P2Text);
            }
            else if (playerPos >= 99)
            {
                buttonArray[99].setText(P1Text);
                playerWon();
            } 
            else if (playerPos == nessie.getSpace()-1)
            {
                nessieLanded();
            }
            else
            {
                buttonArray[playerPos].setText(P1Text);
            }
        }
        else if (currentPlayer == 1)
        {
           if (tempText2.contains("Foot"))
            { 
                //buttonArray[playerPos].setText(P2Text + "Foot");
                index = getMunroFoot(playerPos+1);
                newPos = munrosArray[index].getTip() - 1;
                playerArray[1].setPosition(newPos);
                if (buttonArray[newPos].getText().contains("P1"))
                {
                    buttonArray[newPos].setText(P1Text + P2Text);
                }
                else
                {
                    buttonArray[newPos].setText(P2Text);
                }
                buttonArray[newPos].setBackground(Color.LIGHT_GRAY);
            }
            else if (tempText2.contains("Head"))
            { 
                //buttonArray[playerPos].setText(P2Text + "Head");
                index = getSelkieIndex(playerPos+1);
                newPos = selkiesArray[index].getTail() - 1;
                playerArray[1].setPosition(newPos);
                if (buttonArray[newPos].getText().contains("P1"))
                {
                    buttonArray[newPos].setText(P1Text + P2Text);
                }
                else
                {
                    buttonArray[newPos].setText(P2Text);
                }
                buttonArray[newPos].setBackground(Color.PINK);
            }
            else if (tempText2.contains(P1Text))
            {
                buttonArray[playerPos].setText(P1Text + P2Text);
            }
            else if (playerPos >= 99)
            {
                buttonArray[99].setText(P2Text);
                playerWon();
            } 
            else if (playerPos == nessie.getSpace()-1)
            {
                nessieLanded();
            }
            else
            {
                buttonArray[playerPos].setText(P2Text);
            } 
        }

        if (currentPlayer == 0)
        {
            currentPlayer = 1;
        }
        else if (currentPlayer == 1)
        {
            currentPlayer = 0;
        }

        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rollDice");
        actionMap.put("rollDice", new AbstractAction() {
            public void actionPerformed(ActionEvent e)
            {
                RollDice();
            }
        });
    }

    /**
     * If a player reaches the final space, they win
     */
    private void playerWon()
    {
        JOptionPane.showMessageDialog(frame, ("Congratulations Player " + (currentPlayer+1) + " You Won!"), "Winner", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Get the index of selkie that the player has just landed on the head of
     * @param playerPos
     * @return index
     */
    private int getSelkieIndex(int playerPos)
    {
        for (int i=0; i < 7; i++)
        {
            if (selkiesArray[i].getHead() == playerPos)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the index of the munro that the player just landed on the foot of
     * @param playerPos
     * @return index
     */
    private int getMunroFoot(int playerPos)
    {
        for (int i=0; i < 7; i++)
        {   
            if (munrosArray[i].getFoot() == playerPos)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * If someone lands on the nessie space, randomise all player positions
     */
    private void nessieLanded()
    {
        System.out.println("LANDED ON NESSIE");

        if (currentPlayer == 0)
        {
            //clear player 2 from board
            int playerPos = playerArray[1].getPosition();
            String tempText = buttonArray[playerPos].getText();
        
            if (tempText.contains("Foot"))
            {
                buttonArray[playerPos].setText("Foot"); 
            }
            else if (tempText.contains("Head"))
            {
                buttonArray[playerPos].setText("Head");
            }
            else
            {
                buttonArray[playerPos].setText(String.valueOf(playerPos+1));
            }
        }
        else if (currentPlayer == 1)
        {
            //clear player 1 from board
            int playerPos = playerArray[2].getPosition();
            String tempText = buttonArray[playerPos].getText();
        
            if (tempText.contains("Foot"))
            {
                buttonArray[playerPos].setText("Foot"); 
            }
            else if (tempText.contains("Head"))
            {
                buttonArray[playerPos].setText("Head");
            }
            else
            {
                buttonArray[playerPos].setText(String.valueOf(playerPos+1));
            }
        }

        Random rand = new Random();
        int randomPos;
        int index;
        String tempText = "";
        int newPos;

        for (int i=0; i < 2; i++)
        {
            randomPos = rand.nextInt(2, 98);
            System.out.println("Player " + i + " Position: " + randomPos);

            playerArray[i].setPosition(randomPos);
            tempText = buttonArray[randomPos].getText();
            if (i == 0)
            {
                if (tempText.contains("Foot"))
                { 
                    //buttonArray[playerPos].setText(P1Text + "Foot");
                    index = getMunroFoot(randomPos+1);
                    newPos = munrosArray[index].getTip();
                    playerArray[0].setPosition(newPos);
                    buttonArray[newPos].setText(P1Text);
                    buttonArray[newPos].setBackground(Color.LIGHT_GRAY);
                }
                else if (tempText.contains("Head"))
                { 
                    //buttonArray[playerPos].setText(P1Text + "Head");
                    index = getSelkieIndex(randomPos+1);
                    newPos = selkiesArray[index].getTail();
                    playerArray[0].setPosition(newPos);
                    buttonArray[newPos].setText(P1Text);
                    buttonArray[newPos].setBackground(Color.PINK);
                }
                else if (tempText.contains(P2Text))
                {
                    buttonArray[randomPos].setText(P1Text + P2Text);
                }
                else if (randomPos == nessie.getSpace())
                {
                    nessieLanded();
                }
                else
                {
                    buttonArray[randomPos].setText(P1Text);
                }
            }
            else if (i == 1)
            {   
                if (tempText.contains("Foot"))
                { 
                    //buttonArray[playerPos].setText(P2Text + "Foot");
                    index = getMunroFoot(randomPos+1);
                    newPos = munrosArray[index].getTip();
                    playerArray[1].setPosition(newPos);
                    buttonArray[newPos].setText(P2Text);
                    buttonArray[newPos].setBackground(Color.LIGHT_GRAY);
                }
                else if (tempText.contains("Head"))
                { 
                    //buttonArray[playerPos].setText(P2Text + "Head");
                    index = getSelkieIndex(randomPos+1);
                    newPos = selkiesArray[index].getTail();
                    playerArray[1].setPosition(newPos);
                    buttonArray[newPos].setText(P2Text);

                    buttonArray[newPos].setBackground(Color.PINK);
                }
                else if (tempText.contains(P1Text))
                {
                    buttonArray[randomPos].setText(P1Text + P2Text);
                } 
                else if (randomPos == nessie.getSpace())
                {
                    nessieLanded();
                }
                else
                {
                    buttonArray[randomPos].setText(P2Text);
                } 
            }
        }    

        if (currentPlayer == 0)
        {
            currentPlayer = 1;
        }
        else if (currentPlayer == 1)
        {
            currentPlayer = 0;
        }
    }
}
