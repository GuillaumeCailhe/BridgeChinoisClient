package bridgechinoisclient;

import LibrairieMoteur.ModeDeJeu;
import bridgechinoisclient.controller.Controller;
import bridgechinoisclient.controller.PlateauController;
import bridgechinoisclient.model.reseau.Client;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ApplicationGraphique extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private Client client;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BRIDGE CHINOIS");
        this.primaryStage.getIcons().add(new Image(ApplicationGraphique.class.getResourceAsStream("view/ressources/cartes/symbole_pique.png")));

        afficherMenu();
    }
    
    /**
     * Affiche la fenêtre choisie.
     * @param viewPath le chemin vers le fxml à afficher.
     * @return le controller associé
     */
    private Controller afficherFenetre(String viewPath) {
        try {
            // lecture du FXML
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ApplicationGraphique.class.getResource(viewPath));
            AnchorPane menu = (AnchorPane) loader.load();

            // On charge la scène
            Scene scene = new Scene(menu);
            primaryStage.setScene(scene);

            Controller controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
            return controller;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Affiche le menu principal
     */
    public void afficherMenu() {
        afficherFenetre("view/Accueil.fxml");
    }

    /**
     * Affiche le choix du mode multijoueur
     */
    public void afficherMultijoueur() {
        afficherFenetre("view/Multijoueur.fxml");
        connexionServeur(ModeDeJeu.JOUEUR_CONTRE_JOUEUR);
    }

    /**
     * Affiche le choix du mode IA
     */
    public void afficherModeIA() {
        afficherFenetre("view/ModeIA.fxml");
    }

    /**
     * Affiche le tutoriel
     */
    public void afficherTutoriel() {
        afficherPlateau();
    }

    /**
     * Affiche les règles
     */
    public void afficherRegles() {

    }

    /**
     * Affiche le score
     */
    public void afficherScore() {

    }

    /**
     * Affiche le plateau
     */
    public void afficherPlateau() {
        PlateauController plateauController = (PlateauController) afficherFenetre("view/Plateau.fxml");
        
        plateauController.distributionInitiale(this.client.getMain(), this.client.getPiles(), this.client.getPseudo(), "Adversaire");
    }

    /**
     * Quitte l'application
     */
    public void quitterApplication() {
        Platform.exit();
    }

    public void connexionServeur(ModeDeJeu modeDeJeu) {
        try {
            this.client = new Client("Guillaume", modeDeJeu, 3, this);
            Thread thread = new Thread(this.client);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException ex) {
            
        } catch (InterruptedException ex) {
            
        }      
    }

    public static void main(String[] args) {
        launch(args);
    }
}
