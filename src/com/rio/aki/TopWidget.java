package com.rio.aki;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;

public class TopWidget {
	
	public ICompilationUnit mSelectedClass;
	
	public TopWidget(ICompilationUnit selectedClass) {
		this.mSelectedClass = selectedClass;
	}

	public void show() {
		Display display = Display.getDefault();
		
		final Shell shell = new Shell(display);
		shell.setLayoutData(new RowData(800, 400));
		shell.setText("SpringBoot Utils");
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		TabItem tab1 = new TabItem(tabFolder, SWT.NONE);
		tab1.setText("Tab1");
		
		Group group = new Group(tabFolder, SWT.NONE);
		group.setLayoutData(new RowData(400, 100));
		group.setText("Create Source");
		group.setLayout(new RowLayout(SWT.VERTICAL));
		tab1.setControl(group);
		
		final Button cbRepository = new Button(group, SWT.CHECK);
		final Button cbController = new Button(group, SWT.CHECK);
		final Button cbDto = new Button(group, SWT.CHECK);
		cbRepository.setText("Add Repository");
		cbController.setText("Add Controller");
		cbDto.setText("Add Dto");
		cbRepository.setSelection(true);
		cbController.setSelection(true);
		cbDto.setSelection(true);
		
		Button submit = new Button(shell, SWT.PUSH);
		submit.setLayoutData(new RowData(200, 60));
		submit.setText("run");
		submit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				SourceCreator sourceCreator = new SourceCreator(mSelectedClass);
				if (cbRepository.getSelection()) sourceCreator.createRepository();
				if (cbController.getSelection()) sourceCreator.createController();
				//if (cbDto.getSelection()) createDto();
			}
		});
		
		shell.pack();
		shell.open();
	}
	
	public void showMessageBox(Shell shell, String msg) {
		MessageBox messageBox = new MessageBox(shell);
		messageBox.setMessage(msg);
		messageBox.open();
	}
	
}
