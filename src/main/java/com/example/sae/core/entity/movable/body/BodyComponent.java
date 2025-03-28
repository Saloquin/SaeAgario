package com.example.sae.core.entity.movable.body;

public interface BodyComponent {
    void moveToward(double[] velocity);
    void addClone(BodyComponent clone);
    void removeClone(BodyComponent clone);
    boolean isComposite();
    boolean belongsToSameComposite(BodyComponent other); // New method
}
