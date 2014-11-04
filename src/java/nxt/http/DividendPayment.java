package nxt.http;

import static nxt.http.JSONResponses.INCORRECT_PRICE;
import static nxt.http.JSONResponses.MISSING_PRICE;

import javax.servlet.http.HttpServletRequest;

import nxt.Constants;
import org.json.simple.JSONStreamAware;

import nxt.Account;
import nxt.Asset;
import nxt.Attachment;
import nxt.Nxt;
import nxt.NxtException;

public class DividendPayment extends CreateTransaction {

    static final DividendPayment instance = new DividendPayment();

    private DividendPayment() {
        super(new APITag[] {APITag.AE, APITag.CREATE_TRANSACTION}, "asset", "height", "amountNQTPerQNT");
    }

    @Override
    JSONStreamAware processRequest(final HttpServletRequest request)
            throws NxtException
    {
        final int height = getHeight(request);
        final long amountNQTPerQNT = ParameterParser.getAmountNQTPerQNT(request);
        final Account account = ParameterParser.getSenderAccount(request);
        final Asset asset = ParameterParser.getAsset(request);
        final Attachment attachment = new Attachment.ColoredCoinsDividendPayment(asset.getId(), height, amountNQTPerQNT);
        return this.createTransaction(request, account, attachment);
    }

    private static int getHeight(final HttpServletRequest request) throws ParameterException {
        final int height = ParameterParser.getHeight(request);
        if (height < Nxt.getBlockchain().getHeight() - Constants.MAX_ROLLBACK) {
            throw new ParameterException(JSONResponses.HEIGHT_NOT_AVAILABLE);
        }
        return height;
    }
}
