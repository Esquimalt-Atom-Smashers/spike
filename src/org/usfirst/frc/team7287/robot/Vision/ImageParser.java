package Vision;

import org.opencv.core.Mat;

public class ImageParser 
{
	public static final int RED = 2;
	public static final int GREEN = 1;
	public static final int BLUE = 0;
	
	public static int[][][] pixelMap(Mat image)
	{
		final int width = image.width();
		final int height = image.height();
		
		int[][][] pixelMap = new int[width][height][3];
		
		for (int c = 0; c < width; c ++)
		{
			for (int r = 0; r < height; r ++)
			{
				double[] pixelRGB = image.get(r, c);
				pixelMap[c][r][RED] = (int) pixelRGB[RED];
				pixelMap[c][r][GREEN] = (int) pixelRGB[GREEN];
				pixelMap[c][r][BLUE] = (int) pixelRGB[BLUE];
			}
		}
		
		return pixelMap;
	}
}
