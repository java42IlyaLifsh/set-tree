package telran.util;
//HW_13 IlyaL


import java.util.Comparator;
import java.util.Iterator;

public class TreeSet<T> extends AbstractSet<T> {
 private static class Node<T> {
	 T obj;
	 Node<T> left; //reference to all nodes containing objects less than obj
	 Node<T> right; //reference to all nodes containing objects greater than obj
	 Node<T> parent; //reference to a parent
	 Node(T obj) {
		 this.obj = obj;
	 }
 }
 private Node<T> root;
 private Comparator<T> comp;
 public TreeSet(Comparator<T> comp) {
	 this.comp = comp;
 }
 @SuppressWarnings("unchecked")
public TreeSet() {
	 this((Comparator<T>)Comparator.naturalOrder());
 }
 private Node<T> getMostLeftFrom(Node<T> from) {
	 while(from.left != null) {
		 from = from.left;
	 }
	 return from;
 }
 private Node<T> getFirstParentGreater(Node<T> node) {
	 while(node.parent != null && node.parent.left != node) {
		 node = node.parent;
	 }
	 return node.parent;
 }
 private class TreeSetIterator implements Iterator<T> {
	 Node<T> current = root == null ? root : getMostLeftFrom(root);
	 Node<T> previous = null;
@Override
	public boolean hasNext() {
		
		return current != null;
	}

	@Override
	public T next() {
		T res = current.obj;
		previous = current;
		current = current.right != null ? getMostLeftFrom(current.right) :
			getFirstParentGreater(current);
		return res;
	}
	@Override 
	public void remove() {
		//
		TreeSet.this.remove(previous.obj);
		
	}
	 
 }
	@Override
	public boolean add(T obj) {
		if(root == null) {
			addRoot(obj);
			size++;
			return true;
		}
			Node<T> parent = getParent(obj);
			//If obj already exists getParent will return null
			if (parent == null) {
				return false;
			}
			Node<T> node = new Node<>(obj);
			if (comp.compare(obj, parent.obj) < 0) {
				parent.left = node;
			} else {
				parent.right = node;
			}
			node.parent = parent;
			size++;
		
		return true;
	}

	private Node<T> getParent(T obj) {
		Node<T> current = root;
		Node<T> parent = null;
		while(current != null) {
			int res = comp.compare(obj, current.obj);
			if (res == 0) {
				return null;
			}
			parent = current;
			current = res < 0 ? current.left : current.right;
			
		}
		return parent;
	}
	private void addRoot(T obj) {
		root = new Node<>(obj);
		
	}
	@Override
	public T remove(T pattern) {
		Node<T> removedNode = getNode(pattern);
		if (removedNode == null) {
			return null;
		}
 		removeNodeSubst(removedNode);
// 		removeNodeClassWork(removedNode);
		return removedNode.obj;
	}

	private void removeNodeSubst (Node <T> removedNode) {
	if (removedNode.left!=null && removedNode.right!=null) {
		//This is Junction!!!!
		removeJunctionNode (removedNode);
	} else {
		//it's not junction or leaf
		removeNoJunctionNode (removedNode);
	}
	}
private void removeNoJunctionNode (Node<T> removedNode) {
	// check root / not root
	if (removedNode == root) {
		removedNode.parent = null;
		root = removedNode.right == null ? removedNode.left : removedNode.right;
	} else {
		//connect parent and child
		Node<T> parent = removedNode.parent;
		Node<T> child = removedNode.right == null ? removedNode.left : removedNode.right;
		if (parent.right==removedNode) {
			parent.right=child;
		}else {	parent.left=child;}
		if (child !=null) {child.parent = parent;}

	}
}
	
private void removeJunctionNode (Node<T> removedNode) {
	// Search substNode is most left from right
	Node<T> substitutionNode = getMostLeftFrom(removedNode.right); 
	// Check root or not root
	if (removedNode != root) {
		Node<T> parent = removedNode.parent;
		if (parent.right == removedNode) {
			parent.right = substitutionNode;
			} else {
			parent.left = substitutionNode; 
			}
			substitutionNode.parent = parent;	
	} else {
		substitutionNode.parent = null;
		root = substitutionNode;
	}
	
	substitutionNode.left = removedNode.left;
	substitutionNode.right = removedNode.right;
	
	if(substitutionNode.right != null) {
			substitutionNode.right.parent = substitutionNode;	
	}
	if(substitutionNode.left != null) {
		substitutionNode.left.parent = substitutionNode;	
	}
}
	
	
	
	private void removeNodeClassWork(Node<T> removedNode) {
		//update the method by applying another algorithm
		if (removedNode == root) {
			removeRoot();
		} else {
			Node<T> parent = removedNode.parent;
			Node<T> child = removedNode.right == null ? removedNode.left : removedNode.right;
			
			if (parent.right == removedNode) {
				parent.right = child;
				
			} else {
				parent.left = child;
			}
			if (child != null) {
				child.parent = parent;
			}
			if (removedNode.right != null) {
				Node<T> parentLeft = getMostLeftFrom(removedNode.right);
				parentLeft.left = removedNode.left;
				if(removedNode.left != null) {
					removedNode.left.parent = parentLeft;
				}
				
			}
		}
		size--;
		
	}
	private void removeRoot() {
		//update the method by applying another algorithm (see slide 28)
		Node<T> child = root.right == null ? root.left : root.right;
		if (child != null) {
			child.parent = null;
		}
		if (root.right != null) {
			Node<T> parentLeft = getMostLeftFrom(root.right);
			parentLeft.left = root.left;
			if (root.left != null) {
				root.left.parent = parentLeft;
			}
		}
		root = child;
		
	}
	private Node<T> getNode(T pattern) {
		Node<T> current = root;
		while(current != null && !current.obj.equals(pattern)) {
			current = comp.compare(pattern, current.obj) > 0 ? current.right : current.left;
		}
		return current;
	}
	@Override
	public Iterator<T> iterator() {
		
		return new TreeSetIterator();
	}

	@Override
	public boolean contains(T pattern) {
		
		return getParent(pattern) == null ;
	}

}