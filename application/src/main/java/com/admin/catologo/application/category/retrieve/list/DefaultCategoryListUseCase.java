package com.admin.catologo.application.category.retrieve.list;

import com.admin.catologo.domain.category.CategoryGateway;
import com.admin.catologo.domain.category.CategorySearchQuery;
import com.admin.catologo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultCategoryListUseCase extends CategoryListUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultCategoryListUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery aQuery) {
        return this.categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);
    }
}
