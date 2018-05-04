import java.io.IOException;

public class Main
{

    public static void main(String[] args) throws IOException
    {
    	//Loop over all (wide or square) rectangular dimensions
    	for(int i = 2; i < 3;i++)
    	{
    		for(int j = 2; j <= i; j++)
    		{
    			new PATS(i, j, 2);
    		}
    
    	}
    }
}
