package com.sumeet.coffeeMachine;

import com.sumeet.coffeeMachine.Exception.IngredientNotAvailableException;
import com.sumeet.coffeeMachine.Exception.IngredientNotAvailableInSufficientAmountException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

public class CoffeeMachine {

    private final Semaphore noOfOutlets;

    private final Map<String, Ingredient> ingredients;

    private final Map<String, Beverage> availableBeverages;

    private static CoffeeMachine coffeeMachine;


    /**
     * Create a singleton class to make the setup the CoffeeMachine
     * */
    public static CoffeeMachine setupCoffeeMachine(Integer noOfOutlets, List<Ingredient> ingredients, List<Beverage> availableBeverages) {

        if (coffeeMachine == null) {
            synchronized (CoffeeMachine.class) {
                coffeeMachine = new CoffeeMachine(noOfOutlets, ingredients, availableBeverages);
            }
        }

        return coffeeMachine;
    }


    private CoffeeMachine(Integer noOfOutlets, List<Ingredient> ingredients, List<Beverage> availableBeverages) {
        this.noOfOutlets = new Semaphore(noOfOutlets);

        this.ingredients = ingredients.stream().collect(Collectors.toMap(Ingredient::getName, ingredient -> ingredient));

        this.availableBeverages = availableBeverages.stream().collect(Collectors.toMap(Beverage::getName, beverage -> beverage));
    }


     private boolean makeBeverage( String beverageName ) throws InterruptedException,
             IngredientNotAvailableInSufficientAmountException {

        Beverage beverage= availableBeverages.get(beverageName);
        List<RecipeIngredient> recipeIngredients = beverage.getRecipe().getRecipeIngredientList();

        List<Lock> alreadyHoldingLocked = new ArrayList<>(recipeIngredients.size());
        try {
            //make sure all the ingredients are available before start preparing the beverage
            while (!isAllIngredientsAvailable(recipeIngredients, alreadyHoldingLocked)) {

            }
            //once all the ingredients are available, consume the ingredients and make beverage
            consumeIngredients(recipeIngredients);
        } finally {
            releaseLocks(alreadyHoldingLocked);
        }
        return true;
    }

    private void consumeIngredients(List<RecipeIngredient> recipeIngredients) throws IngredientNotAvailableInSufficientAmountException {
        //consume all the ingredient to make beverage
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            Ingredient ingredient = ingredients.get(recipeIngredient.getIngredientName());
            ingredient.useQuantity(recipeIngredient.getQuantity());
        }
    }

    private boolean isAllIngredientsAvailable(List<RecipeIngredient> recipeIngredients,
                                              List<Lock> alreadyHoldingLocked)
            throws InterruptedException, IngredientNotAvailableInSufficientAmountException {

        boolean allIngredientsAvailable = true;
        //alphabetically based on ingredient name acquire lock to avoid deadlock
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            Ingredient ingredient = ingredients.get(recipeIngredient.getIngredientName());
            //ingredients is not available in the Coffee machine
            if(ingredient == null){
                throw new IngredientNotAvailableException(recipeIngredient.getIngredientName());
            }
            //fetch the lock on the ingredient
            Lock reentrantLock = ingredient.getQuantityLock();
            //try to lock the ingredient and check for sufficient amount of ingredient
            boolean isLocked = reentrantLock.tryLock(5, TimeUnit.MILLISECONDS);
            if (isLocked) {
                if (!ingredient.isQuantityAvailable(recipeIngredient.getQuantity())) {
                    reentrantLock.unlock();
                    throw new IngredientNotAvailableInSufficientAmountException(recipeIngredient.getIngredientName());
                }
                alreadyHoldingLocked.add(reentrantLock);
            } else {
                //if cannot lock all the ingredients, release lock on all the holding ingredients
                allIngredientsAvailable = false;
                releaseLocks(alreadyHoldingLocked);
                break;
            }
        }
        return allIngredientsAvailable;
    }

    private void releaseLocks(List<Lock> alreadyHoldingLocked) {
        for(Lock holdingLock : alreadyHoldingLocked) {
            holdingLock.unlock();
        }
        alreadyHoldingLocked.clear();
    }


    public String orderBeverage(String beverageName) throws InterruptedException {

        StringBuilder orderStatus = new StringBuilder();
        if (!availableBeverages.containsKey(beverageName)) {
            orderStatus.append("---- Cannot server Beverage!!! We don't server ").append(beverageName).append(" !!! ----");
            //System.out.printf("==> %s :: ---- Cannot server Beverage!!! We don't server %s!!! ----%n", Thread.currentThread().getName(),beverageName);

        }
        else {
            try {
                //acquire semaphore
                noOfOutlets.acquire();

                try {
                    boolean canBeverageBeDelivered = makeBeverage(beverageName);
                    if (canBeverageBeDelivered) {
                         orderStatus.append("**** ").append(beverageName).append(" is ready to be served. ****");
                        //System.out.println("==> " + Thread.currentThread().getName() + " :: **** " + beverageName + " is ready to be served. ****");
                    }
                } catch (RuntimeException runtimeException) {

                    orderStatus.append("---- ").append(runtimeException.getMessage()).append("for ").append(beverageName).append(" ----");
                    //System.out.println("==> " + Thread.currentThread().getName() + " :: ---- " + runtimeException.getMessage() + "for " + beverageName + " ----");
                }

            } finally {
                //release semaphore
                noOfOutlets.release();
            }
        }

        return orderStatus.toString();
    }


    public List<String> getLowIndicatorsForAllIngredients() {
        return ingredients.values().stream().filter(Ingredient::getLowQuantityAlert).map(Ingredient::getName).collect(Collectors.toList());
    }

}
