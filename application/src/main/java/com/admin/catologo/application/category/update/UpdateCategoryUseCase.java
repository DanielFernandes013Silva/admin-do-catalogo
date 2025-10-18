package com.admin.catologo.application.category.update;

import com.admin.catologo.application.UseCase;
import com.admin.catologo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}