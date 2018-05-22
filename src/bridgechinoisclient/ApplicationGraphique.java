package bridgechinoisclient;

import bridgechinoisclient.controller.AccueilController;
import bridgechinoisclient.controller.Controller;
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

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BRIDGE  CHINOIS");
        this.primaryStage.getIcons().add(new Image(ApplicationGraphique.class.getResourceAsStream("view/ressources/cartes/symbole_pique.png")));

        afficherMenu();
    }

    private void afficherFenetre(String viewPath) {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
    public void afficherTutoriel(){
        afficherFenetre("view/Plateau.fxml");
    }
    
    /**
     * Affiche les règles
     */
    public void afficherRegles(){
        
    }
    
    /**
     * Affiche le score
     */
    public void afficherScore(){
        
    }
    
    /**
     * Affiche le plateau
     */
    public void afficherPlateau(){
        afficherFenetre("view/Plateau.fxml");
    }
    /**
     * Quitte l'application
     */
    public void quitterApplication() {
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
