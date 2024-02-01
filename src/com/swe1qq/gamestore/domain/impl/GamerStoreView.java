package com.swe1qq.gamestore.domain.impl;

import com.swe1qq.gamestore.appui.Renderable;
import com.swe1qq.gamestore.domain.contract.OrderService;
import com.swe1qq.gamestore.domain.contract.PaymentService;
import com.swe1qq.gamestore.domain.contract.ProductService;
import com.swe1qq.gamestore.domain.exception.AuthException;
import com.swe1qq.gamestore.persistence.entity.impl.Order;
import com.swe1qq.gamestore.persistence.entity.impl.Payment;
import com.swe1qq.gamestore.persistence.entity.impl.Product;
import com.swe1qq.gamestore.persistence.entity.impl.User;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.swe1qq.gamestore.domain.impl.GamerStoreView.StoreMenu.EXIT;
import static com.swe1qq.gamestore.domain.impl.GamerStoreView.StoreMenu.PRODUCT_ADD;
import static com.swe1qq.gamestore.domain.impl.GamerStoreView.StoreMenu.PRODUCT_LIST;

public class GamerStoreView implements Renderable {

    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private User user;
    private Set<Product> storedProducts;

    public GamerStoreView(ProductService productService, OrderService orderService, PaymentService paymentService) {
        this.productService = productService;
        this.orderService = orderService;
        this.paymentService = paymentService;

        this.storedProducts = new HashSet<>();
    }

    @Override
    public void render() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt();
        PromptBuilder promptBuilder = prompt.getPromptBuilder();

        promptBuilder.createListPrompt()
                .name("store-menu")
                .message("Магазин для геймерів")
                .newItem(PRODUCT_LIST.toString()).text(PRODUCT_LIST.getName()).add()
                .newItem(PRODUCT_ADD.toString()).text(PRODUCT_ADD.getName()).add()
                .newItem(EXIT.toString()).text(EXIT.getName()).add()
                .addPrompt();

        var result = prompt.prompt(promptBuilder.build());
        ListResult resultItem = (ListResult) result.get("store-menu");

        StoreMenu selectedItem = StoreMenu.valueOf(resultItem.getSelectedId());
        processShopMenu(selectedItem);
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void processShopMenu(StoreMenu selectedItem) {
        ConsolePrompt prompt = new ConsolePrompt();
        PromptBuilder promptBuilder = prompt.getPromptBuilder();

        switch (selectedItem) {
            case PRODUCT_LIST -> {
                ListPromptBuilder productListPrompt = promptBuilder.createListPrompt()
                        .name("product-list")
                        .message("Список товарів");

                Set<Product> products = productService.getAll();

                products.forEach(product ->
                    productListPrompt.newItem(product.getName()).text(product.getName() + " - " + product.getPrice() + "$").add()
                );

                productListPrompt.newItem(OrderMenu.ORDER.toString()).text("Переглянути замовлення").add();
                productListPrompt.newItem(OrderMenu.BACK.toString()).text(OrderMenu.BACK.getName()).add();

                productListPrompt.addPrompt();

                try {

                    var result = prompt.prompt(promptBuilder.build());
                    ListResult resultSelectedProduct = (ListResult) result.get("product-list");

                    String selectedProduct = resultSelectedProduct.getSelectedId();
                    processProductList(selectedProduct);

                } catch (AuthException e) {
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            case PRODUCT_ADD -> {

                if (user.getRole().equals(User.Role.ADMIN)) {
                    promptBuilder.createInputPrompt()
                            .name("type")
                            .message("Впишіть тип товару: ")
                            .addPrompt();
                    promptBuilder.createInputPrompt()
                            .name("name")
                            .message("Впишіть назву товару: ")
                            .addPrompt();
                    promptBuilder.createInputPrompt()
                            .name("description")
                            .message("Впишіть опис товару: ")
                            .addPrompt();
                    promptBuilder.createInputPrompt()
                            .name("price")
                            .message("Впишіть ціну товару: ")
                            .addPrompt();
                    promptBuilder.createInputPrompt()
                            .name("quantity")
                            .message("Впишіть кількість товару на складі: ")
                            .addPrompt();
                    promptBuilder.createInputPrompt()
                            .name("continue")
                            .message("Додати ще товар?")
                            .addPrompt();

                    try {
                        var result = prompt.prompt(promptBuilder.build());

                        var typeInput = (InputResult) result.get("type");
                        var nameInput = (InputResult) result.get("name");
                        var descriptionInput = (InputResult) result.get("description");
                        var priceInput = (InputResult) result.get("price");
                        var quantityInput = (InputResult) result.get("quantity");
                        var continueInput = (InputResult) result.get("continue");

                        Product productDto = new Product(
                                UUID.randomUUID(),
                                typeInput.getInput(),
                                nameInput.getInput(),
                                descriptionInput.getInput(),
                                Double.parseDouble(priceInput.getInput()),
                                Integer.parseInt(quantityInput.getInput())
                        );

                        productService.add(productDto);

                        boolean shouldContinue = Boolean.parseBoolean(continueInput.getInput());

                        if (shouldContinue) {
                            processShopMenu(PRODUCT_ADD);
                        } else {
                            render();
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("У вас немає прав щоб додавати новий товар.");
                }

            }
            case EXIT -> System.out.close();
            default -> {

            }
        }
    }

    private void processProductList(String selectedItem) throws IOException {
        if (selectedItem.equals(OrderMenu.ORDER.toString())) {
            ConsolePrompt prompt = new ConsolePrompt();
            PromptBuilder promptBuilder = prompt.getPromptBuilder();

            ListPromptBuilder productListPrompt = promptBuilder.createListPrompt()
                    .name("order-list")
                    .message("Ваше замовлення");

            Order order = orderService.findAllByCustomerId(user.getId()).stream()
                    .filter(orderItem -> orderItem.getPayment() == null || !orderItem.getPayment().isPassed())
                    .findFirst()
                    .orElse(null);

            if (order != null) {
                order.getProducts().forEach(product ->
                        productListPrompt.newItem(product.getName()).text(product.getName()).add()
                );
            }

            productListPrompt.newItem(OrderMenu.BACK.toString()).text(OrderMenu.BACK.getName()).add();
            productListPrompt.newItem(OrderMenu.PAY.toString()).text(OrderMenu.PAY.getName()).add();

            productListPrompt.addPrompt();

            try {

                var result = prompt.prompt(promptBuilder.build());
                ListResult resultSelectedProduct = (ListResult) result.get("order-list");

                OrderMenu selectedProduct = OrderMenu.valueOf(resultSelectedProduct.getSelectedId());
                processOrder(selectedProduct, order);

            } catch (AuthException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (OrderMenu.BACK.toString().equals(selectedItem)) {
            render();
        } else {
            ConsolePrompt prompt = new ConsolePrompt();
            PromptBuilder promptBuilder = prompt.getPromptBuilder();

            PromptBuilder productMenuPrompt = promptBuilder.createListPrompt()
                    .name("product-item-menu")
                    .newItem(ProductAddMenu.BUY.toString()).text(ProductAddMenu.BUY.getName()).add()
                    .newItem(ProductAddMenu.EXIT.toString()).text(ProductAddMenu.EXIT.getName()).add()
                    .message(selectedItem)
                    .addPrompt();

            try {
                var result = prompt.prompt(productMenuPrompt.build());
                ListResult resultItem = (ListResult) result.get("product-item-menu");

                ProductAddMenu productItem = ProductAddMenu.valueOf(resultItem.getSelectedId());
                buyProduct(productItem, selectedItem);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processOrder(OrderMenu selectedOrderMenu, Order order) {
        switch (selectedOrderMenu) {
            case BACK:
                processShopMenu(PRODUCT_LIST);
                break;
            case PAY:
                double amount = order.getProducts().stream().mapToDouble(Product::getPrice).sum();

                try {
                    System.out.println("Очікуємо оплати...");
                    Thread.sleep(300);
                    System.out.println("Успішно!");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Payment payment = new Payment(UUID.randomUUID(), user, LocalDateTime.now(), amount, true);
                paymentService.add(payment);

                order.setPayment(payment);
        }
    }

    private void buyProduct(ProductAddMenu productItem, String selectedItem) {
        switch (productItem) {
            case BUY -> {
                Product selectedProduct = productService.getByName(selectedItem);
                if (selectedProduct != null) {
                    storedProducts.add(selectedProduct);
                }

                Order order = new Order(UUID.randomUUID(), user, storedProducts, null);

                orderService.add(order);
                processShopMenu(PRODUCT_LIST);
            }
            case EXIT -> {
                processShopMenu(PRODUCT_LIST);
            }
        }
    }

    enum StoreMenu {
        PRODUCT_LIST("Список товарів"),
        PRODUCT_ADD("Додати продукт"),
        EXIT("Вихід");

        private final String name;

        StoreMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum OrderMenu {
        ORDER("Переглянути замовлення"),
        PAY("Оплатити"),
        BACK("Назад");

        private final String name;

        OrderMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum ProductAddMenu {
        BUY("Купити"),
        EXIT("Назад");

        private final String name;

        ProductAddMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
