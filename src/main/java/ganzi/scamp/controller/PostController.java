package ganzi.scamp.controller;

import ganzi.scamp.constant.Category;
import ganzi.scamp.dto.PostDto;
import ganzi.scamp.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Post API", description = "게시글 조회를 위한 API")
public class PostController {
    private final PostService postService;

    @Operation(summary = "전체 게시글 조회", description = "작성된 모든 게시글의 정보를 리스트 형태로 반환합니다.")
    @GetMapping("posts")
    public List<PostDto> getPosts() {
        return postService.getAllPosts();
    }

    @Operation(summary = "카테고리별 게시글 조회", description = "주어진 카테고리에 해당하는 게시글의 정보를 리스트 형태로 반환합니다.")
    @GetMapping("posts/category/{category}")
    public List<PostDto> getPostsByCategory(@PathVariable Category category) {
        return postService.getPostsByCategory(category);
    }

    @Operation(summary = "게시글 상세 조회", description = "주어진 ID에 해당하는 게시글의 세부 정보를 반환하며, 조회수가 1 증가합니다.")
    @GetMapping("posts/{id}")
    public PostDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }
}
