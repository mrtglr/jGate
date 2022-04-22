package jGate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Helper {
	
	private static  File file = new File("paths.txt");
    private static String path  = file.getPath();
    
    public static ArrayList<Folder> generateArray(ArrayList<String> list) {
		
		ArrayList<Folder> objList = null;
		int size = list.size();		
		
		if(list != null) {
			
			objList = new ArrayList<Folder>();
			
			for(int i=0; i<size; i++) {
				String[] splited_path = (list.get(i).toString()).split("\\\\");
				int spt = splited_path.length;
				boolean locked = stringtoBool(list.get(i).split("-")[0]);
				objList.add(new Folder(splited_path[spt-1], list.get(i).substring(2), locked));
			}
		}
		
		return objList;
	}

	public static String[][] ListtoDArray(ArrayList<Folder> list) {
		
		int size = list.size();
		String[][] tmp_paths = new String[size][4];			
		
		if(list != null) {
			for(int i=0; i<size; i++) {
				Folder tmp_folder = list.get(i);
				tmp_paths[i][0] = tmp_folder._name;
				tmp_paths[i][1] = tmp_folder._path;
				tmp_paths[i][2] = boolToString(tmp_folder._locked);
			}
		}
		
		return tmp_paths;
	}
	
	public static boolean writeFile(String added_path, ArrayList<String> list) {
    	
    	File file = new File(path); 	
		boolean returnFlag = false;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            
            if(file.length()>0) {writer.newLine();}
            if(added_path.charAt(0)=='1' || added_path.charAt(0)=='0') {
            	System.out.println("writing file...");
            	writer.write(added_path);
				returnFlag = true;
            } else {
				if(!isFileExist(added_path, list)){
					System.out.println("writing file...");
					writer.write("0-" + added_path);	
					returnFlag = true;
				} else {
					JOptionPane.showConfirmDialog(null, "File already exist!", "Error", JOptionPane.CLOSED_OPTION);
					returnFlag = false;
				}
				
            }                     
            System.out.println("file write completed");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
		return returnFlag;
    }
	
	public static ArrayList<String> readFile(){
    	
    	File file = new File(path);
    	ArrayList<String> tmp_list = new ArrayList<String>();
    	
    	try {         
    		if(file.length()>0) {
				System.out.println("reading file...");   		
				Scanner read = new Scanner(file);
				while(read.hasNextLine()) {
					String line = read.nextLine(); 
					if(line != "") {
						tmp_list.add(line);
					}             
				}
				read.close();				
				System.out.println("data earned!");
			}		
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  
        return tmp_list;
    }
	
	public static void deletefromFile(String folderPath, ArrayList<String> list) {
    	
        for(int i=0; i<list.size(); i++) {
        	if((list.get(i).substring(2)).equals(folderPath)) {
        		System.out.println(list.get(i)+" will be removed...");
        		list.remove(i);          	
        	}       		
        }
        
        System.out.println("remove successfull!");
        
        file.delete();
        updateFile(list);
    }

	public static void updateFile(ArrayList<String> list) {
		
		System.out.println("updating file...");
		
		for(int i=0; i<list.size(); i++) {
			writeFile(list.get(i), list);
        }
		
		System.out.println("file updated!");
	}
	
	public static void Lock(String folderPath, ArrayList<String> list) {
		
		for(int i=0; i<list.size(); i++) {
        	if((list.get(i).substring(2)).equals(folderPath)) {
        		System.out.println(list.get(i)+" will be locked...");
        		String locked_tmp = "1-" + list.get(i).substring(2);
        		String lockFile = list.get(i).substring(2);
        		list.remove(i);
        		list.add(locked_tmp);
        		
        		String [] command = {"cmd",};					
				Process p;
				
				try {
						p = Runtime.getRuntime().exec(command);
				        new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		                new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		                PrintWriter stdin = new PrintWriter(p.getOutputStream());
		                stdin.println("cacls " + lockFile + " /P everyone:n");
		                stdin.println("y");
		                stdin.close();
		                p.waitFor();
		                
		                JOptionPane.showConfirmDialog(null, "File locked!", "Locking", JOptionPane.CLOSED_OPTION);
						System.out.println("locked successfull!");
						break;
			    	} catch (Exception e) {
			    		JOptionPane.showConfirmDialog(null, "An error ocuured!", "Error", JOptionPane.CLOSED_OPTION);
			    		e.printStackTrace();
				}     		
        	}       		
        }
		file.delete();
        updateFile(list);
	}
	
	public static void Unlock(String folderPath, ArrayList<String> list) {
		for(int i=0; i<list.size(); i++) {
        	if((list.get(i).substring(2)).equals(folderPath)) {
        		System.out.println(list.get(i)+" will be unlocked...");
        		String locked_tmp = "0-" + list.get(i).substring(2);
        		String lockFile = list.get(i).substring(2);
        		list.remove(i);
        		list.add(locked_tmp);
        		
        		String [] command = {"cmd",};					
				Process p;
				
				try {
						p = Runtime.getRuntime().exec(command);
				        new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		                new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		                PrintWriter stdin = new PrintWriter(p.getOutputStream());
		                stdin.println("cacls " + lockFile + " /P everyone:f");
		                stdin.println("y");
		                stdin.close();
		                p.waitFor();
		                
		                JOptionPane.showConfirmDialog(null, "File unlocked!", "Locking", JOptionPane.CLOSED_OPTION);
						System.out.println("unlocked successfull!");
						break;
			    	} catch (Exception e) {
			    		JOptionPane.showConfirmDialog(null, "An error ocuured!", "Error", JOptionPane.CLOSED_OPTION);
			    		e.printStackTrace();
				}	
        	}       		
        }
		file.delete();
        updateFile(list);
	}
	
	public static String boolToString (boolean b) {
		
		if(b)
			return "locked";
		else
			return "not locked";
	}
	
	public static boolean stringtoBool (String s) {
		
		return s.equals("1");
	}
	
	public static String[] listToArray (ArrayList<String> list) {
		
		int size = list.size();
		String[] tmp_arr = new String[size];
		
		if(list != null) {
			for(int i=0; i<size; i++) {
				tmp_arr[i] = list.get(i).substring(2);
			}
		}
		
		return tmp_arr;
	}
	
	public static String[] UnlockedsToArray (ArrayList<String> list) {
		
		ArrayList<String> tmp_list = new ArrayList<String>();
		String[] tmp_arr = new String[0];
		
		if(list != null) {
						
			for(int i=0; i<list.size(); i++) {
				if(list.get(i).charAt(0) == '0') {
					tmp_list.add(list.get(i));
				}				
			}
			
			tmp_arr = new String[tmp_list.size()];
			
			for(int i=0; i<tmp_list.size(); i++) {
				tmp_arr[i] = tmp_list.get(i).substring(2);
			}				
		}
		
		return tmp_arr;
	}
	
	public static String[] LockedsToArray (ArrayList<String> list) {
		
		ArrayList<String> tmp_list = new ArrayList<String>();
		String[] tmp_arr = new String[0];
		
		if(list != null) {
						
			for(int i=0; i<list.size(); i++) {
				if(list.get(i).charAt(0) == '1') {
					tmp_list.add(list.get(i));
				}				
			}
			
			tmp_arr = new String[tmp_list.size()];
			
			for(int i=0; i<tmp_list.size(); i++) {
				tmp_arr[i] = tmp_list.get(i).substring(2);
			}				
		}
		
		return tmp_arr;
	}
	
	public static void printList(ArrayList<String> list) {
		
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	public static boolean isFileExist(String path, ArrayList<String> list) {

		System.out.println(path);
		for(int i=0; i<list.size(); i++) {
			if(path.equals(list.get(i).substring(2))){
				System.out.println("true");
				return true;
			}

				
		}

		return false;
	} 
}
