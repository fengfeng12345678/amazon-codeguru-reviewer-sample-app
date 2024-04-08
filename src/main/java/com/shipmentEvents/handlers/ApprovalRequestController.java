package com.sustain.financial.approval;

import com.google.j2objc.annotations.Property.Suppress;
import com.sustain.SustainModule;
import com.sustain.financial.approval.req.ApprovalReq;
import com.sustain.financial.approval.rsp.ApprovalRsp;
import com.sustain.financial.enums.ApprovalRequestStatus;
import com.sustain.financial.helper.ApprovalRequestHelper;
import com.sustain.financial.model.ApprovalRequest;
import com.sustain.financial.model.Receipt;
import com.sustain.screen.*;
import com.sustain.util.Msg;
import com.sustain.util.Task;
import com.sustain.util.WebUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/approval/request")
public class ApprovalRequestController {

    private static final String APPROVAL_REQUEST_ID = "id";
    private static final String APPROVAL_REQ = "approvalReq";
    private static final String APPROVAL_RSP = "approvalRsp";

    @GetMapping()
    @SustainScreen(name = "Approval Request", module = SustainModule.FINANCIAL, description = "Approve refund request",
            type = ScreenType.USER, entityClass = ApprovalRequest.class, inputs = @ScreenInput(name = "approvalRequestId",
            description = "Approval request id", type = ScreenInputType.REQUIRED))
    @SuppressWarnings("UnusedParameters")
    public String doExecute(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) {
        final ApprovalRsp approvalRsp = new ApprovalRsp();
        ApprovalRequestHelper.validateApprovalRequest(request, approvalRsp);
        if (approvalRsp.getErrors().isEmpty()) {
            final ApprovalRequest approvalRequest = ApprovalRequest.get(WebUtils.getId(request, APPROVAL_REQUEST_ID));
            approvalRsp.populate(approvalRequest);
            ApprovalRequestHelper.passRequestParamsToResponse(approvalRequest, request);
            addMessageForUserEditAbility(approvalRsp);
        }
        WebUtils.setJsonAttribute(request, APPROVAL_RSP, approvalRsp);
        return ".approval.request";
    }

    @PostMapping(value = "/onApprove")
    @SustainNonScreen
    @SuppressWarnings("UnusedParameters")
    public String onApprove(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) {
        final ApprovalReq approvalReq = extractReq(request);
        final ApprovalRsp approvalRsp = ApprovalRequestHelper.approve(request, approvalReq);
        final ApprovalRequest approvalRequest = approvalReq.getEntity();
        if (approvalRequest.isApprovedStatus()) {
            approvalRsp.getMessages().add(Msg.get("Refund request is approved.", "refund.request.is.approved"));
        }
        return WebUtils.writeJson2(response, approvalRsp);
    }

    @PostMapping(value = "/onComplete")
    @SustainNonScreen
    @SuppressWarnings("UnusedParameters")
    public String onComplete(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) {
        final ApprovalReq approvalReq = extractReq(request);
        final ApprovalRsp approvalRsp = new ApprovalRsp();
        ApprovalRequestHelper.validateApprovalRequest(approvalReq, approvalRsp);
        if (approvalRsp.getErrors().isEmpty()) {
            final ApprovalRequest approvalRequest = approvalReq.getEntity();
            final Receipt receipt = WebUtils.runAndCatchRsp(new Task<Receipt>() {
                @Override
                public Receipt run() {
                    ApprovalRequestHelper.preventMultiUpdatesByCurrentUser(approvalRequest);
                    return approvalRequest.createVoucher(approvalReq.getProcessStatus());
                }
            }, request, approvalRsp);
            if (receipt != null) {
                approvalRsp.populate(approvalRequest);
                approvalRsp.getMessages().add(Msg.get("refund.request.is.completed"));
            }
        }
        return WebUtils.writeJson2(response, approvalRsp);
    }

    @PostMapping(value = "/onDeny")
    @SustainNonScreen
    @SuppressWarnings("UnusedParameters")
    public String onDeny(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) {
        final ApprovalReq approvalReq = extractReq(request);
        final ApprovalRsp approvalRsp = ApprovalRequestHelper.updateAndDenyOrCancel(request, approvalReq, ApprovalRequestStatus.REJECTED);
        final ApprovalRequest approvalRequest = approvalReq.getEntity();
        if (approvalRequest.isRejectedStatus()) {
            approvalRsp.getMessages().add(Msg.get("refund.request.is.denied"));
        }
        return WebUtils.writeJson2(response, approvalRsp);
    }

    @PostMapping(value = "/onCancel")
    @SustainNonScreen
    @SuppressWarnings("UnusedParameters")
    public String onCancel(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) {
        final ApprovalReq approvalReq = extractReq(request);
        final ApprovalRsp approvalRsp = ApprovalRequestHelper.updateAndDenyOrCancel(request, approvalReq, ApprovalRequestStatus.CANCELED);
        final ApprovalRequest approvalRequest = approvalReq.getEntity();
        if (approvalRequest.isCanceledStatus()) {
            approvalRsp.getMessages().add(Msg.get("refund.request.is.canceled"));
        }
        return WebUtils.writeJson2(response, approvalRsp);
    }

    @PostMapping(value = "/onReset")
    @SustainNonScreen
    @SuppressWarnings("UnusedParameters")
    public String onReset(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) {
        final ApprovalReq approvalReq = extractReq(request);
        final ApprovalRequest approvalRequest = approvalReq.getEntity();
        final ApprovalRsp approvalRsp = new ApprovalRsp(approvalRequest);
        approvalRsp.getMessages().add(Msg.get("Refund request is reset.", "refund.request.is.reset"));
        return WebUtils.writeJson2(response, approvalRsp);
    }

    @NotNull
    private ApprovalReq extractReq(@NotNull final HttpServletRequest request) {
        return WebUtils.fromJson(request, APPROVAL_REQ, ApprovalReq.class);
    }

    private void addMessageForUserEditAbility(@NotNull final ApprovalRsp approvalRsp) {
        if (!approvalRsp.isEditAllowed()) {
            final ApprovalRequest approvalRequest = approvalRsp.getEntity();
            final String editByDesc = approvalRequest.getDescriptionOfEditByCurrentUser();
            approvalRsp.getMessages().add(Msg.get(editByDesc + " Now you are not allowed to modify this request.", "refund.request.edit.is.not.allowed", editByDesc));
        }
    }
}