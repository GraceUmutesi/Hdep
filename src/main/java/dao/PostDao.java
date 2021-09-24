package dao;

import model.Post;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class PostDao implements PostDaoInterface{

    public final Sql2o sql2o;

    public PostDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Post post) {
        String sql="INSERT INTO posts (content) VALUES (:content)";
        try(Connection con= sql2o.open()){
            int id = (int) con.createQuery(sql,true)
                    .bind(post).executeUpdate().getKey();
            post.setId(id);
        }catch (Sql2oException e){
            System.out.println(e);
        }

    }

    @Override
    public List<Post> findAll() {
        try(Connection con=sql2o.open()){
            return con.createQuery("SELECT * FROM posts")
                    .executeAndFetch(Post.class);
        }
    }

    @Override
    public Post findById(int id) {
        try(Connection con=sql2o.open()){
            return con.createQuery("SELECT * FROM posts WHERE id=:id")
                    .addParameter("id",id)
                    .executeAndFetchFirst(Post.class);
        }
    }

    @Override
    public void update(int id, String content) {
        String sql="UPDATE posts SET (content)= (:content) WHERE id=:id";
        try(Connection con= sql2o.open()){
            con.createQuery(sql,true)
                    .addParameter("content", content)
                    .addParameter("id",id)
                    .executeUpdate();

        }catch (Sql2oException e){
            System.out.println(e);
        }

    }

    @Override
    public void deleteAll() {
        String sql="DELETE FROM posts";
        try(Connection con= sql2o.open()){
            con.createQuery(sql)
                    .executeUpdate();
        }catch (Sql2oException e){
            System.out.println(e);
        }

    }

    @Override
    public void deleteById(int id) {
        String sql="DELETE FROM posts WHERE id=:id";
        try(Connection con= sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id",id)
                    .executeUpdate();
        }catch (Sql2oException e){
            System.out.println(e);
        }

    }
}
