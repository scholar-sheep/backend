package sheep.portal.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value="Follow")  //表名
public class Follow {
    private String portal_id;
    private int user_id;
}
