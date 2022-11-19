public class CFourInfo {
    boolean hasTwoPlayers;
    boolean p1Win;
    boolean p2Win;
    boolean tie;
    int columnMove;
    int rowMove;
    boolean turn;//make it true if p1 turn and false if p2 turn;

    public CFourInfo(){
        hasTwoPlayers = false;
        p1Win = false;
        p2Win = false;
        tie = false;
        turn = true;
    }
}
