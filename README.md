jythonEasierCoercion
=====================

Use python libraries in Java WITHOUT need of python classes to implement Java interfaces (normal Jython method).

jythonEasierCoercion library allows you to create and/or convert Python objects of a library to Java objects and assign them to a Java reference variable of a type (interface) that you create. It uses Jython, and the difference from 'pure' Jython is that jythonEasierCoercion doesn't need that you modify Python library classes so they implement the Java interface. There are two 'modes' of use, Simple and AuxClass, described above.

### Simple mode

If all Python object data is accessible through methods (gets and sets; accessors and mutators) this is the mode. Create a Java interface with the methods you're going to use, and let jythonEasierCoercion do it's work.

### AuxClass mode

This mode is specific for using when some data in Python class is not provided through methods (in other words, the class provides public attributes and no gets or sets). 

## Usage

To use jythonEasierCoercion get a `JythonObjectManager` object by calling `JythonObjectManager.getInstance()` and use the methods below for creating and converting objects.

```java
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
public Object createObject(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName) throws JythonInstantiationException, JythonDynamicCoersionException
```

```java
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
public Object convertPyObject(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName) throws JythonDynamicCoersionException
```

```java
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
public Object createObjectUsingAuxClass(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName, String auxPythonClassPackage, String auxPythonClassName) throws JythonInstantiationException, JythonDynamicCoersionException
```

```java
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
public Object convertPyObjectUsingAuxClass(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName, String auxPythonClassPackage, String auxPythonClassName) throws JythonDynamicCoersionException
```

## Installation

1. Add jythonForceCoersion and Jython Standalone JAR files to your build path, and make sure they're exported with your application bundle (JAR, WAR, etc). 
2. The python library and auxiliary python classes (if using AuxClass mode) must be added to the Lib/ directory of the Jython Standalone JAR to be available during runtime.

## Status

Released

## License

The MIT License (MIT)

Copyright (c) 2014 Daniel Gimenes

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
