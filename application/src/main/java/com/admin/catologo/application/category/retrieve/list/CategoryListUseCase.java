package com.admin.catologo.application.category.retrieve.list;

import com.admin.catologo.application.UseCase;
import com.admin.catologo.domain.category.CategorySearchQuery;
import com.admin.catologo.domain.pagination.Pagination;

public abstract class CategoryListUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
