package mishanesterenko.iad.lb3.id3;

import java.util.ArrayList;
import java.util.List;

import mishanesterenko.iad.lb3.util.StringAttribute;

public class DecisionTreeNode {
	private StringAttribute splittingAttribute;
	private List<DecisionTreeNode> children;
	private DecisionTreeNode parent;

	public DecisionTreeNode(){
		this.setParent(parent);
	}
	
	public DecisionTreeNode(StringAttribute splittingAttr) {
		setChildren(new ArrayList<DecisionTreeNode>());
		this.splittingAttribute = splittingAttr;
	}

	public List<DecisionTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<DecisionTreeNode> children) {
		this.children = children;
	}

	public StringAttribute getSplittingAttribute() {
		return splittingAttribute;
	}

	public void setSplittingAttribute(StringAttribute spliittingAttr) {
		this.splittingAttribute = spliittingAttr;
	}

	public DecisionTreeNode getParent() {
		return parent;
	}

	public void setParent(DecisionTreeNode parent) {
		this.parent = parent;
	}
}
