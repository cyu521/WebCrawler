package Util;

import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> {

    private T data;
    TreeNode<T> parent;
    List<TreeNode<T>> children;

    public List<TreeNode<T>> getChildren() {
		return children;
	}

	public TreeNode(T data) {
        this.setData(data);
        this.children = new LinkedList<TreeNode<T>>();
    }

    public void addChild(TreeNode<T> child) {
    	if(child != null){
        	child.parent = this;
            this.children.add(child);
    	}
    }
    
    public boolean hasChild(){
    	if(children.size() > 0)
    		return true;
    	return false;
    }

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}