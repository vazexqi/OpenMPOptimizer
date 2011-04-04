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
package edu.illinois.parallelism.openmp.refactoring.padscalarvariables.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.eclipse.cdt.ui.tests.refactoring.RefactoringTest;
import org.eclipse.cdt.ui.tests.refactoring.TestSourceFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import edu.illinois.parallelism.openmp.refactoring.padscalarvariables.PadScalarVariablesRefactoring;
import edu.illinois.parallelism.openmp.refactoring.padscalarvariables.VariableToPadTuple;

/**
 * 
 * @author nchen
 * 
 */
@SuppressWarnings("restriction")
public class PadScalarVariablesRefactoringTest extends RefactoringTest {

	private String configuredVariablesToPad;

	public PadScalarVariablesRefactoringTest(String name, Collection<TestSourceFile> files) {
		super(name, files);
	}

	@Override
	protected void configureRefactoring(Properties refactoringProperties) {
		configuredVariablesToPad = refactoringProperties.getProperty("variables", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void runTest() throws Throwable {
		IFile refFile = project.getFile(fileName);
		PadScalarVariablesRefactoring refactoring = new PadScalarVariablesRefactoring(refFile, selection, null, cproject);
		refactoring.isWithinProperOMPRegion();

		RefactoringStatus checkInitialConditions = refactoring.checkInitialConditions(NULL_PROGRESS_MONITOR);
		assertConditionsOk(checkInitialConditions);

		List<VariableToPadTuple> variables = refactoring.getVariablesToPad();
		selectVariablesToPad(variables, configuredVariablesToPad);

		Change createChange = refactoring.createChange(NULL_PROGRESS_MONITOR);
		RefactoringStatus finalConditions = refactoring.checkFinalConditions(NULL_PROGRESS_MONITOR);
		assertConditionsOk(finalConditions);

		createChange.perform(NULL_PROGRESS_MONITOR);
		compareFiles(fileMap);

	}

	private void selectVariablesToPad(List<VariableToPadTuple> variablesToPad, String configuredVariablesToPad) {
		String variableSeparatorRegex = ",";
		List<String> list = Arrays.asList(configuredVariablesToPad.split(variableSeparatorRegex));

		for (VariableToPadTuple variable : variablesToPad) {
			if (list.contains(variable.getName().toString())) {
				variable.setShouldPad(true);
			}
		}

	}
}
