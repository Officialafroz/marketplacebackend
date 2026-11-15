# Elevata Marketplace

A modular monolith e-commerce platform built with **Java 25**, **Spring Boot 4.1.1**, and **Spring Modulith 2.1.1**.

The platform provides a multi-vendor marketplace architecture supporting customers, sellers, delivery partners, and administrators. It features passwordless email-OTP authentication, seller product and inventory management, event-driven catalog synchronization, customer shopping carts with single and bulk checkout, default address management, and order lifecycle management (placement, active tracking, cancellation, and automated stock replenishment).

---

## Tech Stack

* **Language & Runtime:** Java 25
* **Framework:** Spring Boot 4.1.1
* **Modularity Engine:** Spring Modulith 2.1.1 (`core`, `jpa`, `observability`, `actuator`, `test`)
* **Security & Auth:** Spring Security, JJWT 0.13.0 (Stateless JWT + Refresh Token Rotation with HttpOnly Cookies)
* **Persistence & Database:** Spring Data JPA, Hibernate (Dynamic Insert, JPA Auditing), MySQL
* **Messaging & Events:** Spring Modulith Application Events & `@ApplicationModuleListener`
* **Email & Notifications:** Spring Boot Starter Mail (JavaMailSender)
* **Validation & Null Safety:** Bean Validation (Hibernate Validator), JSpecify (`@NullMarked`)
* **Testing:** JUnit 5, Spring Modulith Starter Test

---

## Architectural Principles

The application adheres to **Spring Modulith** architectural design patterns:

1. **Encapsulation & Boundary Enforcement:**
   * Each business capability is modeled as an independent domain module inside `com.elevata.marketplace.<module>`.
   * Public APIs (Service interfaces, DTOs, domain event records) live at the module root package.
   * Internal implementation details (JPA entities, repositories, internal service implementations, and web controllers) reside in package-private `domain`, `internal`, or `web` subpackages and are strictly inaccessible to other modules.
2. **Database Autonomy:**
   * Modules do **not** share database foreign key constraints or perform cross-module SQL joins.
   * Cross-domain references use logical identifiers (`productId`, `sellerId`, `catalogId`, `cartId`, `addressId`, `appUserId`).
3. **Decoupled Inter-Module Communication:**
   * **Synchronous:** Direct invocations of public module interfaces (e.g., `OrderService` calling `AddressService` and `CatalogService`).
   * **Asynchronous Events:** Loose coupling via domain events using `@ApplicationModuleListener`:
     * `StockResponse` & `StockUpdate`: Sync inventory batches to customer catalog.
     * `StockUnitsUpdate`: Deduct or restore inventory upon order placement or cancellation.
     * `CartOrderRequest` & `BulkCartOrderRequest`: Event-driven checkout from `carts` to `orders`.
4. **Architectural Verification:**
   * Modularity rules, cycle detection, and dependency boundaries are verified in unit tests via `ApplicationModules.of(MarketplaceApplication.class).verify()`.

---

## Modules Overview

| Module | Type | Responsibility | Key Entities / Records | Dependencies |
| :--- | :--- | :--- | :--- | :--- |
| `commons` | `OPEN` | Shared cross-cutting components, base auditing entity, mail service, and centralized exception handling. | `AuditableEntity`, `EmailService`, `ErrorResponse` | None |
| `validations` | Standard | Custom constraint annotations and validators (e.g., postal zipcodes, email formats). | `@ValidZipcode`, `@ValidEmail` | None |
| `security` | Standard | Stateless JWT authentication, security filter chain, current user resolution, and JPA auditing bridge. | `SecurityConfig`, `JwtProvider`, `CurrentUser`, `SecurityAuditorAware` | `user` |
| `user` | Standard | User identity, profile management, and role authorization (`CUSTOMER`, `SELLER`, `DELIVERY_PARTNER`, `ADMIN`). | `AppUser`, `AppUserService`, `AddAppUser` | `commons`, `validations` |
| `auth` | Standard | Authentication flow, passwordless email OTP verification, token refresh rotation, and cookie sessions. | `Otp`, `RefreshToken`, `AuthService` | `user`, `security`, `commons`, `validations` |
| `product` | Standard | Seller product definitions, catalog metadata, and status lifecycle (`AVAILABLE`, `UNAVAILABLE`). | `Product`, `ProductService`, `ProductResponse` | `commons`, `security` |
| `stock` | Standard | Seller inventory and batch management, price tracking, manufacturing/expiry dates, stock status, and unit adjustment listener. | `Stock`, `StockService`, `StockResponse`, `StockUpdate`, `StockUnitsUpdate` | `commons` |
| `catalogs` | Standard | Aggregated customer-facing storefront catalog; automatically synchronized from inventory events. | `Catalog`, `CatalogService`, `CatalogResponse`, `CatalogOrderResponse` | `product`, `stock`, `commons` |
| `carts` | Standard | Customer shopping cart management, stock limit checks, soft removals, and event-driven checkout (single item or bulk). | `Cart`, `CartService`, `CartRequest`, `CartResponse`, `CartOrderRequest`, `BulkCartOrderRequest` | `catalogs`, `stock`, `commons`, `security` |
| `address` | Standard | Customer shipping address book with default address resolution, country limits, and Indian postal code validation. | `Address`, `AddressService`, `AddressResponse`, `AddressUpdateRequest` | `commons`, `validations`, `security` |
| `orders` | Standard | Order placement, cancellation, active orders retrieval, and asynchronous cart-to-order checkout listener. | `Order`, `OrderService`, `OrderRequest`, `OrderResponse` | `catalogs`, `address`, `carts`, `stock`, `commons`, `security` |
| `test` | Standard | Application healthcheck and connectivity verification endpoint. | `TestController` (`GET /`) | None |

---

## Package Structure

```text
com.elevata.marketplace
├── MarketplaceApplication.java            # Main entrypoint (@SpringBootApplication)
│
├── commons/                               # [OPEN Module] Cross-cutting utilities
│   ├── AuditableEntity.java               # JPA MappedSuperclass for entity auditing
│   ├── EmailService.java                  # Transactional email sender interface
│   ├── ErrorResponse.java                 # Standard REST error format
│   ├── exceptions/                        # Global ControllerAdvice & exception handlers
│   └── internal/                          # EmailServiceImpl
│
├── validations/                           # Custom bean validation constraints
│   ├── ValidEmail.java / ValidZipcode.java
│   └── validators/                        # EmailValidator, ZipcodeValidator
│
├── security/                              # Stateless authentication & authorization
│   ├── CurrentUser.java                   # Context helper to fetch authenticated appUserId
│   ├── JwtProvider.java                   # Access token generator and parser
│   ├── config/SecurityConfig.java         # SecurityFilterChain & endpoint RBAC rules
│   └── internal/                          # JwtAuthenticationFilter, SecurityAuditorAware
│
├── user/                                  # User accounts and profiles
│   ├── AppUserService.java                # Public user management API
│   ├── AppUserRole.java                   # CUSTOMER, SELLER, DELIVERY_PARTNER, ADMIN
│   ├── eventhandlers/                     # Listens to AddAppUser asynchronously
│   └── domain/                            # AppUser entity, AppUserRepository, AppUserServiceImpl
│
├── auth/                                  # Authentication & session orchestration
│   ├── AuthService.java                   # Public login, register, verify, refresh API
│   ├── domain/                            # Otp, RefreshToken, CookieService, OtpService
│   └── web/controller/                    # AuthController (/auth/**), LogoutController (/api/logout)
│
├── product/                               # Seller product definitions
│   ├── ProductService.java                # Public product API
│   ├── ProductResponse.java               # Public product DTO
│   ├── domain/                            # Product entity, ProductRepository, ProductServiceImpl
│   └── web/controller/ProductController   # /api/seller/product/**
│
├── stock/                                 # Seller stock & inventory tracking
│   ├── StockService.java                  # Public stock API
│   ├── StockResponse.java                 # Public stock event & response DTO
│   ├── StockUpdate.java                   # Stock update event record
│   ├── StockUnitsUpdate.java              # Event for remaining units adjustment
│   ├── eventhander/StockEventHandler      # @ApplicationModuleListener for StockUnitsUpdate
│   ├── domain/                            # Stock entity, StockRepository, StockServiceImpl
│   └── web/controller/StockController     # /api/seller/stock/**
│
├── catalogs/                              # Customer product storefront
│   ├── CatalogService.java                # Public search, browsing, and order info API
│   ├── CatalogResponse.java               # Public catalog DTO
│   ├── CatalogOrderResponse.java          # Public catalog order DTO
│   ├── eventhandlers/CatalogEventHandler  # @ApplicationModuleListener for Stock & StockUnitsUpdate events
│   ├── domain/                            # Catalog entity, CatalogRepository, CatalogServiceImpl
│   └── web/controller/CatalogController   # /api/customer/catalogs/**
│
├── carts/                                 # Customer shopping cart management
│   ├── CartService.java                   # Public cart management API
│   ├── CartRequest.java                   # Add-to-cart request DTO
│   ├── CartResponse.java                  # Cart item response DTO
│   ├── CartOrderRequest.java              # Domain event for single-cart checkout
│   ├── BulkCartOrderRequest.java          # Domain event for bulk cart checkout
│   ├── domain/                            # Cart entity, CartRepository, CartServiceImpl, CartStatus
│   └── web/controller/CartController      # /api/customer/cart/**
│
├── address/                               # Customer delivery address book
│   ├── AddressService.java                # Public address API (CRUD & default address lookup)
│   ├── AddressResponse.java               # Public address DTO
│   ├── domain/                            # Address entity, AddressRepository, AddressServiceImpl
│   └── web/controller/AddressController   # /api/customer/address/**
│
├── orders/                                # Customer checkout & order management
│   ├── OrderService.java                  # Public order API (save, cancel, cart checkout)
│   ├── OrderResponse.java                 # Public order DTO
│   ├── eventhandler/OrderEventHandler     # @ApplicationModuleListener for CartOrderRequest & BulkCartOrderRequest
│   ├── domain/                            # Order entity, OrderRepository, OrderServiceImpl, OrderStatus
│   └── web/controller/OrderController     # /api/customer/order/**
│
└── test/                                  # Health probe
    └── web/controller/TestController      # GET / -> "Application is running"
```

---

## Inter-Module Workflows

### 1. Passwordless Authentication & Session Management
```text
Client                Auth Module              User Module            Email Service
  │                        │                        │                       │
  │── POST /auth/login ───>│                        │                       │
  │   (email)              │── validateUser() ─────>│                       │
  │                        │── create OTP ──────────┼──────────────────────>│ (Send OTP email)
  │<── "Otp generated" ────│                        │                       │
  │                        │                        │                       │
  │── POST /auth/verify ──>│                        │                       │
  │   (email, otp)         │── verifyOtp()          │                       │
  │                        │── findByEmail() ──────>│                       │
  │                        │── generateTokens()     │                       │
  │<── Set-Cookie (Tokens)─│                        │                       │
```
* **Registration:** `POST /auth/register` creates the user in `user` module and issues credentials.
* **Tokens:** Issues short-lived JWT access tokens and persistent refresh tokens delivered via secure HttpOnly cookies with automatic token rotation on `POST /auth/refresh`.

### 2. Event-Driven Inventory & Catalog Synchronization
```text
Seller                  Stock Module                 Catalogs Module
  │                          │                              │
  │── POST /api/seller/stock>│                              │
  │   (productId, units...)  │── (Saves Stock)              │
  │                          │── publish(StockResponse) ───>│ (Async @ApplicationModuleListener)
  │                          │                              │── Fetch product details
  │                          │                              │── Upsert Catalog record
  │                          │                              │
  │── PATCH /api/seller/stock│                              │
  │   (units / status)       │── (Updates Stock)            │
  │                          │── publish(StockUpdate) ─────>│ (Async @ApplicationModuleListener)
  │                          │                              │── Update Catalog units/status
```
* The `catalogs` module maintains a read-optimized representation of products currently in stock without requiring database-level joins across the `stock` and `product` tables.

### 3. Shopping Cart & Event-Driven Cart-to-Order Checkout
```text
Customer              Carts Module            Orders Module          Address / Stock / Catalogs
  │                        │                        │                            │
  │── POST /cart ─────────>│                        │                            │
  │   (catalogId, qty)     │── (Validates Stock)    │                            │
  │                        │── (Saves Cart: CART)   │                            │
  │                        │                        │                            │
  │── POST /cart/purchase─>│                        │                            │
  │   (cartId or all)      │── publish(CartOrder) ─>│ (@ApplicationModuleListener)
  │                        │   (or BulkCartOrder)   │── fetchDefaultAddress() ──>│ (Address)
  │                        │                        │── Save Order (PLACED)      │
  │                        │                        │── publish(StockUnitsUpdate)│
  │                        │                        │           │───────────────>│ (Stock & Catalogs)
  │                        │                        │                            │ (Deduct units)
  │                        │── (Updates: ORDERED)   │                            │
  │<── "Order sent" ───────│                        │                            │
```
* **Single & Bulk Checkout:** Customers can purchase an individual cart item (`POST /api/customer/cart/purchase/{cartId}`) or purchase all active cart items at once (`POST /api/customer/cart/purchase/all`).
* **Decoupled Order Creation:** The `carts` module publishes `CartOrderRequest` or `BulkCartOrderRequest`. The `orders` module listens via `@ApplicationModuleListener`, resolves the customer's default delivery address via `AddressService`, records the orders as `PLACED` with `CASH_ON_DELIVERY`, and emits a `StockUnitsUpdate` event.

### 4. Direct Order Placement, Cancellation & Stock Replenishment
```text
Customer                Orders Module                Catalogs / Address / Stock
  │                           │                                  │
  │── POST /order ───────────>│                                  │
  │   (catalogId, addressId)  │── Validates stock & address ────>│
  │                           │── Save Order (PLACED)            │
  │                           │── publish(StockUnitsUpdate) ────>│ (Stock & Catalogs deduct units)
  │<── OrderResponse ─────────│                                  │
  │                           │                                  │
  │── POST /order/cancel/{id}─>│                                 │
  │                           │── Set OrderStatus: CANCELED      │
  │                           │── publish(StockUnitsUpdate) ────>│ (Stock & Catalogs restore units)
  │<── OrderResponse (CANCELED)                                  │
```
* **Inventory Consistency:** Both direct orders and cart-driven orders emit `StockUnitsUpdate`. Both the `stock` and `catalogs` modules listen asynchronously to update remaining units, keeping inventory consistent.
* **Order Cancellation:** Cancelling an order via `POST /api/customer/order/cancel/{orderId}` transitions status to `CANCELED` and publishes a `StockUnitsUpdate` event with the restored quantity, immediately restocking both inventory tables.

---

## API Endpoints Reference

### Public & Authentication Endpoints
| HTTP Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Public | Application healthcheck (`Application is running`). |
| `POST` | `/auth/register` | Public | Register a new user (`fullName`, `email`, `phoneNumber`, `role`). |
| `POST` | `/auth/login` | Public | Request a 6-digit one-time password (OTP) sent to email. |
| `POST` | `/auth/verify/otp` | Public | Verify OTP; receives access token and refresh token in cookies. |
| `POST` | `/auth/refresh` | Public | Rotate refresh token and issue a new access token via cookie. |
| `GET` | `/api/logout` | Authenticated | Invalidate refresh token and clear session cookies. |

### Seller Endpoints (`SELLER` Authority)
| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/seller/product` | Register a new product (`productName`, `category`). |
| `GET` | `/api/seller/product/page/{pageNumber}` | Retrieve paginated products belonging to the authenticated seller. |
| `PATCH` | `/api/seller/product/{productId}?status=AVAILABLE` | Update product status (`AVAILABLE`, `UNAVAILABLE`). |
| `POST` | `/api/seller/stock` | Add stock inventory batch for a product (emits `StockResponse`). |
| `GET` | `/api/seller/stock/page/{pageNumber}` | Retrieve paginated stock entries. |
| `PATCH` | `/api/seller/stock/{stockId}?units=..&status=..` | Update inventory unit count or status (`AVAILABLE`, `OUT_OF_STOCK`, `EXPIRED`). |

### Customer Endpoints (`CUSTOMER` Authority)

#### Catalog
| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/customer/catalogs/page/{pageNumber}` | Browse active catalog products (paginated). |
| `GET` | `/api/customer/catalogs/search/product-name/page/{pageNumber}?productName=..` | Search catalog items by name. |

#### Shopping Cart (`carts`)
| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/customer/cart` | Add a product to the cart (`catalogId`, `quantity`). Validates quantity against stock. |
| `GET` | `/api/customer/cart/list/active` | Retrieve all active cart items (`CartStatus.CART`) for the authenticated customer. |
| `POST` | `/api/customer/cart/remove/{cartId}` | Remove an item from the cart (sets status to `REMOVED`). |
| `POST` | `/api/customer/cart/purchase/{cartId}` | Purchase a single cart item. Emits `CartOrderRequest` event and marks cart `ORDERED`. |
| `POST` | `/api/customer/cart/purchase/all` | Bulk purchase all active cart items. Emits `BulkCartOrderRequest` event. |

#### Addresses
| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/customer/address` | Add delivery address (Max 5 per user, validates Indian zipcode, supports `isDefault`). |
| `PATCH` | `/api/customer/address/{addressId}` | Update existing address fields via `AddressUpdateRequest`. |

#### Orders
| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/customer/order` | Place an order directly (`catalogId`, `addressId`, `quantity`, `paymentOption`). |
| `GET` | `/api/customer/order/list/active` | Retrieve all active orders (`OrderStatus.PLACED`) for the authenticated customer. |
| `POST` | `/api/customer/order/cancel/{orderId}` | Cancel an order (`CANCELED`) and automatically replenish inventory units. |

---

## Running & Testing Locally

### Prerequisites
* **Java 25** installed and set as active JDK
* **MySQL** running locally or via Docker

### 1. Run Modularity & Unit Tests
Spring Modulith verifies package rules, cyclic dependencies, and generates documentation:
```bash
./mvnw test
```
* **Architecture Verification:** `ModularityTests#verifiesModularStructure` validates module boundary rules.
* **Documentation Generator:** `ModularityTests#createModuleDocumentation` exports C4 PlantUML component diagrams and AsciiDoc documentation into `target/spring-modulith-docs/`.

### 2. Run Application
```bash
./mvnw spring-boot:run
```

### 3. Modulith Actuator Endpoint
Inspect module metadata, runtime interactions, and configuration at runtime:
```
http://localhost:8080/actuator/modulith
```
