package labyrinthe_package;

import java.util.*;

public class Labyrinthe implements Cloneable {

    private int[][] grille;
    private final int taille;
    private final int departX, departY, arriveeX, arriveeY;
    private List<Point> chemin;

    public Labyrinthe(int taille) {
        this.taille = taille;
        grille = new int[taille][taille];
        chemin = null;
        departX = taille - 3;
        departY = taille - 3;
        arriveeX = 1;
        arriveeY = 1;
        System.out.println("Taille du labyrinthe : " + taille);
        System.out.println("Arrivée (initiale) : (" + arriveeX + ", " + arriveeY + ")");
    }

    // Ajout des méthodes getter pour arriveeX et arriveeY
    public int getArriveeX() {
        return arriveeX;
    }

    public int getArriveeY() {
        return arriveeY;
    }
    // Ajout des méthodes getter pour arriveeX et arriveeY

    public int getDepartX() {
        return departX;
    }

    public int getDepartY() {
        return departY;
    }

    public int[][] getGrille() {
        return grille;  // grille est un tableau 2D que tu as probablement déjà défini
    }

    public int getTaille() {
        return taille;  // taille est une variable indiquant la dimension du labyrinthe
    }

    public void genererLabyrinthe() {
        for (int i = 0; i < taille; i++) {
            Arrays.fill(grille[i], 1);
        }

        grille[departX][departY] = 0;
        genererRecursive(departX, departY);

        // Assurer un chemin vers l'arrivée
        grille[arriveeX][arriveeY] = 0;
        grille[arriveeX][1] = 0;
        grille[1][arriveeY] = 0;

        System.out.println("Arrivée (après génération) : (" + arriveeX + ", " + arriveeY + ")");
        System.out.println("Départ : (" + departX + ", " + departY + ")");
        System.out.println("Arrivée : (" + arriveeX + ", " + arriveeY + ")");

        afficherGrille();
    }

    private void genererRecursive(int x, int y) {
        grille[x][y] = 0;
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        Collections.shuffle(Arrays.asList(directions));

        for (int[] dir : directions) {
            int nx = x + dir[0] * 2, ny = y + dir[1] * 2;
            if (nx > 0 && nx < taille - 1 && ny > 0 && ny < taille - 1 && grille[nx][ny] == 1) {
                grille[x + dir[0]][y + dir[1]] = 0;
                genererRecursive(nx, ny);
            }
        }
    }

    private void afficherGrille() {
        for (int[] row : grille) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public void setChemin(List<Point> chemin) {
        this.chemin = chemin;
    }

    public int resoudreDFS() {
        // Si un chemin est déjà trouvé, on ne le recalcul pas
        if (chemin != null) {
            return chemin.size();
        }

        DFSResolution resolver = new DFSResolution(this, departX, departY, arriveeX, arriveeY);
        List<Point> path = resolver.solve();
        chemin = (path != null) ? path : null;
        if (path != null) {
            setChemin(path);  // Sauvegarder le chemin dans le labyrinthe
        }
        return (path != null) ? path.size() : 0;
    }

    public int resoudreBFS() {
        Queue<Point> queue = new LinkedList<>();
        boolean[][] visited = new boolean[taille][taille];
        Point[][] parent = new Point[taille][taille];

        queue.add(new Point(departX, departY));
        visited[departX][departY] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.x == arriveeX && current.y == arriveeY) {
                marquerChemin(parent);
                return compterEtapes(parent);  // Retourne le nombre d'étapes dans le chemin
            }

            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int[] dir : directions) {
                int nx = current.x + dir[0], ny = current.y + dir[1];
                if (nx >= 0 && nx < taille && ny >= 0 && ny < taille && grille[nx][ny] == 0 && !visited[nx][ny]) {
                    queue.add(new Point(nx, ny));
                    visited[nx][ny] = true;
                    parent[nx][ny] = new Point(current.x, current.y); // Copier les coordonnées pour éviter une boucle infinie
                }
            }
        }
        return 0;  // Aucun chemin trouvé
    }

    private int compterEtapes(Point[][] parent) {
        int steps = 0;
        Point current = new Point(arriveeX, arriveeY);

        while (current != null) {
            steps++;
            if (current.x == departX && current.y == departY) {
                break;
            }
            current = parent[current.x][current.y];
        }
        return steps;
    }

    private void marquerChemin(Point[][] parent) {
        chemin = new ArrayList<>();
        Point current = new Point(arriveeX, arriveeY);

        while (current != null) {
            chemin.add(current);
            if (current.x == departX && current.y == departY) {
                break;
            }
            current = parent[current.x][current.y];
        }
        Collections.reverse(chemin);
    }

    //public void effacerChemin() { chemin = null; }
    // Méthode effacerChemin() mise à jour
    public void effacerChemin() {
        chemin = null;
        // Réinitialiser les cases visitées et le chemin
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (grille[i][j] == 2 || grille[i][j] == 3) {  // Si la case est visitée ou fait partie du chemin
                    grille[i][j] = 0;  // Réinitialiser à 0
                }
            }
        }
    }

    public List<Point> getChemin() {
        return chemin;
    }

    public int[][] getMaze() {
        return this.grille;
    }

    public static int getSize() {
        return 20;
    }

    @Override
    public Labyrinthe clone() {
        try {
            Labyrinthe clone = (Labyrinthe) super.clone();
            clone.grille = new int[this.taille][this.taille];
            for (int i = 0; i < this.taille; i++) {
                clone.grille[i] = Arrays.copyOf(this.grille[i], this.taille);
            }
            clone.chemin = (this.chemin != null) ? new ArrayList<>(this.chemin) : null;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
