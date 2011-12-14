package mishanesterenko.iad.lb3.id3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mishanesterenko.iad.lb3.util.Dataset;
import mishanesterenko.iad.lb3.util.Dataset.Entry;
import mishanesterenko.iad.lb3.util.StringAttribute;

public class ID3Algorithm {
	public DecisionTreeNode buildDecisionTree(Dataset ds) {
		int[] counts = new int[ds.getCategorialAttribute().getValues().size()];
		{
			int index = 0;
			for (String val : ds.getCategorialAttribute().getValues()) {
				int cnt = ds.count(ds.getCategorialAttribute().createValue(val));
				counts[index++] = cnt;
			}
		}
		double entropy = calcEntropy(counts);

		return buildDecisionTreeNode(ds, new ArrayList<StringAttribute>(ds.getAttributes()), entropy);
	}

	private DecisionTreeNode buildDecisionTreeNode(Dataset ds, List<StringAttribute> attributes, double entropy) {
		if (ds.getEntries().size() == 0) {
			throw new IllegalStateException("data set is empty");
		}

		for (String val : ds.getCategorialAttribute().getValues()) {
			StringAttribute.Value catVal = ds.getCategorialAttribute().createValue(val);
			if (ds.count(catVal) == ds.getEntries().size()) {
				return new LeafDecisionTreeNode(catVal);
			}
		}

		if (attributes.size() == 0) {
			int maxCnt = Integer.MIN_VALUE;
			StringAttribute.Value goodVal = null;
			for (String catVal : ds.getCategorialAttribute().getValues()) {
				StringAttribute.Value catValObject = ds.getCategorialAttribute().createValue(catVal);
				int cnt = ds.count(catValObject);
				if (cnt > maxCnt) {
					maxCnt = cnt;
					goodVal = catValObject;
				}
			}
			if (goodVal == null) {
				throw new IllegalStateException("categorial value is null");
			}
			return new LeafDecisionTreeNode(goodVal);
		}

		StringAttribute goodAttr = null;
		double gain = Double.MIN_VALUE;
		for (StringAttribute attr : attributes) {
			double nextGain = calcGain(ds, attr, entropy);
			if (nextGain > gain) {
				gain = nextGain;
				goodAttr = attr;
			}
		}

		DecisionTreeNode res = new DecisionTreeNode(goodAttr);
		List<DecisionTreeNode> children = res.getChildren();
		List<StringAttribute> newAttributes = new ArrayList<StringAttribute>(attributes);
		newAttributes.remove(goodAttr);
		for (String val : goodAttr.getValues()) {
			StringAttribute.Value splitValue = goodAttr.createValue(val);
			Dataset subset = ds.subset(splitValue);
			DecisionTreeNode child = buildDecisionTreeNode(subset, newAttributes, entropy);
			children.add(child);
		}

		return res;
	}

	private double calcEntropy(int[] counts) {
		int total = 0;
		for (int count : counts) {
			total += count;
		}

		double result = 0;
		for (int i = 0; i < counts.length; ++i) {
			double ratio = (double)counts[i] / total;
			if (counts[i] != 0) {
				ratio = - ratio * Math.log(ratio) / Math.log(2);
				result += ratio;
			}
		}

		return result;
	}

	private double calcGain(Dataset ds, StringAttribute attr, double entropy) {
		double elementEntropy = 0;
		for (String value : attr.getValues()) {
			int count = ds.count(attr.createValue(value));

			Map<String, Integer> distrs = new HashMap<String, Integer>();
			Dataset subset = ds.subset(attr.createValue(value));
			for (Entry subsetEntry : subset.getEntries()) {
				String catVal = subsetEntry.getCategory().getValue();
				Integer cnt = distrs.get(catVal);
				if (cnt == null) {
					cnt = 1;
				} else {
					cnt = cnt + 1;
				}
				distrs.put(catVal, cnt);
			}
			int[] counts = new int[ds.getCategorialAttribute().getValues().size()];
			{
				int index = 0;
				for (Map.Entry<String, Integer> entry : distrs.entrySet()) {
					counts[index++] = entry.getValue();
				}
			}

			elementEntropy = (double) count / ds.getEntries().size() * calcEntropy(counts);
		}
		return entropy - elementEntropy;
	}

}
