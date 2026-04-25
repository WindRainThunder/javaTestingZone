package abstraction.classes;

import abstraction.interfaces.Vehicle;

public class Bike implements Vehicle {
    private String name;
    private int currentSpeed;

    public Bike(String name) {
        this.name = name;
        this.currentSpeed = 0;
    }

    @Override
    public void start() {
        System.out.println(name + " is moving.");
        currentSpeed = 5;
    }

    @Override
    public void stop() {
        System.out.println(name + " stops.");
        currentSpeed = 0;
    }

    @Override
    public void accelerate(int speed) {
        currentSpeed += speed;
        System.out.println(name + " accelerates to " + currentSpeed + " km/h (max " + MAX_SPEED + ").");
    }

    @Override
    public void brake() {
        if (currentSpeed > 0) {
            currentSpeed -= 3;
        }
        if (currentSpeed < 0) {
            currentSpeed = 0;
        }
        System.out.println(name + " slows down, speed: " + currentSpeed + " km/h.");
    }
}