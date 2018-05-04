public class Matrix
{

    //Constructor to set our m x n matrix filled with empty tiles.
    public Matrix(int m, int n)
    {
        //Set the dimensions for the matrix
        this.m = m;
        this.n = n;
        matrix = new Tile[this.m][this.n];

        //Technically, the first set.
        reset();
    }
    public void setXY(Tile t, int x, int y)
    {
        //displaced by one because starting configuration is static.
        matrix[x + 1][y + 1] = t;
    }
    public Tile getTile(int x, int y)
    {
        //displaced by one because starting configuration is static.
        return matrix[x + 1][y + 1];
    }

    //Useful for testing.
    public void print()
    {
        for(int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                System.out.println("Tile " + "(" + i + "," + j + ")");
                matrix[i][j].print();
            }
        }
    }
    
    public int getM() { return m; }
    public int getN() { return n; }
    
    public void reset()
    {
      for(int i = 0; i < m; i++)
      {
        for(int j = 0; j < n; j++)
        {
            //Place an empty or seed tile at every position in matrix
            if(i == 0)
              matrix[i][j] = new Tile(-1, -1, 0, -1, -2);
            else if(j == 0)
              matrix[i][j] = new Tile(0, -1, -1, -1, -2);
            else
              matrix[i][j] = new Tile(-1, -1, -1, -1, -1);
        }
      }
    }
    
    //Given a tile t and a point (from a pattern), compare the glues
    //with the tile t and the bordering tile in the matrix.
    public boolean cmpW(Tile t, int i, int j)
    {  
    	if(t.getW() == matrix[i][j + 1].getE())
    		return true;
    	else
    		return false;
    }
    public boolean cmpS(Tile t, int i, int j)
    {
    	
      if(t.getS() == matrix[i + 1][j].getN())
        return true;
      else
        return false;
    }
    
    private int n, m;
    private Tile[][] matrix;
}
