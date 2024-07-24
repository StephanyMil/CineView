package cs2024.inf.CineView.handler;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ResponseError {
    private Date timestamp = new Date();
    private String status = "error";
    private int statusCode = 400;
    private String error;
}
