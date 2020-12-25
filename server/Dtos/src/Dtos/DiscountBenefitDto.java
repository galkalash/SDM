package Dtos;

import Engine.DiscountBenefit;
import Engine.DiscountOffer;

import java.util.ArrayList;
import java.util.List;

public class DiscountBenefitDto {
    protected List<DiscountOfferDto> offer;
    protected String operator;

    public DiscountBenefitDto(List<DiscountOfferDto> sdmOffer, String operator) {
        this.offer = sdmOffer;
        this.operator = operator;
    }

    public DiscountBenefitDto(DiscountBenefit discountBenefit) {
        operator = discountBenefit.getOperator();
        offer = new ArrayList<>();
        for(DiscountOffer offer : discountBenefit.getSdmOffer()){
            this.offer.add(new DiscountOfferDto(offer));
        }
    }

    public List<DiscountOfferDto> getOffer() {
        return offer;
    }

    public String getOperator() {
        return operator;
    }

    public void setOffer(List<DiscountOfferDto> offer) {
        this.offer = offer;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
