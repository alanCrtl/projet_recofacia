package projet_recofacia;
import static java.lang.Math.sqrt;
import java.text.DecimalFormat;



//organisation du des methodes:
//fonctions de conversion
//fonctions de transformation
//fonctions de creation
//fonctions d'affichage


public class Visage {
	
	static DecimalFormat numberFormat = new  DecimalFormat("0.00000");
	//-------------------------------------------------------------------------------
	//fonctions de conversions 
	public static int[] matrixToVector(int[][] matrice) {
		int hauteur = matrice.length;
		int largeur = matrice[0].length;
		int longueurVecteur = hauteur*largeur;
		int[] vecteurVisage = new int[longueurVecteur];
		int k=0;
		for(int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				vecteurVisage[k]=matrice[i][j];
				k++;
			}
		}
		return(vecteurVisage);
	}
	
	public static int[][] vectorToMatrix(double[] vector,int dimensionMatrice) {
		int[][] matriceVisage= new int[dimensionMatrice][dimensionMatrice];
		int k=0;
		
		for (int i = 0; i < dimensionMatrice; i++) {
			for (int j = 0; j < dimensionMatrice; j++) {
				matriceVisage[i][j]=(int) vector[k];
				k++;
			}
		}
		return matriceVisage;
	}
	
	//-------------------------------------------------------------------------------
	//fonctions de transformation
	public static void addVectorToMatrix(double[][] matrice, double[] vecteur, int position) {
		for (int i=0;i<vecteur.length;i++) {
			matrice[i][position]=vecteur[i];
		}
	}
	public static void addVectorToMatrixV2(double[][] matrice, double[][] vecteur, int position) {
		for (int i=0;i<vecteur.length;i++) {
			matrice[i][position]=vecteur[i][0];
		}
	}
	
	public static double[][] transposeMatrix(double[][] matrice) {
		int hauteur=matrice.length;
		int largeur=matrice[0].length;
		double[][] matrixTransposed=new double[largeur][hauteur];
		
		for (int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				matrixTransposed[j][i]=matrice[i][j];
			}
		}
		return matrixTransposed;
	}
	
	
	public static double[][] produitMatrice(double[][] A, double[][] B){
		int hauteur1 = A.length;
		int largeur1 = A[0].length;
//		int hauteur2 = B.length;
		int largeur2 = B[0].length;
		double[][] res=new double[hauteur1][largeur2];
//		int nbCalcul = 0;
		for (int i=0;i<hauteur1;i++) {
			for (int j=0;j<largeur2;j++) {
				for (int k=0;k<largeur1;k++) {
					res[i][j] = res[i][j] + A[i][k]*B[k][j];
//					nbCalcul++;
				}
			}
		}
//		System.out.println("Le produit a effectué "+nbCalcul+" opérations");
//		System.out.println("Dimensions nouvelle matrice: "+hauteur1+";"+largeur2);
		return res;
	}
	
	
	public static double normeVecteur(double[] vecteur) {
		double norme=0;
		for (int i=0;i<vecteur.length;i++) {
			norme+=vecteur[i]*vecteur[i];
		}
		norme=sqrt(norme);
		return norme;
	}
	
	
	public static double[] normaliseVecteur(int[] vecteur) {
		//converti le vecteur en double[], pour avoir des valeurs entre 0 et 1
		double[] nouvVecteur = new double[vecteur.length];
		for (int i=0;i<vecteur.length;i++) {
			nouvVecteur[i]=vecteur[i];
		}
		//on calcule la norme et on divise le vecteur par sa norme
		double norme=normeVecteur(nouvVecteur);
//		System.out.println("la norme du vecteur: "+norme);
		for (int i=0;i<vecteur.length;i++) {
			nouvVecteur[i]=nouvVecteur[i]/norme;
		}
		return nouvVecteur;
	}
	
	public static void centrageVisages(double[][] matVisages, double[] visMoy) {
		int hauteur=matVisages.length;
		int largeur=matVisages[0].length;
		
		//pour chaque visages, pour chaque pixels
		for (int i=0;i<hauteur;i++) {
			for (int j=0;j<largeur;j++) {
				matVisages[i][j]=matVisages[i][j]-visMoy[i];
			}
		}
	}
	public static void centrerVisage(double[] visage, double[] visMoy) {
		for (int i =0;i<visage.length;i++) {
			visage[i]-=visMoy[i];
		}
	}
	
	//-------------------------------------------------------------------------------
	//fonctions de creation
	public static double[] getVisageMoyen(double[][] matrice, int nbImages) {
		double[] visageMoyen= new double[matrice.length];
		for (int i=0;i<matrice.length;i++) {
			double pixelMoyen=0;
			for (int j=0;j<nbImages;j++) {
				pixelMoyen=pixelMoyen+matrice[i][j];
			}
			pixelMoyen=pixelMoyen/nbImages;
			visageMoyen[i]=pixelMoyen;
		}
		return visageMoyen;
	}
	
	
	
	//-------------------------------------------------------------------------------
	//fonctions d'affichages (certaines boucles sont raccourcies pour plus de lisibilité dans la console)
	public static void affichageMatrice(int[][] matrice) {
		int height=matrice.length;
		int width=matrice[0].length;
		for (int i = 0; i < height; i++) {
			System.out.print("(");
	        for (int j = 0; j < width; j++) {
	            System.out.print(matrice[i][j]+",");
	        }
	        System.out.println(")");
	    }
	}
	
	public static void affichageMatriceDouble(double[][] matrice) {
		int height=matrice.length;
		int width=matrice[0].length;
		for (int i = 0; i < height; i++) {
			System.out.print("(");
	        for (int j = 0; j < width; j++) {
	            System.out.print(numberFormat.format(matrice[i][j])+", ");
	        }
	        System.out.println(")");
	    }
		System.out.println("dimension: "+height+","+width);
	}
	
	public static void affichageVecteur(int[] vecteur) {
		for (int i=0;i<vecteur.length;i++) {
			System.out.print("("+vecteur[i]+")");
		}
	}
	
	public static void affichageVecteurDouble(double[] vecteur) {
		for (int i=0;i<100;i++) {
			System.out.print("("+vecteur[i]+")");
		}
	}
	
	
}
	

