package carsharing;


import carsharing.dao.CompanyDAO;
import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;
import org.h2.jdbcx.JdbcDataSource;

import java.util.List;

public class DbCompanyDao implements CompanyDAO {
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    private static final String CREATE_DB = """
            CREATE TABLE IF NOT EXISTS COMPANY
            (
                ID   INTEGER auto_increment primary key,
                NAME VARCHAR not null
                constraint name1
                        unique
            );
                      
            CREATE TABLE IF NOT EXISTS car
            (
                id         integer auto_increment,
                name       VARCHAR not null
                    constraint name2
                        unique,
                company_id INTEGER not null,
                constraint id
                    primary key (id),
                constraint company_id
                    foreign key (company_id) references COMPANY (id)
            );
            
            CREATE TABLE IF NOT EXISTS CUSTOMER
            (
                id         integer auto_increment,
                name       VARCHAR not null
                    constraint name3
                        unique,
                rented_car_id INTEGER default NULL,
                constraint id2
                    primary key (id),
                constraint rented_car_id
                    foreign key (rented_car_id) references car (id)
            );
            
            """;
    private static final String SELECT_COMPANY_ALL = "SELECT * FROM COMPANY";
    private static final String SELECT_COMPANY_BY_ID = "SELECT * FROM COMPANY WHERE id = %d";
    private static final String SELECT_COMPANY_NAME = "SELECT * FROM COMPANY WHERE name = %s";
    private static final String INSERT_COMPANY_DATA = "INSERT INTO COMPANY (name) VALUES ('%s')";
    private static final String UPDATE_COMPANY_DATA = "UPDATE COMPANY SET name " +
            "= '%s' WHERE id = %d";
    private static final String DELETE_COMPANY_DATA = "DELETE FROM COMPANY WHERE id = %d";
    private static final String INSERT_CAR_DATA = "INSERT INTO CAR (name, company_id) VALUES ('%s', %d)";
    private static final String SELECT_ALL_CAR = "SELECT * FROM car WHERE company_id = %d";
    private static final String SELECT_CAR_BY_ID = "SELECT * FROM car WHERE id = %d";
    private static final String SELECT_CUSTOMER_ALL = "SELECT * FROM CUSTOMER";
    private static final String UPDATE_CUSTOMER_DATA = "UPDATE CUSTOMER SET RENTED_CAR_ID " +
            "= %d WHERE id = %d AND name = '%s'";
    private static final String SELECT_AVAILABLE_CARS = """
            SELECT ca.*
            FROM CAR ca
            LEFT JOIN CUSTOMER cu ON ca.id = cu.rented_car_id
            WHERE cu.rented_car_id IS NULL
            AND ca.company_id = %d;
            """;
    private static final String INSERT_CUSTOMER_DATA = "INSERT INTO CUSTOMER (name) VALUES ('%s')";

    private final DbClient dbClient;

    public DbCompanyDao() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(DB_URL);
        dbClient = new DbClient(dataSource);
        dbClient.run(CREATE_DB);
        //  System.out.println("Company data structure create");
    }

    @Override
    public List<Company> findCompanyAll() {
        return dbClient.selectForList(SELECT_COMPANY_ALL);
    }

    @Override
    public Company findCompanyById(int id) {
        Company company = dbClient.select(String.format(SELECT_COMPANY_BY_ID, id));

        if (company != null) {
            System.out.println("Company: Id " + id + ", found");
            return company;
        } else {
            System.out.println("Company: Id " + id + ", not found");
            return null;
        }
    }

    @Override
    public Company findCompanyByName(String companyName) {
        Company company = dbClient.select(String.format(SELECT_COMPANY_NAME, companyName));

        if (company != null) {
            System.out.println("Company: name " + companyName + ", found");
            return company;
        } else {
            System.out.println("Company: name " + companyName + ", not found");
            return null;
        }
    }

    @Override
    public void addCompany(Company company) {
        dbClient.run(String.format(
                INSERT_COMPANY_DATA, company.getName()));
        System.out.println("The company was created!");
    }

    @Override
    public void updateCompany(Company company) {
        dbClient.run(String.format(
                UPDATE_COMPANY_DATA, company.getName(), company.getId()));
        System.out.println("Company: Id " + company.getId() + ", updated");
    }

    @Override
    public void deleteCompanyById(int id) {
        dbClient.run(String.format(DELETE_COMPANY_DATA, id));
        System.out.println("Company: Id " + id + ", deleted");
    }

    @Override
    public void addCar(Car car) {
        dbClient.run(String.format(
                INSERT_CAR_DATA, car.getName(), car.getCompanyId()));
        System.out.println("The car was added!");
    }

    @Override
    public List<Car> findCarAllByCompanyId(int companyId) {
        return dbClient.selectCarList(String.format(SELECT_ALL_CAR, companyId));
    }

    @Override
    public List<Customer> findCustomerAll() {
        return dbClient.selectCustomerList(SELECT_CUSTOMER_ALL);
    }

    @Override
    public Car findCarById(int id) {
        return dbClient.selectCarById(String.format(SELECT_CAR_BY_ID, id));
    }

    @Override
    public void updateCustomer(Customer customer) {
        dbClient.run(String.format(
                UPDATE_CUSTOMER_DATA,
                customer.getRentedCarId(),
                customer.getId(),
                customer.getName()
                ));
    }

    @Override
    public List<Car> findAvailableAllCar(int companyId) {
        return dbClient.selectCarList(String.format(SELECT_AVAILABLE_CARS, companyId));
    }

    @Override
    public void addCustomer(String customerName) {
        dbClient.run(String.format(
                INSERT_CUSTOMER_DATA,
                customerName));
        System.out.println("The customer was added!");
    }


}
