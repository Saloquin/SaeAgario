package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;


public abstract class MoveableBody extends Entity{
    public double Speed = 1.5;
    public double Smoothing = 80; // higher numbers mean more smoothing, but also slower circle
    public String name="Camou";
    private Text nameText;

    MoveableBody(Group group, double initialSize) {
        super(group, initialSize);
        initializeNameText(group);
    }

    MoveableBody(Group group, double initialSize, Color color) {
        super(group, initialSize, color);
        initializeNameText(group);
    }

    private void initializeNameText(Group group) {
        nameText = new Text(name);
        nameText.setFill(Color.BLACK);
        nameText.setStyle("-fx-font-size: 14;");
        // Place le texte au-dessus du sprite dans l'ordre de rendu
        nameText.setViewOrder(-1000);

        // Position initiale au centre du cercle
        nameText.setX(Sprite.getCenterX() - nameText.getLayoutBounds().getWidth() / 2);
        nameText.setY(Sprite.getCenterY());

        group.getChildren().add(nameText);
    }

    public void increaseSize(double foodValue) {
        setMasse(getMasse() + foodValue);
        Sprite.setRadius(10 * Math.sqrt(getMasse()));
        setViewOrder(-Sprite.getRadius());
        nameText.setX(Sprite.getCenterX() - nameText.getLayoutBounds().getWidth() / 2);
        nameText.setY(Sprite.getCenterY());
    }

    public void moveToward(double[] mousePosition) {
        double m = getMasse();
        double maxSpeed = 100 / Math.sqrt(m); // Ajuster 100 selon le besoin

        // Vecteur direction vers la souris
        double[] velocity = new double[]{
                mousePosition[0] - Sprite.getCenterX(),
                mousePosition[1] - Sprite.getCenterY()
        };

        // Distance du curseur au centre du joueur
        double distance = Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1]);

        // Normalisation du vecteur de direction
        if (distance > 0) {
            velocity[0] /= distance;
            velocity[1] /= distance;
        }

        // Facteur de vitesse (proportionnel à la distance du curseur au joueur, max à `maxSpeed`)
        double speedFactor = Math.min(distance / MAP_LIMIT_WIDTH, 1.0);
        double speed = maxSpeed * speedFactor;

        // Appliquer la vitesse calculée
        velocity[0] *= speed;
        velocity[1] *= speed;

        // Vérification des limites de la carte
        double newX = Sprite.getCenterX() + velocity[0];
        double newY = Sprite.getCenterY() + velocity[1];

        if (newX < MAP_LIMIT_WIDTH && newX > -MAP_LIMIT_WIDTH) {
            Sprite.setCenterX(newX);
            nameText.setX(newX - nameText.getLayoutBounds().getWidth() / 2);
        }
        if (newY < MAP_LIMIT_HEIGHT && newY > -MAP_LIMIT_HEIGHT) {
            Sprite.setCenterY(newY);
            nameText.setY(newY);
        }
    }

    //TODO: Implement the splitSprite method without using AgarioApplication.root
    public void splitSprite(){
        Player newBody = new Player(AgarioApplication.root, Sprite.getRadius() / 2, Color.RED);
        newBody.Sprite.setCenterX(Sprite.getCenterX() + 30);
        newBody.Sprite.setCenterY(Sprite.getCenterY() + 30);


        Sprite.setRadius(Sprite.getRadius() / 2);

    }
    public double distanceTo(double[] position){
        return Math.sqrt(Math.pow(position[0] - getPosition()[0], 2) + Math.pow(position[1] - getPosition()[1], 2) );
    }

    public double[] normalizeDouble(double[] array){
        //don't worry about it :)

        double magnitude = Math.sqrt( (array[0] * array[0]) + (array[1] * array[1]) );

        if (array[0] != 0 || array[1] != 0 ){
            return new double[]{array[0] / magnitude, array[1] / magnitude};
        }
        return new double[]{0,0};
    }

    @Override
    public void onDeletion() {
        super.onDeletion();
        if (nameText.getParent() != null) {
            ((Group) nameText.getParent()).getChildren().remove(nameText);
        }
    }


}
