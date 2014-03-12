jythonForcedCoersion
=====================

Use python libraries in Java WITHOUT need of python classes to implement Java interfaces (normal Jython method).

Status: in development

How to use: call JythonObjectManager.getInstance() to get the JythonObjectManager object and then execute one of the following commands for creating and converting objects.

```java
	Object createObject(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName) throws JythonInstantiationException, JythonDynamicCoersionException

```java
	Object createObjectUsingAuxClass(Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName, String auxPythonClassPackage,	String auxPythonClassName) throws JythonInstantiationException, JythonDynamicCoersionException

```java
	Object convertPyObject(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName) throws JythonDynamicCoersionException

```java
	Object convertPyObjectUsingAuxPythonClass(PyObject objectToConvert, Class<?> returnInterfaceType, String pythonClassPackage, String pythonClassName, String auxPythonClassPackage, String auxPythonClassName) throws JythonDynamicCoersionException
