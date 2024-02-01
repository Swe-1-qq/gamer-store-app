package com.swe1qq.gamestore.domain.impl;

import com.swe1qq.gamestore.domain.contract.AuthService;
import com.swe1qq.gamestore.domain.contract.OrderService;
import com.swe1qq.gamestore.domain.contract.PaymentService;
import com.swe1qq.gamestore.domain.contract.ProductService;
import com.swe1qq.gamestore.domain.contract.SignUpService;
import com.swe1qq.gamestore.domain.contract.UserService;
import com.swe1qq.gamestore.domain.exception.DependencyException;
import com.swe1qq.gamestore.persistence.repository.RepositoryFactory;

public final class ServiceFactory {

    private static volatile ServiceFactory INSTANCE;
    private final AuthService authService;
    private final GamerStoreView gamerStoreView;
    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final SignUpService signUpService;
    private final RepositoryFactory repositoryFactory;

    private ServiceFactory(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        var userRepository = repositoryFactory.getUserRepository();
        authService = new AuthServiceImpl(userRepository);
        productService = new ProductServiceImpl(repositoryFactory.getProductRepository());
        orderService = new OrderServiceImpl(repositoryFactory.getOrderRepository());
        paymentService = new PaymentServiceImpl(repositoryFactory.getPaymentRepository());
        userService = new UserServiceImpl(userRepository);
        gamerStoreView = new GamerStoreView(productService, orderService, paymentService);
        signUpService = new SignUpServiceImpl(userService);
    }

    /**
     * Використовувати, лише якщо впевнені, що існує об'єкт RepositoryFactory.
     *
     * @return екземпляр типу ServiceFactory
     */
    public static ServiceFactory getInstance() {
        if (INSTANCE.repositoryFactory != null) {
            return INSTANCE;
        } else {
            throw new DependencyException(
                    "Ви забули створити обєкт RepositoryFactory, перед використанням ServiceFactory.");
        }
    }

    public static ServiceFactory getInstance(RepositoryFactory repositoryFactory) {
        if (INSTANCE == null) {
            synchronized (ServiceFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceFactory(repositoryFactory);
                }
            }
        }

        return INSTANCE;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public UserService getUserService() {
        return userService;
    }

    public SignUpService getSignUpService() {
        return signUpService;
    }

    public RepositoryFactory getRepositoryFactory() {
        return repositoryFactory;
    }

    public GamerStoreView getGamerStoreView() {
        return gamerStoreView;
    }
}
