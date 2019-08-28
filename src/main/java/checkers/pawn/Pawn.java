package checkers.pawn;

public enum Pawn {
    PAWN, QUEEN;

    public boolean isPawn(){
        return this==PAWN;
    }
    public boolean isQueen(){
        return this==QUEEN;
    }
}
