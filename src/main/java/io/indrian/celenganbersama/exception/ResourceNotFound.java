package io.indrian.celenganbersama.exception;


public class ResourceNotFound extends RuntimeException {

    public ResourceNotFound(Long id) {
        super("Could not find id: " + id);
    }
}
