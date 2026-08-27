package com.neueda.leap.merchantportal;

public class PayoutApprovalService {

    private PayoutRepository payoutRepository;

    public PayoutApprovalService(PayoutRepository payoutRepository) {
        this.payoutRepository = payoutRepository;
    }

    public void approve(Long payoutId, Long approvingUserId) {
        PayoutRequest payout = payoutRepository.findById(payoutId)
                .orElseThrow(() -> new RuntimeException("Payout not found"));
// preventing self approval by segregating duties
	if (payout.getRequestedByUserID().equals(approvingUserID)){
		throw new IllegalException(
			"A payout cannot be approved by the same user who requested it");
	}
        payout.setApprovalStatus("APPROVED");
        payout.setApprovedByUserId(approvingUserId);
        payoutRepository.save(payout);
    }
}
