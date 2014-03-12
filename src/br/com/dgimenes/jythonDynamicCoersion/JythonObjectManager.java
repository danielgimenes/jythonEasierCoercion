/*
 * The MIT License (MIT)
 * Copyright (c) 2014 Daniel Gimenes
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package br.com.dgimenes.jythonDynamicCoersion;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import br.com.dgimenes.jythonDynamicCoersion.exception.JythonDynamicCoersionException;
import br.com.dgimenes.jythonDynamicCoersion.exception.JythonInstantiationException;

/**
 * Provides methods for creating and/or converting Python objects to Java
 * objects and assigning them to Java reference variables without the need of
 * changing the Python classes code.
 * 
 * This class uses the singleton pattern. It's object is provided through
 * getInstance() method.
 * 
 * @author danielgimenes
 * @version 1.0
 * 
 */
public class JythonObjectManager {
	private static JythonObjectManager instance;
	private PythonInterpreter interpreter;

	/**
	 * Retrives the unique instance of this class. Creates one if needed.
	 * 
	 * @return the unique instance of this class
	 */
	public static JythonObjectManager getInstance() {
		if (JythonObjectManager.instance == null) {
			JythonObjectManager.instance = new JythonObjectManager();
		}
		return JythonObjectManager.instance;
	}

	private JythonObjectManager() {
		this.interpreter = new PythonInterpreter();
	}

	/**
	 * Provides the Jython PythonInterpreter object created and used in this
	 * class.
	 * 
	 * @return jython's python interpreter
	 */
	public PythonInterpreter getPythonInterpreter() {
		return this.interpreter;
	}

	private void loadPythonModule(String pythonClassPackage, String pythonClassName) {
		this.interpreter.exec("from " + pythonClassPackage + " import " + pythonClassName);
	}

	/*
	 * Simple Mode Methods
	 * 
	 * If all Python object data is accessible through methods (gets and sets;
	 * accessors and mutators) this is the mode. Create a Java interface with
	 * the methods you're going to use, and let jythonForcedCoersion do it's
	 * work.
	 */

	/**
	 * Creates Python object and apply coersion to return an object of type
	 * specified in returnInterfaceType parameter.
	 * 
	 * @param returnInterfaceType
	 *            java type to coerse Python object to
	 * @param pythonClassPackage
	 *            package name of Python class
	 * @param pythonClassName
	 *            Python class name
	 * @return object that is of type specified in returnInterfaceType parameter
	 * @throws JythonInstantiationException
	 * @throws JythonDynamicCoersionException
	 */
	public Object createObject(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName) throws JythonInstantiationException,
			JythonDynamicCoersionException {
		// loads Python and Java classes' modules on Python Interpreter
		loadPythonModule(pythonClassPackage, pythonClassName);
		loadPythonModule(returnInterfaceType.getPackage().getName(), returnInterfaceType.getSimpleName());

		// creates dynamic binding class
		String pythonBindingClassName = returnInterfaceType.getSimpleName() + "Binding";
		this.interpreter.exec("class " + pythonBindingClassName + "(" + pythonClassName + "," + returnInterfaceType.getSimpleName() + "): pass");

		// creates reference and gets it
		String pythonReferenceVariableName = returnInterfaceType.getSimpleName() + "Instance";
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

	/**
	 * 
	 * Converts (coerse) Python object to an object of type specified in
	 * returnInterfaceType parameter.
	 * 
	 * @param objectToConvert
	 *            object reprenting Python Object to coerse from
	 * @param returnInterfaceType
	 *            java type to coerse Python object to
	 * @param pythonClassPackage
	 *            package name of Python class
	 * @param pythonClassName
	 *            Python class name
	 * @return object that is of type specified in returnInterfaceType parameter
	 * @throws JythonDynamicCoersionException
	 */
	public Object convertPyObject(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName)
			throws JythonDynamicCoersionException {
		// loads Python and Java classes' modules on Python Interpreter
		loadPythonModule(pythonClassPackage, pythonClassName);
		loadPythonModule(returnInterfaceType.getPackage().getName(), returnInterfaceType.getSimpleName());

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

	/*
	 * AuxClass Mode Methods
	 * 
	 * This mode is specific for using when some data in Python class is not
	 * provided through methods (in other works, the class provides public
	 * attributes and no gets or sets).
	 */

	/**
	 * Creates Python object and apply coersion to return an object of type
	 * specified in returnInterfaceType parameter. During the conversion, the
	 * Python class used for coersion can be specified (Auxiliary Class). That
	 * class must subclass the original object's Python class, and can be used
	 * for exposing attributes.
	 * 
	 * @param returnInterfaceType
	 *            java type to coerse Python object to
	 * @param pythonClassPackage
	 *            package name of Python class
	 * @param pythonClassName
	 *            Python class name
	 * @param auxPythonClassPackage
	 *            package name of auxiliary Python class
	 * @param auxPythonClassName
	 *            auxiliary Python class name
	 * @return object that is of type specified in returnInterfaceType parameter
	 * @throws JythonInstantiationException
	 * @throws JythonDynamicCoersionException
	 */
	public Object createObjectUsingAuxClass(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName, String auxPythonClassPackage,
			String auxPythonClassName) throws JythonInstantiationException, JythonDynamicCoersionException {
		// loads Python and Java classes' modules on Python Interpreter
		loadPythonModule(auxPythonClassPackage, auxPythonClassName);
		loadPythonModule(pythonClassPackage, pythonClassName);
		loadPythonModule(returnInterfaceType.getPackage().getName(), returnInterfaceType.getSimpleName());

		// creates reference and gets it
		String pythonReferenceVariableName = returnInterfaceType.getSimpleName() + "Instance";
		this.interpreter.exec(pythonReferenceVariableName + " = " + auxPythonClassName);
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

	/**
	 * Converts (coerse) Python object to an object of type specified in
	 * returnInterfaceType parameter. During the conversion, the Python class
	 * used for coersion can be specified (Auxiliary Class). That class must
	 * subclass the original object's Python class, and can be used for exposing
	 * attributes.
	 * 
	 * @param objectToConvert
	 *            object reprenting Python Object to coerse from
	 * @param returnInterfaceType
	 *            java type to coerse Python object to
	 * @param pythonClassPackage
	 *            package name of Python class
	 * @param pythonClassName
	 *            Python class name
	 * @param auxPythonClassPackage
	 *            package name of auxiliary Python class
	 * @param auxPythonClassName
	 *            auxiliary Python class name
	 * @return object that is of type specified in returnInterfaceType parameter
	 * @throws JythonDynamicCoersionException
	 */
	public Object convertPyObjectUsingAuxClass(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName,
			String auxPythonClassPackage, String auxPythonClassName) throws JythonDynamicCoersionException {
		// loads Python and Java classes' modules on Python Interpreter
		loadPythonModule(auxPythonClassPackage, auxPythonClassName);
		loadPythonModule(pythonClassPackage, pythonClassName);
		loadPythonModule(returnInterfaceType.getPackage().getName(), returnInterfaceType.getSimpleName());

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
