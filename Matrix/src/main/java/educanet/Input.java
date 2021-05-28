package educanet;

public class Input {
	String coos =
			"-0.19999999;-1.0;0.4\n" +
			"0.20000005;-1.0;0.4\n" +
			"0.6;-1.0;0.4\n" +
			"0.6;-0.6;0.4\n" +
			"-1.0;-0.19999999;0.4\n" +
			"-0.6;-0.19999999;0.4\n" +
			"-0.19999999;-0.19999999;0.4\n" +
			"0.6;-0.19999999;0.4\n" +
			"-1.0;0.20000005;0.4\n" +
			"-1.0;0.6;0.4\n" +
			"-0.6;0.6;0.4\n" +
			"-0.19999999;0.6;0.4";

	public float[][] getCoos() {
		String[] row = coos.split("\n");
		String[][] column = new String[row.length][3];
		float[][] output = new float[row.length][3];
		for(int a = 0; a < row.length; a++){
			column[a] = row[a].split(";");
			for (int b =0;b<column[a].length;b++){
				output[a][b] = Float.parseFloat(column[a][b]);
			}
		}
		return output;
	}
}
