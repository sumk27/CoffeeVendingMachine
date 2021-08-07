package com.sumeet.coffeeMachine.Exception;

public class MachineSetupException extends RuntimeException{

    public MachineSetupException(){
        throw new RuntimeException("Machine cannot be setup!!!Required params are not available!!!");
    }
}
