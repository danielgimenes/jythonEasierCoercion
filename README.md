jythonForcedCoersion
=====================

Use python libraries in Java WITHOUT need of python classes to implement Java interfaces (normal Jython method).

## Status

in development (not production ready)

## Usage

To use jythonForceCoersion get a `JythonObjectManager` object by calling `JythonObjectManager.getInstance()` and use the methods below for creating and converting objects.

```java
	Object createObject(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName) throws JythonInstantiationException, JythonDynamicCoersionException
```

```java
	Object createObjectUsingAuxClass(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName, String auxPythonClassPackage,	String auxPythonClassName) throws JythonInstantiationException, JythonDynamicCoersionException
```

```java
	Object convertPyObject(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName) throws JythonDynamicCoersionException
```

```java
	Object convertPyObjectUsingAuxPythonClass(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName, String auxPythonClassPackage, String auxPythonClassName) throws JythonDynamicCoersionException
```

## Installation

Add the jythonForceCoersion JAR file to your build path, and make sure it's exported with your application bundle (JAR, WAR, etc).

## How it works

// TO DO

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
