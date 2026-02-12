public class Cell {
    int x, y;
    boolean[] walls = {true, true, true, true};
    boolean visited = false;
    int distance = -1;
    boolean isEnd = false;
    boolean isStart = false;
    boolean traveled = false;
    RoomType roomType; // <-- Must be here
    Cell(int x, int y) { this.x = x; this.y = y; }
}
