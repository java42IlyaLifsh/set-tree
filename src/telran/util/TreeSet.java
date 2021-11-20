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
		
// MUST CASE STR-115 or STR-117		
		
		removeNodeSubst(removedNode);

// 		removeNodeClassWork(removedNode);
		return removedNode.obj;
	}

	private void removeNodeSubst(Node<T> removedNode) {
		if(removedNode.left != null && removedNode.right != null) {
			// Junction
			removeJunctionNode(removedNode);
		} else {
			// non-junction/Leaf
			removeNonJunctionNode(removedNode);
		}	
		size--;
	}
	private void removeNonJunctionNode(Node<T> removedNode) {
		if(removedNode == root) {
			removedNode.parent = null;
			root = removedNode.right == null ? removedNode.left : removedNode.right;
		} else {
			// Get parent and child
			Node<T> parent = removedNode.parent;
			Node<T> child = removedNode.right == null ? removedNode.left : removedNode.right;
			// Connect parent with child
			if (parent.right == removedNode) {
				parent.right = child;				
			} else {
				parent.left = child;
			}
			// Connect child with parent
			if (child != null) {
				child.parent = parent;
			}
		}
	} 
	
	private void removeJunctionNode(Node<T> removedNode) {
		// A substitution is the most left node from the right 
		// subtree of the being removed node
		Node<T> substitutionNode = getMostLeftFrom(removedNode.right);
		// Junction should replace its reference to object with 
		// the reference from the substitution
		removedNode.obj = substitutionNode.obj;
		removedNode.right = substitutionNode.right;
		Node<T> parent = substitutionNode.parent;
		
		
//   I don't understand, maybe variant, then the substitutionNode has a parent on the left or only on the right (variant zig-zag) ??????????
		
		
		if (parent.right==substitutionNode) {parent.right = null;}
		if (parent.left==substitutionNode) {parent.left = null;}
		substitutionNode.parent = null;
		// if  remove root must doing additional simple steps;
		if (removedNode == root) {
		root.right = substitutionNode.right;
		root.obj=substitutionNode.obj;
		root.parent=null;
		}
	}
	
	/*   no method needed
	  
	private void removeRootJunction() {
		//update the method by applying another algorithm (see slide 28)
		// Get child node with preference to the right branch
		Node<T> child = root.right;
		// Disconnect child from parent node
		child.parent = null;
		Node<T> parentLeft = getMostLeftFrom(root.right);
		parentLeft.left = root.left;
		root.left.parent = parentLeft;
		root = child;
	}
	*/


	
	@SuppressWarnings("unused")
	
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