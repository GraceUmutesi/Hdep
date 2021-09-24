package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.PostDao;
import model.Post;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) { //type “psvm + tab” to autocreate this :)
        staticFileLocation("/public");

        Sql2o sql2o= new Sql2o("jdbc:postgresql://localhost:5432/blogpost","grace","1234");
        PostDao postDao = new PostDao(sql2o);

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Post> posts = postDao.findAll();
            model.put("posts", posts);

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/posts/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "post-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/posts/new", (request, response) -> { //URL to make new post on POST route
            Map<String, Object> model = new HashMap<String, Object>();
            String content = request.queryParams("content");
            Post newPost = new Post(content);
            postDao.add(newPost);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());



        get("/posts/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfPostToFind = Integer.parseInt(req.params(":id")); //pull id - must match route segment
            Post foundPost = Post.findById(idOfPostToFind); //use it to find post
            model.put("post", foundPost); //add it to model for template to display
            return new ModelAndView(model, "post-detail.hbs"); //individual post page.
        }, new HandlebarsTemplateEngine());
        get("/posts/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Post.clearAllPosts();
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());
        get("/posts/:id/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfPostToEdit = Integer.parseInt(request.params("id"));
            Post editPost = Post.findById(idOfPostToEdit);
            model.put("editPost", editPost);
            return new ModelAndView(model, "post-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update a post

        post("/posts/:id/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String newContent = request.queryParams("content");
            int idOfPostToEdit = Integer.parseInt(request.params("id"));
            Post editPost = Post.findById(idOfPostToEdit);
            editPost.update(newContent); //don’t forget me
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete an individual post

        get("/posts/:id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfPostToDelete = Integer.parseInt(req.params("id")); //pull id - must match route segment
            Post deletePost = Post.findById(idOfPostToDelete); //use it to find post
            deletePost.deletePost();
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

    }
}
