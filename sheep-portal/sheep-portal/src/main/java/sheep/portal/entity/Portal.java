package sheep.portal.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName(value="portal")  //表名
public class Portal {
//    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    //学者姓名
    private String name;
    //学者规范化姓名，如"Huibin Xu"
    private String normalized_name;
    //该学者的职称，例如教授
    private String position;
    //该学者的h指数
    private int h_index;

    //以下在es里
    //记录该作者的所属机构ID
//    private String org;
//    //学者发布的学术成果数量
//    private int n_pubs;
//    //学者的学术成果被引用次数
//    private int n_citation;
//    //学者的研究兴趣标签
//    private String tags_t;
//    //对于研究兴趣的权重
//    private int tags_w;
//    //头像
//    private String head;


    //以下应该放进学者-学术成果表
//    //作为作者的学术成果ID
//    private String pubs_i;
//    //是学术成果的第几作者
//    private int pubs_r;
}
