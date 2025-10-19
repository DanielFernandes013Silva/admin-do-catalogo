package com.admin.catologo.infrastructure.category.persistence;

import com.admin.catologo.domain.category.Category;
import com.admin.catologo.infrastructure.category.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value: com.admin.catologo.infrastructure.category.persistence.CategoryJpaEntity.name";
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCase =
                Assertions.assertInstanceOf(PropertyValueException.class,  actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCase.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCase.getMessage());
    }
}
