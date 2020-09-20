import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.StringTokenizer;





public class bptree {
	

	//this variable will be used in connecting leaf node when load tree from file 
	public static LeafNode sibling = null;
	
	/*
	 * save tree data by doing preorder traversal in recursive way
	 * ObjectOutputStream out : output file stream reference
	 * Node node: a node should be stored in file 
	 */
	
	public static void saveTree(ObjectOutputStream out, Node node) throws IOException
	{
		if(node != null)
		{
			// '(' means start of this node 
			out.writeChar('('); 
			if(node instanceof NonLeafNode)
			{
				// 'N' means this node is NonLeafNode
				out.writeChar('N');
				out.writeInt(node.numberOfKeys);
				for(int i =0; i < node.numberOfKeys ;i++)
				{
					out.writeInt(node.keyValues[i].key);
					//travel child node
					saveTree(out,(Node)node.keyValues[i].value);
				}
				//travel most right side of node
				saveTree(out,((NonLeafNode)node).rightChild);
			}
			else if(node instanceof LeafNode)
			{
				//'L' means this node is LeafNode
				out.writeChar('L');
				out.writeInt(node.numberOfKeys);
				for(int i =0; i < node.numberOfKeys ;i++)
				{
					out.writeInt(node.keyValues[i].key);
					out.writeInt((Integer)node.keyValues[i].value);
					
				}
			}
			//')' means end of this node
			out.writeChar(')');
		}
	}
	
	
	/*
	 * load tree data stored in preorder traversal 
	 * ObjectInputStream out : input file stream reference
	 * int order : the order of tree 
	 */
	
	public static Node loadTree(ObjectInputStream in, int order) throws IOException
	{
		Node node = null;
		char input;
		
		//'(' means read start of node
		if(in.readChar() == '(')
		{
				//read type of Node
				input =in.readChar();
				
				//when this node is NonLeafNode
				if(input == 'N')
				{
					// create NonLeafNode with it's size 
					node = new NonLeafNode(order-1);
					node.numberOfKeys = in.readInt();
					for(int i = 0 ; i < node.numberOfKeys; i++)
					{
						int key = in.readInt();
						//create child node
						Node child = loadTree(in,order);
						node.keyValues[i] = new KeyValue(key,child);
					}
					//create most right side child
					((NonLeafNode)node).rightChild = loadTree(in,order);
				}
				//when this node is LeafNode
				else if(input == 'L')
				{
					// create LeafNode with it's size
					node = new LeafNode(order-1);
					node.numberOfKeys = in.readInt();
					for(int i = 0 ; i < node.numberOfKeys; i++)
					{
						node.keyValues[i] = new KeyValue(in.readInt(),null);
						node.keyValues[i].value = new Integer(in.readInt()); 
					
					}
					//connect leaf node
					if(sibling != null)
					{
						//connect with left LeaFnode
						sibling.rightSibling = (LeafNode)node;
					}
					//store current LeafNode in static variable to connect with next LeafNode
					sibling = (LeafNode)node;
					
				}
		}
		
		//when read this node information successfully
		if( in.readChar() == ')')
		{
			//return created node
			return node;
		}
		//when faield to read information
		else
		{
			System.out.println("read wrong format file!");
			return null;
		}
	}
	
	
	public static void main(String args[])
	{
		
		
		Index tree = null;
		String input = null;
		StringTokenizer token = null;
		
		
		//when command line is start -c, create new index file with size
		if(args[0].equals("-c"))
		{
			try 
			{
				//args[1] = indexfile name
				//args[2] = order of tree
				FileOutputStream fileName = new FileOutputStream(args[1]);
				ObjectOutputStream outputFile = new ObjectOutputStream(fileName);
				
				//store tree's order in file
				outputFile.writeInt(Integer.parseInt(args[2]));
				//create tree instance  
				tree = new Index(Integer.parseInt(args[2]));
				//save tree in file
				saveTree(outputFile, tree.root);
				outputFile.close();
			}
			
			//when failed to open or write file
			catch(FileNotFoundException e1)
			{
				System.out.println(e1.getMessage());
			}
			catch(IOException e2) 
			{
				System.out.println(e2.getMessage());
			}
		}
		
		//when command line is start -i, insert data
		else if(args[0].equals("-i"))
		{
			 
			try 
			{
				//args[1] = indexfile name
				//args[2] = datafile name
				//open index file
				FileInputStream indexInputFileName = new FileInputStream(args[1]);
				ObjectInputStream indexInputFile = new ObjectInputStream(indexInputFileName);
				
				//load tree from index file
				sibling = null;
				tree = new Index(indexInputFile.readInt()); // first content of file is order of tree
				tree.root = loadTree(indexInputFile,tree.order); // load tree from file
				
				//open data file
				FileReader dataFileName = new  FileReader(args[2]);
				BufferedReader dataFile = new BufferedReader(dataFileName);
				
				//insert keys from data file
				while((input = dataFile.readLine())!= null)
				{
					token = new StringTokenizer(input,","); // use delimiter ','
					tree.rootInsert(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken())); // insert key in tree

				}
			
			
				indexInputFile.close();
				dataFile.close();
			
				FileOutputStream  indexOutputFileName= new FileOutputStream(args[1]);
				ObjectOutputStream indexOutputFile = new ObjectOutputStream(indexOutputFileName);
				
				//save tree in file
				indexOutputFile.writeInt(tree.order);
				saveTree(indexOutputFile,tree.root);
				
				indexOutputFile.close();
			
			}

			catch(FileNotFoundException e1) 
			{
				System.out.println(e1.getMessage());
			}
			
			catch(IOException e2) 
			{
				System.out.println(e2.getMessage());
			}
			
			
		}
		
		else if(args[0].equals("-s"))
		{
			try 
			{
				//args[1] = indexfile name
				//args[2] =  key value
				//open index file
				FileInputStream indexFileName = new FileInputStream(args[1]);
				ObjectInputStream indexFile = new ObjectInputStream(indexFileName);
				
				//load tree from index file
				sibling = null; 
				tree = new Index(indexFile.readInt()); //first content of file is tree's order
				tree.root = loadTree(indexFile,tree.order); // load tree from file
				
				tree.searchSingleKey(Integer.parseInt(args[2])); // search a key from tree
				
				//close index file stream
				indexFile.close();
			}
			
			catch(FileNotFoundException e1)
			{
				System.out.println(e1.getMessage());
			}
			catch(IOException e2)
			{
				System.out.println(e2.getMessage());
			}

		}
		
		
		else if(args[0].equals("-r"))
		{
			try 
			{
				//args[1] = indexfile name
				//args[2] = startkey
				//args[3] = endkey
			
				//open index file
				FileInputStream indexFileName = new FileInputStream(args[1]);
				ObjectInputStream indexFile = new ObjectInputStream(indexFileName);
				sibling = null;
				tree = new Index(indexFile.readInt()); //first content of file is tree's order
				tree.root = loadTree(indexFile,tree.order); //load tree from file
				tree.rangeSearch(Integer.parseInt(args[2]),Integer.parseInt(args[3])); //search key located between startkey and end key
				
				//close index file
				indexFile.close();
			}
			
			catch(FileNotFoundException e1)
			{
				System.out.println(e1.getMessage());
			}
			catch(IOException e2)
			{
				System.out.println(e2.getMessage());
			}

		}
		else if(args[0].contentEquals("-d"))
		{
			try
			{	
				//args[1] = indexfile name
				//args[2] = datafile name
				//open index file
				FileInputStream indexInputFileName = new FileInputStream(args[1]);
				ObjectInputStream indexInputFile = new ObjectInputStream(indexInputFileName);
				
				//open data file
				FileReader dataFileName =  new FileReader(args[2]);
				BufferedReader dataFile = new BufferedReader(dataFileName);
				
				sibling = null;
				tree = new Index(indexInputFile.readInt()); //read tree's order from file
				tree.root = loadTree(indexInputFile,tree.order); // load tree from file
				
				while((input = dataFile.readLine())!= null)
				{
					token = new StringTokenizer(input,","); // use delimiter ','
					tree.rootDelete(Integer.parseInt(token.nextToken())); // delete key in tree
				}
				
				
				indexInputFile.close();
				dataFile.close();
				
				FileOutputStream  indexOutputFileName= new FileOutputStream(args[1]);
				ObjectOutputStream indexOutputFile = new ObjectOutputStream(indexOutputFileName); 
				//save tree
				indexOutputFile.writeInt(tree.order); //save tree's order
				saveTree(indexOutputFile,tree.root); 
				
				indexOutputFile.close();
					
			}
			catch(FileNotFoundException e1)
			{
				System.out.println(e1.getMessage());
			}
			catch(IOException e2)
			{
				System.out.println(e2.getMessage());
			}
		}
		//when deal with wrong input command
		else
		{
			System.out.println("Wrong Format!");
		}
		
		
	
	}
}