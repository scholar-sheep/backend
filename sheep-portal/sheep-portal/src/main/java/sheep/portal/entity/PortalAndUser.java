package sheep.portal.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@TableName(value="PortalAndUser")  //表名
public class PortalAndUser {
    private int portal_id;
    private int user_id;
}
