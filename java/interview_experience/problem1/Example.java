package interview_experience.problem1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        Employee e1 = new Employee("aman", "rajput", 50);
        Employee e2 = new Employee("rishan", "rajput", 15);
        Employee e3 = new Employee("prince", "rajput", 25);

        // sort employees on age

        System.out.println(e1.equals(e2));

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(e1);
        employeeList.add(e2);
        employeeList.add(e3);


        employeeList.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.age - o2.age;
            }
        });

        for (Employee e : employeeList) {
            System.out.println(e.firstName);
        }
    }
}
