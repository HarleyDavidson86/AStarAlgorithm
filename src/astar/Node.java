package astar;

import javax.swing.JButton;

/**
 *
 * @author Dominik Sust
 * @creation 05.09.2013 13:12:46
 */
public class Node extends JButton
{

    private String name;
    private boolean isStartNode;
    private boolean isGoalNode;
    private boolean isBlock;
    private Node vorgaenger;
    public final int x;
    public final int y;
    private int distanceToStart = 0;

    public Node( String name, Boolean isStart, Boolean isGoal, Boolean isBlock, int x, int y )
    {
        this.name = name;
        this.isStartNode = isStart;
        this.isGoalNode = isGoal;
        this.isBlock = isBlock;
        this.x = x;
        this.y = y;
        
        //Bei einem Block wird die Distanz auf -1 gesetzt
        if (isBlock)
        {
            this.setDistanceToStart( -1);
        }
    }
    
    public boolean isBlockNode()
    {
        if (isBlock)
        {
            return true;
        }
        return false;
    }
    
    public void setAsBlock(Boolean b)
    {
        if (b)
        {
            this.isBlock = b;
            this.distanceToStart = -1;
        }
        else
        {
            this.isBlock = b;
            this.distanceToStart = 0;
        }
    }
    
    public void setAsStartNode(Boolean b)
    {
        this.isStartNode = b;
    }

    public int getDistanceToStart()
    {
        return distanceToStart;
    }

    public void setDistanceToStart( int distanceToStart )
    {
        this.distanceToStart = distanceToStart;
    }

    public boolean isStartNode()
    {
        if (isStartNode)
        {
            return true;
        }
        return false;
    }

    public boolean isGoalNode()
    {
        if (isGoalNode)
        {
            return true;
        }
        return false;
    }

    public void setVorgaenger( Node vorgaenger )
    {
        this.vorgaenger = vorgaenger;
    }

    public String getName()
    {
        return name;
    }

    public Node getVorgaenger()
    {
        return vorgaenger;
    }

    @Override
    public String toString()
    {
        return "Name: " + this.name + ", Distanz zum Start: " + this.distanceToStart;
    }
}
