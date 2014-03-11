package br.com.dgimenes.jythonDynamicCoersion;

import java.util.Calendar;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import br.com.dgimenes.jythonDynamicCoersion.exception.JythonDynamicCoersionException;
import br.com.dgimenes.jythonDynamicCoersion.exception.JythonInstantiationException;

public class JythonObjectManager {

	private static JythonObjectManager instance;
	private PythonInterpreter interpreter;

	public static JythonObjectManager getInstance() {
		if (JythonObjectManager.instance == null) {
			JythonObjectManager.instance = new JythonObjectManager();
		}
		return JythonObjectManager.instance;
	}

	private JythonObjectManager() {
		long initial = Calendar.getInstance().getTime().getTime();
		this.interpreter = new PythonInterpreter();
		System.out.print(Calendar.getInstance().getTime().getTime() - initial);
		System.out.println(" miliseconds");
	}

	public PythonInterpreter getPythonInterpreter() {
		return this.interpreter;
	}

	public Object createObject(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName) throws JythonInstantiationException,
			JythonDynamicCoersionException {
		// loads Python and Java classes' modules on Python Interpreter
		this.interpreter.exec("from " + pythonClassPackage + " import " + pythonClassName);
		this.interpreter.exec("from " + returnInterfaceType.getPackage().getName() + " import " + returnInterfaceType.getSimpleName());

		// creates dynamic binding class
		String pythonReferenceVariableName = returnInterfaceType.getSimpleName() + "Instance";
		String pythonBindingClassName = returnInterfaceType.getSimpleName() + "Binding";
		this.interpreter.exec("class " + pythonBindingClassName + "(" + pythonClassName + "," + returnInterfaceType.getSimpleName() + "): pass");

		// creates reference and gets it
		this.interpreter.exec(pythonReferenceVariableName + " = " + pythonBindingClassName);
		PyObject pythonReference = this.interpreter.get(pythonReferenceVariableName);
		if (pythonReference == null) {
			throw new JythonInstantiationException();
		}

		// creates object and makes coersion
		PyObject pythonObject = pythonReference.__call__();
		Object convertedObject = pythonObject.__tojava__(returnInterfaceType);
		if (returnInterfaceType.isAssignableFrom(convertedObject.getClass())) {
			return convertedObject;
		} else {
			throw new JythonDynamicCoersionException();
		}
	}

	public Object createObjectUsingAuxClass(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName, String auxPythonClassPackage,
			String auxPythonClassName) throws JythonInstantiationException, JythonDynamicCoersionException {
		return null;
	}

	public Object convertPyObject(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName)
			throws JythonDynamicCoersionException {
		// loads Python and Java classes' modules on Python Interpreter
		this.interpreter.exec("from " + pythonClassPackage + " import " + pythonClassName);
		this.interpreter.exec("from " + returnInterfaceType.getPackage().getName() + " import " + returnInterfaceType.getSimpleName());

		// creates dynamic binding class
		String pythonBindingClassName = returnInterfaceType.getSimpleName() + "Binding";
		this.interpreter.exec("class " + pythonBindingClassName + "(" + pythonClassName + "," + returnInterfaceType.getSimpleName() + "): pass");

		// sets object to reference, modify it's class and coerse
		String pythonReferenceVariableName = returnInterfaceType.getSimpleName() + "Instance";
		this.interpreter.set(pythonReferenceVariableName, objectToConvert);
		this.interpreter.exec(pythonReferenceVariableName + ".__class__ = " + pythonBindingClassName);
		Object convertedObject = interpreter.get(pythonReferenceVariableName).__tojava__(returnInterfaceType);
		if (returnInterfaceType.isAssignableFrom(convertedObject.getClass())) {
			return convertedObject;
		} else {
			throw new JythonDynamicCoersionException();
		}
	}

	public Object convertPyObjectUsingAuxPythonClass(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName,
			String auxPythonClassPackage, String auxPythonClassName) throws JythonDynamicCoersionException {
		// loads Python and Java classes' modules on Python Interpreter
		this.interpreter.exec("from " + auxPythonClassPackage + " import " + auxPythonClassName);
		this.interpreter.exec("from " + pythonClassPackage + " import " + pythonClassName);
		this.interpreter.exec("from " + returnInterfaceType.getPackage().getName() + " import " + returnInterfaceType.getSimpleName());

		// sets object to reference, modify it's class and coerse
		String pythonReferenceVariableName = returnInterfaceType.getSimpleName() + "Instance";
		this.interpreter.set(pythonReferenceVariableName, objectToConvert);
		this.interpreter.exec(pythonReferenceVariableName + ".__class__ = " + auxPythonClassName);
		Object convertedObject = interpreter.get(pythonReferenceVariableName).__tojava__(returnInterfaceType);
		if (returnInterfaceType.isAssignableFrom(convertedObject.getClass())) {
			return convertedObject;
		} else {
			throw new JythonDynamicCoersionException();
		}
	}
}
