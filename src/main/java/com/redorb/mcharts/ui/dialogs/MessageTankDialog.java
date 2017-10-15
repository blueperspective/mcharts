package com.redorb.mcharts.ui.dialogs;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.redorb.commons.ui.CenteredDialog;
import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.ui.MessageTank;

/**
 * Shows all messages contained in a message tank.
 */
@SuppressWarnings("serial")
public class MessageTankDialog extends CenteredDialog {

	private static MessageTankDialog dialog = null;
	
	public static synchronized MessageTankDialog getInstance() {
		
		if (dialog == null) {
			dialog = new MessageTankDialog(null);
		}
		
		return dialog;
	}
	
	/*
	 * Attributes
	 */

	private JLabel jlblMessages;
	private JButton jbutOk;

	/*
	 * Ctors
	 */

	private MessageTankDialog(JFrame owner) {
		super(owner, "", true);
		initComponents();
		initLayout();
	}

	/**
	 * Initializes the components.
	 */
	private void initComponents() {
		
		jlblMessages = new JLabel();
		jbutOk = new JButton();

		/*StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));*/
		/*jlblMessages.setText(sw.toString());*/

		jbutOk.setText(I18n.getMessage("messageTankDialog.jbutOk"));

		jbutOk.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
				dispose();
			}
		});

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * Initializes the layout.
	 */
	private void initLayout() {

		getContentPane().setLayout(new GridBagLayout());
		
		getContentPane().add(jlblMessages, new GBC(0, 0).setAnchor(GBC.WEST));
		getContentPane().add(jbutOk, new GBC(0, 1).setAnchor(GBC.EAST));

		setSize(600, 300);
	}
	
	public void showMessages() {
		
		StringBuilder builder = new StringBuilder();
		
		for (String message : MessageTank.getInstance().messages) {
			builder.append(message);
		}
		
		MessageTank.getInstance().clear();
		
		jlblMessages.setText(builder.toString());
		
		setVisible(true);
	}
}
