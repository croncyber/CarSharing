package carsharing;


import carsharing.dao.CompanyDAO;
import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.util.List;
import java.util.Scanner;


public class Main {

    public static final String MSG_MAIN_MENU = """
            1. Log in as a manager
            2. Log in as a customer
            3. Create a customer
            0. Exit
            """;
    public static final String MSG_MANAGER_MENU = """
                        
            1. Company list
            2. Create a company
            0. Back""";
    public static final String MSG_CUSTOMER_MENU = """
            1. Rent a car
            2. Return a rented car
            3. My rented car
            0. Back
            """;
    public static final String MSG_CAR_MENU = """
            %s company
            1. Car list
            2. Create a car
            0. Back
            """;

    public static final String ENTER_COMPANY_NAME = "Enter the company name:";
    public static final String ENTER_CAR_NAME = "Enter the car name:";
    public static final String ENTER_CUSTOMER_NAME = "Enter the customer name:";
    public static final String MSG_CHOOSE_COMPANY = "Choose the company:";
    public static final String MSG_CHOOSE_CUSTOMER = "Choose a customer:";
    private static final String MSG_CHOOSE_CAR = "Choose a car:";
    public static final String MSG_SHOW_CAR_LIST = "Car list:";
    public static final String MSG_EMPTY_COMPANY_LIST = "The company list is empty!";
    public static final String MSG_EMPTY_CAR_LIST = "The car list is empty!";
    public static final String MSG_EMPTY_CUSTOMER_LIST = "The customer list is empty!";
    public static final String MSG_NOT_RENT_CAR = "You didn't rent a car!";
    public static final String MSG_ALREADY_RENTED_CAR = "You've already rented a car!";
    public static final String MSG_NO_AVAILABLE_CARS = "No available cars in the '%s' company";
    public static final String MSG_RETURNED_RENTED_CAR = "You've returned a rented car!";
    public static final String MSG_YOU_RENTED_CAR = "You rented '%s'";
    public static final String TRY_ENTER_AGAIN = "Command is not correct, try enter again...";
    public static final String BACK = "0. Back";
    public static final String MSG_YOUR_RENTED_CAR = """
            Your rented car:
            %s
            Company:
            %s
            """;


    private static final CompanyDAO companyDao = new DbCompanyDao();


    private static void managerProcess() {

        Scanner scanner = new Scanner(System.in);
        boolean isProcess = true;
        while (isProcess) {
            System.out.println(MSG_MANAGER_MENU);
            int command = scanner.nextInt();
            System.out.println();
            switch (command) {
                case 1 -> {
                    List<Company> companyList = companyDao.findCompanyAll();
                    if (companyList.isEmpty()) {
                        System.out.println(MSG_EMPTY_COMPANY_LIST);
                    } else {
                        System.out.println(MSG_CHOOSE_COMPANY);
                        companyList.forEach(c -> System.out.println(c.getId() + ". " + c.getName()));
                        System.out.println(BACK);
                        int companyId = scanner.nextInt();
                        if (companyId == 0) break;
                        carProcess(companyList.get(companyId - 1));
                    }
                }
                case 2 -> {
                    System.out.println(ENTER_COMPANY_NAME);
                    Scanner scanner1 = new Scanner(System.in);
                    String companyName = scanner1.nextLine();
                    companyDao.addCompany(new Company(companyName));
                }
                case 0 -> isProcess = false;

            }
        }
    }

    private static void carProcess(Company company) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf((MSG_CAR_MENU) + "%n", company.getName());
        boolean isProcess = true;
        while (isProcess) {
            int command = scanner.nextInt();
            System.out.println();
            switch (command) {
                case 1 -> {
                    List<Car> carList = companyDao.findCarAllByCompanyId(company.getId());
                    if (carList.isEmpty()) {
                        System.out.println(MSG_EMPTY_CAR_LIST);
                    } else {
                        System.out.println(MSG_SHOW_CAR_LIST);
                        carList.forEach(c -> System.out.println(carList.indexOf(c) + 1 + ". " + c.getName()));
                    }
                }
                case 2 -> {
                    System.out.println(ENTER_CAR_NAME);
                    Scanner scanner1 = new Scanner(System.in);
                    String carName = scanner1.nextLine();
                    companyDao.addCar(new Car(carName, company.getId()));
                }
                case 0 -> isProcess = false;
            }


        }

    }

    public static void mainProcess() {
        Scanner scanner = new Scanner(System.in);
        boolean isProcess = true;
        while (isProcess) {
            System.out.print(MSG_MAIN_MENU);
            int firstCommand = scanner.nextInt();
            switch (firstCommand) {
                case 1 -> managerProcess();
                case 2 -> {
                    List<Customer> customers = companyDao.findCustomerAll();
                    if (customers.isEmpty()) System.out.println(MSG_EMPTY_CUSTOMER_LIST);
                    else {
                        System.out.println(MSG_CHOOSE_CUSTOMER);
                        customers.forEach(c -> System.out.println(customers.indexOf(c) + 1 + ". " + c.getName()));
                        System.out.println(BACK);
                        int customerId = scanner.nextInt();
                        if (customerId == 0) break;
                        customerProcess(customers.get(customerId - 1));
                    }
                }
                case 3 -> {
                    System.out.println(ENTER_CUSTOMER_NAME);
                    Scanner scanner1 = new Scanner(System.in);
                    String customerName = scanner1.nextLine();
                    companyDao.addCustomer(customerName);
                }
                case 0 -> isProcess = false;
                default -> System.out.println(TRY_ENTER_AGAIN);
            }
        }

    }

    private static void customerProcess(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        boolean isProcess = true;
        while (isProcess) {
            System.out.println(MSG_CUSTOMER_MENU);
            int command = scanner.nextInt();
            System.out.println();
            switch (command) {
                case 1 -> {
                    if (customer.getRentedCarId() != 0) System.out.println(MSG_ALREADY_RENTED_CAR);
                    else {
                        List<Company> companyList = companyDao.findCompanyAll();
                        if (companyList.isEmpty()) {
                            System.out.println(MSG_EMPTY_COMPANY_LIST);
                        } else {
                            System.out.println(MSG_CHOOSE_COMPANY);
                            companyList.forEach(c -> System.out.println(c.getId() + ". " + c.getName()));
                            System.out.println(BACK);
                            int companyId = scanner.nextInt();
                            System.out.println();
                            if (companyId == 0) break;

                            List<Car> cars = companyDao.findAvailableAllCar(companyList.get(companyId - 1).getId());
                            if (cars.isEmpty()) System.out.println(MSG_NO_AVAILABLE_CARS);
                            else {
                                System.out.println(MSG_CHOOSE_CAR);
                                cars.forEach(car -> System.out.println(cars.indexOf(car) + 1 + ". " + car.getName()));
                                System.out.println(BACK);
                                int rentCarId = scanner.nextInt();
                                if (rentCarId == 0) break;

                                System.out.println();
                                System.out.printf((MSG_YOU_RENTED_CAR) + "%n", cars.get(rentCarId - 1).getName());
                                customer.setRentedCarId(rentCarId);
                                companyDao.updateCustomer(customer);
                                System.out.println();
                            }
                        }
                    }

                }
                case 2 -> {
                    if (customer.getRentedCarId() == 0) System.out.println(MSG_NOT_RENT_CAR);
                    else {
                        customer.setRentedCarId(null);
                        companyDao.updateCustomer(customer);
                        System.out.println(MSG_RETURNED_RENTED_CAR);
                    }
                }
                case 3 -> {
                    if (customer.getRentedCarId() == 0) {
                        System.out.println(MSG_NOT_RENT_CAR);
                    } else {
                        Car car = companyDao.findCarById(customer.getRentedCarId());
                        Company company = companyDao.findCompanyById(car.getCompanyId());
                        System.out.printf(MSG_YOUR_RENTED_CAR, car.getName(), company.getName());
                    }
                }
                case 0 -> isProcess = false;

            }
        }
    }


    public static void main(String[] args) {
        mainProcess();
    }
}