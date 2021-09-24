package dao;

import model.Post;

import java.util.List;

public interface PostDaoInterface {
    void add(Post post);
    List<Post> findAll();
    Post findById(int id);
    void update(int id, String content);
    void deleteAll();
    void deleteById(int id);
}
