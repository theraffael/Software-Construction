package model;

public class Board {
    private Checker[][] board = new Checker[8][8];

    public Board(boolean standardSetup){
        if (standardSetup) {
            this.placeCheckersInitialSetup();
        }
    }

    public Board(BoardType boardType){
        if (boardType == BoardType.STANDARD) {
            this.placeCheckersInitialSetup();
        }
        else if (boardType == BoardType.TURKISH){
            this.placeCheckersTurkishSetup();
        }
        else if (boardType == BoardType.EMPTY){}
    }

    private void placeCheckersInitialSetup()
    {
        //this for loop is to set the red piece on top of the board in the arrangement of classic checkers

        for(int j = 0; j < 3; j++) {
            for (int i = 0; i < 8; i += 2) {
                addPiece(new Checker(PlayerColor.RED), i + (j + 1) % 2, j);
            }
        }

        //this for loop sets the white pieces on the bottom of the board in the arrangement of classic checkers
        for(int j = 5; j < 8; j++)
        {
            for(int i = 0; i < 8; i+=2)
            {
                addPiece(new Checker(PlayerColor.WHITE), i + (j+1)%2, j);
            }
        }
    }

    public void placeCheckersTurkishSetup()
    {
        //this for loop is to set the red piece on top of the board in the arrangement of classic checkers

        for(int j = 1; j <= 2; j++) {
            for (int i = 0; i < 8; i++) {
                addPiece(new Checker(PlayerColor.RED), i , j);
            }
        }

        //this for loop sets the white pieces on the bottom of the board in the arrangement of classic checkers
        for(int j = 5; j <= 6; j++)
        {
            for(int i = 0; i < 8; i++)
            {
                addPiece(new Checker(PlayerColor.WHITE), i , j);
            }
        }
    }

    public void checkAndCrown(){
        for (int i = 0; i < 8; i++){
            if (fieldContainsCheckerColor(i, 0, PlayerColor.WHITE)){
                this.board[i][0].crown();
            }
            if (fieldContainsCheckerColor(i, 7, PlayerColor.RED)){
                this.board[i][7].crown();
            }
        }
    }

    public void addPiece(Object checker, int x, int y)
    {
        board[x][y] = (Checker)checker;
    }

    public Checker removePiece(int x, int y)
    {
        Checker toRemove = board[x][y];
        board[x][y] = null;
        return toRemove;
    }

    public boolean fieldIsEmpty(int x, int y) {
        return this.board[x][y] == null;
    }

    public boolean fieldContainsKing(int x, int y) {
        // avoid error if field is empty
        if (fieldIsEmpty(x, y)){return false;}
        return this.board[x][y].isKing();
    }

    public boolean fieldContainsCheckerColor(int x, int y, PlayerColor color) {
        // avoid error if field is empty
        if (fieldIsEmpty(x, y)){return false;}
        return this.board[x][y].playerColor().equals(color);
    }

    public String getBoardString(){
        String boardString = "      a     b     c     d     e     f     g     h\n  +-------------------------------------------------+\n";

        for(int j = 0; j < 8; j++)
        {
            boardString = boardString + (j+1) +" | ";

            for(int i = 0; i < 8; i++)
            {
                if(board[i][j] == null)
                {
                    boardString = boardString + "[   ] ";
                }
                else {
                    boardString = boardString + "[" + board[i][j].toString()+ "] ";
                }
            }
            boardString = boardString + "| "+ (j+1) + "\n";
        }
        boardString = boardString + "  +-------------------------------------------------+\n      a     b     c     d     e     f     g     h\n";

        return boardString;
    }

    // Copy board state to new board object
    public Board(Board oldBoard) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (!oldBoard.fieldIsEmpty(x, y)) {
                    this.addPiece(new Checker(oldBoard.board[x][y]), x, y);
                }
            }
        }
    }
}


