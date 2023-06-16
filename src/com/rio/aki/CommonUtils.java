package com.rio.aki;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;

public class CommonUtils {

	public static boolean isExistMethod(IType type, String name) {
		if (type == null || name == null || "".equals(name)) return false;
		
		try {
			for (IJavaElement javaElement : type.getChildren()) {
				if (javaElement.getElementType() != IJavaElement.METHOD) continue;
				
				if (name.equals(javaElement.getElementName())) return true;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean isExistField(IType type, String name) {
		if (type == null || name == null || "".equals(name)) return false;
		
		try {
			for (IJavaElement javaElement : type.getChildren()) {
				if (javaElement.getElementType() != IJavaElement.FIELD) continue;
				
				if (name.equals(javaElement.getElementName())) return true;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static IType getIType(ICompilationUnit compUnit) {
		try {
			for (IJavaElement tempType : compUnit.getChildren()) {
				if (tempType.getElementType() != IJavaElement.TYPE) continue;
				
				return (IType) tempType;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ICompilationUnit getClassFileInPackage(IPackageFragment packageFragment, String fileName) {
		if (packageFragment == null || fileName == null || "".equals(fileName)) return null;
		
		try {
			for (IJavaElement childElement : packageFragment.getChildren()) {
				if (!(childElement instanceof ICompilationUnit)) continue;
				
				if (fileName.equals(childElement.getElementName())) return (ICompilationUnit) childElement;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static IPackageFragment getIPackageFragment(IPackageFragmentRoot root, String packageName) {
		if (root == null || packageName == null || "".equals(packageName)) return null;
			
		try {
			for (IJavaElement tempPackageFragment : root.getChildren()) {
				IPackageFragment packageFragment = (IPackageFragment) tempPackageFragment;
				
				if (packageName.equals(packageFragment.getElementName())) return packageFragment;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static IPackageFragment getMainPackage(IPackageFragmentRoot root) {
		try {
			for (IJavaElement tempPackageFragment : root.getChildren()) {
				IPackageFragment packageFragment = (IPackageFragment) tempPackageFragment;
				
				for (IJavaElement classElement : packageFragment.getChildren()) {
					if (!(classElement instanceof ICompilationUnit)) continue;
					
					for (IJavaElement tempType : ((ICompilationUnit) classElement).getChildren()) {
						if (tempType.getElementType() != IJavaElement.TYPE) continue;
						
						for (IJavaElement javaElement : ((IType) tempType).getChildren()) {
							if (javaElement.getElementType() != IJavaElement.METHOD) continue;
							if (!"main".equals(javaElement.getElementName())) continue;
							
							return packageFragment;
						}
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
