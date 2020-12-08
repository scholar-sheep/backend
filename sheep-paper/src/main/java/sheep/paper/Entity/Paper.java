package sheep.paper.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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
}
