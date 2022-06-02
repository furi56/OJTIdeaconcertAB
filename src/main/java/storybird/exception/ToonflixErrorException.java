package storybird.exception;

import storybird.enums.ErrorCode;

public class ToonflixErrorException extends RuntimeException{

    private ErrorCode code;
    private String message;

    public ToonflixErrorException(String msg, ErrorCode code){
        super(msg);
        this.code = code;
        this.message = msg;
    }

    public ErrorCode getErrorCode(){
        return code;
    }
}
