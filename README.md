# leap-sprint2-lab13

| Component | Issue | Fix | Priority |
|---|---|---|---:|
| BatchPayoutJob | A10 - Mishandling of Exceptional Conditions. Payouts are logged as paid when failing. | Changed the set status of the payment to a new "failed" status instead of "paid", so we can redo the payment later. | 5 |
| MerchantController | A01 - Access Control Failure. Users can retrieve payment information of a transaction through URLs. | Add a check so only a logged in user with the same ID can access the information of an account. | 8 |
| Pom.xml | A02 - Security Misconfiguration. The version of the apache logger is old and has a lot of vulnerabilities. Needs an update. | Update the version of the logger to the latest. | 3 |
| WebhookController | A08 - Software and Data Integrity Failures. Webhook endpoints accept payment status updates without verifying the request source, allowing anyone to mark payouts as settled. | Add HMAC-SHA256 signature verification and timestamp validation. | 5 |
| PayoutApprovalService | A06 - Insecure Design. No segregation of duties, user can give pay themselves out. | Added a check that prevents user from approving their own requests of payout. | 6 |
| application.properties | A02 - Security Misconfiguration. All env variables were incorrectly set to be shown whenever requested, rather than only shown to admins. | Changed the user access permissions, so only certain users can see the variables. | 10 |
