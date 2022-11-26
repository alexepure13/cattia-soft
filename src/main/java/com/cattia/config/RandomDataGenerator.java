package com.cattia.config;

import com.cattia.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.cattia.model.UserAccount;
import com.cattia.model.UserRole;

public class RandomDataGenerator {

    private static Random rand = new Random();

    private static final List<String> names = List.of(
            "Sarahi", "Parker", "Jaycee", "Kramer", "Karissa", "Blake", "Cherish", "Hansen", "Alessandra", "Copeland",
            "Jenny", "Jensen", "Everett", "Shaffer", "Londyn", "Hayes", "Nathalia", "Shields", "Hazel", "Graves",
            "Steven", "Beard", "Juan", "Maxwell", "Max", "Ryker", "Carlos", "Emmanuel");

    public static String getName() {
        return names.get(rand.nextInt(names.size()));
    }

    public static String identificationNumber() {
        return "ID" + getRandomIntInRage(10000, 99999);
    }

    public static int getRandomIntInRage(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    protected static List<Refugee> getAllRefugees() {
        List<Refugee> refugeeList = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            Refugee refugee = Refugee.builder()
                    .id((long) i)
                    .active(1)
                    .createdOn(LocalDateTime.now().minus(getRandomIntInRage(100, 5000000), ChronoUnit.MINUTES))
                    .identificationNumber(RandomDataGenerator.identificationNumber())
                    .firstName(RandomDataGenerator.getName())
                    .lastName(RandomDataGenerator.getName())
                    .build();
            refugeeList.add(refugee);
        }
        return refugeeList;
    }

    public static List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Corn Flower", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Wheat Flower", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Spagetti", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Sunflower Oil", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Sugar", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Oat flakes", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Semolina", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Musli", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Coffee", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "White beans", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Chocolate", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Wafer", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Biscuits", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Cannes fruits", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Lentils", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Chickpeas", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Milk formula", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Pampers", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Food for babies", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Children juice", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Rice", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Canned chicken", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Canned beef", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Canned pork", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Canned fish", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Chicken pate", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Pork pate", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Canned green peas", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Canned bean with sausages", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Canned white beans", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Canned chickpeas", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Canned corn", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERISHABLE, "Mashed vegetables", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Jam", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Tea", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.UNPERISHABLE, "Instant soup", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Absorbers", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Toilet paper", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Wet napkins", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Toothbrush", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Shower gel", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Toothpaste", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "dish sponge", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Dish detergent", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Clothes detergent", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        productList.add(createProduct(getRandomCode(), ProductCategoryEnum.PERSONAL_HYGENE, "Cleaning products", UnitOfMeasurementEnum.PCS, new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250))));
        return productList;
    }

    private static Product createProduct(String randomCode, ProductCategoryEnum category, String name, UnitOfMeasurementEnum um, BigDecimal quantity) {
        return Product.builder()
                .active(1)
                .createdOn(LocalDateTime.now())
                .userAccount(UserAccount.builder().id(1L).build())
                .code(randomCode)
                .name(name)
                .unitOfMeasurement(um)
                .category(category)
                .build();
    }

    private static String getRandomCode() {
        return String.valueOf(RandomDataGenerator.getRandomIntInRage(1, RandomDataGenerator.getRandomIntInRage(100000, 999999)));
    }

    public static List<StockHistory> getAllStockHistory(List<Product> productList) {
        return productList.stream()
                .map(product -> StockHistory.builder()
                        .userAccount(UserAccount.builder().id(1L).build())
                        .createdOn(LocalDateTime.now())
                        .product(product)
                        .quantity(new BigDecimal(RandomDataGenerator.getRandomIntInRage(1, 250)))
                        .action(StockHistoryActionEnum.ADD)
                        .build())
                .limit(15)
                .collect(Collectors.toList());
    }


    public static List<UserAccount> getUserAccounts() {
        return List.of(
                getUserAccount("admin1", 1L),
                getUserAccount("manager1", 2L),
                getUserAccount("operator1", 3L)
        );
    }

    private static UserAccount getUserAccount(String username, Long roleId) {
        return UserAccount.builder()
                .username(username)
                .password("$2a$10$LNSLvfBQ7fTVzK06BE2i1e.9bsGNs3mB2VuY27WcQ9DVIHXdZJfje") //admin
                .email(username + "@" + username + ".com")
                .firstName(username + "X")
                .lastName(username + "Y")
                .role(UserRole.builder().id(roleId).build())
                .active(1)
                .build();
    }

}
