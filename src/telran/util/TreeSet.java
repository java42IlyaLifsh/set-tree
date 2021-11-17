package telran.util;
//HW_12 IlyaL
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

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
 
 private class TreeSetIterator<T> implements Iterator<T> {
 
	 Node<T> currentNode = getMostLeftNode((Node<T>) root);
 
	@Override
	public boolean hasNext() {
 
		return currentNode != null;
	 
	}

	@Override
	public T next() {
		 
		T res = currentNode.obj;
		currentNode = currentNode.right != null ? 
				getMostLeftNode(currentNode.right) :
				getFirstGreaterParent(currentNode);	
		return res;
	 
	}
	 
	private Node<T> getMostLeftNode(Node<T> node) {
		Node<T> mostLeftNode = node;
		while (mostLeftNode != null && mostLeftNode.left != null) {
			mostLeftNode = mostLeftNode.left;
		}
		return mostLeftNode;
	}
	
	// V.R. Can you do the same without Comparator?
	private Node<T> getFirstGreaterParent(Node<T> node) {
		Node<T> parentNode = node;
		int resComp = -100;
		while(parentNode != null && resComp <= 0) {
			resComp = ((Comparator<T>)comp).compare(parentNode.obj, node.obj);
			if(resComp <= 0) {
				parentNode = parentNode.parent;
			}
		}
		return parentNode;		
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
		// TODO next HW
		return null;
	} 

	

	@Override
	public Iterator<T> iterator() {
		
		return ( new TreeSetIterator<T>());
	}

	@Override
	public boolean contains(T pattern) {
		
		return getParent(pattern) == null ;
	}

}
