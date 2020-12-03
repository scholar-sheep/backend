package sheep.portal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

public class WholePortal {
    private int id;
    //学者姓名
    private String name;
    //学者规范化姓名，如"Huibin Xu"
    private String normalized_name;
    //该学者的职称，例如教授
    private String position;
    //该学者的h指数
    private int h_index;
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
    public WholePortal(){

    }
    public WholePortal(Portal mysql, EsPortal es){
        this.id = mysql.getId();
        this.name = mysql.getName();
        this.normalized_name = mysql.getNormalized_name();
        this.position = mysql.getPosition();
        this.h_index = mysql.getH_index();
        this.org = es.getOrg();
        this.n_pubs = es.getN_pubs();
        this.n_citation = es.getN_citation();
        this.tags_t = es.getTags_t();
        this.tags_w = es.getTags_w();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalized_name() {
        return normalized_name;
    }

    public void setNormalized_name(String normalized_name) {
        this.normalized_name = normalized_name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getH_index() {
        return h_index;
    }

    public void setH_index(int h_index) {
        this.h_index = h_index;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public int getN_pubs() {
        return n_pubs;
    }

    public void setN_pubs(int n_pubs) {
        this.n_pubs = n_pubs;
    }

    public int getN_citation() {
        return n_citation;
    }

    public void setN_citation(int n_citation) {
        this.n_citation = n_citation;
    }

    public String getTags_t() {
        return tags_t;
    }

    public void setTags_t(String tags_t) {
        this.tags_t = tags_t;
    }

    public int getTags_w() {
        return tags_w;
    }

    public void setTags_w(int tags_w) {
        this.tags_w = tags_w;
    }
}
