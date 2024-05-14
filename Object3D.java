import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Object3D {
    private ArrayList<Vector3d> vertexes;
	private ArrayList<int[]> triangles;
	private Vector3d position;
	private double[] rotation;
	private Color color;
	private double scale;

    public Object3D(String filename, Vector3d position, double[] rotation, Color color, double scale) {
        vertexes = ParseOBJ.getVertexesFromFile(filename);
        triangles = ParseOBJ.getTrianglesFromFile(filename);
		this.position = position;
		this.rotation = rotation;
		this.color = color;
		this.scale = scale;

		position.scale(scale);

		for (Vector3d vector : vertexes) {
			vector.scale(scale);
		}
    }

	public static void sortObjects(ArrayList<Object3D> objects, Vector3d cameraPos) {
		for (Object3D object : objects) {
			object.sortTriangles(cameraPos);
		}

        for (int i = 1; i < objects.size(); i++) {
            if (objects.get(i).getDistanceFromCamera(cameraPos) > objects.get(i - 1).getDistanceFromCamera(cameraPos)) {
                for (int j = i; j > 0; j--) {
					
                    if (objects.get(j).getDistanceFromCamera(cameraPos) < objects.get(j - 1).getDistanceFromCamera(cameraPos)) {
                        break;
                    } else {
                        Object3D tmp = objects.get(j - 1);
                        objects.set(j - 1, objects.get(j));
                        objects.set(j, tmp);
                    }
                }
            }
        }
	}

    public void sortTriangles(Vector3d cameraPos) {
		ArrayList<int[]> newTris = new ArrayList<int[]>();

		for (int[] tri : triangles) {
			double distance1 = vertexes.get(tri[0]-1).getDistanceFromPoint(cameraPos);
			double distance2 = vertexes.get(tri[1]-1).getDistanceFromPoint(cameraPos);
			double distance3 = vertexes.get(tri[2]-1).getDistanceFromPoint(cameraPos);
			double average = (distance1 + distance2 + distance3) / 3;

			if (newTris.size() == 0) {
				newTris.add(tri);
				continue;
			}

			boolean added = false;

			for (int i = 0; i < newTris.size(); i++) {
				double otherDistance1 = vertexes.get(newTris.get(i)[0]-1).getDistanceFromPoint(cameraPos);
				double otherDistance2 = vertexes.get(newTris.get(i)[1]-1).getDistanceFromPoint(cameraPos);
				double otherDistance3 = vertexes.get(newTris.get(i)[2]-1).getDistanceFromPoint(cameraPos);
				double otherAverage = (otherDistance1 + otherDistance2 + otherDistance3) / 3;

				if (average < otherAverage) {
					newTris.add(i, tri);
					added = true;
					break;
				}
			}

			if (!added) {
				newTris.add(tri);
			}
		}

		for (int i = 0; i < triangles.size(); i++) {
			triangles.set(i, newTris.get(triangles.size() - i - 1));
		}
	}

	public double getDistanceFromCamera(Vector3d cameraPos) {
		sortTriangles(cameraPos);

		return vertexes.get(triangles.get(0)[0] - 1).getDistanceFromPoint(cameraPos);
	}

    public void setScale(double scale) {
		this.scale = scale;

		for (Vector3d vector : vertexes) {
			vector.scale(scale);
		}

		position.scale(scale);
	}

	public double getScale() {
		return scale;
	}

	public void setPosition(Vector3d newPos) {
		this.position = newPos;
	}

	public void adjustPosition(Vector3d newPos) {
		newPos.scale(scale);

		position.setX(position.getX() + newPos.getX());
		position.setY(position.getY() + newPos.getY());
		position.setZ(position.getZ() + newPos.getZ());
	}

	public Vector3d getPosition() {
		return position;
	}

	public void setRotation(double[] rotation) {
		this.rotation = rotation;
	}

	public void adjustRotation(double[] newRot) {
		rotation[0] += newRot[0];
		rotation[1] += newRot[1];
		rotation[2] += newRot[2];
	}

	public double[] getRotation() {
		return rotation;
	}

	public void drawToScreen(Graphics g, Vector3d cameraPos, double[] cameraAngle, int WIDTH, int HEIGHT, double fov) {
		ArrayList<Vector3d> tmpVertexes = new ArrayList<Vector3d>();

		for (Vector3d vertex : vertexes) {
			tmpVertexes.add(vertex.clone());
		}

		for (Vector3d vertex : tmpVertexes) {
			vertex.setX(vertex.getX() + position.getX());
			vertex.setY(vertex.getY() + position.getY());
			vertex.setZ(vertex.getZ() + position.getZ());
		}

		for (Vector3d vertex : tmpVertexes) {
			Vector3d.rotateX(vertex, rotation[0]);
			Vector3d.rotateY(vertex, rotation[1]);
			Vector3d.rotateZ(vertex, rotation[2]);
		}

		sortTriangles(cameraPos);
 
        for (int[] triangle : triangles) {
			Graphics2D g2 = (Graphics2D) g;

			Vector3d vertex1 = tmpVertexes.get(triangle[0]-1);
			Vector3d vertex2 = tmpVertexes.get(triangle[1]-1);
			Vector3d vertex3 = tmpVertexes.get(triangle[2]-1);

			double[] first = vertex1.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);
			double[] second = vertex2.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);
			double[] third = vertex3.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);

			double distance1 = vertex1.getDistanceFromPoint( new Vector3d(0, 100, 0) );
			double distance2 = vertex2.getDistanceFromPoint( new Vector3d(0, 100, 0) );
			double distance3 = vertex3.getDistanceFromPoint( new Vector3d(0, 100, 0) );
			int avgDistance = (int) (distance1 + distance2 + distance3) / 2;

			if (avgDistance < 0) {
				avgDistance = 0;
			}
			if (avgDistance > 255) {
				avgDistance = 255;
			}

			g2.setColor( new Color(avgDistance, avgDistance, 0) );

			Path2D triShape = new Path2D.Double();
			triShape.moveTo((int) first[0], (int) first[1]);
			triShape.lineTo((int) second[0], (int) second[1]);
			triShape.lineTo((int) third[0], (int) third[1]);
			triShape.closePath();
			g2.fill(triShape);
		}

		// for (Vector3d vertex : vertexes) {
		// 	Vector3d.rotateX(vertex, -rotation[0]);
		// 	Vector3d.rotateY(vertex, -rotation[1]);
		// 	Vector3d.rotateZ(vertex, -rotation[2]);
		// }

		// for (Vector3d vertex : vertexes) {
		// 	vertex.setX(vertex.getX() - position.getX());
		// 	vertex.setY(vertex.getY() - position.getY());
		// 	vertex.setZ(vertex.getZ() - position.getZ());
		// }
	}
}
