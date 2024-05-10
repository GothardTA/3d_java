import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Object3D {
    private ArrayList<Vector3d> vertexes;
	private ArrayList<int[]> triangles;
	private Vector3d position;
	private Color color;

    public Object3D(String filename, Vector3d position, Color color) {
        vertexes = ParseOBJ.getVertexesFromFile(filename);
        triangles = ParseOBJ.getTrianglesFromFile(filename);
		this.position = position;
		this.color = color;

		for (Vector3d vertex : vertexes) {
			vertex.setX(vertex.getX() + position.getX());
			vertex.setY(vertex.getY() + position.getY());
			vertex.setZ(vertex.getZ() + position.getZ());
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

    public void scaleAll(double scale) {
		for (Vector3d vector : vertexes) {
			vector.scale(scale);
		}
	}

	public void setPosition(Vector3d newPos) {
		this.position = newPos;
	}

	public void adjustPosition(Vector3d newPos) {
		position.setX(position.getX() + newPos.getX());
		position.setY(position.getY() + newPos.getY());
		position.setZ(position.getZ() + newPos.getZ());
	}

	public Vector3d getPosition() {
		return position;
	}

	public void drawToScreen(Graphics g, Vector3d cameraPos, double[] cameraAngle, int WIDTH, int HEIGHT, double fov) {
		sortTriangles(cameraPos);
 
        for (int[] triangle : triangles) {
			Graphics2D g2 = (Graphics2D) g;

			Vector3d vertex1 = vertexes.get(triangle[0]-1);
			Vector3d vertex2 = vertexes.get(triangle[1]-1);
			Vector3d vertex3 = vertexes.get(triangle[2]-1);

			double[] first = vertex1.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);
			double[] second = vertex2.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);
			double[] third = vertex3.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);

			g2.setColor(color);

			Path2D triShape = new Path2D.Double();
			triShape.moveTo((int) first[0], (int) first[1]);
			triShape.lineTo((int) second[0], (int) second[1]);
			triShape.lineTo((int) third[0], (int) third[1]);
			triShape.closePath();
			g2.fill(triShape);
		}
	}
}
