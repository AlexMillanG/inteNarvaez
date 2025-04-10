package mx.edu.utez.inteNarvaez.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ResponseDTO {
    private int numErrors;
    private  String message;
}
