package com.admin.catologo.application.category.retrieve.list;

import com.admin.catologo.domain.category.Category;
import com.admin.catologo.domain.category.CategoryID;

import java.time.Instant;
import java.util.List;

public record CategoryListOutput(
        CategoryID id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static CategoryListOutput from(final Category aCategory) {
        return new CategoryListOutput(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt()
        );
    }

    public static List<CategoryListOutput> from(final List<Category> anList) {
        return anList
                .stream()
                .map(CategoryListOutput::from)
                .toList();
    }

}
