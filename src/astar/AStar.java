package astar;

import java.util.ArrayList;

/**
 *
 * @author Dominik Sust
 * @creation 01.03.2017 16:34:42
 */
public interface AStar
{

    /**
     * Konstruktor, dem eine Liste aller Knoten übergeben wird. Ein Knoten muss
     * das Flag isStartNode gesetzt haben
     *
     * @param alleKnoten
     * @param mapHeight
     * @param mapWidth
     * @throws Exception
     */
    void setInformation( ArrayList<Node> alleKnoten, int mapHeight, int mapWidth ) throws Exception;

    ArrayList<Node> searchPath() throws Exception;

    void getPathToGoal();
}
