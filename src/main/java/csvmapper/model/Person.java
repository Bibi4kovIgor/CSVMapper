package csvmapper.model;

import annotation.csv.CsvEntity;
import annotation.csv.CsvField;
import annotation.validators.PhoneValidator;
import annotation.validators.Validator;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import parser.CsvTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validator
@CsvEntity
public class Person {

    @NotNull
    @CsvField(columnId = 0, name = "id", type = CsvTypes.LONG, isNullable = false)
    private Long id;

    @CsvField(columnId = 1, name = "name", isNullable = false)
    private String name;

    @CsvField(columnId = 2, name = "phone", isNullable = false)
    @PhoneValidator
    private String phone;

    @Nullable
    @CsvField(columnId = 3, name = "address")
    private String address;

    @Nullable
    @CsvField(columnId = 4, name = "test", type = CsvTypes.DOUBLE)
    private Double test;

}
