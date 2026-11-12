package com.elevata.marketplace.orders.domain;

public enum OrderStatus {
    CART,
    PLACED,
    PROCESSING,
    SHIPPED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    DELIVERY_ATTEMPT_FAILED,
    CANCELED,
    RETURN_REQUESTED,
    RETURNED,
    DISPUTED

    /*
        Shipped or Dispatched: The carrier has scanned the package, and tracking information is sent to the customer.
        In Transit: The package is moving through the shipping carrier's distribution network.
        Out for Delivery: A local courier has the package on a delivery vehicle for final drop-off.
        Delivered or Completed: The package has reached the customer's address, or a digital product has been delivered.
        
        Delivery Attempt Failed: The carrier could not drop off the package due to an inaccessible address or missing
            signature.
        Canceled: The order was voided by the customer or store admin before shipment, and inventory is restocked.
        Return Requested or Return In Transit: The customer has initiated a return merchandise authorization (RMA) and
            is sending the item back.
        Returned: The warehouse has received and inspected the returned goods.
        Refunded or Partially Refunded: The store has issued a full or partial reimbursement back to the customer's
            original payment method.
        Disputed: The customer has filed a chargeback or payment dispute with their bank or card issuer.
     */

}
