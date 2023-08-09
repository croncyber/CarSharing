package carsharing.dao;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.util.List;

public interface CompanyDAO {
    List<Company> findCompanyAll();

    Company findCompanyById(int id);

    Company findCompanyByName(String companyName);

    void addCompany(Company developer);

    void updateCompany(Company developer);

    void deleteCompanyById(int id);

    void addCar(Car car);

    List<Car> findCarAllByCompanyId(int companyId);

    List<Customer> findCustomerAll();

    Car findCarById(int id);

    void updateCustomer(Customer customer);

    List<Car> findAvailableAllCar(int companyId);

    void addCustomer(String customerName);
}

