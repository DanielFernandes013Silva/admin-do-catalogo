package com.admin.catologo.application.com.admin.catologo.category.retrieve.get;

import com.admin.catologo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.admin.catologo.domain.category.Category;
import com.admin.catologo.domain.category.CategoryGateway;
import com.admin.catologo.domain.category.CategoryID;
import com.admin.catologo.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        // when
        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory));

        final var actualOutput = useCase.execute(expectedId.getValue());

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId, actualOutput.id());
        assertEquals(expectedName, actualOutput.name());
        assertEquals(expectedDescription, actualOutput.description());
        assertEquals(expectedIsActive, actualOutput.isActive());
        assertEquals(actualOutput.createdAt(), actualOutput.createdAt());
        assertEquals(actualOutput.updatedAt(), actualOutput.updatedAt());
        assertNull(actualOutput.deletedAt());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_thenShouldReturnNotFound() {

        final var anId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final  var expectedErrorCount = 1;

        // when
        when(categoryGateway.findById(eq(anId))).thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(anId.getValue()));

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway, times(1)).findById(eq(anId));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_thenShouldReturnCategory() {

        final var expectedId = CategoryID.from("123");
        final var expectedExceptionMessage = "Gateway error";

        // when
        when(categoryGateway.findById(eq(expectedId))).thenThrow(new IllegalStateException("Gateway error"));

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        assertEquals(expectedExceptionMessage, actualException.getMessage());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
    }
}
