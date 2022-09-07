import csvmapper.model.Person;
import io.ReadFileSingleton;
import lombok.SneakyThrows;
import parser.CsvParser;

import java.lang.reflect.Field;
import java.util.List;

public class Launcher {
    @SneakyThrows
    public static void main(String[] args) {
//        Person person = Person.builder().build();
        CsvParser<Person> personParser = new CsvParser<>(Person.class);
        List<String> csvFileSourceList = ReadFileSingleton.getInstance().readFileFromResources("data.csv");

        List<Person> result = csvFileSourceList.stream()
                                .map(personParser::parseModel)
                                .toList();
        result.forEach(System.out::println);

    }
}
