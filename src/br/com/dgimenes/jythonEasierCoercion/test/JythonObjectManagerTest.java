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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package br.com.dgimenes.jythonEasierCoercion.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import br.com.dgimenes.jythonEasierCoercion.JythonObjectManager;
import br.com.dgimenes.jythonEasierCoercion.exception.JythonDynamicCoersionException;
import br.com.dgimenes.jythonEasierCoercion.exception.JythonInstantiationException;
import br.com.dgimenes.jythonEasierCoercion.test.resource.SomeAuxPythonClassJavaInterface;
import br.com.dgimenes.jythonEasierCoercion.test.resource.SomePythonClassJavaInterface;

@RunWith(JUnit4.class)
public class JythonObjectManagerTest {
	private JythonObjectManager objectManager;

	@Before
	public void setupBeforeAllTests() {
		this.objectManager = JythonObjectManager.getInstance();
	}

	private PyObject createPyObject() {
		PythonInterpreter interpreter = objectManager.getPythonInterpreter();
		interpreter.exec("from tests.testclass import SomePythonClass");
		interpreter.exec("pyObj = SomePythonClass()");
		PyObject pyObject = interpreter.get("pyObj");
		return pyObject;
	}

	// Simple Mode

	@Test
	public void testObjectCriation() throws JythonInstantiationException, JythonDynamicCoersionException {
		SomePythonClassJavaInterface object = (SomePythonClassJavaInterface) objectManager.createObject(SomePythonClassJavaInterface.class, "tests.testclass",
				"SomePythonClass");
		assertNotNull(object);
		assertTrue(object.process());
	}

	@Test
	public void testObjectConversion() throws JythonDynamicCoersionException {
		PyObject pyObject = createPyObject();
		SomePythonClassJavaInterface object = (SomePythonClassJavaInterface) objectManager.convertPyObject(pyObject, SomePythonClassJavaInterface.class,
				"tests.testclass", "SomePythonClass");
		assertNotNull(object);
		assertTrue(object.process());
	}

	// AuxClass Mode

	@Test
	public void testObjectCriationWithAuxClass() throws JythonInstantiationException, JythonDynamicCoersionException {
		SomeAuxPythonClassJavaInterface object = (SomeAuxPythonClassJavaInterface) objectManager.createObjectUsingAuxClass(
				SomeAuxPythonClassJavaInterface.class, "tests.testclass", "SomePythonClass", "tests.auxclass", "SomeAuxPythonClass");
		assertNotNull(object);
		assertTrue(object.process());
		assertTrue(object.resultAttribute() == 10);
	}

	@Test
	public void testObjectConversionWithAuxClass() throws JythonDynamicCoersionException {
		PyObject pyObject = createPyObject();
		SomeAuxPythonClassJavaInterface object = (SomeAuxPythonClassJavaInterface) objectManager.convertPyObjectUsingAuxClass(pyObject,
				SomeAuxPythonClassJavaInterface.class, "tests.testclass", "SomePythonClass", "tests.auxclass", "SomeAuxPythonClass");
		assertNotNull(object);
		assertTrue(object.process());
		assertTrue(object.resultAttribute() == 10);
	}
}
