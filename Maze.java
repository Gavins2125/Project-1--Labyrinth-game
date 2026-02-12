

import java.util.Random;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class Maze {
    private final int width, height;
    private final Cell[][] grid;
    private final Random rand = new Random();
    private Cell start, end;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Cell[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                grid[y][x] = new Cell(x, y);
    }

    public void generate() {
        start = grid[height - 1][rand.nextInt(width)];
        start.isStart = true;
        generateMazeDFS(start);
        calculateDistances(start);
        end = findFarthestCell();
        end.isEnd = true;
        makeDeadEnd(end);
        assignRoomTypes(); // default normal mode
    }

    /** Default assignRoomTypes call */
    public void assignRoomTypes() {
        assignRoomTypes(false); // normal mode
    }

    /** Overloaded method to handle Loot Mode */
    public void assignRoomTypes(boolean lootMode) {
        int maxDistance = 0;
        for (Cell[] row : grid)
            for (Cell c : row)
                if (c.distance > maxDistance) maxDistance = c.distance;

        for (Cell[] row : grid) {
            for (Cell c : row) {
                if (c.isStart) {
                    c.roomType = RoomType.BLANK;
                    continue;
                }

                double bossChance = 0.05 + 0.45 * ((double)c.distance / maxDistance);
                double roll = rand.nextDouble();

                if (c.distance == maxDistance) {
                    c.roomType = RoomType.FINAL_BOSS;
                    end = c;
                } else if (lootMode) {
                    // Loot Mode: favor items
                    if (roll < 0.60) c.roomType = RoomType.ITEM;
                    else if (roll < 0.75) c.roomType = RoomType.PUZZLE;
                    else if (roll < 0.90) c.roomType = RoomType.BOSS;
                    else c.roomType = RoomType.BLANK;
                } else {
                    // Normal mode: blank rooms less common
                    if (roll < 0.25) c.roomType = RoomType.ITEM;
                    else if (roll < 0.45) c.roomType = RoomType.PUZZLE;
                    else if (roll < bossChance) c.roomType = RoomType.BOSS;
                    else c.roomType = RoomType.BLANK;
                }
            }
        }
    }

    /** Generates a random item for the player */
    public String generateItem() {
        String[] items = {"Gold", "Potion", "Key", "Scroll", "Gem"};
        return items[rand.nextInt(items.length)];
    }

    public Cell getStart() { return start; }
    public Cell getEnd() { return end; }
    public Cell[][] getGrid() { return grid; }
    public Cell getCell(int x, int y) { return grid[y][x]; }

    // ---------------- Maze Generation ---------------- //

    private void generateMazeDFS(Cell start) {
        Stack<Cell> stack = new Stack<>();
        start.visited = true;
        stack.push(start);

        int[][] dirs = {{0,-1},{1,0},{0,1},{-1,0}};

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            List<Cell> neighbors = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                int nx = current.x + dirs[i][0];
                int ny = current.y + dirs[i][1];
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && !grid[ny][nx].visited)
                    neighbors.add(grid[ny][nx]);
            }
            if (!neighbors.isEmpty()) {
                Cell next = neighbors.get(rand.nextInt(neighbors.size()));
                removeWall(current, next);
                next.visited = true;
                stack.push(next);
            } else stack.pop();
        }
    }

    private void removeWall(Cell a, Cell b) {
        int dx = b.x - a.x, dy = b.y - a.y;
        if (dx == 1) { a.walls[1] = false; b.walls[3] = false; }
        if (dx == -1) { a.walls[3] = false; b.walls[1] = false; }
        if (dy == 1) { a.walls[2] = false; b.walls[0] = false; }
        if (dy == -1) { a.walls[0] = false; b.walls[2] = false; }
    }

    private void calculateDistances(Cell start) {
        Queue<Cell> q = new LinkedList<>();
        start.distance = 0; q.add(start);
        int[][] dirs = {{0,-1},{1,0},{0,1},{-1,0}};
        while (!q.isEmpty()) {
            Cell c = q.poll();
            for (int i = 0; i < 4; i++) {
                int nx = c.x + dirs[i][0];
                int ny = c.y + dirs[i][1];
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    Cell n = grid[ny][nx];
                    if (n.distance == -1 && !c.walls[i]) {
                        n.distance = c.distance + 1;
                        q.add(n);
                    }
                }
            }
        }
    }

    private Cell findFarthestCell() {
        Cell farthest = grid[0][0];
        for (Cell[] row : grid)
            for (Cell c : row)
                if (c.distance > farthest.distance)
                    farthest = c;
        return farthest;
    }

    private void makeDeadEnd(Cell end) {
        int[][] dirs = {{0,-1},{1,0},{0,1},{-1,0}};
        List<Integer> openings = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int nx = end.x + dirs[i][0], ny = end.y + dirs[i][1];
            if (nx >= 0 && nx < width && ny >= 0 && ny < height && !end.walls[i])
                openings.add(i);
        }
        while (openings.size() > 1) {
            int i = openings.remove(rand.nextInt(openings.size()));
            end.walls[i] = true;
            int nx = end.x + dirs[i][0], ny = end.y + dirs[i][1];
            grid[ny][nx].walls[(i+2)%4] = true;
        }
    }
}
