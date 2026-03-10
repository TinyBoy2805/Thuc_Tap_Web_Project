package user.controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import user.model.cart.Cart;
import user.model.cart.CartItem;
import user.model.product.ProductCard;
import user.service.CartService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CartController", value = "/cart/*")
public class CartController extends HttpServlet
{
//
//    AddItem(Product)
//    DeleteItem(Pid)
//    DelAll
//            List
//    Get(Pid)
//    TotalQuantities
//            TotalAmount
//    Update(pid, p)
//    Promotion(p)

    private CartService cartService;

    @Override
    public void init() throws ServletException
    {
        this.cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        Cart myCart = (Cart) session.getAttribute("cart");
        if(myCart == null)
        {
            myCart = new Cart();
        }

        request.setAttribute("cart", myCart);
        request.getRequestDispatcher("/user/pages/Cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = (request.getParameter("quantity") != null || request.getParameter("quantity").isEmpty()) ? Integer.parseInt(request.getParameter("quantity")) : 1;

            HttpSession session = request.getSession(false);
            Cart myCart = (Cart) session.getAttribute("cart");

            if(myCart == null) myCart = new Cart();

            ProductCard productCard = this.cartService.getProductCard(productId);

            CartItem exist = myCart.isExist(productCard);

            boolean success = false;
            if(exist != null)
            {
                exist.increaseQuantity(quantity);
                success = true;
            }else
            {
                //add new cart item
                CartItem newItem = new CartItem(productCard, quantity, productCard.getPrice());
                myCart.addNewItem(newItem);
                success = true;
            }

            System.out.println("Cart: " + myCart.getCart());


            session.setAttribute("cart", myCart);
            // Trả JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK

            // Tạo JSON: Có thể tùy chỉnh chi tiết hơn (ví dụ add items list nếu cần update full cart UI)
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("success", success);
            jsonResponse.put("cart", myCart.getCart());

            Gson gson = new Gson();
            String json = gson.toJson(jsonResponse);
            response.getWriter().print(json);
            response.getWriter().flush();



    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        try
        {
            BufferedReader reader = req.getReader();
            Map<String, Object> body = gson.fromJson(reader, Map.class);
            Object productIdObj = body.get("productId");
            Object quantityObj = body.get("quantity");

            int productId = (productIdObj instanceof Double)
                    ? ((Double) productIdObj).intValue()
                    : Integer.parseInt(String.valueOf(productIdObj));

            int quantity = (quantityObj instanceof Double)
                    ? ((Double) quantityObj).intValue()
                    : Integer.parseInt(String.valueOf(quantityObj));




            HttpSession session = req.getSession(false);
            Cart cart = (Cart) session.getAttribute("cart");
            if(cart == null) cart = new Cart();

            boolean success = cart.updateQuantity(productId, quantity);

            if(success){
                double subtotal = cart.getCart().get(productId).getPrice() * quantity;
                double total = cart.getCart().values().stream()
                        .mapToDouble(i -> i.getPrice() * i.getQuantity())
                        .sum();

                session.setAttribute("cart", cart);

                out.write(gson.toJson(Map.of(
                        "success", true,
                        "subtotal", new DecimalFormat("#,###").format(subtotal),
                        "total", new DecimalFormat("#,###").format(total)
                )));
            } else
            {
                out.write(gson.toJson(Map.of("success", false, "message", "Không tìm thấy sản phẩm trong giỏ hàng")));
            }
        } catch(Exception e)
        {
            e.printStackTrace();
            out.write(gson.toJson(Map.of("success", false, "message", "Có lỗi xảy ra!")));
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        int cartItemId = Integer.parseInt(req.getParameter("cartItemId"));

        HttpSession session = req.getSession(false);
        Cart myCart = (Cart) session.getAttribute("cart");

        boolean success = myCart.deleteItemById(cartItemId);

        session.setAttribute("cart", myCart);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"status\":\"success\"}");

    }
}