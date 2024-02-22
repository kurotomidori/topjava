package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int NOT_FOUND = 10;
    public static final int USER_MEAL1_ID = START_SEQ + 3;

    public static final Meal userMeal1 = new Meal(USER_MEAL1_ID, LocalDateTime.of(2004, 10, 18, 10, 23, 54), "завтрак", 300);
    public static final Meal userMeal2 = new Meal(USER_MEAL1_ID + 1, LocalDateTime.of(2004, 10, 19, 15, 20, 1), "обед", 500);
    public static final Meal userMeal3 = new Meal(USER_MEAL1_ID + 2, LocalDateTime.of(2004, 11, 10, 21, 20, 1), "ужин", 1000);
    public static final Meal userMeal4 = new Meal(USER_MEAL1_ID + 3, LocalDateTime.of(2005, 2, 10, 11, 23, 54), "завтрак", 500);
    public static final Meal userMeal5 = new Meal(USER_MEAL1_ID + 4, LocalDateTime.of(2005, 8, 20, 14, 21, 1), "обед", 1000);
    public static final Meal userMeal6 = new Meal(USER_MEAL1_ID + 5, LocalDateTime.of(2006, 12, 11, 19, 20, 1), "ужин", 700);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2024, 1, 1, 10, 11), "description", 100);
    }

    public static Meal getUpdate() {
        Meal updated = new Meal(userMeal1.getId(), userMeal1.getDateTime(), userMeal1.getDescription(), userMeal1.getCalories());
        updated.setDateTime(LocalDateTime.of(2024, 1, 1, 10, 11));
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
