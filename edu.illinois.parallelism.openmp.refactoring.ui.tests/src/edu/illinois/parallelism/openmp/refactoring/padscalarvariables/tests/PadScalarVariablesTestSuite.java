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

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.illinois.parallelism.openmp.refactoring.ui.tests.OpenMPRefactoringTester;

/**
 * 
 * @author nchen
 * 
 */
public class PadScalarVariablesTestSuite extends TestSuite {
	public static Test suite() throws Exception {
		TestSuite suite = new PadScalarVariablesTestSuite();
		suite.addTest(OpenMPRefactoringTester.suite("Pad Scalar Variables In Function Scope",
				"tests-descriptors/padscalarvariables/PadScalarVariablesInFunctionScope.rts"));
		return suite;
	}
}
