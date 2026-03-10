package user.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import user.service.ContactService;

import java.io.IOException;

@WebServlet(name = "ContactController", value = "/contact")
public class ContactController extends HttpServlet
{

    private ContactService contactService;

    @Override
    public void init() throws ServletException
    {
        this.contactService = new ContactService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

            HttpSession session = request.getSession(false);
            Integer userId = (Integer) session.getAttribute("userId");

            if(userId == null)
            {
                response.sendRedirect(request.getContextPath() + "/user/pages/Contact.jsp");
                return;
            }


            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String subject = request.getParameter("subject");
            String message = request.getParameter("message");


            boolean success = this.contactService.sendMessage(userId, username, email, phone, subject, message);

            request.setAttribute("send_status", success);
            request.getRequestDispatcher("/user/pages/Contact.jsp").forward(request, response);

        }
}
