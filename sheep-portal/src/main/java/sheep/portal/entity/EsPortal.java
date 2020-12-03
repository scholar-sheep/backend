package sheep.portal.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "portal")
public class EsPortal {
    @Id
    private int id;
    //学者姓名
    private String name;
    //记录该作者的所属机构ID
    private String org;
    //学者发布的学术成果数量
    private int n_pubs;
    //学者的学术成果被引用次数
    private int n_citation;
    //学者的研究兴趣标签
    private String tags_t;
    //对于研究兴趣的权重
    private int tags_w;
    public EsPortal(){

    }
    public EsPortal(WholePortal wholePortal){
        this.id = wholePortal.getId();
        this.name = wholePortal.getName();
        this.org = wholePortal.getOrg();
        this.n_pubs = wholePortal.getN_pubs();
        this.n_citation = wholePortal.getN_citation();
        this.tags_t = wholePortal.getTags_t();
        this.tags_w = wholePortal.getTags_w();
    }
}
