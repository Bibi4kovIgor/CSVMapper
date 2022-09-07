package parser;


import annotation.csv.CsvField;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvParser<T> {
    public static final String EMPTY_FIELD_EXCEPTION_MESSAGE = " - Empty field";
    public static final String ERROR_ON_CREATE_OBJECT_MESSAGE = "Error on create object";
    Logger logger = Logger.getLogger(CsvParser.class.getName());

    private final Class<T> clazz;

    public CsvParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parseModel( String csvString) {
        T object = getObject();
        Field[] fields = object.getClass().getDeclaredFields();
        String[] values = csvString.split(",");
        for (int i = 0; i < fields.length; i++) {
            CsvTypes csvType = fields[i].getAnnotation(CsvField.class).type();
            try {
                fields[i].setAccessible(true);
                fields[i].set(object, csvType.getValue(values[i]));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                logger.log(Level.INFO, e.getLocalizedMessage() + EMPTY_FIELD_EXCEPTION_MESSAGE);
            }
        }
        return object;
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