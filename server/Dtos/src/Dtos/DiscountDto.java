package Dtos;

import Engine.Discount;
import Engine.DiscountBenefit;
import Engine.DiscountCondition;

public class DiscountDto {
    private String name;
    private DiscountConditionDto condition;
    private DiscountBenefitDto benefit;

    public DiscountDto(String name, DiscountConditionDto condition, DiscountBenefitDto benefit) {
        this.name = name;
        this.condition = condition;
        this.benefit = benefit;
    }

    public DiscountDto(Discount discount, String conditionItemName) {
        this.name = discount.getName();
        condition = new DiscountConditionDto(discount.getCondition() , conditionItemName);
        benefit = new DiscountBenefitDto(discount.getBenefit());
    }

    public String getName() {
        return name;
    }

    public DiscountConditionDto getCondition() {
        return condition;
    }

    public DiscountBenefitDto getBenefit() {
        return benefit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCondition(DiscountConditionDto condition) {
        this.condition = condition;
    }

    public void setBenefit(DiscountBenefitDto benefit) {
        this.benefit = benefit;
    }
}
