package cn.kgc.exception;

public class UploadException extends Exception {
    public UploadException(){ }

    public UploadException(String message){
        super(message);
    }
}
