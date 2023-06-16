package com.rio.aki;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class SourceCreator {

	public IJavaProject mJavaProject;
	public IPackageFragmentRoot mRoot;
	public IPackageFragment mMainPackage;
	public ICompilationUnit mSelectedClass;
	
	public SourceCreator(ICompilationUnit selectedClass) {
		this.mSelectedClass = selectedClass;
	}
	
	public boolean init() {
		if (mSelectedClass == null) return false;
		
		mJavaProject = mSelectedClass.getJavaProject();
		mRoot = mJavaProject.getPackageFragmentRoot(mJavaProject.getProject().getFolder("src"));
		mMainPackage = CommonUtils.getMainPackage(mRoot);
		if (mMainPackage == null) return false;
		return true;
	}
	
	public void createRepository() {
		if (mMainPackage == null && !init()) return;
		
		try {
			String packageName = mMainPackage.getElementName() + ".repository";
			IPackageFragment repositoryPackage = CommonUtils.getIPackageFragment(mRoot, packageName);
			if (repositoryPackage == null) {
				repositoryPackage = mRoot.createPackageFragment(packageName, false, null);
			}
			
			String entityName = mSelectedClass.getElementName().replace(".java", "");
			String className = entityName + "Repository";
			if (CommonUtils.getClassFileInPackage(repositoryPackage, className + ".java") == null) {
				String source = "package " + packageName + ";\n\n" + 
								"import org.springframework.data.jpa.repository.JpaRepository;" + "\n" +
								"import org.springframework.stereotype.Repository;" + "\n\n" +
								"import " + mMainPackage.getElementName() + ".entity." + entityName + ";" + "\n\n" +
								"@Repository" + "\n" + 
								"public interface " + className + " extends JpaRepository<" + entityName + ", Long> {\n\n}";
				ICompilationUnit createdClass = repositoryPackage.createCompilationUnit(className + ".java", source, false, null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public void createController() {
		if (mMainPackage == null && !init()) return;
		
		try {
			String packageName = mMainPackage.getElementName() + ".controller";
			IPackageFragment repositoryPackage = CommonUtils.getIPackageFragment(mRoot, packageName);
			if (repositoryPackage == null) {
				repositoryPackage = mRoot.createPackageFragment(packageName, false, null);
			}
			
			String entityName = mSelectedClass.getElementName().replace(".java", "");
			String className = "MainController";
			ICompilationUnit controllerClass = CommonUtils.getClassFileInPackage(repositoryPackage, className + ".java");
			if (controllerClass == null) {
				String source = "package " + packageName + ";\n\n" +
								"import org.slf4j.Logger;\n" +
								"import org.slf4j.LoggerFactory;\n" +
								"import org.springframework.stereotype.Controller;\n" +
								"import org.springframework.ui.Model;\n" +
								"import org.springframework.web.bind.annotation.GetMapping;\n" +
								"import lombok.AllArgsConstructor;\n" +
								"import " + mMainPackage.getElementName() + ".repository." + entityName + "Repository" + ";\n\n" +
								"@Controller" + "\n" + 
								"@AllArgsConstructor" + "\n" + 
								"public class " + className + " {\n}";
				controllerClass = repositoryPackage.createCompilationUnit(className + ".java", source, false, null);
			}
			
			IType classModifier = CommonUtils.getIType(controllerClass);
			if (!CommonUtils.isExistField(classModifier, "mLogger")) {
				classModifier.createField("protected final static Logger mLogger = LoggerFactory.getLogger(" + className + ".class);", null, false, null);
			}
			String methodName = "get" + entityName;
			if (!CommonUtils.isExistMethod(classModifier, methodName)) {
				classModifier.createMethod("@GetMapping(\"/" + entityName.toLowerCase() + "\")\n" + 
						"public String " + methodName + "(Model model){\n" +
						"	return \"" + entityName.toLowerCase() + "\";\n" +
						"}", null, false, null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
}
