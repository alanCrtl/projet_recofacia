package projet_recofacia;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//import java.util.Scanner;
import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;

//import Jama.Matrix;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
//import javafx.scene.paint.Color;
//import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import Jama.EigenvalueDecomposition;


// RESTE A ENREGISTRER VECTEUR DES ID 

public class Main extends Application {
	//elements de l'interface
	private Button choixImg;
	private Button lancerIden;
	private BufferedImage imgChoisi;
	//elements obtenu lors des calculs dans le main, maintenant lu depuis des fichiers csv
//	private double[][]matAlphaDeTest = CSVinteract.fromFileToMat("csv/matAlphaVisTest.csv");
	private double[][] matAlpha = CSVinteract.fromFileToMat("csv/matAlpha.csv");
	private double[][] matVectPropRogne = CSVinteract.fromFileToMat("csv/matVecProp_k14.csv");
	public static double[] realPart = CSVinteract.fromFileToVec("csv/valPropres.csv");
	private double[] visageMoyen = CSVinteract.fromFileToVec("csv/visMoy.csv");
//	private double[][] matVectPropConverted= CSVinteract.fromFileToMat("csv/matVecPropTous.csv");
	private String[] idVisa = CSVinteract.fromFileToStrVec("csv/id_visa.csv");
	//compteur d'element d'interface
	private int nbelt=0;
	
	//interface graphique
	public void start(Stage stage) throws Exception{
		//pour afficher les instructions à l'utilisateur
		TextField retourTexte = new TextField();
		retourTexte.setText("bienvenue !");
		retourTexte.setEditable(false);
		retourTexte.prefHeight(80);
		retourTexte.prefWidth(200);
		//permet de selectionner les images
		FileChooser fileChooser = new FileChooser();
		//permet de recuperer des informations à afficher
		ListView<String> maListe = new ListView<String>();
		maListe.setOrientation(Orientation.VERTICAL);
		maListe.setPrefSize(800,300);
		//boutons de fonctions
		choixImg = new Button("Choisissez une image");
		lancerIden = new Button("Identifier");
		
		//scene principal
		FlowPane menuPr = new FlowPane();
		menuPr.setPadding(new Insets(50,20,50,20));
		menuPr.setVgap(5);
		menuPr.setHgap(5);
		menuPr.setAlignment(Pos.CENTER);
		String ttxt1 = "Interface reconnaissance faciale - ";
		Text title1 = new Text();
		title1.setText(ttxt1);
		menuPr.getChildren().add(title1);nbelt++;
		menuPr.getChildren().add(choixImg);nbelt++;
		menuPr.getChildren().add(lancerIden);nbelt++;
		menuPr.getChildren().add(retourTexte);nbelt++;
		menuPr.getChildren().add(maListe);nbelt++;
		Text text = new Text("[Image choisie]  | ");
		Text text2 = new Text("[Image identifiée]");
		menuPr.getChildren().add(text);nbelt++;
		menuPr.getChildren().add(text2);nbelt++;
		
		Scene menuPrincip = new Scene(menuPr,900,600);
		
		//fonction boutons
		choixImg.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (nbelt+1<menuPr.getChildren().size()) {
					menuPr.getChildren().remove(nbelt+1);
				}
				if (nbelt<menuPr.getChildren().size()) {
					menuPr.getChildren().remove(nbelt);
				}
				File file = fileChooser.showOpenDialog(stage);
				retourTexte.setText(file.getPath());
				try {
					imgChoisi = ImageIO.read(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}//pour après
				Image img = new Image(file.toURI().toString());
				ImageView imageView1 = new ImageView();
				imageView1.setImage(img);
				imageView1.setFitHeight(200);
			    imageView1.setFitWidth(200);
				menuPr.getChildren().add(imageView1);
			}
		});
		lancerIden.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				int[][] mat = ImageVisage.getMatrixOfImage(imgChoisi);
				int[] vec = Visage.matrixToVector(mat);
				double[] visage = Visage.normaliseVecteur(vec);
				Visage.centrerVisage(visage, visageMoyen);
				double[] visageProj = LinAlg.projeteVisage(visage,matVectPropRogne);
				double[] minEtInd = LinAlg.identificationVis(visageProj, matAlpha, matVectPropRogne, idVisa, maListe);
				//affichage du visage le plus proche
				int indVis = (int) minEtInd[1];
				File fichiers = new File("BaseImages/Reference2/");
				File[] baseImage=fichiers.listFiles();
				for (File file : baseImage) {
					if (file.getName().equals(idVisa[indVis])) {
						Image img = new Image(file.toURI().toString());
						ImageView imageView2 = new ImageView();
						imageView2.setImage(img);
						imageView2.setFitHeight(200);
					    imageView2.setFitWidth(200);
						menuPr.getChildren().add(imageView2);
					}
				}
				
			}
		});
		//Mise en place de la scene
		stage.setTitle("Reconnaissance faciale - JavaFX");
		stage.setScene(menuPrincip);
		stage.show();
	}
	
	
	//FONCTION MAIN
	//_______________________________________________________________________________________________________________________
	public static void main(String[] args) throws IOException, InterruptedException   {
	/*	//on initialise les dimensions des images(seule chose à changer si votre base de données change de taille)
		int width=50;
		int height=50;
		int qtePixel = height*width;
		
		//creation d'une image vide
		BufferedImage image=null;
		
		//on charge les bases d'image (bien verifier que le chemin correponde)
		File fichiers = new File("BaseImages/Reference2/");
		File tests = new File("BaseImages/Test2/");
		File[] baseImage=fichiers.listFiles();
		File[] baseTest=tests.listFiles();
		
		//on compte le nombre d'image
		int nbImages=0;
		for (@SuppressWarnings("unused") File file : baseImage){
			nbImages++;
		}
		System.out.println("\nNombre d'images dans la base: "+nbImages+"\nNous allons tous d'abord récupérer les images dans les bases de données");
		
		//on compte le nombre d'images test
		int nbImTest=0;
		for (@SuppressWarnings("unused") File file : baseTest) {
			nbImTest++;
		}
		
		//pour compter le nombre d'image converti traitée (ie c'est le numéro de l'image en cours de traitement)
		int compteurImage=0; 
		int comptImTest=0;
		
		//la matrice des visages de reference. Contient les vecteurs visages en colonne
		double[][] matriceDesVisages= new double[qtePixel][nbImages];
		
		//matrice des visages de test
		double[][] matVisTest = new double[qtePixel][nbImTest];
		
		//norme moyenne (pas forcément utile)
		int normeMoy=0;
		
		//norme des visagee
		int[] normeDesVisages = new int[nbImages];
		
		//id des visages
		String[] idVisa = new String[nbImages];
		
		//----------------------------------------Debut traitement des images----------------------------------------
		//pour chaque image dans la base de reference
		for (File file : baseImage) {
	
			File sourceimage = file;
			idVisa[compteurImage]=sourceimage.getName();
			System.out.println("\n\n---\nimage n°: "+compteurImage);
			
			//on prepare l'image
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			image = ImageIO.read(sourceimage);
			
			
			//on traduit l'image en matrice
			int[][] matrice;
			matrice = ImageVisage.getMatrixOfImage(image);
			System.out.println(">MATRICE OBTENU DEPUIS L'IMAGE");
			//affiche la matrice
//			Visage.affichageMatrice(matrice);
//			ImageVisage.createImage(matrice,"/home/cytech/Desktop/test"+compteurImage);
			
			//on converti la matrice en vecteur
			int[] vecteurImage;
			vecteurImage = Visage.matrixToVector(matrice);
			System.out.println(">VECTEUR OBTENU DEPUIS LA MATRICE");
//			Visage.affichageVecteur(vecteurImage);
			
			
			//calcul norme moyenne et ajout de la norme du vecteur aux vecteur des normes (nécessite conversion en type double[] au préalable)
			double[] vecteurImageD= new double[qtePixel];
			for (int i=0;i<vecteurImageD.length;i++) {
				vecteurImageD[i]=vecteurImage[i];
			}
			double v = Visage.normeVecteur(vecteurImageD);
			normeDesVisages[compteurImage] = (int) v;
			normeMoy+=v;
			
			
			//normalisation du vecteur 
			double[] vecteurNormalise=new double[qtePixel];
			vecteurNormalise = Visage.normaliseVecteur(vecteurImage);
			System.out.println(">VECTEUR NORMALISE");
			Visage.affichageVecteurDouble(vecteurNormalise);
			//on verifie que la norme vaut 1
			double normeQuiVaut1=Visage.normeVecteur(vecteurNormalise); 
			System.out.println("\nLa norme doit valoir 1: "+normeQuiVaut1);
			
			//on ajoute le vecteur visage à la matrice des visages
			Visage.addVectorToMatrix(matriceDesVisages, vecteurNormalise, compteurImage);
			System.out.println(">VECTEUR AJOUTE DANS LA MATRICE DES VISAGES (à la position pos="+compteurImage+")");
			for (int i = 0; i < 20; i++) {
				System.out.print("{"+matriceDesVisages[i][compteurImage]+"}");
		    }
			
			
//			Thread.sleep(2000);
			compteurImage++;
		
		} 
		CSVinteract.writeStrVecToFile(idVisa, "csv/id_visa.csv");
		
		System.out.println("\n\n-------------------------------------LECTURE DE LA BASE DE TEST-------------------------------------");
		//pour chaque image de la base de test
		for (File file : baseTest) {
			File sourceimage = file;
			System.out.println("\n---\nimage test n°: "+comptImTest);
			
			//on prepare l'image
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			image = ImageIO.read(sourceimage);
			
			//on traduit l'image en matrice
			int[][] matrice;
			matrice = ImageVisage.getMatrixOfImage(image);
			System.out.println(">MATRICE OBTENU DEPUIS L'IMAGE");
			
			//on converti la matrice en vecteur
			int[] vecteurImage;
			vecteurImage = Visage.matrixToVector(matrice);
			System.out.println(">VECTEUR OBTENU DEPUIS LA MATRICE");
//			Visage.affichageVecteur(vecteurImage);
			
			//normalisation du vecteur 
			double[] vecteurNormalise=new double[qtePixel];
			vecteurNormalise = Visage.normaliseVecteur(vecteurImage);
			System.out.println(">VECTEUR NORMALISE");
			//on verifie que la norme vaut 1
			double normeQuiVaut1=Visage.normeVecteur(vecteurNormalise); 
			System.out.println("La norme doit valoir 1: "+normeQuiVaut1);
			
			//on ajoute le vecteur visage à la matrice des visages
			Visage.addVectorToMatrix(matVisTest, vecteurNormalise, comptImTest);
			System.out.println(">VECTEUR AJOUTE DANS LA MATRICE DES VISAGES DE TEST (à la position pos="+comptImTest+")");
			
			
			
			comptImTest++;
		}//----------------------------------------fin boucles image----------------------------------------
		
		
		
		System.out.println("\n\n------------------------------------------------------------------------------------"
				+ "\nFIN DE LA CAPTURE DES IMAGES DE LA BASE DE REFERENCE ET DE TEST");
//		System.out.println("\nnorme des visages: ");
//		Visage.affichageVecteur(normeDesVisages);
//		normeMoy=normeMoy/nbImages;
//		System.out.println("\nnormeMoy = "+normeMoy);
		
		
		//---calcule du visage moyen à partir de la matrice des visages---
		double[] visageMoyen=new double[qtePixel];
		//Une deuxieme version pour en faire une image
		double[] visageMoyenEnImage=new double[qtePixel];
		visageMoyen=Visage.getVisageMoyen(matriceDesVisages,nbImages);
		visageMoyenEnImage=Visage.getVisageMoyen(matriceDesVisages,nbImages);
		System.out.println("\n>Visage moyen obtenu");
		CSVinteract.writeMatToFile(visageMoyen, "csv/visMoy.csv");
		
		//on "dénormalise" le visage moyen en le multipliant par la norme moyenne afin de traduire 
		//la matrice en une image jpg
		for (int i=0;i<visageMoyenEnImage.length;i++) {
			visageMoyenEnImage[i]=visageMoyenEnImage[i]*normeMoy;
		}
		//on génère le visage moyen
//		int[][] visageMoyenImageMat=Visage.vectorToMatrix(visageMoyenEnImage,width);
		//marche pas
//		ImageVisage.createImage(visageMoyenImageMat,"LeVisageMoyen");
		System.out.println("Visage moyen généré dans le dossier du projet");
		
		
		//---centrage des visages (retirer le visage moyen à chaque visage)---
//		Visage.centrageVisages(matriceDesVisages,visageMoyen);
		System.out.println("\n>Matrice des Visages a été centrée");
//		Visage.affichageMatriceDouble(matriceDesVisages);
		
		
		//---faire MatVisageTranspose*MatVisage (dim=46*46)---
		//matrice covariance des visages (qui contient les inerties des pixels sur la diagonale)
		double[][] matVisageTransp=new double[nbImages][qtePixel];
		matVisageTransp=Visage.transposeMatrix(matriceDesVisages);
//		Visage.affichageMatriceDouble(matVisageTransp);
		
		Matrix A1 = LinAlg.doubleToMatrix(matriceDesVisages);
		Matrix A2 = LinAlg.doubleToMatrix(matVisageTransp);
		Matrix C = A2.times(A1);
		System.out.println("\n>Produit ATranspose.A EFFECTUE (où A est la matrice des visages)");
//		C.print(6, 4);
		
		
		//---obtenir valeurs propres de la matrice ci-dessus (matCovariance)---
		EigenvalueDecomposition decomp = C.eig();
		double[] realPart = decomp.getRealEigenvalues();
		CSVinteract.writeMatToFile(realPart, "csv/valPropres.csv");
		
		double[] imagPart = decomp.getImagEigenvalues();
		System.out.println("\n------------\n-->Decomposition valeurs propres effectué sur la matrice de covariance\n");
		for(int i = 0; i < realPart.length; i++) {
		    System.out.println("Valeur Propre " + i + " est " +
				       "[" + realPart[i] + ", " + 
				       + imagPart[i] + "]");
		}
		
		//matrice des vecteurs propres associés
		Matrix evectors = decomp.getV();
		evectors.print(6, 4);
		double[][] vecProp = LinAlg.MatrixToDouble(evectors);
		System.out.println("\n>Vecteur des valeurs propres et matrice des vecteurs propres obtenus");
//		Visage.affichageMatriceDouble(vecProp);
		
		//Verification: la matrice des Vecteurs propres * sa transpose doit donner une matrice unitaire
//		Matrix trans = evectors.transpose();
//		Matrix u = evectors.times(trans);
//		u.print(8, 4);
		
		
		//---déduire valeurs propres de MatVisage*MatVisageTranspose (dim=10000*10000)---
		//il suffit de multiplier chaque vecteur propre par la matrice des visages: (dim (10000,46).(46,1)= dim (10000,1))
		double[][] matVectPropConverted = LinAlg.convertVecProp(matriceDesVisages, vecProp);
		CSVinteract.writeMatToFile(matVectPropConverted, "csv/matVecPropTous.csv");
		
		System.out.println("\n>Des vecteur propres de AT * A, on déduit les vec p de A * AT (en faisant newVec = A * ancienVec, où A: mat des visages)");
//		Visage.affichageMatriceDouble(matVectPropConverted);
		
		
		//---on prend k vecteur propre les plus grands, k choisi, et on les met dans une matrice---
		//pourcentage cumulé d'inertie dans les valeurs propres
		System.out.println("\n------------\n-->Pourcentage d'inertie cumulé selon le nombre de valeur propre (se lit: %age d'inertie | Valeur propre | nb de vp selectionnée)");
		LinAlg.methDuCoude(realPart);
		System.out.println("\nCi-dessus le pourcentage d'inertie cumulé selon le nombre de valeur propre (se lit: %age d'inertie | Valeur propre | nb de vp selectionnée)");
		//demande utilisateur valeur de k
		int valk = 20;
//		Scanner sc;
//		sc = new Scanner(System.in);
//		while (valk<1 || valk>nbImages) {
//			System.out.println("Choisissez le nombre de vecteur propre à garder(k) : ");
//			valk = sc.nextInt();
//			if (valk<1 || valk>nbImages) {
//				System.out.println("k doit être entre 1 et "+nbImages);
//			}
//		}
//		sc.close();
		
		double crit = realPart[realPart.length-valk]; 
		double[][] matVecPropRogne = LinAlg.rognerVecProp(matVectPropConverted, realPart, crit);
		double[][] matAlpha = LinAlg.matriceAlpha(matriceDesVisages, matVecPropRogne);
		double[][] matAlphaDeTest = LinAlg.matriceAlpha(matVisTest, matVecPropRogne);
		System.out.println(">Matrice Alpha obtenu, elle contient les coefficients correspondant aux poids de chaque vecteur propre pour chaque visage");
//		Visage.affichageMatriceDouble(matAlpha);

		CSVinteract.writeMatToFile(matAlpha,"csv/matAlpha.csv");
		CSVinteract.writeMatToFile(matVecPropRogne, "csv/matVecProp_k14.csv");
		CSVinteract.writeMatToFile(matAlphaDeTest, "csv/matAlphaVisTest.csv");
		
		
		
		
		//---projection des visages sur l'espace des vecteurs propres et génération des images---
		System.out.println("\n>On projette les visages sur l'espace des vecteurs propres");
		for (int j = 0; j<nbImages;j++) {
			double[] visageProjete = LinAlg.projeteVisage(matAlpha, matVecPropRogne, j);
//			double[] vectProp = LinAlg.getColumnFromMatrix(matVectPropConverted,j);
//			Visage.affichageVecteurDouble(visageProjete);
//			Visage.affichageVecteurDouble(visageMoyen);
			
//			LinAlg.decentrageEtDenormalisation(vectProp, visageMoyen);
			LinAlg.decentrageEtDenormalisation(visageProjete, visageMoyen);
			int[][] visageReconstruit = Visage.vectorToMatrix(visageProjete,width);
//			int[][] vectPropReconstruit = Visage.vectorToMatrix(vectProp,width);
//			Visage.affichageMatrice(visageReconstruit);
			
			ImageVisage.createImage(visageReconstruit, "visagesReconstruits/visage_reconstruit"+j+"_k="+valk);
//			ImageVisage.createImage(vectPropReconstruit, "vecteursPropres/vect_prop"+j);
			
		}for (int t = 0; t<nbImTest;t++) {
			double[] visageTestProjete = LinAlg.projeteVisage(matAlphaDeTest, matVecPropRogne, t);			
			LinAlg.decentrageEtDenormalisation(visageTestProjete, visageMoyen);
			int[][] visageTestReconstruit = Visage.vectorToMatrix(visageTestProjete,width);
//			Visage.affichageMatrice(visageReconstruit);
			ImageVisage.createImage(visageTestReconstruit, "visagesTest/visage_TestReconstruit"+t+"_k="+valk);
			
		}//----------------------------------------fin boucle generation images---------------------------------------
		
		
		System.out.println("\n>>> Les visages ont été converti en image, ainsi que les vecteurs propres.\n"
				+ "Vous pourrez les trouver dans visagesReconstruits/, visagesTest/ et vecteursPropres/");
		
		
		
		
		
		//---calcul de l'erreur de reconstruction pour différentes valeurs de K.----
		System.out.println("\n\n\n------------------------------------------\n"
				+ "------Debut du calcul de l'evolution de l'erreur de reconstruction (sur 1 visage particulier)------"
				+ "\n------------------------------------------");
		double[][] erreurRecon = new double[nbImages][2];
		for (int i = 0; i<nbImages; i++) {
//			
			double critTmp = realPart[i];
			double[][] matVecPropRogneTmp = LinAlg.rognerVecProp(matVectPropConverted, realPart, critTmp);
			System.out.println("On a gardé un certain nombres de vecteur propre selon le critère crit: "+critTmp);
			//Visage.affichageMatriceDouble(matVecPropRogne);
			//Visage.affichageMatriceDouble(matriceDesVisages);
			
			//---projection de chaque visage sur l'espace des vecteurs propres (produit scalaire visage, vecteur propres)---
			double[][] matAlphaTmp = LinAlg.matriceAlpha(matriceDesVisages, matVecPropRogneTmp);
//			Visage.affichageMatriceDouble(matAlpha);
			
			double[] visageProjete = LinAlg.projeteVisage(matAlphaTmp, matVecPropRogneTmp, 0);
			double[] visageOriginal = LinAlg.getColumnFromMatrix(matriceDesVisages, 0);
			erreurRecon[i][0] = nbImages-i;
			erreurRecon[i][1] = LinAlg.erreurReconstruc(visageOriginal, visageProjete);
			System.out.println("L'erreur de reconstruction a été calculé (= "+erreurRecon[i][1]+")\n");
			
			LinAlg.decentrageEtDenormalisation(visageProjete, visageMoyen);
			int[][] visageReconstruit = Visage.vectorToMatrix(visageProjete,width);
			ImageVisage.createImage(visageReconstruit, "erreurReconstruction/visage_à_erreurk="+(nbImages-i));
		}//----------------------------------------fin boucle erreur de reconstruction----------------------------------------
		
		
		System.out.println("\nFin calculs erreur de reconstruction\n------------\n-->Evolution de l'erreur de reconstruction en fonction de k sur 1 visage particulier"
				+ " (se lit: k | ErrDeReconstruction)\n");
//		Visage.affichageMatriceDouble(erreurRecon);
		System.out.println("\n>>> Les visages ont été converti en image, ainsi que les vecteurs propres.\n"
				+ "Vous pourrez les trouver dans visagesReconstruits/, visagesTest/ et vecteursPropres/.	"
				+ "\nLe visage sur lequel on a calculé l'erreur de reconstruction se trouve dans erreurReconstruction/, on l'a reconstruit pour toutes les valeurs de k");
		
		
		
		for (int i=0;i<16;i++) {
			double[] visageTest = LinAlg.projeteVisage(matAlphaDeTest, matVecPropRogne, i);
			LinAlg.identificationVis(visageTest, matAlpha, matVecPropRogne, idVisa);
		}
		
		
		
		System.out.println("\n\n~Veuillez remonter au début de la console pour suivre les étapes dans l'ordre~");
		System.out.println("\n\n------------FIN------------");	
		
		
		*/
		
		launch(args);
		
	}
}







