package mx.edu.utez.inteNarvaez.models.dtos;

public class ResponseDTO {

    private int numErrors;
    private  String message;

    public int getNumErrors() {
        return numErrors;
    }

    public void setNumErrors(int numErrors) {
        this.numErrors = numErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
