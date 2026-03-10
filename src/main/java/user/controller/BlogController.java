package user.controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import admin.model.Blog;
import user.service.BlogService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BlogController", value = "/blog/*")
public class BlogController extends HttpServlet
{

    private BlogService blogService;
    private final int PAGE_SIZE = 6;
    private Gson gson;
    
    @Override
    public void init() throws ServletException
    {
        this.blogService = new BlogService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String pathInfo = request.getPathInfo();

        if(pathInfo == null || pathInfo.equals("/"))
        {
            String pageParam = request.getParameter("page");
            if(pageParam != null && !pageParam.equals("1"))
            {
                this.getBlogsJSON(request, response);
                return;
            }else
            {
                this.getBlogsByPage(request, response);
                return;
            }
        }

        String action = pathInfo.substring(1);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }


    private void getBlogsJSON(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String pageParam = request.getParameter("page");
        int page = 1;
        if (pageParam != null && !pageParam.isEmpty())
        {
            try
            {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e)
            {
                page = 1;
            }
        }

        List<Blog> blogs = this.blogService.getBlogs(page, PAGE_SIZE);
        String json = gson.toJson(blogs);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private void getBlogsByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //call user.service --> call DAO --> get BLOG
        //DAO --data--> user.service --data-->controller--page-->render

        String pageParam = request.getParameter("page");
        int page = 1;
        if (pageParam != null && !pageParam.isEmpty())
        {
            try
            {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e)
            {
                page = 1;
            }
        }


        List<Blog> blogs = this.blogService.getBlogs(page, PAGE_SIZE);
        for(Blog b: blogs)
        {
            System.out.println(b);
        }

        request.setAttribute("blogs", blogs);

        request.getRequestDispatcher("/user/pages/Blog.jsp").forward(request, response);
    }


}
