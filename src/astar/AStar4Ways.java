package astar;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Implements the A* algorithm that calculates the shortest path to all areas of
 * the map. The distance between the nodes is always 1. Diagonal paths are
 * ignored, only north, east, south and west.
 *
 * @author Dominik Sust
 */
public class AStar4Ways implements AStar
{

    int mapHeight;
    int mapWidth;
    Node goalNode;

    /*
     Stores all nodes
     */
    ArrayList<Node> allNodes = new ArrayList<Node>();

    /*
     Unknown nodes These nodes were not found during the search. No path is
     known to them. Each node (except the start node) Is unknown at the
     beginning of the algorithm.
     */
    ArrayList<Node> unknownList = new ArrayList<Node>();

    /* 
     Known nodes
     A (possibly suboptimal) path is known to these nodes.
     All known nodes are stored together with their f-value in the
     so-called Open List. From this list the most reliable nodes will be 
     selected and investigated. The implementation of 
     the OpenList has great influence on the runtime and is often called
     as simple priority queue (eg, binary heap).
     At the beginning only the start node is known. 
     */
    ArrayList<Node> openList = new ArrayList<Node>();

    /* Conclusively examined nodes
     The shortest path is known to these nodes. The final
     examined nodes are stored in the so-called Closed List,
     So that they are not examined several times. To make efficient decisions too
     Whether an element is on the closed list, this is often
     As a set. The Closed List is empty at the beginning.
     */
    ArrayList<Node> closeList = new ArrayList<Node>();

    @Override
    public void setInformation( ArrayList<Node> alleKnoten, int mapHeight, int mapWidth ) throws Exception
    {
        //Clear all lists
        allNodes.clear();
        openList.clear();
        closeList.clear();

        allNodes.addAll( alleKnoten );

        if ( hasStartNode( alleKnoten ) )
        {
            while ( !alleKnoten.isEmpty() )
            {
                //Examine node of position 0
                Node n = alleKnoten.get( 0 );
                //We found the start node
                if ( n.isStartNode() )
                {
                    alleKnoten.remove( 0 );
                    openList.add( n );
                }
                //We found the target node
                else if ( n.isGoalNode() )
                {
                    goalNode = n;
                    alleKnoten.remove( 0 );
                    unknownList.add( n );
                }
                //Node is a block
                else if ( n.isBlockNode() )
                {
                    alleKnoten.remove( 0 );
                    closeList.add( n );
                }
                //Node is a simple one
                else
                {
                    alleKnoten.remove( 0 );
                    unknownList.add( n );
                }
            }
            this.mapHeight = mapHeight;
            this.mapWidth = mapWidth;
        }
        else
        {
            throw new Exception( "Kein Startknoten gefunden!" );
        }
    }

    /**
     * Checks if a start node is present
     *
     * @param n List of all nodes
     * @return true, if there is a start node
     */
    private boolean hasStartNode( ArrayList<Node> n )
    {
        for ( Node node : n )
        {
            if ( node.isStartNode() )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Main method of the algorithm. 
     * Returns an ArrayList with all nodes at the end.
     *
     * @return ArrayList{@literal <Node>} list with all nodes
     * @throws Exception will be thrown if a node is not been found
     */
    @Override
    public ArrayList<Node> searchPath() throws Exception
    {
        while ( !openList.isEmpty() )
        {
            //Remove first node of openlist
            Node currentNode = openList.get( 0 );
            openList.remove( 0 );

            //Target found?
            //return true, exiting algorithm
            // The current node should not be investigated further by following 
            // functions so that no cycles are created
            closeList.add( currentNode );
            // If the target has not yet been found, set the successor node 
            // of the current node to the Open List
            expandNode( currentNode );
        }
        return allNodes;
    }

    /**
     * Checks all successor nodes and adds them to the OpenList when
     * either the successor node is found for the first time or
     * a better way to this node has been found
     *
     * @param currentNode current examined node
     * @throws Exception will be thrown if a node is not been found
     */
    private void expandNode( Node currentNode ) throws Exception
    {
        //If the successor node is already on the closed list - do nothing
        //A node can have a maximum of 4 successor nodes
        //North
        if ( currentNode.y > 0 )
        {
            Node nachfolger = getNodeAtXY( currentNode.x, currentNode.y - 1 );
            checkNachfolger( currentNode, nachfolger );
        }
        //East
        if ( currentNode.x < mapWidth - 1 )
        {
            Node nachfolger = getNodeAtXY( currentNode.x + 1, currentNode.y );
            checkNachfolger( currentNode, nachfolger );
        }
        //South
        if ( currentNode.y < mapHeight - 1 )
        {
            Node nachfolger = getNodeAtXY( currentNode.x, currentNode.y + 1 );
            checkNachfolger( currentNode, nachfolger );
        }
        //West
        if ( currentNode.x > 0 )
        {
            Node nachfolger = getNodeAtXY( currentNode.x - 1, currentNode.y );
            checkNachfolger( currentNode, nachfolger );
        }

    }

    /**
     * Returns the node with given coordinates
     * Throws an exception if not was not found
     *
     * @param x
     * @param y
     * @return Node with given coordinates
     * @throws Exception if node was not found
     * wurde
     */
    private Node getNodeAtXY( int x, int y ) throws Exception
    {
        for ( Node n : allNodes )
        {
            if ( n.x == x && n.y == y )
            {
                return n;
            }
        }
        throw new Exception( "Knoten nicht gefunden: x: " + x + ", y: " + y );
    }

    /**
     * Checks the neighbour-nodes of current node. 
     * If they are not in close list, they will be checked
     *
     * @param currentNode current node
     * @param nachfolger current nodes neighbours
     */
    private void checkNachfolger( Node currentNode, Node nachfolger )
    {
        //Neighbournode is not in CloseList
        if ( !closeList.contains( nachfolger ) )
        {
            //Calculate distance (every way is one step)
            int dist = currentNode.getDistanceToStart() + 1;
            //If following node is not in openList and the new way is better than the old one do nothing
            if ( !openList.contains( nachfolger ) && nachfolger.getDistanceToStart() <= dist )
            {
                //Set previous node and update distance
                nachfolger.setVorgaenger( currentNode );
                nachfolger.setDistanceToStart( dist );
                if ( !openList.contains( nachfolger ) )
                {
                    openList.add( nachfolger );
                }
            }
        }
    }

    /**
     * Recursive Function to display the path
     */
    @Override
    public void getPathToGoal()
    {
        Node n = goalNode.getVorgaenger();
        while ( n.getVorgaenger() != null )
        {
            n.setBackground( Color.GREEN );
            n = n.getVorgaenger();
        }
    }
}
