package com.admin.catologo.domain.category;

import com.admin.catologo.domain.validation.Error;
import com.admin.catologo.domain.validation.ValidationHandler;
import com.admin.catologo.domain.validation.Validator;

import java.util.Objects;

public class CategoryValidator extends Validator {

    private static final int MINIMUM_SIZE_CHARACTERS = 3;
    private static final int MAXIMUM_SIZE_CHARACTERS = 255;
    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = Objects.requireNonNull(aCategory);
    }

    @Override
    public void validate() {

        checkNameConstraints();

    }

    private void checkNameConstraints() {

        final var name = this.category.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final var length = name.trim().length();
        if (length > MAXIMUM_SIZE_CHARACTERS || length < MINIMUM_SIZE_CHARACTERS) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
