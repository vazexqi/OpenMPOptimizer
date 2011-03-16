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
package edu.illinois.parallelism.handlers;

import org.eclipse.cdt.core.model.IWorkingCopy;
import org.eclipse.cdt.internal.ui.refactoring.utils.EclipseObjects;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.cdt.ui.refactoring.actions.RefactoringAction;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.illinois.parallelism.openmp.refactoring.padscalarvariables.PadScalarVariablesRunner;

/**
 * This class is invoked when the Pad Scalar Variable Refactoring actions is
 * invoked from the menu. This mirrors {@link RefactoringAction} except that we
 * are not an {@link Action} but an {@link AbstractHandler}
 * 
 * @author nchen
 * 
 */
@SuppressWarnings("restriction")
public class PadScalarVariablesHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		saveAllEditors();
		runRefactoringIfPossible(event);

		return null; // done
	}

	private void runRefactoringIfPossible(ExecutionEvent event) {
		// We have a <visibleWhen> check in the plugin.xml that there is an
		// editor and that it is a CEditor so we are sure that the editor is
		// present. The following statements collect the necessary information
		// to pass to a RefactoringRunner.
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		IShellProvider shellProvider = editorPart.getEditorSite();
		ITextEditor editor = (ITextEditor) editorPart;
		IWorkingCopy workingCopy = CUIPlugin.getDefault().getWorkingCopyManager().getWorkingCopy(editor.getEditorInput());
		ISelection selection = editor.getSelectionProvider().getSelection();

		runRefactoring(shellProvider, workingCopy, selection);
	}

	private void runRefactoring(IShellProvider shellProvider, IWorkingCopy workingCopy, ISelection selection) {
		IResource resource = workingCopy.getResource();
		if (resource instanceof IFile) {
			PadScalarVariablesRunner runner = new PadScalarVariablesRunner((IFile) resource, selection, null, shellProvider, workingCopy.getCProject());
			runner.run();
		}

	}

	private void saveAllEditors() {
		EclipseObjects.getActivePage().saveAllEditors(true);
	}

}
