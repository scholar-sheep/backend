package sheep.paper.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sheep.paper.Entity.Paper;

public interface PaperRepository extends JpaRepository<Paper, Integer> {
    Paper findPaperByPaperId(int paperId);
}
