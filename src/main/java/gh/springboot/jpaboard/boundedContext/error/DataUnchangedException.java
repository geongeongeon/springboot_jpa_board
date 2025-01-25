package gh.springboot.jpaboard.boundedContext.error;

public class DataUnchangedException extends RuntimeException {

    public DataUnchangedException(String msg) {
        super(msg);
    }

}