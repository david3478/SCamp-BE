package ganzi.scamp.service;

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

    public List<PostDto> getAllPosts() {
        try {
            return postRepository.findAll()
                    .stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("게시글 목록 조회 중 오류가 발생했습니다.");
        }
    }

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
