package com.admin.catologo.application.category.update;

import com.admin.catologo.domain.category.Category;
import com.admin.catologo.domain.category.CategoryGateway;
import com.admin.catologo.domain.category.CategoryID;
import com.admin.catologo.domain.exceptions.DomainException;
import com.admin.catologo.domain.validation.Error;
import com.admin.catologo.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand aCommand) {
        final var anId = CategoryID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var aCategory = this.categoryGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();

        aCategory
                .update(aName, aDescription, isActive)
                .validate(notification);

        return notification.hasErrors() ? Left(notification) : create(aCategory);
    }

    private static Supplier<RuntimeException> notFound(CategoryID anId) {
        return () -> DomainException.with(
                new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }

    private Either<Notification, UpdateCategoryOutput> create(final Category aCategory) {
        return Try(()-> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
