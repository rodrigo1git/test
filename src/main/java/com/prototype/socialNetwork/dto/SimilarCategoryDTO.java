package com.prototype.socialNetwork.dto;

public interface SimilarCategoryDTO {
    // Spring buscará la columna "categoryId"
    Integer getCategoryId();

    // Spring buscará la columna "similarity"
    Double getSimilarity();
}