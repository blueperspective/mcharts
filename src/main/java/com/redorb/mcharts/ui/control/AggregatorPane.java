package com.redorb.mcharts.ui.control;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.aggregator.GlobalAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.KindAggregator;
import com.redorb.mcharts.data.restriction.GlobalNFirstRestriction;
import com.redorb.mcharts.data.restriction.IRestriction;
import com.redorb.mcharts.data.restriction.LocalNFirstRestriction;
import com.redorb.mcharts.data.restriction.RestrictionFactory;
import com.redorb.mcharts.ui.MessageTank;
import com.redorb.mcharts.ui.Ui;
import com.redorb.mcharts.ui.dialogs.ListPane;
import com.redorb.mcharts.ui.renderers.MapListRenderer;

/**
 * Panel to build graphically an aggregator.
 */
@SuppressWarnings("serial")
public class AggregatorPane extends JPanel {

	/*
	 * Attributes
	 */

	private JLabel lblDimensions;
	private JCheckBox chkSeparateInOut;
	private JList<Dimension> lstDimensions;
	private JScrollPane scrolDimensions;
	private ListPane<Dimension> listPane;

	private DefaultListModel<Dimension> aggregateObjectModel = new DefaultListModel<>();

	private JCheckBox chkRestriction;
	private JComboBox<Class> cmbRestriction;
	private JLabel lblParameter;
	private JTextField txtParameter;

	/*
	 * Ctor
	 */

	public AggregatorPane() {
		initComponents();
		initLayout();
	}

	/*
	 * Internal methods
	 */

	private void initComponents() {


		lblDimensions = new JLabel(I18n.getMessage("aggregatorPane.lblDimensions"));
		
		// dimensions
		
		lstDimensions = new JList<Dimension>();
		scrolDimensions = new JScrollPane();
		scrolDimensions.setViewportView(lstDimensions);
		
		chkSeparateInOut = new JCheckBox(I18n.getMessage("aggregatorPane.separateInOut"));

		// aggregation panel
		
		aggregateObjectModel.addElement(Dimension.ACCOUNT);
		aggregateObjectModel.addElement(Dimension.CATEGORY);
		aggregateObjectModel.addElement(Dimension.SUB_CATEGORY);
		aggregateObjectModel.addElement(Dimension.PAYEE);
		aggregateObjectModel.addElement(Dimension.WEEK);
		aggregateObjectModel.addElement(Dimension.MONTH);
		aggregateObjectModel.addElement(Dimension.TRIMESTER);
		aggregateObjectModel.addElement(Dimension.SEMESTER);
		aggregateObjectModel.addElement(Dimension.YEAR);
		
		ListCellRenderer<Dimension> renderer = new MapListRenderer<Dimension>(
				Ui.getMapDimensionsNames(), Ui.getMapDimensionsIcons());
		lstDimensions.setCellRenderer(renderer);
		lstDimensions.setModel(aggregateObjectModel);

		listPane = new ListPane(ListPane.Orientation.LEFT);
		listPane.setRenderer(renderer);
		listPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onAddDimension(evt);
			}
		});
		
		// restriction

		chkRestriction = new javax.swing.JCheckBox();
		chkRestriction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onCheckRestriction();
			}
		});
		
		cmbRestriction = new JComboBox<Class>();
		
		DefaultComboBoxModel<Class> restrictionObjectModel = new DefaultComboBoxModel<Class>();
		restrictionObjectModel.addElement(LocalNFirstRestriction.class);
		restrictionObjectModel.addElement(GlobalNFirstRestriction.class);

		cmbRestriction.setRenderer(new MapListRenderer<Class>(Ui.getMapRestrictionNames()));
		cmbRestriction.setModel(restrictionObjectModel);
		
		lblParameter = new JLabel(I18n.getMessage("restrictionPane.jlblParameter"));
		txtParameter = new JTextField();
		
		// default values

		onCheckRestriction();
	}

	private void initLayout() {
		
		// aggregation
		
		JPanel pnlAggregation = new JPanel();
		pnlAggregation.setLayout(new GridBagLayout());
		pnlAggregation.setBorder(BorderFactory.createTitledBorder(I18n.getMessage("aggregatorPane.title")));

		pnlAggregation.add(lblDimensions, new GBC(0, 0).setAnchor(GBC.WEST));
		pnlAggregation.add(chkSeparateInOut, new GBC(1, 0).setAnchor(GBC.WEST));
		pnlAggregation.add(scrolDimensions, new GBC(0, 1, GBC.BOTH));
		pnlAggregation.add(listPane, new GBC(1, 1, GBC.BOTH));

		// restriction
		
		// restriction check box
		
		JPanel pnlRestriction = new JPanel();
		pnlRestriction.setLayout(new GridBagLayout());
		pnlRestriction.setBorder(BorderFactory.createTitledBorder(I18n.getMessage("restrictionPane.title")));
		
		pnlRestriction.add(chkRestriction, new GBC(0, 0).setAnchor(GBC.WEST));

		// restriction combo box

		pnlRestriction.add(cmbRestriction, new GBC(0, 1, GBC.HORIZONTAL)
		.setAnchor(GBC.WEST));

		pnlRestriction.add(lblParameter, new GBC(1, 0, GBC.HORIZONTAL)
		.setAnchor(GBC.WEST)
		.setGridWidth(GBC.REMAINDER));

		pnlRestriction.add(txtParameter, new GBC(1, 1, GBC.HORIZONTAL)
		.setAnchor(GBC.WEST));

		// global
		
		setLayout(new GridBagLayout());
		
		add(pnlAggregation, new GBC(0, 1, GBC.BOTH));
		add(pnlRestriction, new GBC(0, 2, GBC.BOTH));
		add(Box.createGlue(), new GBC(0, 3, GBC.VERTICAL));
	}

	/*
	 * Event handler
	 */

	public void onAddDimension(ActionEvent evt) {

		if (evt.getSource() instanceof DefaultListModel) {

			DefaultListModel<Object> model = (DefaultListModel<Object>) evt.getSource();

			Object o = lstDimensions.getSelectedValue();
			if (o != null) {
				model.addElement(o);
			}
		}
	}
	
	private void onCheckRestriction() {

		boolean enabled = chkRestriction.isSelected();

		cmbRestriction.setEnabled(enabled);
		lblParameter.setEnabled(enabled);
		txtParameter.setEnabled(enabled);
	}

	/*
	 * Operations
	 */

	/**
	 * Initializes the pane from an instanciated aggregator.
	 * @param aggregator the aggregator
	 */
	public void init(IAggregator aggregator) {

		if (aggregator == null) {
			return;
		}

		chkSeparateInOut.setSelected(aggregator instanceof KindAggregator);

		List<Dimension> dimensions = aggregator.getDimensions();

		for (Dimension dim : dimensions) {
			listPane.getModel().addElement(dim);
		}
	}
	
	/**
	 * Initializes the pane from an instanciated aggregator.
	 * @param aggregator the aggregator
	 */
	public void init(IRestriction restriction) {

		if (restriction == null) {
			return;
		}
		
		chkRestriction.setEnabled(true);
		txtParameter.setText(restriction.getValue());
		
		onCheckRestriction();
	}

	/**
	 * Creates the aggregator from the selected combo boxes, check boxes, etc.
	 * @return an aggregator
	 */
	public IAggregator buildAggregator() {

		List<Dimension> dimensions = new ArrayList<Dimension>();

		for (int i = 0; i < listPane.getModel().getSize(); i++) {
			Dimension o = listPane.getModel().get(i);
			dimensions.add(o);
		}

		IAggregator aggregator = null;

		if (chkSeparateInOut.isSelected()) {
			aggregator = new KindAggregator(dimensions);
		}
		else {
			aggregator = new GlobalAggregator(dimensions);
		}

		return aggregator;
	}

	/**
	 * Check the validity of the form
	 */
	public void checkAggregator() {

		if (listPane.getModel().getSize() <= 0) {
			MessageTank.getInstance().error(I18n.getMessage("errors.noDimensions"));
		}
	}
	
	/**
	 * Build a restriction using the form
	 * @return
	 */
	public IRestriction buildRestriction() {

		IRestriction restriction = null;

		if (chkRestriction.isSelected()) {

			Object o = cmbRestriction.getModel().getSelectedItem();

			if (o != null) {

				String[] parameterNames = new String[] { RestrictionFactory.PARAM_RESTRICTION_N };
				String[] parameterValues = new String[] { txtParameter.getText() };

				restriction = RestrictionFactory.newInstance((Class<?>) o, parameterNames, parameterValues);
			}
		}

		return restriction;
	}

	/**
	 * Check the validity of the form
	 */
	public void checkRestriction() {

		if (chkRestriction.isSelected() && txtParameter.getText().isEmpty()) {
			MessageTank.getInstance().error("errors.noRestrictionParameter");
		}
	}
	
	public void check() {
		
		checkAggregator();
		checkRestriction();
	}
	
	public int getDimensionCount() {
		return listPane.getModel().getSize();
	}
}
