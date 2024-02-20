package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;
    public static final int USER_MEAL1_ID = START_SEQ + 3;

    public static final Meal userMeal1 = new Meal(USER_MEAL1_ID, LocalDateTime.parse("2004-10-18T10:23:54") ,"завтрак",300);
    public static final Meal userMeal2 = new Meal(100004, LocalDateTime.parse("2004-10-19T15:20:01") ,"обед",500);
    public static final Meal userMeal3 = new Meal(100005, LocalDateTime.parse("2004-11-10T21:20:01") ,"ужин",1000);
    public static final Meal userMeal4 = new Meal(100006, LocalDateTime.parse("2005-02-10T11:23:54") ,"завтрак",500);
    public static final Meal userMeal5 = new Meal(100007, LocalDateTime.parse("2005-08-20T14:21:01") ,"обед",1000);
    public static final Meal userMeal6 = new Meal(100008, LocalDateTime.parse("2006-12-11T19:20:01") ,"ужин",700);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now(), "description", 100);
    }

    public static Meal getUpdate() {
        Meal updated = new Meal(userMeal1.getId(), userMeal1.getDateTime(), userMeal1.getDescription(), userMeal1.getCalories());
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription("UpdatedDescription");
        updated.setCalories(1);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

}
