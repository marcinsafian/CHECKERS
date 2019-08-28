package checkers.board;

import checkers.core.AiLogic;
import checkers.core.BoardLogic;
import checkers.pawn.Pawn;
import checkers.pawn.PawnClass;
import checkers.pawn.PawnColor;
import checkers.pawn.PawnMove;
import javafx.scene.input.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Board {
    private static HashMap<Coordinates, PawnClass> board = new HashMap<>();

    private boolean isMark = false;
    private boolean newForce = false;
    private Coordinates markCoordinates;

    private Set<Coordinates> canGo = new HashSet<>();
    private Set<Coordinates> canForce = new HashSet<>();
    private Set<Coordinates> possibleTake = new HashSet<>();

    private boolean endGame = false;
    private boolean aiRound = false;

    private AiLogic computer = new AiLogic();

    public Board() {
        addPawn();
    }

    public static Map<Coordinates, PawnClass> getBoard() {
        return board;
    }

    private void addPawn() {
        board.put(new Coordinates(1, 0), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(3, 0), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(5, 0), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(7, 0), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(0, 1), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(2, 1), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(4, 1), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(6, 1), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(1, 2), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(3, 2), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(5, 2), new PawnClass(Pawn.PAWN, PawnColor.RED));
        board.put(new Coordinates(7, 2), new PawnClass(Pawn.PAWN, PawnColor.RED));

        board.put(new Coordinates(0, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(2, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(4, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(6, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(1, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(3, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(5, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(7, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(0, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(2, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(4, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(6, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            BoardLogic.addPawn(entry.getKey(), entry.getValue());
        }
    }

    public void readMouseEvent(MouseEvent event) {
        if(aiRound) {
            return;
        }

        Coordinates eventCoordinates = new Coordinates((int) ((event.getX()-5) / 85), (int) ((event.getY()-5) / 85));

        if(isMark) {
            if(markCoordinates.equals(eventCoordinates) && !newForce) {
                notShowMark(markCoordinates);

                markCoordinates = null;
                isMark = false;
            } else if(canGo.contains(eventCoordinates)) {

                notShowMark(markCoordinates);
                movePawn(markCoordinates, eventCoordinates);
                markCoordinates = null;
                isMark = false;

                aiMove();
            } else if(canForce.contains(eventCoordinates) && !isEmpty(eventCoordinates)) {

                notShowMark(markCoordinates);

                if(!forcePawn(markCoordinates, eventCoordinates)) {
                    isMark = false;
                    newForce = false;
                    aiMove();
                } else {
                    newForce = true;
                    markCoordinates = eventCoordinates;
                }
            }
        } else if(eventCoordinates.isCorrect()) {
            if(isEmpty(eventCoordinates)) {
                if(getPawn(eventCoordinates).getColor().isWhite() && isPawn(eventCoordinates, PawnColor.WHITE)) {
                    isMark = true;
                    markCoordinates = eventCoordinates;
                    showMark(eventCoordinates);
                }
            }
        }
    }

    private void aiMove() {

        aiRound = true;
        computer.getData();

        if(!newForce) {
            markCoordinates = computer.choosePawn();
        }

        showMark(markCoordinates);
    }

    private boolean isPawn(Coordinates coordinates, PawnColor color) {
        Set<Coordinates> bePawn = new HashSet<>();

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            if(entry.getValue().getColor() == color) {
                PawnMove pawnMoves = new PawnMove(entry.getKey(), entry.getValue());

                if(pawnMoves.getCanForce().size() > 0) {
                    bePawn.add(entry.getKey());
                }
            }
        }

        if(bePawn.size() == 0 || bePawn.contains(coordinates)) {
            return true;
        }

        return false;
    }

    private void movePawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);

        if(possibleTake.contains(newCoordinates)) {
            pawn = new PawnClass(Pawn.QUEEN, pawn.getColor());
        }

        BoardLogic.removePawn(oldCoordinates);
        BoardLogic.removePawn(newCoordinates);
        BoardLogic.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.put(newCoordinates, pawn);
    }

    private boolean forcePawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);

        if(possibleTake.contains(newCoordinates)) {
            pawn = new PawnClass(Pawn.QUEEN, pawn.getColor());
        }

        Coordinates enemyCoordinates = getVs(newCoordinates);

        BoardLogic.removePawn(oldCoordinates);
        BoardLogic.removePawn(enemyCoordinates);
        BoardLogic.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.remove(enemyCoordinates);
        board.put(newCoordinates, pawn);

        PawnMove pawnMove = new PawnMove(newCoordinates, pawn);

        if(pawnMove.getCanForce().size() > 0) {
            showNewForce(newCoordinates);
            return true;
        }

        return false;
    }

    private Coordinates getVs(Coordinates coordinates) {
        Coordinates checkUpLeft = new Coordinates(coordinates.getX() - 1, coordinates.getY() - 1);

        if(canForce.contains(checkUpLeft)) {
            return checkUpLeft;
        }

        Coordinates checkUpRight = new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1);

        if(canForce.contains(checkUpRight)) {
            return checkUpRight;
        }

        Coordinates checkBottomLeft = new Coordinates(coordinates.getX() - 1, coordinates.getY() + 1);

        if(canForce.contains(checkBottomLeft)) {
            return checkBottomLeft;
        }

        Coordinates checkBottomRight = new Coordinates(coordinates.getX() + 1, coordinates.getY() + 1);

        if(canForce.contains(checkBottomRight)) {
            return checkBottomRight;
        }

        return null;
    }

    private void showMark(Coordinates coordinates) {
        PawnMove pawnMove = new PawnMove(coordinates, getPawn(coordinates));

        canGo = pawnMove.getCanGo();
        canForce = pawnMove.getCanForce();
        possibleTake = pawnMove.getPossibleTake();

        if(canForce.size() > 0) {
            canGo.clear();
        }

        canGo.forEach(this::showGo);
        canForce.forEach(this::showGo);

        showPawn(coordinates);
    }

    private void showNewForce(Coordinates coordinates) {
        PawnMove pawnMove = new PawnMove(coordinates, getPawn(coordinates));

        canGo.clear();
        canForce = pawnMove.getCanForce();
        possibleTake = pawnMove.getPossibleTake();

        canForce.forEach(this::showGo);

        showPawn(coordinates);
    }

    private void showPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        BoardLogic.removePawn(coordinates);
        BoardLogic.addLightPawn(coordinates, pawn);
    }

    private void showGo(Coordinates coordinates) {
        BoardLogic.addLightMove(coordinates);
    }

    private void notShowMark(Coordinates coordinates) {
        canGo.forEach(this::notShowGo);
        canForce.forEach(this::notShowForce);

        notShowPawn(coordinates);
    }

    private void notShowPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        BoardLogic.removePawn(coordinates);
        BoardLogic.addPawn(coordinates, pawn);
    }

    private void notShowGo(Coordinates coordinates) {
        BoardLogic.removePawn(coordinates);
    }

    private void notShowForce(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);

        if(pawn != null) {
            notShowPawn(coordinates);
        } else {
            notShowGo(coordinates);
        }
    }

    public static boolean isEmpty(Coordinates coordinates) {
        return getPawn(coordinates) != null;
    }

    public static boolean theSame(Coordinates coordinates, PawnColor color) {
        return getPawn(coordinates).getColor() == color;
    }

    public static PawnClass getPawn(Coordinates coordinates) {
        return board.get(coordinates);
    }
}