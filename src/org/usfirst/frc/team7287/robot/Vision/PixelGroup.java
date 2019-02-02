package Vision;

public class PixelGroup 
{
	
	private static final int INCREMENT = 5;
	private boolean[][] pixels;
	
	private int smallestY;
	private int highestY;
	
	private int width;
	private int height;
	private int area;
	private int x;
	private int y;
	
	private int rank = 1;
	
	private float percentOfArea;
	private int[] center = new int[2];
	
	
	
	public PixelGroup(boolean[][] pixelArea, int xStart)
	{
		this.x = xStart;
		pixels = pixelArea;		
		width = pixels.length;
		
		highestY = 0; //starting highest is the bottom of array
		smallestY = pixels[0].length; //starting low is 0
		
		smallestY = findLowY();
		highestY = findHighY();
		y = smallestY;
		height = highestY - smallestY;
		
		area = width * height;
		
		center[0] = x + width/2;
		center[1] = y + height/2;
	}
	
	public void setRank(int r)
	{
		rank = r;
	}
	
	public int getRank()
	{
		return rank;
	}
	
	public void setAreaPercent(float percent) //the area this pixel group covers out of all the pixelGroups
	{
		percentOfArea = percent * 100;
	}
	
	public float getAreaPercent()
	{
		return percentOfArea;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int[] getCenter()
	{
		return center;
	}
	
	public int getArea()
	{
		return area;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	private int findLowY()
	{
		int lastFalse, r;
		boolean pixelFound;
		for (int c = 0; c < pixels.length; c += INCREMENT) //uses high increments to decrease processing time
		{
			lastFalse = 0;
			r = smallestY; //start from the last smallestY
			pixelFound = false;
			while (!pixelFound)
			{
				r -= INCREMENT;
				if (r < 0) break; //if array index goes outofbounds
				
				if (pixels[c][r]) //if there is a hit
				{
					int falseCounter = 0; //counts false pixel to see if the found pixel is close by others
					int lastSmallestY = smallestY;
					
					if (r < smallestY) smallestY = r; //test to see if r is the current smallest y
					
					for (int index = lastFalse; index < r; index ++) //go from the the lastFalse pixel to r pixel 
					{
						if (pixels[c][index])
						{
							smallestY = (index < smallestY)? index:smallestY;
							pixelFound = true;
							//if a pixel was found check pixels below as well to avoid a few pixels that were true
						}
						
					}
					
				}
				else
				{
					lastFalse = r;
				}
			}
		}
		
		return smallestY;
	}
	
	private int findHighY()
	{
		int lastFalse, j;
		boolean pixelFound = false;
		for (int i = 0; i < pixels.length; i += 5) //uses high increments to decrease processing time
		{
			lastFalse = 0;
			j = highestY; //start from the last smallestY
			pixelFound = false;
			while (!pixelFound)
			{
				j += 5;
				if (j > pixels[i].length - 1) return highestY; //if the new j value would cause a ArrayOutOfBoundsException return the highestY
				if (pixels[i][j]) //if there is a hit
				{
					if (j < smallestY) smallestY = j; //test to see if j is the current smallest y
					
					for (int index = j; index > lastFalse; index --) //
					{
						if (pixels[i][index])
						{
							highestY = (index > highestY)? index:highestY;
							pixelFound = true;
						}
					}
				}
				else
				{
					lastFalse = j;
				}
			}
		}
		
		return highestY;
	}

	
}
