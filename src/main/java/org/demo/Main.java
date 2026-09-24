package org.demo;

import org.demo.entity.Category;
import org.demo.entity.Order;
import org.demo.entity.OrderDetail;
import org.demo.entity.Product;
import org.demo.entity.User;
import org.demo.entity.UserRole;
import org.demo.util.HibernateUtil;
import org.demo.util.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        SessionFactory sessionFactory =
                HibernateUtil.getSessionFactory();

        Session session = null;
        Transaction transaction = null;

        try {

            session = sessionFactory.openSession();

            transaction = session.beginTransaction();

            // ==================================================
            // 1. FIND OR CREATE CATEGORY
            // ==================================================

            Category category =
                    session.createQuery(
                                    "from Category c " +
                                            "where c.name = :name",
                                    Category.class
                            )
                            .setParameter(
                                    "name",
                                    "Electronics"
                            )
                            .uniqueResult();

            if (category == null) {

                category =
                        new Category(
                                "Electronics",
                                "Electronic products"
                        );

                session.persist(category);

                System.out.println(
                        "Category created."
                );
            }


            // ==================================================
            // 2. FIND OR CREATE PRODUCTS
            // ==================================================

            Product laptop =
                    session.createQuery(
                                    "from Product p " +
                                            "where p.name = :name",
                                    Product.class
                            )
                            .setParameter(
                                    "name",
                                    "Gaming Laptop"
                            )
                            .uniqueResult();

            if (laptop == null) {

                laptop =
                        new Product(
                                "Gaming Laptop",
                                79999.99,
                                10
                        );

                laptop.setCategory(category);

                session.persist(laptop);

                System.out.println(
                        "Gaming Laptop created."
                );
            }


            Product mouse =
                    session.createQuery(
                                    "from Product p " +
                                            "where p.name = :name",
                                    Product.class
                            )
                            .setParameter(
                                    "name",
                                    "Wireless Mouse"
                            )
                            .uniqueResult();

            if (mouse == null) {

                mouse =
                        new Product(
                                "Wireless Mouse",
                                1499.00,
                                50
                        );

                mouse.setCategory(category);

                session.persist(mouse);

                System.out.println(
                        "Wireless Mouse created."
                );
            }


            // ==================================================
            // 3. FIND OR CREATE USER
            // ==================================================

            User user =
                    session.createQuery(
                                    "from User u " +
                                            "where u.username = :username",
                                    User.class
                            )
                            .setParameter(
                                    "username",
                                    "admin"
                            )
                            .uniqueResult();

            if (user == null) {

                String hashedPassword =
                        PasswordUtil.hashPassword(
                                "Admin@123"
                        );

                user =
                        new User(
                                "admin",
                                hashedPassword,
                                "admin@example.com",
                                UserRole.ADMIN
                        );

                session.persist(user);

                System.out.println(
                        "User created."
                );
            }


            // ==================================================
            // 4. CREATE A NEW ORDER EVERY RUN
            // ==================================================

            int laptopQuantity = 1;
            int mouseQuantity = 2;

            double laptopTotal =
                    laptop.getPrice() * laptopQuantity;

            double mouseTotal =
                    mouse.getPrice() * mouseQuantity;

            double totalAmount =
                    laptopTotal + mouseTotal;


            Order order =
                    new Order(
                            LocalDateTime.now(),
                            totalAmount,
                            user
                    );


            // ==================================================
            // 5. CREATE ORDER DETAILS
            // ==================================================

            OrderDetail laptopDetail =
                    new OrderDetail(
                            laptopQuantity,
                            laptop.getPrice(),
                            order,
                            laptop
                    );


            OrderDetail mouseDetail =
                    new OrderDetail(
                            mouseQuantity,
                            mouse.getPrice(),
                            order,
                            mouse
                    );


            // Add details to order
            order.getOrderDetails().add(laptopDetail);
            order.getOrderDetails().add(mouseDetail);


            // ==================================================
            // 6. SAVE ORDER
            // ==================================================

            // CascadeType.ALL in Order
            // will save both OrderDetails
            session.persist(order);


            // ==================================================
            // 7. COMMIT
            // ==================================================

            transaction.commit();

            System.out.println();
            System.out.println(
                    "======================================"
            );
            System.out.println(
                    "ORDER CREATED SUCCESSFULLY"
            );
            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Order ID: " + order.getId()
            );

            System.out.println(
                    "Total Amount: ₹" +
                            order.getTotalAmount()
            );


            // ==================================================
            // 8. FETCH ORDERS WITH USER + PRODUCTS
            // ==================================================

            List<Order> orders =
                    session.createQuery(
                                    """
                                    select distinct o
                                    from Order o
                                    join fetch o.user
                                    join fetch o.orderDetails od
                                    join fetch od.product
                                    order by o.id
                                    """,
                                    Order.class
                            )
                            .getResultList();


            System.out.println();
            System.out.println(
                    "========== ALL ORDERS =========="
            );


            for (Order currentOrder : orders) {

                System.out.println(
                        "Order ID: " +
                                currentOrder.getId()
                );

                System.out.println(
                        "Customer: " +
                                currentOrder
                                        .getUser()
                                        .getUsername()
                );

                System.out.println(
                        "Email: " +
                                currentOrder
                                        .getUser()
                                        .getEmail()
                );

                System.out.println(
                        "Order Date: " +
                                currentOrder.getOrderDate()
                );

                System.out.println(
                        "Total: ₹" +
                                currentOrder.getTotalAmount()
                );


                for (
                        OrderDetail detail :
                        currentOrder.getOrderDetails()
                ) {

                    System.out.println(
                            "Product: " +
                                    detail
                                            .getProduct()
                                            .getName()
                    );

                    System.out.println(
                            "Quantity: " +
                                    detail.getQuantity()
                    );

                    System.out.println(
                            "Unit Price: ₹" +
                                    detail.getUnitPrice()
                    );

                    System.out.println(
                            "----------------------------"
                    );
                }
            }


            // ==================================================
            // 9. UPDATE PRODUCT STOCK
            // ==================================================

            transaction =
                    session.beginTransaction();

            Product product =
                    session.find(
                            Product.class,
                            laptop.getId()
                    );

            if (product != null) {

                product.setStockQuantity(8);

                System.out.println(
                        "Laptop stock updated to 8."
                );
            }

            transaction.commit();


            // ==================================================
            // 10. PASSWORD VERIFICATION
            // ==================================================

            boolean passwordCorrect =
                    PasswordUtil.checkPassword(
                            "Admin@123",
                            user.getPassword()
                    );

            System.out.println(
                    "Password verification: " +
                            passwordCorrect
            );


        } catch (Exception e) {

            if (transaction != null) {

                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            e.printStackTrace();

        } finally {

            if (session != null) {
                session.close();
            }

            HibernateUtil.shutdown();
        }
    }
}