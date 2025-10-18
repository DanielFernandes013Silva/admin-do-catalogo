package com.admin.catologo.application.com.admin.catologo.category.retrieve.list;

import com.admin.catologo.application.category.retrieve.list.CategoryListOutput;
import com.admin.catologo.application.category.retrieve.list.DefaultCategoryListUseCase;
import com.admin.catologo.domain.category.Category;
import com.admin.catologo.domain.category.CategoryGateway;
import com.admin.catologo.domain.category.CategorySearchQuery;
import com.admin.catologo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultCategoryListUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_shouldReturnCategories() {

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedCategories = List.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Séries", null, true));

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedCategories.size(),
                expectedCategories
        );

        final var expectedItemsCount = expectedCategories.size();
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(actualResult.items().size(), expectedItemsCount);
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedCategories.size(), actualResult.total());

        verify(categoryGateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListCategoriesAndResultIsEmpty_shouldReturnEmptyCategories() {

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedCategories = List.<Category>of();
        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                0,
                expectedCategories
        );

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedItemsCount, actualResult.items().size());

        verify(categoryGateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_shouldReturnException() {

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException("Gateway error"));

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway).findAll(eq(aQuery));
    }
}
