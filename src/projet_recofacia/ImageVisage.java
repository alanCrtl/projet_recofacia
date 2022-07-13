package projet_recofacia;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageVisage {
	
	public static int[][] getMatrixOfImage(BufferedImage Image) {
	    int width = Image.getWidth(null);
	    int height = Image.getHeight(null);
	    int[][] pixels = new int[width][height];
	    
	    for (int i = 0; i < width; i++) {
	        for (int j = 0; j < height; j++) {
	        	// le pb avec int colour = img.getRGB(x, y); c'est qu'on a un entier 
	        	//qui correspond au code binaire de la couleur, c'est pas pratique
	        	//un type "Color" est mieux, c'est juste un vecteur avec les 3 
	        	//entier R,V,B correspondant
	        	Color mycolor = new Color(Image.getRGB(i, j));
	            pixels[i][j] = mycolor.getRed();
	        }
	    }

	    return pixels;
	}
	
	public static void createImage(int[][] matrice,String nomImg) throws IOException {
		
		BufferedImage image=null;
		image = new BufferedImage(matrice.length, matrice[0].length, BufferedImage.TYPE_INT_RGB);
		for (int i=0;i<matrice.length;i++) {
			for (int j=0;j<matrice[0].length;j++) {
				int teinte = matrice[i][j];
				Color newColor = new Color(teinte,teinte,teinte);
				int rgb = newColor.getRGB();
				image.setRGB(i,j,rgb);
				
			}
		}try{
		File output =new File(nomImg+".jpg");
		ImageIO.write(image,"jpg",output);
		}catch(IOException e) {}
	}
	

}