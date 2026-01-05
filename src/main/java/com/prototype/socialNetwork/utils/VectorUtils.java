package com.prototype.socialNetwork.utils;

import org.springframework.stereotype.Component;

@Component
public class VectorUtils {

    /**
     * Actualiza el perfil del usuario usando "Cumulative Moving Average".
     * Fórmula: NuevoPromedio = ((PromedioViejo * N) + NuevoDato) / (N + 1)
     */
    public float[] calculateNewAverage(float[] currentProfileVector, float[] newPostVector, long totalLikesSoFar) {
        // Caso 1: Usuario nuevo (no tiene vector aún)
        if (currentProfileVector == null || currentProfileVector.length == 0) {
            return newPostVector; // El promedio es el único post que le gustó
        }

        if (currentProfileVector.length != newPostVector.length) {
            throw new IllegalArgumentException("Dimensiones incorrectas: Perfil=" + currentProfileVector.length + " vs Post=" + newPostVector.length);
        }

        int dimensions = currentProfileVector.length;
        float[] result = new float[dimensions];
        long newTotal = totalLikesSoFar + 1;

        for (int i = 0; i < dimensions; i++) {
            // Deshacemos el promedio anterior multiplicando por N, sumamos el nuevo, y dividimos por N+1
            double sum = (currentProfileVector[i] * totalLikesSoFar) + newPostVector[i];
            result[i] = (float) (sum / newTotal);
        }

        return result;
    }
}