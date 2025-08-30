package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {

    private int statusCode;
    private String error;
    private Object message; //message co the la string hoac arrayList
    private T data;
}
