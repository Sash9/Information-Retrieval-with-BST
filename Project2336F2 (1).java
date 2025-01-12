/**
  Project Requirement: Refer to the project document provided with the assignment 
  Project Description:
  Information retrieval systems allow users to enter keywords and retrieve articles that have those keywords associated with them.
  For example, once a student named Yi Li wrote a paper called, â€œObject Class Recognition using Images of Abstract Regions,"
  and included the following keywords: `object recognition', `abstract regions', `mixture models', and `EM algorithm'.
  If someone does a search for all articles about the EM algorithm, this paper (and many others) will be retrieved.

  Implement a binary search tree and use it to store and retrieve articles. The tree will be sorted by keyword, and each node will
  contain an unordered linked list of Record objects which contain information about each article that corresponds to that
  keyword

  datafile contains the following per Article record
  Title Id
  Title
  Author
  Number of keys identifier
  List of keys in each corresponding article

  Keys are inserted into the Binary Search tree using the insert method in the BST class
  Each key will reference an unordered linked list of article objects (articleid, titleid, and author)

  Algorithm:
    - Create a BufferedReader Object to read the text from an Input stream (datafile.txt) by buffering characters that seamlessly
    reads lines (characters, arrays or lines).
    Note: Each read request made of a Reader causes a corresponding read request to be made of the underlying character or byte stream.
    It wraps BufferedReader in Java around a java FileReaders (whose read() operations may be costly)
    - Loop:
      - read titleid, title, author
        - create an article object
      - read the number of keys identifier
        - Loop read number of keys
          - insert each key into a BST data structure (BST class will insert the key if not exist)
          - add the article object to each of the respective keyword node in the BST data structure

      - ouput the resultant BST along with the list of articles per keyword
  */



// Sashank Merugumuvvala
// IDE: OnlineGDB


import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Project2336F2 {
    
    public static void main(String args[]) {    
        BinarySearchTree bst = new BinarySearchTree();
        readFileRecords(bst, "datafile.txt");
        
        System.out.println("\t\tWelcome to Information Retrieval System");
        
        Scanner input = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. InOrder Traversal with Details <Output keywords along with their associated referenced articles.>");
            System.out.println("2. InOrder Traversal (Keywords Only) <Output only the keywords, excluding the referenced articles.>");
            System.out.println("3. PreOrder Traversal (Keywords Only) <Output only the keywords in pre-order traversal, without the referenced articles.> ");
            System.out.println("4. Search for a specific Keyword <If found, display the keyword with referenced articles; otherwise, output the keyword not found message.>");
            System.out.println("5. Exit <Terminates the program.>");
            System.out.print("\nEnter a choice? ");
            choice = input.nextInt();
            
            
            switch(choice){
                case 1:
                    bst.inOrderWithDetails();
                    break;
                case 2:
                    bst.inOrderWithKeywords();
                    System.out.println();
                    break;
                case 3:
                    bst.preOrder();
                    System.out.println();
                    break;
                case 4:
                    System.out.print("Enter the keyword: ");
                    String keyword = input.next();
                    
                    TreeNode<String> result = bst.search(keyword);
                    if(result != null && keyword.equals(result.element))
                    {
                        System.out.println(keyword);
                        for(Article article : result.head){
                        System.out.print(article);
                        }
                        System.out.println();
                        
                    }
                    else{
                        System.out.println("Database does not exist in the Information Retrieval System!");
                        System.out.println();
                    }
                    
                    
                    break;
                
            }
        } while (choice != 5);
        
    }
    
    // method to read files
    public static void readFileRecords(BinarySearchTree bst, String filename) {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(filename));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        while(true) {
            if (fileReader == null) {
                System.out.println("Error: file must be opened first!");
                break;
            }
            else {
                try {
                    String strId = fileReader.readLine();
                    if (strId == null) break;
                    int id = Integer.parseInt(strId);
                    String title = fileReader.readLine();
                    String author = fileReader.readLine();
                    int numKeys = Integer.parseInt(fileReader.readLine());
                    
                    String keyword;
                    Article art;
                    for (int i=0; i<numKeys; i++) {
                        keyword = fileReader.readLine();
                        art = new Article(id, title, author);
                        bst.insert(keyword, art);
                    }
                    
                    fileReader.readLine();
                }
                
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            
        }
        
        // free up resources
        if (fileReader != null) {
            try {
                fileReader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

// BST class
class BinarySearchTree <E extends Comparable<E>> {
    protected TreeNode<E> root;
    protected int size;
    
    public TreeNode<E> search(E element) {
        
        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while (current != null) {
            if (element.compareTo(current.element) < 0) {
                parent = current;
                current = current.leftC;
            }
            else if (element.compareTo(current.element) > 0) {
                parent = current;
                current = current.rightC;
                
            }
            else if (element.compareTo(current.element) == 0) {
                return current;
            }
        }
        
        return parent;
    }
    
    public void insert(E element, Article art) 
    {
        if (root == null) {
            root = new TreeNode<>(element);
            root.head.addFirst(art);
        }
        else {
            TreeNode<E> parent = search(element);
            if (parent != null) {
                if (element.compareTo(parent.element) < 0) {
                    parent.leftC = new TreeNode<>(element);
                    parent.leftC.head.addFirst(art);
                }
                else if (element.compareTo(parent.element) > 0) {
                    parent.rightC = new TreeNode<>(element);
                    parent.rightC.head.addFirst(art);
                }
                else {
                    parent.head.addFirst(art);
                }
            }
        }
        size++;
    }
    
    

    // Keyword method
    public void inOrderWithKeywords() 
    {
        System.out.println("\n====================================================");
        System.out.println("Running InOrder Traversal of the Binary Search tree:");
        inOrderWithKeywords(root, "", true);
    }


    // Key word method
    protected void inOrderWithKeywords(TreeNode<E> node, String prefix, boolean isLeft) 
    {
        if (node == null) return;

        inOrderWithKeywords(node.leftC, prefix + (isLeft ? " " : " "), true);

        System.out.printf("%s%s %s\n", prefix, (isLeft ? "L" : "R"), node.element);

        inOrderWithKeywords(node.rightC, prefix + (isLeft ? " " : " "), false);
    }
    
    // Method for in order traversal
    public void inOrderWithDetails() 
    {
        System.out.println("\n====================================================");
        System.out.println("Running InOrder Traversal of the Binary Search tree:");
        inOrderWithDetails(root, "", true);
    }

    protected void inOrderWithDetails(TreeNode<E> node, String prefix, boolean isLeft) 
    {
        if (node == null) return;

        inOrderWithDetails(node.leftC, prefix + (isLeft ? "  " : "  "), true);

        System.out.printf("%s%s %s\n", prefix, (isLeft ? "L" : "R"), node.element);
    
        if (node.head.isEmpty()) {
            System.out.println(prefix + "   No articles found for this keyword.");
        } else {
        for (Article article : node.head) {
            System.out.println(prefix + "" + article);
        }
    }

        inOrderWithDetails(node.rightC, prefix + (isLeft ? "  " : "  "), false);
    }

    
    // preorder traversal method
    public void preOrder() 
    {
        System.out.println("\n====================================================");
        System.out.println("Running PreOrder Traversal of the Binary Search tree:");
        preOrder(root, "", true);
    }


    protected void preOrder(TreeNode<E> node, String prefix, boolean isLeft) 
    {
        if (node == null) return;

        System.out.printf("%s%s %s\n", prefix, (isLeft ? "L" : "R"), node.element);

        preOrder(node.leftC, prefix + (isLeft ? "  " : "  "), true);

        preOrder(node.rightC, prefix + (isLeft ? "  " : "  "), false);
    }
    
    
}


class TreeNode<E> {
    protected E element;
    protected TreeNode<E> leftC;
    protected TreeNode<E> rightC;
    protected LinkedList<Article> head;
    
    public TreeNode(E e) {
        element = e;
        head = new LinkedList<Article>();
    }
    
}


class Article {
    private int id;
    private String title;
    private String author;
    
    public Article() { }
    public Article(int i, String t, String a) {
        id =i;
        title = t;
        author = a;
    }
    
    @Override
    public String toString() {
        return String.format("\t %d | %s | %s-->\n", id, title, author);
    }
}