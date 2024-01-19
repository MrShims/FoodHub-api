package testentity;

import lombok.With;
import org.mrshim.menuservice.model.Dish;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@With
public class DishTestBuilder implements TestBuilder<Dish> {

    private String name = "name " + random.nextInt() * 100;
    private List<String> ingredients = new ArrayList<>();
    private BigDecimal price = new BigDecimal(random.nextInt() * 100);
    private String description = "description " + random.nextInt() * 100;

    private DishTestBuilder() {
    }

    private DishTestBuilder(String name, List<String> ingredients, BigDecimal price, String description) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.description = description;
    }

    public static DishTestBuilder aDish() {
        return new DishTestBuilder();
    }

    @Override
    public Dish build() {
        final Dish dish = new Dish();
        dish.setName(name);
        dish.setPrice(price);
        dish.setIngredients(ingredients);
        dish.setDescription(description);
        return dish;
    }
}
