package logic;
import model.Board;
import model.Player;
import model.Checker;

import java.util.ArrayList;
import java.util.List;

//todo: if it is a jump, then the checker of the rival player has to be removed
//todo: move has to be of length 1 (now it can move more than one field)

public class Game {
    private int turnCounter = 0;
    private Board board;
    private Player redPlayer;
    private Player whitePlayer;

    public boolean isValidMove() {
        return isValidMove;
    }

    public void setValidMove(boolean validMove) {
        isValidMove = validMove;
    }

    private boolean isValidMove;

    public Game(Board board, Player redPlayer, Player whitePlayer) {
        this.board = board;
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
    }

    //executes a move
    // todo: 13.10.2021: check if move is valid
    public void newMove(int fromX, int fromY, int toX, int toY){
        int x;
        int y;
        /*  checking for validity    */
        //isMove(fromX, fromY, toX, toY);
        //isJump(fromX, fromY, toX, toY);
        String moveType = checkIfSingleOrJump(fromX, fromY, toX, toY);
        if (moveType == "Single") {
            isValidMove = this.isMove(fromX, fromY, toX, toY);
            if (isValidMove) {
                Checker checker = board.removePiece(fromX, fromY);
                board.addPiece(checker, toX, toY);
                turnCounter++;
                board.display();
                System.out.println("Player Turn: " + this.getActivePlayer().getColorWord());
            }
            else{
                System.out.println("Move is invalid, please try again");
            }
        }
        // todo: check if jump possible
        else if(moveType == "Jump"){
            isValidMove = this.isSingleJump(fromX, fromY, toX, toY);
            if (isValidMove) {
                //add and remove moving checker
                Checker checker = board.removePiece(fromX, fromY);
                board.addPiece(checker, toX, toY);
                //remove captured checker

                if (fromX-toX < 0){
                    x = fromX+1;
                }
                else{
                    x = fromX - 1;
                }
                if (fromY-toY < 0){
                    y = fromY+1;
                }
                else{
                    y = fromY- 1;
                }

                board.removePiece(x, y);
                turnCounter++;
                board.display();
                System.out.println("Player Turn: " + this.getActivePlayer().getColorWord());
            }
            else{
                System.out.println("Move is invalid, please try again");
            }
        }

        else {
            System.out.println("Move is invalid, please try again");
        }
    }

    //checks if single move is legal
    public boolean isMove(int currentX, int currentY, int nextX, int nextY){
        // Coordinates of start square must be valid
        if ((currentX < 0) || (currentY < 0) || (currentX > 7) || (currentY > 7)){return false;}
        // Coordinates of target square must be valid
        if ((nextX < 0) || (nextY < 0) || (nextX > 7) || (nextY > 7)){return false;}

        // Start square cannot be empty
        if (board.getBoard()[currentX][currentY] == null) {return false;}
        // Target square must be empty
        if (board.getBoard()[nextX][nextY] != null) {return false;}

        if (getActivePlayer().getColor() == "R") {   //when it is red player's turn
            // Return false if checker belongs to white player
            if (board.getBoard()[currentX][currentY].getColor() == "W") {return false;}

            // if checker is pawn, nextY must be 1 larger than currentY
            int distanceY = nextY - currentY;
            // if checker is king, take absolute difference between nextY and currentY
            if (board.getBoard()[currentX][currentY].isKing()){
                distanceY = Math.abs(distanceY);
            }

            if (distanceY == 1 && (Math.abs(currentX - nextX) == 1 )){return true;}

            else{return false;}
        }

        else {   //when it is white player's turn
            // Return false if checker belongs to red player
            if (board.getBoard()[currentX][currentY].getColor() == "R") {return false;}

            // if checker is pawn, nextY must be 1 smaller than currentY
            int distanceY = currentY - nextY;
            // if checker is king, take absolute difference between nextY and currentY
            if (board.getBoard()[currentX][currentY].isKing()){
                distanceY = Math.abs(distanceY);
            }
            if (distanceY == 1 && (Math.abs(currentX - nextX) == 1 )){
                return true;
            }
            else{return false;}
        }
    }
    //checks if a move is a jump/capture => if at least one jump on a single turn is available, at least one jump has to be taken.
    public boolean isJump(int currentX, int currentY, int nextX, int nextY){
        return true;
    }

    public boolean isSingleJump(int currentX, int currentY, int nextX, int nextY){
        // Start square cannot be empty
        if (board.getBoard()[currentX][currentY] == null) {return false;}
        // Target square must be empty
        if (board.getBoard()[nextX][nextY] != null) {return false;}

        int distanceY = nextY - currentY;
        int distanceX = nextX - currentX;
        // X distance must be 2 or -2, check Y later
        if (Math.abs(distanceX) != 2 ) {return false;}

        if (getActivePlayer().getColor() == "R") {  //when it is red player's turn
        // Return false if checker belongs to white player
            if (board.getBoard()[currentX][currentY].getColor() == "W") {return false;}

            // Y distance must be 2, can be -2 if checker is king
            if (distanceY == 2 || (distanceY == -2 && board.getBoard()[currentX][currentY].isKing())){
                // there must be an opponents piece to jump over
                int opponentX = currentX + distanceX/2;
                int opponentY = currentY + distanceY/2;
                if (board.getBoard()[opponentX][opponentY] != null && board.getBoard()[opponentX][opponentY].getColor() == "W"){
                    return true;}
            }
        }

        else { //when it is white player's turn
            // Return false if checker belongs to red player
            if (board.getBoard()[currentX][currentY].getColor() == "R") {return false;}

            // X distance must be 2 or -2
            if (Math.abs(distanceX) != 2 ) {return false;}

            // Y distance must be -2, can be 2 if checker is king
            if (distanceY == -2 || (distanceY == 2 && board.getBoard()[currentX][currentY].isKing())){
                // there must be an opponents piece to jump over
                int opponentX = currentX + distanceX/2;
                int opponentY = currentY + distanceY/2;
                if (board.getBoard()[opponentX][opponentY] != null && board.getBoard()[opponentX][opponentY].getColor() == "R"){
                    return true;}
            }
        }
        // in all other cases, move is not legal
        return false;
    }

    public static String checkIfSingleOrJump(int fromX, int fromY, int toX, int toY){
        int x = Math.abs(fromX - toX);
        if (x == 1){
            return "Single";
        }
        if (x==2){
            return "Jump";
        }
        else{
            return "";
        }
    }

    //checks whether the game is finished
    public boolean isFinished(){
        ArrayList checkers = getActivePlayer().getCheckers();
        for (int i = 0; i < checkers.size(); i++ ) {
            Checker c = (Checker)checkers.get(i);
            // if at least one checker is not captured, the game is not over yet
            // todo: at least one checker has to be able to move
            if (!c.isCaptured()) {//todo: debug isCaptured()...
                return false;
            }
        }
        return true;
    }

    //returns the Player whose turn it is
    public Player getActivePlayer(){
        if (turnCounter % 2 == 0){
            return redPlayer;
        }
        else {
            return whitePlayer;
        }
    }

    //calculates all possible moves
    public void CalcPossibleMoves(){
        ArrayList checkers = getActivePlayer().getCheckers();
        for (int i = 0; i<checkers.size(); i++){
            Checker c = (Checker)checkers.get(i);
        }
    }

    public void setTurnCounter(int turnCounter) {
        this.turnCounter = turnCounter;
    }

    public void increaseTurnCounter() {
        this.turnCounter++;
    }

    public void newMove(ArrayList<List<Integer>> convertedMoves) {
        Board testBoard = new Board(board.getBoard());

        if (convertedMoves.size() == 1){
            List<Integer> move = convertedMoves.get(0);
            int fromX = move.get(0);
            int fromY = move.get(1);
            int toX = move.get(2);
            int toY = move.get(3);

            if (isMove(move)){
                // todo: check if no jump is possible anywhere
                isValidMove = true;
                // perform move
                Checker checker = testBoard.removePiece(fromX, fromY);
                testBoard.addPiece(checker, toX, toY);
            }
            else if (isSingleJump(move)){
                // perform single jump
                isValidMove = true;
                Checker checker = testBoard.removePiece(fromX, fromY);
                testBoard.addPiece(checker, toX, toY);

                int distanceX = toX - fromX;
                int distanceY = toY - fromY;

                Checker capturedChecker = testBoard.removePiece(fromX + distanceX/2, fromY + distanceY/2);
                capturedChecker.capture();

                // todo: check if no more jump is possible
            }
            else{
                isValidMove = false;
            }

        }

        else {
            for (int i = 0; i < convertedMoves.size(); i++){
                List<Integer> move = convertedMoves.get(i);
                int fromX = move.get(0);
                int fromY = move.get(1);
                int toX = move.get(2);
                int toY = move.get(3);
                if (!isSingleJump(move)) {
                    isValidMove = false;
                    break;}
                else{
                    isValidMove = true;
                    // perform jump, update testboard
                    Checker checker = testBoard.removePiece(fromX, fromY);
                    testBoard.addPiece(checker, toX, toY);
                    int distanceX = toX - fromX;
                    int distanceY = toY - fromY;
                    Checker capturedChecker = testBoard.removePiece(fromX + distanceX/2, fromY + distanceY/2);
                    capturedChecker.capture();
                }
            }
        }
        if (isValidMove){
            turnCounter++;
            board = testBoard;
            board.display();
            System.out.println("Player Turn: " + this.getActivePlayer().getColorWord());
        }


    }

    private boolean isSingleJump(List<Integer> integers) {
        return isSingleJump(integers.get(0), integers.get(1), integers.get(2), integers.get(3));
    }

    private boolean isMove(List<Integer> integers) {
        return isMove(integers.get(0), integers.get(1), integers.get(2), integers.get(3));
    }
}
