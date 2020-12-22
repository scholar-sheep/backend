package sheep.portal.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import sheep.portal.entity.EsPortal;

import java.util.List;

@Data
@NoArgsConstructor
public class WholePortal<T>{
    private String portal_id;
    //学者姓名
    private String name;
//    //学者规范化姓名，如"Huibin Xu"
//    private String normalizedName;
//    //该学者的职称，例如教授
//    private String position;
//    //该学者的h指数
//    private int hIndex;
    //记录该作者的所属机构
    private String org;
    //学者发布的学术成果数量
    private int n_pubs;
    //学者的学术成果被引用次数
    private int n_citation;
    private List<EsPortal.Tag> tags;
    private List<EsPortal.Pub>pubs;
    private List<T> paperList;

    public WholePortal(EsPortal es, List<T> paperList){
        this.portal_id = es.getId();
        this.name = es.getName();
        this.org = es.getOrg();
        this.n_pubs = es.getN_pubs();
        this.n_citation = es.getN_citation();
        this.tags=es.getTags();
        this.pubs = es.getPubs();
        this.paperList=paperList;
    }
}
