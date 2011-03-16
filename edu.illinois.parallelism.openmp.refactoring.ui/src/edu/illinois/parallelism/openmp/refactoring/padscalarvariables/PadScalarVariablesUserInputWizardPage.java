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
import org.eclipse.cdt.internal.ui.viewsupport.CElementImageProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
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

import edu.illinois.parallelism.openmp.refactoring.padscalarvariables.PadScalarVariablesRefactoring.VariableToPadTuple;

/**
 * 
 * @author nchen
 * 
 */
@SuppressWarnings("restriction")
public class PadScalarVariablesUserInputWizardPage extends UserInputWizardPage {

	class PadScalarVariableContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
			// Not applicable
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			// No children to return
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			// The inputElement must be of type List<VariableToPadTuple>
			if (inputElement instanceof List) {
				final List<?> list = (List<?>) inputElement;
				return list.toArray();
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			// All elements are stand alone
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return false;
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// Not applicable
		}

	}

	class PadScalarVariableLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			if (element != null) {
				return CElementImageProvider.getMethodImageDescriptor(ASTAccessVisibility.PUBLIC).createImage();
			}
			return null;
		}
	}

	private final PadScalarVariablesRefactoring padScalarRefactoring;
	private CheckboxTreeViewer variableSelectionView;

	private PadScalarVariableLabelProvider labelProvider;

	private PadScalarVariableContentProvider treeContentProvider;

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
		final Composite composite = new Composite(parent, SWT.NONE);

		setTitle(Messages.PadScalarVariablesUserInputWizardPage_dialogTitle);
		setMessage(Messages.PadScalarVariablesUserInputWizardPage_dialogMessage);

		composite.setLayout(new GridLayout(2, false));
		createTree(composite);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		variableSelectionView.getTree().setLayoutData(gridData);

		final Composite btComp = createButtonComposite(composite);
		gridData = new GridData();
		gridData.verticalAlignment = SWT.TOP;
		btComp.setLayoutData(gridData);

		final Button placeImplemetation = new Button(composite, SWT.CHECK);
		placeImplemetation.setText("Some bogus text");
		gridData = new GridData();
		placeImplemetation.setLayoutData(gridData);
		placeImplemetation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

			}

		});

		setControl(composite);
	}

	private void createTree(Composite composite) {
		variableSelectionView = new CheckboxTreeViewer(composite, SWT.BORDER);
		labelProvider = new PadScalarVariableLabelProvider();
		variableSelectionView.setLabelProvider(labelProvider);
		treeContentProvider = new PadScalarVariableContentProvider();
		variableSelectionView.setContentProvider(treeContentProvider);
		variableSelectionView.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				final VariableToPadTuple tuple = (VariableToPadTuple) event.getElement();
				tuple.setShouldPad(event.getChecked());
			}
		});

		// Forced the tree to start displaying something
		variableSelectionView.setInput(padScalarRefactoring.getVariablesToPad());
	}

}
