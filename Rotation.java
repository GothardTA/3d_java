public class Rotation {
	public static double[] rotateX3D(double[] point, double angle) {
		double x = point[0];
		double y = point[1];
		double z = point[2];

		double[] newPoint = new double[3];
		newPoint[0] = point[0];
		newPoint[1] = point[1];
		newPoint[2] = point[2];

		newPoint[1] = (y * Math.cos(Math.toRadians(angle))) - (z * Math.sin(Math.toRadians(angle)));
		newPoint[2] = (z * Math.cos(Math.toRadians(angle))) + (y * Math.sin(Math.toRadians(angle)));

		return newPoint;
	}

	public static double[] rotateY3D(double[] point, double angle) {
		double x = point[0];
		double y = point[1];
		double z = point[2];

		double[] newPoint = new double[3];
		newPoint[0] = point[0];
		newPoint[1] = point[1];
		newPoint[2] = point[2];

		newPoint[0] = (x * Math.cos(Math.toRadians(angle))) - (z * Math.sin(Math.toRadians(angle)));
		newPoint[2] = (z * Math.cos(Math.toRadians(angle))) + (x * Math.sin(Math.toRadians(angle)));

		return newPoint;
	}

	public static double[] rotateZ3D(double[] point, double angle) {
		double x = point[0];
		double y = point[1];
		double z = point[2];

		double[] newPoint = new double[3];
		newPoint[0] = point[0];
		newPoint[1] = point[1];
		newPoint[2] = point[2];

		newPoint[0] = (x * Math.cos(Math.toRadians(angle))) - (y * Math.sin(Math.toRadians(angle)));
		newPoint[1] = (y * Math.cos(Math.toRadians(angle))) + (x * Math.sin(Math.toRadians(angle)));

		return newPoint;
	}

    public static double[] rotateX3DAroundPoint(double[] point1, double[] point2, double angle) {
        double[] vector = {point1[0]-point2[0], point1[1]-point2[1], point1[2]-point2[2]};
        double[] newPoint = new double[3];

        newPoint[0] = point1[0];
        newPoint[1] = point2[1] + ( Math.cos(Math.toRadians(angle)) * vector[1] ) - ( Math.sin(Math.toRadians(angle)) * vector[2] );
        newPoint[2] = point2[2] + ( Math.cos(Math.toRadians(angle)) * vector[2] ) + ( Math.sin(Math.toRadians(angle)) * vector[1] );

        return newPoint;
    }

    public static double[] rotateY3DAroundPoint(double[] point1, double[] point2, double angle) {
        double[] vector = {point1[0]-point2[0], point1[1]-point2[1], point1[2]-point2[2]};
        double[] newPoint = new double[3];

        newPoint[0] = point2[0] + ( Math.cos(Math.toRadians(angle)) * vector[0] ) - ( Math.sin(Math.toRadians(angle)) * vector[2] );
        newPoint[1] = point1[1];
        newPoint[2] = point2[2] + ( Math.cos(Math.toRadians(angle)) * vector[2] ) + ( Math.sin(Math.toRadians(angle)) * vector[0] );

        return newPoint;
    }

    public static double[] rotateZ3DAroundPoint(double[] point1, double[] point2, double angle) {
        double[] vector = {point1[0]-point2[0], point1[1]-point2[1], point1[2]-point2[2]};
        double[] newPoint = new double[3];

        newPoint[0] = point2[0] + ( Math.cos(Math.toRadians(angle)) * vector[0] ) - ( Math.sin(Math.toRadians(angle)) * vector[1] );
        newPoint[1] = point2[1] + ( Math.cos(Math.toRadians(angle)) * vector[1] ) + ( Math.sin(Math.toRadians(angle)) * vector[0] );
        newPoint[2] = point1[2];

        return newPoint;
    }
}
