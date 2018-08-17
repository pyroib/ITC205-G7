public class PayFineControl {
    
    
    private PayFineUi ui;
    private ControlState controlState;
    private Library library;
    private Member member;
    private enum ControlState {
        INITIALISED, READY, PAYING, COMPLETED, CANCELLED
    };
    
    
    @SuppressWarnings("static-access")
    public PayFineControl() {
        this.library = library.INSTANCE();
        controlState = ControlState.INITIALISED;
    }
    
    
    public void setUi(PayFineUi ui) {
        Boolean currentControlStateInitialised = controlState.equals(ControlState.READY);
        if (!currentControlStateInitialised) {
            throw new RuntimeException("PayFineControl: cannot call setUI except in "
                    + "INITIALISED state");
        }
        this.ui = ui;
        ui.setState(PayFineUi.UiState.READY);
        controlState = ControlState.READY;
    }
    
    
    public void cardSwiped(int memberId) {
        Boolean currentControlStateReady = controlState.equals(ControlState.READY);
        if (!currentControlStateReady) {
            throw new RuntimeException("PayFineControl: cannot call cardSwiped except in "
                    + "READY state");
        }
        member = library.getMember(memberId);
        
        if (member == null) {
            ui.printOutput("Invalid Member Id");
            return;
        }
        String memberData = member.toString();
        ui.printOutput(memberData);
        ui.setState(PayFineUi.UiState.PAYING);
        controlState = ControlState.PAYING;
    }
    
    
    public void cancelState() {
        ui.setState(PayFineUi.UiState.CANCELLED);
        controlState = ControlState.CANCELLED;
    }
    
    
    public double payFine(double amount) {
        Boolean currentControlStatePaying = controlState.equals(ControlState.PAYING);
        if (!currentControlStatePaying) {
            throw new RuntimeException("PayFineControl: cannot call payFine except in "
                    + "PAYING state");
        }
        double change = member.payFine(amount);
        if (change > 0) {
            String displayMessage = String.format("Change: $%.2f", change);
            ui.printOutput(displayMessage);
        }
        String memberData = member.toString();
        ui.printOutput(memberData);
        ui.setState(PayFineUi.UiState.COMPLETED);
        controlState = ControlState.COMPLETED;
        return change;
    }
    
    
}
