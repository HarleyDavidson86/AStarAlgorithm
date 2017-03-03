package main;

import astar.AStar;
import astar.AStar4Ways;
import astar.AStar8Ways;
import astar.Node;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author Dominik Sust
 * @creation 01.03.2017 16:06:45
 */
public class Main
{

    int breite = 15;
    int hoehe = 10;
    Node[][] buttons;
    JFrame frame;

    /**
     * @param args the command line arguments
     */
    public static void main( String args[] )
    {
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                new Main();
            }
        } );
    }
    private final JRadioButtonMenuItem mi_eight;
    private final JRadioButtonMenuItem mi_four;
    private AStar astar;

    public Main()
    {
        frame = new JFrame( "A Star Algorithmus 4 Wege" );
        JPanel contentPane = new JPanel();
        contentPane.setLayout( new GroupLayout( contentPane ) );
        contentPane.setPreferredSize( new Dimension( 60 * breite, 60 * hoehe ) );
        placeButtons( contentPane );

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu( "A Star" );

        mi_four = new JRadioButtonMenuItem( "4 Wege" );
        mi_four.setSelected( true );
        menu.add( mi_four );

        mi_eight = new JRadioButtonMenuItem( "8 Wege" );
        mi_eight.setSelected( false );
        menu.add( mi_eight );
        
        JMenuItem infoItem = new JMenuItem("Info");
        infoItem.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                String message = "A* Algorithums\n"
                        + "Über das Menü kann man auswählen, ob 4 Wege oder 8 Wege berechnet werden sollen.\n"
                        + "Bei 8 Wege werden auch diagonale Schritte mit berechnet.\n"
                        + "SHIFT+Klick setzt einen Block (Hindernis)\n"
                        + "Klick setzt Kachel auf Start und startet den Berechnungsalgorithmus";
                JOptionPane.showMessageDialog( frame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add( infoItem);

        menubar.add( menu );

        ButtonGroup group = new ButtonGroup();
        group.add( mi_four );
        group.add( mi_eight );

        ItemListener il = new ItemListener()
        {

            @Override
            public void itemStateChanged( ItemEvent e )
            {
                if ( mi_eight.isSelected() )
                {
                    astar = new AStar8Ways();
                    frame.setTitle( "A Star Algorithmus 8 Wege" );
                }
                if ( mi_four.isSelected() )
                {
                    astar = new AStar4Ways();
                    frame.setTitle( "A Star Algorithmus 4 Wege" );
                }

            }
        };
        il.itemStateChanged( null );

        mi_four.addItemListener( il );
        mi_eight.addItemListener( il );

        frame.setJMenuBar( menubar );

        frame.setLayout( null );
        frame.setContentPane( contentPane );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.pack();
        frame.setVisible( true );
    }

    private void placeButtons( JPanel contentPane )
    {
        buttons = new Node[hoehe][breite];

        for ( int i = 0; i < hoehe; i++ )
        {
            for ( int j = 0; j < breite; j++ )
            {
                Node b = new Node( j + "/" + i, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, j, i );
                //b.addActionListener( new MyButtonListener() );
                b.addMouseListener( new MyButtonListener() );
                b.setBounds( j * 60, i * 60, 60, 60 );
                b.setText( "0" );
                contentPane.add( b );
                buttons[i][j] = b;
            }
        }
    }

    class MyButtonListener extends MouseAdapter
    {

        @Override
        public void mouseClicked( MouseEvent e )
        {
            //Mit gedrückter SHIFT Taste wird ein Block gesetzt
            if ( e.isShiftDown() )
            {
                Node current = (Node) e.getSource();
                current.setAsBlock( !current.isBlockNode() );
                current.setBackground( new JButton().getBackground());
                if ( current.isBlockNode() )
                {
                    current.setBackground( Color.BLACK);
                }
            }
            else
            {
                //Alle StartButtons löschen und Distanz resetten
                for ( int i = 0; i < hoehe; i++ )
                {
                    for ( int j = 0; j < breite; j++ )
                    {
                        buttons[i][j].setAsStartNode( false );
                        buttons[i][j].setDistanceToStart( 0 );
                    }
                }
                //Geklickter Button wird Block
                Node current = (Node) e.getSource();
                current.setAsStartNode( true );
                runAStar();
            }
        }
    }

    public void runAStar()
    {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for ( int i = 0; i < hoehe; i++ )
        {
            for ( int j = 0; j < breite; j++ )
            {
                nodes.add( buttons[i][j] );
            }
        }
        try
        {
            astar.setInformation( nodes, hoehe, breite );
            nodes = astar.searchPath();
            for ( Node n : nodes )
            {
                n.setText( String.valueOf( n.getDistanceToStart() ) );
            }
        }
        catch ( Exception ex )
        {
            Logger.getLogger( Main.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }
}
