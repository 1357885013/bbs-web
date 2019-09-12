import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int count = 0;
        ServletContext servletContext = req.getServletContext();
        count = (int) servletContext.getAttribute("count");
        resp.getOutputStream().write(("已被访问" + count + "次 " + req.getQueryString()).getBytes());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        if (servletContext.getAttribute("count") == null)
            servletContext.setAttribute("count", 0);
        else
            servletContext.setAttribute("count", (int) servletContext.getAttribute("count") + 1);

        super.service(req, resp);
    }

}
