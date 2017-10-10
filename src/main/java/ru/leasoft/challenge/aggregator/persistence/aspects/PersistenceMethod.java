package ru.leasoft.challenge.aggregator.persistence.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
    Use this annotation on methods dealing with persistence to prevent exception propagation
    from persistence layer (like ValidationException, SQLException and so on) wrapping them into PersistenceLevelException
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistenceMethod {
}
