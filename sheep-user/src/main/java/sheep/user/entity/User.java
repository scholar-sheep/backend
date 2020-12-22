package sheep.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName(value="user")  //表名
public class User implements Serializable {   //分布式传输实体需要序列化
    @TableId(value = "ID", type = IdType.AUTO)
    private int ID;
    private int sex;
    private String username;
    private int usertype;
    private String mobile;
    private String password;
    private String avatar;
    private String email;
    private String note;
    private String code;
    private Date birthday;
//    private String github;
//    private String wechat;
    //private Date create_time;
    //private Date login_time;

}
