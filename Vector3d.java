public class Vector3d {
    private double x;
    private double y;
    private double z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // setters
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    // getters
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    // scale point
    public void scale(double amount) {
        x *= amount;
        y *= amount;
        z *= amount;
    }

    // convert to 2d
    public double[] perspective2D(Vector3d cameraPos, double[] cameraAngle, int screenWidth, int screenHeight, double fov) {
        // double n = 1.0 / Math.tan(Math.toRadians(fov));
        double n = 1.0 / Math.tan((fov * 3.14159 / 180.0) / 180.0);

        double point[] = {x, y, z};

		point[0] -= cameraPos.getX();
		point[1] -= cameraPos.getY();
		point[2] -= cameraPos.getZ();

        point = rotateXAroundPoint(point[0], point[1], point[2], cameraPos, cameraAngle[0]);
        point = rotateYAroundPoint(point[0], point[1], point[2], cameraPos, cameraAngle[1]);
        point = rotateZAroundPoint(point[0], point[1], point[2], cameraPos, cameraAngle[2]);

		double xp = (point[0] * n) / point[2];
		double yp = (point[1] * n) / point[2];

		// normalize the points to the center of the screen
		xp += screenWidth / 2;
		yp += screenHeight / 2;

		double[] point2d = {xp, yp};
		return point2d;
    }

    // rotation
	// private void rotateX(double xt, double yt, double zt, double angle) {
    //     // xt = xt;
	// 	yt = (yt * Math.cos(Math.toRadians(angle))) - (zt * Math.sin(Math.toRadians(angle)));
	// 	zt = (zt * Math.cos(Math.toRadians(angle))) + (yt * Math.sin(Math.toRadians(angle)));
	// }

	// private void rotateY(double xt, double yt, double zt, double angle) {
	// 	xt = (xt * Math.cos(Math.toRadians(angle))) - (zt * Math.sin(Math.toRadians(angle)));
    //     // yt = yt;
	// 	zt = (zt * Math.cos(Math.toRadians(angle))) + (xt * Math.sin(Math.toRadians(angle)));
	// }

	// private void rotateZ(double xt, double yt, double zt, double angle) {
	// 	xt = (xt * Math.cos(Math.toRadians(angle))) - (y * Math.sin(Math.toRadians(angle)));
	// 	yt = (yt * Math.cos(Math.toRadians(angle))) + (x * Math.sin(Math.toRadians(angle)));
    //     // zt = zt;
	// }

    // rotation around a point
    private double[] rotateXAroundPoint(double xt, double yt, double zt, Vector3d point, double angle) {
        double[] vector = {xt-point.getX(), yt-point.getY(), zt-point.getZ()};
        double[] newPoint = new double[3];

        newPoint[0] = xt;
        newPoint[1] = point.getY() + ( Math.cos(Math.toRadians(angle)) * vector[1] ) - ( Math.sin(Math.toRadians(angle)) * vector[2] );
        newPoint[2] = point.getZ() + ( Math.cos(Math.toRadians(angle)) * vector[2] ) + ( Math.sin(Math.toRadians(angle)) * vector[1] );

        return newPoint;
    }

    private double[] rotateYAroundPoint(double xt, double yt, double zt, Vector3d point, double angle) {
        double[] vector = {xt-point.getX(), yt-point.getY(), zt-point.getZ()};
        double[] newPoint = new double[3];

        newPoint[0] = point.getX() + ( Math.cos(Math.toRadians(angle)) * vector[0] ) - ( Math.sin(Math.toRadians(angle)) * vector[2] );
        newPoint[1] = yt;
        newPoint[2] = point.getZ() + ( Math.cos(Math.toRadians(angle)) * vector[2] ) + ( Math.sin(Math.toRadians(angle)) * vector[0] );

        return newPoint;
    }

    private double[] rotateZAroundPoint(double xt, double yt, double zt, Vector3d point, double angle) {
        double[] vector = {xt-point.getX(), yt-point.getY(), zt-point.getZ()};
        double[] newPoint = new double[3];

        newPoint[0] = point.getX() + ( Math.cos(Math.toRadians(angle)) * vector[0] ) - ( Math.sin(Math.toRadians(angle)) * vector[1] );
        newPoint[1] = point.getY() + ( Math.cos(Math.toRadians(angle)) * vector[1] ) + ( Math.sin(Math.toRadians(angle)) * vector[0] );
        newPoint[2] = zt;

        return newPoint;
    }
}
