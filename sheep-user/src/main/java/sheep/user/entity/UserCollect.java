package sheep.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "collect")
public class UserCollect {
    @TableId
    private int userId;
    @TableId
    private int paperId;
    @TableId
    private int infoId;
}
