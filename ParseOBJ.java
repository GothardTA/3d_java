import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseOBJ {
    public static ArrayList<Vector3d> getVertexesFromFile(String path) {
        ArrayList<Vector3d> verticies = new ArrayList<Vector3d>();

        try {
            File objFile = new File(path);
            Scanner reader = new Scanner(objFile);

            while (reader.hasNextLine()) {
                String data = reader.nextLine();

                if (data.startsWith("v ")) {
                    String[] coords = data.split(" ");
                    double x = Double.parseDouble(coords[1]);
                    double y = Double.parseDouble(coords[2]);
                    double z = Double.parseDouble(coords[3]);

                    verticies.add( new Vector3d(x, y, z) );
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    
        return verticies;
    }
    
    public static ArrayList<int[]> getTrianglesFromFile(String path) {
        ArrayList<int[]> triangles = new ArrayList<int[]>();

        try {
            File objFile = new File(path);
            Scanner reader = new Scanner(objFile);

            while (reader.hasNextLine()) {
                String data = reader.nextLine();

                if (data.startsWith("f ")) {
                    String[] coords = data.split(" ");
                    int p1 = Integer.parseInt(coords[1].split("/")[0]);
                    int p2 = Integer.parseInt(coords[2].split("/")[0]);
                    int p3 = Integer.parseInt(coords[3].split("/")[0]);

                    int[] triangle = {p1, p2, p3};

                    triangles.add( triangle );
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    
        return triangles;
    }
}
