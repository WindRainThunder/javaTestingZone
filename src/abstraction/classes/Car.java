package abstraction.classes;

import abstraction.interfaces.Vehicle;

public class Car implements Vehicle {
    private String brand;
    private int currentSpeed;

    public Car(String brand) {
        this.brand = brand;
        this.currentSpeed = 0;
    }

    @Override
    public void start() {
        System.out.println(brand + " starts, pedal to the metal!");
        currentSpeed = 10;
    }

    @Override
    public void stop() {
        System.out.println(brand + " stops.");
        currentSpeed = 0;
    }

    @Override
    public void accelerate(int speed) {
        currentSpeed += speed;
        if (currentSpeed > MAX_SPEED) {
            currentSpeed = MAX_SPEED;
        }
        System.out.println(brand + " accelerates " + currentSpeed + " km/h.");
    }

    @Override
    public void brake() {
        if (currentSpeed > 0) {
            currentSpeed -= 10;
        }
        if (currentSpeed < 0) {
            currentSpeed = 0;
        }
        System.out.println(brand + " slows down, speed: " + currentSpeed + " km/h.");
    }
}