package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealMemoryStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MealServlet extends HttpServlet {

    private Storage<Meal> storage;

    private static final Meal EMPTY = new Meal();

    @Override
    public void init() {
        storage = new MealMemoryStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("meals", MealsUtil.filteredByStreams(storage.getAll(), LocalTime.of(0, 0),
                    LocalTime.MAX, MealsUtil.CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        String id = request.getParameter("id");
        Meal meal;
        switch (action) {
            case "delete":
                storage.delete(Integer.valueOf(id));
                response.sendRedirect("meals");
                return;
            case "edit":
                meal = storage.get(Integer.valueOf(id));
                break;
            case "new":
                meal = EMPTY;
                break;
            default:
                response.sendRedirect("meals");
                return;
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal m = new Meal(LocalDateTime.parse(request.getParameter("date")),
                request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        if (id != null && !id.trim().isEmpty()) {
            m.setId(Integer.valueOf(id));
            storage.update(m);
        } else {
            storage.add(m);
        }
        response.sendRedirect("meals");
    }
}
