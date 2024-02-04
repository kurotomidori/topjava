package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MapIdStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MealServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(){
        storage = new MapIdStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if(action == null) {
            request.setAttribute("meals", MealsUtil.filteredByStreams(storage.getAll(), LocalTime.of(0, 0),
                    LocalTime.of(23, 59, 59), MealsUtil.CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        Meal meal;
        switch(action) {
            case "delete":
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "edit":
                meal = storage.get(id);
                break;
            case "new":
                meal = Meal.EMPTY;
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal" );
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal m = new Meal(LocalDateTime.parse(request.getParameter("date"), DateTimeFormatter.ISO_DATE_TIME),
                request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        if(id != null && id.trim().length() != 0) {
            m.setId(id);
            storage.update(m);
        } else {
            storage.add(m);
        }
        response.sendRedirect("meals");
    }
}
