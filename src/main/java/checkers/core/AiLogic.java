package checkers.core;

import checkers.board.Board;
import checkers.board.Coordinates;
import checkers.pawn.PawnClass;
import checkers.pawn.PawnMove;

import java.util.*;
import java.util.stream.Collectors;

public class AiLogic {
    private Random random = new Random();

    private Set<Coordinates> canForce = new HashSet<>();
    private Set<Coordinates> canGo = new HashSet<>();
    private boolean beForceGo = false;

    public void getData() {
        Map<Coordinates, PawnClass> cacheBoard = new HashMap<>(Board.getBoard());

        canGo.clear();
        canForce.clear();
        beForceGo = false;

        for (Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            if (entry.getValue().getColor().isRed()) {
                PawnMove moves = new PawnMove(entry.getKey(), entry.getValue());

                if(moves.getCanGo().size() > 0) {
                    canGo.add(entry.getKey());
                }

                if(moves.getCanForce().size() > 0) {
                    canForce.add(entry.getKey());
                }
            }
        }


    }

    public Coordinates choosePawn() {
        if(canForce.size() > 0) {
            return selectRandom(canForce);
        } else {
            return selectRandom(canGo);
        }
    }


    public Coordinates chooseMove(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMove moves = new PawnMove(coordinates, pawn);

        Set<Coordinates> kickList = moves.getCanForce();
        Set<Coordinates> moveList = moves.getCanGo();

        if(kickList.size() > 0) {
            beForceGo = true;

            return selectRandom(kickList.stream()
                    .filter(entry -> Board.getPawn(entry) == null)
                    .collect(Collectors.toSet()));
        } else {
            return selectRandom(moveList);
        }
    }

    private Coordinates selectRandom(Set<Coordinates> list) {
        Object[] object = list.toArray();
        return (Coordinates) object[random.nextInt(object.length)];
    }

    public boolean beForceGo() {
        return beForceGo;
    }
}
