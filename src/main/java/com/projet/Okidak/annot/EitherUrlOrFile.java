package com.projet.Okidak.annot;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.projet.Okidak.config.UrlOrFileValidator;

@Constraint(validatedBy = UrlOrFileValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EitherUrlOrFile {
    String message() default "You must provide either a URL or a file, but not both.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
