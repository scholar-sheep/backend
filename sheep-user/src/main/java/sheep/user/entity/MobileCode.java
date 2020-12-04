package sheep.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "mobileCode")
public class MobileCode {
    @TableId(value = "mobile")
    private String mobile;
    private String code;
}
