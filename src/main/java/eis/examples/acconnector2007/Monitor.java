package eis.examples.acconnector2007;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;

class ScreenToken {
	
	public int x;
	public int y;
	public Color color;
	
}

class EntityData {
	
	public int x;
	public int y;
	public int step;
	public Vector<ScreenToken> tokens = new Vector<ScreenToken>();
	
}

public class Monitor extends JFrame implements Runnable {

	/** Life of the thread. */
	private boolean running = true;
	
	/** Width of the grid. */
	private int gridWidth = 0;
	
	/** Height of the grid. */
	private int gridHeight = 0;
	
	/** Cell-size. */
	private int cellSize = 20;
	
	/** Entity data. */
	private ConcurrentHashMap<String, EntityData> data = new ConcurrentHashMap<String,EntityData>();
	
	private int depotX = 0;
	private int depotY = 0;

	private boolean[][] obstacle = null;
	
	private String simId = "";
	
	private String opponent = "";
	
	private int steps = 0;
	private int currentStep = 0;
	

	/**
	 * Constructs a monitor.
	 */
	public Monitor() {
		
		this.setSize(5, 5);
		
		this.setTitle("EIS Agent Contest 2007");
		this.setResizable(true);
		this.setVisible(true);
		this.createBufferStrategy(2);
		
		new Thread(this).start();
		
	}
	
	@Override
	public void run() {

		while(running == true) {

			try {
			
			// get the strategy
			BufferStrategy strategy = null;
			Graphics2D g2d = null;
			strategy = this.getBufferStrategy();
			g2d = (Graphics2D) strategy.getDrawGraphics();

			// paint
			paint(g2d);

			// done
			g2d.dispose();
			
			// display
			strategy.show();
			
			} 
			catch( ConcurrentModificationException e) {
				System.out.println("cme");
			}
			
			// re-render every 0.5 seconds
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void paint(Graphics g) {
	
		((Graphics2D)g).clearRect(0, 0, 2000, 2000);
		
		// draw background
		g.setColor(Color.GREEN.darker());
		g.fillRect(20, 20, gridWidth*cellSize, gridHeight*cellSize);
		
		// draw obstacles
		g.setColor(Color.GREEN.darker().darker().darker());
		for( int x = 0 ; x < this.gridWidth ; x++ )
			for( int y = 0 ; y < this.gridHeight ; y++ ) 
				if( obstacle[x][y] == true ) 
					g.fillRect(x*cellSize+20, y*cellSize+20, cellSize, cellSize);
		
		// draw depot
		g.setColor(Color.GRAY);
		g.fillRect(depotX*cellSize+20, depotY*cellSize+20, cellSize, cellSize);
		
		// draw gold-miner 
		for( EntityData d : data.values() ) {

				for( ScreenToken t : d.tokens ) {
					
					g.setColor(t.color);
					g.fillOval(t.x*cellSize+20, t.y*cellSize+20, cellSize, cellSize);
					
				}

				g.setColor(Color.BLUE);
				g.fillOval(d.x*cellSize+20, d.y*cellSize+20, cellSize, cellSize);
				g.drawRect((d.x-1)*cellSize+20, (d.y-1)*cellSize+20, cellSize*3, cellSize*3);

			}
		
	}	
	
	
	public void processPercept(Percept p, String entity) {

		// already in data-structure?
		EntityData d = data.get(entity);
		if( d == null ) {
			d = new EntityData();
			data.put(entity, d);
		}
		
		if( p.getName().equals("gridSize") ) {
			
			gridWidth =  ((Numeral)p.getParameters().get(0)).getValue().intValue();
			gridHeight =  ((Numeral)p.getParameters().get(1)).getValue().intValue();
			this.setSize(gridWidth*cellSize+40, gridHeight*cellSize+40);
			obstacle = new boolean[gridWidth][gridHeight];
			
			
		}
		else if ( p.getName().equals("depotPos") ) {

			depotX = ((Numeral)p.getParameters().get(0)).getValue().intValue();
			depotY = ((Numeral)p.getParameters().get(1)).getValue().intValue();
			
		}
		else if ( p.getName().equals("pos") ) {
			
			d.x = ((Numeral)p.getParameters().get(0)).getValue().intValue();
			d.y = ((Numeral)p.getParameters().get(1)).getValue().intValue();
			d.tokens = new Vector<ScreenToken>();
			
		} 
		else if ( p.getName().equals("cell") ) {
			
			String content = ((Identifier)p.getParameters().get(3)).getValue();
			int x = ((Numeral)p.getParameters().get(1)).getValue().intValue();
			int y = ((Numeral)p.getParameters().get(2)).getValue().intValue();
			
			if( content.equals("obstacle") ) {

				obstacle[x][y] = true;
				
			}
			else if( (content.equals("empty") || content.equals("unknown")) == false ) {

				ScreenToken t = new ScreenToken();
				t.x = ((Numeral)p.getParameters().get(1)).getValue().intValue();
				t.y = ((Numeral)p.getParameters().get(2)).getValue().intValue();

				if( content.equals("obstacle") ) 
					t.color = Color.green.darker().darker().darker();
				else if( content.equals("gold") ) 
					t.color = Color.yellow;
				else if( content.equals("agent") ) 
					t.color = Color.blue.darker();
				else assert false : "Unhandled content " + content;
				
				d.tokens.add(t);

			}
			
		} 
		else if ( p.getName().equals("simId") ) {
			simId = p.getParameters().get(0).toProlog();
			this.setTitle();
		}
		else if ( p.getName().equals("opponent") ) {
			opponent = p.getParameters().get(0).toProlog();
			this.setTitle();
		}
		else if ( p.getName().equals("steps") ) {
			steps = ((Numeral)p.getParameters().get(0)).getValue().intValue();
			this.setTitle();
		}
		else if ( p.getName().equals("simStart") ) {
		}
		else if ( p.getName().equals("currentStep") ) {
			currentStep = ((Numeral)p.getParameters().get(0)).getValue().intValue();
			this.setTitle();
		}
		else if ( p.getName().equals("currentItems") ) {
		}
		else assert false : "Unexpected percept " + p.toProlog();
		
	}
	
	private void setTitle() {
		
		String title = "Goldminers";
		
		title += " Simulation:" + simId;
		title += " Opponent:" + opponent;
		title += " Steps:" + currentStep + "/" + steps;
		
		
		this.setTitle(title);
	}
	
}
