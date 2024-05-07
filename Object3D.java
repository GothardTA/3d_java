import java.util.ArrayList;

public class Object3D {
    private ArrayList<Vector3d> vertexes;
	private ArrayList<int[]> triangles;

    public Object3D(String filename) {
        vertexes = ParseOBJ.getVertexesFromFile(filename);
        triangles = ParseOBJ.getTrianglesFromFile(filename);
    }

    public void sortTriangles(ArrayList<int[]> tris, Vector3d cameraPos) {
		ArrayList<int[]> newTris = new ArrayList<int[]>();

		for (int[] tri : tris) {
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
				double otherAverage = (distance1 + distance2 + distance3) / 3;

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

		for (int i = 0; i < tris.size(); i++) {
			tris.set(i, newTris.get(tris.size() - i - 1));
		}
	}

    public void scaleAll()
}
