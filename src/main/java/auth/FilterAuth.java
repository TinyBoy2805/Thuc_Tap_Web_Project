package auth;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "FilterAuth", value = "/FilterAuth")
public class FilterAuth extends HttpServlet implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();

        //skip these routes
        String[] publicPaths = {"/auth", "/product", "/about", "/blog", "/product", "/home", "/contact"};
        for (String p : publicPaths) {
            if (path.startsWith(req.getContextPath() + p)) {
                chain.doFilter(req, res);
                return;
            }
        }

        //routes khác sẽ bị chuyển về login
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/user/pages/NotFoundPage.jsp");
            return;
        }

        chain.doFilter(req, res);

    }
}