package sheep.paper.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import sheep.paper.Entity.Favorite;

import javax.transaction.Transactional;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findFavoritesByUserid(int userid);

    List<Favorite> findAllByUserid(int userid);

    Favorite findFavoriteByUseridAndPaperid(int userid, String paperIdStr);

    void deleteFavoriteByFavoriteid(int favoriteid);

    @Modifying
    @Transactional
    void deleteByFavoriteid(int favoriteid);

    Boolean existsByPaperidAndUserid(String paperIdStr, int userId);

}
