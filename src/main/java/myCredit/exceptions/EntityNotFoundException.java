package myCredit.exceptions;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(Class entityClass, Object id) {
        super(String.format("Entity %s with id=%s is not found!", entityClass.getSimpleName(), id));
    }
}
