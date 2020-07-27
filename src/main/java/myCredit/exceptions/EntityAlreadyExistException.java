package myCredit.exceptions;

public class EntityAlreadyExistException extends Exception {
    public EntityAlreadyExistException(Class entityClass, Object id) {
        super(String.format("Entity %s with id=%s is already exist!", entityClass.getSimpleName(), id));
    }
}
