# TikaFoods - Multi-vendor Online Food Ordering Application

## Overview

Multivendor(multiple restaurants) food ordering application. It allows users to order food from multiple restaurants.
Frontend is built with React and backend is built with Spring Boot. Plan to deploy it on AWS EKS(Kubernetes) using IaC and automated CI/CD pipeline.

Currently, using MySQL as a database, plan to be able to use PostgreSQL (known as Postgres) as well.

Plan to create a version using microservices architecture.

## Motivation

To be a reference project for Spring Boot and React, using MySQL as a database (PostgreSQL will also be supported).
Initially using Terraform to manage the infrastructure on AWS and CI/CD with GitHub Actions, Jenkins, SonarQube, Snyk,
ArgoCD, Prometheus, Grafana, Elasticsearch, Kibana, Logstash, etc.

Java using the power of Spring Boot, Java 21 version features, Native Image, GraalVM, etc.

Also implement a notification system to send notifications to Slack and email.

## Requirements

- Java 21 Graal VM
- MySQL
- Postgres
- Kubernetes
- GitHub Actions
- Jenkins
- SonarQube
- Snyk
- ArgoCD
- Prometheus
- Grafana
- Elasticsearch
- Kibana
- Logstash
- AWS EKS
- React


## API testing

### Testing POST /api/v1/restaurant

#### Creating Restaurant

```json
{
    "restaurantName": "Takeru Delivery",
    "description": "Description of the restaurant here.",
    "cuisineType": "Japanese",
    "address": {
        "streetAddress": "12 Yamato Takeru Street",
        "city": "Nara",
        "stateProvince": "Nara",
        "postalCode": "12121212",
        "country": "Japan" 
    },
    "contactInformation": {
        "email": "yamato.takeru@email.com",
        "mobile": "090-1234-5678",
        "website": "takeru_delivery.com",
        "twitter": "@takeru_delivery",
        "instagram": "@takeru_delivery"
    },
    "openingHours": "Mon-Sun: 9:00 AM - 9:00 PM",
    "images": [
        "https://cdn.pixabay.com/photo/2022/02/11/05/54/food-7006591_1280.jpg",
        "https://cdn.pixabay.com/photo/2024/05/02/18/08/rolls-8735316_960_720.jpg"]
}
```

## Usage

## Contributing

You are welcome to contribute. See LICENSE for details.

## License

See [LICENSE](LICENSE).

## TODOs:


Now, let's talk about performance. The current implementation using EntityUniquenessService and the findByCategoryNameAndRestaurant method in IngredientCategoryRepository is a reasonable approach. However, there are a few things we can do to potentially improve performance:
1. AUTOMATICALLY IMPLEMENTED? - Database Indexing:
   Ensure Proper Indexing: Make sure you have a database index on the columns involved in the unique constraint (categoryName and restaurant_id in your ingredient_category table). This index will significantly speed up the database's search for duplicates.
   AUTOMATICALLY IMPLEMENTED? - Composite Index: Consider creating a composite index on both columns together. This can be even more efficient than separate indexes, especially if you frequently query based on both categoryName and restaurant_id.
2. PARTIALLY IMPLEMENTED - Caching (Consider Carefully):
   Hibernate Second-Level Cache: If you have a large number of ingredient categories and find that the uniqueness check is a performance bottleneck, you could explore using Hibernate's second-level cache. This would cache the results of the findByCategoryNameAndRestaurant query, reducing the number of database hits.
   Caching Trade-offs: Be aware that caching introduces complexity and potential data consistency issues. If you decide to use caching, make sure you understand how to manage it properly (e.g., cache invalidation strategies) to avoid serving stale data.
3. Code Optimization (Minor):
   Direct Repository Call: In your createIngredientCategory method, you could potentially bypass the EntityUniquenessService and directly call the findByCategoryNameAndRestaurant method on the IngredientCategoryRepository. This would eliminate a small amount of overhead, but it might reduce the reusability of your uniqueness check logic.
4. Database Optimization:
   Database Tuning: General database tuning (e.g., optimizing database configuration, query planner settings) can also impact performance. However, this is a broader topic and might not be necessary for your specific use case.

List and Arrays: PersistentList and PersistentArray can be used to store and retrieve lists and arrays in your database.


### Protect Against SQL Injection:
Parameterized Queries/Prepared Statements: Always use parameterized queries or prepared statements when interacting with your database. This prevents attackers from injecting malicious SQL code.
String query = "SELECT * FROM users WHERE username = ?";
PreparedStatement statement = connection.prepareStatement(query);
statement.setString(1, username);
ResultSet resultSet = statement.executeQuery();

### Protect Against Cross-Site Scripting (XSS):
ORMs and Query Builders: If you're using JPA/Hibernate, they often handle parameterization for you. However, be cautious with custom JPQL or native queries. 3. Input Validation:
Validate All Input: Validate user input (especially in forms) on both the frontend (for immediate feedback) and backend (for security).
Data Type, Length, Format: Check for valid data types, lengths, and formats (e.g., email addresses, phone numbers).
Sanitize Data: Escape or sanitize data before displaying it to prevent cross-site scripting (XSS) vulnerabilities. 4. Authentication and Authorization:
Strong Passwords: Enforce strong password policies (length, complexity, no common passwords).
Multi-Factor Authentication (MFA): Consider implementing MFA to add an extra layer of security.
Principle of Least Privilege: Grant users only the necessary permissions. 5. Cross-Site Request Forgery (CSRF) Protection:
Enable CSRF Protection: Spring Security has built-in CSRF protection. Ensure it's enabled.
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
@Override
protected void configure(HttpSecurity http) throws Exception {
http
// ... other configurations ...
.csrf().disable(); // Remove this line to enable CSRF protection
}
}
CSRF Tokens: Use CSRF tokens in your forms to prevent unauthorized requests. 6. Cross-Site Scripting (XSS) Prevention:
Output Encoding: Encode data when displaying it in web pages to prevent the browser from interpreting it as code.
Content Security Policy (CSP): Implement CSP headers to control the resources the browser is allowed to load. 7. Session Management:
Secure Cookies: Use the HttpOnly and Secure flags for session cookies.
Session Timeout: Set appropriate session timeouts.
Regenerate Session IDs: Regenerate session IDs after authentication and other sensitive actions. 8. Keep Dependencies Updated:
Regular Updates: Regularly update your Spring Boot, Spring Security, and other dependencies to patch known vulnerabilities. 9. Error Handling:
Don't Reveal Sensitive Information: Avoid exposing sensitive information in error messages.
Log Errors Securely: Log errors appropriately, but avoid logging sensitive data. 10. Security Testing: * Penetration Testing: Consider professional penetration testing to identify vulnerabilities. * Automated Security Scans: Use automated tools to scan your code for common vulnerabilities.


   The current implementation is functional and likely performs well for a reasonable number of ingredient categories. We can further optimize it with indexing and potentially caching. The choice to use caching depends on specific performance needs and willingness to manage the added complexity.
   
### Admin Panel

Building an admin panel for the owner of the online food ordering application is a great way to provide control and insights into the business.
Here's a breakdown of key features and considerations for the admin panel:

1. Authentication and Authorization
   Secure Login: Implement a robust authentication system (e.g., Spring Security) to protect the admin panel from unauthorized access.
   Role-Based Access Control (RBAC): Define different roles (e.g., owner, manager, staff) and restrict access to features and data based on roles.
2. Dashboard
   Key Metrics: Display important metrics like total orders, revenue, average order value, new customers, and top-selling items.
   Visualizations: Use charts and graphs to present data in an easy-to-understand format.
   Real-time Updates: Consider using technologies like WebSockets to provide near real-time updates on orders and other activities.
3. Restaurant Management
   CRUD Operations: Allow the owner to create, read, update, and delete (CRUD) restaurants, including details like name, address, cuisine, opening hours, and contact information.
   Menu Management: Enable the owner to manage the menu for each restaurant, including adding, editing, and removing food items, categories, prices, and images.
   Order Management: Provide a comprehensive view of orders for each restaurant, including order details, customer information, order status, and payment status.
4. User Management
   Customer Management: View and manage customer accounts, including their order history, addresses, and payment methods.
   Staff Management: Allow the owner to add, edit, and remove staff members for their restaurants, assigning roles and permissions.
5. Reporting and Analytics
   Sales Reports: Generate reports on sales by day, week, month, or custom date ranges.
   Product Performance: Analyze the performance of individual food items and categories.
   Customer Insights: Gain insights into customer behavior, such as average order value, order frequency, and popular items.
6. Settings and Configuration
   Payment Gateway Integration: Allow the owner to configure payment gateway settings (e.g., Stripe API keys).
   Delivery Zones: Define delivery zones and associated fees.
   Email Templates: Customize email templates for order confirmations, notifications, and marketing campaigns.
   Technology Stack
   Frontend: React is a great choice for building a dynamic and interactive admin panel.
   Backend: Spring Boot provides a robust framework for building RESTful APIs to handle data interactions.
   Database: MySQL is a suitable relational database for storing your application data.
   Payment Gateway: Stripe is a popular and reliable payment gateway for online businesses.
   Additional Considerations
   User Experience (UX): Design a clean, intuitive, and user-friendly interface for the admin panel.
   Security: Implement appropriate security measures to protect sensitive data.
   Scalability: Consider the future growth of your application and design the admin panel to be scalable.
  
7. Getting Started
   1. Plan Your Features: Create a detailed list of features and prioritize them based on importance.
   2. Design the UI: Sketch out the user interface for the admin panel, considering user flow and information architecture.
   3. Build the Backend APIs: Develop RESTful APIs using Spring Boot to expose the necessary data and functionality to the frontend.
   4. Develop the Frontend: Use React to build the user interface and interact with the backend APIs.
   5. Test Thoroughly: Conduct thorough testing to ensure the admin panel is functional, secure, and user-friendly.
   Remember to break down the project into smaller, manageable tasks and iterate on your development process. Good luck with building your admin panel!