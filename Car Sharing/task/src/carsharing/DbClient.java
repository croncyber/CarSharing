package carsharing;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbClient {
    private final DataSource dataSource;

    public DbClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void run(String str) {
        try (Connection con = dataSource.getConnection(); // Statement creation
             Statement statement = con.createStatement()
        ) {
            //con.setAutoCommit(true);
            statement.executeUpdate(str); // Statement execution
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Company select(String query) {
        List<Company> companies = selectForList(query);
        if (companies.size() == 1) {
            return companies.get(0);
        } else if (companies.isEmpty()) {
            return null;
        } else {
            throw new IllegalStateException("Query returned more than one object");
        }
    }

    public List<Company> selectForList(String query) {
        List<Company> companies = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                int id = resultSetItem.getInt("id");
                String name = resultSetItem.getString("name");
                Company company = new Company(id, name);
                companies.add(company);
            }

            return companies;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    public List<Car> selectCarList(String query) {
        List<Car> cars = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                int id = resultSetItem.getInt("id");
                String name = resultSetItem.getString("name");
                String companyId = resultSetItem.getString("company_id");
                Car car = new Car(id, name, companyId==null?null:Integer.parseInt(companyId));
                cars.add(car);
            }

            return cars;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public List<Customer> selectCustomerList(String query) {
        List<Customer> customers = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                int id = resultSetItem.getInt("id");
                String name = resultSetItem.getString("name");
                Integer rentedCarId = resultSetItem.getInt("rented_car_id");
                Customer customer = new Customer(id, name, rentedCarId);
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public Car selectCarById(String query) {
        List<Car> cars = selectCarList(query);
        if (cars.size() == 1) {
            return cars.get(0);
        } else if (cars.isEmpty()) {
            return null;
        } else {
            throw new IllegalStateException("Query returned more than one object");
        }
    }
}
