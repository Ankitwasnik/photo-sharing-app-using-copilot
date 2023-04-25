package com.talentica.copilot.service;

import com.talentica.copilot.dto.CreatePostRequest;
import com.talentica.copilot.dto.PostDto;
import com.talentica.copilot.enums.ReactionType;
import com.talentica.copilot.exception.ResourceNotFoundException;
import com.talentica.copilot.model.Post;
import com.talentica.copilot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  @Override
  // Create a new post
  public PostDto createPost(CreatePostRequest request) {
    log.info("Creating a new post");
    log.debug("Request: {}", request);

    Post post = mapToEntity(request);
    Post savedPost = postRepository.save(post);
    return mapToDto(savedPost);
  }

  @Override
  public PostDto getPostById(Long id) {
     log.info("Getting post by id: {}", id);
    Post post = postRepository.findById(id).orElseThrow(() -> {
      log.error("Post not found with id: {}", id);
      return new ResourceNotFoundException("Post not found");
    });
    return mapToDto(post);
  }

  @Override
  public Page<PostDto> getAllPosts(int page, int size) {
    log.info("Getting all posts");
    Page<Post> posts = postRepository.findAll(PageRequest.of(page, size));
    return posts.map(this::mapToDto);
  }

  private PostDto mapToDto(Post post) {
    return PostDto.builder()
        .id(post.getId())
        .caption(post.getCaption())
        .mediaUrl(post.getMediaUrl())
        .build();
  }

  private Post mapToEntity(CreatePostRequest request) {
    return Post.builder()
        .caption(request.getCaption())
        .mediaUrl(request.getMediaUrl())
        .build();
  }


}