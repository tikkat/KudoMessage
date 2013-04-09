package se.kudomessage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.browser.Browser;

public class BrowserFrame {

	protected Shell shlKudomessageErland;
	private Browser browser;
	
	public Browser getBrowser() {
		return browser;
	}

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlKudomessageErland.open();
		shlKudomessageErland.layout();
		while (!shlKudomessageErland.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlKudomessageErland = new Shell();
		shlKudomessageErland.setSize(450, 302);
		shlKudomessageErland.setText("KudoMessage - Erland");
		
		browser = new Browser(shlKudomessageErland, SWT.WEBKIT);
		GroupLayout gl_shlKudomessageErland = new GroupLayout(shlKudomessageErland);
		gl_shlKudomessageErland.setHorizontalGroup(
			gl_shlKudomessageErland.createParallelGroup(GroupLayout.LEADING)
				.add(GroupLayout.TRAILING, browser, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
		);
		gl_shlKudomessageErland.setVerticalGroup(
			gl_shlKudomessageErland.createParallelGroup(GroupLayout.LEADING)
				.add(browser, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
		);
		shlKudomessageErland.setLayout(gl_shlKudomessageErland);
	}
}
