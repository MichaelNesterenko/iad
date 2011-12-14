package mishanesterenko.iad.lb3.id3;

import java.util.Collections;
import java.util.List;

import mishanesterenko.iad.lb3.util.StringAttribute;

public class LeafDecisionTreeNode extends DecisionTreeNode {
	private StringAttribute.Value category;

	public LeafDecisionTreeNode(StringAttribute.Value cat) {
		category = cat;
	}

	public StringAttribute.Value getCategory() {
		return category;
	}

	@Override
	public List<DecisionTreeNode> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public void setChildren(List<DecisionTreeNode> children) {
		throw new UnsupportedOperationException();
	}

	@Override
	public StringAttribute getSplittingAttribute() {
		return null;
	}

	@Override
	public void setSplittingAttribute(StringAttribute spliittingAttr) {
		throw new UnsupportedOperationException();
	}

}
