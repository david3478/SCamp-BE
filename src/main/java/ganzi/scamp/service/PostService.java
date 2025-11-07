package ganzi.scamp.service;

import ganzi.scamp.constant.Category;
import ganzi.scamp.dto.PostDto;
import ganzi.scamp.entity.Post;
import ganzi.scamp.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // 전체 게시글 조회
    public List<PostDto> getAllPosts() {
        try {
            return postRepository.findAll()
                    .stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("전체 게시글 조회 중 오류가 발생했습니다.");
        }
    }

    // 카테고리별 게시글 조회
    public List<PostDto> getPostsByCategory(Category category) {
        try {
            return postRepository.findByCategory(category)
                    .stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("카테고리별 게시글 조회 중 오류가 발생했습니다.");
        }
    }

    // 게시글 상세 조회
    public PostDto getPost(Long id) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

            // 조회수 증가
            post.increaseViewCount();
            postRepository.save(post);

            return new PostDto(post);
        } catch (Exception e) {
            throw new RuntimeException("게시글 조회 중 오류가 발생했습니다.");
        }
    }
}
