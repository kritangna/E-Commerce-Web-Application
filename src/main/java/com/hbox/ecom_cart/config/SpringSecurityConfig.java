package com.hbox.ecom_cart.config;

import com.hbox.ecom_cart.entity.CustomerProfile;
import com.hbox.ecom_cart.entity.User;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.CustomerProfileRespository;
import com.hbox.ecom_cart.repositoty.UserRepository;
import com.hbox.ecom_cart.security.JwtAuthenticationEntryPoint;
import com.hbox.ecom_cart.security.JwtAuthenticationFilter;
import com.hbox.ecom_cart.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Optional;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SpringSecurityConfig {

    private JwtTokenProvider jwtTokenProvider;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private UserRepository userRepository;
    private CustomerProfileRespository customerProfileRespository;

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationEntryPoint authenticationEntryPoint,
                                            CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {

        System.out.println("********** SecurityFilterChain is being initialized **********");
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                {

                    authorizeUsersForAPICalls(authorize);
                    authorizeProductsForAPICalls(authorize);
                    authorizeCategoriesForAPICalls(authorize);
                    authorizeOrdersForAPICalls(authorize);
                    authorizeRazorpayPaymentOrderForAPICalls(authorize);
                    authorizeProfilesForAPICalls(authorize);
                    authorizeOrderHistoryForAPICalls(authorize);
                    authorizeWishListForAPICalls(authorize);
                    authorizeRazorpayPaymentsAPICalls(authorize);
                    authorizeSendEmailsAPICalls(authorize);
                    authorizePaginationAPICalls(authorize);

                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //************* Authorizing Requests for accessing Users ***************//
    private void authorizeUsersForAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
    {
        // POST Request to register/login -> Any Role/Authority
        authorize.requestMatchers(HttpMethod.POST, "api/e-com-cart/users/**").permitAll();

        // GET Request to fetch all the users -> Role_ADMIN
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/users").hasAuthority("ROLE_ADMIN");

        // GET Request Based on Id -> ROLE_ADMIN can get details of any user, ROLE_CUSTOMER can get details of itself
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/users/**").access(this::isAdminOrSelf);

        // PUT Request Based on Id -> Any ROLE can update details of itself only
        authorize.requestMatchers(HttpMethod.PUT, "/api/e-com-cart/users/**").access(this::isSelf);

        // DELETE Request Based on Id -> Any Role can delete itself only
        authorize.requestMatchers(HttpMethod.DELETE, "/api/e-com-cart/users/**").access(this::isSelf);
    }

    //************* Authorizing Requests for accessing Products ***************//
    private void authorizeProductsForAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
    {
        // POST Request to add Products
        authorize.requestMatchers(HttpMethod.POST, "/api/e-com-cart/products").hasAuthority("ROLE_ADMIN");

        // GET Request to get a Product by ID
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/products/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER");

        // GET Request to get all Products
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/products").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER");

        // PUT Request to update th product
        authorize.requestMatchers(HttpMethod.PUT, "/api/e-com-cart/products/**").hasAuthority("ROLE_ADMIN");

        // DELETE Request to delete a product
        authorize.requestMatchers(HttpMethod.DELETE, "/api/e-com-cart/products/**").hasAuthority("ROLE_ADMIN");
    }

    //************* Authorizing Requests for accessing Categories ***************//
    private void authorizeCategoriesForAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
    {
        // POST Request to add Categories
        authorize.requestMatchers(HttpMethod.POST, "/api/e-com-cart/categories").hasAuthority("ROLE_ADMIN");

        // GET Request to get a Categories by ID
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/categories/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER");

        // GET Request to get all Categories
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/categories").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER");

        // PUT Request to update the Category by ID
        authorize.requestMatchers(HttpMethod.PUT, "/api/e-com-cart/categories/**").hasAuthority("ROLE_ADMIN");

        // DELETE Request to delete a Category
        authorize.requestMatchers(HttpMethod.DELETE, "/api/e-com-cart/categories/**").hasAuthority("ROLE_ADMIN");
    }

    //************* Authorizing Requests for accessing Orders ***************//
    private void authorizeOrdersForAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
    {
        // POST Request to place an Order
        authorize.requestMatchers(HttpMethod.POST, "api/e-com-cart/orders/place-order").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER");

        // GET Request to get all placed Orders
        authorize.requestMatchers(HttpMethod.GET, "api/e-com-cart/orders").hasAuthority("ROLE_ADMIN");

        // GET Request to get an Order placed by a Customer/Admin by Order ID
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/orders/**").hasAuthority("ROLE_ADMIN");

        // PUT Request to update Order Status
        authorize.requestMatchers(HttpMethod.PUT, "/api/e-com-cart/orders/**").hasAuthority("ROLE_ADMIN");

        // DELETE Request to delete an Order once it is delivered
        authorize.requestMatchers(HttpMethod.DELETE, "/api/e-com-cart/orders/**").hasAuthority("ROLE_ADMIN");
    }

    //************* Authorizing Requests for accessing Order History ***************//
    private void authorizeOrderHistoryForAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
    {
        authorize.requestMatchers(HttpMethod.POST, "/api/e-com-cart/order-history/**").access(this::isAdminOrSelf);
    }

    //************* Authorizing Requests for accessing Order History ***************//
    private void authorizeWishListForAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
    {
        // POST Request to add products to WishList by a Customer using customer ID
        authorize.requestMatchers(HttpMethod.POST, "/api/e-com-cart/wish-list/**").access(this::isCustomer);

        // GET Request to retrieve the wishlist of a particular customer
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/wish-list/**").access(this::isAdminOrCustomer);
    }

    //************* Authorizing Requests for Creating Order for Razorpay Payments ***************//
    private void authorizeRazorpayPaymentOrderForAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        // POST Request to create payment order for Razorpay payment
        authorize.requestMatchers(HttpMethod.POST, "/api/e-com-cart/payments/razorpay/create-order").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER");
    }

    //************* Authorizing Requests for accessing Customer Profiles ***************//
    private void authorizeProfilesForAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        // POST Request to create a Customer Profile by userId
        authorize.requestMatchers(HttpMethod.POST, "/api/e-com-cart/profiles/**").access(this::isSelf);

        // GET Request to get all the customer profiles
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/profiles").hasAuthority("ROLE_ADMIN");

        // GET Request to get a Customer's profile by userId
        authorize.requestMatchers(HttpMethod.GET, "/api/e-com-cart/profiles/**").access(this::isAdminOrSelf);

        // PUT Request to update a customer's profile by the logged in customer only
        authorize.requestMatchers(HttpMethod.PUT, "/api/e-com-cart/profiles/**").access(this::isSelf);

        // DELETE Request to delete a customer's profile
        authorize.requestMatchers(HttpMethod.DELETE, "/api/e-com-cart/profiles/**").access(this::isAdminOrSelf);
    }

    //************* Authorizing Requests for accessing Razorpay Payments ***************//
    private void authorizeRazorpayPaymentsAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
    {
        authorize.requestMatchers(HttpMethod.POST, "api/e-com-cart/razorpay-payments/webhook").permitAll();
    }

    //************* Authorizing Requests for Sending Mails ***************//
    private void authorizeSendEmailsAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
    {
        // POST Request to send mail to the Customer from the application
        authorize.requestMatchers(HttpMethod.POST, "api/e-com-cart/email/send").hasAuthority("ROLE_ADMIN");
    }

    //************* Authorizing Requests for Pagination ***************//
    private void authorizePaginationAPICalls(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
    {
        // GET Request to access the product list with specified number of products
        authorize.requestMatchers(HttpMethod.GET, "api/e-com-cart/products/product-list").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER");
    }

    // Provide API Request access to both ADMIN and CUSTOMER - based on their Id
    private AuthorizationDecision isAdminOrSelf(Supplier<Authentication> auth, RequestAuthorizationContext request) {
        String token = request.getRequest().getHeader("Authorization");
        System.out.println("Token: " + token);

        if (token == null || !token.startsWith("Bearer")) {
            return new AuthorizationDecision(false);
        }
        token = token.replace("Bearer ", "");
        System.out.println("Token: " + token);

        Authentication authentication = auth.get();
        Long loggedInUserId = jwtTokenProvider.getUserIdFromToken(token);
        String[] uriPath = request.getRequest().getRequestURI().split("/");
        System.out.println("Logged In UserId: " + loggedInUserId);

        if (uriPath.length < 5) {
            return new AuthorizationDecision(false);
        }

        Long requestedUserId;
        try {
            requestedUserId = Long.parseLong(uriPath[4]);
            System.out.println("Requested UserId: " + requestedUserId);
        } catch (NumberFormatException e) {
            return new AuthorizationDecision(false);
        }

        boolean isAuthorized = authentication.getAuthorities().stream().anyMatch(
                authority -> authority.getAuthority().equals("ROLE_ADMIN")
                        || authority.getAuthority().equals("ROLE_CUSTOMER")
                        && loggedInUserId.equals(requestedUserId));

        if (!isAuthorized) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(true);
    }

    // Provide API Request access to individual role - ADMIN or CUSTOMER - based on their Id
    private AuthorizationDecision isSelf(Supplier<Authentication> auth, RequestAuthorizationContext request) {
        String[] uriPath = request.getRequest().getRequestURI().split("/");
        if (uriPath.length < 5) {
            return new AuthorizationDecision(false);
        }
        Long requestedUserId = Long.parseLong(uriPath[4]);

        if (requestedUserId == null) {
            return new AuthorizationDecision(false);
        }

        Long loggedInUserId = getLoggedInUserIdFromToken(request.getRequest().getHeader("Authorization"));
        if (loggedInUserId == null) {
            return new AuthorizationDecision(false);
        }
        boolean isAuthorized = requestedUserId.equals(loggedInUserId);
        return new AuthorizationDecision(isAuthorized);
    }

    private AuthorizationDecision isAdminOrCustomer(Supplier<Authentication> auth, RequestAuthorizationContext request)
    {
        try
        {
            String token = request.getRequest().getHeader("Authorization");
            System.out.println("Token: " + token);

            if (token == null || !token.startsWith("Bearer")) {
                return new AuthorizationDecision(false);
            }
            token = token.replace("Bearer ", "");
            System.out.println("Token: " + token);

            Authentication authentication = auth.get();
            System.out.println("Authentication object: " + authentication);
            System.out.println("Principal: " + authentication.getPrincipal());
            Long loggedInUserId = jwtTokenProvider.getUserIdFromToken(token);
            String[] uriPath = request.getRequest().getRequestURI().split("/");
            System.out.println("Logged In UserId: " + loggedInUserId);

            if (uriPath.length < 5) {
                return new AuthorizationDecision(false);
            }

            Long requestedCustomerId;

            requestedCustomerId = Long.parseLong(uriPath[4]);
            System.out.println("Customer Id: " + requestedCustomerId);

            Optional<CustomerProfile> optionalCustomer = customerProfileRespository.findById(requestedCustomerId);
            if (optionalCustomer.isEmpty()) {
                return new AuthorizationDecision(false);
            }
            CustomerProfile customer = optionalCustomer.get();
            Optional<User> optionalUser = userRepository.findById(customer.getUser().getId());
            if (optionalUser.isEmpty()) {
                return new AuthorizationDecision(false);
            }
            User customerUser = optionalUser.get();

            Long customerUserId = customerUser.getId();
            boolean isAuthorized = authentication.getAuthorities().stream().anyMatch(
                    authority -> authority.getAuthority().equals("ROLE_ADMIN")
                            || (authority.getAuthority().equals("ROLE_CUSTOMER")
                            && loggedInUserId.equals(customerUserId)));

            System.out.println("Is Authorized: " + isAuthorized);
            return new AuthorizationDecision(isAuthorized);
        }
        catch (NumberFormatException e) {
            return new AuthorizationDecision(false);
        }
    }
    private AuthorizationDecision isCustomer(Supplier<Authentication> auth, RequestAuthorizationContext request)
    {
        try
        {
            String[] uriPath = request.getRequest().getRequestURI().split("/");
            if (uriPath.length < 5) {
                return new AuthorizationDecision(false);
            }
            Long customerId = Long.parseLong(uriPath[4]);
            System.out.println("Customer Id: " + customerId);
            if (customerId == null) {
                return new AuthorizationDecision(false);
            }
            CustomerProfile customer = customerProfileRespository.findById(customerId).orElseThrow(() -> new EcomCartException(HttpStatus.NOT_FOUND, "Customer Not Found"));
            if(customer == null)
            {
                return new AuthorizationDecision(false);
            }
            User user = userRepository.findById(customer.getUser().getId()).orElseThrow(() -> new EcomCartException(HttpStatus.NOT_FOUND, "User Not Found"));
            if(user == null)
            {
                return new AuthorizationDecision(false);
            }
            Long customerUserId = user.getId();

            Long loggedInUserId = getLoggedInUserIdFromToken(request.getRequest().getHeader("Authorization"));
            if (loggedInUserId == null) {
                return new AuthorizationDecision(false);
            }
            boolean isAuthorized = customerUserId.equals(loggedInUserId);
            return new AuthorizationDecision(isAuthorized);
        }
        catch (Exception e)
        {
            return new AuthorizationDecision(false);
        }
    }


    private Long getLoggedInUserIdFromToken(String authorizationHeader)
    {
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
        {
            throw  new EcomCartException(HttpStatus.UNAUTHORIZED, "Invalid Authorization");
        }
        String token = authorizationHeader.replace("Bearer ", "");

        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
