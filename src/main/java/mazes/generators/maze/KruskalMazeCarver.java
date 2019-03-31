package mazes.generators.maze;

import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Wall;
import java.util.Random;
import misc.graphs.Graph;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

        for (Wall wall : maze.getWalls()) {
            wall.setDistance(new Random().nextDouble());
        }
        ISet<Wall> walls = new Graph<>(maze.getRooms(), maze.getWalls()).findMinimumSpanningTree();
        for (Wall wall : walls) {
            wall.resetDistanceToOriginal();
        }
        return walls;
    }
}
