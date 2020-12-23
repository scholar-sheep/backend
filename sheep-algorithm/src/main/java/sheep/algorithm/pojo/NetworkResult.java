package sheep.algorithm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetworkResult<T>{
    private ArrayList<T> nodes;
    private ArrayList<T> lines;
}
