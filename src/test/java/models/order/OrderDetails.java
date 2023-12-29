package models.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class OrderDetails {
    private int id;
    private String access_key;
    private String status;
    private String status_text;
    private String partner_status;
    private String replacement_status;
    private String payment_type;
    private String created_at;
    private String delivery_date;
    private String delivery_time;
    private TotalAmount total;
    private PartnerTotal partner_total;
    private String currency;
    private int account_currency;
    private UserData user;
    private DeliveryData delivery;
    private String customer_name;
    private String customer_phone;
    private String customer_email;
    private String recipient_name;
    private String recipient_address;
    private String recipient_city;
    private String recipient_country;
    private String recipient_region;
    private String recipient_phone;
    private int recipient_address_type;
    private boolean recipient_photo;
    private Map<String, Object> recipient_photo_gallery;
    private String message;
    private String additional;
    private boolean inform;
    //private Map<String, Item> cart;
    private Map<String, CartItem> cart;
    private Map<String, Object> coupon;
    private Map<String, Object> partner_photo;
    private int unread_comments;
    private boolean can_say_name;
    private boolean do_not_say;
    private String max_paid_date;
    private int surprise;
    private boolean is_ecommerce_send;
}
