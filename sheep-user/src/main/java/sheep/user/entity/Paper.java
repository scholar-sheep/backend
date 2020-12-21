package sheep.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("paper_info")
public class Paper {
    @TableId
    private int paperId;
    private int paperStat;
    private int paperEnd;
    private int docType;
    private int volume;
    private int issue;
    private String lang;
    private String publisher;
    @TableId
    private int infoId;
    private int refPaperId;
    private int origPaperId;
    private String isbn;
    private String doi;
    private String pdf;
}
