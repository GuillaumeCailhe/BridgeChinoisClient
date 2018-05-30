package bridgechinoisclient;

import LibrairieCarte.SymboleCarte;
import LibrairieMoteur.ModeDeJeu;
import bridgechinoisclient.controller.Controller;
import bridgechinoisclient.controller.PlateauController;
import bridgechinoisclient.model.reseau.Client;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ApplicationGraphique extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private Client client;
    private PlateauController plateauController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BRIDGE CHINOIS");
        this.primaryStage.getIcons().add(new Image(ApplicationGraphique.class.getResourceAsStream("view/ressources/cartes/symbole_pique.png")));

        afficherMenu();
    }

    /**
     * Affiche la fenêtre choisie.
     *
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
        afficherFenetre("view/Mode_IA.fxml");
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
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationGraphique.class.getResource("view/Regles.fxml"));
        AnchorPane menu;
        try {
            menu = (AnchorPane) loader.load();
            // On charge la scène
            Scene scene = new Scene(menu);
            dialog.setScene(scene);

            Controller controller = loader.getController();
            controller.setMainApp(this);
            
            dialog.setScene(scene);
            dialog.show();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationGraphique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Affiche le score
     */
    public void afficherOptions() {

    }

    /**
     * Affiche le plateau
     */
    public void afficherPlateau() {
        this.plateauController = (PlateauController) afficherFenetre("view/Plateau.fxml");
        this.plateauController.initialiser(this.client, this.client.getMain(), this.client.getPiles(), this.client.getPseudo(), this.client.getPseudoAdversaire());
    }

    /**
     * Quitte l'application
     */
    public void quitterApplication() {
        Platform.exit();
    }

    public void connexionServeur(ModeDeJeu modeDeJeu) {
        try {
            Random rand = new Random();
            int randomNum = rand.nextInt((10000) + 1);
            this.client = new Client("Joueur" + randomNum, modeDeJeu, 3, this);
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

    public Client getClient() {
        return client;
    }

    public PlateauController getPlateauController() {
        return plateauController;
    }
}
