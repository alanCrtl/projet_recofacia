# Projet Reconnaissance Faciale Par ACP

## **Pour exécuter le programme**

**sur linux:**

**->version terminale:**

`java -jar Executable.jar`

**->version interface:**

`java -jar --module-path /home/cytech/ing1/Java/javafx-sdk-18/lib --add-modules javafx.controls,javafx.fxml InterfaceRecoFac.jar`

**!!! ATTENTION, il faut changer "/home/cytech/ing1/Java/" par le chemin qui correspond à l'emplacement de votre librairie javafx**

## **Informations**
**Comment le programme fonctionne**

Le programme version console requiert 1 entrée utilisateur, la valeur de k.
Une fois le programme lancé, les étapes de calculs sont 
décrite en commentaire.

N'ayant pas trouvé comment générer les graphiques de l'erreur 
de reconstruction et du pourcentage cumulé d'inertie en fonction 
de k, nous avons mis dans le dossier les 2 graphique obtenues 
à partir des données du programme. Cependant, les tableaux 
sont affichés lors de l'exécution du programme.

**Localisation des ressources**

le visage moyen est généré dans le dossier source du projet
les vecteur propres (ou eigenfaces) se trouvent dans vecteurPropres/.
les visages reconstruits dans visagesReconstruits/.
les visages test reconstruit à partir des eigenfaces se trouvent
dans visagesTest/.
le visage utilisé pour calculer l'erreur de reconstruction
a été généré dans erreurReconstruction/.

**Rajout d'images**

Vous pouvez modifier la base données (rajouter ou retirer des images) tant que celles-ci ont toutes la même dimension.
Si votre base de données à des dimensions différentes, il faut
changer 'height' et 'width' au tout début du main du programme.

## **Ouverture du projet**

**Pour ouvrir le projet dans eclipse:**

file -> open project from file system -> sélectionner ce dossier (projet_recofacia)

**ou bien:**

créer un nouveau projet -> clic droit sur le projet -> import -> File system -> sélectionner le dossier

## **Auteurs**
COURTEL Alan

GIRAUD Hugo

LARTIGUE Thomas

MAZEAU Leo 

SENDERENS Guillaume

