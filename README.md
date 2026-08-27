# leap-sprint2-lab13

BatchPayoutJob | A10 - Mishandling of Exceptional Conditions. Payouts are logged as paid when failing | 5
MerchantController | A01 - Access Control Failure. Users can retrieve payment information of a transaction through URLs. | 8
Pom.xml | A02 - Security Misconfiguration. The version of the apache logger is old, and has a lot of vulnerabilities. Needs an update. | 3

WebhookController | A08 - Software and Data Integrity Failures. Webhook endpoints accept payment status updates without verifying the request source, allowing anyone to mark payouts as settled. 
Fix: Add HMAC-SHA256 signature verification and timestamp validation. | 5

PayoutApprovalService | A06 - Insecure Design. No segregation of duties, user can give pay themselves out. 
Fix: Added a check that prevents user from approving their own requests of payout. | 6
