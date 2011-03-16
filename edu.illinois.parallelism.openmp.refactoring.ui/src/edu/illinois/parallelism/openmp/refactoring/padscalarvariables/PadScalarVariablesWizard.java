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

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

/**
 * 
 * @author nchen
 * 
 */
public class PadScalarVariablesWizard extends RefactoringWizard {

	private final PadScalarVariablesRefactoring padScalarVariablesRefactoring;

	public PadScalarVariablesWizard(PadScalarVariablesRefactoring refactoring) {
		super(refactoring, WIZARD_BASED_USER_INTERFACE);
		padScalarVariablesRefactoring = refactoring;
	}

	@Override
	protected void addUserInputPages() {
		addPage(new PadScalarVariablesUserInputWizardPage(padScalarVariablesRefactoring));
	}

}
