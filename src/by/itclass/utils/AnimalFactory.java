package by.itclass.utils;

import by.itclass.exceptions.CompetitionException;
import by.itclass.model.Animal;
import by.itclass.model.Cat;
import by.itclass.model.Dog;
import by.itclass.model.Genus;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class AnimalFactory {
    private static final String DELIMITER = "[;,]";
    private static final String CHIP_REGEX = "(?=\\d{15}\\b)\\[112|643]09(?:81|56)\\d{8}";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String EMAIL_REGEX = "^\\S+@\\S+\\.\\S+$";
    public static Animal getInstance(String textString) throws CompetitionException {
        var stringArray = textString.split(DELIMITER);
        try {
            var chipNumber = Long.parseLong(validateStringByPattern(stringArray[0], CHIP_REGEX));
            var name = validateStringForEmpty(stringArray[2]);
            var birthDate = LocalDate.parse(stringArray[3], FORMATTER);
            var breed = validateStringForEmpty(stringArray[4]);
            var email = validateStringByPattern(stringArray[5], EMAIL_REGEX);
            return StringUtils.equals("cat", stringArray[1]) ? new Cat(chipNumber, Genus.of(stringArray[1]), name, birthDate, breed, email)
                                                             : new Dog(chipNumber, Genus.of(stringArray[1]), name, birthDate, breed, email);

        } catch (IllegalStateException e) {
            throw new CompetitionException(e, textString);
        }
    }
    private static String validateStringByPattern(String value, String regex) {
        if(value.matches(regex)) {
            return value;
        }
        throw new IllegalStateException("chip number or email has wrong format");
    }
    private static String validateStringForEmpty(String value) {
        if(StringUtils.isNotBlank(value)) {
            return value;
        }
        throw new IllegalStateException("Name or Breed is empty");
    }
}
