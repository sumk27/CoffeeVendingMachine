package com.sumeet.coffeeMachine;

import com.sumeet.coffeeMachine.Exception.IngredientNotAvailableInSufficientAmountException;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ingredient {

    private final String name;

    private final Integer totalQuantity;

    private Integer currentQuantity;

    private final AtomicInteger currentQuantityAtomic;

    private final Integer lowQuantityAlertThreshold;

    private Boolean lowQuantityAlert;

    private final Lock quantityLock;


    public Ingredient(String name, Integer totalQuantity, Integer currentQuantity, Integer lowQuantityAlertThreshold) {
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.currentQuantity = currentQuantity;
        this.currentQuantityAtomic = new AtomicInteger(currentQuantity);
        this.lowQuantityAlertThreshold = lowQuantityAlertThreshold;
        this.lowQuantityAlert = currentQuantity <= lowQuantityAlertThreshold;
        this.quantityLock = new ReentrantLock();
    }

    public String getName() {
        return name;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Integer getLowQuantityAlertThreshold() {
        return lowQuantityAlertThreshold;
    }

    public Boolean getLowQuantityAlert() {
        return lowQuantityAlert;
    }

    public Boolean isQuantityAvailable(Integer quantity) {
        return currentQuantity >= quantity;
    }

    public void useQuantity(Integer quantity) throws IngredientNotAvailableInSufficientAmountException {
        if (currentQuantity < quantity) {
            throw new IngredientNotAvailableInSufficientAmountException(name);
        }
        currentQuantity = currentQuantity - quantity;
        this.lowQuantityAlert = currentQuantity <= lowQuantityAlertThreshold;
        if(lowQuantityAlert) {
            System.out.printf("Low %s %n", name);
        }
    }

    public Lock getQuantityLock() {
        return quantityLock;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", currentQuantity=" + currentQuantity +
                ", lowQuantityAlertThreshold=" + lowQuantityAlertThreshold +
                ", lowQuantityAlert=" + lowQuantityAlert +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;

        Ingredient that = (Ingredient) o;

        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(totalQuantity, that.totalQuantity))
            return false;
        if (!Objects.equals(currentQuantity, that.currentQuantity))
            return false;
        if (!lowQuantityAlertThreshold.equals(that.lowQuantityAlertThreshold))
            return false;
        return Objects.equals(lowQuantityAlert, that.lowQuantityAlert);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (totalQuantity != null ? totalQuantity.hashCode() : 0);
        result = 31 * result + (currentQuantity != null ? currentQuantity.hashCode() : 0);
        result = 31 * result + lowQuantityAlertThreshold.hashCode();
        result = 31 * result + (lowQuantityAlert != null ? lowQuantityAlert.hashCode() : 0);
        return result;
    }
}
