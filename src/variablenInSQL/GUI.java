package variablenInSQL;

import java.awt.Color;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.undo.UndoManager;

public class GUI
{
	private JFrame frame;

	private JTabbedPane paneTabbedAufgabe;
	private JTabbedPane paneTabbedUnparsed;
	private JTabbedPane paneTabbedParsed;
	private JTabbedPane paneTabbedTable;
	private Color defaultColor;

	private JScrollPane paneScrollUnparsed;
	private JScrollPane paneScrollParsed;
	private JScrollPane scrollPaneTable;

	private JEditorPane paneQueryUnparsed;
	private JEditorPane paneQueryParsed;
	private JEditorPane paneQueryError;
	private JEditorPane paneEinführungsaufgabe;
	private JEditorPane paneVersicherungsaufgabe;
	private JEditorPane paneRennaufgabe;
	private JEditorPane paneLastText;

	private JTable tableResult;

	private JTree paneDataTree;

	private JButton buttonAnfrageParsen;
	private JButton buttonAnfrageAbschicken;
	private JButton buttonAnfrageLeeren;
	private JButton buttonNächsteAufgabe;

	private JMenu menuDatei;
	private JMenu menuBearbeiten;
	private JMenu menuEinstellungen;
	private JMenu menuHilfe;

	private JMenuItem menuItemAnfrageParsen;
	private JMenuItem menuItemAnfrageAbschicken;
	private JMenuItem menuItemAnfrageLeeren;
	private JMenuItem menuItemBeenden;
	private JMenuItem menuItemUndo;
	private JMenuItem menuItemRedo;
	private JMenuItem menuItemPause;
	private JMenuItem menuItemSkipTask;
	private JMenuItem menuItemReset;
	private JMenuItem menuItemInformation;

	private JSeparator separatorDatei1;

	private KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
	private KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
	private KeyStroke parseKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
	private KeyStroke executeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
	private KeyStroke clearKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
	private KeyStroke pauseKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
	private KeyStroke skipTaskKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
	private KeyStroke resetKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK);
	private KeyStroke informationKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);

	private String[] columnNames;
	private String[][] data;
	private String selectedText;
	private String subjectName;

	private boolean executable = false;
	private boolean isSolution = false;
	static boolean finished = false;
	private boolean vorletzte = false;
	private boolean changeable = false;
	private boolean skippedTask0 = false;
	private boolean skippedTask1 = false;
	private boolean skippedTask2 = false;

	private int id = 0;
	private int index = 0;
	private long startTime0 = 0;
	private long startTime1 = 0;
	private long startTime2 = 0;
	private long finishTime = 0;
	private long pause0 = 0;
	private long pause1 = 0;
	private long pause2 = 0;

	private JFormattedTextField paneStatus;

	// Setzt Teile der Texte der GUI, die für die Bachelorarbeit gescreenshotted werdenm ussten, auf Englisch
	private boolean english = false;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					GUI window = new GUI();
					window.frame.setVisible(true);

					// Informationen für den User und Startzeitpunkt der Bearbeitung
					window.paneDataTree.expandRow(6);
					window.paneDataTree.expandRow(5);
					window.paneDataTree.setEnabled(false);
					window.aufgabenID();
					window.information(window.id);
					window.startTime0 = System.currentTimeMillis();
					window.paneEinführungsaufgabe.setText("Ziel dieser ersten Augabe ist es, Sie mit diesem Tool vertraut zu machen. \nDabei soll diese Aufgabe sowohl einmal mit als auch einmal ohne Verwendung der erweiterten SQL-Syntax bearbeitet werden.\n(Klicken Sie also bitte erst auf \"Nächste Aufgabe\", nachdem Sie die Aufgabe mit beiden Ansätzen gelöst haben und Sie sich mit der neuen Syntax vertraut gemacht haben)\n\nGeben Sie die Mitarbeiter und ihren zugehörigen Lohn für solche Mitarbeiter aus,\ndie an einem Projekt arbeiten, in denen weniger als 5 Personen mitwirken.\nAusgabe: MitarbeiterID, Lohn");
					window.paneDataTree.expandRow(4);
					window.paneDataTree.expandRow(3);
					window.paneDataTree.expandRow(2);
					window.paneDataTree.setEnabled(true);
					window.focus();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public GUI()
	{
		initialize();
	}

	private void initialize()
	{
		// Erstelllung des Frames.
		frame = new JFrame("SQL Code Editor");
		frame.setResizable(false);
		frame.setTitle("SQL Code Editor");
		frame.setBounds(-8, -1, 1936, 1061);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// Erstellung des Dialogs beim Versuch, die Applikation zu schließen.
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				JFrame frame = (JFrame) e.getSource();

				int result = JOptionPane.showConfirmDialog(frame, "Sind Sie sicher, dass Sie die Applikation schließen möchten?", "SQL Code Edior: Warnung", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION)
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});

		// Tabbedpane des Aufgabenbereichs
		paneTabbedAufgabe = new JTabbedPane(JTabbedPane.TOP);
		paneTabbedAufgabe.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				if (!changeable)
				{
					paneTabbedAufgabe.setSelectedIndex(index);
				}
			}
		});
		paneTabbedAufgabe.setBounds(253, 9, 1656, 198);
		paneTabbedAufgabe.setFocusable(false);
		frame.getContentPane().add(paneTabbedAufgabe);

		paneEinführungsaufgabe = new JEditorPane();
		paneEinführungsaufgabe.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paneEinführungsaufgabe.setEditable(false);
		if (english)
		{
			paneTabbedAufgabe.addTab("Introductory Task", null, paneEinführungsaufgabe, null);
		}
		else
		{
			paneTabbedAufgabe.addTab("Einf\u00FChrungsaufgabe", null, paneEinführungsaufgabe, null);
		}

		paneVersicherungsaufgabe = new JEditorPane();
		paneVersicherungsaufgabe.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paneVersicherungsaufgabe.setEditable(false);
		if (english)
		{
			paneVersicherungsaufgabe.setText("Return all customers who are uneconomical from the point of view of a particular insurance agency.\r\n" + "Output: CustomerID, degreeOfUneconomicalness. Consider three variables:\n\nQuotient: The fee defined for a contract added to the surcharge received by a customer divided by the fee of the contract.\r\n" + "In this case, the quotient of a customer must be greater than the average quotient of all customers, otherwise the customer is economical.\nDebt: The sum of the not yet paid requested money of a customer.\r\n" + "In this case, the debt of a customer must be greater than his quotient multiplied by the sum of his paid requested money, otherwise the customer is economical.\r\nLoss: The sum of the costs for all incidents of a customer.\r\n" + "In this case, the loss of a customer must be greater than his debt added to the sum of the values ​​of his securities, otherwise the customer is economical.\r\n" + "\r\n" + "This insurance agency wants to terminate a customer, if its degreeOfUneconomicalness fulfills (quotient * debt + loss) > 150,000.");
		}
		else
		{
			paneVersicherungsaufgabe.setText("Geben Sie alle Kunden aus, die aus Sicht einer bestimmten Versicherungsagentur unwirtschaftlich sind.\r\nAusgabe: KundenID, Unwirtschaftlichkeitsgrad. Ziehen Sie hierf\u00FCr drei Variablen in Betracht:\r\n\r\nQuotient: Der f\u00FCr einen Vertrag definierte Betrag addiert mit dem Aufschlag, den ein Kunde erhalten hat, dividiert durch den Betrag des Vertrages.\r\nDabei muss für den Quotienten eines Kunden gelten, dass er größer als der durchschnittliche Quotient aller Kunden ist, ansonsten ist der Kunde wirtschaftlich.\r\nSchuld: Die Summe des noch nicht beglichenen geforderten Geldes eines Kunden.\r\nDabei muss für die Schuld eines Kunden gelten, dass sie größer als sein Quotient multipliziert mit der Summe seiner geleisteten Betr\u00E4ge ist, ansonsten ist der Kunde wirtschaftlich.\r\nVerlust: Die Summe der Kosten f\u00FCr alle Zwischenf\u00E4lle eines Kunden.\r\nDabei muss für den Verlust eines Kunden gelten, dass er größer als seine Schuld addiert mit der Summe der Werte seiner Wertpapiere ist, ansonsten ist der Kunde wirtschaftlich.\r\n\r\nKündigen will diese Versicherungsagentur nun einen Kunden, wenn sein Unwirtschaftlichkeitsgrad (Quotient * Schuld + Verlust) > 150.000 ist.");
		}
		if (english)
		{
			paneTabbedAufgabe.addTab("Insurance Task", null, paneVersicherungsaufgabe, null);
		}
		else
		{
			paneTabbedAufgabe.addTab("Versicherungsaufgabe", null, paneVersicherungsaufgabe, null);
		}

		paneRennaufgabe = new JEditorPane();
		paneRennaufgabe.setText("Geben Sie alle Rennfahrer aus, die aus Sicht eines bestimmten F1 Teams als w\u00FCrdig erachtet werden, als Fahrer angeworben zu werden.\r\nAusgabe: FahrerID, Attraktivitätsgrad. Ziehen Sie hierf\u00FCr drei Variablen in Betracht:\r\n\r\nSignifikanz: Die Punkte des Teams eines Fahrers addiert mit den Punkten des Fahrers geteilt durch die Punkte seines Teams.\r\nDabei muss für die Signifikanz eines Fahrers gelten, dass sie größer als die durchschnittliche Signifikanz aller Fahrer ist, ansonsten ist der Fahrer unattraktiv.\r\nDifferenz: Die Differenz aus der Summe der von Analysten erwateten Plätze und der Summe der real errungenen Plätze eines Fahrers.\r\nDabei muss für die Differenz eines Fahrers gelten, dass sie größer als die Summe seiner erwarteten Positionen geteilt durch seine Signifikanz ist, ansonsten ist der Fahrer unattraktiv.\r\nPodiumsPunkte: Die im Laufe der Karriere eines Fahrers aufsummierten Punkte durch Podiumspositionen.\r\nDabei muss für die Podiumspunkte eines Fahrers gelten, dass sie addiert mit seiner Differenz gr\u00F6\u00DFer als die Summe der durch Rennunfälle verursachten Kosten des betrachteten Fahrers / 1000000 ist, ansonsten ist der Fahrer unattraktiv.\r\n\r\nInteressiert ist nun dieses Team an einen Fahrer, wenn sein Akktraktivitätsgrad (Signifikanz * Differenz + Podiumspunkte) > 500 ist.");
		paneRennaufgabe.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paneRennaufgabe.setEditable(false);
		if (english)
		{
			paneTabbedAufgabe.addTab("Racing Team Task", null, paneRennaufgabe, null);
		}
		else
		{
			paneTabbedAufgabe.addTab("Rennteamaufgabe", null, paneRennaufgabe, null);
		}

		defaultColor = paneTabbedAufgabe.getBackgroundAt(0);

		// Knöpfe
		if (english)
		{
			buttonAnfrageParsen = new JButton("Translate Query (F4)");
		}
		else
		{
			buttonAnfrageParsen = new JButton("Anfrage parsen (F4)");
		}
		buttonAnfrageParsen.setBounds(426, 781, 210, 21);
		buttonAnfrageParsen.setFont(new Font("Tahoma", Font.PLAIN, 13));

		if (english)
		{
			buttonAnfrageAbschicken = new JButton("Execute Query (F5)");
		}
		else
		{
			buttonAnfrageAbschicken = new JButton("Anfrage abschicken (F5)");
		}
		buttonAnfrageAbschicken.setFont(new Font("Tahoma", Font.PLAIN, 13));
		buttonAnfrageAbschicken.setBounds(644, 781, 210, 21);

		if (english)
		{
			buttonAnfrageLeeren = new JButton("Clear Query (F6)");
		}
		else
		{
			buttonAnfrageLeeren = new JButton("Anfrage leeren (F6)");
		}
		buttonAnfrageLeeren.setFont(new Font("Tahoma", Font.PLAIN, 13));
		buttonAnfrageLeeren.setBounds(862, 781, 212, 21);

		if (english)
		{
			buttonNächsteAufgabe = new JButton("Next Task");
		}
		else
		{
			buttonNächsteAufgabe = new JButton("N\u00E4chste Aufgabe");
		}

		buttonNächsteAufgabe.setFont(new Font("Tahoma", Font.PLAIN, 13));
		if (english)
		{
			buttonNächsteAufgabe.setBounds(1742, 790, 167, 21);
		}
		else
		{
			buttonNächsteAufgabe.setBounds(1730, 790, 179, 21);

		}
		buttonNächsteAufgabe.setEnabled(false);

		frame.getContentPane().add(buttonAnfrageAbschicken);
		frame.getContentPane().add(buttonAnfrageParsen);
		frame.getContentPane().add(buttonAnfrageLeeren);
		frame.getContentPane().add(buttonNächsteAufgabe);
		frame.getContentPane().setLayout(null);

		// EventListener für die Knöpfe
		buttonAnfrageParsen.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent click)
			{
				if (finished)
				{
					return;
				}
				clear();
				focus();
				parse();
				feedback(false);
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				focus();
			}
		});

		buttonAnfrageAbschicken.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (finished)
				{
					return;
				}
				focus();
				parse();
				create(null);
				display();
				feedback(true);

			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				focus();
			}
		});

		buttonAnfrageLeeren.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (finished)
				{
					return;
				}
				focus();
				clear();
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				focus();
			}
		});

		buttonNächsteAufgabe.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (finished)
				{
					return;
				}
				nächsteAufgabe();
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				focus();
			}
		});

		scrollPaneTable = new JScrollPane();
		paneQueryError = new JEditorPane();
		paneQueryError.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paneQueryError.setEditable(false);
		paneQueryError.setBackground(SystemColor.controlHighlight);
		scrollPaneTable.setViewportView(paneQueryError);

		paneTabbedTable = new JTabbedPane(JTabbedPane.TOP);
		paneTabbedTable.setBounds(253, 789, 1656, 198);
		paneTabbedTable.setFocusable(false);
		if (english)
		{
			paneTabbedTable.addTab("Output Area for Tables", null, scrollPaneTable, null);
		}
		else
		{
			paneTabbedTable.addTab("Ausgabefeld für Tabellen", null, scrollPaneTable, null);
		}

		frame.getContentPane().add(paneTabbedTable);

		// Tree zur Darstellung der Tabellen und deren Spalten
		JScrollPane paneScrollDataTree = new JScrollPane();
		paneScrollDataTree.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		paneScrollDataTree.setBounds(10, 11, 235, 977);
		frame.getContentPane().add(paneScrollDataTree);

		UIManager.put("Tree.rendererFillBackground", false);
		paneDataTree = new JTree();
		paneDataTree.setBackground(SystemColor.controlHighlight);
		paneDataTree.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Tabellen Explorer", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		paneDataTree.setFont(new Font("Tahoma", Font.PLAIN, 15));
		paneDataTree.setShowsRootHandles(true);

		paneScrollDataTree.setViewportView(paneDataTree);

		if (english)
		{
			paneDataTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Databases")
			{
				{
					DefaultMutableTreeNode node_1;
					DefaultMutableTreeNode node_2;
					node_1 = new DefaultMutableTreeNode("Introductory Task");
					node_2 = new DefaultMutableTreeNode("Mitarbeiter");
					node_2.add(new DefaultMutableTreeNode("MitarbeiterID"));
					node_2.add(new DefaultMutableTreeNode("Lohn"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("ArbeitetAn");
					node_2.add(new DefaultMutableTreeNode("MitarbeiterID"));
					node_2.add(new DefaultMutableTreeNode("ProjektID"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Projekt");
					node_2.add(new DefaultMutableTreeNode("ProjektID"));
					node_2.add(new DefaultMutableTreeNode("Beschreibung"));
					node_1.add(node_2);
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Insurance Task");
					node_2 = new DefaultMutableTreeNode("Customer");
					node_2.add(new DefaultMutableTreeNode("CustomerID"));
					node_2.add(new DefaultMutableTreeNode("ContractID"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Contract");
					node_2.add(new DefaultMutableTreeNode("ContractID"));
					node_2.add(new DefaultMutableTreeNode("Fee"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Surcharge");
					node_2.add(new DefaultMutableTreeNode("CustomerID"));
					node_2.add(new DefaultMutableTreeNode("Fee"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Incident");
					node_2.add(new DefaultMutableTreeNode("IncidentID"));
					node_2.add(new DefaultMutableTreeNode("CustomerID"));
					node_2.add(new DefaultMutableTreeNode("Cost"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Security");
					node_2.add(new DefaultMutableTreeNode("SecurityID"));
					node_2.add(new DefaultMutableTreeNode("CustomerID"));
					node_2.add(new DefaultMutableTreeNode("Value"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Request");
					node_2.add(new DefaultMutableTreeNode("CustomerID"));
					node_2.add(new DefaultMutableTreeNode("Date"));
					node_2.add(new DefaultMutableTreeNode("RequestedMoney"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Payment");
					node_2.add(new DefaultMutableTreeNode("CustomerID"));
					node_2.add(new DefaultMutableTreeNode("Date"));
					node_2.add(new DefaultMutableTreeNode("PayedMoney"));
					node_1.add(node_2);
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Racing Team Task");
					node_2 = new DefaultMutableTreeNode("Fahrer");
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("TeamID"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Team");
					node_2.add(new DefaultMutableTreeNode("TeamID"));
					node_2.add(new DefaultMutableTreeNode("Punkte"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Wertung");
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("Punkte"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Rennen");
					node_2.add(new DefaultMutableTreeNode("RennenID"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Rennunfall");
					node_2.add(new DefaultMutableTreeNode("RennunfallID"));
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("Kosten"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Podiumsposition");
					node_2.add(new DefaultMutableTreeNode("PodiumspositionID"));
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("Punkte"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Erwartung");
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("RennenID"));
					node_2.add(new DefaultMutableTreeNode("ErwartetePosition"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Realit\u00E4t");
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("RennenID"));
					node_2.add(new DefaultMutableTreeNode("ErrungenePosition"));
					node_1.add(node_2);
					add(node_1);
				}
			}));
		}
		else
		{
			paneDataTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Datenbanken")
			{
				{
					DefaultMutableTreeNode node_1;
					DefaultMutableTreeNode node_2;
					node_1 = new DefaultMutableTreeNode("Einf\u00FChrungsaufgabe");
					node_2 = new DefaultMutableTreeNode("Mitarbeiter");
					node_2.add(new DefaultMutableTreeNode("MitarbeiterID"));
					node_2.add(new DefaultMutableTreeNode("Lohn"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("ArbeitetAn");
					node_2.add(new DefaultMutableTreeNode("MitarbeiterID"));
					node_2.add(new DefaultMutableTreeNode("ProjektID"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Projekt");
					node_2.add(new DefaultMutableTreeNode("ProjektID"));
					node_2.add(new DefaultMutableTreeNode("Beschreibung"));
					node_1.add(node_2);
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Versicherungsaufgabe");
					node_2 = new DefaultMutableTreeNode("Kunde");
					node_2.add(new DefaultMutableTreeNode("KundenID"));
					node_2.add(new DefaultMutableTreeNode("VertragsID"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Vertrag");
					node_2.add(new DefaultMutableTreeNode("VertragsID"));
					node_2.add(new DefaultMutableTreeNode("Betrag"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Aufschlag");
					node_2.add(new DefaultMutableTreeNode("KundenID"));
					node_2.add(new DefaultMutableTreeNode("Betrag"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Zwischenfall");
					node_2.add(new DefaultMutableTreeNode("ZwischenfallID"));
					node_2.add(new DefaultMutableTreeNode("KundenID"));
					node_2.add(new DefaultMutableTreeNode("Kosten"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Wertpapier");
					node_2.add(new DefaultMutableTreeNode("WertpapierID"));
					node_2.add(new DefaultMutableTreeNode("KundenID"));
					node_2.add(new DefaultMutableTreeNode("Wert"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Forderung");
					node_2.add(new DefaultMutableTreeNode("KundenID"));
					node_2.add(new DefaultMutableTreeNode("Datum"));
					node_2.add(new DefaultMutableTreeNode("Geforderterbetrag"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Zahlung");
					node_2.add(new DefaultMutableTreeNode("KundenID"));
					node_2.add(new DefaultMutableTreeNode("Datum"));
					node_2.add(new DefaultMutableTreeNode("Geleisteterbetrag"));
					node_1.add(node_2);
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Rennteamaufgabe");
					node_2 = new DefaultMutableTreeNode("Fahrer");
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("TeamID"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Team");
					node_2.add(new DefaultMutableTreeNode("TeamID"));
					node_2.add(new DefaultMutableTreeNode("Punkte"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Wertung");
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("Punkte"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Rennen");
					node_2.add(new DefaultMutableTreeNode("RennenID"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Rennunfall");
					node_2.add(new DefaultMutableTreeNode("RennunfallID"));
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("Kosten"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Podiumsposition");
					node_2.add(new DefaultMutableTreeNode("PodiumspositionID"));
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("Punkte"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Erwartung");
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("RennenID"));
					node_2.add(new DefaultMutableTreeNode("ErwartetePosition"));
					node_1.add(node_2);
					node_2 = new DefaultMutableTreeNode("Realit\u00E4t");
					node_2.add(new DefaultMutableTreeNode("FahrerID"));
					node_2.add(new DefaultMutableTreeNode("RennenID"));
					node_2.add(new DefaultMutableTreeNode("ErrungenePosition"));
					node_1.add(node_2);
					add(node_1);
				}
			}));
		}
		paneDataTree.expandRow(1);

		paneTabbedUnparsed = new JTabbedPane(JTabbedPane.TOP);
		paneTabbedUnparsed.setBounds(253, 215, 821, 567);
		paneTabbedUnparsed.setFocusable(false);
		frame.getContentPane().add(paneTabbedUnparsed);

		paneScrollUnparsed = new JScrollPane();
		if (english)
		{
			paneTabbedUnparsed.addTab("Input Area for Queries", null, paneScrollUnparsed, null);
		}
		else
		{
			paneTabbedUnparsed.addTab("Eingabefeld f\u00FCr Queries", null, paneScrollUnparsed, null);
		}
		paneScrollUnparsed.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		paneScrollUnparsed.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		paneQueryUnparsed = new JEditorPane();
		Document docUnparsed = paneQueryUnparsed.getDocument();
		if (docUnparsed instanceof PlainDocument)
		{
			docUnparsed.putProperty(PlainDocument.tabSizeAttribute, 4);
		}
		paneScrollUnparsed.setViewportView(paneQueryUnparsed);
		paneQueryUnparsed.setFont(new Font("Tahoma", Font.PLAIN, 12));

		paneTabbedParsed = new JTabbedPane(JTabbedPane.TOP);
		paneTabbedParsed.setBounds(1084, 215, 825, 567);
		paneTabbedParsed.setFocusable(false);
		frame.getContentPane().add(paneTabbedParsed);

		paneScrollParsed = new JScrollPane();
		if (english)
		{
			paneTabbedParsed.addTab("Output Area for Translated Queries", null, paneScrollParsed, null);
		}
		else
		{
			paneTabbedParsed.addTab("Ausgabefeld f\u00FCr geparste Queries", null, paneScrollParsed, null);
		}
		paneScrollParsed.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollParsed.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		paneQueryParsed = new JEditorPane();
		Document docParsed = paneQueryParsed.getDocument();
		if (docParsed instanceof PlainDocument)
		{
			docParsed.putProperty(PlainDocument.tabSizeAttribute, 4);
		}
		paneScrollParsed.setViewportView(paneQueryParsed);
		paneQueryParsed.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paneQueryParsed.setEditable(false);

		// Textbereich, in dem der Status der momentanen Aufgabe angezeigt wird
		JLayeredPane panefeedback = new JLayeredPane();
		panefeedback.setBounds(1084, 781, 639, 21);
		frame.getContentPane().add(panefeedback);
		paneStatus = new JFormattedTextField();
		paneStatus.setFont(new Font("Tahoma", Font.PLAIN, 13));
		paneStatus.setBackground(SystemColor.controlHighlight);
		panefeedback.setLayer(paneStatus, 2);
		paneStatus.setText(" Status: Bitte versuche, die angegebene Aufgabe zu l\u00F6sen");
		paneStatus.setBounds(0, 0, 639, 22);
		panefeedback.add(paneStatus);

		// Undo und Redo manager im editierbaren Textfeld
		UndoManager undoManager = new UndoManager();
		paneQueryUnparsed.getDocument().addUndoableEditListener(undoManager);

		// Menü
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		// Menüitems
		if (english)
		{
			menuDatei = new JMenu("File");
			menuBearbeiten = new JMenu("Edit");
			menuEinstellungen = new JMenu("Properties");
			menuHilfe = new JMenu("Help");
		}
		else
		{
			menuDatei = new JMenu("Datei");
			menuBearbeiten = new JMenu("Bearbeiten");
			menuEinstellungen = new JMenu("Einstellungen");
			menuHilfe = new JMenu("Hilfe");
		}

		menuBar.add(menuDatei);
		menuBar.add(menuBearbeiten);
		menuBar.add(menuEinstellungen);
		menuBar.add(menuHilfe);

		// Menüitemeinträge
		menuItemAnfrageParsen = new JMenuItem("Anfrage parsen   ");
		menuItemAnfrageAbschicken = new JMenuItem("Anfrage abschicken   ");
		menuItemAnfrageLeeren = new JMenuItem("Anfrage leeren   ");
		separatorDatei1 = new JSeparator();
		menuItemBeenden = new JMenuItem("Beenden   ");
		menuItemUndo = new JMenuItem("R\u00FCckg\u00E4ngig   ");
		menuItemRedo = new JMenuItem("Wiederherstellen   ");
		menuItemPause = new JMenuItem("Session Pausieren   ");
		menuItemSkipTask = new JMenuItem("Aufgabe Skippen   ");
		menuItemReset = new JMenuItem("Reset   ");
		menuItemReset.setEnabled(false);
		menuItemInformation = new JMenuItem("Information   ");

		menuDatei.add(menuItemAnfrageParsen);
		menuDatei.add(menuItemAnfrageAbschicken);
		menuDatei.add(menuItemAnfrageLeeren);
		menuDatei.add(separatorDatei1);
		menuDatei.add(menuItemBeenden);
		menuBearbeiten.add(menuItemUndo);
		menuBearbeiten.add(menuItemRedo);
		menuEinstellungen.add(menuItemPause);
		menuEinstellungen.add(menuItemSkipTask);
		menuEinstellungen.add(menuItemReset);
		menuHilfe.add(menuItemInformation);

		// EventListener für Menüitemeinträge
		menuItemAnfrageParsen.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				if (finished)
				{
					return;
				}
				clear();
				parse();
				feedback(false);
			}
		});
		menuItemAnfrageParsen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));

		menuItemAnfrageAbschicken.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				if (finished)
				{
					return;
				}
				parse();
				create(null);
				display();
				feedback(true);
			}
		});
		menuItemAnfrageAbschicken.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));

		menuItemAnfrageLeeren.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				if (finished)
				{
					return;
				}
				clear();
			}
		});
		menuItemAnfrageLeeren.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));

		menuItemBeenden.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				int result = JOptionPane.showConfirmDialog(frame, "Sind sie sicher, dass Sie die Applikation beenden möchten?", "SQL Code Edior: Warnung", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION)
				{
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					System.exit(0);
				}
			}
		});

		menuItemUndo.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (undoManager.canUndo() && !finished)
				{
					undoManager.undo();
				}
			}
		});
		menuItemUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		menuItemRedo.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (undoManager.canRedo() && !finished)
				{
					undoManager.redo();
				}
			}
		});
		menuItemRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		menuItemPause.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (!finished)
				{
					pause();
				}
			}
		});
		menuItemPause.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		menuItemSkipTask.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (!finished)
				{
					int result = skipWarnung();
					if (result == JOptionPane.YES_OPTION)
					{
						skip();
						nächsteAufgabe();
					}
				}
			}
		});
		menuItemSkipTask.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		menuItemReset.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (finished)
				{
					int result = resetWarnung();
					if (result == JOptionPane.YES_OPTION)
					{
						reset();
					}
				}
			}
		});
		menuItemReset.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		menuItemInformation.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (!finished)
				{
					aufgabenID();
					information(id);
				}
			}
		});
		menuItemInformation.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));

		// Hotkeys für redo und undo
		paneQueryUnparsed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "undoKeyStroke");
		paneQueryUnparsed.getActionMap().put("undoKeyStroke", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (undoManager.canUndo() && !finished)
				{
					undoManager.undo();
				}
			}
		});

		paneQueryUnparsed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "redoKeyStroke");
		paneQueryUnparsed.getActionMap().put("redoKeyStroke", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (undoManager.canRedo() && !finished)
				{
					undoManager.redo();
				}
			}
		});

		// Hotkeys zum parsen, abschicken und leeren der Abfrage
		paneQueryParsed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(parseKeyStroke, "parseKeyStroke");
		paneQueryParsed.getActionMap().put("parseKeyStroke", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (finished)
				{
					return;
				}
				clear();
				focus();
				parse();
				feedback(false);
			}
		});

		paneQueryUnparsed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(executeKeyStroke, "executeKeyStroke");
		paneQueryUnparsed.getActionMap().put("executeKeyStroke", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (finished)
				{
					return;
				}
				focus();
				parse();
				create(null);
				display();
				feedback(true);
			}
		});

		paneQueryUnparsed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(clearKeyStroke, "clearKeyStroke");
		paneQueryUnparsed.getActionMap().put("clearKeyStroke", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (finished)
				{
					return;
				}
				focus();
				clear();
			}
		});

		// Hotkey zum Pausieren, Skippen und Resetten
		paneQueryUnparsed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(pauseKeyStroke, "pauseKeyStroke");
		paneQueryUnparsed.getActionMap().put("pauseKeyStroke", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (!finished)
				{
					pause();
				}
			}
		});

		paneQueryUnparsed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(skipTaskKeyStroke, "skipTaskKeyStroke");
		paneQueryUnparsed.getActionMap().put("skipTaskKeyStroke", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (!finished)
				{
					int result = skipWarnung();
					if (result == JOptionPane.YES_OPTION)
					{
						skip();
						nächsteAufgabe();
					}
				}
			}
		});

		paneQueryUnparsed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(resetKeyStroke, "resetKeyStroke");
		paneQueryUnparsed.getActionMap().put("resetKeyStroke", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (finished)
				{
					int result = resetWarnung();
					if (result == JOptionPane.YES_OPTION)
					{
						reset();
					}
				}
			}
		});

		// Hotkey zum Abrufen des Informations Dialoges
		paneQueryUnparsed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(informationKeyStroke, "informationKeyStore");
		paneQueryUnparsed.getActionMap().put("informationKeyStore", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (finished)
				{
					return;
				}
				aufgabenID();
				information(id);
			}
		});
	}

	// If expand is true, expands all nodes in the tree. Otherwise, collapses all nodes in the tree.
	public static void expandAll(JTree tree, boolean expand)
	{
		TreeNode root = (TreeNode) tree.getModel().getRoot();

		// Traverse tree from root
		expandAll(tree, new TreePath(root), expand);
	}

	private static void expandAll(JTree tree, TreePath parent, boolean expand)
	{
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0)
		{
			for (Enumeration e = node.children(); e.hasMoreElements();)
			{
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand)
		{
			tree.expandPath(parent);
		}
		else
		{
			tree.collapsePath(parent);
		}
	}

	// Fokus und Selektion beibehalten.
	public void focus()
	{
		String query = "";
		int start = paneQueryUnparsed.getSelectionStart();
		int end = paneQueryUnparsed.getSelectionEnd();

		if (paneQueryUnparsed.getSelectedText() != null)
		{
			paneQueryUnparsed.setSelectionStart(start);
			paneQueryUnparsed.setSelectionEnd(end);
			try
			{
				selectedText = paneQueryUnparsed.getText(start, end - start);
			}
			catch (BadLocationException e)
			{
				System.out.println(e.getMessage());
			}
		}
		paneQueryUnparsed.requestFocusInWindow();
	}

	// Überprüfung, ob übersetzter Ausdruck legales SQL Statement ist.
	public boolean executable(String query)
	{
		SQL sql = new SQL(query);
		if (sql.getE() == null)
		{
			return true;
		}
		else if (query.equals(""))
		{
			return false;
		}
		else if (sql.getE().getMessage().equals("Die Abfrage lieferte kein Ergebnis."))
		{
			paneQueryError = new JEditorPane();
			paneQueryError.setFont(new Font("Tahoma", Font.PLAIN, 12));
			paneQueryError.setEditable(false);
			paneQueryError.setText("FEHLER: Syntaxfehler\n   Leere Anfragen können nicht ausgeführt werden");
			paneQueryError.setBackground(SystemColor.controlHighlight);
			scrollPaneTable.setViewportView(paneQueryError);
			return false;
		}
		else if (query.contains("FEHLER: Syntaxfehler"))
		{
			paneQueryError = new JEditorPane();
			paneQueryError.setFont(new Font("Tahoma", Font.PLAIN, 12));
			paneQueryError.setEditable(false);
			paneQueryError.setText(query);
			scrollPaneTable.setViewportView(paneQueryError);
			paneQueryError.setBackground(SystemColor.controlHighlight);
			return false;
		}
		else
		{
			paneQueryError = new JEditorPane();
			paneQueryError.setFont(new Font("Tahoma", Font.PLAIN, 12));
			paneQueryError.setEditable(false);
			paneQueryError.setText(sql.getE().getMessage());
			paneQueryError.setBackground(SystemColor.controlHighlight);
			scrollPaneTable.setViewportView(paneQueryError);
			return false;
		}
	}

	// Übersetzung der Eingabe
	public void parse()
	{
		String query = "";
		if (paneQueryUnparsed.getSelectedText() != null)
		{
			query = Parser.parse(paneQueryUnparsed.getSelectedText());
			if (executable(query))
			{
				executable = true;
				paneQueryParsed.setText(query);
			}
			else
			{
				paneQueryParsed.setText("");
				executable = false;
			}
		}
		else
		{
			query = Parser.parse(paneQueryUnparsed.getText());
			if (executable(query))
			{
				executable = true;
				paneQueryParsed.setText(query);
			}
			else
			{
				paneQueryParsed.setText("");
				executable = false;
			}
		}
	}

	// Daten Einfügen
	public void insert(String query)
	{
		SQL sql = new SQL(query);
	}

	// Erzeugen des Ergebnisqueries als zweidimensionales Array
	public void create(String lastQuery)
	{
		String query = paneQueryParsed.getText();
		boolean notEmpty = !query.isEmpty();

		// Falls fertig soll Zeiten des Subjekts in Datenbank geschrieben werden
		if (lastQuery != null && !vorletzte)
		{
			SQL sql = new SQL(lastQuery);
			vorletzte = true;
			return;
		}
		if (lastQuery != null && vorletzte)
		{
			executable = true;
			query = lastQuery;
			notEmpty = true;
		}

		// Extraktion der Informationen aus dem Query
		if (executable && notEmpty)
		{
			SQL sql = new SQL(query);
			ResultSet resultSet = sql.getResultSet();
			try
			{
				ResultSetMetaData rsmd = resultSet.getMetaData();
				int columnCount = rsmd.getColumnCount();

				// Überprüfen, ob Ausgabe mit erwarteter Struktur des Ergebnisqueries übereinstimmt
				if (columnCount == 2 && ("" + rsmd.getColumnTypeName(1)).equals("int4") && ("" + rsmd.getColumnTypeName(2)).equals("numeric"))
				{
					isSolution = true;
				}
				else
				{
					isSolution = false;
				}

				int rowCount = resultSet.last() ? resultSet.getRow() : 0;
				resultSet.beforeFirst();
				columnNames = new String[columnCount];
				data = new String[rowCount][columnCount];
				for (int i = 0; i < columnCount; i++)
				{
					String name = ("" + rsmd.getColumnLabel(i + 1).charAt(0)).toUpperCase();
					name = name + rsmd.getColumnLabel(i + 1).substring(1);
					String type = rsmd.getColumnTypeName(i + 1);
					if (type.equals("int4"))
					{
						type = "int";
					}
					else if (type.equals("varchar"))
					{
						type = "time";
					}
					columnNames[i] = name + " (" + type + ")";
				}
				while (resultSet.next())
				{
					for (int i = 0; i < columnCount; i++)
					{
						data[resultSet.getRow() - 1][i] = resultSet.getString(i + 1);
					}
				}
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
				executable = false;
			}
		}
	}

	// Darstellung des Ergebnisses des erhaltenen Ergebnisqueries als Tabelle
	public void display()
	{
		if (!executable)
		{
			return;
		}
		tableResult = new JTable(data, columnNames);
		tableResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableResult.setGridColor(Color.BLACK);
		tableResult.setColumnSelectionAllowed(true);
		tableResult.setCellSelectionEnabled(true);
		tableResult.setFocusable(false);
		tableResult.setRowSelectionAllowed(true);
		scrollPaneTable.setViewportView(tableResult);
		DefaultTableModel model = new DefaultTableModel(data, columnNames)
		{
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		TableCellRenderer renderer = tableResult.getTableHeader().getDefaultRenderer();
		JLabel label = (JLabel) renderer;
		label.setHorizontalAlignment(JLabel.LEFT);
		tableResult.setModel(model);
	}

	// Ausgabe leeren
	public void clear()
	{
		paneQueryError.setText("");
		paneQueryError.setBackground(SystemColor.controlHighlight);
		paneQueryParsed.setText("");
		paneStatus.setText(" Status: Bitte versuche, die Aufgabe zu lösen");
		paneStatus.setBackground(SystemColor.controlHighlight);
		scrollPaneTable.setViewportView(paneQueryError);
		paneStatus.setText(" Status: Bitte versuche, die angegebene Aufgabe zu lösen");
	}

	// Prüfen, ob Ausgabe die gewünschte Ausgabe ist
	public void isSolution(String query)
	{
		if (executable && isSolution)
		{
			query = query.trim().replaceAll("\\s{2,}", " ").toLowerCase();
			if (!query.contains(";"))
			{
				query = query + ";";
			}
			query = query.replace("( ", "(").replace(" )", ")");

			// Order By befehl entfernt werden, damit die folgende Überprüfung funktioniert (Thx Tristan)
			while (query.contains("order by"))
			{
				int posOfOrderByStart = query.indexOf("order by");
				int posOfOrderByEndLimit = query.indexOf("limit", posOfOrderByStart);
				int posOfOrderByEndBracket = query.indexOf(")", posOfOrderByStart);
				int posOfOrderByEndEnd = query.length();

				if (posOfOrderByEndLimit == -1)
				{
					posOfOrderByEndLimit = Integer.MAX_VALUE;
				}
				if (posOfOrderByEndBracket == -1)
				{
					posOfOrderByEndBracket = Integer.MAX_VALUE;
				}

				int posOfTrim = 0;
				if (posOfOrderByEndLimit < posOfOrderByEndBracket && posOfOrderByEndLimit < posOfOrderByEndEnd)
				{
					posOfTrim = posOfOrderByEndLimit;
				}
				else if (posOfOrderByEndBracket < posOfOrderByEndLimit && posOfOrderByEndBracket < posOfOrderByEndEnd)
				{
					posOfTrim = posOfOrderByEndBracket + 1;
				}
				else if (posOfOrderByEndEnd < posOfOrderByEndLimit && posOfOrderByEndEnd < posOfOrderByEndBracket)
				{
					posOfTrim = posOfOrderByEndEnd;
				}
				String queryTmp = query.substring(posOfOrderByStart, posOfTrim - 1);

				query = query.replace(" " + queryTmp, "");
			}
			query = query.replace(';', ' ').stripTrailing();

			int index = paneTabbedAufgabe.getSelectedIndex();
			System.out.println(index);
			if (index == 0)
			{
				String lösung = "select mitarbeiterid, lohn from mitarbeiter natural join arbeitetan natural join (select projektid from arbeitetan natural join projekt group by projektid having count(mitarbeiterid) < 5) p";
				SQL sql = new SQL("( " + "(" + query + ")" + " EXCEPT " + lösung + ") UNION (" + lösung + " EXCEPT " + "(" + query + ")" + " )");
				ResultSet set = sql.getResultSet();
				try
				{
					if (set.next())
					{
						isSolution = false;
					}
				}
				catch (SQLException e)
				{
					System.out.println(e.getMessage());
				}
			}
			else if (index == 1)
			{
				String lösung = "SELECT 159 AS kundenID, 178145.532756741250876007 AS Unwirtschaftlichkeitsgrad";
				SQL sql = new SQL("( " + "(" + query + ")" + " EXCEPT " + lösung + ") UNION (" + lösung + " EXCEPT " + "(" + query + ")" + " )");
				ResultSet set = sql.getResultSet();
				try
				{
					if (set.next())
					{
						isSolution = false;
					}
				}
				catch (SQLException e)
				{
					System.out.println(e.getMessage());
				}
			}
			else if (index == 2)
			{
				String lösung = "(select 27, 765.67 union select 34, 747.830625 union select 65, 1306.9)";
				SQL sql = new SQL("( " + "(" + query + ")" + " EXCEPT " + lösung + ") UNION (" + lösung + " EXCEPT " + "(" + query + ")" + " )");
				ResultSet set = sql.getResultSet();
				try
				{
					if (set.next())
					{
						isSolution = false;
					}
				}
				catch (SQLException e)
				{
					System.out.println(e.getMessage());
				}
			}
			else
			{
				isSolution = false;
			}
		}
	}

	// Rückmeldung, ob richtig, falsch oder fehlerhaft
	public void feedback(boolean status)
	{
		isSolution(paneQueryParsed.getText());

		// Grau (Keine Anfrage)
		if (paneQueryUnparsed.getText().isEmpty())
		{

		}

		// Rot (Syntaxfehler)
		else if (!executable)
		{
			paneStatus.setText(" Status: Es konnte aufgrund eines Syntaxfehlers keine Ausgabe generiert werden, nähere Informationen unten");
			paneQueryError.setBackground(new Color(240, 128, 128));
		}

		else if (!status)
		{
			return;
		}

		// Orange (Falsche Ausgabe)
		else if (executable && !isSolution)
		{
			if (english)
			{
				paneStatus.setText(" Status: The retrieved table does not match the expected solution");
			}
			else
			{
				paneStatus.setText(" Status: Die zurückgegebene Ergebnisrelation enspricht nicht der richtigen Lösung");
			}
			tableResult.setBackground(new Color(255, 165, 0));
		}

		// Grün (Richtige Ausgabe)
		else if (executable && isSolution)
		{
			String ausgabe = "Status: Die Ausgabe ist richtig! Fahre bitte mit der nächsten Aufgabe fort";
			if (paneTabbedAufgabe.getSelectedIndex() == 0)
			{
				ausgabe = " Status: Die Ausgabe ist richtig! Fahre bitte mit der nächsten Aufgabe fort, falls beide Ansätze genutzt wurden";
			}
			paneStatus.setText(ausgabe);
			tableResult.setBackground(new Color(152, 251, 152));
			buttonNächsteAufgabe.setEnabled(true);
		}
	}

	// Welche aufgabenID hat das Subjekt
	public void aufgabenID()
	{
		executable = true;
		vorletzte = true;
		create("SELECT COUNT(*) FROM zeit");
		executable = false;
		vorletzte = false;
		if (Integer.valueOf(data[0][0]) % 2 == 0)
		{
			id = 0;
		}
		else
		{
			id = 1;
		}
	}

	// Informationen für den User anzeigen lassen
	public void information(int id)
	{
		String typ0 = "Für Sie gilt, dass Sie die Versicherungsaufgabe mit der regulären SQL-Syntax und die Rennteamaufgabe mit der erweiterten SQL-Syntax zu bearbeiten haben.";
		String typ1 = "";
		if (english)
		{
			typ1 = "You will have to solve the insurance task with use of the extended SQL syntax and the racing team task with the regular SQL syntax.";
		}
		else
		{
			typ1 = "Für Sie gilt, dass Sie die Versicherungsaufgabe mit der erweiterten SQL-Syntax und die Rennteamaufgabe mit der regulären SQL-Syntax zu bearbeiten haben.";
		}

		String typ = "";
		if (id == 0)
		{
			typ = typ0;
		}
		else
		{
			typ = typ1;
		}
		if (english)
		{
			JOptionPane.showMessageDialog(frame, "With the help of this tool it is possible to modularize SQL queries. Queries can be saved as variables and referenced in later queries.\r\n" + "Your task is to first work on an introductory task and then to solve the two actual tasks. One with, and one without this extended SQL syntax.\r\n" + "Comments in the code are not supported.\r\n" + "The Table Explorer on the left shows the individual tables needed to solve the tasks and their associated attributes.\r\n" + "If you have questions about the usual SQL syntax, you are free to independently clarify these on the Internet.\r\n" + "If you have questions about the new syntax or the task, you can always consult the supervisor.\r\n" + "\r\n" + "Important: " + typ + "\n" + "Here are two small examples of how to use this extended SQL syntax:\n\n" + "Name1 = SELECT id, salary FROM Employee;                                                                Name1 = SELECT MAX(salary) FROM Employee;\nName2 = SELECT id, firstname FROM Person;                                                               Name2 = SELECT * FROM Employee WHERE salary IN Name1;\nName3 = SELECT * FROM Name1 NATURAL JOIN Name2;\r\n" + "\r\n" + "By pressing the F1 key, or by the menu \"Help\", you can open up this dialog box at any time.\r\n" + "After completion of the insurance task, a break can be taken if required.\r\n" + "If you have any questions, please clarify them now. Clicking \"OK\" will start a timer to record your performance. Good luck!", "Important information for the test subject", JOptionPane.INFORMATION_MESSAGE, null);
		}
		else
		{
			JOptionPane.showMessageDialog(frame, "Mit Hilfe dieses Tools ist es möglich, SQL Anfragen zu modularisieren. Queries können als Variable gespeichert werden, und bei späteren Queries referenziert werden.\nIhre Aufgabe ist es, zunächst eine Einführungsaufgabe zu bearbeiten und danach von den zwei eigentlichen Aufgaben eine mit, und eine ohne diese erweiterte SQL-Syntax zu lösen.\nKommentare im Code werden nicht unterstüzt.\nIm Tabellen Explorer links sind die einzelnen zur Lösung der Aufgaben benötigten Tabellen und ihre zugehörigen Attribute einsehbar.\nIhnen steht es bei Fragen zur gewöhnlichen SQL-Syntax frei, diese im Internet eigenständig zu klären.\nBei Verständnisfragen zur neuen Syntax oder zur Aufgabenstellung können Sie jederzeit die Aufsichtsperson zu Rate ziehen.\n\nWichtig: " + typ + "\nNachfolgend zwei kleine Beispiele, wie diese erweiterte SQL-Syntax eingesetzt werden kann:\n\nName1 = SELECT ID, Lohn FROM Mitarbeiter;                                                                                                                           Name1 = SELECT MAX(Lohn) FROM Mitarbeiter;\nName2 = SELECT ID, Vorname FROM Person;                                                                                                                          Name2 = SELECT * FROM Mitarbeiter WHERE Lohn IN Name1;\nName3 = SELECT * FROM Name1 NATURAL JOIN Name2;\n\nDurch drücken der F1 Taste, oder durch das Menuitem \"Hilfe\" können Sie dieses Dialogfenster jederzeit aufrufen.\nNach Beendigung der Versicherungsaufgabe kann je nach Bedarf eine Pause eingelegt werden.\nFalls Sie noch Fragen haben sollten, so klären Sie sie bitte jetzt. Durch das anschließende Klicken auf \"OK\" beginnt ein Timer, um ihre Leistung aufzuzeichnen. Viel Erfolg!", "Wichtige Informationen für das Testsubjekt", JOptionPane.INFORMATION_MESSAGE, null);
		}
	}

	// Zur nächsten Aufgabe überleiten
	public void nächsteAufgabe()
	{
		focus();
		buttonNächsteAufgabe.setEnabled(false);
		index = paneTabbedAufgabe.getSelectedIndex();

		switch (index)
		{
			case 0:
				startTime1 = System.currentTimeMillis();
				expandAll(paneDataTree, false);
				paneDataTree.expandRow(0);
				paneDataTree.expandRow(2);
				paneDataTree.expandRow(9);
				paneDataTree.expandRow(8);
				paneDataTree.expandRow(7);
				paneDataTree.expandRow(6);
				paneDataTree.expandRow(5);
				paneDataTree.expandRow(4);
				paneDataTree.expandRow(3);
				break;
			case 1:
				pause();
				startTime2 = System.currentTimeMillis();
				expandAll(paneDataTree, false);
				paneDataTree.expandRow(0);
				paneDataTree.expandRow(3);
				paneDataTree.expandRow(11);
				paneDataTree.expandRow(10);
				paneDataTree.expandRow(9);
				paneDataTree.expandRow(8);
				paneDataTree.expandRow(7);
				paneDataTree.expandRow(6);
				paneDataTree.expandRow(5);
				paneDataTree.expandRow(4);
				paneDataTree.expandRow(3);
				break;
			case 2:
				finishTime = System.currentTimeMillis();
				break;
			default:
				System.out.println("Etwas lief gewaltig schief");
		}
		if (index < 2)
		{
			paneStatus.setText(" Status: Bitte versuche, die angegebene Aufgabe zu lösen");
			paneQueryParsed.setText("");
			paneQueryUnparsed.setText("");
			clear();
		}
		else
		{
			// Umstellung auf Endscreen
			finished = true;
			paneStatus.setText(" Status: Sie haben alle Aufgaben gelöst! Ihre Dienste in diesem Experiment werden Ihnen hoch angerechnet :)");
			paneQueryParsed.setText("");;
			paneQueryUnparsed.setText("");
			// paneQueryParsed.setText(" ,:/+/-\r\n" + " /M/ .,-=;//;-\r\n" + " .:/= ;MH/, ,=/+%$XH@MM#@:\r\n" + " -$##@+$###@H@MMM#######H:. -/H#\r\n" + " .,H@H@ X######@ -H#####@+- -+H###@X\r\n" + " .,@##H; +XM##M/, =%@###@X;-\r\n" + "X%- :M##########$. .:%M###@%:\r\n" + "M##H, +H@@@$/-. ,;$M###@%, -\r\n" + "M####M=,,---,.-%%H####M$: ,+@##\r\n" + "@##################@/. :%H##@$-\r\n" + "M###############H, ;HM##M$=\r\n" + "#################. .=$M##M$=\r\n" + "################H..;XM##M$= .:+\r\n" + "M###################@%= =+@MH%\r\n" + "@#################M/. =+H#X%=\r\n" + "=+M###############M, ,/X#H+:,\r\n" + " .;XM###########H= ,/X#H+:;\r\n" + " .=+HM#######M+/+HM@+=.\r\n" + " ,:/%XM####H/.\r\n" + " ,.:=-.");
			// paneQueryUnparsed.setText(" .,-:;//;:=,\r\n" + " . :H@@@MM@M#H/.,+%;,\r\n" + " ,/X+ +M@@M@MM%=,-%HMMM@X/,\r\n" + " -+@MM; $M@@MH+-,;XMMMM@MMMM@+-\r\n" + " ;@M@@M- XM@X;. -+XXXXXHHH@M@M#@/.\r\n" + " ,%MM@@MH ,@%= .---=-=:=,.\r\n" + " -@#@@@MX ., -%HX$$%%%+;\r\n" + " =-./@M@M$ .;@MMMM@MM:\r\n" + " X@/ -$MM/ .+MM@@@M$\r\n" + ",@M@H: :@: . -X#@@@@-\r\n" + ",@@@MMX, . /H- ;@M@M=\r\n" + ".H@@@@M@+, %MM+..%#$.\r\n" + " /MMMM@MMH/. XM@MH; -;\r\n" + " /%+%$XHH@$= , .H@@@@MX,\r\n" + " .=--------. -%H.,@@@@@MX,\r\n" + " .%MM@@@HHHXX$$$%+- .:$MMX -M@@MM%.\r\n" + " =XMMM@MM@MM#H;,-+HMM@M+ /MMMX=\r\n" + " =%@M@M#@$-.=$@MM@@@M; %M%=\r\n" + " ,:+$+-,/H#MMMMMMM@- -,\r\n" + " =++%%%%+/:-.");
			// paneQueryParsed.setFont(new Font("Courier New", Font.PLAIN, 22));
			// paneQueryUnparsed.setFont(new Font("Courier New", Font.PLAIN, 22));
			// paneTabbedParsed.addTab("Cake for the Test Subject", null, paneScrollParsed, null);
			// paneTabbedUnparsed.addTab("Aperture Science", null, paneScrollUnparsed, null);
			paneQueryUnparsed.setEditable(false);
			buttonAnfrageParsen.setEnabled(false);
			buttonAnfrageAbschicken.setEnabled(false);
			buttonAnfrageLeeren.setEnabled(false);
			menuItemAnfrageAbschicken.setEnabled(false);
			menuItemAnfrageParsen.setEnabled(false);
			menuItemAnfrageLeeren.setEnabled(false);
			menuItemRedo.setEnabled(false);
			menuItemUndo.setEnabled(false);
			menuItemPause.setEnabled(false);
			menuItemSkipTask.setEnabled(false);
			menuItemReset.setEnabled(true);
			menuItemInformation.setEnabled(false);

			expandAll(paneDataTree, false);
			paneDataTree.expandRow(0);
			paneDataTree.expandRow(3);
			paneDataTree.expandRow(2);
			paneDataTree.expandRow(1);
			paneDataTree.setEnabled(false);

			// Berechnung der benötigten Zeiten und Einfügung und Selektierung dessen
			String[][] times = new String[4][4];
			times[0][0] = "" + ((startTime1 - startTime0) - (pause0)) / 1000; // Aufgabe1
			times[1][0] = "" + ((startTime2 - startTime1) - (pause1)) / 1000; // Aufgabe2
			times[2][0] = "" + ((finishTime - startTime2) - (pause2)) / 1000; // Aufgabe3
			times[3][0] = "" + ((finishTime - startTime0) - (pause0 + pause1 + pause2)) / 1000; // Summe der Zeiten
			for (int i = 0; i < times.length; i++)
			{
				times[i][1] = "" + Long.valueOf((times[i][0])) % 60; // seconds
				times[i][2] = "" + (Long.valueOf(times[i][0]) / 60) % 60; // minutes
				times[i][3] = "" + Long.valueOf(times[i][0]) / 3600; // hours

				for (int j = 0; j < times[i].length; j++)
				{
					if (Long.valueOf(times[i][j]) < 10)
					{
						times[i][j] = "0" + times[i][j];
					}
				}
			}
			if (skippedTask0)
			{
				times[0][1] += " (Skipped)";
			}
			if (skippedTask1)
			{
				times[1][1] += " (Skipped)";
			}
			if (skippedTask2)
			{
				times[2][1] += " (Skipped)";
			}

			aufgabenID();
			create("insert into zeit(aufgabenid, zeit1, zeit2, zeit3, zeitsumme) values(" + id + ",'" + times[0][3] + ":" + times[0][2] + ":" + times[0][1] + "', '" + times[1][3] + ":" + times[1][2] + ":" + times[1][1] + "', '" + times[2][3] + ":" + times[2][2] + ":" + times[2][1] + "', '" + times[3][3] + ":" + times[3][2] + ":" + times[3][1] + "')");
			create("SELECT * FROM zeit ORDER BY zeitSumme");
			display();

			// Erstellen des letzten Textes
			int id = 0;
			for (int i = 0; i < data.length; i++)
			{
				int inhalt = Integer.valueOf(data[i][0]);
				if (inhalt > id)
				{
					id = inhalt;
				}
			}
			paneLastText = new JEditorPane();
			paneLastText.setFont(new Font("Tahoma", Font.PLAIN, 16));
			paneLastText.setEditable(false);
			paneLastText.setText("Danke für Ihre Partizipation in diesem Experiment!\nIm Ausgabefeld für Tabellen können Sie Ihre benötigte Zeit mit der der anderer Testsubjekte vergleichen.\nSortiert wurde in aufsteigender Ordnung die Zeitsumme für alle drei Aufgaben.\n\nIhnen wurde dabei die TestsubjektID " + id + " zugewiesen.");
			paneTabbedAufgabe.addTab("Schlusswort", null, paneLastText, null);
		}
		index++;
		changeable = true;
		paneTabbedAufgabe.setSelectedIndex(index);
		changeable = false;
		for (int i = 0; i < index; i++)
		{
			paneTabbedAufgabe.setBackgroundAt(i, Color.getHSBColor((float) 0.325, (float) 0.4, (float) 0.8));
		}
	}

	// Pausieren
	public void pause()
	{
		long begin = System.currentTimeMillis();
		JOptionPane.showMessageDialog(frame, "Die Session wurde pausiert und der Timer wird genau dann reaktiviert, sobald dieses Fenster geschlossen wurde.", "Wichtige Informationen für das Testsubjekt", JOptionPane.INFORMATION_MESSAGE, null);
		long ende = System.currentTimeMillis();
		long dauerDerPause = ende - begin;
		index = paneTabbedAufgabe.getSelectedIndex();
		switch (index)
		{
			case 0:
				pause0 += dauerDerPause;
				break;
			case 1:
				pause1 += dauerDerPause;
				break;
			case 2:
				pause2 += dauerDerPause;
				break;
			default:
				System.out.println("Etwas lief gewaltig schief");
		}
	}

	// Dialogfenster, ob wirklich geskippt werden möchte
	public int skipWarnung()
	{
		return JOptionPane.showConfirmDialog(frame, "Sind Sie sicher, dass Sie die momentane Aufgabe überspringen möchten?\nHiervon bitte nur Gebrauch machen, wenn Sie absolut gegen die Wand gefahren sind!\n\n(Sie sitzen seit mehr als einer Stunde an der momentanen Aufgabe und haben kein Land in Sicht)", "SQL Code Editor: Warnung", JOptionPane.YES_NO_OPTION);
	}

	// Eine Aufgabe überspringen
	public void skip()
	{
		index = paneTabbedAufgabe.getSelectedIndex();
		if (index == 0)
		{
			skippedTask0 = true;
		}
		else if (index == 1)
		{
			skippedTask1 = true;
		}
		else if (index == 2)
		{
			skippedTask2 = true;
		}
	}

	// Dialogfenster, ob wirklich resettet werden möchte
	public int resetWarnung()
	{
		return JOptionPane.showConfirmDialog(frame, "Sind Sie sicher, dass Sie die momentane Session resetten möchten?", "SQL Code Editor: Warnung", JOptionPane.YES_NO_OPTION);
	}

	// Das Programm "Resetten"
	public void reset()
	{
		finished = false;
		paneStatus.setText(" Status: Bitte versuche, die angegebene Aufgabe zu lösen");
		paneQueryParsed.setText("");
		paneQueryUnparsed.setText("");
		paneQueryParsed.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paneQueryUnparsed.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paneTabbedParsed.addTab("Ausgabefeld für geparste Queries", null, paneScrollParsed, null);
		paneTabbedUnparsed.addTab("Eingabefeld für Queries", null, paneScrollUnparsed, null);
		paneQueryUnparsed.setEditable(true);
		buttonAnfrageParsen.setEnabled(true);
		buttonAnfrageAbschicken.setEnabled(true);
		buttonAnfrageLeeren.setEnabled(true);
		menuItemAnfrageAbschicken.setEnabled(true);
		menuItemAnfrageParsen.setEnabled(true);
		menuItemAnfrageLeeren.setEnabled(true);
		menuItemRedo.setEnabled(true);
		menuItemUndo.setEnabled(true);
		menuItemPause.setEnabled(true);
		menuItemSkipTask.setEnabled(true);
		menuItemReset.setEnabled(false);
		menuItemInformation.setEnabled(true);
		clear();
		focus();
		changeable = true;
		paneTabbedAufgabe.setSelectedIndex(0);
		changeable = false;
		paneTabbedAufgabe.remove(3);
		for (int i = 0; i < index; i++)
		{
			paneTabbedAufgabe.setBackgroundAt(i, defaultColor);
		}
		index = 0;
		pause0 = 0;
		pause1 = 0;
		pause2 = 0;
		paneEinführungsaufgabe.setText("");
		expandAll(paneDataTree, false);
		paneDataTree.expandRow(0);
		paneDataTree.expandRow(1);
		paneDataTree.expandRow(6);
		paneDataTree.expandRow(5);
		aufgabenID();
		information(id);
		paneEinführungsaufgabe.setText("Ziel dieser ersten Augabe ist es, Sie mit diesem Tool vertraut zu machen. \nDabei soll diese Aufgabe sowohl einmal mit als auch einmal ohne Verwendung der erweiterten SQL-Syntax bearbeitet werden.\n(Klicken Sie also bitte erst auf \"Nächste Aufgabe\", nachdem Sie die Aufgabe mit beiden Ansätzen gelöst haben)\n\nGeben Sie die Mitarbeiter und ihren zugehörigen Lohn für solche Mitarbeiter aus,\ndie an einem Projekt arbeiten, in denen weniger als 5 Personen mitwirken.\nAusgabe: MitarbeiterID, Lohn");
		paneDataTree.expandRow(4);
		paneDataTree.expandRow(3);
		paneDataTree.expandRow(2);
		paneDataTree.setEnabled(true);
		startTime0 = System.currentTimeMillis();
	}
}