package Engine;

public class Discount {
    private final String name;
    private final DiscountCondition condition;
    private final DiscountBenefit benefit;

    public Discount(DiscountCondition condition, DiscountBenefit benefit, String name) {
        this.condition = condition;
        this.benefit = benefit;
        this.name = name;
    }

    public DiscountCondition getCondition() {
        return condition;
    }

    public DiscountBenefit getBenefit() {
        return benefit;
    }

    public String getName() {
        return name;
    }
}
