package sheep.paper.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

public class Paper implements Serializable {
    private String paperId;
    private String title;
    private List<Author> authors;
    private String venue;
    private int year;
    private List<String> keywords;
    private int nCitation;
    private String publisher;
    private String isbn;
    private String issn;
    private String pdfLink;
    private List<String> url;
    private String paperAbstract;
    private Boolean favored;

    public String getPaperId() {
        return paperId;
    }

    public Boolean getFavored() {
        return favored;
    }

    public void setFavored(Boolean favored) {
        this.favored = favored;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
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

    public List<String> getKeywords() {
        return keywords;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
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
