package sheep.paper.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "paper_info")
public class Paper implements Serializable {
    @Id
    @Column(name = "paper_id")
    private String paperId;
    @Column(name = "paper_start")
    private String paperStart;
    @Column(name = "paper_end")
    private String paperEnd;
    @Column(name = "doc_type")
    private String docType;
    private String lang;
    private String publisher;
    private String volume;
    private String issue;
    private String isbn;
    @Column(name = "pdf")
    private String pdfLink;

//    Data in ElasticSearch
    @Transient
    private String paperAbstract;
    @Transient
    private List<Author> authors;
    @Transient
    private String fieldOfStudy;
    @Transient
    private List<String> keywords;
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

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getPaperStart() {
        return paperStart;
    }

    public void setPaperStat(String paperStart) {
        this.paperStart = paperStart;
    }

    public String getPaperEnd() {
        return paperEnd;
    }

    public void setPaperEnd(String paperEnd) {
        this.paperEnd = paperEnd;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
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

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void appendAuthors(Author new_author) {
        this.authors.add(new_author);
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
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
