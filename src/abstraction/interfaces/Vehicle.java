package abstraction.interfaces;

public interface Vehicle {
    void start();
    void stop();
    void accelerate(int speed);
    void brake();

    int MAX_SPEED = 200;
}