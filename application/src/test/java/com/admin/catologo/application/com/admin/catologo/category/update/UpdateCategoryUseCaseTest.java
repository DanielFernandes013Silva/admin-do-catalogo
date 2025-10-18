package com.admin.catologo.application.com.admin.catologo.category.update;

import com.admin.catologo.application.category.update.DefaultUpdateCategoryUseCase;
import com.admin.catologo.application.category.update.UpdateCategoryCommand;
import com.admin.catologo.domain.category.Category;
import com.admin.catologo.domain.category.CategoryGateway;
import com.admin.catologo.domain.category.CategoryID;
import com.admin.catologo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    //1. Teste do caminho feliz e retorne o CategoryId
    //2. Teste de validação do nome invalido
    //3. Teste de criar categoria inativa
    //4. Teste de simulando erro generico do gateway
    //5. Teste passando um ID invalido (nao existente)

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryId() {
        final var aCategory =
                Category.newCategory("Film", "A categoria", true);
        final var expectedId = aCategory.getId();

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(Category.clone(aCategory)));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                        && Objects.equals(aCategory.getId(), aUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                        && Objects.isNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithNullName_whenCallsUpdateCategory_thenShouldReturnDomainException() {

        final var aCategory =
                Category.newCategory("Film", "A categoria", true);
        final var expectedId = aCategory.getId();

        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                null,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getFirstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(0)).update(any());
    }

    @Test
    public void givenAValidActivateCommand_whenCallsUpdateCategory_thenShouldReturnCategoryInactivated() {

        final var aCategory =
                Category.newCategory("Filme", "A categoria mais assistida", true);

        final var expectedId = aCategory.getId();

        final String expectedName = "Filme";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(Category.clone(aCategory)));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1)).update(argThat(
                aCategoryUpdated ->
                        Objects.equals(expectedName, aCategoryUpdated.getName())
                        && Objects.equals(expectedDescription, aCategoryUpdated.getDescription())
                        && Objects.equals(expectedIsActive, aCategoryUpdated.isActive())
                        && Objects.equals(aCategory.getId(), aCategoryUpdated.getId())
                        && Objects.equals(aCategory.getCreatedAt(), aCategoryUpdated.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(aCategoryUpdated.getUpdatedAt())
                        && Objects.nonNull(aCategoryUpdated.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldReturnAException() {

        final var aCategory =
                Category.newCategory("Filmes", "A categoria", true);
        final var expectedId = aCategory.getId();

        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getFirstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1)).update(argThat(
                aCategoryUpdated ->
                        Objects.equals(expectedName, aCategoryUpdated.getName())
                                && Objects.equals(expectedDescription, aCategoryUpdated.getDescription())
                                && Objects.equals(expectedIsActive, aCategoryUpdated.isActive())
                                && Objects.equals(aCategory.getId(), aCategoryUpdated.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aCategoryUpdated.getCreatedAt())
                                && Objects.equals(aCategory.getUpdatedAt(), aCategoryUpdated.getUpdatedAt())
                                && Objects.isNull(aCategoryUpdated.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithInvalidID_whenCallsUpdateCategory_thenShouldReturnNotFoundException() {

        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand).getLeft());

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));

        verify(categoryGateway, times(0)).update(any());
    }

}
