package parser;


import annotation.csv.CsvEntity;
import annotation.csv.CsvField;
import annotation.validators.PhoneValidator;
import annotation.validators.Validator;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvParser<T> {
    public static final String EMPTY_FIELD_EXCEPTION_MESSAGE = " - Empty field";
    public static final String ERROR_ON_CREATE_OBJECT_MESSAGE = "Error on create object";
    public static final String CLASS_DOES_NOT_SUPPORTS_CSV = "Class does not supports CSV";
    public static final String PHONE_VALIDATION_PROBLEM = "Phone validation problem";
    public static final String DEFAULT_CONSTRUCTOR_EXISTS_EXCEPTION_MESSAGE = "Default constructor declaring is needed!";
    Logger logger = Logger.getLogger(CsvParser.class.getName());

    private final Class<T> clazz;

    public CsvParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parseModel(String csvString) {
        if (!isDefaultConstructorExists()) {
            throw new RuntimeException(DEFAULT_CONSTRUCTOR_EXISTS_EXCEPTION_MESSAGE);
        }
        if (!clazz.isAnnotationPresent(CsvEntity.class)) {
            throw new RuntimeException(CLASS_DOES_NOT_SUPPORTS_CSV);
        }
        T object = getObject();
        Field[] fields = object.getClass().getDeclaredFields();
        String[] values = csvString.split(",");
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            CsvTypes csvType = fields[i].getAnnotation(CsvField.class).type();
            try {
                fields[i].set(object, csvType.getValue(values[i]));
                checkField(object, fields[i]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                logger.log(Level.INFO, e.getLocalizedMessage() + EMPTY_FIELD_EXCEPTION_MESSAGE);
            }
        }
        return object;
    }

    private boolean isDefaultConstructorExists() {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    private void checkField(Object object, Field field) throws IllegalAccessException {
        if (!clazz.isAnnotationPresent(Validator.class)) {
            return;
        }

        validatePhone(object, field);

    }

    private void validatePhone(Object object, Field field) throws IllegalAccessException {
        if (!field.isAnnotationPresent(PhoneValidator.class)) {
            return;
        }

        String phoneNumber = String.valueOf(field.get(object));
        if (phoneNumber.isBlank() || phoneNumber.isEmpty()) {
            return;
        }

        if (!phoneNumber.trim()
                .matches(field.getAnnotation(PhoneValidator.class).validatePhone())) {
            throw new RuntimeException(PHONE_VALIDATION_PROBLEM);
        }
    }

    @NotNull
    private T getObject() {
        T object = null;
        try {
            object = clazz.getConstructor().newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException exception) {
            logger.log(Level.WARNING, ERROR_ON_CREATE_OBJECT_MESSAGE);
        }

        return Objects.requireNonNull(object);
    }
}
