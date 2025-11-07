package ganzi.scamp.repository;

import ganzi.scamp.constant.Category;
import ganzi.scamp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 주어진 카테고리에 해당하는 게시글 목록을 조회
    List<Post> findByCategory(Category category);
}