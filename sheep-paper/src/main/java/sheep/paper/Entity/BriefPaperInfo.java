package sheep.paper.Entity;

import java.util.List;

public class BriefPaperInfo {
    private String paperId;
    private String paperTitle;
    private List<String> authorNames;
    private int nCitation;
    private String venue;
    private int year;

    public int getnCitation() {
        return nCitation;
    }

    public void setnCitation(int nCitation) {
        this.nCitation = nCitation;
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

    //    private String paperAbstract;
//    private String publisher;
//    private String pdfLink;
    private boolean favored;

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

//    public String getPaperAbstract() {
//        return paperAbstract;
//    }

//    public void setPaperAbstract(String paperAbstract) {
//        this.paperAbstract = paperAbstract;
//    }

//    public String getPublisher() {
//        return publisher;
//    }

//    public void setPublisher(String publisher) {
//        this.publisher = publisher;
//    }

//    public String getPdfLink() {
//        return pdfLink;
//    }

//    public void setPdfLink(String pdfLink) {
//        this.pdfLink = pdfLink;
//    }

    public boolean isFavored() {
        return favored;
    }

    public void setFavored(boolean favored) {
        this.favored = favored;
    }
}
