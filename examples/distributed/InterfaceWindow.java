package eis.examples.distributed;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import eis.exceptions.AgentException;

public class InterfaceWindow extends JFrame {

	private JTextPane stats = null;
	private JTextPane log = null;

	public InterfaceWindow() {
		
		this.setSize(425, 450);
		this.setResizable(false);

		// adding tabs
		stats = new JTextPane();
		stats.setBorder(BorderFactory.createLineBorder (Color.gray, 1));
		stats.setEnabled(false);
		stats.setContentType("text/html");

		log = new JTextPane();
		log.setBorder(BorderFactory.createLineBorder (Color.gray, 1));
		log.setEnabled(false);
		log.setContentType("text/html");

		JTabbedPane tabs = new JTabbedPane();
		tabs.add("Log", log);
		tabs.add("Statistics", stats);
		
		this.add(tabs);
		
		this.setVisible(true);
		
	}

	public void update(Server server, Environment env) {
		
		String text = "";
		
		text += "<b>Entities:</b><i>";
		text += server.getEntities();
		text += "</i><br>";
		
		text += "<b>Agents:</b><i>";
		text += server.getAgents();
		text += "</i><br>";
		
		for( String ag : server.getAgents() ) {
			
			try {
				
				String line = "Entity <i>" + ag + "</i> is related with <i>" + server.getAssociatedEntities(ag) + "</i><br>";
				text += line;
					
			} catch (AgentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}

		stats.setText(text);
		
	}

	public void logPrintln(Object obj) {
		
		String text = "";
		
		//SimpleDateFormat formater = new SimpleDateFormat();
		DateFormat formater;
	    formater = DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.MEDIUM );
		
		text += "<b>";
		text += formater.format(new Date());
		text += "</b>: ";
		text += obj.toString();
		text += "<br>";
		text += log.getText();
		
		log.setText(text);
		
	}
	
}
