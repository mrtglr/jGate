package jGate;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class Driver extends JFrame {
	
	static ArrayList<String> paths = new ArrayList<String>();
	static ArrayList<Folder> folders = new ArrayList<Folder>();
	static String[][] dPaths;
	static DefaultTableModel tableModel;
	static JTable table = null;
	
	Driver() {
		
		JLabel panel = new JLabel();
		this.setTitle("jGate");
		this.setResizable(false);	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(550, 250);		
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		String[] columnNames = { "Name", "Path", "Locked" };
		JFileChooser chooser;
		String choosertitle = null;
		chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle(choosertitle);
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
	    tableModel = new DefaultTableModel(dPaths, columnNames){
	        @Override
	        public boolean isCellEditable(int row, int column) {
	           return false;
	        }
	    };
	    
	    if(dPaths != null) {
	    	table = TableWithColors.createTable(tableModel);
	    	table.setBounds(30, 40, 200, 300);
	    	table.setFont(new Font("Arial", Font.BOLD, 12));
	    }  
	    
	    TableColumnModel columnModel = table.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(0).setMaxWidth(80);
        columnModel.getColumn(1).setPreferredWidth(500);
        columnModel.getColumn(1).setMaxWidth(500);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(2).setMaxWidth(80); 

		JScrollPane sp = new JScrollPane(table);
		sp.getViewport().setBackground(Colors.Background());
		this.add(sp);			    
	    
	    JMenuBar jMenuBar = new JMenuBar();
	    this.setJMenuBar(jMenuBar);
	    
	    JMenu files = new JMenu("Files");
	    jMenuBar.add(files);
	    
	    JMenu locker = new JMenu("Lock/Unlock");
	    jMenuBar.add(locker);
					
		this.setVisible(true);
		this.add(panel);
		
		JMenuItem addFile = new JMenuItem(new AbstractAction("Add") {
	        public void actionPerformed(ActionEvent e) {
	        	
	        	if (chooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) { 
			      String selectedPath =  chooser.getSelectedFile().toString();	
			      
				  if(Helper.writeFile(selectedPath,paths)) {
					getPaths();
		      	  	tableModel.addRow(new String[] { 
		    		    folders.get(folders.size()-1)._name,
		    		    folders.get(folders.size()-1)._path,
		      		    Helper.boolToString(folders.get(folders.size()-1)._locked)});
				  }	      
			    }
			    else {
			      System.out.println("No Selection ");
			    }
	        }
	    }); files.add(addFile);
	    
	    JMenuItem deleteFile = new JMenuItem(new AbstractAction("Delete") {
	        public void actionPerformed(ActionEvent e) {
	        	
	        	new InnerFrame("Delete folder", Helper.listToArray(paths), "Delete", 0);
	        }
	    }); files.add(deleteFile);
	    
	    JMenuItem refresh = new JMenuItem(new AbstractAction("Refresh") {
	        public void actionPerformed(ActionEvent e) {
	        	
	        	getPaths();	        	
				dispose();
				new Driver();
	        }
	    }); files.add(refresh);
	    
	    JMenuItem lockFile = new JMenuItem(new AbstractAction("Lock") {
	        public void actionPerformed(ActionEvent e) {
	        	
	        	new InnerFrame("Lock folder", Helper.UnlockedsToArray(Driver.paths), "Lock", 1);
	        }
	    }); locker.add(lockFile);
	    
	    JMenuItem unlockFile = new JMenuItem(new AbstractAction("Unlock") {
	        public void actionPerformed(ActionEvent e) {
	        	
	        	new InnerFrame("Unlock folder", Helper.LockedsToArray(Driver.paths), "Unlock", 2);
	        }
	    }); locker.add(unlockFile);
	    
	}	
	
	public static void getPaths() {
		paths = Helper.readFile();
	    folders = Helper.generateArray(paths);
	    dPaths = Helper.ListtoDArray(folders);
	}

	public static void refreshFrame(JFrame j) {
		getPaths();	        	
		j.dispose();
		new Driver();
	}
		
	public static void main(String[] args) {
		
		getPaths();
		new Driver();
	}
}
