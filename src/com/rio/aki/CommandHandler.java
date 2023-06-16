package com.rio.aki;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class CommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {
		// ProjectExplorerで選択されたファイルを取得
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelection selection = window.getSelectionService().getSelection("org.eclipse.ui.navigator.ProjectExplorer");
		if(selection == null) return null;
		
		ICompilationUnit selectedClass = (ICompilationUnit) ((IStructuredSelection)selection).getFirstElement();
		if (selectedClass == null) return null;
		
		TopWidget widget = new TopWidget(selectedClass);
		widget.show();
		
		return null;
	}
	
}
