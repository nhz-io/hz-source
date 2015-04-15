package nxt.http;

import nxt.NxtException;
import nxt.Transaction;
import nxt.util.Convert;
import nxt.util.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

public final class ParseTransaction extends APIServlet.APIRequestHandler {

    static final ParseTransaction instance = new ParseTransaction();

    private ParseTransaction() {
        super(new APITag[] {APITag.TRANSACTIONS}, "transactionJSON", "transactionBytes", "prunableAttachmentJSON");
    }

    @Override
    JSONStreamAware processRequest(HttpServletRequest req) throws NxtException {

        String transactionBytes = Convert.emptyToNull(req.getParameter("transactionBytes"));
        String transactionJSON = Convert.emptyToNull(req.getParameter("transactionJSON"));
        String prunableAttachmentJSON = Convert.emptyToNull(req.getParameter("prunableAttachmentJSON"));

        Transaction transaction = ParameterParser.parseTransaction(transactionJSON, transactionBytes, prunableAttachmentJSON).build();
        JSONObject response = JSONData.unconfirmedTransaction(transaction);
        try {
            transaction.validate();
        } catch (NxtException.ValidationException|RuntimeException e) {
            Logger.logDebugMessage(e.getMessage(), e);
            response.put("validate", false);
            JSONData.putException(response, e, "Invalid transaction");
        }
        response.put("verify", transaction.verifySignature());
        return response;
    }

}
