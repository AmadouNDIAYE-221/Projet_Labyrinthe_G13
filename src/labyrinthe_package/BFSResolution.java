package labyrinthe_package;

import java.util.LinkedList;
import java.util.Queue;

public class BFSResolution {
    private final int[][] maze; // Stockage du labyrinthe

    // Ajout du constructeur qui prend le labyrinthe en paramètre
    public BFSResolution(int[][] maze) {
        this.maze = maze;
    }

    public void solve() {
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[maze.length][maze.length];
        queue.add(new int[]{0, 0});
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0], y = pos[1];

            if (x == maze.length - 1 && y == maze.length - 1) break;

            int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
            for (int[] dir : directions) {
                int newX = x + dir[0], newY = y + dir[1];
                if (newX >= 0 && newY >= 0 && newX < maze.length && newY < maze.length && maze[newX][newY] == 0 && !visited[newX][newY]) {
                    queue.add(new int[]{newX, newY});
                    visited[newX][newY] = true;
                    maze[newX][newY] = 2; // Marquer le chemin avec +
                }
            }
        }
    }
}
