public class MatrixMath {
    public static double[][] add(double[][] one, double[][] two) {
        if (one.length != two.length || one[0].length != two[0].length) {
            return null;
        }

        double[][] result = new double[one.length][one[0].length];

        for (int row = 0; row < one.length; row++) {
            for (int col = 0; col < one[0].length; col++) {
                result[row][col] = one[row][col] + two[row][col];
            }
        }

        return result;
    }

    public static double[][] subtract(double[][] one, double[][] two) {
        if (one.length != two.length || one[0].length != two[0].length) {
            return null;
        }

        double[][] result = new double[one.length][one[0].length];

        for (int row = 0; row < one.length; row++) {
            for (int col = 0; col < one[0].length; col++) {
                result[row][col] = one[row][col] - two[row][col];
            }
        }

        return result;
    }

    double[][] multiply(double[][] one, double[][] two) {
        if (one[0].length != two.length) {
            return null;
        }

        double[][] result = new double[one.length][two[0].length];
    
        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                double cell = 0;
                for (int i = 0; i < two.length; i++) {
                    cell += one[row][i] * two[i][col];
                }
                result[row][col] = cell;
            }
        }
    
        return result;
    }
}
