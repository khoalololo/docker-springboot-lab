CREATE TABLE IF NOT EXISTS departments (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS employees (
    employee_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    hire_date DATE NOT NULL,
    job_title VARCHAR(100),
    salary DECIMAL(10, 2),
    department_id INTEGER REFERENCES departments(department_id)
);

INSERT INTO departments (department_name, location) VALUES
    ('Engineering', 'San Francisco'),
    ('Human Resources', 'New York'),
    ('Sales', 'Chicago'),
    ('Marketing', 'Los Angeles'),
    ('Finance', 'Boston');

INSERT INTO employees (first_name, last_name, email, phone, hire_date, job_title, salary, department_id) VALUES
    ('John', 'Doe', 'john.doe@company.com', '555-0101', '2020-01-15', 'Software Engineer', 95000.00, 1),
    ('Jane', 'Smith', 'jane.smith@company.com', '555-0102', '2019-03-20', 'Senior Developer', 110000.00, 1),
    ('Mike', 'Johnson', 'mike.johnson@company.com', '555-0103', '2021-06-10', 'HR Manager', 85000.00, 2),
    ('Emily', 'Brown', 'emily.brown@company.com', '555-0104', '2020-09-05', 'Sales Representative', 75000.00, 3),
    ('David', 'Wilson', 'david.wilson@company.com', '555-0105', '2018-11-30', 'Marketing Director', 105000.00, 4),
    ('Sarah', 'Taylor', 'sarah.taylor@company.com', '555-0106', '2022-02-14', 'Financial Analyst', 80000.00, 5),
    ('Chris', 'Anderson', 'chris.anderson@company.com', '555-0107', '2021-08-22', 'Junior Developer', 70000.00, 1),
    ('Lisa', 'Martinez', 'lisa.martinez@company.com', '555-0108', '2020-04-18', 'HR Coordinator', 65000.00, 2);

CREATE INDEX idx_employees_department ON employees(department_id);
CREATE INDEX idx_employees_email ON employees(email);

SELECT 'Departments created:' AS info;
SELECT * FROM departments;

SELECT 'Employees created:' AS info;
SELECT * FROM employees;