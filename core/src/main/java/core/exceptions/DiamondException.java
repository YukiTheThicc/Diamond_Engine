package core.exceptions;

public class DiamondException extends RuntimeException {

    public Class<?> origin;

    public DiamondException(Class<?> origin, String message) {
        super(message);
        this.origin = origin;
    }
}
