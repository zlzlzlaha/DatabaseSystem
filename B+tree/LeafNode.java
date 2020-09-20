

public class LeafNode extends Node 
{
	public LeafNode rightSibling;
	
	LeafNode(int size)
	{
		super(size);
		rightSibling = null;
	}
}
