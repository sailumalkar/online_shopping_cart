

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Product> productList = new ArrayList<>();
    private static List<Product> cart = new ArrayList<>();

    public static void main(String[] args) {
        // Load products from database into productList
        loadProductsFromDB();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                // Display menu
                System.out.println("\n=== Online Shopping Cart Application ===");
                System.out.println("1. View Products");
                System.out.println("2. Add Product to Cart");
                System.out.println("3. Remove Product from Cart");
                System.out.println("4. View Cart");
                System.out.println("5. Calculate Total");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewProducts();
                        break;

                    case 2:
                        addProduct(scanner);
                        break;

                    case 3:
                        removeProduct(scanner);
                        break;

                    case 4:
                        viewCart();
                        break;

                    case 5:
                        calculateTotal();
                        break;

                    case 6:
                        System.out.println("Thank you for shopping!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear scanner buffer
            }
        }
    }

    private static void loadProductsFromDB() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products");
             ResultSet rs = stmt.executeQuery()) {
            productList.clear();
            while (rs.next()) {
                productList.add(new Product(
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("product_price")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    private static void viewProducts() {
        if (productList.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("Available Products:");
            for (Product product : productList) {
                System.out.println(product);
            }
        }
    }

    private static void addProduct(Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to add: ");
        String productId = scanner.nextLine();
        Product productToAdd = null;
        for (Product product : productList) {
            if (product.getProductId().equalsIgnoreCase(productId)) {
                productToAdd = product;
                break;
            }
        }
        if (productToAdd != null) {
            cart.add(productToAdd);
            addToCartDB(productToAdd.getProductId()); // Use the actual product ID from the database
            System.out.println(productToAdd.getProductName() + " added to cart.");
        } else {
            System.out.println("Product ID not found. Here are the available products:");
            viewProducts();
        }
    }

    private static void removeProduct(Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to remove: ");
        String productId = scanner.nextLine();
        Product productToRemove = null;
        for (Product product : cart) {
            if (product.getProductId().equalsIgnoreCase(productId)) {
                productToRemove = product;
                break;
            }
        }
        if (productToRemove != null) {
            cart.remove(productToRemove);
            removeFromCartDB(productToRemove.getProductId()); // Use the actual product ID from the database
            System.out.println("Product with ID " + productToRemove.getProductId() + " removed from cart.");
        } else {
            System.out.println("Product with ID " + productId + " not found in cart.");
        }
    }

    private static void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            System.out.println("Cart Contents:");
            for (Product product : cart) {
                System.out.println(product);
            }
        }
    }

    private static void calculateTotal() {
        double cartTotal = 0.0;
        for (Product product : cart) {
            cartTotal += product.getProductPrice();
        }
        System.out.printf("Total Cart Value: $%.2f\n", cartTotal);
    }

    private static void addToCartDB(String productId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO cart_items (product_id) VALUES (?)")) {
            stmt.setString(1, productId);
            stmt.executeUpdate();
        }
    }

    private static void removeFromCartDB(String productId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM cart_items WHERE product_id = ? LIMIT 1")) {
            stmt.setString(1, productId);
            stmt.executeUpdate();
        }
    }
}
