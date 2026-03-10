package user.service;

import user.dao.BlogDAO;
import admin.model.Blog;

import java.util.List;

public class BlogService
{
    private BlogDAO blogDAO;

    public BlogService()
    {
        this.blogDAO = new BlogDAO();
    }

    public List<Blog> getBlogs(int page, int pageSize)
    {
        return this.blogDAO.getBlogs(page, pageSize);
    }



}
