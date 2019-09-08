package checkers.pawn;

public enum PawnColor {
    WHITE,RED;

    public boolean isWhite(){
        return this==WHITE;
    }
    public boolean isRed(){
        return this==RED;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
