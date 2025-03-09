package game.restAPI;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    CONFLICT(409, "Conflict"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getStatusLine() {
        return "HTTP/1.1 " + code + " " + message;
    }

    public int getCode() {
        return code;
    }
}
