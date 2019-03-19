package regEx;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Exception;

public class RegEx {
  //MACROS
  static final int CONCAT = 0xC04CA7;
  static final int ETOILE = 0xE7011E;
  static final int ALTERN = 0xA17;
  static final int DOT = 0xD07;
  
  //REGEX
  private static String regEx;
  
  //CONSTRUCTOR
  public RegEx(){}

  //MAIN
  public static void main(String arg[]) {    
	  regEx = "S(a|g|r)+on";
	  System.out.println("grep -E "+regEx+"\n");
	  
	  System.out.println(">> Parsing regEx \""+regEx+"\".");
	  if(regEx.length()<1) {
		  System.err.println(">> ERROR: empty regEx.");
	  } else {
		  System.out.print(">> ASCII codes: ["+(int)regEx.charAt(0));
	      for (int i=1;i<regEx.length();i++) System.out.print(","+(int)regEx.charAt(i));
	      System.out.println("].");
	      try {
	        RegExTree ret = parse();
	        System.out.println(">> Tree result: "+ret.toString()+".\n");
	      } catch (Exception e) {
	        System.err.println(">> ERROR: syntax error for regEx \""+regEx+"\".");
	      }
	  }
	  
	  /**
	   * Lecture du fichier txt
	   */
	  try{
		  InputStream flux=new FileInputStream("C:\\Users\\ocean\\Documents\\monfichier.txt"); // Chemin du fichier
		  InputStreamReader lecture=new InputStreamReader(flux);
		  BufferedReader buff=new BufferedReader(lecture);
		  String ligne;
		  while ((ligne=buff.readLine())!=null){
			  RegExReadTxt(ligne);
		  }
		  buff.close(); 
	  } catch (Exception e){
		  System.out.println(e.toString());
	  }
	  System.out.println("\n>> Parsing completed.");
  }
  
  /**
   * Lit le fichier et affiche en foncion du RegEx
   */
  private static void RegExReadTxt(String ligne) {
	  // Séparation en mot
	  String[] parts = ligne.split(" ");
	  
	  for(int i=0; i<parts.length; i++) {
		  if(!parts[i].isEmpty()) RegExSearch(parts[i]);
	  }
  }
  
  /**
   * Recherche des infos dans le mot, affichage du mot si il existe
   * @param mot
   */
  private static void RegExSearch(String mot) {	 
	  int a = 0;
	  int b = 0;
	  int c = 0;
	  boolean check = false;
	  
	  if(mot.charAt(0) == 'S' && mot.charAt(mot.length()-2) == 'o' && mot.charAt(mot.length()-1) == 'n') {
		  for(int i=1; i<mot.length(); i++) {
			  char caract = mot.charAt(i);// Séparare lettre par lettre			  
			  if(caract == 'a'||caract == 'g'||caract == 'r') check = true;
		  }
		  
		  if(check == true) System.out.println(">> Mot : "+mot);
	  }
  }
  
  

  //FROM REGEX TO SYNTAX TREE
  // --> Under construction
  private static RegExTree parse() throws Exception {
    if (false) throw new Exception();
    return exampleAhoUllman();
  }
  // --> RegEx from Aho-Ullman book Chap.10 Example 10.25
  private static RegExTree exampleAhoUllman() {
    RegExTree a = new RegExTree((int)'a', new ArrayList<RegExTree>());
    RegExTree b = new RegExTree((int)'b', new ArrayList<RegExTree>());
    RegExTree c = new RegExTree((int)'c', new ArrayList<RegExTree>());
    ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
    subTrees.add(c);
    RegExTree cEtoile = new RegExTree(ETOILE, subTrees);
    subTrees = new ArrayList<RegExTree>();
    subTrees.add(b);
    subTrees.add(cEtoile);
    RegExTree dotBCEtoile = new RegExTree(CONCAT, subTrees);
    subTrees = new ArrayList<RegExTree>();
    subTrees.add(a);
    subTrees.add(dotBCEtoile);
    return new RegExTree(ALTERN, subTrees);
  }
}

//UTILITARY CLASS
class RegExTree {
  private int root;
  private ArrayList<RegExTree> subtrees;
  public RegExTree(int root, ArrayList<RegExTree> subtrees) {
    this.root = root;
    this.subtrees = subtrees;
  }
  //FROM TREE TO PARENTHESIS
  public String toString() {
    if (subtrees.isEmpty()) return rootToString();
    String result = rootToString()+"("+subtrees.get(0).toString();
    for (int i=1;i<subtrees.size();i++) result+=","+subtrees.get(i).toString();
    return result+")";
  }
  private String rootToString() {
    if (root==RegEx.CONCAT) return ".";
    if (root==RegEx.ETOILE) return "*";
    if (root==RegEx.ALTERN) return "|";
    if (root==RegEx.DOT) return ".";
    return Character.toString((char)root);
  }
}