package core.exceptions;

public class DiamondCriticalException extends DiamondException {
    public DiamondCriticalException(Class<?> origin, String message) {
        super(origin, message);
    }
}
