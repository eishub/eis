package eis.examples.acconnector2007;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.dom.AttributeMap;

import eis.iilang.Percept;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.DataContainer;
import eis.iilang.Function;
import eis.iilang.ParameterList;

/**
 * Represents a single connection to a MASSim-server. Each controllable entity (cowboy)
 * can have one connection.
 * 
 * @author tristanbehrens
 *
 */
public class Connection extends Socket implements Runnable {

	/* For building and transforming XML-documents. */
	protected static DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
	protected static TransformerFactory transformerfactory = TransformerFactory.newInstance();
	
	/* For sending and receiving XML-documents. */
	private InputStream inputstream = null;
	private OutputStream outputstream = null;
	
	/** The username of the entity. */
	private String username = null;
	
	/** The password. */
	private String password = null;
	
	/** A listener that is to be informed once there is an incoming message. */
	private ConnectionListener listener = null;

	/** Stores whether the connection is executing. Executing means listening for messages. */
	private boolean executing = true;
	
	/** Contains the last action-id. It is sent if there is an action. Used for blocking action-methods. */
	private String actionId = null;
	
	/** 
	 * Establishes a socket-connection to the MASSim-server.
	 * 
	 * @param listener is the object that is to be provided with incoming messages.
	 * @param host is the hostname (URL or ip-address) of the MASSim-server.
	 * @param port is the port of the MASSim-Server.
	 * @throws UnknownHostException is thrown if the host is unknown.
	 * @throws IOException is thrown if the connection cannot be established.
	 */
	public Connection(ConnectionListener listener, String host, int port) throws UnknownHostException, IOException {
		
		// establish connection
		super(host,port);
		
		// listener for incoming messages
		this.listener = listener;
		
		// prepare for sending and receiving messages
		inputstream = this.getInputStream();
		outputstream = this.getOutputStream();
	
	}
	
	/**
	 * Transforms an XML-document to a string.
	 * 
	 * @param node is an XML-node.
	 * @return the String representation.
	 */
	public static String xmlToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * Sends an authentication-message to the server and waits for the reply.
	 * 
	 * @param username the username of the cowboy.
	 * @param password the password of the cowboy.
	 * @return true on success.
	 */
	public boolean authenticate(String username, String password) {
		
		// 1. Send message
		
		// the document to be sent
		Document doc = null;
		
		// construct the auth-request-message
		try {
			
			doc = documentbuilderfactory.newDocumentBuilder().newDocument();
			Element root = doc.createElement("message");
			root.setAttribute("type","auth-request");
			doc.appendChild(root);
			
			Element auth = doc.createElement("authentication");
			auth.setAttribute("username",username);
			auth.setAttribute("password",password);
			root.appendChild(auth);
			
		} catch (ParserConfigurationException e) {

			System.err.println("unable to create new document for authentication.");

			// could but should not happen
			return false;
			
		}

		// sending the document
		try {
			
			sendDocument(doc);
		
		} catch (IOException e1) {

			System.out.println("Sending document failed.");
			
			return false;
		
		}
		
		// 2. receive reply
		Document reply;
		try {
			reply = receiveDocument();

		} 
		 catch (IOException e) {

			e.printStackTrace();
		
			return false;
			
		}
		
		// check for success
		Element root = reply.getDocumentElement();
		if (root==null) return false;
		if (!root.getAttribute("type").equalsIgnoreCase("auth-response")) return false;
		NodeList nl = root.getChildNodes();
		Element authresult = null;
		for (int i=0;i<nl.getLength();i++) {
			Node n = nl.item(i);
			if (n.getNodeType()==Element.ELEMENT_NODE && n.getNodeName().equalsIgnoreCase("authentication")) {
				authresult = (Element) n;
				break;
			}
		}
		if (!authresult.getAttribute("result").equalsIgnoreCase("ok")) return false;

		// success
		this.username = username;
		this.password = password;
		
		return true;
		
	}

	/**
	 * Sends off an action-message.
	 * 
	 * @param action the action to be sent.
	 * @return true on success.
	 */
	public boolean act(String action, String param) {

		// only mark-action should have a parameter
		if( param!=null)
			assert action.equals("mark");
		
		// the document to be sent
		Document doc = null;
		
		// if there is no agent-id then block for 0.5 secs
		while( actionId == null ) {
		
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {}
		
		}
		
		// create document and send it off
		try {
			
			doc = documentbuilderfactory.newDocumentBuilder().newDocument();
			Element root = doc.createElement("message");
			root.setAttribute("type","action");
			doc.appendChild(root);
			
			Element auth = doc.createElement("action");
			auth.setAttribute("id",actionId);
			auth.setAttribute("type",action);
			if( param!=null )
				auth.setAttribute("param",param);
			root.appendChild(auth);
			
		} catch (ParserConfigurationException e) {

			System.err.println("unable to create new document for authentication.");

			return false;
			
		}

		try {
			sendDocument(doc);
		} catch (IOException e) {

			System.out.println("Sending action-message failed");
			
			return false;
		
		}

		actionId = null;
		
		return true;
	}

	/** 
	 * Receives a document
	 * 
	 * @return the received document.
	 * @throws IOException is thrown if reception failed.
	 */
	public Document receiveDocument() throws IOException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int read = inputstream.read();
		while (read!=0) {
			if (read==-1) {
				throw new IOException(); 
			}
			buffer.write(read);
			try {
				read = inputstream.read();
			} catch (IOException e) {

				throw new IOException("Reading from input-stream failed.");

			}
		}
	
		byte[] raw = buffer.toByteArray();
		
		Document doc;
		try {
			doc = documentbuilderfactory.newDocumentBuilder().parse(new ByteArrayInputStream(raw));
		} catch (SAXException e) {

			throw new IOException("Error parsing");

		} catch (IOException e) {
			throw new IOException("Error parsing");

		} catch (ParserConfigurationException e) {

			throw new IOException("Error parsing");

		}
		
		return doc;
	
	}	
	
	/** 
	 * Sends a document.
	 * 
	 * @param doc is the document to be sent.
	 * @throws IOException is thrown if the document could not be sent.s
	 */
	private void sendDocument(Document doc) throws IOException {
		
		try {
			transformerfactory.newTransformer().transform(new DOMSource(doc),new StreamResult(outputstream));

			ByteArrayOutputStream temp = new ByteArrayOutputStream();
			transformerfactory.newTransformer().transform(new DOMSource(doc),new StreamResult(temp));
			outputstream.write(0);
			outputstream.flush();

		} catch (TransformerConfigurationException e) {

			throw new IOException("transformer config error");
			
		} catch (TransformerException e) {
		
			throw new IOException("transformer error");

		} catch (IOException e) {

			throw new IOException();
		
		} 

	}

	/**
	 * Listen for incoming messages.
	 * 
	 */
	public void run() {

		while( executing ) {

			try {

				// 1. receive a document
				Document doc = this.receiveDocument();
				//System.out.println("Received: " + xmlToString(doc) );
		
				// 2. evaluate document
				LinkedList<Percept> percepts = evaluateDocument(doc);
				
				// 3. notify
				listener.handleMessage(this, percepts);
				
			} catch (IOException e) {

				// terminate connection etc...

				LinkedList<Percept> percepts = new LinkedList<Percept>();
				percepts.add(new Percept("connectionLost"));
				listener.handleMessage(this, percepts);

				executing = false;

			} 			
		}
		
		// done -> shutdown
		
		try {
			this.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Transforms a document into a data-container.
	 * 
	 * @param doc is the document to be transformed.
	 * @return the result of the transformation.
	 */
	private LinkedList<Percept> evaluateDocument(Document doc) {

		// return value
		LinkedList<Percept> ret = new LinkedList<Percept>();

		Element root = doc.getDocumentElement();
		String type = root.getAttribute("type");
		if( type.equals("sim-start") ) {
		
			// The data about the starting simulation are contained in one <simulation>
			// element with attributes:
			// 	"id" - unique identifier of the simulation (string);
			// 	"opponent" - unique identifier of the enemy team (string);
			// 	"steps" - number of steps, simulation will perform (numeric);
			// 	"gsizex" - horizontal size of the grid environment (west-east)
			// 		(numeric);
			// 	"gsizey" - vertical size of the environment (north-south)
			// 		(numeric);
			// 	"depotx" - column of a cell containing the depot (numeric);
			// 	"depoty" - row of a cell containing the depot (numeric).
			
			NodeList nodes = root.getChildNodes();
			Node sim = null;
			for( int a = 0 ; a < nodes.getLength() ; a++ ) {
				
				if(nodes.item(a).getNodeName().equals("simulation"))
					sim = nodes.item(a);
				
			}
			
			assert sim != null;
			
			NamedNodeMap attributes = sim.getAttributes();
			
			Percept percept = null;
						
			// depot
			percept = new Percept(
					"depotPos",
					new Numeral( new Integer(attributes.getNamedItem("depotx").getNodeValue()) ),
					new Numeral( new Integer(attributes.getNamedItem("depoty").getNodeValue()) )
				);
			ret.add(percept);
		
			// grid-size
			percept = new Percept(
					"gridSize",
					new Numeral( new Integer(attributes.getNamedItem("gsizex").getNodeValue()) ),
					new Numeral( new Integer(attributes.getNamedItem("gsizey").getNodeValue()) )
				);
			ret.add(percept);
			
			// simlation id
			percept = new Percept(
					"simId",
					new Identifier( attributes.getNamedItem("id").getNodeValue() )
				);
			ret.add(percept);
			
			// opponent
			percept = new Percept(
					"opponent",
					new Identifier( attributes.getNamedItem("opponent").getNodeValue() )
			);
			ret.add(percept);

			// steps
			percept = new Percept(
					"steps",
					new Numeral( new Integer(attributes.getNamedItem("steps").getNodeValue()) )
			);
			ret.add(percept);
		
			// sim-start
			percept = new Percept("simStart");
			ret.add(percept);

			return ret;
			
		}
		else if ( type.equals("sim-end") ) {

//			<?xml version="1.0" encoding="UTF-8"?><message timestamp="1247641769801" type="sim-end">
//			<sim-result result="draw" score="0"/>
//			</message>
	
			NodeList children = root.getChildNodes();
			
			Node result = null;
			for( int a = 0 ; a < children.getLength() ; a++ ) {
			
				if( children.item(a).getNodeName().equals("sim-result") )
					result = children.item(a);
			
			}
			
			assert result != null;

			NamedNodeMap attributes = result.getAttributes();
		
			Percept percept = null;
			
			percept = new Percept(
					"simEnd"
			); 
			ret.add(percept);
				
			percept = new Percept(
					"result",
					new Identifier( attributes.getNamedItem("result").getNodeValue() )
			);
			ret.add(percept);
			
			// TODO does not work... bug in server?
//			percept = new Percept(
//					"score",
//					new Numeral( new Integer( attributes.getNamedItem("score").getNodeValue() ))
//			); 
//			ret.add(percept);

			return ret;
			
		}
		else if ( type.equals("bye") ) {
		
			ret.add(new Percept("bye"));
			
			return ret;
			
		}
		else if( type.equals("request-action") ) {
			
//			The message envelope of the REQUEST-ACTION message contains element
//			<perception> with four attributes:
//				"step" - current simulation step (numeric);
//				"posx" - column of current agent's position (numeric);
//				"posy" - row of current agent's position (numeric);
//				"items" - number of gold items the agent carries (numeric);
//				"deadline" - server global timer value until which the agent has
//					to deliver its action (the same format as timestamp);
//				"id" - unique identifier of the REQUEST-ACTION message (string).

			NodeList children = root.getChildNodes();
			
			Node perceptNode = null;
			for( int a = 0 ; a < children.getLength() ; a++ ) {
			
				if( children.item(a).getNodeName().equals("perception") )
					perceptNode = children.item(a);
			
			}
			
			assert perceptNode != null;
			
			NamedNodeMap attributes = perceptNode.getAttributes();


			actionId = attributes.getNamedItem("id").getNodeValue();
			int step = new Integer(attributes.getNamedItem("step").getNodeValue());
			
			Percept percept = null;
		
			// currentStep
			percept = new Percept(
					"currentStep", 
					new Numeral( step ) 
			);
			ret.add(percept);

			
			// id
			/*percept = new Percept(
					"id", 
					new Numeral( new Integer(attributes.getNamedItem("id").getNodeValue()) ) 
			);
			ret.add(percept);*/

			// pos
			int posX = new Integer(attributes.getNamedItem("posx").getNodeValue());
			int posY = new Integer(attributes.getNamedItem("posy").getNodeValue());
			
			percept = new Percept(
					"pos",
					new Numeral( posX ),
					new Numeral( posY ),
					new Numeral( step )
			);
			ret.add(percept);

			// current items
			percept = new Percept(
					"currentItems", 
					new Numeral( new Integer(attributes.getNamedItem("items").getNodeValue()) ),
					new Numeral( step )
			);
			ret.add(percept);

			NodeList cellNodes = perceptNode.getChildNodes();

//			Element <perception> contains a number of subelements <cell> with one
//			attribute "id" which can be one of the following values
//			{nw,n,ne,w,cur,e,sw,s,se}, identifying particular cells visible to the
//			agent. These identifiers correspond to the following grid fragment:
			for( int a = 0 ; a < cellNodes.getLength() ; a++ ) {
				
				if( cellNodes.item(a).getNodeName().equals("cell") ) {
					
//					Each element <cell> contains a number of subelements indicating the
//					content of the given cell. Each subelement is a empty element tag
//					without further subelements.
//						<agent> - there's an agent in the cell. The string attribute
//							"type" indicates whether it is an agent of the enemy
//							team, or ally. Allowed values for attribute "type" are
//							{ally,enemy};
//						<obstacle> - the cell contains an obstacle. This element has no
//							associated attributes;
//						<mark> - the cell contains a string mark. The attribute "value"
//							contains the contents of the mark. The value of this
//							attribute can be arbitrary string up to 5 character
//							long.
//						<gold> - the cell contains a gold item. There are no attributes
//							associated with this element.
//						<depot> - the cell is the depot. There are no attributes
//							associated with this element.
//						<empty> - the cell is empty.
//						<unknown> - the content of a cell is not provided by the server
//							because of information distrotion.
					
					NamedNodeMap cellAttributes = cellNodes.item(a).getAttributes();
					String cellPos = cellAttributes.getNamedItem("id").getNodeValue();
					
					Node nab = cellNodes.item(a).getChildNodes().item(1);
					
					String cellContent = nab.getNodeName();

					int cellPosX = 0;
					int cellPosY = 0;
					
					if( cellPos.equals("e") ) {
						cellPosX = posX + 1;
						cellPosY = posY;
					}
					else if( cellPos.equals("ne") ) {
						cellPosX = posX + 1;
						cellPosY = posY - 1;
					}
					else if( cellPos.equals("n") ) {
						cellPosX = posX;
						cellPosY = posY - 1;
					}
					else if( cellPos.equals("nw") ) {
						cellPosX = posX - 1;
						cellPosY = posY - 1;
					}
					else if( cellPos.equals("w") ) {
						cellPosX = posX - 1;
						cellPosY = posY;
					}
					else if( cellPos.equals("sw") ) {
						cellPosX = posX - 1;
						cellPosY = posY + 1;
					}
					else if( cellPos.equals("s") ) {
						cellPosX = posX;
						cellPosY = posY + 1;
					}
					else if( cellPos.equals("se") ) {
						cellPosX = posX + 1;
						cellPosY = posY + 1;
					}
					else if( cellPos.equals("cur") ) {
						cellPosX = posX;
						cellPosY = posY;
					}
					else assert false : "Unexpected direction " + cellPos;
					
					percept = new Percept(
							"cell",
							new Identifier(cellPos),
							new Numeral( cellPosX ),
							new Numeral( cellPosY ), 
							new Identifier(cellContent),
							new Numeral( step )
					);
					ret.add(percept);
					
				}

			}
			
			return ret;
		}
		else {
			
			assert false: "Unknown type " + type;
		}
		
		return null;
	}
	
	/**
	 * Returns the state of the execution.
	 * 
	 * @return the state of the execution.
	 */
	public boolean isExecuting() {
		
		return executing;
		
	}
	
	/**
	 * Closes the connection.
	 */
	public void close() throws IOException {
		
		// stop listening for incoming messages
		executing = false;
		
		// close socket
		super.close();
		
	}
	
	
	
}
