package myself.programing.coding.annountation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import myself.programing.coding.annountation.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private int minLength;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;

        return password.length() >= minLength &&
                password.matches(".*[A-Z].*") &&       // ít nhất 1 chữ hoa
                password.matches(".*[a-z].*") &&       // ít nhất 1 chữ thường
                password.matches(".*\\d.*") &&         // ít nhất 1 số
                password.matches(".*[!@#$%^&*()].*");  // ít nhất 1 ký tự đặc biệt
    }
}
