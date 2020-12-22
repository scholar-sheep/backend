package sheep.paper.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sheep.paper.Entity.Favorite;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findFavoritesByUserid(int userid);

    Favorite findFavoriteByUseridAndPaperid(int userid, String paperIdStr);

    void deleteFavoriteByFavoriteid(int favoriteid);

    Boolean existsByPaperidAndUserid(String paperIdStr, int userId);

}
