package mishanesterenko.iad.lb1.ui;

import java.nio.FloatBuffer;
import java.util.List;

import mishanesterenko.iad.lb1.core.Cluster;
import mishanesterenko.iad.lb1.core.dataset.AbstractDataSet.Vector;
import mishanesterenko.iad.lb1.core.dataset.DataSet;

import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.debug.Arrow;
import com.jme3.util.BufferUtils;

public class ClusteringApplication extends SimpleApplication {
	public final static int POINT_SIZE = 15;

	private Node pointsNode;

	private DataSet initialDataSet;

	public ClusteringApplication(DataSet dataSet) {
		initialDataSet = dataSet;
	}

	@Override
	public void simpleInitApp() {
		this.flyCam.setEnabled(false);
		cam.setFrustumNear(0.5f);
		cam.setFrustumPerspective(45.0f,(float) (float) 800 / (float) 600, 0.1f, 1000);

		{
			pointsNode = new Node();
			rootNode.attachChild(pointsNode);
		}

		{
			Node axisNode = new Node();
			rootNode.attachChild(axisNode);

			Material xAxisMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			xAxisMaterial.setColor("Color", ColorRGBA.Red);
			Material yAxisMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			yAxisMaterial.setColor("Color", ColorRGBA.Green);
			Material zAxisMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			zAxisMaterial.setColor("Color", ColorRGBA.Blue);

			Arrow x = new Arrow(new Vector3f(10, 0, 0));
			Arrow y = new Arrow(new Vector3f(0, 10, 0));
			Arrow z = new Arrow(new Vector3f(0, 0, 10));

			Geometry xAxis = new Geometry("x", x);
			Geometry yAxis = new Geometry("y", y);
			Geometry zAxis = new Geometry("z", z);

			xAxis.setMaterial(xAxisMaterial);
			yAxis.setMaterial(yAxisMaterial);
			zAxis.setMaterial(zAxisMaterial);
			axisNode.attachChild(xAxis);
			axisNode.attachChild(yAxis);
			axisNode.attachChild(zAxis);
		}

		ChaseCamera chaseCam = new ChaseCamera(cam, rootNode, inputManager);
		chaseCam.setInvertVerticalAxis(true);
		chaseCam.setMinDistance(0.15f);
		chaseCam.setSmoothMotion(true);

		setPoints(initialDataSet);
		initialDataSet = null;
	}

	public void setClusters(List<? extends Cluster> clusters) {
		pointsNode.detachAllChildren();
		for (Cluster cluster : clusters) {
			ColorRGBA clusterColor = ColorRGBA.randomColor();
			List<Vector> clusteredVectors = cluster.getClusteredVectors();

			FloatBuffer coordinatesBuffer = BufferUtils.createFloatBuffer(clusteredVectors.size() * 3);
			for (Vector clusteredVector : clusteredVectors) {
				coordinatesBuffer.put((float) clusteredVector.getValue(0)); // x
				coordinatesBuffer.put((float) clusteredVector.getValue(1)); // y
				coordinatesBuffer.put((float) clusteredVector.getValue(2)); // z
			}
			Mesh clusterPointsMesh = new Mesh();
			clusterPointsMesh.setPointSize(POINT_SIZE);
			clusterPointsMesh.setMode(Mesh.Mode.Points);
			clusterPointsMesh.setBuffer(Type.Position, 3, coordinatesBuffer);
			clusterPointsMesh.updateBound();
			clusterPointsMesh.updateCounts();

			Geometry clusterGeometry = new Geometry(clusterPointsMesh.toString(), clusterPointsMesh);
			Material clusterMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			clusterMaterial.setColor("Color", clusterColor);
			clusterGeometry.setMaterial(clusterMaterial);
			pointsNode.attachChild(clusterGeometry);
		}
	}

	public void setPoints(DataSet points) {
		pointsNode.detachAllChildren();

		FloatBuffer coordinatesBuffer = BufferUtils.createFloatBuffer(points.size() * 3);
		for(Vector point : points) {
			coordinatesBuffer.put((float) point.getValue(0)); // x
			coordinatesBuffer.put((float) point.getValue(1)); // y
			coordinatesBuffer.put((float) point.getValue(2)); // z
		}
		Mesh pointsMesh = new Mesh();
		pointsMesh.setPointSize(POINT_SIZE);
		pointsMesh.setMode(Mesh.Mode.Points);
		pointsMesh.setBuffer(Type.Position, 3, coordinatesBuffer);
		pointsMesh.updateBound();
		pointsMesh.updateCounts();

		Geometry pointsGeometry = new Geometry(pointsMesh.toString(), pointsMesh);
		Material pointsMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		pointsMaterial.setColor("Color", ColorRGBA.Yellow);
		pointsGeometry.setMaterial(pointsMaterial);
		pointsNode.attachChild(pointsGeometry);
	}

	public static void main(String[] args) {
		ClusteringApplication app = new ClusteringApplication(new DataSet());
		app.start();
	}
}