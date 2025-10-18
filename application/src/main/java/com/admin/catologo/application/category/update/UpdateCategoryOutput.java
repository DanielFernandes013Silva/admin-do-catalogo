package com.admin.catologo.application.category.update;

import com.admin.catologo.domain.category.Category;
import com.admin.catologo.domain.category.CategoryID;

public record UpdateCategoryOutput(
       CategoryID id
) {
    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
