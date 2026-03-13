package interview_experience.problem1;

import java.util.Objects;

class Employee{
    String firstName;
    String secondName;
    int age;

    Employee(String firstName, String secondName, int age){
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null ) return false;

        Employee employee = (Employee) o;
        return firstName.equals(employee.firstName) && secondName.equals(employee.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, secondName);
    }
}