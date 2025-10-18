package com.admin.catologo.application.category.create;

import com.admin.catologo.domain.category.Category;
import com.admin.catologo.domain.category.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {

    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId());
    }
}
