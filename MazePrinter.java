import java.util.Arrays;

/**
 * Renders the maze in ASCII format with fog-of-war visibility.
 */
public class MazePrinter {

    public static void print(Maze maze, int px, int py) {
        Cell[][] grid = maze.getGrid();
        int height = grid.length;
        int width = grid[0].length;

        for (int y = 0; y < height; y++) {
            // Top walls
            for (int x = 0; x < width; x++) {
                Cell c = grid[y][x];
                boolean[] w = displayWalls(c, px, py, grid);
                System.out.print("+");
                System.out.print(w[0] ? "───" : "   ");
            }
            System.out.println("+");

            // Cell content
            for (int x = 0; x < width; x++) {
                Cell c = grid[y][x];
                boolean[] w = displayWalls(c, px, py, grid);
                boolean showLeft = (x == 0) || visible(c, px, py, grid) || (x > 0 && visible(grid[y][x - 1], px, py, grid));
                System.out.print(showLeft && w[3] ? "|" : " ");

                // Player
                if (px == x && py == y) System.out.print(" P ");
                // Visited rooms
                else if (c.traveled) {
                    switch (c.roomType) {
                        case BLANK -> System.out.print(" . ");
                        case ITEM -> System.out.print(" I ");
                        case PUZZLE -> System.out.print(" ? ");
                        case BOSS -> System.out.print(" B ");
                        case FINAL_BOSS -> System.out.print(" F ");
                    }
                }
                // Visible but untraveled rooms
                else if (visible(c, px, py, grid)) System.out.print("   ");
                else System.out.print("   "); // fully hidden
            }

            boolean showRight = displayWalls(grid[y][width - 1], px, py, grid)[1] &&
                    (visible(grid[y][width - 1], px, py, grid) || (width > 1 && visible(grid[y][width - 2], px, py, grid)));
            System.out.println(showRight ? "|" : " ");
        }

        // Bottom walls
        for (int x = 0; x < width; x++) {
            System.out.print("+");
            System.out.print("───");
        }
        System.out.println("+");
    }

    /** Returns whether a cell is visible to the player */
    private static boolean visible(Cell c, int px, int py, Cell[][] grid) {
        if (c.traveled) return true;
        Cell current = grid[py][px];
        int distDiff = Math.abs(c.distance - current.distance);
        int dx = Math.abs(c.x - px);
        int dy = Math.abs(c.y - py);
        return distDiff <= 1 && (dx + dy == 1);
    }

    /** Returns the walls to display for a cell, considering fog-of-war */
    private static boolean[] displayWalls(Cell c, int px, int py, Cell[][] grid) {
        boolean[] w = Arrays.copyOf(c.walls, 4);
        if (!c.traveled && visible(c, px, py, grid)) {
            for (int i = 0; i < 4; i++) {
                int nx = c.x + (i == 1 ? 1 : i == 3 ? -1 : 0);
                int ny = c.y + (i == 2 ? 1 : i == 0 ? -1 : 0);
                if (nx >= 0 && nx < grid[0].length && ny >= 0 && ny < grid.length && grid[ny][nx].traveled)
                    continue; // keep connection to traveled neighbor
                else w[i] = true; // block other sides
            }
        } else if (!c.traveled && !visible(c, px, py, grid)) {
            Arrays.fill(w, true); // fully hidden
        }
        return w;
    }
}
    
