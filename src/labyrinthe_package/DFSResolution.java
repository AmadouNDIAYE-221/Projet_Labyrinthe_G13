package labyrinthe_package;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


public class DFSResolution {
    private final Labyrinthe labyrinthe;
    private final int startX, startY, endX, endY;

    public DFSResolution(Labyrinthe labyrinthe, int startX, int startY, int endX, int endY) {
        this.labyrinthe = labyrinthe;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public List<Point> solve() {
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(startX, startY));
        labyrinthe.getGrille()[startX][startY] = 2;  // Marquer comme visité
        List<Point> path = new ArrayList<>();
        
        while (!stack.isEmpty()) {
            Point current = stack.peek();
            
            if (!path.contains(current)) {
                path.add(current);
            }

            if (current.x == endX && current.y == endY) {
                return path;  // Si on a atteint la destination
            }

            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            boolean found = false;

            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                if (nx >= 0 && nx < labyrinthe.getTaille() && ny >= 0 && ny < labyrinthe.getTaille() && labyrinthe.getGrille()[nx][ny] == 0) {
                    stack.push(new Point(nx, ny));
                    labyrinthe.getGrille()[nx][ny] = 2;  // Marquer comme visité
                    found = true;
                    break;
                }
            }

            if (!found) {
                stack.pop();
                path.remove(path.size() - 1);  // Retirer de la liste du chemin
            }
        }
        return null;  // Pas de solution
    }
}



