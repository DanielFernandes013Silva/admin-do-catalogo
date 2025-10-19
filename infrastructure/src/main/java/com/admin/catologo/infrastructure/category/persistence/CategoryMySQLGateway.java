package com.admin.catologo.infrastructure.category.persistence;

import com.admin.catologo.domain.category.Category;
import com.admin.catologo.domain.category.CategoryGateway;
import com.admin.catologo.domain.category.CategoryID;
import com.admin.catologo.domain.category.CategorySearchQuery;
import com.admin.catologo.domain.pagination.Pagination;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
    }

    @Override
    public Category create(Category aCategory) {
        return this.categoryRepository
                .save(CategoryJpaEntity.from(aCategory))
                .toAggregate();
    }

    @Override
    public void deleteById(CategoryID anId) {

    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return Optional.empty();
    }

    @Override
    public Category update(Category aCategory) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }
}
