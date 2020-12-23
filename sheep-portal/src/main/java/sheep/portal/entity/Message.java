package sheep.portal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value="Message")  //表名
public class Message {
    //把主动发第一条消息的人当成存入用户ID
    //被发消息的人存入学者ID
    int user_id;
    String portal_id;
    String message;
    Date time;
}
