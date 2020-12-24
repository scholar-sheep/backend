package sheep.portal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value="Message")  //表名
public class Message {
    //把主动发第一条消息的人当成存入用户ID
    //被发消息的人存入学者ID
    //this为当前用户
    //that为另一个有交流的用户
    int this_user_id;
    int that_user_id;
    String message;
    Date time;
    //0为未读，1为已读
    int status;

    public Message(int this_user_id, int that_user_id, String message) {
        this.this_user_id = this_user_id;
        this.that_user_id = that_user_id;
        this.message = message;
        this.time = new Date();
        this.status = 0;
    }
}
