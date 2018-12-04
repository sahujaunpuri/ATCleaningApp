package professional.service.corporate.com.atcleaningapp;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

public class Config {

    // PayPal app configuration
    public static final String PAYPAL_CLIENT_ID = "AWLl2dTBszCUuBeUGFjldUwQPirYHGUF_T2AKvAn2CQF0sxa2eUxXMKkjfYcdJ6tuK71JQlA2IzUI1jO";
    public static final String PAYPAL_CLIENT_SECRET = "ELtbnE0vFA0rLWntV2GMdoD1xNln4JYPG25UtMQX6v4gciFbTS3Anv5joFefapfACnBi6eRXOICOGa1h";

    public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public static final String PAYMENT_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
    public static final String DEFAULT_CURRENCY = "CAD";

    // PayPal server urls
    public static final String URL_PRODUCTS = "http://urzom.com/androidapp/index.php/get_products";
    public static final String URL_VERIFY_PAYMENT = "http://urzom.com/androidapp/index.php/verify_payment";

    public static final String URL_EDITED_PAYMENT = "http://urzom.com/androidapp/index.php/edited_payment";
    public static final String URL_MILESTONES = "http://urzom.com/androidapp/index.php/get_milestones";
    public static final String URL_VERIFY_PAYMENT_STATUS = "http://urzom.com/androidapp/index.php/milestone_pay_status";
    public static final String URL_UNPAID_MILESTONES = "http://urzom.com/androidapp/index.php/unpaid_milestone";
    public static final String URL_PAID_MILESTONES = "http://urzom.com/androidapp/index.php/paid_milestone";
    public static final String URL_UNPAID_MILESTONES_H = "http://urzom.com/androidapp/index.php/unpaid_milestone_h";
    public static final String URL_PAID_MILESTONES_H = "http://urzom.com/androidapp/index.php/paid_milestone_h";
    public static final String URL_RELEASED_MILESTONES = "http://urzom.com/androidapp/index.php/released_milestone";
    public static final String URL_RELEASED_MILESTONES_H = "http://urzom.com/androidapp/index.php/released_milestone_h";
    public static final String URL_CONTRACT_PAY = "http://urzom.com/androidapp/index.php/get_contract_pay";
    public static final String URL_ADD_CONTRACT_PAYMENT = "http://urzom.com/androidapp/index.php/add_contract_pay";
    public static final String URL_VERIFY_CONTRACT_PAYMENT = "http://urzom.com/androidapp/index.php/verify_contract_payment";
    public static final String URL_JOBS_BID_AMOUNT = "http://urzom.com/androidapp/index.php/create_safe_deposit";
    public static final String URL_SEND_PAYOUT =  "http://urzom.com/androidapp/CreateSinglePayout.php/send_payout";
    public static final String URL_GET_PAYEE = "http://urzom.com/androidapp/index.php/get_recepient";
}