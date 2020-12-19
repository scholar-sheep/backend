package sheep.portal.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import sheep.portal.pojo.WholePortal;

import java.util.List;

@Data
@Document(indexName = "sheep-scholar")
public class EsPortal {
    @Id
    private String id;
    //学者姓名
    private String name;
    //记录该作者的所属机构
    private String org;
    //学者发布的学术成果数量
    private int n_pubs;
    //学者的学术成果被引用次数
    private int n_citation;

    private List<Tag>tags;
    private List<Pub>pubs;
    @Data
    @AllArgsConstructor
    public static class Tag
    {
        private String t;
        private int w;
    }
    @Data
    @AllArgsConstructor
    public static class Pub
    {
        private String i;
        private int r;
    }

    public EsPortal(){

    }
    public EsPortal(WholePortal wholePortal){
        this.id = wholePortal.getPortal_id();
        this.name = wholePortal.getName();
        this.org = wholePortal.getOrg();
        this.n_pubs = wholePortal.getN_pubs();
        this.n_citation = wholePortal.getN_citation();
        this.tags=wholePortal.getTags();
    }
}
