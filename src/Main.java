import abstraction.classes.Bike;
import abstraction.classes.Car;
import abstraction.interfaces.Vehicle;
import encapsulation.BankAccount;
import inheritance.Person;
import inheritance.Student;
import polymorphism.Circle;
import polymorphism.Rectangle;
import polymorphism.Shape;
import strings.FirstUniqueCharacterProblem;
import strings.PalindromeProblem;
import strings.StringHelper;

public class Main {
    public static void main(String[] args) {
            System.out.println("Inheritance");
            Person john = new Person("John", 24);
            Student michael = new Student("Michael", 20, "12345678901");
            System.out.println("Name: " + john.getName());
            System.out.println("StudentID: " + michael.getStudentID());

            System.out.println("\nPolymorphism");
            Shape circle = new Circle();
            Shape rectangle = new Rectangle();
            circle.draw();
            rectangle.draw();
            System.out.println("\nAbstraction");
            Vehicle car = new Car("Toyota");
            Vehicle bike = new Bike("Bike");
            car.start();
            car.accelerate(50);
            car.brake();
            car.stop();
            System.out.println("--------");
            bike.start();
            bike.accelerate(15);
            bike.brake();
            bike.stop();

            System.out.println("\nEncapsulation");
            BankAccount account = new BankAccount("Ana", 1000);
            //account.balance = -10000; "The variable is private, and this operation is illegal!"
            System.out.println("Owner: " + account.getOwner());
            System.out.println("Balance: " + account.getBalance());
            account.deposit(500);
            account.withdraw(200);
            System.out.println("Balance after operations: " + account.getBalance());

            System.out.println("\nReverseString");
            String myString = "myString";
            StringHelper reverse = new StringHelper();
            String reversedMyString = reverse.reverse(myString);
            System.out.println("MyString: " + myString);
            System.out.println("Reversed MyString: " + reversedMyString);

            System.out.println("\nPalindrome Prolbem");
            Boolean isPalindrome;
            String palindromeExample = "Alam";
            PalindromeProblem palindromeProblem = new PalindromeProblem();
            isPalindrome = palindromeProblem.isPalindrome(palindromeExample);
            System.out.println("Is " + palindromeExample + " palindrome? " + isPalindrome);


            System.out.println("\nFirst Unique Character Prolbem");
            FirstUniqueCharacterProblem firstUniqueCharacterProblem = new FirstUniqueCharacterProblem();
            System.out.println(firstUniqueCharacterProblem.firstUniqueChar("ala ma kota a kot ma ale"));

        }
    }
