package Engine;

import java.util.List;

public class DiscountBenefit {
    protected final List<DiscountOffer> offer;
    protected final String operator;

    public DiscountBenefit(List<DiscountOffer> offers, String operator) {
        this.offer = offers;
        this.operator = operator;
    }

    public List<DiscountOffer> getSdmOffer() {
        return offer;
    }

    public String getOperator() {
        return operator;
    }
}
