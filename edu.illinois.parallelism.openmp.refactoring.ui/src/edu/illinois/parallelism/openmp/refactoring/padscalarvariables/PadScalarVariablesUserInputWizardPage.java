/*******************************************************************************
 * Copyright (c) 2011 University of Illinois at Urbana-Champaign
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: 
 * University of Illinois at Urbana-Champaign - initial API and implementation
 *******************************************************************************/
package edu.illinois.parallelism.openmp.refactoring.padscalarvariables;

import java.util.List;

import org.eclipse.cdt.core.parser.ast.ASTAccessVisibility;
import org.eclipse.cdt.internal.ui.util.TableLayoutComposite;
import org.eclipse.cdt.internal.ui.viewsupport.CElementImageProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import edu.illinois.parallelism.openmp.refactoring.padscalarvariables.PadScalarVariablesRefactoring.VariableToPadTuple;

/**
 * Creates a simple input page with tables to allow the user to select which
 * variables to pad and to change their padding size.
 * 
 * @author nchen
 * 
 */
public class PadScalarVariablesUserInputWizardPage extends UserInputWizardPage {

	private static final int DIALOG_HEIGHT = 200;
	private static final int DIALOG_WIDTH = 300;
	private final PadScalarVariablesRefactoring padScalarRefactoring;
	private CheckboxTableViewer variableSelectionView;
	private Table variableSelectionTable;
	private PadScalarVariableContentProvider tableContentProvider;

	public PadScalarVariablesUserInputWizardPage(PadScalarVariablesRefactoring padScalarRefactoring) {
		super(Messages.PadScalarVariablesUserInputWizardPage_wizardPageTitle);
		this.padScalarRefactoring = padScalarRefactoring;
	}

	private Composite createButtonComposite(Composite comp) {
		final Composite btComp = new Composite(comp, SWT.NONE);
		final FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.spacing = 4;
		btComp.setLayout(layout);

		final Button selectAll = new Button(btComp, SWT.PUSH);
		selectAll.setText(Messages.PadScalarVariablesUserInputWizardPage_selectAllButtonLabel);
		selectAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (final VariableToPadTuple variable : padScalarRefactoring.getVariablesToPad()) {
					variableSelectionView.setChecked(variable, true);
				}
			}

		});

		final Button deselectAll = new Button(btComp, SWT.PUSH);
		deselectAll.setText(Messages.PadScalarVariablesUserInputWizardPage_deselectAllButtonLabel);
		deselectAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (final VariableToPadTuple variable : padScalarRefactoring.getVariablesToPad()) {
					variableSelectionView.setChecked(variable, false);
				}
			}

		});
		return btComp;
	}

	@Override
	public void createControl(Composite parent) {
		// Make the dialog smaller
		parent.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		final Composite composite = new Composite(parent, SWT.NONE);

		setTitle(Messages.PadScalarVariablesUserInputWizardPage_dialogTitle);
		setMessage(Messages.PadScalarVariablesUserInputWizardPage_dialogMessage);

		composite.setLayout(new GridLayout(2, false));
		createTable(composite);

		final Composite btComp = createButtonComposite(composite);
		GridData gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		btComp.setLayoutData(gridData);

		setControl(composite);
	}

	private void createTable(Composite composite) {
		variableSelectionTable = new Table(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION
				| SWT.CHECK);
		variableSelectionTable.setLinesVisible(true);
		variableSelectionTable.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		variableSelectionTable.setLayoutData(data);

		String[] tableHeaders = { "Variable", "Type", "Padding (bytes)" };

		for (String tableHeader : tableHeaders) {
			TableColumn column = new TableColumn(variableSelectionTable, SWT.NONE);
			column.setText(tableHeader);
			column.setAlignment(SWT.RIGHT);
		}

		variableSelectionView = new CheckboxTableViewer(variableSelectionTable);
		tableContentProvider = new PadScalarVariableContentProvider();
		variableSelectionView.setContentProvider(tableContentProvider);
		variableSelectionView.setLabelProvider(new PadScalarVariableLabelProvider());
		variableSelectionView.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				VariableToPadTuple tuple = (VariableToPadTuple) event.getElement();
				tuple.setShouldPad(event.getChecked());
			}
		});

		variableSelectionView.setInput(padScalarRefactoring.getVariablesToPad());
		for (int i = 0; i < tableHeaders.length; i++) {
			variableSelectionTable.getColumn(i).pack();
		}
		
		//TODO: Allow the user to edit the table cells
	}

	private class PadScalarVariableContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			// The inputElement must be of type List<VariableToPadTuple>
			if (inputElement instanceof List) {
				final List<?> list = (List<?>) inputElement;
				return list.toArray();
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// Do nothing since the viewer listens to resource deltas
		}

		public void dispose() {
		}

	}

	private class PadScalarVariableLabelProvider implements ITableLabelProvider {

		private static final int VARIABLE_NAME_COL = 0;
		private static final int VARIABLE_TYPE_COL = 1;
		private static final int VARIABLE_BYTES_COL = 2;

		@SuppressWarnings("restriction")
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == VARIABLE_NAME_COL) {
				if (element != null) {
					return CElementImageProvider.getMethodImageDescriptor(ASTAccessVisibility.PUBLIC).createImage();
				}
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			VariableToPadTuple tuple = (VariableToPadTuple) element;
			switch (columnIndex) {
			case VARIABLE_NAME_COL:
				return tuple.getName().toString();
			case VARIABLE_TYPE_COL:
				return "int";
			case VARIABLE_BYTES_COL:
				return new Integer(tuple.getBytesToPad()).toString();

			default:
				return null;
			}
		}

		// The following methods are not applicable

		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

	}
	// TODO: Disable the NEXT button if nothing is selected
}
