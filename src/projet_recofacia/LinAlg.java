package projet_recofacia;

import static java.lang.Math.abs;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import Jama.Matrix;
import javafx.scene.control.ListView;



public class LinAlg {
	
	public static Matrix doubleToMatrix(double[][] mat) {
		Matrix A = new Matrix(mat.length,mat[0].length);
		for (int i=0;i<mat.length;i++) {
			for (int j=0;j<mat[0].length;j++) {
				A.set(i, j, mat[i][j]);
			}
		}
		return A;
	}
	
	public static double[][] MatrixToDouble(Matrix mat) {
		double[][] A = new double[mat.getRowDimension()][mat.getColumnDimension()];
		for (int i=0;i<mat.getRowDimension();i++) {
			for (int j=0;j<mat.getColumnDimension();j++) {
				A[i][j]=mat.get(i, j);
			}
		}
		return A;
	}
	
	public static double[][] convertVecProp(double[][] matVisage, double[][] matVecProp) {
		double[][] nouvMatVecProp = new double[matVisage.length][matVecProp[0].length];
		double[][] vecProp = new double[matVecProp.length][1];
		double[][] nouvVecProp = new double[matVisage.length][1];
		
//		System.out.println(matVisage.length+","+matVisage[0].length);
//		System.out.println(matVecProp.length+","+matVecProp[0].length);
		//pour chaque vecteur propre, pour toutes leur valeurs
		for(int j=0;j<matVecProp[0].length;j++) {
			for (int i=0;i<matVecProp.length;i++) {
				vecProp[i][0] = matVecProp[i][j];
				
			}
			nouvVecProp = Visage.produitMatrice(matVisage, vecProp);
//			Visage.affichageMatriceDouble(nouvVecProp);
			Visage.addVectorToMatrixV2(nouvMatVecProp, nouvVecProp, j);
		}
		
		
		return nouvMatVecProp;
	}
	
	public static void methDuCoude (double[] valProp) {
		double somme = 0;
		double[][] tableauCoude = new double[valProp.length][3];
		for (int i = 0;i<valProp.length;i++) {
			somme += valProp[i];
		}
		System.out.println("\nSomme des valeurs propres: "+somme);
		double infoCumu = 0;
		for (int i = 0;i<valProp.length;i++) {
			infoCumu += valProp[valProp.length-i-1]/somme;
			tableauCoude[i][0] = infoCumu;
			tableauCoude[i][1] = valProp[valProp.length-i-1];
			tableauCoude[i][2] = i+1;
		}
		Visage.affichageMatriceDouble(tableauCoude);
	}
	
	public static int[] vectARogner(double[] valProp, double crit) {
		int[] listVectAEjecter = new int[valProp.length];
		for (int i=0;i<valProp.length;i++) {
			if (abs(valProp[i])<crit) {
//				System.out.println("le vect prop n°"+i+"="+valProp[i]+" va être retirer, car val prop plus petite que le critere="+crit);
				listVectAEjecter[i]=1;
			}else {
				listVectAEjecter[i]=0;
			}
		}
		return listVectAEjecter;
	}
	
	public static double[][] rognerVecProp(double[][] matVecProp, double[] valProp, double crit) {
		int[] list = vectARogner(valProp, crit);
		int nbVecGarde = 0;
		for (int i = 0;i<list.length;i++) {
			if (list[i]==0) {
				nbVecGarde++;
			}
		}
		System.out.println("-->On garde "+nbVecGarde+" vecteur propre qui ont l'importance la plus grande selon leurs valeurs propres associées");
		double[][] nouvMatVecProp = new double[matVecProp.length][nbVecGarde];
		int compt = 0;
		for (int j = 0; j<list.length; j++) {
			if (list[j]==0) {
				double[] vecPropGarde = getColumnFromMatrix(matVecProp, j);
				Visage.addVectorToMatrix(nouvMatVecProp, vecPropGarde, compt);
				compt++;
			}
		}
		return nouvMatVecProp;
	}
	
	public static double[] getColumnFromMatrix(double[][] mat, int pos) {
		double[] vec = new double[mat.length];
		for (int i=0;i<mat.length;i++) {
			vec[i] = mat[i][pos];
		}
		return vec;
	}
	
	public static double produitScalaireVisage(double[] visage, double[] vecProp) {
		double alpha = 0;
		for (int i=0;i<visage.length;i++) {
			alpha = alpha + (visage[i] * vecProp[i]);
		}
		return alpha;
	}
	
	public static double[][] matriceAlpha(double[][] matVisage, double[][] matVecProp) {
		double[][] matAlpha = new double[matVecProp[0].length][matVisage[0].length];
		System.out.println("\nDimensions matAlpha: "+matAlpha.length+","+matAlpha[0].length);
		
		for (int i=0;i<matAlpha.length;i++) {
			for (int j=0;j<matAlpha[0].length;j++) {
				double[] visage = getColumnFromMatrix(matVisage, j);
				double[] vecProp = getColumnFromMatrix(matVecProp, i);
				double n = Visage.normeVecteur(vecProp);
				double alpha = produitScalaireVisage(visage,vecProp);
				alpha = alpha/(n*n);
//				System.out.println("norme, alpha:"+n+"  ,  "+alpha);
				matAlpha[i][j] = alpha;
//				System.out.println("alpha: "+alpha);
			}
		}

		
		return matAlpha;
	}
	
	
	
	public static double[] projeteVisage(double[][] matAlpha, double[][] matVecProp, int numVis) {
		double[] visageProjete = new double[matVecProp.length];
		
		for (int j=0; j<matAlpha.length; j++) {
			for (int i=0; i<matVecProp.length; i++) {
				visageProjete[i] = visageProjete[i] + matAlpha[j][numVis]*matVecProp[i][j];
			}
			
		}
		return visageProjete;
	}
	public static double[] projeteVisage(double[] visage, double[][] matVecProp) {
		double[] visageProjete = new double[matVecProp.length];
		for (int j=0; j<matVecProp[0].length; j++) {
			double[] vecProp = LinAlg.getColumnFromMatrix(matVecProp, j);
			double n = Visage.normeVecteur(vecProp);
			double alpha = produitScalaireVisage(visage, vecProp);
			alpha = alpha/(n*n);
			for (int i=0; i<matVecProp.length; i++) {
				visageProjete[i] = visageProjete[i] +  vecProp[i]*alpha;
			}
			
		}
		return visageProjete;
	}
	
	public static double erreurReconstruc(double[] visageOriginal, double[] visageProj) {
		double[] visageDiff = new double[visageOriginal.length];
		for (int i = 0; i<visageOriginal.length;i++) {
			visageDiff[i] = visageOriginal[i] - visageProj[i];
		}
		double norme;
		norme = Visage.normeVecteur(visageDiff);
		return norme;
	}
	
	public static double[] identificationVis(double[] visageProjTest, double[][] matAlpha, double[][] matVecProp, String[] idVis, ListView<String> maListe) {
		int nbImages = matAlpha[0].length;
		double[] ecarts = new double[nbImages];
//		Visage.affichageVecteurDouble(visageProjTest);
		//pour chaque visage projeté
		for (int i=0;i<nbImages;i++) {
			double[] visageProjete = LinAlg.projeteVisage(matAlpha, matVecProp, i);
//			Visage.affichageVecteurDouble(visageProjete);
			double somme = 0;
			//pour chaque pixel on mesure la dist quadratique
			for (int j=0;j<visageProjete.length;j++) {
				double ecartpix = 0;
				ecartpix=visageProjTest[j]-visageProjete[j];
				somme+=ecartpix*ecartpix;
			}
			ecarts[i]=Math.sqrt(somme);
		}
//		Visage.affichageVecteurDouble(ecarts);
		double[] minEtInd=new double[2];
		minEtInd[0]=ecarts[0];
		minEtInd[1]=0;
		System.out.println("---------------\n");
		for (int j=0;j<ecarts.length;j++) {
//			System.out.println(ecarts[j]);
			if (ecarts[j]<minEtInd[0]) {
				minEtInd[0]=ecarts[j];
				minEtInd[1]=j;
			}
			
		}
		ByteArrayOutputStream txtconsole = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(txtconsole);
		PrintStream old = System.out;
		System.setOut(ps);
		if(minEtInd[0]>0.98) {
					System.out.println("visage non reconnu");
		} else {
			System.out.println("visage est reconnu");
		}
		System.out.println("["+idVis[(int) minEtInd[1]]+"]"+" - [valeur de l'ecart]: "+minEtInd[0]);
		maListe.getItems().add(txtconsole.toString());
		txtconsole.reset();
		System.out.flush(); //pour retourner à la console normale
		System.setOut(old);
		return(minEtInd);
	}
	public static double[] identificationVis(double[] visageProjTest, double[][] matAlpha, double[][] matVecProp, String[] idVis) {
		int nbImages = matAlpha[0].length;
		double[] ecarts = new double[nbImages];
		//pour chaque visage projeté
		for (int i=0;i<nbImages;i++) {
			double[] visageProjete = LinAlg.projeteVisage(matAlpha, matVecProp, i);
			double somme = 0;
			//pour chaque pixel on mesure la dist quadratique
			for (int j=0;j<visageProjete.length;j++) {
				double ecartpix = 0;
				ecartpix=visageProjTest[j]-visageProjete[j];
				somme+=ecartpix*ecartpix;
			}
			ecarts[i]=Math.sqrt(somme);
		}
//		Visage.affichageVecteurDouble(ecarts);
		double[] minEtInd=new double[2];
		minEtInd[0]=ecarts[0];
		minEtInd[1]=0;
		for (int j=0;j<ecarts.length;j++) {
			if (ecarts[j]<minEtInd[0]) {
				minEtInd[0]=ecarts[j];
				minEtInd[1]=j;
			}
			
		}
		System.out.println("\n"+idVis[(int) minEtInd[1]]+", valeur de l'ecart: "+minEtInd[0]);
		return(minEtInd);
	}
	
	
	public static double trouveMax(double[] visage) {
		double max = 0;
		for (int i = 0; i<visage.length;i++) {
			if (visage[i]>max) {
				max = visage[i];
			}
			
		}
		return max;
	}
	public static double trouveMin(double[] visage) {
		double min = 1;
		for (int i = 0; i<visage.length;i++) {
			if (visage[i]<min) {
				min = visage[i];
			}
			
		}
		return min;
	}
	
	public static void decentrageEtDenormalisation(double[] visage, double[] visMoy) {
		for (int i = 0; i<visage.length; i++) {
			visage[i]=visage[i]+visMoy[i];
		}
		double max = trouveMax(visage);
		for (int i = 0; i<visage.length; i++) {
			visage[i]=visage[i]*(255/max);
		}
		for (int i = 0; i<visage.length; i++) {
			if (visage[i] > 255) {
				visage[i] = 255;
			}else if(visage[i] < 0) {
				visage[i] = 0;
			}
		}
	}

	
}





