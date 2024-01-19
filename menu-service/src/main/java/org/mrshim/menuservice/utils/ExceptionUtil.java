package org.mrshim.menuservice.utils;

import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ExceptionUtil {
    public static String extractErrorLocation(Exception ex) {
        return Arrays.stream(ex.getStackTrace())
                .findFirst()
                .map(element -> "at " + element.getClassName() + "."
                        + element.getMethodName() + "("
                        + element.getFileName() + ":"
                        + element.getLineNumber() + ")")
                .orElse("Location unavailable");
    }

    public static List<String> extractErrorMessages(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
    }
}
