import logic.*;
import model.*;

import java.util.Locale;
import java.util.regex.*;
import java.util.Scanner;

public class CheckerGame {

    public static Board board = new Board();

    public static Player redPlayer = new Player("R");
    public static Player whitePlayer = new Player("W");
    public static Scanner keyBoard = new Scanner(System.in);

    public static void main(String[] args) {
        setBoard();
        runGame();
    }

    public static void runGame(){
        Game game = new Game(board, redPlayer, whitePlayer);
        while (!game.isFinished()){
            boolean isInputCorrect = false;
            String move = null;
            board.display();
            System.out.println("Player Turn: "+ game.getActivePlayer().getColorWord());
            while(!isInputCorrect){
                System.out.println("Please enter coordinate format in form of [a1]x[b2]");
                move = keyBoard.nextLine();
                isInputCorrect = checkInputIsValid(move);
                if (isInputCorrect){
                    break;
                }
                else{
                    System.out.println("Coordinate format is incorrect, please try again");
                }
            }
            String convertedMove = convertInputToXY(move);
            // move red pawn at D3 to E4, no logic implemented yet
            game.newMove(convertedMove.charAt(0),convertedMove.charAt(1),convertedMove.charAt(2),convertedMove.charAt(3));
        }
    }

    public static String convertInputToXY(String s){
        s = s.replaceAll("[\\[x\\]]","").toLowerCase(Locale.ROOT);
        String newstring = "";
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isAlphabetic(ch)) {
                int pos = ch - 'a' + 1;
                newstring = newstring + pos;
            } else {
                newstring = newstring + ch;
            }
        }
        return newstring;
    }
    public static boolean checkInputIsValid(String input){
        Pattern p = Pattern.compile("\\[[a-z][1-8]\\]x\\[[a-z][1-8]\\]");
        String lowercase = input.toLowerCase(Locale.ROOT);
        Matcher m = p.matcher(lowercase);
        return m.matches();
    }

    public static void setBoard()
    {
        int k = 0;
        //this for loop is to set the red piece on top of the board in the arrangement of classic checkers

        for(int j = 0; j < 3; j++)
        {
            for(int i = 0; i < 8; i+=2)
            {
                board.addPiece(redPlayer.getCheckers().get(k), i + (j+1)%2, j);
                redPlayer.setCheckerPosition(i+(j+1)%2,j);
                k++;
            }
        }
        k=0;
        //this for loop sets the white pieces on the bottom of the board in the arrangement of classic checkers
        for(int j = 5; j < 8; j++)
        {
            for(int i = 0; i < 8; i+=2)
            {
                board.addPiece(whitePlayer.getCheckers().get(k), i + (j+1)%2, j);
                whitePlayer.setCheckerPosition(i+(j+1)%2,j);
                k++;
            }
        }
        k=0;

    }
}
