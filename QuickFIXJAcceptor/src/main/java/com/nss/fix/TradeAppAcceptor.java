package com.nss.fix;

import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.ExecutionReport;

public class TradeAppAcceptor extends MessageCracker implements Application{

    @Override
    public void onCreate(SessionID sessionID) {

    }

    @Override
    public void onLogon(SessionID sessionID) {

    }

    @Override
    public void onLogout(SessionID sessionID) {

    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {

    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        System.out.println("Admin Message Received (Acceptor) :" + message.toString());
    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {

    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        crack(message, sessionID);
    }

    public void onMessage(quickfix.fix42.NewOrderSingle order, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        System.out.println("###NewOrder Received:" + order.toString());
        System.out.println("###Symbol" + order.getSymbol().toString());
        System.out.println("###Side" + order.getSide().toString());
        System.out.println("###Type" + order.getOrdType().toString());
        System.out.println("###TransactioTime" + order.getTransactTime().toString());

        sendMessageToClient(order, sessionID);
    }

    public void sendMessageToClient(quickfix.fix42.NewOrderSingle order, SessionID sessionID) {
        try {
            OrderQty orderQty = null;
            //orderQty = new OrderQty(56.0);

            ExecutionReport accept = new ExecutionReport(new OrderID("133456"), new ExecID("789"),
                    new ExecTransType(ExecTransType.NEW), new ExecType(ExecType.NEW), new OrdStatus(OrdStatus.NEW), order.getSymbol(), order.getSide(),
                    new LeavesQty(56),new CumQty(0), new AvgPx(0));
            accept.set(order.getClOrdID());
            System.out.println("###Sending Order Acceptance:" + accept.toString() + "sessionID:" + sessionID.toString());
            Session.sendToTarget(accept, sessionID);
        } catch (RuntimeException e) {
            LogUtil.logThrowable(sessionID, e.getMessage(), e);
        } catch (FieldNotFound fieldNotFound) {
            fieldNotFound.printStackTrace();
        } catch (SessionNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
        }
    }


}


