public class FixBookControl {

	private FixBookUi ui;

	private enum ControlState {
		INITIALISED, READY, FIXING
	};

	private ControlState state;

	private Library library;
	private book currentBook;

	public FixBookControl() {
		this.library = library.INSTANCE();
		state = ControlState.INITIALISED;
	}

	public void setUi(FixBookUi ui) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
		}
		this.ui = ui;
		ui.setState(FixBookUi.UiState.READY);
		state = ControlState.READY;
	}

	public void bookScanned(int bookId) {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
		}
		currentBook = library.Book(bookId);

		if (currentBook == null) {
			ui.display("Invalid bookId");
			return;
		}
		if (!currentBook.Damaged()) {
			ui.display("\"Book has not been damaged");
			return;
		}
		ui.display(currentBook.toString());
		ui.setState(FixBookUi.UiState.FIXING);
		state = ControlState.FIXING;
	}

	public void fixBook(boolean mustFix) {
		if (!state.equals(ControlState.FIXING)) {
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
		}
		if (mustFix) {
			library.repairBook(currentBook);
		}
		currentBook = null;
		ui.setState(FixBookUi.UiState.READY);
		state = ControlState.READY;
	}

	public void scanningComplete() {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
		}
		ui.setState(FixBookUi.UiState.COMPLETED);
	}

}
