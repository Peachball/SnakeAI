import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        int decision;
        Scanner in = new Scanner(System.in);
        Board board = new Board(20, 20);
        board.appleGenerator = true;
        board.speed = 50;
        System.out.println("Choose your game mode:");
        System.out.println("1:Normal player mode");
        System.out.println("2:Simple AI mode");
        System.out.println("3: Super Simple Greedy AI mode (OMFG IT WORKED)"); //These two are both attempts to
        System.out.println("4: Super Simple NOT GONNA DIE AI mode (Close enough)");// avoiding A*...
        System.out.println("5: Attempt at legit pathfinding..."); //dijkstras (accurate but suspiciously slow)
        System.out.println("6: A variant on AI5 (super dijkstras, also, WIP)"); //I don't really want to work on this...
        System.out.println("7: Attempt at A* pathfinding (and some other improvements)"); // I think it's A* at least...

        decision = Integer.parseInt(in.nextLine());
        switch (decision) {
            case 1:
                System.out.println("Enter your name:");
                String buffer = in.nextLine();
                board.player = buffer;
                normalMode(board);
                break;
            case 2:
                easyAI(board);
                break;
            case 3:
                GreedySearch(board);
                break;
            case 4:
                AI3(board);
                break;
            case 5:
                AI4(board);
                break;
            case 6:
                AI5(board);
                break;
            case 7:
                AI6(board);
                break;
        }
    }

    static boolean[][] deathRegions(Board board, int time) {
        boolean[][] deathRegions = new boolean[board.board.length][board.board[0].length];
        boolean done = false;
        Coord s;
        while (!done) {
            done = true;
            for (int counterX = 0; counterX < board.board[0].length; counterX++) {
                for (int counterY = 0; counterY < board.board.length; counterY++) {
                    if (deathRegions[counterY][counterX]) {
                        continue;
                    }
                    if (isSnake(board, new Coord(counterX, counterY), time)) {
                        deathRegions[counterY][counterX] = true;
                        done = false;
                        continue;
                    }
                    int sum = 0;
                    for (int counter = 1; counter < 5; counter++) {
                        s = newCoord(board, counter, new Coord(counterX, counterY));
                        if (isSnake(board, s, time) || deathRegions[s.y][s.x]) {
                            sum++;
                        }
                    }
                    if (sum >= 3) {
                        deathRegions[counterY][counterX] = true;
                        done = false;
                    }
                }
            }
        }
        return deathRegions;
    }

    //Check how large the closed space is...
    static int isClosed(Board board, Coord coord) {
        LinkedList<Coord> checked = new LinkedList<Coord>();
        LinkedList<Coord> unchecked = new LinkedList<Coord>();
        unchecked.add(coord);
        while (unchecked.size() != 0) {
            Collections.sort(checked, new CoordComparator());
            Collections.sort(unchecked, new CoordComparator());
            if (isSnakez(board, unchecked.get(0))) {
                unchecked.remove(0);
                continue;
            }
            if (Collections.binarySearch(checked, unchecked.get(0), new CoordComparator()) > 0) {
                unchecked.remove(0);
                continue;
            }
            for (int counter = 1; counter < 5; counter++) {
                if (!(Collections.binarySearch(checked, newCoord(board, counter, unchecked.get(0)), new CoordComparator()) > 0)) {
                    unchecked.add(newCoord(board, counter, unchecked.get(0)));
                }
            }
            checked.add(unchecked.get(0));
            unchecked.remove(0);
        }
        return checked.size();
    }

    //Literally the only use for this is for the isclosed function
    private static boolean isSnakez(Board board, Coord coord) {
        return board.board[coord.y][coord.x] > 0;
    }

    static void AI6(Board board) {
        board.nextIteration();
        while (true) {
            int direction;
            Coord snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            snake.distance = 0;
            snake.direction = directionReverser(board.snake1.snakeDirection);
            LinkedList<Integer> directions = new LinkedList<Integer>();
            Coord apple = new Coord(board.appleX, board.appleY);
            //Speed Settings
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                        break;
                    case 'g':
                        board.speed = 150;
                }
            }
            directions = AStar(board, snake, apple);
            //waste time here
            while (directions.size() == 0) {
                survive(board, board.snake1);
                directions = AStar(board, snake, apple);

            }
            snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            snake.distance = 0;
            snake.direction = directionReverser(board.snake1.snakeDirection);
            directions = AStar(board, snake, apple);
            //Run the fastest path
            while (directions.size() != 0) {
//                int appleSpace = isClosed(board, apple);
//                int snakeSpace = isClosed(board, new Coord(board.snake1.snakeX, board.snake1.snakeY));
//                if (appleSpace < snakeSpace) {
////                    break;
//                }
                board.setDirection(board.snake1, directionReverser(directions.get(directions.size() - 1)));
                directions.remove(directions.size() - 1);
                board.nextIteration();
            }
        }
    }

    static void survive(Board board, Snake asdf) {
        boolean[][] deathRegions = new boolean[board.board.length][board.board[0].length];
        boolean done = false;
        Coord s;
        while (!done) {
            done = true;
            for (int counterX = 0; counterX < board.board[0].length; counterX++) {
                for (int counterY = 0; counterY < board.board.length; counterY++) {
                    if (deathRegions[counterY][counterX]) {
                        continue;
                    }
                    if (isSnake(board, new Coord(counterX, counterY))) {
                        deathRegions[counterY][counterX] = true;
                        done = false;
                        continue;
                    }
                    int sum = 0;
                    for (int counter = 1; counter < 5; counter++) {
                        s = newCoord(board, counter, new Coord(counterX, counterY));
                        if (isSnake(board, s) || deathRegions[s.y][s.x]) {
                            sum++;
                        }
                    }
                    if (sum >= 3) {
                        deathRegions[counterY][counterX] = true;
                        done = false;
                    }
                }
            }
        }
        Coord snake = new Coord(asdf.snakeX, asdf.snakeY);
        snake.distance = 0;
        snake.direction = directionReverser(board.snake1.snakeDirection);
        int direction = board.snake1.snakeDirection;

        int safestDirection = 1;
        int largestArea = 0;
        for (int counter = 1; counter < 5; counter++) {
            if (isClosed(board, newCoord(board, counter, snake)) > largestArea) {
                safestDirection = counter;
                largestArea = isClosed(board, newCoord(board, counter, snake));
            }
        }
        if (largestArea == 0) {
            if (!isSnake(board, newCoord(board, turnRight(direction), snake))
                    && !deathRegions[newCoord(board, turnRight(direction), snake).y][newCoord(board, turnRight(direction), snake).x]) {
                safestDirection = turnRight(direction);
            } else if (!isSnake(board, newCoord(board, direction, snake))
                    && !deathRegions[newCoord(board, direction, snake).y][newCoord(board, direction, snake).x]) {
                safestDirection = direction;
            } else if (!isSnake(board, newCoord(board, turnLeft(direction), snake))
                    && !deathRegions[newCoord(board, turnLeft(direction), snake).y][newCoord(board, turnLeft(direction), snake).x]) {
                safestDirection = turnLeft(direction);
            } else if (!isSnake(board, newCoord(board, turnRight(direction), snake))) {
                safestDirection = turnRight(direction);
            } else if (!isSnake(board, newCoord(board, direction, snake))) {
                safestDirection = direction;
            } else {
                safestDirection = turnLeft(direction);
            }
        }
        board.setDirection(asdf, safestDirection);

        board.nextIteration();
    }

    static void AI5(Board board) {
        board.nextIteration();
        while (true) {
            Coord snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            snake.distance = 0;
            snake.direction = directionReverser(board.snake1.snakeDirection);
            LinkedList<Coord> read = new LinkedList<Coord>(); //read like in red (not reed)
            LinkedList<Coord> buffer = new LinkedList<Coord>();
            LinkedList<Integer> directions = new LinkedList<Integer>();
            Coord apple = new Coord(board.appleX, board.appleY);
            buffer.add(snake);
            //Speed Settings
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                        break;
                    case 'g':
                        board.speed = 150;
                }
            }
            int shortestDistance = (int) shortestPath(board, snake, apple);
            //Figure out the distances of each node to the origin through dijkstras
            while (Collections.binarySearch(read, apple, new CoordComparator()) < 0) {
                if (buffer.size() == 0) {
                    board.endGame(false);
                }
                Collections.sort(buffer, new AStarComparator());
                for (int counter = 1; counter < 5; counter++) {

                    //Check to make sure that this isn't the same direction as where the buffer came from
                    if (buffer.get(0).direction == counter) {
                        continue;
                    }
                    Collections.sort(read, new CoordComparator());

                    Coord bufferedCoord = newCoord(board, counter, buffer.get(0));

                    //Direction points to the parent coord, not the children node
                    bufferedCoord.direction = directionReverser(counter);
                    bufferedCoord.distance = buffer.get(0).distance + 1;
                    bufferedCoord.parent = buffer.get(0);
                    bufferedCoord.shortestDistance = shortestDistance;

                    //Check if the place is a snake or not...
                    if (isSnake(board, bufferedCoord, bufferedCoord.distance)) {
                        continue;
                    }
                    buffer.add(bufferedCoord);
                    read.add(bufferedCoord);

                }
                buffer.remove(0);
                Collections.sort(read, new CoordComparator());
            }

            //Figure out the fastest path...
            Coord currentLocation = apple;
            currentLocation = read.get(Collections.binarySearch(read, currentLocation, new CoordComparator()));
            while (!currentLocation.equals(snake)) {
                directions.add(newDirection(board, currentLocation, currentLocation.parent));
                currentLocation = currentLocation.parent;
            }

            //Run the fastest path
            while (directions.size() != 0) {
                board.setDirection(board.snake1, directionReverser(directions.get(directions.size() - 1)));
                directions.remove(directions.size() - 1);
                board.nextIteration();
            }
        }
    }

    static void AI4(Board board) {
        board.nextIteration();
        while (true) {
            int direction;
            Coord snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            snake.distance = 0;
            snake.direction = directionReverser(board.snake1.snakeDirection);
            LinkedList<Integer> directions = new LinkedList<Integer>();
            Coord apple = new Coord(board.appleX, board.appleY);
            //Speed Settings
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                        break;
                    case 'g':
                        board.speed = 150;
                }
            }

            directions = dijkstras(board, snake, apple);

            //waste time here
            while (directions.size() == 0) {
                boolean[][] deathRegions = new boolean[board.board.length][board.board[0].length];
                boolean done = false;
                Coord s;
                while (!done) {
                    done = true;
                    for (int counterX = 0; counterX < board.board[0].length; counterX++) {
                        for (int counterY = 0; counterY < board.board.length; counterY++) {
                            if (deathRegions[counterY][counterX]) {
                                continue;
                            }
                            if (isSnake(board, new Coord(counterX, counterY))) {
                                deathRegions[counterY][counterX] = true;
                                done = false;
                                continue;
                            }
                            int sum = 0;
                            for (int counter = 1; counter < 5; counter++) {
                                s = newCoord(board, counter, new Coord(counterX, counterY));
                                if (isSnake(board, s) || deathRegions[s.y][s.x]) {
                                    sum++;
                                }
                            }
                            if (sum >= 3) {
                                deathRegions[counterY][counterX] = true;
                                done = false;
                            }
                        }
                    }
                }
                snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
                snake.distance = 0;
                snake.direction = directionReverser(board.snake1.snakeDirection);
                direction = board.snake1.snakeDirection;
                int safestDirection = 1;
                int largestArea = 0;
                for (int counter = 1; counter < 5; counter++) {
                    if (isClosed(board, newCoord(board, counter, snake)) > largestArea) {
                        safestDirection = counter;
                        largestArea = isClosed(board, newCoord(board, counter, snake));
                    }
                }
                if (largestArea == 0) {
                    if (!isSnake(board, newCoord(board, turnRight(direction), snake))
                            && !deathRegions[newCoord(board, turnRight(direction), snake).y][newCoord(board, turnRight(direction), snake).x]) {
                        safestDirection = turnRight(direction);
                    } else if (!isSnake(board, newCoord(board, direction, snake))
                            && !deathRegions[newCoord(board, direction, snake).y][newCoord(board, direction, snake).x]) {
                        safestDirection = direction;
                    } else if (!isSnake(board, newCoord(board, turnLeft(direction), snake))
                            && !deathRegions[newCoord(board, turnLeft(direction), snake).y][newCoord(board, turnLeft(direction), snake).x]) {
                        safestDirection = turnLeft(direction);
                    } else if (!isSnake(board, newCoord(board, turnRight(direction), snake))) {
                        safestDirection = turnRight(direction);
                    } else if (!isSnake(board, newCoord(board, direction, snake))) {
                        safestDirection = direction;
                    } else {
                        safestDirection = turnLeft(direction);
                    }
                }
                board.setDirection(board.snake1, safestDirection);

                board.nextIteration();
                directions = dijkstras(board, snake, apple);

            }
            snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            snake.distance = 0;
            snake.direction = directionReverser(board.snake1.snakeDirection);
            directions = dijkstras(board, snake, apple);
            //Run the fastest path
            while (directions.size() != 0) {
                board.setDirection(board.snake1, directionReverser(directions.get(directions.size() - 1)));
                directions.remove(directions.size() - 1);
                board.nextIteration();
            }
        }
    }

    static LinkedList<Integer> AStar(Board board, Coord start, Coord end) {
        LinkedList<Integer> directions = new LinkedList<Integer>();
        LinkedList<Coord> read = new LinkedList<Coord>();
        LinkedList<Coord> buffer = new LinkedList<Coord>();
        start.distanceToGoal = (int) shortestPath(board, start, end);
        buffer.add(start);
        int shortestDistance = (int) shortestPath(board, start, end);
//        boolean[][] deathRegions = new boolean[board.board.length][board.board[0].length];
//        boolean done = false;
//        Coord s;
//        while (!done) {
//            done = true;
//            for (int counterX = 0; counterX < board.board[0].length; counterX++) {
//                for (int counterY = 0; counterY < board.board.length; counterY++) {
//                    if (deathRegions[counterY][counterX]) {
//                        continue;
//                    }
//                    if (isSnake(board, new Coord(counterX, counterY))) {
//                        deathRegions[counterY][counterX] = true;
//                        done = false;
//                        continue;
//                    }
//                    int sum = 0;
//                    for (int counter = 1; counter < 5; counter++) {
//                        s = newCoord(board, counter, new Coord(counterX, counterY));
//                        if (isSnake(board, s) || deathRegions[s.y][s.x]) {
//                            sum++;
//                        }
//                    }
//                    if (sum >= 3) {
//                        deathRegions[counterY][counterX] = true;
//                        done = false;
//                    }
//                }
//            }
//        }
        //Figure out the distances of each node to the origin through dijkstras
        while (Collections.binarySearch(read, end, new CoordComparator()) < 0) {
            if (buffer.size() == 0) {
                return directions;
            }

            //Pick the node closest to the end...
            Collections.sort(buffer, new AStarComparator());
            for (int counter = 1; counter < 5; counter++) {

                //Check to make sure that this isn't the same direction as where the buffer came from
                if (buffer.get(0).direction == counter) {
                    continue;
                }
                Collections.sort(read, new CoordComparator());

                Coord bufferedCoord = newCoord(board, counter, buffer.get(0));

                //Direction points to the parent coord, not the children node
                bufferedCoord.direction = directionReverser(counter);
                bufferedCoord.distance = buffer.get(0).distance + 1;
                bufferedCoord.distanceToGoal = (int) shortestPath(board, bufferedCoord, end);
                bufferedCoord.parent = buffer.get(0);
                bufferedCoord.shortestDistance = shortestDistance;

                //Check if the place is a snake or not...
                if (isSnake(board, bufferedCoord, bufferedCoord.distance) /*|| deathRegions[bufferedCoord.y][bufferedCoord.x]*/) {
                    continue;
                }
                int bufferedPosition = Collections.binarySearch(read, bufferedCoord, new CoordComparator());
                if (bufferedPosition < 0) {
                    buffer.add(bufferedCoord);
                    read.add(bufferedCoord);
                } else {
                    if (read.get(bufferedPosition).distance
                            > bufferedCoord.distance) {
                        read.set(bufferedPosition, bufferedCoord);
                    }
                }

            }
            buffer.remove(0);
            Collections.sort(read, new CoordComparator());
        }

        //Figure out the fastest path...
        Coord currentLocation = end;
        currentLocation = read.get(Collections.binarySearch(read, currentLocation, new CoordComparator()));
        while (!currentLocation.equals(start)) {
            directions.add(newDirection(board, currentLocation, currentLocation.parent));
            currentLocation = currentLocation.parent;
        }
        return directions;
    }

    static LinkedList<Integer> dijkstras(Board board, Coord start, Coord end) {
        LinkedList<Integer> directions = new LinkedList<Integer>();
        LinkedList<Coord> read = new LinkedList<Coord>();
        LinkedList<Coord> buffer = new LinkedList<Coord>();
        buffer.add(start);
//        boolean[][] deathRegions = new boolean[board.board.length][board.board[0].length];
//        boolean done = false;
//        Coord s;
//        while (!done) {
//            done = true;
//            for (int counterX = 0; counterX < board.board[0].length; counterX++) {
//                for (int counterY = 0; counterY < board.board.length; counterY++) {
//                    if (deathRegions[counterY][counterX]) {
//                        continue;
//                    }
//                    if (isSnake(board, new Coord(counterX, counterY))) {
//                        deathRegions[counterY][counterX] = true;
//                        done = false;
//                        continue;
//                    }
//                    int sum = 0;
//                    for (int counter = 1; counter < 5; counter++) {
//                        s = newCoord(board, counter, new Coord(counterX, counterY));
//                        if (isSnake(board, s) || deathRegions[s.y][s.x]) {
//                            sum++;
//                        }
//                    }
//                    if (sum >= 3) {
//                        deathRegions[counterY][counterX] = true;
//                        done = false;
//                    }
//                }
//            }
//        }

        //Figure out the distances of each node to the origin through dijkstras
        while (Collections.binarySearch(read, end, new CoordComparator()) < 0) {
            for (int counter = 1; counter < 5; counter++) {
                if (buffer.size() == 0) {
                    return directions;
                }
                //Check to make sure that this isn't the same direction as where the buffer came from
                if (buffer.get(0).direction == counter) {
                    continue;
                }
                Collections.sort(read, new CoordComparator());

                Coord bufferedCoord = newCoord(board, counter, buffer.get(0));

                //Direction points to the parent coord, not the children node
                bufferedCoord.direction = directionReverser(counter);
                bufferedCoord.distance = buffer.get(0).distance + 1;
                bufferedCoord.parent = buffer.get(0);

                //Check if the place is a snake or not...
                if (isSnake(board, bufferedCoord, bufferedCoord.distance) /*|| deathRegions[bufferedCoord.y][bufferedCoord.x]*/) {
                    continue;
                }
                int bufferedPosition = Collections.binarySearch(read, bufferedCoord, new CoordComparator());
                if (bufferedPosition < 0) {
                    buffer.add(bufferedCoord);
                    read.add(bufferedCoord);
                } else {
                    if (read.get(bufferedPosition).distance
                            > bufferedCoord.distance) {
                        read.set(bufferedPosition, bufferedCoord);
                    }
                }

            }
            buffer.remove(0);
            Collections.sort(read, new CoordComparator());
        }

        //Figure out the fastest path...
        Coord currentLocation = end;
        currentLocation = read.get(Collections.binarySearch(read, currentLocation, new CoordComparator()));
        while (!currentLocation.equals(start)) {
            directions.add(newDirection(board, currentLocation, currentLocation.parent));
            currentLocation = currentLocation.parent;
        }
        return directions;
    }

    static void AI3(Board board) {
        while (true) {
            //Some helpful things to have...
            boolean[][] deathRegions = new boolean[board.board.length][board.board[0].length];
            Coord snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            Coord buffer;
            Coord apple = new Coord(board.appleX, board.appleY);

            //Speed Settings
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                        break;
                    case 'g':
                        board.speed = 150;
                }
            }

            //Mark the death regions
            boolean done = false;
            while (!done) {
                done = true;
                for (int counterX = 0; counterX < board.board[0].length; counterX++) {
                    for (int counterY = 0; counterY < board.board.length; counterY++) {
                        if (deathRegions[counterY][counterX]) {
                            continue;
                        }
                        if (isSnake(board, new Coord(counterX, counterY))) {
                            deathRegions[counterY][counterX] = true;
                            done = false;
                            continue;
                        }
                        int sum = 0;
                        for (int counter = 1; counter < 5; counter++) {
                            buffer = newCoord(board, counter, new Coord(counterX, counterY));
                            if (isSnake(board, buffer) || deathRegions[buffer.y][buffer.x]) {
                                sum++;
                            }
                        }
                        if (sum >= 3) {
                            deathRegions[counterY][counterX] = true;
                            done = false;
                        }
                    }
                }
            }

            //Super Greedy Search
            int shortestDistance = 10000;
            int shortestDirection = 1;
            for (int counter = 1; counter < 5; counter++) {

                buffer = newCoord(board, counter, snake);
                if (deathRegions[buffer.y][buffer.x]) {
                    continue;
                }
                if (shortestDistance > shortestPath(board, apple, buffer)) {
                    shortestDirection = counter;
                    shortestDistance = (int) shortestPath(board, apple, buffer);
                }
            }
            board.setDirection(board.snake1, shortestDirection);
            board.nextIteration();
        }
    }

    static void easyAI(Board board) {
        board.speed = 25;
        board.setDirection(board.snake1, 2);
        while (true) {
            board.nextIteration();
            if (board.snake1.snakeX == board.board[0].length - 1) {
                board.setDirection(board.snake1, 3);
                board.nextIteration();
                board.setDirection(board.snake1, 4);
            }
            if (board.snake1.snakeX == 0) {
                board.setDirection(board.snake1, 3);
                board.nextIteration();
                board.setDirection(board.snake1, 2);
            }
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                    case 'g':
                        board.speed = 150;
                }
            }
        }
    }

    static double shortestPath(Board board, int x, int y) {
        return Math.abs(board.snake1.snakeX - x) + Math.abs(board.snake1.snakeY - y);
    }

    static double shortestPath(Board board, Coord location1, Coord location2) {
        int diff1;
        int diff2;
        if (Math.abs(location2.x - location1.x) < board.board[0].length - 1 - Math.abs(location1.x - location2.x)) {
            diff1 = Math.abs(location2.x - location1.x);
        } else {
            diff1 = board.board[0].length - 1 - Math.abs(location1.x - location2.x);
        }
        if (Math.abs(location2.y - location1.y) < board.board.length - 1 - Math.abs(location1.y - location2.y)) {
            diff2 = Math.abs(location2.y - location1.y);
        } else {
            diff2 = board.board.length - 1 - Math.abs(location1.y - location2.y);
        }
        return diff1 + diff2;
    }

    static void normalMode(Board board) {
        while (true) {
            board.nextIteration();
            if (StdDraw.hasNextKeyTyped()) {
                board.setDirection(board.snake1, keyboardListener());
            }
        }
    }

    static void GreedySearch(Board board) {
        int[][] distances = new int[board.board.length][board.board[0].length];
        ArrayList<Coord> checked = new ArrayList<Coord>();
        ArrayList<Coord> nextChecked = new ArrayList<Coord>();
        board.nextIteration();
        while (true) {
            Coord buffer;
            Coord snake;
            Coord apple;
            apple = new Coord(board.appleX, board.appleY);
            snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            //Speed Settings
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                        break;
                    case 'g':
                        board.speed = 150;
                }
            }
            //Simplest solution(hopefully)
            int shortestDistance = 10000;
            int shortestDirection = 1;
            for (int counter = 1; counter < 5; counter++) {

                buffer = newCoord(board, counter, snake);
                if (isSnake(board, buffer)) {
                    continue;
                }
                if (shortestDistance > shortestPath(board, apple, buffer)) {
                    shortestDirection = counter;
                    shortestDistance = (int) shortestPath(board, apple, buffer);
                }
            }
            board.setDirection(board.snake1, shortestDirection);
            board.nextIteration();

        }

    }

    static int newDirection(Board board, Coord start, Coord end) {
        if (start.x != end.x) {
            if (start.x - end.x == -1 || start.x - end.x == board.board[0].length - 1) {
                return 2;
            }
            if (start.x - end.x == 1 || start.x - end.x == -1 * board.board[0].length + 1) {
                return 4;
            }

        } else if (start.x == end.x) {
            if (start.y - end.y == 1 || start.y - end.y == -1 * board.board.length + 1) {
                return 3;
            }
            if (start.y - end.y == -1 || start.y - end.y == board.board.length - 1) {
                return 1;
            }
        }
        return -1;
    }

    static boolean isSnake(Board board, int x, int y) {
        return board.board[y][x] > 0;
    }

    static boolean isSnake(Board board, Coord coord) {
        if (coord.y == board.snake1.snakeY && coord.x == board.snake1.snakeX) {
            return false;
        }
        if (coord.equals(board.tail)) {
            return false;
        }
        return board.board[coord.y][coord.x] > 0;
    }

    static boolean isSnake(Board board, Coord coord, int time) {
        if (coord.y == board.snake1.snakeY && coord.x == board.snake1.snakeX) {
            return false;
        }
        if (coord.equals(board.tail)) {
            return false;
        }
        return board.board[coord.y][coord.x] - time > 0;
    }
    /*
     * Directionally based, not for the distance
     */

    //NOTE TO SELF: YOU CAN CHANGE THE GIVEN VALUES...
    //E.G. YOU CAN CHANGE THE INITIAL VALUE OF SAY THE DIRECTION...
    //FML...
    static Coord newCoord(Board board, int direction, Coord coord) {
        Coord buffer = new Coord(0, 0);
        buffer.x = coord.x;
        buffer.y = coord.y;
        switch (direction) {
            case 1:
                buffer.y++;   //1 up
                if (buffer.y >= board.board.length) {
                    buffer.y = board.board.length - buffer.y;
                }
                break;
            case 2:
                buffer.x++;   //2 right
                if (buffer.x >= board.board[0].length) {
                    buffer.x = board.board[0].length - buffer.x;
                }
                break;
            case 3:
                buffer.y--;   //3 down
                if (buffer.y < 0) {
                    buffer.y = board.board.length + buffer.y;
                }
                break;
            case 4:
                buffer.x--;   //4 left
                if (buffer.x < 0) {
                    buffer.x = board.board[0].length + buffer.x;
                }
                break;
        }
        return buffer;
    }

    static Coord newCoord(Board board, int direction, int x, int y) {
        int bufferx = x;
        int buffery = y;
        switch (direction) {
            case 1:
                buffery++;   //1 up
                if (buffery >= board.board.length) {
                    buffery = board.board.length - buffery;
                }
                break;
            case 2:
                bufferx++;   //2 right
                if (bufferx >= board.board[0].length) {
                    bufferx = board.board[0].length - bufferx;
                }
                break;
            case 3:
                buffery--;   //3 down
                if (y < 0) {
                    buffery = board.board.length + buffery;
                }
                break;
            case 4:
                bufferx--;   //4 left
                if (x < 0) {
                    bufferx = board.board[0].length + bufferx;
                }
                break;
        }
        return new Coord(bufferx, buffery);
    }

    static int keyboardListener() {
        int direction = 1;
        switch (StdDraw.nextKeyTyped()) {
            case 'a':
                direction = 4;
                break;
            case 's':
                direction = 3;
                break;
            case 'd':
                direction = 2;
                break;
            case 'w':
                direction = 1;
                break;
            case 'q':
                direction = 0;
                break;
            case 'p':
                direction = 5;
                break;
        }
        return direction;
    }

    static int directionReverser(int direction) {
        int newDirection = direction - 2;
        if (newDirection < 1) {
            return newDirection + 4;
        }
        return newDirection;
    }

    static int turnRight(int direction) {
        int newDirection = direction + 1;
        if (newDirection > 4) {
            return newDirection - 1;
        }
        return newDirection;
    }

    static int turnLeft(int direction) {
        int newDirection = direction - 1;
        if (newDirection < 1) {
            return newDirection + 4;
        }
        return newDirection;
    }
}

class Coord {

    public int x;
    public int y;
    public int direction;
    public int distance;
    public Coord parent;
    public LinkedList<Coord> snake;
    public int distanceToGoal;
    public int shortestDistance;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
        direction = -1;
        distance = -1;
        parent = null;
        snake = new LinkedList<Coord>();
        distanceToGoal = -1;
        shortestDistance = 0;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean equals(Coord coord2) {
        if (x == coord2.x && y == coord2.y) {
            return true;
        }
        return false;
    }
}

class CoordComparator implements Comparator<Coord> {

    @Override
    public int compare(Coord t, Coord t1) {
        if (t.x != t1.x) {
            return t.x - t1.x;
        } else {
            return t.y - t1.y;
        }
    }

}

class AStarComparator implements Comparator<Coord> {

    @Override
    public int compare(Coord t, Coord t1) {
        if (t.distanceToGoal == -1 || t1.distanceToGoal == -1
                || t.distance == -1 || t1.distance == -1) {
            return 0;
        }
//        if (Math.abs(t.distanceToGoal + t.distance - t.shortestDistance) - Math.abs(t1.distance + t1.distanceToGoal - t.shortestDistance) != 0) {
//            return t.direction - t1.direction;
//        }
        return Math.abs(t.distanceToGoal + t.distance - t.shortestDistance) - Math.abs(t1.distance + t1.distanceToGoal - t.shortestDistance);
    }

}

class GreedySearch implements Comparator<Coord> {

    @Override
    public int compare(Coord t, Coord t1) {
        return t.distanceToGoal - t1.distanceToGoal;
    }

}
