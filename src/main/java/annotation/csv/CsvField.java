package annotation.csv;

import parser.CsvTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvField {
    String name();
    CsvTypes type() default CsvTypes.STRING;
    int columnId();
    boolean isNullable() default true;
}
