/*
 * 
 * Megan Mosier
 * CSD-420
 * Module 6.2
 * 
 * Generic Bubble Sort
 * 
 * This program implements bubble sort as two generic methods:
 * 1. bobbleSortComparable- sorts using the Comparable interface
 *  2. bubbleSortComparator- sorts using a Comparator passed in by the caller
 * 
 * Both methods use the sane nested-loop bubble sort logic; only the comparison line differs
 * 9compareTo() versus comparator.compare()).
 */

 import java.util.Comparator;
 public class Generic_Bubble_Sort {

    public static void main(String[] args) {

        //  Test 1:  Comparable bubble sort with Integers 
        Integer[] intValues = {5, 3, 8, 1, 9, 2, 7};

        System.out.println("Test 1: Comparable bubble sort - Integer array");
        System.out.print("Before: ");
        printArray(intValues);

        bubbleSortComparable(intValues);

        System.out.print("After: ");
        printArray(intValues);

        //  Test 2:  Comparable bubble sort with Strings
        String[] strValues = {"banana", "apple", "cherry", "date", "fig"};

        System.out.println("\nTest 2: Comparable bubble sort - String array");
        System.out.print("Before: ");
        printArray(strValues);

        bubbleSortComparable(strValues);

        System.out.print("After: ");
        printArray(strValues);

        //  Test 3:  Comparator bubble sort - Employees sorted by name
        Employee[] employees = {
            new Employee("Diana", 29),
            new Employee("Aaron", 41),
            new Employee("Chris", 35),
            new Employee("Beth", 24)
        };

        System.out.println("\nTest 3: Comparator bubble sort - Employees by name");
        System.out.print("Before: ");
        printArray(employees);

        // Comparator built from Employee's name field
        Comparator<Employee> byName = Comparator.comparing(Employee::getName);
        bubbleSortComparator(employees, byName);

        System.out.print("After: ");
        printArray(employees);
    }

    /*
     * Generic bubble sort using the Comparable interface.  
     * T must extend Comparable<T>m
     */
    public static <T extends Comparable<T>> void bubbleSortComparable(T[] array) {

        int n= array.length;

        for(int i = 0; i < n -1; ++i) {

            for(int j = 0; j < n - 1 - i; ++j) {

                //compareTo() returns . 0 if array[j] is "greater than" array[j + 1]
                if (array[j].compareTo(array[j + 1]) > 0) {

                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    /*
     * Generic bubble sort using the Comparator interface.
     * T can be any ty[pe because the ordering logic lives in the Comparator object passed
     * in by the caller, not in the type itself
     */
    public static <T> void bubbleSortComparator(T[] array, Comparator<T> comparator) {
        int n = array.length;

        for (int i = 0; i < n - 1; ++i) {

            for (int j = 0; j < n - 1 - i; ++j) {

                //compare() returns > 0 if array[j] should come after array[j + 1]
                if (comparator.compare(array[j], array[j + 1]) > 0) {

                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    //Generic array printer - works for any object type via toString()
    public static <T> void printArray(T[] arrayParam) {

        System.out.print("{ ");

        for (T e : arrayParam) {
            System.out.print(e + " ");
        }

        System.out.println("}");
    }
        static class Employee {

            private String name;
            private int age;

            public Employee(String name, int age) {
                this.name = name;
                this.age = age;
            }

            public String getName() {
                return name;
            }

            public int getAge() {
                return age;
            }

            @Override
            public String toString() {
                return name + "(" + age + ")";
            }
        }
    }
 