package sheep.paper.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "paper_info")
public class Paper implements Serializable {
    @Id
    @Column(name = "paper_id")
    private int paperId;
    @Column(name = "paper_stat")
    private int paperStat;
    @Column(name = "paper_end")
    private int paperEnd;
    @Column(name = "doc_type")
    private int docType;
    private String lang;
    private String publisher;
    private int volume;
    private int issue;
    private String isbn;
    private String doi;
    @Column(name = "pdf")
    private String pdfLink;

//    Data in ElasticSearch
    @Transient
    private String paperAbstract;
//    TODO 多作者？
    @Transient
    private String authorName;
    @Transient
    private String authorOrg;
    @Transient
    private String fieldOfStudy;
//    TODO keywords 应该改为 List<String>
    @Transient
    private String keywords;
    @Transient
    private int nCitation;
    @Transient
    private String title;
    @Transient
    private String url;
    @Transient
    private String venue;
    @Transient
    private int year;

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public int getPaperStat() {
        return paperStat;
    }

    public void setPaperStat(int paperStat) {
        this.paperStat = paperStat;
    }

    public int getPaperEnd() {
        return paperEnd;
    }

    public void setPaperEnd(int paperEnd) {
        this.paperEnd = paperEnd;
    }

    public int getDocType() {
        return docType;
    }

    public void setDocType(int docType) {
        this.docType = docType;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public String getPaperAbstract() {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorOrg() {
        return authorOrg;
    }

    public void setAuthorOrg(String authorOrg) {
        this.authorOrg = authorOrg;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getnCitation() {
        return nCitation;
    }

    public void setnCitation(int nCitation) {
        this.nCitation = nCitation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
