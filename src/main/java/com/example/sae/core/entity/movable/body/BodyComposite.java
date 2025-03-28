package com.example.sae.core.entity.movable.body;

import com.example.sae.client.utils.config.Constants;

import java.util.ArrayList;
import java.util.List;

public class BodyComposite implements BodyComponent {
    private MoveableBody mainBody;
    private final List<BodyComponent> clones = new ArrayList<>();

    public BodyComposite(MoveableBody mainBody) {
        this.mainBody = mainBody;
    }

    @Override
    public void moveToward(double[] velocity) {
        for (BodyComponent clone : clones) {
            clone.moveToward(velocity); // Direct movement to mouse position
        }
    }

    @Override
    public void addClone(BodyComponent clone) {
        clones.add(clone);
    }

    @Override
    public void removeClone(BodyComponent clone) {
        clones.remove(clone);
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public boolean belongsToSameComposite(BodyComponent other) {
        // If the other component is this composite
        if (other == this) {
            return true;
        }
        // If other is the main body
        if (other == mainBody) {
            return true;
        }
        // If other is a clone in this composite
        if (clones.contains(other)) {
            return true;
        }
        // If other is another composite
        if (other instanceof BodyComposite otherComposite) {
            // Check if they share the same main body
            return otherComposite.mainBody == this.mainBody;
        }
        // If other is a MoveableBody, check if it's associated with this composite
        if (other instanceof MoveableBody moveable) {
            return moveable.composite == this;
        }

        return false;
    }


    public MoveableBody getMainBody() {
        return mainBody;
    }
    public List<BodyComponent> getClones() {
        return new ArrayList<>(clones);
    }

    private long lastSplitTime = 0;
    private static final double BASE_COOLDOWN = Constants.getCloneMergeCooldown();

    public void updateLastSplitTime() {
        this.lastSplitTime = System.currentTimeMillis();
    }

    public boolean canClonesEatEachOther() {
        long currentTime = System.currentTimeMillis();
        double totalMasse = getTotalMasse();
        double cooldown = BASE_COOLDOWN + (totalMasse / 100);
        return currentTime - lastSplitTime > cooldown;
    }

    private double getTotalMasse() {
        double total = mainBody.getMasse();
        for (BodyComponent clone : clones) {
            if (clone instanceof MoveableBody) {
                total += ((MoveableBody) clone).getMasse();
            }
        }
        return total;
    }

    public void setMainBody(MoveableBody newMainBody) {
        // Conserver les propriétés importantes de l'ancien mainBody
        MoveableBody oldMainBody = this.mainBody;
        String oldName = oldMainBody.getNom();

        // Mettre à jour la référence du mainBody
        this.mainBody = newMainBody;

        // Transférer les propriétés
        newMainBody.setNom(oldName);

        // Mettre à jour le composite pour tous les clones
        for (BodyComponent clone : clones) {
            if (clone instanceof MoveableBody moveable) {
                moveable.setComposite(this);
            }
        }
    }

}
