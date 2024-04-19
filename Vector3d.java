public class Vector3d {
    private double startX;
    private double startY;
    private double startZ;

    private double x;
    private double y;
    private double z;

    public Vector3d(double x, double y, double z) {
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        
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
    public double[] perspective2D(Vector3d cameraPos, int width, int height, double fov) {
        // double n = 1.0 / Math.tan(Math.toRadians(fov));
        double n = 1.0 / Math.tan((fov * 3.1415 / 180.0) / 180.0);

        double xt = x;
		double yt = y;
		double zt = z;

		xt -= cameraPos.getX();
		yt -= cameraPos.getY();
		zt -= cameraPos.getZ();

		double xp = (xt * n) / zt;
		double yp = (yt * n) / zt;

		// normalize the points to the center of the screen
		xp += width / 2;
		yp += height / 2;

		double[] point2d = {xp, yp};
		return point2d;
    }

    // rotation
	public void rotateX(double angle) {
        // this.x = x;
		this.y = (y * Math.cos(Math.toRadians(angle))) - (z * Math.sin(Math.toRadians(angle)));
		this.z = (z * Math.cos(Math.toRadians(angle))) + (y * Math.sin(Math.toRadians(angle)));
	}

	public void rotateY(double angle) {
		this.x = (x * Math.cos(Math.toRadians(angle))) - (z * Math.sin(Math.toRadians(angle)));
        // this.y = y;
		this.z = (z * Math.cos(Math.toRadians(angle))) + (x * Math.sin(Math.toRadians(angle)));
	}

	public void rotateZ(double angle) {
		this.x = (x * Math.cos(Math.toRadians(angle))) - (y * Math.sin(Math.toRadians(angle)));
		this.y = (y * Math.cos(Math.toRadians(angle))) + (x * Math.sin(Math.toRadians(angle)));
        // this.z = z;
	}

    // rotation around a point
    public void rotateXAroundPoint(Vector3d point, double angle) {
        double[] vector = {x-point.getX(), y-point.getY(), z-point.getZ()};

        // this.x = x;
        this.y = point.getY() + ( Math.cos(Math.toRadians(angle)) * vector[1] ) - ( Math.sin(Math.toRadians(angle)) * vector[2] );
        this.z = point.getZ() + ( Math.cos(Math.toRadians(angle)) * vector[2] ) + ( Math.sin(Math.toRadians(angle)) * vector[1] );
    }

    public void rotateYAroundPoint(Vector3d point, double angle) {
        double[] vector = {x-point.getX(), y-point.getY(), z-point.getZ()};

        this.x = point.getX() + ( Math.cos(Math.toRadians(angle)) * vector[0] ) - ( Math.sin(Math.toRadians(angle)) * vector[2] );
        // this.y = y;
        this.z = point.getZ() + ( Math.cos(Math.toRadians(angle)) * vector[2] ) + ( Math.sin(Math.toRadians(angle)) * vector[0] );
    }

    public void rotateZAroundPoint(Vector3d point, double angle) {
        double[] vector = {x-point.getX(), y-point.getY(), z-point.getZ()};

        this.x = point.getX() + ( Math.cos(Math.toRadians(angle)) * vector[0] ) - ( Math.sin(Math.toRadians(angle)) * vector[1] );
        this.y = point.getY() + ( Math.cos(Math.toRadians(angle)) * vector[1] ) + ( Math.sin(Math.toRadians(angle)) * vector[0] );
        // this.z = z;
    }

    // reset points
    public void reset() {
        x = startX;
        y = startY;
        z = startZ;
    }
}
